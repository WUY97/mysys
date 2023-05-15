package com.tongtong.auth.crypt;

public class PlainTextPassword implements PasswordCrypt {

    @Override
    public String hashPassword(String plainTextPassword) {
        return plainTextPassword;
    }

    @Override
    public boolean checkPassword(String plainTextPassword, String storedHash) {
        return plainTextPassword.equals(storedHash);
    }

}