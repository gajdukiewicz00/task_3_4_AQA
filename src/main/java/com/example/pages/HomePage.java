package com.example.pages;

import com.example.core.config.Config;
import com.example.pages.components.Header;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class HomePage extends BasePage {

    @FindBy(css = "header")
    private WebElement headerRoot;

    /**
     * Service method: Navigate to Pricing Hub page
     * @return PricingHubPage instance
     */
    public PricingHubPage goToPricing() {
        try {
            if (headerRoot != null) {
                SearchContext ctx = (SearchContext) headerRoot;
                new Header(ctx).openPlansAndPricing();
            } else {
                driver.get(Config.baseUrl() + "pricing");
            }
        } catch (Exception e) {
            driver.get(Config.baseUrl() + "pricing");
        }
        return PageFactoryManager.get(PricingHubPage.class);
    }
}
