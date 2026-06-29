package com.deshko.task4.command.impl;

import com.deshko.task4.command.Command;
import com.deshko.task4.command.Router;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ToSignUpPageCommand implements Command {
    private static final Logger logger = LogManager.getLogger(ToSignUpPageCommand.class);
    private static final String PATH_SIGNUP = "/WEB-INF/pages/signup.jsp";

    @Override
    public Router execute(HttpServletRequest request) {
        logger.debug("ToSignUpPageCommand");
        return new Router(PATH_SIGNUP, Router.Type.FORWARD);
    }
}
