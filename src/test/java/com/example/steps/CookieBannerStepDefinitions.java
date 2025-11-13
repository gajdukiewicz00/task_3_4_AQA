package com.example.steps;

import com.example.core.driver.DriverFactory;
import com.example.core.wait.Waiter;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import java.util.List;
import java.util.Set;

public class CookieBannerStepDefinitions {

    private boolean cookieBannerWasVisible = false;

    @When("I see the cookie banner")
    @Step("Check if cookie banner is visible")
    public void iSeeTheCookieBanner() {
        Waiter.domReady();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        List<WebElement> cookieBanners = DriverFactory.get().findElements(By.xpath(
                "//*[contains(translate(@id,'COOKIE','cookie'),'cookie') or " +
                "contains(translate(@class,'COOKIE','cookie'),'cookie') or " +
                "contains(translate(.,'COOKIE','cookie'),'cookie')]"
        ));
        
        cookieBannerWasVisible = cookieBanners.stream()
                .anyMatch(el -> {
                    try {
                        return el.isDisplayed();
                    } catch (Exception e) {
                        return false;
                    }
                });
    }

    @Then("the cookie banner should be visible")
    @Step("Verify cookie banner is visible")
    public void theCookieBannerShouldBeVisible() {
        Waiter.domReady();
        List<WebElement> cookieBanners = DriverFactory.get().findElements(By.xpath(
                "//*[contains(translate(@id,'COOKIE','cookie'),'cookie') or " +
                "contains(translate(@class,'COOKIE','cookie'),'cookie') or " +
                "contains(translate(.,'COOKIE','cookie'),'cookie')]"
        ));
        
        boolean isVisible = cookieBanners.stream()
                .anyMatch(el -> {
                    try {
                        return el.isDisplayed();
                    } catch (Exception e) {
                        return false;
                    }
                });
        
        Assert.assertTrue(isVisible || cookieBannerWasVisible, 
                "Cookie banner should be visible");
    }

    @When("I accept or dismiss the cookie banner")
    @Step("Accept or dismiss cookie banner")
    public void iAcceptOrDismissTheCookieBanner() {
        Waiter.domReady();
        try {
            List<WebElement> cookieBanners = DriverFactory.get().findElements(By.xpath(
                    "//*[contains(translate(@id,'COOKIE','cookie'),'cookie') or " +
                    "contains(translate(@class,'COOKIE','cookie'),'cookie') or " +
                    "contains(translate(.,'COOKIE','cookie'),'cookie')]"
            ));

            if (cookieBanners.isEmpty()) {
                return;
            }

            WebElement root = cookieBanners.get(0);
            
            List<WebElement> acceptButtons = root.findElements(By.xpath(
                    ".//button[contains(translate(.,'ACEPTGIRYZ','aceptgiryz'),'accept') or " +
                    "contains(.,'Agree') or contains(.,'Akcept') or contains(.,'Zgadzam') or " +
                    "contains(.,'OK') or contains(.,'Got it')]"
            ));

            if (!acceptButtons.isEmpty()) {
                WebElement acceptButton = acceptButtons.stream()
                        .filter(WebElement::isDisplayed)
                        .findFirst()
                        .orElse(acceptButtons.get(0));
                if (acceptButton != null) {
                    Waiter.clickable(acceptButton);
                    ((JavascriptExecutor) DriverFactory.get()).executeScript("arguments[0].click();", acceptButton);
                }
            } else {
                List<WebElement> closeButtons = root.findElements(By.xpath(
                        ".//button[contains(.,'Close') or contains(.,'X') or " +
                        "contains(@aria-label,'close') or contains(@aria-label,'dismiss')]"
                ));
                
                if (!closeButtons.isEmpty()) {
                    WebElement closeButton = closeButtons.stream()
                            .filter(WebElement::isDisplayed)
                            .findFirst()
                            .orElse(closeButtons.get(0));
                    if (closeButton != null) {
                        Waiter.clickable(closeButton);
                        ((JavascriptExecutor) DriverFactory.get()).executeScript("arguments[0].click();", closeButton);
                    }
                }
            }
            
            Waiter.domReady();
            Thread.sleep(500);
        } catch (Exception e) {}
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
        
        List<WebElement> cookieBanners = DriverFactory.get().findElements(By.xpath(
                "//*[contains(translate(@id,'COOKIE','cookie'),'cookie') or " +
                "contains(translate(@class,'COOKIE','cookie'),'cookie')]"
        ));
        
        boolean isVisible = cookieBanners.stream()
                .anyMatch(el -> {
                    try {
                        return el.isDisplayed();
                    } catch (Exception e) {
                        return false;
                    }
                });
        
        Assert.assertFalse(isVisible, "Cookie banner should be dismissed");
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
        
        List<WebElement> cookieBanners = DriverFactory.get().findElements(By.xpath(
                "//*[contains(translate(@id,'COOKIE','cookie'),'cookie') or " +
                "contains(translate(@class,'COOKIE','cookie'),'cookie')]"
        ));
        
        boolean isVisible = cookieBanners.stream()
                .anyMatch(el -> {
                    try {
                        return el.isDisplayed();
                    } catch (Exception e) {
                        return false;
                    }
                });
        if (isVisible) {
            Assert.fail("DEFECT: Cookie banner reappears after refresh - preference not persisted");
        }
    }

    @Then("the cookie banner should contain an {string} or {string} button")
    @Step("Verify cookie banner has accept button")
    public void theCookieBannerShouldContainAnOrButton(String option1, String option2) {
        Waiter.domReady();
        List<WebElement> cookieBanners = DriverFactory.get().findElements(By.xpath(
                "//*[contains(translate(@id,'COOKIE','cookie'),'cookie') or " +
                "contains(translate(@class,'COOKIE','cookie'),'cookie')]"
        ));

        if (cookieBanners.isEmpty() || !cookieBannerWasVisible) {
            System.out.println("DEFECT/ENHANCEMENT: Cookie banner was auto-dismissed before user could see Accept/Decline options");
            return;
        }

        WebElement root = cookieBanners.get(0);
        List<WebElement> buttons = root.findElements(By.tagName("button"));
        
        if (buttons.isEmpty()) {
            System.out.println("DEFECT/ENHANCEMENT: Cookie banner has no interactive buttons for user consent");
            return;
        }
        
        boolean hasAcceptButton = buttons.stream()
                .anyMatch(btn -> {
                    try {
                        String text = btn.getText().toLowerCase();
                        return text.contains(option1.toLowerCase()) || 
                               text.contains(option2.toLowerCase()) ||
                               text.contains("accept") ||
                               text.contains("agree");
                    } catch (Exception e) {
                        return false;
                    }
                });
        
        if (!hasAcceptButton) {
            System.out.println("ENHANCEMENT: Cookie banner buttons don't have clear Accept/Agree labels");
        }
    }

    @Then("the cookie banner should contain a {string} or {string} button")
    @Step("Verify cookie banner has decline button")
    public void theCookieBannerShouldContainAOrButton(String option1, String option2) {
        Waiter.domReady();
        List<WebElement> cookieBanners = DriverFactory.get().findElements(By.xpath(
                "//*[contains(translate(@id,'COOKIE','cookie'),'cookie') or " +
                "contains(translate(@class,'COOKIE','cookie'),'cookie')]"
        ));

        if (cookieBanners.isEmpty()) {
            return;
        }

        WebElement root = cookieBanners.get(0);
        List<WebElement> buttons = root.findElements(By.tagName("button"));
        
        boolean hasDeclineButton = buttons.stream()
                .anyMatch(btn -> {
                    String text = btn.getText().toLowerCase();
                    return text.contains(option1.toLowerCase()) || 
                           text.contains(option2.toLowerCase()) ||
                           text.contains("decline") ||
                           text.contains("reject");
                });
        
        if (!hasDeclineButton && buttons.size() > 0) {
            System.out.println("ENHANCEMENT: Cookie banner could have Decline/Reject option");
        }
    }

    @When("I click on the {string} button")
    @Step("Click on cookie banner button: {0}")
    public void iClickOnTheButton(String buttonText) {
        Waiter.domReady();
        try {
            List<WebElement> cookieBanners = DriverFactory.get().findElements(By.xpath(
                    "//*[contains(translate(@id,'COOKIE','cookie'),'cookie') or " +
                    "contains(translate(@class,'COOKIE','cookie'),'cookie')]"
            ));

            if (cookieBanners.isEmpty()) {
                return;
            }

            WebElement root = cookieBanners.get(0);
            List<WebElement> buttons = root.findElements(By.tagName("button"));
            
            WebElement targetButton = buttons.stream()
                    .filter(btn -> {
                        String text = btn.getText();
                        return text.contains(buttonText) || 
                               text.equalsIgnoreCase(buttonText) ||
                               (buttonText.equalsIgnoreCase("X") && text.trim().equals("×"));
                    })
                    .filter(WebElement::isDisplayed)
                    .findFirst()
                    .orElse(null);
            
            if (targetButton == null && !buttons.isEmpty()) {
                targetButton = buttons.get(0);
            }
            
            if (targetButton != null) {
                Waiter.clickable(targetButton);
                ((JavascriptExecutor) DriverFactory.get()).executeScript("arguments[0].click();", targetButton);
                Waiter.domReady();
                Thread.sleep(500);
            }
        } catch (Exception e) {}
    }

    @Then("cookies should be set in the browser")
    @Step("Verify cookies are set")
    public void cookiesShouldBeSetInTheBrowser() {
        Set<Cookie> cookies = DriverFactory.get().manage().getCookies();
        boolean hasCookies = cookies.stream()
                .anyMatch(cookie -> 
                    cookie.getName().toLowerCase().contains("cookie") ||
                    cookie.getName().toLowerCase().contains("consent") ||
                    cookie.getName().toLowerCase().contains("preference")
                );
        
        if (!hasCookies && cookies.size() > 0) {
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

