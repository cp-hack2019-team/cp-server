package com.puzzle.security.jwt;

import org.springframework.security.core.AuthenticationException;

/**
 * @author ibez
 * @since 2019-06-08
 */
public class InvalidJwtAuthenticationException extends AuthenticationException {
    public InvalidJwtAuthenticationException(String e) {
        super(e);
    }
}
