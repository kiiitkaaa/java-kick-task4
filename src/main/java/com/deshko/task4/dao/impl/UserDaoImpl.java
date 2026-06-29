package com.deshko.task4.dao.impl;

import com.deshko.task4.dao.BaseDao;
import com.deshko.task4.dao.UserDao;
import com.deshko.task4.entity.User;
import com.deshko.task4.exception.DaoException;
import com.deshko.task4.mapper.impl.UserMapper;
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

public class UserDaoImpl implements BaseDao<User>, UserDao {
    private static final Logger logger = LogManager.getLogger(UserDaoImpl.class);
    private static final String SQL_FIND_ALL = "SELECT id, login, password_hash, role FROM users";
    private static final String SQL_FIND_BY_ID = "SELECT id, login, password_hash, role FROM users WHERE id = ?";
    private static final String SQL_FIND_BY_LOGIN = "SELECT id, login, password_hash, role FROM users WHERE login = ?";
    private static final String SQL_CREATE = "INSERT INTO users (login, password_hash, role) VALUES (?, ?, ?)";
    private static final String SQL_UPDATE = "UPDATE users SET login = ?, password_hash = ?, role = ? WHERE id = ?";
    private static final String SQL_DELETE = "DELETE FROM users WHERE id = ?";
    private static final UserDaoImpl INSTANCE = new UserDaoImpl();
    private final UserMapper mapper = new UserMapper();

    private UserDaoImpl() {}

    public static UserDaoImpl getInstance() {
        return INSTANCE;
    }

    @Override
    public Optional<User> findByLogin(String login) throws DaoException {
        logger.debug("Executing SQL: '{}' with login='{}'", SQL_FIND_BY_LOGIN, login);
        Connection connection = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement statement = connection.prepareStatement(SQL_FIND_BY_LOGIN)) {
            statement.setString(1, login);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    logger.debug("User with login='{}' found successfully", login);
                    return Optional.of(mapper.mapRow(resultSet));
                }
            }
            logger.debug("User with login='{}' NOT found", login);
        } catch (SQLException e) {
            logger.error("SQL Error while finding user by login='{}'", login, e);
            throw new DaoException("Failed to find user by login: " + login, e);
        } finally {
            ConnectionPool.getInstance().releaseConnection(connection);
        }
        return Optional.empty();
    }

    @Override
    public List<User> findAll() throws DaoException {
        logger.debug("Executing SQL: '{}'", SQL_FIND_ALL);
        List<User> users = new ArrayList<>();
        Connection connection = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement statement = connection.prepareStatement(SQL_FIND_ALL);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                users.add(mapper.mapRow(resultSet));
            }
            logger.debug("Fetched {} users from database", users.size());
        } catch (SQLException e) {
            logger.error("SQL Error during findAll users", e);
            throw new DaoException("Failed to find all users", e);
        } finally {
            ConnectionPool.getInstance().releaseConnection(connection);
        }
        return users;
    }

    @Override
    public Optional<User> findById(Long id) throws DaoException {
        logger.debug("Executing SQL (find by id): '{}' with id={}", SQL_FIND_BY_ID, id);
        Connection connection = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement statement = connection.prepareStatement(SQL_FIND_BY_ID)) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    logger.debug("User with id={} found successfully", id);
                    return Optional.of(mapper.mapRow(resultSet));
                }
            }
            logger.debug("User with id={} NOT found", id);
        } catch (SQLException e) {
            logger.error("SQL Error while finding user by id={}", id, e);
            throw new DaoException("Failed to find user by id: " + id, e);
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
            boolean deleted = statement.executeUpdate() > 0;
            logger.debug("Delete result for user id={}: {}", id, deleted);
            return deleted;
        } catch (SQLException e) {
            logger.error("SQL Error while deleting user id={}", id, e);
            throw new DaoException("Failed to delete user", e);
        } finally {
            ConnectionPool.getInstance().releaseConnection(connection);
        }
    }

    @Override
    public boolean create(User entity) throws DaoException {
        String roleStr = entity.getRole().name();
        logger.debug("Executing SQL: '{}' with params: login='{}', role='{}'",
                SQL_CREATE, entity.getLogin(), roleStr);

        Connection connection = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement statement = connection.prepareStatement(SQL_CREATE)) {
            statement.setString(1, entity.getLogin());
            statement.setString(2, entity.getPasswordHash());
            statement.setString(3, entity.getRole().name());
            boolean created = statement.executeUpdate() > 0;
            logger.debug("Create user result: {}", created);
            return created;
        } catch (SQLException e) {
            logger.error("SQL Error while creating user with login='{}'", entity.getLogin(), e);
            throw new DaoException("Failed to create user", e);
        } finally {
            ConnectionPool.getInstance().releaseConnection(connection);
        }
    }

    @Override
    public User update(User entity) throws DaoException {
        String roleStr = entity.getRole().name();
        logger.debug("Executing SQL: '{}' with params: id={}, login='{}', role='{}'",
                SQL_UPDATE, entity.getId(), entity.getLogin(), roleStr);

        Connection connection = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement statement = connection.prepareStatement(SQL_UPDATE)) {
            statement.setString(1, entity.getLogin());
            statement.setString(2, entity.getPasswordHash());
            statement.setString(3, entity.getRole().name());
            statement.setLong(4, entity.getId());
            if (statement.executeUpdate() == 0) {
                logger.warn("Update failed: user with id={} not found in database", entity.getId());
                throw new DaoException("Update failed, user not found");
            }
            logger.debug("User id={} successfully updated", entity.getId());
            return entity;
        } catch (SQLException e) {
            logger.error("SQL Error while updating user id={}", entity.getId(), e);
            throw new DaoException("Failed to update user", e);
        } finally {
            ConnectionPool.getInstance().releaseConnection(connection);
        }
    }
}