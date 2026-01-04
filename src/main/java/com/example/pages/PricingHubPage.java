package com.example.pages;

import com.example.core.config.Config;
import com.example.core.wait.Waiter;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;

public class PricingHubPage extends BasePage {

    private static final String SHARED_HOSTING_PATH = "shared-hosting";
    private static final String VPS_HOSTING_PATH = "vps-hosting";

    @FindBy(css = "a[href*='" + SHARED_HOSTING_PATH + "']")
    private WebElement sharedHostingLink;

    @FindBy(css = "a[href*='" + VPS_HOSTING_PATH + "']")
    private WebElement vpsHostingLink;

    /**
     * Service method: Navigate to Shared Hosting page
     * 
     * @return SharedHostingPage instance
     */
    public SharedHostingPage openSharedHosting() {
        try {
            if (sharedHostingLink != null && sharedHostingLink.isDisplayed()) {
                Waiter.scrollIntoView(sharedHostingLink);
                Waiter.clickable(sharedHostingLink);
                sharedHostingLink.click();
            } else {
                driver.navigate().to(Config.baseUrl() + SHARED_HOSTING_PATH);
            }
        } catch (Exception e) {
            driver.navigate().to(Config.baseUrl() + SHARED_HOSTING_PATH);
        }
        return PageFactoryManager.get(SharedHostingPage.class);
    }

    /**
     * Service method: Navigate to VPS Hosting page
     * 
     * @return VpsHostingPage instance
     */
    public VpsHostingPage openVpsHosting() {
        try {
            if (vpsHostingLink != null && vpsHostingLink.isDisplayed()) {
                Waiter.scrollIntoView(vpsHostingLink);
                Waiter.clickable(vpsHostingLink);
                vpsHostingLink.click();
            } else {
                driver.navigate().to(Config.baseUrl() + VPS_HOSTING_PATH);
            }
        } catch (Exception e) {
            driver.navigate().to(Config.baseUrl() + VPS_HOSTING_PATH);
        }
        return PageFactoryManager.get(VpsHostingPage.class);
    }
}
