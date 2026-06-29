package com.deshko.task4.command.impl;

import com.deshko.task4.command.Command;
import com.deshko.task4.command.Router;
import com.deshko.task4.entity.User;
import com.deshko.task4.exception.CommandException;
import com.deshko.task4.exception.ServiceException;
import com.deshko.task4.service.impl.OrderServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MakeOrderCommand implements Command {
    private static final Logger logger = LogManager.getLogger(MakeOrderCommand.class);
    private static final String PATH_INDEX = "/index.jsp";
    private static final String PATH_ORDERS = "/controller?command=VIEW_MY_ORDERS";

    @Override
    public Router execute(HttpServletRequest request) throws CommandException {
        String productIdStr = request.getParameter("productId");
        User currentUser = (User) request.getSession().getAttribute("user");
        logger.info("Attempting to create order. ProductID: '{}', UserID: '{}'",
                productIdStr, (currentUser != null ? currentUser.getId() : "null"));

        if (currentUser == null) {
            logger.warn("Unauthorized attempt to make an order. Redirecting to index.");
            return new Router(request.getContextPath() + PATH_INDEX, Router.Type.REDIRECT);
        }

        try {
            Long productId = Long.valueOf(productIdStr);
            OrderServiceImpl.getInstance().makeOrder(currentUser.getId(), productId);
            logger.info("Order successfully created for User ID: '{}', Product ID: '{}'",
                    currentUser.getId(), productId);
            return new Router(request.getContextPath() + PATH_ORDERS, Router.Type.REDIRECT);
        } catch (ServiceException e) {
            logger.error("Failed to make order. User: '{}', Product: '{}', Error: {}",
                    currentUser.getId(), productIdStr, e.getMessage(), e);
            throw new CommandException("Failed to make order", e);
        }
    }
}
