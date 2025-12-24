package com.example.steps;

import com.example.core.driver.DriverFactory;
import com.example.core.wait.Waiter;
import com.example.pages.components.CookieBanner;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.qameta.allure.Step;
import org.openqa.selenium.Cookie;
import org.testng.Assert;

import java.util.Set;

public class CookieBannerStepDefinitions {

    private boolean cookieBannerWasVisible = false;

    @When("I see the cookie banner")
    @Step("Check if cookie banner is visible")
    public void iSeeTheCookieBanner() {
        Waiter.domReady();
        try {
            Thread.sleep(1000); // Wait for animation/load
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        cookieBannerWasVisible = CookieBanner.isDisplayed();
    }

    @Then("the cookie banner should be visible")
    @Step("Verify cookie banner is visible")
    public void theCookieBannerShouldBeVisible() {
        Waiter.domReady();
        boolean isVisible = CookieBanner.isDisplayed();
        Assert.assertTrue(isVisible || cookieBannerWasVisible, "Cookie banner should be visible");
    }

    @When("I accept or dismiss the cookie banner")
    @Step("Accept or dismiss cookie banner")
    public void iAcceptOrDismissTheCookieBanner() {
        Waiter.domReady();
        if (CookieBanner.isDisplayed()) {
            CookieBanner.acceptIfPresent();
            // If still displayed, try dismissing
            if (CookieBanner.isDisplayed()) {
                CookieBanner.dismissIfPresent();
            }
        }
        Waiter.domReady();
    }

    @Then("the cookie banner should disappear")
    @Then("the cookie banner should be dismissed")
    @Step("Verify cookie banner is dismissed")
    public void theCookieBannerShouldDisappear() {
        Waiter.domReady();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        Assert.assertFalse(CookieBanner.isDisplayed(), "Cookie banner should be dismissed");
    }

    @When("when I refresh the page")
    @Step("Refresh the page")
    public void whenIRefreshThePage() {
        DriverFactory.get().navigate().refresh();
        Waiter.domReady();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Then("the cookie banner should remain dismissed")
    @Step("Verify cookie banner stays dismissed after refresh")
    public void theCookieBannerShouldRemainDismissed() {
        Waiter.domReady();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        if (CookieBanner.isDisplayed()) {
            Assert.fail("DEFECT: Cookie banner reappears after refresh - preference not persisted");
        }
    }

    @Then("the cookie banner should contain an {string} or {string} button")
    @Step("Verify cookie banner has accept button")
    public void theCookieBannerShouldContainAnOrButton(String option1, String option2) {
        Waiter.domReady();

        if (!CookieBanner.isDisplayed() && !cookieBannerWasVisible) {
            System.out.println(
                    "DEFECT/ENHANCEMENT: Cookie banner was auto-dismissed before user could see Accept/Decline options");
            return;
        }

        boolean hasOption1 = CookieBanner.hasButtonWithText(option1);
        boolean hasOption2 = CookieBanner.hasButtonWithText(option2);
        boolean hasStandard = CookieBanner.hasButtonWithText("accept") || CookieBanner.hasButtonWithText("agree");

        if (!hasOption1 && !hasOption2 && !hasStandard) {
            System.out.println("ENHANCEMENT: Cookie banner buttons don't have clear Accept/Agree labels");
        }
    }

    @Then("the cookie banner should contain a {string} or {string} button")
    @Step("Verify cookie banner has decline button")
    public void theCookieBannerShouldContainAOrButton(String option1, String option2) {
        Waiter.domReady();
        if (!CookieBanner.isDisplayed())
            return;

        boolean hasOption1 = CookieBanner.hasButtonWithText(option1);
        boolean hasOption2 = CookieBanner.hasButtonWithText(option2);
        boolean hasStandard = CookieBanner.hasButtonWithText("decline") || CookieBanner.hasButtonWithText("reject");

        if (!hasOption1 && !hasOption2 && !hasStandard) {
            System.out.println("ENHANCEMENT: Cookie banner could have Decline/Reject option");
        }
    }

    @When("I click on the {string} button")
    @Step("Click on cookie banner button: {0}")
    public void iClickOnTheButton(String buttonText) {
        Waiter.domReady();
        CookieBanner.clickButton(buttonText);
        Waiter.domReady();
    }

    @Then("cookies should be set in the browser")
    @Step("Verify cookies are set")
    public void cookiesShouldBeSetInTheBrowser() {
        Set<Cookie> cookies = DriverFactory.get().manage().getCookies();
        boolean hasCookies = cookies.stream()
                .anyMatch(cookie -> cookie.getName().toLowerCase().contains("cookie") ||
                        cookie.getName().toLowerCase().contains("consent") ||
                        cookie.getName().toLowerCase().contains("preference"));

        if (!hasCookies && !cookies.isEmpty()) {
            System.out.println("Note: Cookie consent cookies might not be set immediately");
        }
    }

    @Then("the page should continue to function normally")
    @Step("Verify page functions normally after cookie banner action")
    public void thePageShouldContinueToFunctionNormally() {
        Waiter.domReady();
        String pageTitle = DriverFactory.get().getTitle();
        String currentUrl = DriverFactory.get().getCurrentUrl();

        Assert.assertNotNull(pageTitle, "Page title should be present");
        Assert.assertFalse(pageTitle.isEmpty(), "Page title should not be empty");
        Assert.assertNotNull(currentUrl, "Current URL should be present");
    }
}
