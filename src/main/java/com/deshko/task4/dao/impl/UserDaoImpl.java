package com.deshko.task4.dao.impl;

import com.deshko.task4.dao.BaseDao;
import com.deshko.task4.dao.UserDao;
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

public class UserDaoImpl implements BaseDao<User>, UserDao {
    private static final String SQL_FIND_ALL = "SELECT id, login, password_hash, role FROM users";
    private static final String SQL_FIND_BY_ID = "SELECT id, login, password_hash, role FROM users WHERE id = ?";
    private static final String SQL_FIND_BY_LOGIN = "SELECT id, login, password_hash, role FROM users WHERE login = ?";
    private static final String SQL_CREATE = "INSERT INTO users (login, password_hash, role) VALUES (?, ?, ?)";
    private static final String SQL_UPDATE = "UPDATE users SET login = ?, password_hash = ?, role = ? WHERE id = ?";
    private static final String SQL_DELETE = "DELETE FROM users WHERE id = ?";
    private static final UserDaoImpl INSTANCE = new UserDaoImpl();

    private UserDaoImpl() {}

    public static UserDaoImpl getInstance() {
        return INSTANCE;
    }

    @Override
    public Optional<User> findByLogin(String login) throws DaoException {
        Connection connection = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement statement = connection.prepareStatement(SQL_FIND_BY_LOGIN)) {
            statement.setString(1, login);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapRow(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Failed to find user by login: " + login, e);
        } finally {
            ConnectionPool.getInstance().releaseConnection(connection);
        }
        return Optional.empty();
    }

    @Override
    public List<User> findAll() throws DaoException {
        List<User> users = new ArrayList<>();
        Connection connection = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement statement = connection.prepareStatement(SQL_FIND_ALL);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                users.add(mapRow(resultSet));
            }
        } catch (SQLException e) {
            throw new DaoException("Failed to find all users", e);
        } finally {
            ConnectionPool.getInstance().releaseConnection(connection);
        }
        return users;
    }

    @Override
    public Optional<User> findById(Long id) throws DaoException {
        Connection connection = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement statement = connection.prepareStatement(SQL_FIND_BY_ID)) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapRow(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Failed to find user by id: " + id, e);
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
            throw new DaoException("Failed to delete user", e);
        } finally {
            ConnectionPool.getInstance().releaseConnection(connection);
        }
    }

    @Override
    public boolean create(User entity) throws DaoException {
        Connection connection = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement statement = connection.prepareStatement(SQL_CREATE)) {
            statement.setString(1, entity.getLogin());
            statement.setString(2, entity.getPasswordHash());
            statement.setString(3, entity.getRole());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException("Failed to create user", e);
        } finally {
            ConnectionPool.getInstance().releaseConnection(connection);
        }
    }

    @Override
    public User update(User entity) throws DaoException {
        Connection connection = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement statement = connection.prepareStatement(SQL_UPDATE)) {
            statement.setString(1, entity.getLogin());
            statement.setString(2, entity.getPasswordHash());
            statement.setString(3, entity.getRole());
            statement.setLong(4, entity.getId()); // Из AbstractEntity
            if (statement.executeUpdate() == 0) {
                throw new DaoException("Update failed, user not found");
            }
            return entity;
        } catch (SQLException e) {
            throw new DaoException("Failed to update user", e);
        } finally {
            ConnectionPool.getInstance().releaseConnection(connection);
        }
    }

    private User mapRow(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getLong("id"));
        user.setLogin(rs.getString("login"));
        user.setPasswordHash(rs.getString("password_hash"));
        user.setRole(rs.getString("role"));
        return user;
    }
}