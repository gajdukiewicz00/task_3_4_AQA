package com.example.pages;

public final class PageFactoryManager {
    private PageFactoryManager() {}
    public static <T extends BasePage> T get(Class<T> page) {
        try { return page.getDeclaredConstructor().newInstance(); }
        catch (Exception e) { throw new RuntimeException("Cannot init page: " + page.getSimpleName(), e); }
    }
}
