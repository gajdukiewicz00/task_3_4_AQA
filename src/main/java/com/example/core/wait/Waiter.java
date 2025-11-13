package com.example.core.wait;

import com.example.core.config.Config;
import com.example.core.driver.DriverFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public final class Waiter {
    private Waiter() {}

    public static WebDriverWait waitDefault() {
        return new WebDriverWait(DriverFactory.get(), Duration.ofSeconds(Config.defaultTimeoutSec()));
    }

    public static void domReady() {
        waitDefault().until(d -> "complete".equals(
                ((JavascriptExecutor)d).executeScript("return document.readyState")));
    }

    public static WebElement visible(By by) {
        return waitDefault().until(ExpectedConditions.visibilityOfElementLocated(by));
    }

    public static void scrollIntoView(WebElement el) {
        ((JavascriptExecutor) DriverFactory.get())
                .executeScript("arguments[0].scrollIntoView({block:'center', inline:'center'});", el);
    }

    public static WebElement clickable(WebElement el) {
        return waitDefault().until(ExpectedConditions.elementToBeClickable(el));
    }
}
