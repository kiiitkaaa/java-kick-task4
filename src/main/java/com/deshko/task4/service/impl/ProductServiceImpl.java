package com.deshko.task4.service.impl;

import com.deshko.task4.dao.impl.ProductDaoImpl;
import com.deshko.task4.entity.Product;
import com.deshko.task4.exception.DaoException;
import com.deshko.task4.exception.ServiceException;
import com.deshko.task4.service.ProductService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.List;
import java.util.Optional;

public class ProductServiceImpl implements ProductService {
    private static final Logger logger = LogManager.getLogger(ProductServiceImpl.class);
    private static final ProductServiceImpl INSTANCE = new ProductServiceImpl();

    private ProductServiceImpl() {}

    public static ProductServiceImpl getInstance() {
        return INSTANCE;
    }

    @Override
    public List<Product> getAllProducts() throws ServiceException {
        logger.info("Fetching all products");

        try {
            List<Product> products = ProductDaoImpl.getInstance().findAll();
            logger.debug("Loaded {} products", products.size());
            return products;
        } catch (DaoException e) {
            logger.error("Failed to load products catalog", e);
            throw new ServiceException("Failed to load products catalog", e);
        }
    }

    @Override
    public Optional<Product> getProductById(Long id) throws ServiceException {
        logger.info("Fetching product by id={}", id);
        try {
            return ProductDaoImpl.getInstance().findById(id);
        } catch (DaoException e) {
            logger.error("Failed to find product with id={}", id, e);
            throw new ServiceException("Failed to find product with id: " + id, e);
        }
    }

    @Override
    public boolean addProduct(Product product) throws ServiceException {
        logger.info("Adding new product: name={}, price={}", product.getName(), product.getPrice());

        if (product.getPrice() == null || product.getPrice().doubleValue() < 0) {
            logger.warn("Attempt to add product with invalid price: {}", product.getPrice());
            throw new ServiceException("Product price cannot be negative!");
        }

        try {
            product.setActive(true);
            boolean created = ProductDaoImpl.getInstance().create(product);
            logger.info("Product added successfully: name={}", product.getName());
            return created;
        } catch (DaoException e) {
            logger.error("Failed to add new product: name={}", product.getName(), e);
            throw new ServiceException("Failed to add new product", e);
        }
    }

    @Override
    public boolean deleteProduct(Long id) throws ServiceException {
        logger.info("Deleting product with id={}", id);

        try {
            boolean deleted = ProductDaoImpl.getInstance().delete(id);
            logger.info("Product deleted: id={}", id);
            return deleted;
        } catch (DaoException e) {
            logger.error("Failed to delete product with id={}", id, e);
            throw new ServiceException("Failed to delete product", e);
        }
    }

    @Override
    public Product updateProduct(Product product) throws ServiceException {
        logger.info("Updating product: id={}", product.getId());

        try {
            Product updated = ProductDaoImpl.getInstance().update(product);

            logger.info("Product updated successfully: id={}", product.getId());
            return updated;
        } catch (DaoException e) {
            logger.error("Failed to update product settings: id={}", product.getId(), e);
            throw new ServiceException("Failed to update product settings", e);
        }
    }
}
