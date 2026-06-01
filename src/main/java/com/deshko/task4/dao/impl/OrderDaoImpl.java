package com.deshko.task4.dao.impl;

import com.deshko.task4.dao.BaseDao;
import com.deshko.task4.dao.OrderDao;
import com.deshko.task4.entity.Order;
import com.deshko.task4.entity.Product;
import com.deshko.task4.entity.User;
import com.deshko.task4.exception.DaoException;
import com.deshko.task4.pool.ConnectionPool;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OrderDaoImpl implements BaseDao<Order>, OrderDao {
    private static final String SQL_FIND_ALL = "SELECT id, user_id, product_id, status FROM orders";
    private static final String SQL_FIND_BY_USER_ID = "SELECT id, user_id, product_id, status FROM orders WHERE user_id = ?";
    private static final String SQL_FIND_BY_ID = "SELECT id, user_id, product_id, status FROM orders WHERE id = ?";
    private static final String SQL_CREATE = "INSERT INTO orders (user_id, product_id, status) VALUES (?, ?, ?)";
    private static final String SQL_UPDATE = "UPDATE orders SET user_id = ?, product_id = ?, status = ? WHERE id = ?";
    private static final String SQL_DELETE = "DELETE FROM orders WHERE id = ?";
    private static final OrderDaoImpl INSTANCE = new OrderDaoImpl();

    private OrderDaoImpl() {}

    public static OrderDaoImpl getInstance() {
        return INSTANCE;
    }

    @Override
    public List<Order> findByUserId(Long userId) throws DaoException {
        List<Order> orders = new ArrayList<>();
        Connection connection = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement statement = connection.prepareStatement(SQL_FIND_BY_USER_ID)) {
            statement.setLong(1, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    orders.add(mapRow(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Failed to find orders for user: " + userId, e);
        } finally {
            ConnectionPool.getInstance().releaseConnection(connection);
        }
        return orders;
    }

    @Override
    public List<Order> findAll() throws DaoException {
        List<Order> orders = new ArrayList<>();
        Connection connection = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement statement = connection.prepareStatement(SQL_FIND_ALL);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                orders.add(mapRow(resultSet));
            }
        } catch (SQLException e) {
            throw new DaoException("Failed to find all orders", e);
        } finally {
            ConnectionPool.getInstance().releaseConnection(connection);
        }
        return orders;
    }

    @Override
    public Optional<Order> findById(Long id) throws DaoException {
        Connection connection = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement statement = connection.prepareStatement(SQL_FIND_BY_ID)) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapRow(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Failed to find order by id", e);
        } finally {
            ConnectionPool.getInstance().releaseConnection(connection);
        }
        return Optional.empty();
    }

    @Override
    public boolean delete(Long id) throws DaoException {
        Connection connection = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement statement = connection.prepareStatement(SQL_DELETE)) {
            statement.setLong(1, id);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException("Failed to delete order", e);
        } finally {
            ConnectionPool.getInstance().releaseConnection(connection);
        }
    }

    @Override
    public boolean create(Order entity) throws DaoException {
        Connection connection = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement statement = connection.prepareStatement(SQL_CREATE)) {
            statement.setLong(1, entity.getUser().getId());
            statement.setLong(2, entity.getProduct().getId());
            statement.setString(3, entity.getStatus());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException("Failed to create order", e);
        } finally {
            ConnectionPool.getInstance().releaseConnection(connection);
        }
    }

    @Override
    public Order update(Order entity) throws DaoException {
        Connection connection = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement statement = connection.prepareStatement(SQL_UPDATE)) {
            statement.setLong(1, entity.getUser().getId());
            statement.setLong(2, entity.getProduct().getId());
            statement.setString(3, entity.getStatus());
            statement.setLong(4, entity.getId());
            if (statement.executeUpdate() == 0) {
                throw new DaoException("Update failed, order not found");
            }
            return entity;
        } catch (SQLException e) {
            throw new DaoException("Failed to update order", e);
        } finally {
            ConnectionPool.getInstance().releaseConnection(connection);
        }
    }

    private Order mapRow(ResultSet rs) throws SQLException {
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
