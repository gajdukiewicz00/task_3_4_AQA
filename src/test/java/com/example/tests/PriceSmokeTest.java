package com.example.tests;

import com.example.core.config.Config;
import com.example.core.driver.DriverFactory;
import com.example.pages.*;
import io.qameta.allure.Step;
import org.testng.Assert;
import org.testng.annotations.*;

import java.math.BigDecimal;
import java.util.List;

@Listeners({com.example.listeners.TestListener.class})
public class PriceSmokeTest {

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        DriverFactory.start();
        DriverFactory.get().get(Config.baseUrl());
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        DriverFactory.stop();
    }

    @Test(groups = "smoke", retryAnalyzer = com.example.utils.RetryOnFailure.class,
            description = "Shared hosting prices are shown and positive for 1-year term")
    public void sharedHostingPricesAreVisibleAndPositive() {
        SharedHostingPage sharedHostingPage = openSharedHosting();
        List<BigDecimal> prices = sharedHostingPage.getPrices1Year();
        int planCount = sharedHostingPage.getPlanCount();

        Assert.assertTrue(planCount > 0, "No hosting plans found on Shared Hosting page");
        Assert.assertFalse(prices.isEmpty(), "No prices found for Shared Hosting plans");
        prices.forEach(price -> 
            Assert.assertTrue(price.compareTo(BigDecimal.ZERO) > 0, 
                "Price must be positive, but found: " + price)
        );
    }

    @Test(groups = "smoke", retryAnalyzer = com.example.utils.RetryOnFailure.class,
            description = "VPS hosting prices are shown and positive for 1-year term")
    public void vpsHostingPricesAreVisibleAndPositive() {
        VpsHostingPage vpsHostingPage = openVpsHosting();
        List<BigDecimal> prices = vpsHostingPage.getPrices1Year();
        int planCount = vpsHostingPage.getPlanCount();

        Assert.assertTrue(planCount > 0, "No hosting plans found on VPS Hosting page");
        Assert.assertFalse(prices.isEmpty(), "No prices found for VPS Hosting plans");
        prices.forEach(price -> 
            Assert.assertTrue(price.compareTo(BigDecimal.ZERO) > 0, 
                "Price must be positive, but found: " + price)
        );
    }

    @Test(groups = "smoke", retryAnalyzer = com.example.utils.RetryOnFailure.class,
            description = "Shared hosting page has at least one plan with valid price")
    public void sharedHostingHasAtLeastOnePlan() {
        SharedHostingPage sharedHostingPage = openSharedHosting();
        int planCount = sharedHostingPage.getPlanCount();
        List<BigDecimal> prices = sharedHostingPage.getPrices1Year();

        Assert.assertTrue(planCount >= 1, "Expected at least 1 plan, but found: " + planCount);
        Assert.assertTrue(prices.size() >= 1, "Expected at least 1 price, but found: " + prices.size());
    }

    @Test(groups = "smoke", retryAnalyzer = com.example.utils.RetryOnFailure.class,
            description = "VPS hosting page has at least one plan with valid price")
    public void vpsHostingHasAtLeastOnePlan() {
        VpsHostingPage vpsHostingPage = openVpsHosting();
        int planCount = vpsHostingPage.getPlanCount();
        List<BigDecimal> prices = vpsHostingPage.getPrices1Year();

        Assert.assertTrue(planCount >= 1, "Expected at least 1 plan, but found: " + planCount);
        Assert.assertTrue(prices.size() >= 1, "Expected at least 1 price, but found: " + prices.size());
    }

    @Step("Open Shared Hosting from Home -> Pricing")
    private SharedHostingPage openSharedHosting() {
        HomePage homePage = PageFactoryManager.get(HomePage.class);
        PricingHubPage pricingHubPage = homePage.goToPricing();
        return pricingHubPage.openSharedHosting();
    }

    @Step("Open VPS Hosting from Home -> Pricing")
    private VpsHostingPage openVpsHosting() {
        HomePage homePage = PageFactoryManager.get(HomePage.class);
        PricingHubPage pricingHubPage = homePage.goToPricing();
        return pricingHubPage.openVpsHosting();
    }
}