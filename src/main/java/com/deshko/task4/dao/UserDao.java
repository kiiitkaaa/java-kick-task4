package com.deshko.task4.dao;

import java.util.Optional;
import com.deshko.task4.entity.User;
import com.deshko.task4.exception.DaoException;

public interface UserDao {
    Optional<User> findByLogin(String login) throws DaoException;
}
