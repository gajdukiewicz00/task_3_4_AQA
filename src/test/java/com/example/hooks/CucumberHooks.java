package com.example.hooks;

import com.example.core.driver.DriverFactory;
import com.example.core.config.Config;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.qameta.allure.Allure;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

public class CucumberHooks {

    @Before("@bdd")
    public void setUp(Scenario scenario) {
        DriverFactory.start();
        DriverFactory.get().get(Config.baseUrl());
        Allure.step("Starting scenario: " + scenario.getName());
    }

    @After("@bdd")
    public void tearDown(Scenario scenario) {
        if (scenario.isFailed()) {
            byte[] screenshot = ((TakesScreenshot) DriverFactory.get())
                    .getScreenshotAs(OutputType.BYTES);
            scenario.attach(screenshot, "image/png", "Screenshot on failure");
                Allure.getLifecycle().addAttachment("Screenshot on failure", 
                        "image/png", ".png", 
                        new java.io.ByteArrayInputStream(screenshot));
        }
        DriverFactory.stop();
        Allure.step("Finished scenario: " + scenario.getName() + 
                " - Status: " + (scenario.isFailed() ? "FAILED" : "PASSED"));
    }
}

