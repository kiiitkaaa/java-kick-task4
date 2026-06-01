package com.deshko.task4.command.impl;

import com.deshko.task4.command.Command;
import com.deshko.task4.command.Router;
import com.deshko.task4.entity.Product;
import com.deshko.task4.exception.CommandException;
import com.deshko.task4.exception.ServiceException;
import com.deshko.task4.service.impl.ProductServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

public class ViewProductsCommand implements Command {
    @Override
    public Router execute(HttpServletRequest request) throws CommandException {
        try {
            List<Product> products = ProductServiceImpl.getInstance().getAllProducts();
            request.setAttribute("products", products);

            return new Router("/WEB-INF/pages/catalog.jsp", Router.Type.FORWARD);
        } catch (ServiceException e) {
            throw new CommandException("Error executing ViewProductsCommand", e);
        }
    }
}
