package ru.myword.service.security;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import ru.myword.service.bo.User;
import ru.myword.service.exception.BaseException;
import ru.myword.service.repository.UserRepository;

/**
 * Сервис по созданию параметров авторизации
 *
 * @author Maria Amend
 * @since 19.10.2019
 */
@Component
public class CustomUserDetailsService implements UserDetailsService
{
    private final UserRepository userRepository;

    public CustomUserDetailsService(final UserRepository userRepository)
    {
        this.userRepository = userRepository;
    }

    @Transactional
    public UserDetails loadUserByUsername(final String login) throws BaseException
    {
        Optional<User> optionalUser = userRepository.findByLogin(login);
        if (!optionalUser.isPresent())
        {
            throw new BaseException("User not found");
        }

        User user = optionalUser.get();
        return new org.springframework.security.core.userdetails.User(user.getLogin(), user.getPassword(), true,
                true, true, true, new ArrayList<>());
    }
}
