package com.deshko.task4.command.impl;

import com.deshko.task4.command.Command;
import com.deshko.task4.command.Router;
import jakarta.servlet.http.HttpServletRequest;

public class ToAddProductPageCommand implements Command {
    private static final String PATH_ADD_PRODUCT_PAGE = "/WEB-INF/pages/add_product.jsp";

    @Override
    public Router execute(HttpServletRequest request) {
        return new Router(PATH_ADD_PRODUCT_PAGE, Router.Type.FORWARD);
    }
}
