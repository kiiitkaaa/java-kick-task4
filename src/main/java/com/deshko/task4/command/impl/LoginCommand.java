package com.deshko.task4.command.impl;

import com.deshko.task4.command.Command;
import com.deshko.task4.command.Router;
import com.deshko.task4.entity.User;
import com.deshko.task4.exception.CommandException;
import com.deshko.task4.exception.ServiceException;
import com.deshko.task4.service.impl.UserServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.util.Optional;

public class LoginCommand implements Command {
    private static final String PATH_PRODUCTS = "/controller?command=VIEW_PRODUCTS";
    private static final String PATH_LOGIN = "/WEB-INF/pages/login.jsp";

    @Override
    public Router execute(HttpServletRequest request) throws CommandException {
        String login = request.getParameter("login");
        String pass = request.getParameter("password");

        try {
            Optional<User> userOpt = UserServiceImpl.getInstance().login(login, pass);

            if (userOpt.isPresent()) {
                HttpSession session = request.getSession();
                session.setAttribute("user", userOpt.get());

                return new Router(request.getContextPath() + PATH_PRODUCTS, Router.Type.REDIRECT);
            } else {
                request.setAttribute("errorMessage", "Invalid username or password");
                return new Router(PATH_LOGIN, Router.Type.FORWARD);
            }
        } catch (ServiceException e) {
            throw new CommandException("Error executing LoginCommand", e);
        }
    }
}
