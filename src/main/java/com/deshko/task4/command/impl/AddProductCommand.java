package com.deshko.task4.command.impl;

import com.deshko.task4.command.Command;
import com.deshko.task4.command.Router;
import com.deshko.task4.entity.Product;
import com.deshko.task4.exception.CommandException;
import com.deshko.task4.exception.ServiceException;
import com.deshko.task4.service.impl.ProductServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import java.math.BigDecimal;

public class AddProductCommand implements Command {
    @Override
    public Router execute(HttpServletRequest request) throws CommandException {
        String name = request.getParameter("name");
        String description = request.getParameter("description");
        String priceStr = request.getParameter("price");

        try {
            Product product = new Product();
            product.setName(name);
            product.setDescription(description);
            product.setPrice(new BigDecimal(priceStr));
            ProductServiceImpl.getInstance().addProduct(product);

            return new Router(request.getContextPath() + "/controller?command=VIEW_PRODUCTS", Router.Type.REDIRECT);

        } catch (ServiceException | NumberFormatException e) {
            request.setAttribute("errorMessage", "Ошибка добавления товара. Проверьте корректность цены.");
            return new Router("/WEB-INF/pages/admin/add_product.jsp", Router.Type.FORWARD);
        }
    }
}
