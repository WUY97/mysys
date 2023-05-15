package com.tongtong.auth.core;

import com.tongtong.auth.crypt.PasswordCrypt;
import com.tongtong.auth.crypt.PasswordCryptFactory;
import com.tongtong.common.entity.UserAuth;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.LinkedList;
import java.util.List;

public abstract class DBAuthMgr implements AuthMgr {

    @Value("${auth.password.plain:false}")
    private Boolean usePlainTextPassword;

    @Autowired
    private PasswordCryptFactory passwordCryptFactory;

    private PasswordCrypt passwordCrypt;

    public PasswordCrypt getPasswordCrypt() {
        return passwordCrypt;
    }

    public void setPasswordCrypt(PasswordCrypt passwordCrypt) {
        this.passwordCrypt = passwordCrypt;
    }

    @PostConstruct
    public void postConstruct() {
        passwordCrypt = passwordCryptFactory.getPasswordCrypt(usePlainTextPassword);
    }

    @Override
    public boolean createUser(UserAuth userAuth) {
        String hashedPassword = getPasswordCrypt().hashPassword(userAuth.getPassword());
        Object userAuthRet = createUser(userAuth, hashedPassword);
        return userAuthRet != null;
    }

    @Override
    public boolean authenticate(String userId, String passwordParam) {
        UserAuth userAuth = getUserAuth(userId);
        if (userAuth == null) {
            return false;
        }
        String dbPassword = userAuth.getPassword();
        if (dbPassword != null) {
            return getPasswordCrypt().checkPassword(passwordParam, dbPassword);
        }
        return false;
    }

    @Override
    public List<String> getUserRole(String userId) {
        UserAuth userAuth = getUserAuth(userId);
        return (userAuth == null) ? new LinkedList<>() : userAuth.getRoles();
    }

    @Override
    public abstract boolean deleteUsers();

    @Override
    public abstract UserAuth getUserAuth(String userId);

    @Override
    public abstract List<UserAuth> getAllUserAuth();

    protected abstract UserAuth createUser(UserAuth userAuth, String hashedPassword);
}
