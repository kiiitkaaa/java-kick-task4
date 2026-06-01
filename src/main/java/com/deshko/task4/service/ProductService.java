package com.deshko.task4.service;

import java.util.List;
import java.util.Optional;
import com.deshko.task4.entity.Product;
import com.deshko.task4.exception.ServiceException;

public interface ProductService {
    List<Product> getAllProducts() throws ServiceException;
    Optional<Product> getProductById(Long id) throws ServiceException;
    boolean addProduct(Product product) throws ServiceException;
    boolean deleteProduct(Long id) throws ServiceException;
    Product updateProduct(Product product) throws ServiceException;
}
