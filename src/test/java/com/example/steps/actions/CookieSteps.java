package com.example.steps.actions;

import com.example.core.driver.DriverFactory;
import com.example.core.wait.Waiter;
import com.example.pages.components.CookieBanner;
import io.qameta.allure.Step;
import org.openqa.selenium.Cookie;
import org.testng.Assert;

import java.util.Set;

public class CookieSteps {

    private CookieBanner cookieBanner;
    private boolean cookieBannerWasVisible = false;

    public CookieSteps() {
        // Ленивая инициализация - CookieBanner будет создан при первом использовании
    }

    private CookieBanner banner() {
        if (cookieBanner == null) {
            cookieBanner = new CookieBanner(DriverFactory.get());
        }
        return cookieBanner;
    }

    @Step("Check if cookie banner is visible")
    public void checkBannerVisible() {
        Waiter.domReady();
        cookieBannerWasVisible = banner().isDisplayed();
    }

    @Step("Verify cookie banner is visible")
    public void verifyBannerVisible() {
        Waiter.domReady();
        boolean isVisible = banner().isDisplayed();
        Assert.assertTrue(isVisible || cookieBannerWasVisible, "Cookie banner should be visible");
    }

    @Step("Accept cookie banner")
    public void acceptBanner() {
        Waiter.domReady();
        if (banner().isDisplayed()) {
            banner().acceptIfPresent();
        }
        Waiter.domReady();
    }

    @Step("Dismiss cookie banner")
    public void dismissBanner() {
        Waiter.domReady();
        if (banner().isDisplayed()) {
            banner().dismissIfPresent();
        }
        Waiter.domReady();
    }

    @Step("Verify cookie banner is dismissed")
    public void verifyBannerDismissed() {
        Waiter.domReady();
        Assert.assertFalse(banner().isDisplayed(), "Cookie banner should be dismissed");
    }

    @Step("Refresh the page")
    public void refreshPage() {
        DriverFactory.get().navigate().refresh();
        Waiter.domReady();
    }

    @Step("Verify cookie banner stays dismissed after refresh")
    public void verifyBannerRemainsDismissed() {
        Waiter.domReady();
        if (banner().isDisplayed()) {
            Assert.fail("DEFECT: Cookie banner reappears after refresh - preference not persisted");
        }
    }

    @Step("Verify cookie banner has accept button with text: {0} or {1}")
    public void verifyAcceptButton(String option1, String option2) {
        Waiter.domReady();

        if (!banner().isDisplayed() && !cookieBannerWasVisible) {
            Assert.fail(
                    "DEFECT: Cookie banner was auto-dismissed or not displayed before user could see Accept/Decline options");
        }

        boolean hasOption1 = banner().hasButtonWithText(option1);
        boolean hasOption2 = banner().hasButtonWithText(option2);
        boolean hasStandard = banner().hasButtonWithText("accept") || banner().hasButtonWithText("agree");

        if (!hasOption1 && !hasOption2 && !hasStandard) {
            Assert.fail("ENHANCEMENT: Cookie banner buttons don't have clear Accept/Agree labels. Found: " + option1
                    + ", " + option2);
        }
    }

    @Step("Verify cookie banner has decline button with text: {0} or {1}")
    public void verifyDeclineButton(String option1, String option2) {
        Waiter.domReady();
        if (!banner().isDisplayed()) {
            Assert.fail("DEFECT: Cookie banner not displayed when checking for Decline options");
        }

        boolean hasOption1 = banner().hasButtonWithText(option1);
        boolean hasOption2 = banner().hasButtonWithText(option2);
        boolean hasStandard = banner().hasButtonWithText("decline") || banner().hasButtonWithText("reject");

        if (!hasOption1 && !hasOption2 && !hasStandard) {
            Assert.fail(
                    "ENHANCEMENT: Cookie banner could have Decline/Reject option. Found: " + option1 + ", " + option2);
        }
    }

    @Step("Click on cookie banner button: {0}")
    public void clickButton(String buttonText) {
        Waiter.domReady();
        banner().clickButton(buttonText);
        Waiter.domReady();
    }

    @Step("Verify cookies are set")
    public void verifyCookiesSet() {
        Set<Cookie> cookies = DriverFactory.get().manage().getCookies();
        boolean hasCookies = cookies.stream()
                .anyMatch(cookie -> cookie.getName().toLowerCase().contains("cookie") ||
                        cookie.getName().toLowerCase().contains("consent") ||
                        cookie.getName().toLowerCase().contains("preference") ||
                        cookie.getName().toLowerCase().contains("onetrust"));

        Assert.assertTrue(hasCookies, "Cookies should be set in the browser");
    }

    @Step("Verify page functions normally after cookie banner action")
    public void verifyPageFunctionsNormally() {
        Waiter.domReady();
        String pageTitle = DriverFactory.get().getTitle();
        String currentUrl = DriverFactory.get().getCurrentUrl();

        Assert.assertNotNull(pageTitle, "Page title should be present");
        Assert.assertFalse(pageTitle.isEmpty(), "Page title should not be empty");
        Assert.assertNotNull(currentUrl, "Current URL should be present");
    }
}
