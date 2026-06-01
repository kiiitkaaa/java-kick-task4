package com.deshko.task4.service.impl;

import com.deshko.task4.dao.impl.ProductDaoImpl;
import com.deshko.task4.entity.Product;
import com.deshko.task4.exception.DaoException;
import com.deshko.task4.exception.ServiceException;
import com.deshko.task4.service.ProductService;
import java.util.List;
import java.util.Optional;

public class ProductServiceImpl implements ProductService {
    private static final ProductServiceImpl INSTANCE = new ProductServiceImpl();

    private ProductServiceImpl() {}

    public static ProductServiceImpl getInstance() {
        return INSTANCE;
    }

    @Override
    public List<Product> getAllProducts() throws ServiceException {
        try {
            return ProductDaoImpl.getInstance().findAll();
        } catch (DaoException e) {
            throw new ServiceException("Failed to load products catalog", e);
        }
    }

    @Override
    public Optional<Product> getProductById(Long id) throws ServiceException {
        try {
            return ProductDaoImpl.getInstance().findById(id);
        } catch (DaoException e) {
            throw new ServiceException("Failed to find product with id: " + id, e);
        }
    }

    @Override
    public boolean addProduct(Product product) throws ServiceException {
        if (product.getPrice().doubleValue() < 0) {
            throw new ServiceException("Product price cannot be negative!");
        }
        try {
            product.setActive(true);
            return ProductDaoImpl.getInstance().create(product);
        } catch (DaoException e) {
            throw new ServiceException("Failed to add new product", e);
        }
    }

    @Override
    public boolean deleteProduct(Long id) throws ServiceException {
        try {
            return ProductDaoImpl.getInstance().delete(id);
        } catch (DaoException e) {
            throw new ServiceException("Failed to delete product", e);
        }
    }

    @Override
    public Product updateProduct(Product product) throws ServiceException {
        try {
            return ProductDaoImpl.getInstance().update(product);
        } catch (DaoException e) {
            throw new ServiceException("Failed to update product settings", e);
        }
    }
}
