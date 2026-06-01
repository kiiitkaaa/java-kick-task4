package com.deshko.task4.command;

import com.deshko.task4.command.impl.*;

public enum CommandType {
    LOGIN(new LoginCommand()),
    LOGOUT(new LogoutCommand()),
    TO_SIGN_UP_PAGE(new ToSignUpPageCommand()),
    SIGN_UP(new SignUpCommand()),
    UPDATE_PASSWORD(new UpdatePasswordCommand()),
    VIEW_PRODUCTS(new ViewProductsCommand()),
    ADD_PRODUCT(new AddProductCommand()),
    DELETE_PRODUCT(new DeleteProductCommand()),
    MAKE_ORDER(new MakeOrderCommand()),
    VIEW_MY_ORDERS(new ViewMyOrdersCommand()),
    CANCEL_ORDER(new CancelOrderCommand());

    private final Command command;

    CommandType(Command command) {
        this.command = command;
    }

    public Command getCommand() {
        return command;
    }

    public static Command defineCommand(String commandStr) {
        if (commandStr == null || commandStr.isEmpty()) {
            return VIEW_PRODUCTS.getCommand();
        }
        try {
            return CommandType.valueOf(commandStr.toUpperCase()).getCommand();
        } catch (IllegalArgumentException e) {
            return VIEW_PRODUCTS.getCommand();
        }
    }
}
