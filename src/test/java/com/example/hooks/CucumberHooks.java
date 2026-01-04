package com.example.hooks;

import com.example.core.driver.DriverFactory;
import com.example.core.config.Config;
import com.example.pages.components.CookieBanner;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.qameta.allure.Allure;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

public class CucumberHooks {

    @Before("@bdd")
    public void setUp(Scenario scenario) {
        DriverFactory.start();
        DriverFactory.get().get(Config.baseUrl());
        if (Config.autoAcceptCookies()) {
            CookieBanner cookieBanner = new CookieBanner(DriverFactory.get());
            cookieBanner.acceptIfPresent();
        }
        Allure.step("Starting scenario: " + scenario.getName());
    }

    @After("@bdd")
    public void tearDown(Scenario scenario) {
        if (scenario.isFailed()) {
            WebDriver driver = DriverFactory.get();
            if (driver != null) {
                byte[] screenshot = ((TakesScreenshot) driver)
                        .getScreenshotAs(OutputType.BYTES);
                scenario.attach(screenshot, "image/png", "Screenshot on failure");
                Allure.getLifecycle().addAttachment("Screenshot on failure",
                        "image/png", ".png",
                        new java.io.ByteArrayInputStream(screenshot));
            }
        }
        DriverFactory.stop();
        Allure.step("Finished scenario: " + scenario.getName() +
                " - Status: " + (scenario.isFailed() ? "FAILED" : "PASSED"));
    }
}
