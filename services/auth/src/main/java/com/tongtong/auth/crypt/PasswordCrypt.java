package com.tongtong.auth.crypt;

public interface PasswordCrypt {

    String hashPassword(String plainTextPassword);

    boolean checkPassword(String plainTextPassword, String storedHash);
}