package com.deshko.task4.command.impl;

import com.deshko.task4.command.Command;
import com.deshko.task4.command.Router;
import com.deshko.task4.exception.CommandException;
import com.deshko.task4.exception.ServiceException;
import com.deshko.task4.service.impl.UserServiceImpl;
import com.deshko.task4.util.UserValidator;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SignUpCommand implements Command {
    private static final Logger logger = LogManager.getLogger(SignUpCommand.class);
    private static final String PATH_INDEX = "/index.jsp";
    private static final String PATH_SIGNUP = "/WEB-INF/pages/signup.jsp";

    @Override
    public Router execute(HttpServletRequest request) throws CommandException {
        String login = request.getParameter("login");
        String pass = request.getParameter("password");
        String passConfirm = request.getParameter("confirmPassword");

        logger.info("Attempting registration for login: '{}'", login);

        if (pass == null || !pass.equals(passConfirm)) {
            logger.warn("Registration failed: Passwords do not match for user '{}'", login);
            request.setAttribute("errorMessage", "Passwords do not match");
            return new Router(PATH_SIGNUP, Router.Type.FORWARD);
        }

        if (!UserValidator.getInstance().isValidPassword(pass)) {
            logger.warn("Registration failed: Password validation failed for user '{}'", login);
            request.setAttribute("errorMessage",
                    "Password must be at least 6 characters long, contain letters and numbers");
            return new Router(PATH_SIGNUP, Router.Type.FORWARD);
        }

        try {
            boolean isRegistered = UserServiceImpl.getInstance().register(login, pass, passConfirm);
            if (isRegistered) {
                logger.info("User '{}' successfully registered", login);
                return new Router(request.getContextPath() + PATH_INDEX, Router.Type.REDIRECT);
            } else {
                logger.warn("Registration failed: Service returned false for login '{}'", login);
                request.setAttribute("errorMessage", "Registration error");
                return new Router(PATH_SIGNUP, Router.Type.FORWARD);
            }
        } catch (ServiceException e) {
            logger.error("System error during registration for login '{}'. Error: {}",
                    login, e.getMessage(), e);
            request.setAttribute("errorMessage", e.getMessage());
            return new Router(PATH_SIGNUP, Router.Type.FORWARD);
        }
    }
}
