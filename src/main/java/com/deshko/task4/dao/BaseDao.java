package com.deshko.task4.dao;

import java.util.List;
import java.util.Optional;
import com.deshko.task4.entity.AbstractEntity;
import com.deshko.task4.exception.DaoException;

public interface BaseDao<T extends AbstractEntity> {
    List<T> findAll() throws DaoException;
    Optional<T> findById(Long id) throws DaoException;
    boolean delete(Long id) throws DaoException;
    boolean create(T entity) throws DaoException;
    T update(T entity) throws DaoException;
}
