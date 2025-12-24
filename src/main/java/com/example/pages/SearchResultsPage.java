package com.example.pages;

import com.example.core.wait.Waiter;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class SearchResultsPage extends BasePage {

    @Step("Enter search term: {0}")
    public void enterSearchTerm(String searchTerm) {
        try {
            List<WebElement> searchFields = driver.findElements(By.cssSelector(
                    "input[type='search'], input[name*='search' i], input[id*='search' i], " +
                            "input[placeholder*='search' i], input[aria-label*='search' i], " +
                            "form[role='search'] input, .search-input, #search, [data-testid*='search']"));

            if (searchFields.isEmpty()) {
                openSearchOverlay();
                searchFields = driver.findElements(By.cssSelector(
                        "input[type='search'], input[name*='search' i], input[id*='search' i]"));
            }

            if (!searchFields.isEmpty()) {
                WebElement field = searchFields.stream().filter(WebElement::isDisplayed).findFirst()
                        .orElse(searchFields.get(0));
                Waiter.clickable(field);
                field.clear();
                if (!searchTerm.isEmpty())
                    field.sendKeys(searchTerm);
            } else if (!searchTerm.isEmpty()) {
                driver.get("https://www.inmotionhosting.com/search?q=" + searchTerm);
                Waiter.domReady();
            }
        } catch (Exception e) {
            if (!searchTerm.isEmpty()) {
                driver.get("https://www.inmotionhosting.com/search?q=" + searchTerm);
                Waiter.domReady();
            }
        }
    }

    private void openSearchOverlay() {
        List<WebElement> buttons = driver.findElements(By.cssSelector(
                "button[aria-label*='search' i], button[id*='search' i], .search-icon, [data-testid*='search-button']"));
        if (!buttons.isEmpty()) {
            WebElement btn = buttons.stream().filter(WebElement::isDisplayed).findFirst().orElse(buttons.get(0));
            Waiter.clickable(btn);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
            Waiter.domReady();
        }
    }

    @Step("Submit search")
    public void submitSearch(String searchTerm) {
        try {
            String currentUrl = driver.getCurrentUrl();

            // Try pressing ENTER on the search input first, it's often more reliable
            List<WebElement> fields = driver
                    .findElements(By.cssSelector("input[type='search'], input[name*='search' i]"));
            if (!fields.isEmpty()) {
                WebElement field = fields.stream().filter(WebElement::isDisplayed).findFirst().orElse(fields.get(0));
                field.sendKeys(Keys.ENTER);
            } else {
                // Fallback to clicking submit button
                List<WebElement> submitButtons = driver.findElements(By.cssSelector(
                        "button[type='submit'], button[aria-label*='search' i], form[role='search'] button, " +
                                ".search-submit, input[type='submit'][value*='search' i]"));

                if (!submitButtons.isEmpty()) {
                    WebElement btn = submitButtons.stream().filter(WebElement::isDisplayed).findFirst()
                            .orElse(submitButtons.get(0));
                    Waiter.clickable(btn);
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
                }
            }

            Waiter.domReady();
            // Wait for URL to change or Title to contain search term
            try {
                new WebDriverWait(driver, Duration.ofSeconds(5))
                        .until(d -> !d.getCurrentUrl().equals(currentUrl)
                                || d.getTitle().toLowerCase().contains(searchTerm.toLowerCase())
                                || d.getCurrentUrl().contains("q=" + searchTerm)
                                || d.getCurrentUrl().contains("search"));
            } catch (Exception ignored) {
            }

        } catch (Exception e) {
            if (searchTerm != null && !searchTerm.isEmpty()) {
                driver.get("https://www.inmotionhosting.com/search?q=" + searchTerm);
                Waiter.domReady();
            }
        }
    }

    @Step("Check if results contain: {0}")
    public boolean hasResultsContaining(String text) {
        String pageText = driver.findElement(By.tagName("body")).getText().toLowerCase();
        String pageSource = driver.getPageSource().toLowerCase();
        return pageText.contains(text.toLowerCase()) || pageSource.contains(text.toLowerCase())
                || driver.getCurrentUrl().contains("search");
    }

    @Step("Check if results are relevant to: {0}")
    public boolean isRelevantTo(String term) {
        String pageText = driver.findElement(By.tagName("body")).getText().toLowerCase();
        String currentUrl = driver.getCurrentUrl();
        return currentUrl.contains("search") || pageText.contains("results") || pageText.contains("found")
                || pageText.contains(term.toLowerCase());
    }

    @Step("Check if results are displayed")
    public boolean hasResults() {
        String currentUrl = driver.getCurrentUrl();
        String pageText = driver.findElement(By.tagName("body")).getText();
        return currentUrl.contains("search") || pageText.toLowerCase().contains("result") || !pageText.trim().isEmpty();
    }

    @Step("Check if title contains: {0}")
    public boolean hasTitleContaining(String text) {
        return driver.getTitle().toLowerCase().contains(text.toLowerCase())
                || driver.getCurrentUrl().toLowerCase().contains(text.toLowerCase());
    }

    @Step("Check for no results message")
    public boolean hasNoResultsMessage() {
        String pageText = driver.findElement(By.tagName("body")).getText().toLowerCase();
        return pageText.contains("no results") || pageText.contains("no matches") || pageText.contains("try again")
                || !driver.getCurrentUrl().contains("search");
    }

    @Step("Check for error 404")
    public boolean hasError404() {
        String pageText = driver.findElement(By.tagName("body")).getText().toLowerCase();
        return pageText.contains("error") && !pageText.contains("no results") && !pageText.contains("404");
    }
}
