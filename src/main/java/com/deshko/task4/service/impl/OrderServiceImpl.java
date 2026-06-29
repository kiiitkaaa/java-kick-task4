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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class OrderServiceImpl implements OrderService {
    private static final Logger logger = LogManager.getLogger(OrderServiceImpl.class);
    private static final OrderServiceImpl INSTANCE = new OrderServiceImpl();

    private OrderServiceImpl() {}

    public static OrderServiceImpl getInstance() {
        return INSTANCE;
    }

    @Override
    public List<Order> getAllOrders() throws ServiceException {
        logger.info("Fetching all orders");

        try {
            List<Order> orders = OrderDaoImpl.getInstance().findAll();
            logger.debug("Fetched {} orders", orders.size());
            return orders;
        } catch (DaoException e) {
            logger.error("Failed to fetch all orders", e);
            throw new ServiceException("Failed to fetch all orders", e);
        }
    }

    @Override
    public List<Order> getOrdersByUserId(Long userId) throws ServiceException {
        logger.info("Fetching orders for userId={}", userId);

        try {
            List<Order> result = OrderDaoImpl.getInstance().findAll().stream()
                    .filter(order -> order.getUser() != null
                            && order.getUser().getId().equals(userId))
                    .collect(Collectors.toList());

            logger.debug("Found {} orders for userId={}", result.size(), userId);
            return result;
        } catch (DaoException e) {
            logger.error("Failed to fetch orders for userId={}", userId, e);
            throw new ServiceException("Failed to fetch orders for user: " + userId, e);
        }
    }

    @Override
    public boolean makeOrder(Long userId, Long productId) throws ServiceException {
        logger.info("Creating order: userId={}, productId={}", userId, productId);

        try {
            Optional<User> userOpt = UserDaoImpl.getInstance().findById(userId);
            Optional<Product> productOpt = ProductDaoImpl.getInstance().findById(productId);

            if (userOpt.isPresent() && productOpt.isPresent()) {
                Order order = new Order();
                order.setUser(userOpt.get());
                order.setProduct(productOpt.get());
                order.setStatus("PROCESSING");
                
                boolean created = OrderDaoImpl.getInstance().create(order);
                logger.info("Order created successfully for userId={}, productId={}", userId, productId);
                return created;
            } else {
                logger.warn("Cannot create order: user or product not found (userId={}, productId={})",
                        userId, productId);
                throw new ServiceException("User or Product not found. Cannot place order.");
            }
        } catch (DaoException e) {
            logger.error("Error creating order (userId={}, productId={})", userId, productId, e);
            throw new ServiceException("Error creating an order", e);
        }
    }

    @Override
    public boolean cancelOrder(Long orderId) throws ServiceException {
        logger.info("Cancelling order with id={}", orderId);

        try {
            boolean deleted = OrderDaoImpl.getInstance().delete(orderId);
            logger.info("Order cancelled successfully, id={}", orderId);
            return deleted;
        } catch (DaoException e) {
            logger.error("Failed to cancel order with id={}", orderId, e);
            throw new ServiceException("Failed to cancel order with id: " + orderId, e);
        }
    }
}
