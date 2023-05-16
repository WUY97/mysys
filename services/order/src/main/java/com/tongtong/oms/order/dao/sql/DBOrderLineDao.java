package com.tongtong.oms.order.dao.sql;

import com.tongtong.oms.order.entity.OrderLine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Component
public class DBOrderLineDao {

    @Autowired(required = false)
    @Qualifier("orderJdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final String GET_ORDER_SQL = "select * from orderline where orderId=?";

    public List<OrderLine> getOrderLines(String id) {
        List<OrderLine> dbOrderLines;
        try {
            dbOrderLines = jdbcTemplate.query(GET_ORDER_SQL, new Object[]{id},
                    new BeanPropertyRowMapper<>(OrderLine.class));
        } catch (Exception e) {
            return null;
        }
        if (dbOrderLines.isEmpty()) {
            return null;
        }
        return dbOrderLines;
    }

    private static final String ORDER_INSERT_SQL =
            "insert into OrderLine " +
                    "(orderId, orderLineId, productId, quantity) " +
                    "values(?,?,?,?)";

    public boolean saveOrderLines(String orderId, List<OrderLine> orderLines) {
        int[] updateStatusArr;
        try {
            updateStatusArr = jdbcTemplate.batchUpdate(ORDER_INSERT_SQL, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i)
                        throws SQLException {
                    ps.setString(1, orderId);
                    ps.setInt(2, orderLines.get(i).getId());
                    ps.setString(3, orderLines.get(i).getProductId());
                    ps.setFloat(4, orderLines.get(i).getQuantity());
                }

                @Override
                public int getBatchSize() {
                    return orderLines.size();
                }
            });
        } catch (Exception e) {
            return false;
        }
        if (updateStatusArr.length == 0) {
            return false;
        }
        return true;
    }

    private static final String DELETE_ORDER_SQL = "delete from orderline where orderId=?";

    public boolean removeOrderLines(String id) {
        try {
            jdbcTemplate.update(DELETE_ORDER_SQL, new Object[]{id});
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    private static final String DELETE_ORDERS_SQL = "delete from orderline";

    public boolean removeOrdersLines() {
        try {
            jdbcTemplate.update(DELETE_ORDERS_SQL);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

}
