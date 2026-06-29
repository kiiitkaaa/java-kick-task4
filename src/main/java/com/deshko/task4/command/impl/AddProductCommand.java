package com.deshko.task4.command.impl;

import com.deshko.task4.command.Command;
import com.deshko.task4.command.Router;
import com.deshko.task4.entity.Product;
import com.deshko.task4.exception.CommandException;
import com.deshko.task4.exception.ServiceException;
import com.deshko.task4.service.impl.ProductServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AddProductCommand implements Command {
    private static final Logger logger = LogManager.getLogger(AddProductCommand.class);
    private static final String PATH_PRODUCTS = "/controller?command=VIEW_PRODUCTS";
    private static final String PATH_ADD_PRODUCT_PAGE = "/WEB-INF/pages/add_product.jsp";

    @Override
    public Router execute(HttpServletRequest request) throws CommandException {
        String name = request.getParameter("name");
        String description = request.getParameter("description");
        String priceStr = request.getParameter("price");

        logger.info("Attempting to add new product. Name: '{}', Price: '{}'", name, priceStr);

        try {
            Product product = new Product();
            product.setName(name);
            product.setDescription(description);
            product.setPrice(new BigDecimal(priceStr));
            ProductServiceImpl.getInstance().addProduct(product);

            logger.info("Product '{}' successfully added", name);
            return new Router(request.getContextPath() + PATH_PRODUCTS, Router.Type.REDIRECT);
        } catch (ServiceException | NumberFormatException e) {
            logger.error("Failed to add product '{}'. Reason: {}", name, e.getMessage(), e);
            request.setAttribute("errorMessage",
                    "Error adding the product. Check that the price is correct");
            return new Router(PATH_ADD_PRODUCT_PAGE, Router.Type.FORWARD);
        }
    }
}
