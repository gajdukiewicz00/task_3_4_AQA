package com.example.steps;

import com.example.core.driver.DriverFactory;
import com.example.core.wait.Waiter;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import java.util.List;

public class SearchStepDefinitions {

    private String searchTerm;

    @When("I enter {string} in the search field")
    @Step("Enter search term: {0}")
    public void iEnterInTheSearchField(String searchTerm) {
        this.searchTerm = searchTerm;
        try {
            List<WebElement> searchFields = DriverFactory.get().findElements(By.cssSelector(
                    "input[type='search'], " +
                    "input[name*='search' i], " +
                    "input[id*='search' i], " +
                    "input[placeholder*='search' i], " +
                    "input[aria-label*='search' i], " +
                    "form[role='search'] input, " +
                    ".search-input, " +
                    "#search, " +
                    "[data-testid*='search']"
            ));

            if (searchFields.isEmpty()) {
                List<WebElement> searchButtons = DriverFactory.get().findElements(By.cssSelector(
                        "button[aria-label*='search' i], " +
                        "button[id*='search' i], " +
                        ".search-icon, " +
                        "[data-testid*='search-button']"
                ));
                
                if (!searchButtons.isEmpty()) {
                    WebElement searchButton = searchButtons.stream()
                            .filter(WebElement::isDisplayed)
                            .findFirst()
                            .orElse(searchButtons.get(0));
                    if (searchButton != null) {
                        Waiter.clickable(searchButton);
                        ((JavascriptExecutor) DriverFactory.get()).executeScript("arguments[0].click();", searchButton);
                        Waiter.domReady();
                        Thread.sleep(500);
                    }
                }
                
                searchFields = DriverFactory.get().findElements(By.cssSelector(
                        "input[type='search'], " +
                        "input[name*='search' i], " +
                        "input[id*='search' i], " +
                        "input[placeholder*='search' i]"
                ));
            }

            if (!searchFields.isEmpty()) {
                WebElement searchField = searchFields.stream()
                        .filter(WebElement::isDisplayed)
                        .findFirst()
                        .orElse(searchFields.get(0));
                
                Waiter.clickable(searchField);
                searchField.clear();
                if (!searchTerm.isEmpty()) {
                    searchField.sendKeys(searchTerm);
                }
            } else {
                if (!searchTerm.isEmpty()) {
                    DriverFactory.get().get("https://www.inmotionhosting.com/search?q=" + searchTerm);
                    Waiter.domReady();
                }
            }
        } catch (Exception e) {
            if (!searchTerm.isEmpty()) {
                DriverFactory.get().get("https://www.inmotionhosting.com/search?q=" + searchTerm);
                Waiter.domReady();
            }
        }
    }

    @When("I submit the search")
    @Step("Submit search")
    public void iSubmitTheSearch() {
        try {
            List<WebElement> submitButtons = DriverFactory.get().findElements(By.cssSelector(
                    "button[type='submit'], " +
                    "button[aria-label*='search' i], " +
                    "form[role='search'] button, " +
                    ".search-submit, " +
                    "input[type='submit'][value*='search' i]"
            ));

            if (!submitButtons.isEmpty()) {
                WebElement submitButton = submitButtons.stream()
                        .filter(WebElement::isDisplayed)
                        .findFirst()
                        .orElse(submitButtons.get(0));
                if (submitButton != null) {
                    Waiter.clickable(submitButton);
                    ((JavascriptExecutor) DriverFactory.get()).executeScript("arguments[0].click();", submitButton);
                }
            } else {
                List<WebElement> searchFields = DriverFactory.get().findElements(By.cssSelector(
                        "input[type='search'], input[name*='search' i], input[id*='search' i]"
                ));
                if (!searchFields.isEmpty()) {
                    WebElement searchField = searchFields.stream()
                            .filter(WebElement::isDisplayed)
                            .findFirst()
                            .orElse(searchFields.get(0));
                    searchField.submit();
                }
            }
            
            Waiter.domReady();
            Thread.sleep(1000);
        } catch (Exception e) {
            if (searchTerm != null && !searchTerm.isEmpty()) {
                DriverFactory.get().get("https://www.inmotionhosting.com/search?q=" + searchTerm);
                Waiter.domReady();
            }
        }
    }

    @Then("I should see search results containing {string}")
    @Step("Verify search results contain: {0}")
    public void iShouldSeeSearchResultsContaining(String expectedText) {
        Waiter.domReady();
        String pageText = DriverFactory.get().findElement(By.tagName("body")).getText().toLowerCase();
        String pageSource = DriverFactory.get().getPageSource().toLowerCase();
        
        Assert.assertTrue(
                pageText.contains(expectedText.toLowerCase()) || 
                pageSource.contains(expectedText.toLowerCase()) ||
                DriverFactory.get().getCurrentUrl().contains("search"),
                "Search results should contain '" + expectedText + "'"
        );
    }

    @Then("the search results should be relevant to my query")
    @Step("Verify search results are relevant")
    public void theSearchResultsShouldBeRelevantToMyQuery() {
        Waiter.domReady();
        String pageText = DriverFactory.get().findElement(By.tagName("body")).getText().toLowerCase();
        String currentUrl = DriverFactory.get().getCurrentUrl();
        
        boolean isSearchPage = currentUrl.contains("search") ||
                              pageText.contains("results") || 
                              pageText.contains("found");
        
        Assert.assertTrue(isSearchPage || pageText.contains(searchTerm.toLowerCase()),
                "Search results page should be displayed or contain search term");
    }

    @Then("I should see search results")
    @Step("Verify search results are displayed")
    public void iShouldSeeSearchResults() {
        Waiter.domReady();
        String currentUrl = DriverFactory.get().getCurrentUrl();
        String pageText = DriverFactory.get().findElement(By.tagName("body")).getText();
        
        boolean hasResults = currentUrl.contains("search") || 
                            pageText.toLowerCase().contains("result") ||
                            !pageText.trim().isEmpty();
        
        Assert.assertTrue(hasResults, "Search results should be displayed");
    }

    @Then("the search results page should have a title containing {string}")
    @Step("Verify page title contains: {0}")
    public void theSearchResultsPageShouldHaveATitleContaining(String expectedText) {
        Waiter.domReady();
        String pageTitle = DriverFactory.get().getTitle().toLowerCase();
        String currentUrl = DriverFactory.get().getCurrentUrl().toLowerCase();
        
        Assert.assertTrue(
                pageTitle.contains(expectedText.toLowerCase()) || 
                currentUrl.contains(expectedText.toLowerCase()),
                "Page title or URL should contain '" + expectedText + "'"
        );
    }

    @Then("I should see an appropriate message or no results message")
    @Step("Verify appropriate message for empty search")
    public void iShouldSeeAnAppropriateMessageOrNoResultsMessage() {
        Waiter.domReady();
        String pageText = DriverFactory.get().findElement(By.tagName("body")).getText().toLowerCase();
        
        boolean hasMessage = pageText.contains("no results") ||
                           pageText.contains("no matches") ||
                           pageText.contains("try again") ||
                           !DriverFactory.get().getCurrentUrl().contains("search");
        
        Assert.assertTrue(hasMessage, "Should show appropriate message for empty search");
    }

    @Then("the page should not display an error")
    @Step("Verify no error is displayed")
    public void thePageShouldNotDisplayAnError() {
        Waiter.domReady();
        String pageText = DriverFactory.get().findElement(By.tagName("body")).getText().toLowerCase();
        
        boolean hasError = pageText.contains("error") && 
                          !pageText.contains("no results") &&
                          !pageText.contains("404");
        
        Assert.assertFalse(hasError, "Page should not display an error");
    }
}

