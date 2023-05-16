package com.tongtong.oms.cart.dao.sql;

import com.tongtong.oms.cart.dao.CartDao;
import com.tongtong.oms.cart.entity.Cart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by anurag on 24/03/17.
 */
@Component("CartSQL")
public class DBCartDao implements CartDao {

    @Autowired(required = false)
    @Qualifier("cartJdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static String SQL_GET_CARTLINES = "select * from cart c, cartline cl where c.id=cl.cartid and c.id=?";

    @Override
    public Cart getCart(String id) {
        List<DBCartLine> dbCartLines;
        try {
            dbCartLines = jdbcTemplate.query(SQL_GET_CARTLINES, new Object[]{id},
                    new BeanPropertyRowMapper<>(DBCartLine.class));
        } catch (Exception e) {
            return null;
        }
        if (dbCartLines.isEmpty()) {
            return null;
        }
        Cart cart = DBCartLine.getCart(id, dbCartLines);
        return cart;
    }

    private static String CART_INSERT_SQL =
            "insert into Cart (id) values (?)";
    private static String CARTLINE_INSERT_SQL =
            "insert into CartLine (cartLineId, cartId, productId, quantity) values (?,?,?,?)";

    @Override
    public boolean saveCart(Cart cart) {
        try {
            int retValue = jdbcTemplate.update(CART_INSERT_SQL,
                    new Object[]{cart.getId()});
            if (retValue <= 0) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        List<DBCartLine> dbCartLines = DBCartLine.createDBCartLines(cart);
        try {
            int[] updateStatusArr = jdbcTemplate.batchUpdate(CARTLINE_INSERT_SQL, new BatchPreparedStatementSetter() {

                @Override
                public void setValues(PreparedStatement ps, int i)
                        throws SQLException {
                    ps.setInt(1, dbCartLines.get(i).getCartLineId());
                    ps.setString(2, dbCartLines.get(i).getCartId());
                    ps.setString(3, dbCartLines.get(i).getProductId());
                    ps.setFloat(4, dbCartLines.get(i).getQuantity());
                }

                @Override
                public int getBatchSize() {
                    return dbCartLines.size();
                }
            });
            if (updateStatusArr == null) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    @Transactional
    public boolean updateCart(Cart cart) {
        boolean removeCartSuccessful = removeCart(cart.getId());
        if (!removeCartSuccessful) {
        }
        saveCart(cart);

        return true;
    }

    private static String DELETE_CART_SQL = "delete from cart where id=?";
    private static String DELETE_CARTLINE_SQL = "delete from cartline where cartId=?";

    @Override
    public boolean removeCart(String id) {
        try {
            int rowsUpdated = jdbcTemplate.update(DELETE_CARTLINE_SQL, new Object[]{id});
            if (rowsUpdated <= 0) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }

        try {
            int rowsUpdated = jdbcTemplate.update(DELETE_CART_SQL, new Object[]{id});
            if (rowsUpdated <= 0) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    private static final String DELETE_CARTS_SQL = "delete from cart";
    private static final String DELETE_CARTLINES_SQL = "delete from cartline";

    @Override
    public boolean removeCarts() {
        try {
            int rowsUpdated = jdbcTemplate.update(DELETE_CARTLINES_SQL);
            if (rowsUpdated < 0) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }

        try {
            int rowsUpdated = jdbcTemplate.update(DELETE_CARTS_SQL);
            if (rowsUpdated < 0) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
