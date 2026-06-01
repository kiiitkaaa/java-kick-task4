package com.deshko.task4.dao;

import java.util.List;
import com.deshko.task4.entity.Product;
import com.deshko.task4.exception.DaoException;

public interface ProductDao {
    List<Product> findActiveProducts() throws DaoException;
}
