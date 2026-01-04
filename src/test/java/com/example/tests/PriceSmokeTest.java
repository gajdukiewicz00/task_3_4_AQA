package com.example.tests;

import com.example.core.config.Config;
import com.example.core.driver.DriverFactory;
import com.example.pages.HostingPage;
import com.example.steps.actions.NavigationSteps;
import org.testng.Assert;
import org.testng.annotations.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.Supplier;

@Listeners({ com.example.listeners.TestListener.class })
public class PriceSmokeTest {

    private NavigationSteps navigationSteps;

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        DriverFactory.start();
        DriverFactory.get().get(Config.baseUrl());
        navigationSteps = new NavigationSteps();
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        DriverFactory.stop();
    }

    @DataProvider(name = "hostingPages")
    public Object[][] hostingPagesDataProvider() {
        return new Object[][] {
            { "Shared Hosting", (Supplier<HostingPage>) () -> navigationSteps.openSharedHosting() },
            { "VPS Hosting", (Supplier<HostingPage>) () -> navigationSteps.openVpsHosting() }
        };
    }

    @Test(groups = "smoke", dataProvider = "hostingPages", retryAnalyzer = com.example.utils.RetryOnFailure.class, 
          description = "Hosting prices are shown and positive for 1-year term")
    public void hostingPricesAreVisibleAndPositive(String hostingType, Supplier<HostingPage> pageSupplier) {
        HostingPage hostingPage = pageSupplier.get();
        List<BigDecimal> prices = hostingPage.getPrices1Year();
        int planCount = hostingPage.getPlanCount();

        Assert.assertTrue(planCount > 0, "No hosting plans found on " + hostingType + " page");
        Assert.assertFalse(prices.isEmpty(), "No prices found for " + hostingType + " plans");
        prices.forEach(price -> Assert.assertTrue(price.compareTo(BigDecimal.ZERO) > 0,
                "Price must be positive, but found: " + price));
    }

    @Test(groups = "smoke", dataProvider = "hostingPages", retryAnalyzer = com.example.utils.RetryOnFailure.class,
          description = "Hosting page has at least one plan with valid price")
    public void hostingHasAtLeastOnePlan(String hostingType, Supplier<HostingPage> pageSupplier) {
        HostingPage hostingPage = pageSupplier.get();
        int planCount = hostingPage.getPlanCount();
        List<BigDecimal> prices = hostingPage.getPrices1Year();

        Assert.assertTrue(planCount >= 1, "Expected at least 1 plan on " + hostingType + ", but found: " + planCount);
        Assert.assertTrue(prices.size() >= 1, "Expected at least 1 price on " + hostingType + ", but found: " + prices.size());
    }
}