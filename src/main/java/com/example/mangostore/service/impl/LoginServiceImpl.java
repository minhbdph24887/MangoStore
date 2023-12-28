package com.example.mangostore.service.impl;

import com.example.mangostore.entity.Account;
import com.example.mangostore.entity.Role;
import com.example.mangostore.repository.AccountRepository;
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
import java.util.HashSet;
import java.util.Set;

@Service
public class LoginServiceImpl implements LoginService {
    private final AccountRepository accountRepository;
    private final PasswordEncoder encoder;
    private final RoleRepository roleRepository;

    public LoginServiceImpl(AccountRepository accountRepository,
                            PasswordEncoder encoder,
                            RoleRepository roleRepository) {
        this.accountRepository = accountRepository;
        this.encoder = encoder;
        this.roleRepository = roleRepository;
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

            Role roleUser = roleRepository.getAllRoleByUser();
            Set<Role> rolesUser = new HashSet<>();
            rolesUser.add(roleUser);
            newAccount.setRoles(rolesUser);

            accountRepository.save(newAccount);
            existUser = newAccount;
        }
        HttpSession session = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession();
        session.setAttribute("loginEmail", existUser.getEmail());
        response.sendRedirect("/mangostore/home");
    }
}
