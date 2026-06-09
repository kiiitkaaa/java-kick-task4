package com.deshko.task4.command.impl;

import com.deshko.task4.command.Command;
import com.deshko.task4.command.Router;
import com.deshko.task4.exception.CommandException;
import com.deshko.task4.exception.ServiceException;
import com.deshko.task4.service.impl.ProductServiceImpl;
import jakarta.servlet.http.HttpServletRequest;

public class DeleteProductCommand implements Command {
    private static final String PATH_PRODUCTS = "/controller?command=VIEW_PRODUCTS";

    @Override
    public Router execute(HttpServletRequest request) throws CommandException {
        try {
            Long productId = Long.valueOf(request.getParameter("productId"));
            ProductServiceImpl.getInstance().deleteProduct(productId);

            return new Router(request.getContextPath() + PATH_PRODUCTS, Router.Type.REDIRECT);
        } catch (ServiceException | NumberFormatException e) {
            throw new CommandException("Failed to delete product. Invalid ID format or database error.", e);
        }
    }
}