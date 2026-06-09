package com.deshko.task4.service.impl;

import com.deshko.task4.dao.impl.UserDaoImpl;
import com.deshko.task4.entity.Role;
import com.deshko.task4.entity.User;
import com.deshko.task4.exception.DaoException;
import com.deshko.task4.exception.ServiceException;
import com.deshko.task4.service.UserService;
import com.deshko.task4.util.PasswordHasher;
import java.util.Optional;

public class UserServiceImpl implements UserService {
    private static final UserServiceImpl INSTANCE = new UserServiceImpl();

    private UserServiceImpl() {}

    public static UserServiceImpl getInstance() {
        return INSTANCE;
    }

    @Override
    public Optional<User> login(String login, String password) throws ServiceException {
        try {
            Optional<User> userOpt = UserDaoImpl.getInstance().findByLogin(login);
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                String inputHash = PasswordHasher.hash(password);
                if (user.getPasswordHash().equals(inputHash)) {
                    return Optional.of(user);
                }
            }
            return Optional.empty();
        } catch (DaoException e) {
            throw new ServiceException("Error during login process", e);
        }
    }

    @Override
    public boolean register(String login, String password, String confirmPassword) throws ServiceException {
        if (!password.equals(confirmPassword)) {
            throw new ServiceException("Passwords do not match!");
        }
        try {
            if (UserDaoImpl.getInstance().findByLogin(login).isPresent()) {
                throw new ServiceException("Login '" + login + "' is already taken!");
            }

            User user = new User();
            user.setLogin(login);
            user.setPasswordHash(PasswordHasher.hash(password));
            user.setRole(Role.CLIENT);

            return UserDaoImpl.getInstance().create(user);
        } catch (DaoException e) {
            throw new ServiceException("Error during user registration", e);
        }
    }

    @Override
    public boolean updatePassword(Long userId, String newPassword) throws ServiceException {
        try {
            Optional<User> userOpt = UserDaoImpl.getInstance().findById(userId);
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                user.setPasswordHash(PasswordHasher.hash(newPassword));
                UserDaoImpl.getInstance().update(user);
                return true;
            }
            return false;
        } catch (DaoException e) {
            throw new ServiceException("Error updating user profile", e);
        }
    }
}
