package com.deshko.task4.service.impl;

import com.deshko.task4.dao.impl.OrderDaoImpl;
import com.deshko.task4.dao.impl.ProductDaoImpl;
import com.deshko.task4.dao.impl.UserDaoImpl;
import com.deshko.task4.entity.Order;
import com.deshko.task4.entity.Product;
import com.deshko.task4.entity.User;
import com.deshko.task4.exception.DaoException;
import com.deshko.task4.exception.ServiceException;
import com.deshko.task4.service.OrderService;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class OrderServiceImpl implements OrderService {
    private static final OrderServiceImpl INSTANCE = new OrderServiceImpl();

    private OrderServiceImpl() {}

    public static OrderServiceImpl getInstance() {
        return INSTANCE;
    }

    @Override
    public List<Order> getAllOrders() throws ServiceException {
        try {
            return OrderDaoImpl.getInstance().findAll();
        } catch (DaoException e) {
            throw new ServiceException("Failed to fetch all orders", e);
        }
    }

    @Override
    public List<Order> getOrdersByUserId(Long userId) throws ServiceException {
        try {
            return OrderDaoImpl.getInstance().findAll().stream()
                    .filter(order -> order.getUser() != null && order.getUser().getId().equals(userId))
                    .collect(Collectors.toList());
        } catch (DaoException e) {
            throw new ServiceException("Failed to fetch orders for user: " + userId, e);
        }
    }

    @Override
    public boolean makeOrder(Long userId, Long productId) throws ServiceException {
        try {
            Optional<User> userOpt = UserDaoImpl.getInstance().findById(userId);
            Optional<Product> productOpt = ProductDaoImpl.getInstance().findById(productId);

            if (userOpt.isPresent() && productOpt.isPresent()) {
                Order order = new Order();
                order.setUser(userOpt.get());
                order.setProduct(productOpt.get());
                order.setStatus("PROCESSING");

                return OrderDaoImpl.getInstance().create(order);
            } else {
                throw new ServiceException("User or Product not found. Cannot place order.");
            }
        } catch (DaoException e) {
            throw new ServiceException("Error creating an order", e);
        }
    }

    @Override
    public boolean cancelOrder(Long orderId) throws ServiceException {
        try {
            return OrderDaoImpl.getInstance().delete(orderId);
        } catch (DaoException e) {
            throw new ServiceException("Failed to cancel order with id: " + orderId, e);
        }
    }
}
