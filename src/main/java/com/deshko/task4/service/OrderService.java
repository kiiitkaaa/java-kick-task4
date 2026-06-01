package com.deshko.task4.service;

import java.util.List;
import com.deshko.task4.entity.Order;
import com.deshko.task4.exception.ServiceException;

public interface OrderService {
    List<Order> getAllOrders() throws ServiceException;
    List<Order> getOrdersByUserId(Long userId) throws ServiceException;
    boolean makeOrder(Long userId, Long productId) throws ServiceException;
    boolean cancelOrder(Long orderId) throws ServiceException;
}
