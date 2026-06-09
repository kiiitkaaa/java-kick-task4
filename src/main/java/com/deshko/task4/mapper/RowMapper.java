package com.deshko.task4.mapper;

import com.deshko.task4.entity.AbstractEntity;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface RowMapper<T extends AbstractEntity> {
    T mapRow(ResultSet rs) throws SQLException;
}
