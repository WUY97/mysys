package com.tongtong.admin.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(SQLAdmin.class);

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
        logger.debug("Fetched timestamp from DB; TS={}", ts.toString());
        return new Date(ts.getTime());
    }

    @Override
    public String getConnection() {
        return environment.getProperty("database.postgres.host") +
                ":" + environment.getProperty("database.postgres.port") +
                ":" + environment.getProperty("database.postgres.schema");
    }

}
