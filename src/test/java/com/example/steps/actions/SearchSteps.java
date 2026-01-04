package com.example.steps.actions;

import com.example.pages.PageFactoryManager;
import com.example.pages.SearchResultsPage;
import io.qameta.allure.Step;
import org.testng.Assert;

public class SearchSteps {

    private SearchResultsPage searchResultsPage;

    public SearchSteps() {
        // Ленивая инициализация - страница будет создана при первом использовании
    }

    private SearchResultsPage page() {
        if (searchResultsPage == null) {
            searchResultsPage = PageFactoryManager.get(SearchResultsPage.class);
        }
        return searchResultsPage;
    }

    @Step("Enter search term: {0}")
    public void enterSearchTerm(String searchTerm) {
        page().enterSearchTerm(searchTerm);
    }

    @Step("Submit search")
    public void submitSearch(String searchTerm) {
        page().submitSearch(searchTerm);
    }

    @Step("Verify search results contain: {0}")
    public void verifyResultsContain(String expectedText) {
        Assert.assertTrue(page().hasResultsContaining(expectedText),
                "Search results should contain '" + expectedText + "'");
    }

    @Step("Verify search results are relevant to: {0}")
    public void verifyResultsAreRelevant(String searchTerm) {
        Assert.assertTrue(page().isRelevantTo(searchTerm),
                "Search results page should be displayed or contain search term");
    }

    @Step("Verify search results are displayed")
    public void verifyResultsDisplayed() {
        Assert.assertTrue(page().hasResults(), "Search results should be displayed");
    }

    @Step("Verify page title contains: {0}")
    public void verifyPageTitleContains(String expectedText) {
        Assert.assertTrue(page().hasTitleContaining(expectedText),
                "Page title or URL should contain '" + expectedText + "'");
    }

    @Step("Verify appropriate message for empty search")
    public void verifyNoResultsMessage() {
        Assert.assertTrue(page().hasNoResultsMessage(),
                "Should show appropriate message for empty search");
    }

    @Step("Verify no error is displayed")
    public void verifyNoErrorDisplayed() {
        Assert.assertFalse(page().hasError404(), "Page should not display an error");
    }
}
