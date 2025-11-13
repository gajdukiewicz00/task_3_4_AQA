package com.example.steps;

import com.example.pages.HomePage;
import com.example.pages.PageFactoryManager;
import io.cucumber.java.en.Given;
import io.qameta.allure.Step;
import org.testng.Assert;

/**
 * Common step definitions shared across multiple feature files
 */
public class CommonStepDefinitions {

    private HomePage homePage;

    @Given("I am on the InMotion Hosting homepage")
    @Step("Navigate to homepage")
    public void iAmOnTheInMotionHostingHomepage() {
        homePage = PageFactoryManager.get(HomePage.class);
        Assert.assertNotNull(homePage, "HomePage should be initialized");
    }

    public HomePage getHomePage() {
        return homePage;
    }
}


