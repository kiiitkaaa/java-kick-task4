package com.deshko.task4.command.impl;

import com.deshko.task4.command.Command;
import com.deshko.task4.command.Router;
import jakarta.servlet.http.HttpServletRequest;

public class ToSignUpPageCommand implements Command {
    private static final String PATH_SIGNUP = "/WEB-INF/pages/signup.jsp";

    @Override
    public Router execute(HttpServletRequest request) {
        return new Router(PATH_SIGNUP, Router.Type.FORWARD);
    }
}
