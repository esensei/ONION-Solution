package ru.myword.service.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

/**
 * Фильтер по авторизации пользователя
 *
 * @author Maria Amend
 * @since 19.10.2019
 */
public class TokenAuthenticationFilter extends AbstractAuthenticationProcessingFilter
{
    private static final Logger LOG = LogManager.getLogger(TokenAuthenticationFilter.class);


    public TokenAuthenticationFilter()
    {
        super("/service/**");

        setAuthenticationSuccessHandler(((request, response, authentication) ->
        {
            SecurityContextHolder.getContext().setAuthentication(authentication);
            request.getRequestDispatcher(request.getRequestURI())
                    .forward(request,response);
        }));

        setAuthenticationFailureHandler(((request, response, exception) -> {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getOutputStream().print(exception.getMessage());
        }));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException
    {
        String token = request.getHeader(Constant.JwtToken.NAME_HEADER);
        if (token == null)
        {
            throw new AuthenticationServiceException("No user authorization");
        }

        TokenAuthentication tokenAuthentication = new TokenAuthentication(token);
        return getAuthenticationManager().authenticate(tokenAuthentication);
    }
}
