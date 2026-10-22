package com.bornfire.brrs.services;

import java.security.SecureRandom;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class OtpService {

    @Autowired
    private JdbcTemplate jdbc;

    @Autowired
    private JavaMailSender mailSender;

    private final PasswordEncoder encoder = new BCryptPasswordEncoder();

    private static final SecureRandom RANDOM = new SecureRandom();

    public void sendOtp(String userId, String email) {
        String otp = String.format("%06d", RANDOM.nextInt(1_000_000));

        jdbc.update("DELETE FROM BRRS_LOGIN_OTP WHERE USER_ID = ?", userId);
        jdbc.update(
            "INSERT INTO BRRS_LOGIN_OTP (USER_ID, OTP_HASH, EXPIRY_TIME, ATTEMPTS) " +
            "VALUES (?, ?, SYSTIMESTAMP + INTERVAL '5' MINUTE, 0)",
            userId, encoder.encode(otp));

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(email);
        msg.setSubject("BRRS Login OTP");
        msg.setText("Your OTP is " + otp + ". It is valid for 5 minutes. Do not share it.");
        mailSender.send(msg);
    }

    public boolean verify(String userId, String otp) {
        Map<String, Object> row;
        try {
            row = jdbc.queryForMap(
                "SELECT OTP_HASH, ATTEMPTS, " +
                "CASE WHEN EXPIRY_TIME > SYSTIMESTAMP THEN 1 ELSE 0 END AS VALID " +
                "FROM BRRS_LOGIN_OTP WHERE USER_ID = ?", userId);
        } catch (EmptyResultDataAccessException e) {
            return false;
        }

        int attempts = ((Number) row.get("ATTEMPTS")).intValue();
        boolean valid = ((Number) row.get("VALID")).intValue() == 1;
        if (!valid || attempts >= 3) return false;

        if (encoder.matches(otp, (String) row.get("OTP_HASH"))) {
            jdbc.update("DELETE FROM BRRS_LOGIN_OTP WHERE USER_ID = ?", userId); // one-time use
            return true;
        }
        jdbc.update("UPDATE BRRS_LOGIN_OTP SET ATTEMPTS = ATTEMPTS + 1 WHERE USER_ID = ?", userId);
        return false;
    }
}