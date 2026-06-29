package com.deshko.task4.dao.impl;

import com.deshko.task4.dao.BaseDao;
import com.deshko.task4.dao.ProductDao;
import com.deshko.task4.entity.Product;
import com.deshko.task4.exception.DaoException;
import com.deshko.task4.mapper.impl.ProductMapper;
import com.deshko.task4.pool.ConnectionPool;
import java.util.List;
import java.util.Optional;
import java.sql.*;
import java.util.ArrayList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ProductDaoImpl implements BaseDao<Product>, ProductDao {
    private static final Logger logger = LogManager.getLogger(ProductDaoImpl.class);
    private static final String SQL_FIND_ALL = "SELECT id, name, description, price, is_active FROM products";
    private static final String SQL_FIND_ACTIVE = "SELECT id, name, description, price, is_active FROM products WHERE is_active = true";
    private static final String SQL_FIND_BY_ID = "SELECT id, name, description, price, is_active FROM products WHERE id = ?";
    private static final String SQL_CREATE = "INSERT INTO products (name, description, price, is_active) VALUES (?, ?, ?, ?)";
    private static final String SQL_UPDATE = "UPDATE products SET name = ?, description = ?, price = ?, is_active = ? WHERE id = ?";
    private static final String SQL_DELETE = "DELETE FROM products WHERE id = ?";
    private static final ProductDaoImpl INSTANCE = new ProductDaoImpl();
    private final ProductMapper mapper = new ProductMapper();

    private ProductDaoImpl() {}

    public static ProductDaoImpl getInstance() {
        return INSTANCE;
    }

    @Override
    public List<Product> findActiveProducts() throws DaoException {
        return executeSelectList(SQL_FIND_ACTIVE);
    }

    @Override
    public List<Product> findAll() throws DaoException {
        return executeSelectList(SQL_FIND_ALL);
    }

    @Override
    public Optional<Product> findById(Long id) throws DaoException {
        logger.debug("Executing SQL (find by id): '{}' with id={}", SQL_FIND_BY_ID, id);
        Connection connection = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement statement = connection.prepareStatement(SQL_FIND_BY_ID)) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    logger.debug("Product with id={} found successfully", id);
                    return Optional.of(mapper.mapRow(resultSet));
                }
            }
            logger.debug("Product with id={} NOT found", id);
        } catch (SQLException e) {
            logger.error("SQL Error while finding product by id={}", id, e);
            throw new DaoException("Failed to find product by id: " + id, e);
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
            logger.debug("Delete result for product id={}: {}", id, deleted);
            return deleted;
        } catch (SQLException e) {
            logger.error("SQL Error while deleting product id={}", id, e);
            throw new DaoException("Failed to delete product", e);
        } finally {
            ConnectionPool.getInstance().releaseConnection(connection);
        }
    }

    @Override
    public boolean create(Product entity) throws DaoException {
        logger.debug("Executing SQL: '{}' with params: name='{}', price={}, isActive={}",
                SQL_CREATE, entity.getName(), entity.getPrice(), entity.isActive());

        Connection connection = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement statement = connection.prepareStatement(SQL_CREATE)) {
            statement.setString(1, entity.getName());
            statement.setString(2, entity.getDescription());
            statement.setBigDecimal(3, entity.getPrice());
            statement.setBoolean(4, entity.isActive());
            boolean created = statement.executeUpdate() > 0;
            logger.debug("Create product result: {}", created);
            return created;
        } catch (SQLException e) {
            logger.error("SQL Error while creating product: '{}'", entity.getName(), e);
            throw new DaoException("Failed to create product", e);
        } finally {
            ConnectionPool.getInstance().releaseConnection(connection);
        }
    }

    @Override
    public Product update(Product entity) throws DaoException {
        logger.debug("Executing SQL: '{}' with params: id={}, name='{}', price={}, isActive={}",
                SQL_UPDATE, entity.getId(), entity.getName(), entity.getPrice(), entity.isActive());

        Connection connection = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement statement = connection.prepareStatement(SQL_UPDATE)) {
            statement.setString(1, entity.getName());
            statement.setString(2, entity.getDescription());
            statement.setBigDecimal(3, entity.getPrice());
            statement.setBoolean(4, entity.isActive());
            statement.setLong(5, entity.getId());
            if (statement.executeUpdate() == 0) {
                logger.warn("Update failed: product with id={} not found in database", entity.getId());
                throw new DaoException("Update failed, product not found");
            }
            logger.debug("Product id={} successfully updated", entity.getId());
            return entity;
        } catch (SQLException e) {
            logger.error("SQL Error while updating product id={}", entity.getId(), e);
            throw new DaoException("Failed to update product", e);
        } finally {
            ConnectionPool.getInstance().releaseConnection(connection);
        }
    }

    private List<Product> executeSelectList(String query) throws DaoException {
        logger.debug("Executing list SQL: '{}'", query);
        List<Product> products = new ArrayList<>();
        Connection connection = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                products.add(mapper.mapRow(resultSet));
            }
            logger.debug("Fetched {} products from database", products.size());
        } catch (SQLException e) {
            logger.error("SQL Error during executeSelectList with query: '{}'", query, e);
            throw new DaoException("Failed to execute product list query", e);
        } finally {
            ConnectionPool.getInstance().releaseConnection(connection);
        }
        return products;
    }
}
