package com.deshko.task4.command.impl;

import com.deshko.task4.command.Command;
import com.deshko.task4.command.Router;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ToAddProductPageCommand implements Command {
    private static final Logger logger = LogManager.getLogger(ToAddProductPageCommand.class);
    private static final String PATH_ADD_PRODUCT_PAGE = "/WEB-INF/pages/add_product.jsp";

    @Override
    public Router execute(HttpServletRequest request) {
        logger.debug("ToAddProductPageCommand");
        return new Router(PATH_ADD_PRODUCT_PAGE, Router.Type.FORWARD);
    }
}
