package com.example.steps.actions;

import com.example.core.config.Config;
import com.example.core.driver.DriverFactory;
import com.example.pages.HomePage;
import com.example.pages.PageFactoryManager;
import com.example.pages.PricingHubPage;
import com.example.pages.SharedHostingPage;
import com.example.pages.VpsHostingPage;
import io.qameta.allure.Step;

public class NavigationSteps {

    @Step("Open Home Page")
    public HomePage openHomePage() {
        DriverFactory.get().get(Config.baseUrl());
        return PageFactoryManager.get(HomePage.class);
    }

    @Step("Open Shared Hosting from Home -> Pricing")
    public SharedHostingPage openSharedHosting() {
        HomePage homePage = openHomePage();
        PricingHubPage pricingHubPage = homePage.goToPricing();
        return pricingHubPage.openSharedHosting();
    }

    @Step("Open VPS Hosting from Home -> Pricing")
    public VpsHostingPage openVpsHosting() {
        HomePage homePage = openHomePage();
        PricingHubPage pricingHubPage = homePage.goToPricing();
        return pricingHubPage.openVpsHosting();
    }
}
