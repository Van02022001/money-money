package com.example.moneymoney.exception;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserNotFoundException extends UsernameNotFoundException {
    private static final long serialVersionUID = 1L;
    public UserNotFoundException(String email, String message) {
        super(String.format("Failed for [%s]: %s", email, message));
    }


}
