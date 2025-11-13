package com.example.pages;

import com.example.core.driver.DriverFactory;
import com.example.core.wait.Waiter;
import com.example.pages.components.CookieBanner;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;

public abstract class BasePage {
    protected final WebDriver driver;

    protected BasePage() {
        this.driver = DriverFactory.get();
        PageFactory.initElements(new AjaxElementLocatorFactory(driver, 20), this);
        Waiter.domReady();
        CookieBanner.acceptIfPresent();
    }

    public String title() { return driver.getTitle(); }
}
