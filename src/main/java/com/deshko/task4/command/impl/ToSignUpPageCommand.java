package com.deshko.task4.command.impl;

import com.deshko.task4.command.Command;
import com.deshko.task4.command.Router;
import jakarta.servlet.http.HttpServletRequest;

public class ToSignUpPageCommand implements Command {
    @Override
    public Router execute(HttpServletRequest request) {
        return new Router("/WEB-INF/pages/signup.jsp", Router.Type.FORWARD);
    }
}
