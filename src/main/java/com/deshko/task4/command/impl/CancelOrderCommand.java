package com.deshko.task4.command.impl;

import com.deshko.task4.command.Command;
import com.deshko.task4.command.Router;
import com.deshko.task4.exception.CommandException;
import com.deshko.task4.exception.ServiceException;
import com.deshko.task4.service.impl.OrderServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CancelOrderCommand implements Command {
    private static final Logger logger = LogManager.getLogger(CancelOrderCommand.class);
    private static final String PATH_ORDERS = "/controller?command=VIEW_MY_ORDERS";

    @Override
    public Router execute(HttpServletRequest request) throws CommandException {
        String orderIdStr = request.getParameter("orderId");
        logger.info("Attempting to cancel order with ID: '{}'", orderIdStr);

        try {
            Long orderId = Long.valueOf(orderIdStr);
            OrderServiceImpl.getInstance().cancelOrder(orderId);
            logger.info("Order ID '{}' was successfully canceled", orderId);
            return new Router(request.getContextPath() + PATH_ORDERS, Router.Type.REDIRECT);
        } catch (ServiceException | NumberFormatException e) {
            logger.error("Error occurred while canceling order ID: '{}'", orderIdStr, e);
            throw new CommandException("Failed to cancel order.", e);
        }
    }
}
