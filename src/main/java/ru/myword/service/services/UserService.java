package ru.myword.service.services;

import java.security.NoSuchAlgorithmException;
import java.util.Optional;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import ru.myword.service.bo.User;
import ru.myword.service.exception.BaseException;
import ru.myword.service.exception.DoubleSaveUserException;
import ru.myword.service.repository.UserRepository;
import ru.myword.service.security.AuthenticationFacade;

/**
 * Сервис с логикой взаимодействия с пользователем
 *
 * @author Maria Amend
 * @since 20.10.2019
 */
@Component
public class UserService
{
    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final AuthenticationFacade authenticationFacade;
    private final PasswordEncryption passwordEncryption;

    @Inject
    public UserService(final UserRepository userRepository,
            final AuthenticationFacade authenticationFacade,
            final PasswordEncryption passwordEncryption)
    {
        this.userRepository = userRepository;
        this.authenticationFacade = authenticationFacade;
        this.passwordEncryption = passwordEncryption;
    }

    public User save(User user)
    {
        boolean existsUser = userRepository.existsByLogin(user.getLogin());
        if (existsUser)
        {
            throw new DoubleSaveUserException("Данный пользователь уже существует");
        }

        try
        {
            String encryption = passwordEncryption.encryption(user.getPassword());
            user.setPassword(encryption);
            return userRepository.save(user);
        }
        catch (NoSuchAlgorithmException e)
        {
            LOG.error("Не удалось зашифровать пароль. ", e);
            throw new BaseException("Не удалось добавить пользователя. Обратитесь к разработчикам.");
        }
     }

    public Optional<User> get(String login)
    {
        return userRepository.findByLogin(login);
    }

    public void delete(User user)
    {
        userRepository.delete(user);
    }

    public Optional<User> get()
    {
        UserDetails principal = (UserDetails)authenticationFacade.getAuthentication().getDetails();
        return userRepository.findByLogin(principal.getUsername());
    }

}
