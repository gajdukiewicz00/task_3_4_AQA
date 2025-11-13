package com.example.core.driver;

import com.example.core.config.Config;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.events.EventFiringDecorator;
import java.net.URL;
import java.time.Duration;

public final class DriverFactory {
    private static final ThreadLocal<WebDriver> TL = new ThreadLocal<>();

    public static void start() {
        WebDriver driver = Config.gridUrl().isBlank() ? local() : remote();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(0));
        driver.manage().window().maximize();
        driver = new EventFiringDecorator<>(new WebDriverEventLogger()).decorate(driver);
        TL.set(driver);
    }

    public static WebDriver get() { return TL.get(); }

    public static void stop() {
        var d = TL.get();
        if (d != null) { d.quit(); TL.remove(); }
    }

    private static WebDriver local() {
        String browser = Config.browser().toLowerCase();
        switch (browser) {
            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                if (Config.headless()) {
                    firefoxOptions.addArguments("-headless");
                }
                firefoxOptions.setAcceptInsecureCerts(true);
                return new FirefoxDriver(firefoxOptions);
            case "chrome":
            default:
                WebDriverManager.chromedriver().setup();
                ChromeOptions chromeOptions = new ChromeOptions();
                if (Config.headless()) chromeOptions.addArguments("--headless=new");
                chromeOptions.addArguments("--window-size=1920,1080", "--no-sandbox", "--disable-gpu");
                return new org.openqa.selenium.chrome.ChromeDriver(chromeOptions);
        }
    }

    private static WebDriver remote() {
        String browser = Config.browser().toLowerCase();
        MutableCapabilities caps;
        if ("firefox".equals(browser)) {
            caps = new FirefoxOptions();
        } else {
            caps = new ChromeOptions().addArguments("--window-size=1920,1080");
        }
        return new RemoteWebDriver(toUrl(Config.gridUrl()), caps);
    }

    private static URL toUrl(String s) {
        try { return new java.net.URI(s).toURL(); } catch (Exception e) { throw new RuntimeException(e); }
    }
}
