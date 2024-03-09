package com.example.mangostore.service.impl;

import com.example.mangostore.config.Gender;
import com.example.mangostore.entity.Account;
import com.example.mangostore.entity.Rank;
import com.example.mangostore.entity.Role;
import com.example.mangostore.repository.AccountRepository;
import com.example.mangostore.repository.AuthenticationRepository;
import com.example.mangostore.repository.RankRepository;
import com.example.mangostore.repository.RoleRepository;
import com.example.mangostore.service.LoginService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.IOException;
import java.util.Objects;

@Service
public class LoginServiceImpl implements LoginService {
    private final AccountRepository accountRepository;
    private final PasswordEncoder encoder;
    private final RoleRepository roleRepository;
    private final AuthenticationRepository authenticationRepository;
    private final RankRepository rankRepository;
    private final Gender gender;

    public LoginServiceImpl(AccountRepository accountRepository,
                            PasswordEncoder encoder,
                            RoleRepository roleRepository,
                            AuthenticationRepository authenticationRepository,
                            RankRepository rankRepository,
                            Gender gender) {
        this.accountRepository = accountRepository;
        this.encoder = encoder;
        this.roleRepository = roleRepository;
        this.authenticationRepository = authenticationRepository;
        this.rankRepository = rankRepository;
        this.gender = gender;
    }

    @Override
    public String loginAccount(String email, String password, HttpSession session) throws IOException {
        Account detailAccount = accountRepository.detailAccountByEmail(email);
        if (detailAccount == null || !encoder.matches(password, detailAccount.getEncryptionPassword())) {
            return "redirect:/mangostore/login/from";
        } else {
            if (detailAccount.getStatus() == 0) {
                return "redirect:/mangostore/login/from";
            } else {
                session.setAttribute("loginEmail", email);
                return "redirect:/mangostore/home";
            }
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
            newAccount.setAccumulatedPoints(0);

            Rank detailRank = rankRepository.detailRankByAccumulatedPoints(0);
            newAccount.setRank(detailRank);

            newAccount.setStatus(1);
            accountRepository.save(newAccount);

            Role roleUser = roleRepository.getAllRoleByUser();
            com.example.mangostore.entity.Authentication newAuthentication = new com.example.mangostore.entity.Authentication();
            newAuthentication.setAccount(newAccount);
            newAuthentication.setRole(roleUser);
            authenticationRepository.save(newAuthentication);

            existUser = newAccount;

            HttpSession session = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession();
            session.setAttribute("loginEmailForgot", existUser.getEmail());
            if (existUser.getEncryptionPassword() == null) {
                response.sendRedirect("/mangostore/login/password/refresh");
            } else {
                session.setAttribute("loginEmail", existUser.getEmail());
                session.removeAttribute("loginEmailForgot");
                response.sendRedirect("/mangostore/home");
            }
        } else {
            if (existUser.getStatus() == 0) {
                response.sendRedirect("/mangostore/login/from");
            } else {
                HttpSession session = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession();
                session.setAttribute("loginEmailForgot", existUser.getEmail());
                if (existUser.getEncryptionPassword() == null) {
                    response.sendRedirect("/mangostore/login/password/refresh");
                } else {
                    session.setAttribute("loginEmail", existUser.getEmail());
                    session.removeAttribute("loginEmailForgot");
                    response.sendRedirect("/mangostore/home");
                }
            }
        }
    }

    @Override
    public String forgotEmail(String email) {
        Account detailAccount = accountRepository.detailAccountByEmail(email);
        if (detailAccount == null) {
            return "redirect:/mangostore/login/forgot";
        } else {
            String verificationCode = gender.generateVerificationCode();
            gender.saveVerificationCode(email, verificationCode);
            gender.sendEmail(email, "Đặt lại mật khẩu", verificationCode);
            return "redirect:/mangostore/login/forgot";
        }
    }

    @Override
    public String authenticationCode(String codeForgot, HttpSession session) {
        String email = (String) session.getAttribute("loginEmailForgot");
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
    public String refreshPassword(String email, String passwordRefresh, HttpSession session) {
        Account detailAccount = accountRepository.detailAccountByEmail(email);
        detailAccount.setEncryptionPassword(encoder.encode(passwordRefresh));
        accountRepository.save(detailAccount);
        session.setAttribute("loginEmail", email);
        session.removeAttribute("loginEmailForgot");
        return "redirect:/mangostore/home";
    }

    @Override
    public String signUpAccount(String fullName, String email, String passwordRefresh) {
        Account detailAccount = accountRepository.detailAccountByEmail(email);
        if (detailAccount == null) {
            Account newAccount = new Account();
            newAccount.setFullName(fullName);
            newAccount.setEmail(email);
            newAccount.setEncryptionPassword(encoder.encode(passwordRefresh));
            newAccount.setAccumulatedPoints(0);

            Rank detailRank = rankRepository.detailRankByAccumulatedPoints(0);
            newAccount.setRank(detailRank);

            newAccount.setStatus(1);
            accountRepository.save(newAccount);

            Role roleUser = roleRepository.getAllRoleByUser();
            com.example.mangostore.entity.Authentication newAuthentication = new com.example.mangostore.entity.Authentication();
            newAuthentication.setAccount(newAccount);
            newAuthentication.setRole(roleUser);
            authenticationRepository.save(newAuthentication);

            return "redirect:/mangostore/login/from";
        } else {
            return "redirect:/mangostore/login/signup";
        }
    }

    @Override
    public String logOutWebsite(HttpSession session) {
        session.removeAttribute("loginEmail");
        session.invalidate();
        return "redirect:/mangostore/home";
    }
}
