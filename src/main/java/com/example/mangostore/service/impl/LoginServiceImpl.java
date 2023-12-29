package com.example.mangostore.service.impl;

import com.example.mangostore.entity.Account;
import com.example.mangostore.entity.Role;
import com.example.mangostore.repository.AccountRepository;
import com.example.mangostore.repository.RoleRepository;
import com.example.mangostore.service.LoginService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.IOException;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Service
public class LoginServiceImpl implements LoginService {
    private final AccountRepository accountRepository;
    private final PasswordEncoder encoder;
    private final RoleRepository roleRepository;
    private final JavaMailSender mailSender;

    public LoginServiceImpl(AccountRepository accountRepository,
                            PasswordEncoder encoder,
                            RoleRepository roleRepository,
                            JavaMailSender mailSender) {
        this.accountRepository = accountRepository;
        this.encoder = encoder;
        this.roleRepository = roleRepository;
        this.mailSender = mailSender;
    }

    @Override
    public String loginAccount(String email, String password, HttpSession session) throws IOException {
        System.out.println("aaaaaaaaaaaaaaaaa " + email);
        System.out.println("bbbbbbbbbbbbbbbbb " + password);
        System.out.println("ccccccccccccccccc " + encoder.encode(password));
        Account detailAccount = accountRepository.detailAccountByEmail(email);
        if (detailAccount == null || !encoder.matches(password, detailAccount.getEncryptionPassword())) {
            return "redirect:/mangostore/login/from";
        } else {
            session.setAttribute("loginEmail", email);
            return "redirect:/mangostore/home";
        }
    }

    @Override
    public void checkLoginGoogleAccount(HttpServletResponse response, Authentication authentication) throws IOException {
        DefaultOidcUser oauthUser = (DefaultOidcUser) authentication.getPrincipal();
        System.out.println("aaaaaaaaaaaaaaa " + oauthUser.getEmail());
        System.out.println("ccccccccccccccc " + oauthUser.getFullName());
        String email = oauthUser.getEmail();
        String fullName = oauthUser.getFullName();

        Account existUser = accountRepository.detailAccountByEmail(email);
        if (existUser == null) {
            Account newAccount = new Account();
            newAccount.setEmail(email);
            newAccount.setFullName(fullName);
            newAccount.setStatus(1);

            Role roleUser = roleRepository.getAllRoleByUser();
            Set<Role> rolesUser = new HashSet<>();
            rolesUser.add(roleUser);
            newAccount.setRoles(rolesUser);

            accountRepository.save(newAccount);
            existUser = newAccount;
        }
        HttpSession session = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession();
        session.setAttribute("loginEmail", existUser.getEmail());
        response.sendRedirect("/mangostore/login/password/refresh");
    }

    public String generateVerificationCode() {
        int code = (int) ((Math.random() * 900000) + 100000);
        return String.valueOf(code);
    }

    public void saveVerificationCode(String email, String verificationCode) {
        Account account = accountRepository.detailAccountByEmail(email);
        if (account != null) {
            account.setVeryCode(verificationCode);
            accountRepository.save(account);
        }
    }

    public void sendEmail(String to, String subject, String verificationCode) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        String content = "Code để bạn đặt lại mật khẩu là: " + verificationCode;
        message.setText(content);
        mailSender.send(message);
    }

    @Override
    public String forgotEmail(String email) {
        Account detailAccount = accountRepository.detailAccountByEmail(email);
        if (detailAccount == null) {
            return "redirect:/mangostore/login/forgot";
        } else {
            String verificationCode = generateVerificationCode();
            saveVerificationCode(email, verificationCode);
            sendEmail(email, "Đặt lại mật khẩu", verificationCode);
            return "redirect:/mangostore/login/forgot";
        }
    }

    @Override
    public String authenticationCode(String codeForgot, HttpSession session) {
        String email = (String) session.getAttribute("forgotEmail");
        if (email == null) {
            return "redirect:/mangostore/login/forgot";
        } else {
            Account detailAccount = accountRepository.detailAccountByEmail(email);
            if (Objects.equals(codeForgot, detailAccount.getVeryCode())) {
                return "redirect:/mangostore/login/password/refresh";
            } else {
                return "redirect:/mangostore/login/forgot";
            }
        }
    }

    @Override
    public String refreshPassword(String email, String passwordRefresh) {
        Account detailAccount = accountRepository.detailAccountByEmail(email);
        detailAccount.setEncryptionPassword(encoder.encode(passwordRefresh));
        accountRepository.save(detailAccount);
        return "redirect:/mangostore/home";
    }
}
