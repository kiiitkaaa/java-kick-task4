package com.deshko.task4.mapper.impl;

import com.deshko.task4.entity.Role;
import com.deshko.task4.entity.User;
import com.deshko.task4.mapper.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserMapper implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getLong("id"));
        user.setLogin(rs.getString("login"));
        user.setPasswordHash(rs.getString("password_hash"));
        String role = rs.getString("role");
        user.setRole(Role.valueOf(role.toUpperCase()));
        return user;
    }
}
