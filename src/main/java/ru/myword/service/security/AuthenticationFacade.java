package ru.myword.service.security;

import org.springframework.security.core.Authentication;

/**
 *
 * @author Борис Лисков
 */
public interface AuthenticationFacade
{

    Authentication getAuthentication();
}
