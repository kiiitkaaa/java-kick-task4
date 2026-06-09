package com.deshko.task4.command.impl;

import com.deshko.task4.command.Command;
import com.deshko.task4.command.Router;
import com.deshko.task4.exception.CommandException;
import com.deshko.task4.exception.ServiceException;
import com.deshko.task4.service.impl.OrderServiceImpl;
import jakarta.servlet.http.HttpServletRequest;

public class CancelOrderCommand implements Command {
    private static final String PATH_ORDERS = "/controller?command=VIEW_MY_ORDERS";

    @Override
    public Router execute(HttpServletRequest request) throws CommandException {
        try {
            Long orderId = Long.valueOf(request.getParameter("orderId"));
            OrderServiceImpl.getInstance().cancelOrder(orderId);

            return new Router(request.getContextPath() + PATH_ORDERS, Router.Type.REDIRECT);
        } catch (ServiceException | NumberFormatException e) {
            throw new CommandException("Failed to cancel order.", e);
        }
    }
}
