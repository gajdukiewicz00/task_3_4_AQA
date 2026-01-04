package com.example.steps;

import com.example.steps.actions.SearchSteps;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class SearchStepDefinitions {

    private final SearchSteps searchSteps;
    private String searchTerm;

    public SearchStepDefinitions() {
        this.searchSteps = new SearchSteps();
    }

    @When("I enter {string} in the search field")
    public void iEnterInTheSearchField(String searchTerm) {
        this.searchTerm = searchTerm;
        searchSteps.enterSearchTerm(searchTerm);
    }

    @When("I submit the search")
    public void iSubmitTheSearch() {
        searchSteps.submitSearch(searchTerm);
    }

    @Then("I should see search results containing {string}")
    public void iShouldSeeSearchResultsContaining(String expectedText) {
        searchSteps.verifyResultsContain(expectedText);
    }

    @Then("the search results should be relevant to my query")
    public void theSearchResultsShouldBeRelevantToMyQuery() {
        searchSteps.verifyResultsAreRelevant(searchTerm);
    }

    @Then("I should see search results")
    public void iShouldSeeSearchResults() {
        searchSteps.verifyResultsDisplayed();
    }

    @Then("the search results page should have a title containing {string}")
    public void theSearchResultsPageShouldHaveATitleContaining(String expectedText) {
        searchSteps.verifyPageTitleContains(expectedText);
    }

    @Then("I should see an appropriate message or no results message")
    public void iShouldSeeAnAppropriateMessageOrNoResultsMessage() {
        searchSteps.verifyNoResultsMessage();
    }

    @Then("the page should not display an error")
    public void thePageShouldNotDisplayAnError() {
        searchSteps.verifyNoErrorDisplayed();
    }
}
