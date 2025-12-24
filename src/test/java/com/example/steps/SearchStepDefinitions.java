package com.example.steps;

import com.example.pages.PageFactoryManager;
import com.example.pages.SearchResultsPage;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.qameta.allure.Step;
import org.testng.Assert;

public class SearchStepDefinitions {

    private SearchResultsPage searchResultsPage;
    private String searchTerm;

    public SearchStepDefinitions() {
        this.searchResultsPage = PageFactoryManager.get(SearchResultsPage.class);
    }

    @When("I enter {string} in the search field")
    @Step("Enter search term: {0}")
    public void iEnterInTheSearchField(String searchTerm) {
        this.searchTerm = searchTerm;
        searchResultsPage.enterSearchTerm(searchTerm);
    }

    @When("I submit the search")
    @Step("Submit search")
    public void iSubmitTheSearch() {
        searchResultsPage.submitSearch(searchTerm);
    }

    @Then("I should see search results containing {string}")
    @Step("Verify search results contain: {0}")
    public void iShouldSeeSearchResultsContaining(String expectedText) {
        Assert.assertTrue(searchResultsPage.hasResultsContaining(expectedText),
                "Search results should contain '" + expectedText + "'");
    }

    @Then("the search results should be relevant to my query")
    @Step("Verify search results are relevant")
    public void theSearchResultsShouldBeRelevantToMyQuery() {
        Assert.assertTrue(searchResultsPage.isRelevantTo(searchTerm),
                "Search results page should be displayed or contain search term");
    }

    @Then("I should see search results")
    @Step("Verify search results are displayed")
    public void iShouldSeeSearchResults() {
        Assert.assertTrue(searchResultsPage.hasResults(), "Search results should be displayed");
    }

    @Then("the search results page should have a title containing {string}")
    @Step("Verify page title contains: {0}")
    public void theSearchResultsPageShouldHaveATitleContaining(String expectedText) {
        Assert.assertTrue(searchResultsPage.hasTitleContaining(expectedText),
                "Page title or URL should contain '" + expectedText + "'");
    }

    @Then("I should see an appropriate message or no results message")
    @Step("Verify appropriate message for empty search")
    public void iShouldSeeAnAppropriateMessageOrNoResultsMessage() {
        Assert.assertTrue(searchResultsPage.hasNoResultsMessage(),
                "Should show appropriate message for empty search");
    }

    @Then("the page should not display an error")
    @Step("Verify no error is displayed")
    public void thePageShouldNotDisplayAnError() {
        Assert.assertFalse(searchResultsPage.hasError404(), "Page should not display an error");
    }
}
