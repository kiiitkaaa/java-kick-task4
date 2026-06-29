package com.deshko.task4.service.impl;

import com.deshko.task4.dao.impl.UserDaoImpl;
import com.deshko.task4.entity.Role;
import com.deshko.task4.entity.User;
import com.deshko.task4.exception.DaoException;
import com.deshko.task4.exception.ServiceException;
import com.deshko.task4.service.UserService;
import com.deshko.task4.util.PasswordHasher;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.Optional;

public class UserServiceImpl implements UserService {
    private static final Logger logger = LogManager.getLogger(UserServiceImpl.class);
    private static final UserServiceImpl INSTANCE = new UserServiceImpl();

    private UserServiceImpl() {}

    public static UserServiceImpl getInstance() {
        return INSTANCE;
    }

    @Override
    public Optional<User> login(String login, String password) throws ServiceException {
        logger.info("Login attempt for user={}", login);

        try {
            Optional<User> userOpt = UserDaoImpl.getInstance().findByLogin(login);
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                String inputHash = PasswordHasher.hash(password);
                if (user.getPasswordHash().equals(inputHash)) {
                    logger.info("User logged in successfully: {}", login);
                    return Optional.of(user);
                }
            }
            logger.warn("Login failed");
            return Optional.empty();
        } catch (DaoException e) {
            logger.error("Error during login process for user={}", login, e);
            throw new ServiceException("Error during login process", e);
        }
    }

    @Override
    public boolean register(String login, String password, String confirmPassword) throws ServiceException {
        logger.info("Registration attempt for user={}", login);

        if (!password.equals(confirmPassword)) {
            logger.warn("Registration failed: passwords do not match for user={}", login);
            throw new ServiceException("Passwords do not match!");
        }

        try {
            if (UserDaoImpl.getInstance().findByLogin(login).isPresent()) {
                logger.warn("Registration failed: login already taken ({})", login);
                throw new ServiceException("Login '" + login + "' is already taken!");
            }

            User user = new User();
            user.setLogin(login);
            user.setPasswordHash(PasswordHasher.hash(password));
            user.setRole(Role.CLIENT);

            boolean created = UserDaoImpl.getInstance().create(user);
            logger.info("User registered successfully: {}", login);
            return created;
        } catch (DaoException e) {
            logger.error("Error during user registration: {}", login, e);
            throw new ServiceException("Error during user registration", e);
        }
    }

    @Override
    public boolean updatePassword(Long userId, String newPassword) throws ServiceException {
        logger.info("Password update request for userId={}", userId);

        try {
            Optional<User> userOpt = UserDaoImpl.getInstance().findById(userId);
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                user.setPasswordHash(PasswordHasher.hash(newPassword));
                UserDaoImpl.getInstance().update(user);
                logger.info("Password updated successfully for userId={}", userId);
                return true;
            }

            logger.warn("Password update failed");
            return false;
        } catch (DaoException e) {
            logger.error("Error updating password for userId={}", userId, e);
            throw new ServiceException("Error updating user profile", e);
        }
    }
}
