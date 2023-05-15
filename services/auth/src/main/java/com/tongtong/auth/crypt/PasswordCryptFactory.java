package com.tongtong.auth.crypt;

import org.springframework.stereotype.Component;

@Component
public class PasswordCryptFactory {
    public PasswordCrypt getPasswordCrypt(Boolean plainTextPassword) {
        if (plainTextPassword) {
            return new PlainTextPassword();
        }
        return new HashedPasswordCrypt();
    }
}