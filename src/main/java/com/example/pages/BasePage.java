package com.example.pages;

import com.example.core.driver.DriverFactory;
import com.example.core.wait.Waiter;
import com.example.pages.components.CookieBanner;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;

import com.example.core.config.Config;

public abstract class BasePage {
    protected final WebDriver driver;
    public final CookieBanner cookieBanner;

    protected BasePage() {
        this.driver = DriverFactory.get();
        this.cookieBanner = new CookieBanner(driver);
        PageFactory.initElements(new AjaxElementLocatorFactory(driver, Config.defaultTimeoutSec()), this);
        Waiter.domReady();
    }

    public String title() {
        return driver.getTitle();
    }
}
