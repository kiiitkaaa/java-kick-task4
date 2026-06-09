package com.deshko.task4.command.impl;

import com.deshko.task4.command.Command;
import com.deshko.task4.command.Router;
import com.deshko.task4.exception.CommandException;
import com.deshko.task4.exception.ServiceException;
import com.deshko.task4.service.impl.UserServiceImpl;
import jakarta.servlet.http.HttpServletRequest;

public class SignUpCommand implements Command {
    private static final String PATH_INDEX = "/index.jsp";
    private static final String PATH_SIGNUP = "/WEB-INF/pages/signup.jsp";

    @Override
    public Router execute(HttpServletRequest request) throws CommandException {
        String login = request.getParameter("login");
        String pass = request.getParameter("password");
        String passConfirm = request.getParameter("confirmPassword");

        try {
            boolean isRegistered = UserServiceImpl.getInstance().register(login, pass, passConfirm);
            if (isRegistered) {
                return new Router(request.getContextPath() + PATH_INDEX, Router.Type.REDIRECT);
            } else {
                request.setAttribute("errorMessage", "Registration error");
                return new Router(PATH_SIGNUP, Router.Type.FORWARD);
            }
        } catch (ServiceException e) {
            request.setAttribute("errorMessage", e.getMessage());
            return new Router(PATH_SIGNUP, Router.Type.FORWARD);
        }
    }
}
