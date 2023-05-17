package com.tongtong.oms.admin.db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.Date;

@Component
@Qualifier("sqlAdmin")
public class SQLAdmin implements DBAdmin {

    @Autowired(required = false)
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private Environment environment;

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private String SQL_GET_DATE_TIME = "Select now()";

    @Override
    public Date getDateTime() {
        Timestamp ts = jdbcTemplate.queryForObject(SQL_GET_DATE_TIME, Timestamp.class);
        return new Date(ts.getTime());
    }

    @Override
    public String getConnection() {
        return new StringBuilder().append(environment.getProperty("database.postgres.host"))
                .append(":").append(environment.getProperty("database.postgres.port"))
                .append(":").append(environment.getProperty("database.postgres.schema"))
                .toString();
    }

}
