package com.deshko.task4.service;

import java.util.Optional;
import com.deshko.task4.entity.User;
import com.deshko.task4.exception.ServiceException;

public interface UserService {
    Optional<User> login(String login, String password) throws ServiceException;
    boolean register(String login, String password, String confirmPassword) throws ServiceException;
    boolean updatePassword(Long userId, String newPassword) throws ServiceException;
}
