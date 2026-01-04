package com.example.steps;

import com.example.steps.actions.CookieSteps;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class CookieBannerStepDefinitions {

    private final CookieSteps cookieSteps;

    public CookieBannerStepDefinitions() {
        this.cookieSteps = new CookieSteps();
    }

    @When("I see the cookie banner")
    public void iSeeTheCookieBanner() {
        cookieSteps.checkBannerVisible();
    }

    @Then("the cookie banner should be visible")
    public void theCookieBannerShouldBeVisible() {
        cookieSteps.verifyBannerVisible();
    }

    @When("I accept the cookie banner")
    public void iAcceptTheCookieBanner() {
        cookieSteps.acceptBanner();
    }

    @When("I dismiss the cookie banner")
    public void iDismissTheCookieBanner() {
        cookieSteps.dismissBanner();
    }

    @Then("the cookie banner should disappear")
    @Then("the cookie banner should be dismissed")
    public void theCookieBannerShouldDisappear() {
        cookieSteps.verifyBannerDismissed();
    }

    @When("I refresh the page")
    public void whenIRefreshThePage() {
        cookieSteps.refreshPage();
    }

    @Then("the cookie banner should remain dismissed")
    public void theCookieBannerShouldRemainDismissed() {
        cookieSteps.verifyBannerRemainsDismissed();
    }

    @Then("the cookie banner should contain an {string} or {string} button")
    public void theCookieBannerShouldContainAnOrButton(String option1, String option2) {
        cookieSteps.verifyAcceptButton(option1, option2);
    }

    @Then("the cookie banner should contain a {string} or {string} button")
    public void theCookieBannerShouldContainAOrButton(String option1, String option2) {
        cookieSteps.verifyDeclineButton(option1, option2);
    }

    @When("I click on the {string} button")
    public void iClickOnTheButton(String buttonText) {
        cookieSteps.clickButton(buttonText);
    }

    @Then("cookies should be set in the browser")
    public void cookiesShouldBeSetInTheBrowser() {
        cookieSteps.verifyCookiesSet();
    }

    @Then("the page should continue to function normally")
    public void thePageShouldContinueToFunctionNormally() {
        cookieSteps.verifyPageFunctionsNormally();
    }
}
