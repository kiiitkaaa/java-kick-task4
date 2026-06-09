package com.deshko.task4.command.impl;

import com.deshko.task4.command.Command;
import com.deshko.task4.command.Router;
import com.deshko.task4.entity.User;
import com.deshko.task4.exception.CommandException;
import com.deshko.task4.exception.ServiceException;
import com.deshko.task4.service.impl.UserServiceImpl;
import jakarta.servlet.http.HttpServletRequest;

public class UpdatePasswordCommand implements Command {
    private static final String PATH_INDEX = "/index.jsp";
    private static final String PATH_SETTINGS = "/WEB-INF/pages/settings.jsp";

    @Override
    public Router execute(HttpServletRequest request) throws CommandException {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return new Router(request.getContextPath() + PATH_INDEX, Router.Type.REDIRECT);
        }

        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");

        if (newPassword == null || !newPassword.equals(confirmPassword)) {
            request.setAttribute("errorMessage", "Passwords do not match");
            return new Router(PATH_SETTINGS, Router.Type.FORWARD);
        }

        try {
            boolean updated = UserServiceImpl.getInstance().updatePassword(user.getId(), newPassword);

            if (updated) {
                request.setAttribute("successMessage", "Password successfully updated");
            } else {
                request.setAttribute("errorMessage", "Error updating profile");
            }
            return new Router(PATH_SETTINGS, Router.Type.FORWARD);
        } catch (ServiceException e) {
            throw new CommandException("Error while updating password", e);
        }
    }
}
