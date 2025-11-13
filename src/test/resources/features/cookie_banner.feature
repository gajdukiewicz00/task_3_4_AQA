@bdd
Feature: Cookie Banner Behavior
  As a user
  I want to control cookie preferences
  So that I can manage my privacy settings

  Background:
    Given I am on the InMotion Hosting homepage

  @defect-2 @cookie
  Scenario: Cookie banner should be dismissible and stay dismissed
    When I see the cookie banner
    Then the cookie banner should be visible
    When I accept or dismiss the cookie banner
    Then the cookie banner should disappear
    And when I refresh the page
    Then the cookie banner should remain dismissed

  @defect-2 @cookie
  Scenario: Cookie banner should have accept and decline options
    When I see the cookie banner
    Then the cookie banner should contain an "Accept" or "Agree" button
    And the cookie banner should contain a "Decline" or "Reject" button
    When I click on the "Accept" button
    Then the cookie banner should be dismissed
    And cookies should be set in the browser

  @defect-2 @cookie
  Scenario Outline: Cookie banner should work with different actions
    When I see the cookie banner
    And I click on the "<action>" button
    Then the cookie banner should be dismissed
    And the page should continue to function normally

    Examples:
      | action |
      | Accept |
      | Agree  |
      | Close  |
      | X      |

