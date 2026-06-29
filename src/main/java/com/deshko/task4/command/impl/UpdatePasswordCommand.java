package com.deshko.task4.command.impl;

import com.deshko.task4.command.Command;
import com.deshko.task4.command.Router;
import com.deshko.task4.entity.User;
import com.deshko.task4.exception.CommandException;
import com.deshko.task4.exception.ServiceException;
import com.deshko.task4.service.impl.UserServiceImpl;
import com.deshko.task4.util.UserValidator;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UpdatePasswordCommand implements Command {
    private static final Logger logger = LogManager.getLogger(UpdatePasswordCommand.class);
    private static final String PATH_INDEX = "/index.jsp";
    private static final String PATH_SETTINGS = "/WEB-INF/pages/settings.jsp";

    @Override
    public Router execute(HttpServletRequest request) throws CommandException {
        User user = (User) request.getSession().getAttribute("user");
        logger.info("Attempting to update password for user ID: '{}'",
                (user != null ? user.getId() : "unknown"));

        if (user == null) {
            logger.warn("Unauthorized password update attempt.");
            return new Router(request.getContextPath() + PATH_INDEX, Router.Type.REDIRECT);
        }

        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");

        if (newPassword == null || !newPassword.equals(confirmPassword)) {
            logger.warn("Password update failed: Passwords do not match for user ID: '{}'",
                    user.getId());
            request.setAttribute("errorMessage", "Passwords do not match");
            return new Router(PATH_SETTINGS, Router.Type.FORWARD);
        }

        if (!UserValidator.getInstance().isValidPassword(newPassword)) {
            logger.warn("Password update failed: Validation failed for user ID: '{}'",
                    user.getId());
            request.setAttribute("errorMessage",
                    "The password must contain at least 6 characters, including Latin letters and numbers");
            return new Router(PATH_SETTINGS, Router.Type.FORWARD);
        }

        try {
            boolean updated = UserServiceImpl.getInstance().updatePassword(user.getId(), newPassword);

            if (updated) {
                logger.info("Password successfully updated for user ID: '{}'", user.getId());
                request.setAttribute("successMessage", "Password successfully updated");
            } else {
                logger.warn("Password update failed: Service returned false for user ID: '{}'",
                        user.getId());
                request.setAttribute("errorMessage", "Error updating profile");
            }
            return new Router(PATH_SETTINGS, Router.Type.FORWARD);
        } catch (ServiceException e) {
            logger.error("System error during password update for user ID: '{}'. Error: {}",
                    user.getId(), e.getMessage(), e);
            throw new CommandException("Error while updating password", e);
        }
    }
}