package com.tongtong.product.dao.sql;

import com.tongtong.product.dao.ProductDao;
import com.tongtong.product.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Component("SQL")
public class DBProductDao implements ProductDao {

    @Autowired(required = false)
    JdbcTemplate jdbcTemplate;

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Product> getProducts() {
        String allProductSql = "select * from Product";
        List<Product> products;
        try {
            products = jdbcTemplate.query(allProductSql, new BeanPropertyRowMapper<>(Product.class));
        } catch (Exception e) {
            return null;
        }
        if (products.isEmpty()) {
            return new LinkedList<>();
        }
        return products;
    }

    @Override
    public Product getProduct(String id) {
        String productSql = "select * from product where id=?";
        Product product;
        try {
            product = jdbcTemplate.queryForObject(productSql,
                    new Object[]{id}, new BeanPropertyRowMapper<>(Product.class));
        } catch (Exception e) {
            return null;
        }
        if (product == null) {
            return null;
        }
        return product;
    }

    @Override
    public boolean addProduct(Product product) {
        String addProductSql = "insert into Product (id, name, imageUrl, price) values (?,?,?,?)";
        int retValue = jdbcTemplate.update(addProductSql,
                new Object[]{product.getId(), product.getName(), product.getImageUrl(), Float.valueOf(product.getPrice())});
        if (retValue <= 0) {
            return false;
        }
        return true;
    }

    @Override
    public Product modifyProduct(Product product) {
        String modifyProductSql = "update Product set id=?, name=?, imageUrl=?, price=?";
        int retValue;
        try {
            retValue = jdbcTemplate.update(modifyProductSql,
                    new Object[]{product.getId(), product.getName(), product.getImageUrl(), Float.valueOf(product.getPrice())});
        } catch (Exception e) {
            return null;
        }
        if (retValue <= 0) {
            return null;
        }
        return product;
    }

    @Override
    public boolean removeProduct(String id) {
        String removeProductSql = "delete from product where id=?";
        int retValue;
        try {
            retValue = jdbcTemplate.update(removeProductSql,
                    new Object[]{id});
        } catch (Exception e) {
            return false;
        }
        if (retValue <= 0) {
            return false;
        }
        return true;
    }

    @Override
    public boolean removeProducts() {
        String removeProductSql = "delete from product";
        int retValue;
        try {
            retValue = jdbcTemplate.update(removeProductSql);
        } catch (Exception e) {
            return false;
        }
        if (retValue < 0) {
            return false;
        }
        return true;
    }

    @Override
    public List<Product> getProductsByIds(List<String> ids) {
        String allProductSql = "select * from Product where id in (:ids)";
        List<Product> products;
        try {
            MapSqlParameterSource parameters = new MapSqlParameterSource();
            parameters.addValue("ids", ids);
            products = jdbcTemplate.query(allProductSql, (PreparedStatementSetter) parameters, new BeanPropertyRowMapper<>(Product.class));
        } catch (Exception e) {
            return null;
        }
        if (products.isEmpty()) {
            return new LinkedList<>();
        }
        return products;
    }
}