package com.deshko.task4.command.impl;

import com.deshko.task4.command.Command;
import com.deshko.task4.command.Router;
import com.deshko.task4.exception.CommandException;
import com.deshko.task4.exception.ServiceException;
import com.deshko.task4.service.impl.ProductServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DeleteProductCommand implements Command {
    private static final Logger logger = LogManager.getLogger(DeleteProductCommand.class);
    private static final String PATH_PRODUCTS = "/controller?command=VIEW_PRODUCTS";

    @Override
    public Router execute(HttpServletRequest request) throws CommandException {
        String productIdStr = request.getParameter("productId");
        logger.info("Attempting to delete product with ID: '{}'", productIdStr);

        try {
            Long productId = Long.valueOf(productIdStr);
            ProductServiceImpl.getInstance().deleteProduct(productId);
            logger.info("Product ID '{}' was successfully deleted", productId);
            return new Router(request.getContextPath() + PATH_PRODUCTS, Router.Type.REDIRECT);
        } catch (ServiceException | NumberFormatException e) {
            logger.error("Failed to delete product with ID: '{}'. Reason: {}",
                    productIdStr, e.getMessage(), e);
            throw new CommandException("Failed to delete product. Invalid ID format or database error.", e);
        }
    }
}