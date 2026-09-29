Feature: Create a Settlement inline from the Prepayment Account form

  As a finance user filling in a Prepayment Account, when the Settlement
  ("Prepayment Transaction") I need does not exist yet, I want to create it
  without losing the Prepayment Account I am already editing.

  Scenario: Settlement does not exist yet, so I create one inline and return with it selected
    Given I am editing a Prepayment Account with catalogue number "20732"
    When I choose "Create New" on the Prepayment Transaction field
    Then a new-Settlement request is recorded for the "prepaymentTransaction" field
    And I am taken to the Settlement create form
    And the Settlement create form knows it is in create mode

  Scenario: Saving the inline-created Settlement returns me to the Prepayment Account with it filled in
    Given I am editing a Prepayment Account with catalogue number "20732"
    And I have chosen "Create New" on the Prepayment Transaction field
    When I save the new Settlement with payment number "DC317"
    Then the new-Settlement request is marked complete with the saved Settlement
    And I am returned to the Prepayment Account I was editing
    And the Prepayment Transaction field is filled in with the saved Settlement
    And no pending or completed inline-create requests remain
