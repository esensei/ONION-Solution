package ru.myword.service.services;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 *
 * @author Борис Лисков
 */
@Component
public class PasswordEncryption
{
    @Value("${encryption.password.key}")
    private String key;

    public String encryption(String password) throws NoSuchAlgorithmException
    {
        byte[]salt = key.getBytes();
        MessageDigest md = MessageDigest.getInstance("SHA-512");
        md.update(salt);
        byte[]hashedPassword = md.digest(password.getBytes(StandardCharsets.UTF_8));
        String encr = new String(hashedPassword);
        return encr.replace('\u0000', '.');
    }
}
