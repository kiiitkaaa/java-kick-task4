package com.deshko.task4.command;

public class Router {
    public enum Type {
        FORWARD, REDIRECT
    }

    private String page;
    private Type type = Type.FORWARD;

    public Router(String page) {
        this.page = page;
    }

    public Router(String page, Type type) {
        this.page = page;
        this.type = type;
    }

    public String getPage() {
        return page;
    }

    public Type getType() {
        return type;
    }
}