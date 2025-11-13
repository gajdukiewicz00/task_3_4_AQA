package com.example.core.config;

public final class Config {
    private Config() {}
    public static String baseUrl() {
        return System.getProperty("baseUrl", "https://www.inmotionhosting.com/");
    }
    public static String browser() { return System.getProperty("browser", "chrome"); }
    public static boolean headless() { return Boolean.parseBoolean(System.getProperty("headless","true")); }
    public static String gridUrl() { return System.getProperty("gridUrl",""); }
    public static int defaultTimeoutSec() { return Integer.parseInt(System.getProperty("timeout","15")); }
}
