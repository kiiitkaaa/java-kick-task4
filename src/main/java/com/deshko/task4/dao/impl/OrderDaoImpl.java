package com.deshko.task4.dao.impl;

import com.deshko.task4.dao.BaseDao;
import com.deshko.task4.dao.OrderDao;
import com.deshko.task4.entity.Order;
import com.deshko.task4.exception.DaoException;
import com.deshko.task4.mapper.impl.OrderMapper;
import com.deshko.task4.pool.ConnectionPool;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class OrderDaoImpl implements BaseDao<Order>, OrderDao {
    private static final Logger logger = LogManager.getLogger(OrderDaoImpl.class);
    private static final String SQL_FIND_ALL = "SELECT id, user_id, product_id, status FROM orders";
    private static final String SQL_FIND_BY_USER_ID = "SELECT id, user_id, product_id, status FROM orders WHERE user_id = ?";
    private static final String SQL_FIND_BY_ID = "SELECT id, user_id, product_id, status FROM orders WHERE id = ?";
    private static final String SQL_CREATE = "INSERT INTO orders (user_id, product_id, status) VALUES (?, ?, ?)";
    private static final String SQL_UPDATE = "UPDATE orders SET user_id = ?, product_id = ?, status = ? WHERE id = ?";
    private static final String SQL_DELETE = "DELETE FROM orders WHERE id = ?";
    private static final OrderDaoImpl INSTANCE = new OrderDaoImpl();
    private final OrderMapper mapper = new OrderMapper();

    private OrderDaoImpl() {}

    public static OrderDaoImpl getInstance() {
        return INSTANCE;
    }

    @Override
    public List<Order> findByUserId(Long userId) throws DaoException {
        logger.debug("Executing SQL: '{}' with userId={}", SQL_FIND_BY_USER_ID, userId);
        List<Order> orders = new ArrayList<>();
        Connection connection = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement statement = connection.prepareStatement(SQL_FIND_BY_USER_ID)) {
            statement.setLong(1, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    orders.add(mapper.mapRow(resultSet));
                }
            }
            logger.debug("Found {} orders for userId={}", orders.size(), userId);
        } catch (SQLException e) {
            logger.error("SQL Error while finding orders for userId={}", userId, e);
            throw new DaoException("Failed to find orders for user: " + userId, e);
        } finally {
            ConnectionPool.getInstance().releaseConnection(connection);
        }
        return orders;
    }

    @Override
    public List<Order> findAll() throws DaoException {
        logger.debug("Executing SQL: '{}'", SQL_FIND_ALL);
        List<Order> orders = new ArrayList<>();
        Connection connection = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement statement = connection.prepareStatement(SQL_FIND_ALL);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                orders.add(mapper.mapRow(resultSet));
            }
            logger.debug("Found {} orders in total", orders.size());
        } catch (SQLException e) {
            logger.error("SQL Error during findAll orders", e);
            throw new DaoException("Failed to find all orders", e);
        } finally {
            ConnectionPool.getInstance().releaseConnection(connection);
        }
        return orders;
    }

    @Override
    public Optional<Order> findById(Long id) throws DaoException {
        logger.debug("Executing SQL (find by id): '{}' with id={}", SQL_FIND_BY_ID, id);
        Connection connection = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement statement = connection.prepareStatement(SQL_FIND_BY_ID)) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    logger.debug("Order with id={} found successfully", id);
                    return Optional.of(mapper.mapRow(resultSet));
                }
            }
            logger.debug("Order with id={} NOT found", id);
        } catch (SQLException e) {
            logger.error("SQL Error while finding order by id={}", id, e);
            throw new DaoException("Failed to find order by id", e);
        } finally {
            ConnectionPool.getInstance().releaseConnection(connection);
        }
        return Optional.empty();
    }

    @Override
    public boolean delete(Long id) throws DaoException {
        logger.debug("Executing SQL (delete): '{}' with id={}", SQL_DELETE, id);
        Connection connection = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement statement = connection.prepareStatement(SQL_DELETE)) {
            statement.setLong(1, id);
            boolean updated = statement.executeUpdate() > 0;
            logger.debug("Delete result for order id={}: {}", id, updated);
            return updated;
        } catch (SQLException e) {
            logger.error("SQL Error while deleting order id={}", id, e);
            throw new DaoException("Failed to delete order", e);
        } finally {
            ConnectionPool.getInstance().releaseConnection(connection);
        }
    }

    @Override
    public boolean create(Order entity) throws DaoException {
        Long userId = entity.getUser().getId();
        Long productId = entity.getProduct().getId();
        logger.debug("Executing SQL: '{}' with params: userId={}, productId={}, status='{}'",
                SQL_CREATE, userId, productId, entity.getStatus());

        Connection connection = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement statement = connection.prepareStatement(SQL_CREATE)) {
            statement.setLong(1, entity.getUser().getId());
            statement.setLong(2, entity.getProduct().getId());
            statement.setString(3, entity.getStatus());
            boolean created = statement.executeUpdate() > 0;
            logger.debug("Create order result: {}", created);
            return created;
        } catch (SQLException e) {
            logger.error("SQL Error while creating order for userId={}, productId={}",
                    userId, productId, e);
            throw new DaoException("Failed to create order", e);
        } finally {
            ConnectionPool.getInstance().releaseConnection(connection);
        }
    }

    @Override
    public Order update(Order entity) throws DaoException {
        Long userId = entity.getUser().getId();
        Long productId = entity.getProduct().getId();
        logger.debug("Executing SQL: '{}' with params: userId={}, productId={}, status='{}', id={}",
                SQL_UPDATE, userId, productId, entity.getStatus(), entity.getId());

        Connection connection = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement statement = connection.prepareStatement(SQL_UPDATE)) {
            statement.setLong(1, entity.getUser().getId());
            statement.setLong(2, entity.getProduct().getId());
            statement.setString(3, entity.getStatus());
            statement.setLong(4, entity.getId());
            if (statement.executeUpdate() == 0) {
                logger.warn("Update failed: order with id={} not found in database",
                        entity.getId());
                throw new DaoException("Update failed, order not found");
            }
            logger.debug("Order id={} successfully updated", entity.getId());
            return entity;
        } catch (SQLException e) {
            logger.error("SQL Error while updating order id={}", entity.getId(), e);
            throw new DaoException("Failed to update order", e);
        } finally {
            ConnectionPool.getInstance().releaseConnection(connection);
        }
    }
}
