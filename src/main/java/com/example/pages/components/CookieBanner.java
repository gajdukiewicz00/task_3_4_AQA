package com.example.pages.components;

import com.example.core.driver.DriverFactory;
import com.example.core.wait.Waiter;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public final class CookieBanner {
    private CookieBanner() {
    }

    private static final String BANNER_XPATH = "//*[contains(translate(@id,'COOKIE','cookie'),'cookie') or " +
            "contains(translate(@class,'COOKIE','cookie'),'cookie') or " +
            "contains(translate(.,'COOKIE','cookie'),'cookie')]";

    @Step("Check if cookie banner is displayed")
    public static boolean isDisplayed() {
        try {
            List<WebElement> banners = DriverFactory.get().findElements(By.xpath(BANNER_XPATH));
            return banners.stream().anyMatch(WebElement::isDisplayed);
        } catch (Exception e) {
            return false;
        }
    }

    @Step("Accept cookie banner")
    public static void acceptIfPresent() {
        if (!isDisplayed())
            return;

        clickButtonContaining(new String[] { "accept", "agree", "akcept", "zgadzam", "ok", "got it" });
    }

    @Step("Dismiss cookie banner")
    public static void dismissIfPresent() {
        if (!isDisplayed())
            return;

        clickButtonContaining(new String[] { "close", "x", "dismiss", "reject", "decline" });
    }

    @Step("Click button containing text: {0}")
    public static void clickButton(String text) {
        clickButtonContaining(new String[] { text });
    }

    private static List<WebElement> getInteractiveElements(WebElement root) {
        List<WebElement> elements = root.findElements(By.tagName("button"));
        elements.addAll(root.findElements(By.tagName("a")));
        elements.addAll(root.findElements(By.cssSelector("input[type='button'], input[type='submit']")));
        elements.addAll(root.findElements(By.cssSelector("[role='button']")));
        return elements;
    }

    private static void clickButtonContaining(String[] keywords) {
        WebDriver d = DriverFactory.get();
        List<WebElement> banners = d.findElements(By.xpath(BANNER_XPATH));
        if (banners.isEmpty())
            return;

        WebElement root = banners.get(0);
        List<WebElement> buttons = getInteractiveElements(root);

        for (String keyword : keywords) {
            for (WebElement btn : buttons) {
                if (btn.isDisplayed() && btn.getText().toLowerCase().contains(keyword.toLowerCase())) {
                    Waiter.clickable(btn);
                    ((JavascriptExecutor) d).executeScript("arguments[0].click();", btn);

                    // Wait for banner to disappear
                    try {
                        new org.openqa.selenium.support.ui.WebDriverWait(d, java.time.Duration.ofSeconds(2))
                                .until(org.openqa.selenium.support.ui.ExpectedConditions.invisibilityOf(root));
                    } catch (Exception ignored) {
                    }
                    return;
                }
            }
        }

        // Fallback for close buttons with aria-label
        List<WebElement> closeButtons = root.findElements(By.xpath(
                ".//button[contains(@aria-label,'close') or contains(@aria-label,'dismiss')]"));
        if (!closeButtons.isEmpty() && closeButtons.get(0).isDisplayed()) {
            WebElement btn = closeButtons.get(0);
            Waiter.clickable(btn);
            ((JavascriptExecutor) d).executeScript("arguments[0].click();", btn);

            // Wait for banner to disappear
            try {
                new org.openqa.selenium.support.ui.WebDriverWait(d, java.time.Duration.ofSeconds(2))
                        .until(org.openqa.selenium.support.ui.ExpectedConditions.invisibilityOf(root));
            } catch (Exception ignored) {
            }
        }
    }

    @Step("Check if banner has button with text: {0}")
    public static boolean hasButtonWithText(String text) {
        WebDriver d = DriverFactory.get();
        List<WebElement> banners = d.findElements(By.xpath(BANNER_XPATH));
        if (banners.isEmpty())
            return false;

        WebElement root = banners.get(0);
        List<WebElement> buttons = getInteractiveElements(root);

        return buttons.stream()
                .anyMatch(btn -> btn.getText().toLowerCase().contains(text.toLowerCase()));
    }
}
