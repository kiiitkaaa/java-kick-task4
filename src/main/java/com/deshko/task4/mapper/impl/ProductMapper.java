package com.deshko.task4.mapper.impl;

import com.deshko.task4.entity.Product;
import com.deshko.task4.mapper.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductMapper implements RowMapper<Product> {
    @Override
    public Product mapRow(ResultSet rs) throws SQLException {
        Product product = new Product();
        product.setId(rs.getLong("id"));
        product.setName(rs.getString("name"));
        product.setDescription(rs.getString("description"));
        product.setPrice(rs.getBigDecimal("price"));
        product.setActive(rs.getBoolean("is_active"));
        return product;
    }
}
