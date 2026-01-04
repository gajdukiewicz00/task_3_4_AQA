package com.example.steps;

import com.example.core.config.Config;
import com.example.core.driver.DriverFactory;
import com.example.pages.HomePage;
import com.example.pages.PageFactoryManager;
import io.cucumber.java.en.Given;

public class CommonStepDefinitions {

    @Given("I am on the InMotion Hosting homepage")
    public void iAmOnTheInMotionHostingHomepage() {
        DriverFactory.get().get(Config.baseUrl());
        PageFactoryManager.get(HomePage.class);
    }
}
