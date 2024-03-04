package com.example.mangostore.config;

import com.example.mangostore.entity.Account;
import com.example.mangostore.repository.AccountRepository;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

@Configuration
public class Gender {
    private final AccountRepository accountRepository;
    private final JavaMailSender mailSender;

    public Gender(AccountRepository accountRepository,
                  JavaMailSender mailSender) {
        this.accountRepository = accountRepository;
        this.mailSender = mailSender;
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

    public String getCurrentDateTime() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd : HH:mm:ss");
        return now.format(formatter);
    }

    public String generateCode() {
        int leftLimit = 48;
        int rightLimit = 90;
        int targetStringLength = 10;
        Random random = new Random();
        return random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }
}