package com.deshko.task4.command.impl;

import com.deshko.task4.command.Command;
import com.deshko.task4.command.Router;
import com.deshko.task4.exception.CommandException;
import com.deshko.task4.exception.ServiceException;
import com.deshko.task4.service.impl.UserServiceImpl;
import jakarta.servlet.http.HttpServletRequest;

public class SignUpCommand implements Command {
    @Override
    public Router execute(HttpServletRequest request) throws CommandException {
        String login = request.getParameter("login");
        String pass = request.getParameter("password");
        String passConfirm = request.getParameter("confirmPassword");

        try {
            boolean isRegistered = UserServiceImpl.getInstance().register(login, pass, passConfirm);
            if (isRegistered) {
                return new Router(request.getContextPath() + "/index.jsp", Router.Type.REDIRECT);
            } else {
                request.setAttribute("errorMessage", "Ошибка при регистрации");
                return new Router("/WEB-INF/pages/signup.jsp", Router.Type.FORWARD);
            }
        } catch (ServiceException e) {
            request.setAttribute("errorMessage", e.getMessage());
            return new Router("/WEB-INF/pages/signup.jsp", Router.Type.FORWARD);
        }
    }
}
