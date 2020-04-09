package ru.myword.service.security;

import static org.springframework.util.StringUtils.isEmpty;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import ru.myword.service.exception.BaseException;
import ru.myword.service.security.Constant.JwtToken;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * Реализация интерфейса {@link UserDetailsService}
 *
 * @author Maria Amend
 * @since 14.10.2019
 */
@Component
@PropertySource("classpath:configuration.properties")
public class GetTokenServiceImpl implements GetTokenService
{
    @Value("${jwt.token.key}")
    private String key;
    @Value("${jwt.token.ttl}")
    private int timeToLive;

    private final UserDetailsService userDetailsService;

    @Inject
    public GetTokenServiceImpl(final @Named("customUserDetailsService") UserDetailsService userDetailsService)
    {
        this.userDetailsService = userDetailsService;
    }

    @Override
    public String getToken(@NotNull final String username, @NotNull final String password) throws BaseException
    {
        User user = (User)userDetailsService.loadUserByUsername(username);
        Map<String, Object> tokenData = new HashMap<>();

        if (password.equals(user.getPassword()))
        {
            tokenData.put(JwtToken.USERNAME, user.getUsername());
            Calendar calendar = Calendar.getInstance();
            tokenData.put(JwtToken.CREATE_DATE, calendar.getTime());
            calendar.add(Calendar.MILLISECOND, timeToLive);
            tokenData.put(JwtToken.EXPIRATION_DATE, calendar.getTime());
            JwtBuilder jwtBuilder = Jwts.builder();
            jwtBuilder.setExpiration(calendar.getTime());
            jwtBuilder.setClaims(tokenData);
            return jwtBuilder.signWith(SignatureAlgorithm.HS512, key).compact();
        }
        else
        {
            throw new BaseException("Authentication error");
        }
    }
}
