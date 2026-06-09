package com.deshko.task4.mapper.impl;

import com.deshko.task4.entity.Order;
import com.deshko.task4.entity.Product;
import com.deshko.task4.entity.User;
import com.deshko.task4.mapper.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderMapper implements RowMapper<Order> {
    @Override
    public Order mapRow(ResultSet rs) throws SQLException {
        Order order = new Order();
        order.setId(rs.getLong("id"));
        order.setStatus(rs.getString("status"));

        User userProxy = new User();
        userProxy.setId(rs.getLong("user_id"));
        order.setUser(userProxy);

        Product productProxy = new Product();
        productProxy.setId(rs.getLong("product_id"));
        order.setProduct(productProxy);

        return order;
    }
}
