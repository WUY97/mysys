package com.tongtong.auth.db.sql;

import com.tongtong.auth.core.DBAuthMgr;
import com.tongtong.common.entity.UserAuth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component("SQL")
public class SQLAuthMgr extends DBAuthMgr {

    @Autowired(required = false)
    JdbcTemplate jdbcTemplate;

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    private static final String GET_USER_ROLE_SQL = "select role from UserRole where id = ?";

    @Override
    public List<String> getUserRole(String userId) {
        List<String> roles;
        try {
            roles = jdbcTemplate.queryForList(GET_USER_ROLE_SQL, new Object[]{userId}, String.class);
        } catch (Exception e) {
            return null;
        }
        if (roles.isEmpty()) {
            return null;
        }
        return roles;
    }

    private static final String GET_USER_AUTH_SQL = "select * from UserAuth where id = ?";

    @Override
    public UserAuth getUserAuth(String userId) {
        UserAuth userAuth;
        try {
            userAuth = jdbcTemplate.queryForObject(GET_USER_AUTH_SQL, new Object[]{userId},
                    new BeanPropertyRowMapper<UserAuth>(UserAuth.class));
        } catch (Exception e) {
            return null;
        }
        if (userAuth == null) {
        }
        List<String> roles = getUserRole(userId);
        if (roles != null) {
            userAuth.setRoles(roles);
        }
        return userAuth;
    }

    private static final String GET_USERS_ROLE_SQL = "select * from UserRole";

    private List<UserRole> getAllUsersRole() {
        List<UserRole> userRoles;
        try {
            userRoles = jdbcTemplate.query(GET_USERS_ROLE_SQL,
                    new BeanPropertyRowMapper<>(UserRole.class));
        } catch (Exception e) {
            return null;
        }
        if (userRoles.isEmpty()) {
            return null;
        }
        return userRoles;
    }

    private static final String GET_USERS_AUTH_SQL = "select * from UserAuth";

    @Override
    public List<UserAuth> getAllUserAuth() {
        List<UserAuth> userAuthList;
        try {
            userAuthList = jdbcTemplate.query(GET_USERS_AUTH_SQL,
                    new BeanPropertyRowMapper<>(UserAuth.class));
        } catch (Exception e) {
            return null;
        }
        if (userAuthList.isEmpty()) {
            return null;
        }

        List<UserRole> userRoles = getAllUsersRole();
        Map<String, UserAuth> userAuthMap = new HashMap<>(1000);
        for (UserAuth userAuth : userAuthList) {
            userAuthMap.put(userAuth.getId(), userAuth);
        }
        for (UserRole userRole : userRoles) {
            UserAuth userAuth = userAuthMap.get(userRole.getId());
            if (userAuth == null) {
                continue;
            }
            userAuth.getRoles().add(userRole.getRole());
        }
        return userAuthList;
    }

    private static final String INSERT_USER_AUTH_SQL =
            "insert into UserAuth (id, emailId, name, password) values (?,?,?,?)";

    @Override
    public UserAuth createUser(UserAuth userAuth, String hashedPassword) {
        int updateStatus;
        try {
            updateStatus = jdbcTemplate.update(INSERT_USER_AUTH_SQL, new Object[]{
                    userAuth.getId(), userAuth.getEmailId(), userAuth.getName(), hashedPassword
            });
        } catch (Exception e) {
            return null;
        }
        if (updateStatus <= 0) {
            return null;
        }
        List<String> roles = userAuth.getRoles();
        String sqlUserRole = "insert into UserRole (id, role) values (?,?)";
        int[] updateStatusArr;
        try {
            updateStatusArr = jdbcTemplate.batchUpdate(sqlUserRole, new BatchPreparedStatementSetter() {

                @Override
                public void setValues(PreparedStatement ps, int i)
                        throws SQLException {
                    ps.setString(1, userAuth.getId());
                    ps.setString(2, roles.get(i));
                }

                @Override
                public int getBatchSize() {
                    return roles.size();
                }
            });
        } catch (Exception e) {
            return userAuth;
        }
        return userAuth;
    }

    private static final String DELETE_USERS_AUTH_SQL = "delete from UserAuth";
    private static final String DELETE_USERS_ROLE_SQL = "delete from UserRole";

    @Override
    public boolean deleteUsers() {
        boolean success = true;
        try {
            getJdbcTemplate().execute(DELETE_USERS_ROLE_SQL);
        } catch (Exception e) {
            success = false;
        }
        try {
            getJdbcTemplate().execute(DELETE_USERS_AUTH_SQL);
        } catch (Exception e) {
            success = false;
        }
        return success;
    }

}
