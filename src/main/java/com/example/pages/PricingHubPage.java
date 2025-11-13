package com.example.pages;

import com.example.core.config.Config;
import com.example.core.wait.Waiter;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import java.time.Duration;

public class PricingHubPage extends BasePage {

    @FindBy(css = "a[href*='shared-hosting']")
    private WebElement sharedHostingLink;

    @FindBy(css = "a[href*='vps-hosting']")
    private WebElement vpsHostingLink;

    /**
     * Service method: Navigate to Shared Hosting page
     * @return SharedHostingPage instance
     */
    public SharedHostingPage openSharedHosting() {
        try {
            if (sharedHostingLink != null && sharedHostingLink.isDisplayed()) {
                ((JavascriptExecutor) driver)
                        .executeScript("arguments[0].scrollIntoView({block:'center', inline:'center'});", sharedHostingLink);
                Waiter.clickable(sharedHostingLink);
                new Actions(driver).moveToElement(sharedHostingLink).pause(Duration.ofMillis(60)).click().perform();
            } else {
                driver.navigate().to(Config.baseUrl() + "shared-hosting");
            }
        } catch (Exception e) {
            driver.navigate().to(Config.baseUrl() + "shared-hosting");
        }
        return PageFactoryManager.get(SharedHostingPage.class);
    }

    /**
     * Service method: Navigate to VPS Hosting page
     * @return VpsHostingPage instance
     */
    public VpsHostingPage openVpsHosting() {
        try {
            if (vpsHostingLink != null && vpsHostingLink.isDisplayed()) {
                ((JavascriptExecutor) driver)
                        .executeScript("arguments[0].scrollIntoView({block:'center', inline:'center'});", vpsHostingLink);
                Waiter.clickable(vpsHostingLink);
                new Actions(driver).moveToElement(vpsHostingLink).pause(Duration.ofMillis(60)).click().perform();
            } else {
                driver.navigate().to(Config.baseUrl() + "vps-hosting");
            }
        } catch (Exception e) {
            driver.navigate().to(Config.baseUrl() + "vps-hosting");
        }
        return PageFactoryManager.get(VpsHostingPage.class);
    }
}

