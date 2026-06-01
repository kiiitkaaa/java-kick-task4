package com.deshko.task4.dao;

import java.util.List;
import com.deshko.task4.entity.Order;
import com.deshko.task4.exception.DaoException;

public interface OrderDao {
    List<Order> findByUserId(Long userId) throws DaoException;
}
