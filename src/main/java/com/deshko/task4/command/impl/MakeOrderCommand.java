package com.deshko.task4.command.impl;

import com.deshko.task4.command.Command;
import com.deshko.task4.command.Router;
import com.deshko.task4.entity.User;
import com.deshko.task4.exception.CommandException;
import com.deshko.task4.exception.ServiceException;
import com.deshko.task4.service.impl.OrderServiceImpl;
import jakarta.servlet.http.HttpServletRequest;

public class MakeOrderCommand implements Command {
    @Override
    public Router execute(HttpServletRequest request) throws CommandException {
        Long productId = Long.valueOf(request.getParameter("productId"));
        User currentUser = (User) request.getSession().getAttribute("user");

        if (currentUser == null) {
            return new Router(request.getContextPath() + "/index.jsp", Router.Type.REDIRECT);
        }

        try {
            OrderServiceImpl.getInstance().makeOrder(currentUser.getId(), productId);
            return new Router(request.getContextPath() + "/controller?command=VIEW_MY_ORDERS", Router.Type.REDIRECT);
        } catch (ServiceException e) {
            throw new CommandException("Failed to make order", e);
        }
    }
}
