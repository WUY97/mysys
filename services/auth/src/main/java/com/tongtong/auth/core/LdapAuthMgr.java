package com.tongtong.auth.core;

import com.tongtong.common.entity.UserAuth;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.stereotype.Component;

import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.NamingException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.springframework.ldap.query.LdapQueryBuilder.query;

@Component("LDAP")
public class LdapAuthMgr implements AuthMgr {
    private LdapTemplate ldapTemplate;

    @Override
    public boolean createUser(UserAuth userAuth) {
        throw new UnsupportedOperationException(
                "Not yet implemented for ldap. Upload users directly to ldap using .ldif script");
    }

    public void setLdapTemplate(LdapTemplate ldapTemplate) {
        this.ldapTemplate = ldapTemplate;
    }

    /**
     * @param userId   user id that matches with uid in ldap
     * @param password password that matches with userPassword
     * @return true on successful authentication
     */
    public boolean authenticate(String userId, String password) {
        return ldapTemplate.authenticate(OU_PEOPLE, "(" + UID + "=" + userId + ")", password);
    }

    /**
     * @param userId user id that matches with uid of inetOrgPerson
     * @return DBUserAuth object having user details except role
     */
    public UserAuth getUserAuth(String userId) {
        List<UserAuth> list = ldapTemplate.search(query().base(OU_PEOPLE)
                        .attributes(COMMON_NAME, UID, MAIL, PASSWORD)
                        .where(UID).is(userId)
                        .and(OBJECT_CLASS).is(PERSON),
                new UserAttributesMapper());
        if (list != null && !list.isEmpty()) {
            if (list.size() != 1) {
                throw new RuntimeException("User not found or not unique");
            }
            return list.get(0);
        }
        return null;
    }

    /**
     * @param userId user id that matches with uid of inetOrgPerson
     * @return list of role strings
     */
    public List<String> getUserRole(String userId) {
        String dnString = UID + "=" + userId + "," + OU_PEOPLE + "," + DOMAIN;
        List<String> roleIds = ldapTemplate.search(query().
                        base(OU_ROLES).
                        attributes(COMMON_NAME).
                        where(ROLE_OCCUPANT).is(dnString).
                        and(OBJECT_CLASS).is(ROLE),
                new NameAttributesMapper());
        if (roleIds == null || roleIds.isEmpty()) {
            throw new RuntimeException("No roles assigned to user " + userId);
        }
        return roleIds;
    }

    @Override
    public boolean deleteUsers() {
        // ToDo: To be implemented
        return false;
    }

    private class UserAttributesMapper implements AttributesMapper<UserAuth> {

        public UserAuth mapFromAttributes(Attributes attributes) throws NamingException {
            UserAuth user;
            if (attributes == null) {
                return null;
            }
            user = new UserAuth();
            user.setName(attributes.get("cn").get().toString());

            if (attributes.get(PASSWORD) != null) {
                String userPassword;
                userPassword = new String((byte[]) attributes.get(PASSWORD).get(), StandardCharsets.UTF_8);
                user.setPassword(userPassword);
            }
            if (attributes.get(UID) != null) {
                user.setId(attributes.get(UID).get().toString());
            }
            if (attributes.get(MAIL) != null) {
                user.setEmailId(attributes.get(MAIL).get().toString());
            }
            return user;
        }
    }

    @Override
    public List<UserAuth> getAllUserAuth() {
        throw new UnsupportedOperationException("Method yet to be implemented");
    }

    private class NameAttributesMapper implements AttributesMapper<String> {
        public String mapFromAttributes(Attributes attrs) throws NamingException {
            Attribute cn = attrs.get(COMMON_NAME);
            return cn.get().toString();
        }
    }

    private static final String DOMAIN = "dc=newtechways,dc=com";
    private static final String OBJECT_CLASS = "objectClass";
    private static final String OU_PEOPLE = "ou=People";
    private static final String OU_ROLES = "ou=Roles";
    private static final String PERSON = "inetOrgPerson";
    private static final String ROLE = "organizationalRole";
    private static final String ROLE_OCCUPANT = "roleOccupant";
    private static final String UID = "uid";
    private static final String PASSWORD = "userPassword";
    private static final String COMMON_NAME = "cn";
    private static final String MAIL = "mail";
}