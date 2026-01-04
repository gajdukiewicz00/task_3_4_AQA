@bdd
Feature: Search Functionality
  As a user
  I want to search for content on the website
  So that I can quickly find relevant information

  Background:
    Given I am on the InMotion Hosting homepage

  @defect-1 @search
  Scenario: Search should display relevant results for valid queries
    When I enter "hosting" in the search field
    And I submit the search
    Then I should see search results containing "hosting"
    And the search results should be relevant to my query

  @defect-1 @search
  Scenario Outline: Search should handle different search terms
    When I enter "<search_term>" in the search field
    And I submit the search
    Then I should see search results
    And the search results page should have a title containing "<search_term>"

    Examples:
      | search_term |
      | vps         |
      | shared      |
      | domain      |
      | email       |

  @defect-1 @search
  Scenario: Search should handle empty search gracefully
    When I enter "" in the search field
    And I submit the search
    Then I should see an appropriate message or no results message
    And the page should not display an error

