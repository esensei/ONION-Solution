package ru.myword.service.controller;

import java.util.function.Predicate;

import javax.inject.Inject;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.util.StringUtils;

import ru.myword.service.bo.User;
import ru.myword.service.exception.DoubleSaveUserException;
import ru.myword.service.services.UserService;

/**
 * Регистрация пользователя в системе
 *
 * @author Губа Георгий
 */
@RestController
@RequestMapping(RegistrationController.BASE_PATH_REQUEST)
public class RegistrationController
{
    public static final String BASE_PATH_REQUEST = "/registration";

    private final UserService userService;

    @Inject
    public RegistrationController(final UserService userService)
    {
        this.userService = userService;
    }

    /**
     * Регистрация пользователя
     *
     * @param user пользователь для создания
     * @return созданный пользователь
     */
    @PostMapping
    public ResponseEntity<User> registration(final @RequestBody User user)
    {
        if (StringUtils.isEmpty(user.getLogin()) || StringUtils.isEmpty(user.getPassword()))
        {
            return ResponseEntity.badRequest().build();
        }

        try
        {
            User saveUser = userService.save(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(saveUser);
        }
        catch (DoubleSaveUserException e)
        {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

}
