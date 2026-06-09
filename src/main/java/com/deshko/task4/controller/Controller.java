package com.deshko.task4.controller;

import java.io.*;

import com.deshko.task4.command.Command;
import com.deshko.task4.command.CommandType;
import com.deshko.task4.command.Router;
import com.deshko.task4.exception.CommandException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet(name = "helloServlet", urlPatterns = {"/controller", "*.do"})
public class Controller extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String commandStr = request.getParameter("command");
        Command command = CommandType.defineCommand(commandStr);

        try {
            Router router = command.execute(request);
            String page = router.getPage();

            if (router.getType() == Router.Type.FORWARD) {
                request.getRequestDispatcher(page).forward(request, response);
            } else {
                response.sendRedirect(page);
            }

        } catch (CommandException e) {
            throw new ServletException("Application error during command execution", e);
        }
    }
}