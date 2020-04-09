package ru.myword.service.security;

import java.security.Principal;
import java.util.Collection;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Токен для авторизации
 *
 * @author Maria Amend
 * @since 14.10.2019
 */
public class TokenAuthentication implements Authentication
{
    private String token;
    private Collection<? extends GrantedAuthority> authorities;
    private boolean isAuthenticated;
    private UserDetails details;
    private Principal principal;

    TokenAuthentication(final String token)
    {
        this.token = token;
    }

    TokenAuthentication(final String token, final UserDetails details)
    {
        this.token = token;
        this.details = details;
    }

    TokenAuthentication(
            final String token,
            final Collection<GrantedAuthority> authorities,
            final boolean isAuthenticated,
            final UserDetails details)
    {
        this.token = token;
        this.authorities = authorities;
        this.isAuthenticated = isAuthenticated;
        this.details = details;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities()
    {
        return authorities;
    }

    @Override
    public Object getCredentials()
    {
        return null;
    }

    @Override
    public Object getDetails()
    {
        return details;
    }

    @Override
    public String getName()
    {
        if (details != null)
            return details.getUsername();
        else
            return null;
    }

    @Override
    public Object getPrincipal()
    {
        return  principal;
    }

    @Override
    public boolean isAuthenticated()
    {
        return isAuthenticated;
    }

    @Override
    public void setAuthenticated(final boolean isAuthenticated) throws IllegalArgumentException
    {
        this.isAuthenticated = isAuthenticated;
    }

    public String getToken()
    {
        return token;
    }
}
