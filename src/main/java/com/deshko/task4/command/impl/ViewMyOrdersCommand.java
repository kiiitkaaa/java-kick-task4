package com.deshko.task4.command.impl;

import com.deshko.task4.command.Command;
import com.deshko.task4.command.Router;
import com.deshko.task4.entity.Order;
import com.deshko.task4.entity.User;
import com.deshko.task4.exception.CommandException;
import com.deshko.task4.exception.ServiceException;
import com.deshko.task4.service.impl.OrderServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

public class ViewMyOrdersCommand implements Command {
    @Override
    public Router execute(HttpServletRequest request) throws CommandException {
        User currentUser = (User) request.getSession().getAttribute("user");

        if (currentUser == null) {
            return new Router(request.getContextPath() + "/index.jsp", Router.Type.REDIRECT);
        }

        try {
            List<Order> myOrders = OrderServiceImpl.getInstance().getOrdersByUserId(currentUser.getId());
            request.setAttribute("orders", myOrders);
            return new Router("/WEB-INF/pages/orders.jsp", Router.Type.FORWARD);
        } catch (ServiceException e) {
            throw new CommandException("Failed to fetch user orders", e);
        }
    }
}