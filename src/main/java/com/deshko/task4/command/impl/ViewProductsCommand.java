package com.deshko.task4.command.impl;

import com.deshko.task4.command.Command;
import com.deshko.task4.command.Router;
import com.deshko.task4.entity.Product;
import com.deshko.task4.exception.CommandException;
import com.deshko.task4.exception.ServiceException;
import com.deshko.task4.service.impl.ProductServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ViewProductsCommand implements Command {
    private static final Logger logger = LogManager.getLogger(ViewProductsCommand.class);
    private static final String PATH_CATALOG = "/WEB-INF/pages/catalog.jsp";

    @Override
    public Router execute(HttpServletRequest request) throws CommandException {
        logger.debug("Attempting to fetch all products for catalog page.");

        try {
            List<Product> products = ProductServiceImpl.getInstance().getAllProducts();
            logger.debug("Successfully fetched {} products.",
                    (products != null ? products.size() : 0));
            request.setAttribute("products", products);
            return new Router(PATH_CATALOG, Router.Type.FORWARD);
        } catch (ServiceException e) {
            logger.error("Error executing ViewProductsCommand: {}", e.getMessage(), e);
            throw new CommandException("Error executing ViewProductsCommand", e);
        }
    }
}
