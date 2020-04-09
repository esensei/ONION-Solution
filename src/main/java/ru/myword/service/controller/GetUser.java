package ru.myword.service.controller;

import java.util.Optional;

import javax.inject.Inject;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.util.StringUtils;

import ru.myword.service.bo.User;
import ru.myword.service.security.AuthenticationFacade;
import ru.myword.service.services.UserService;

/**
 *
 * @author Губа Георгий

 */
@RestController
@RequestMapping("/service/user")
public class GetUser
{
    private final UserService userService;
    private final AuthenticationFacade authenticationFacade;

    @Inject
    public GetUser(final UserService userService, final AuthenticationFacade authenticationFacade)
    {
        this.userService = userService;
        this.authenticationFacade = authenticationFacade;
    }

    @GetMapping("/{login}")
    public ResponseEntity<User> getUserByLogin(@PathVariable String login)
    {
        if (StringUtils.isEmpty(login))
        {
            return ResponseEntity.badRequest().build();
        }

        Optional<User> userOptional = userService.get(login);

        if (!userOptional.isPresent())
        {
            return ResponseEntity.notFound().build();
        }

        User user = userOptional.get();
        return ResponseEntity.ok(user);
    }

    @GetMapping
    public ResponseEntity<User> getUser()
    {
        Optional<User> userOptional = userService.get();

        if (!userOptional.isPresent())
        {
            return ResponseEntity.notFound().build();
        }

        User user = userOptional.get();
        return ResponseEntity.ok(user);
    }

}
