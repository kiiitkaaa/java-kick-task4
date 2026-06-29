package com.deshko.task4.controller;

import java.io.*;
import com.deshko.task4.command.Command;
import com.deshko.task4.command.CommandType;
import com.deshko.task4.command.Router;
import com.deshko.task4.exception.CommandException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@WebServlet(name = "helloServlet", urlPatterns = {"/controller", "*.do"})
public class Controller extends HttpServlet {
    private static final Logger logger = LogManager.getLogger(Controller.class);

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String commandStr = request.getParameter("command");
        logger.debug("Processing request. Method: '{}', Command: '{}'",
                request.getMethod(), commandStr);
        Command command = CommandType.defineCommand(commandStr);

        try {
            Router router = command.execute(request);
            String page = router.getPage();
            Router.Type routerType = router.getType();

            logger.info("Command '{}' executed successfully. Routing to page: '{}' via '{}'",
                    commandStr, page, routerType);

            if (routerType == Router.Type.FORWARD) {
                request.getRequestDispatcher(page).forward(request, response);
            } else {
                response.sendRedirect(page);
            }

        } catch (CommandException e) {
            logger.error("Global application error during execution of command: '{}'. Message: {}",
                    commandStr, e.getMessage(), e);
            throw new ServletException("Application error during command execution", e);
        }
    }
}