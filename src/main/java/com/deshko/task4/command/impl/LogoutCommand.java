package com.deshko.task4.command.impl;

import com.deshko.task4.command.Command;
import com.deshko.task4.command.Router;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LogoutCommand implements Command {
    private static final Logger logger = LogManager.getLogger(LogoutCommand.class);
    private static final String PATH_INDEX = "/index.jsp";

    @Override
    public Router execute(HttpServletRequest request) {
        logger.debug("Logout command");
        request.getSession().invalidate();
        return new Router(request.getContextPath() + PATH_INDEX, Router.Type.REDIRECT);
    }
}
