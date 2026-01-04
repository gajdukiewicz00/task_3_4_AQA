package com.example.pages.components;

import com.example.core.wait.Waiter;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

public class CookieBanner {
    private final WebDriver driver;

    public CookieBanner(WebDriver driver) {
        this.driver = driver;
    }

    private static final String TAG_CHECK = "(self::button or self::a or @role='button' or (self::input and (@type='button' or @type='submit')))";

    private static final String ACCEPT_BTN_XPATH = "//*[" + TAG_CHECK + " and (" +
            "contains(translate(., 'ACCEPT', 'accept'), 'accept') or " +
            "contains(translate(., 'AGREE', 'agree'), 'agree') or " +
            "contains(translate(., 'GOT IT', 'got it'), 'got it') or " +
            "contains(translate(., 'AKCEPT', 'akcept'), 'akcept') or " +
            "contains(translate(., 'ZGADZAM', 'zgadzam'), 'zgadzam')" +
            ")]";

    private static final String DISMISS_BTN_XPATH = "//*[" + TAG_CHECK + " and (" +
            "contains(translate(., 'REJECT', 'reject'), 'reject') or " +
            "contains(translate(., 'DECLINE', 'decline'), 'decline') or " +
            "contains(translate(., 'CLOSE', 'close'), 'close') or " +
            "contains(@aria-label, 'close') or " +
            "contains(@aria-label, 'Close') or " +
            "contains(@aria-label, 'dismiss')" +
            ")]";

    @Step("Check if cookie banner is displayed")
    public boolean isDisplayed() {
        try {
            return !driver.findElements(By.xpath(ACCEPT_BTN_XPATH)).isEmpty() ||
                    !driver.findElements(By.xpath(DISMISS_BTN_XPATH)).isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    @Step("Accept cookie banner")
    public void acceptIfPresent() {
        clickButtonByXPath(ACCEPT_BTN_XPATH);
    }

    @Step("Dismiss cookie banner")
    public void dismissIfPresent() {
        clickButtonByXPath(DISMISS_BTN_XPATH);
    }

    @Step("Click button containing text: {0}")
    public void clickButton(String text) {
        String xpath = "//*[" + TAG_CHECK + " and contains(translate(., '" + text.toUpperCase() + "', '"
                + text.toLowerCase() + "'), '" + text.toLowerCase() + "')]";
        clickButtonByXPath(xpath);
    }

    private void clickButtonByXPath(String xpath) {
        List<WebElement> buttons = driver.findElements(By.xpath(xpath));
        for (WebElement btn : buttons) {
            if (btn.isDisplayed()) {
                Waiter.clickable(btn);
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);

                // Try to wait for it to disappear (stale or invisible)
                try {
                    Waiter.waitDefault()
                            .until(ExpectedConditions.invisibilityOf(btn));
                } catch (Exception ignored) {
                }
                return;
            }
        }
    }

    @Step("Check if banner has button with text: {0}")
    public boolean hasButtonWithText(String text) {
        String xpath = "//*[" + TAG_CHECK + " and contains(translate(., '" + text.toUpperCase() + "', '"
                + text.toLowerCase() + "'), '" + text.toLowerCase() + "')]";
        List<WebElement> buttons = driver.findElements(By.xpath(xpath));
        return buttons.stream().anyMatch(WebElement::isDisplayed);
    }
}
