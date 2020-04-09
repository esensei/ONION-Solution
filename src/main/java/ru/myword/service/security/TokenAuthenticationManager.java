package ru.myword.service.security;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import ru.myword.service.security.Constant.JwtToken;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.impl.DefaultClaims;

/**
 * Менеджер по авторизации пользователя через токен
 *
 * @author Maria Amend
 * @since 19.10.2019
 */
@Component
public class TokenAuthenticationManager implements AuthenticationManager
{
    @Value("${jwt.token.key}")
    private String key;

    private final UserDetailsService userDetailsService;

    @Inject
    public TokenAuthenticationManager(
            final @Named("customUserDetailsService") UserDetailsService userDetailsService)
    {
        this.userDetailsService = userDetailsService;
    }

    @Override
    public Authentication authenticate(final Authentication authentication) throws AuthenticationException
    {
        if (authentication instanceof TokenAuthentication) {
            return processAuthentication((TokenAuthentication) authentication);
        } else {
            authentication.setAuthenticated(false);
            return authentication;
        }
    }

    private TokenAuthentication processAuthentication(TokenAuthentication authentication) throws AuthenticationException {
        String token = authentication.getToken();
        DefaultClaims claims;
        try {
            claims = (DefaultClaims) Jwts.parser().setSigningKey(key).parse(token).getBody();
        } catch (Exception ex) {
            throw new AuthenticationServiceException("JwtToken corrupted");
        }

        Long expirationDate = claims.get(JwtToken.EXPIRATION_DATE, Long.class);
        if (expirationDate == null)
        {
            throw new AuthenticationServiceException("Invalid token");
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(expirationDate);
        if (calendar.before(new Date()))
        {
            throw new AuthenticationServiceException("JwtToken expired date error");
        }

        return buildFullTokenAuthentication(authentication, claims);
    }

    private TokenAuthentication buildFullTokenAuthentication(TokenAuthentication authentication, DefaultClaims claims) {
        User user = (User) userDetailsService.loadUserByUsername(claims.get(JwtToken.USERNAME, String.class));
        if (!user.isEnabled())
        {
            throw new AuthenticationServiceException("User disabled");
        }

        Collection<GrantedAuthority> authorities = user.getAuthorities();
        return new TokenAuthentication(authentication.getToken(), authorities, true, user);
    }
}
