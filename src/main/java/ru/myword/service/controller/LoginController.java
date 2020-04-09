package ru.myword.service.controller;

import java.security.NoSuchAlgorithmException;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.util.StringUtils;

import ru.myword.service.bo.User;
import ru.myword.service.exception.BaseException;
import ru.myword.service.security.GetTokenService;
import ru.myword.service.services.PasswordEncryption;

/**
 * Авторизация пользователя в системе и выдача токена
 *
 * @author Губа Георгий
 */
@RestController
@RequestMapping(LoginController.BASE_PATH_REQUEST)
public class LoginController
{
    private static final Logger LOG = LoggerFactory.getLogger(LoginController.class);

    public static final String BASE_PATH_REQUEST = "/login";

    private final GetTokenService getTokenService;
    private final PasswordEncryption passwordEncryption;

    @Inject
    public LoginController(final GetTokenService getTokenService,
            final PasswordEncryption passwordEncryption)
    {
        this.getTokenService = getTokenService;
        this.passwordEncryption = passwordEncryption;
    }

    @PostMapping
    public ResponseEntity<String> login(final @RequestBody User user) throws Exception
    {
        if (StringUtils.isEmpty(user.getLogin()) || StringUtils.isEmpty(user.getPassword()))
        {
            return ResponseEntity.badRequest().build();
        }

        try
        {
            String encryption = passwordEncryption.encryption(user.getPassword());
            String token = getTokenService.getToken(user.getLogin(), encryption);
            return ResponseEntity.ok(token);
        }
        catch (NoSuchAlgorithmException e)
        {
            LOG.error("Не удалось зашифровать пароль. ", e);
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("Не удалось получить пользователя. Обратитесь к "
                    + "разработчикам");
        }
        catch (BaseException ex)
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }
}
