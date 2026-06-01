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
    @Override
    public Router execute(HttpServletRequest request) throws CommandException {
        String login = request.getParameter("login");
        String pass = request.getParameter("password");

        try {
            Optional<User> userOpt = UserServiceImpl.getInstance().login(login, pass);

            if (userOpt.isPresent()) {
                HttpSession session = request.getSession();
                session.setAttribute("user", userOpt.get());

                return new Router(request.getContextPath() + "/controller?command=VIEW_PRODUCTS", Router.Type.REDIRECT);
            } else {
                request.setAttribute("errorMessage", "Неверный логин или пароль");
                return new Router("/WEB-INF/pages/login.jsp", Router.Type.FORWARD);
            }
        } catch (ServiceException e) {
            throw new CommandException("Error executing LoginCommand", e);
        }
    }
}
