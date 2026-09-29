Feature: Settlement inline-create workflow state

  The Prepayment Account form captures its own draft and starts this workflow before
  navigating to the Settlement create form; the Settlement form reports what it saved back
  into this same slice and navigates back. Neither side talks to the other directly - this
  slice is the only handoff, so its state transitions are what the whole "create the missing
  Settlement without losing my place" user story rests on.

  Scenario: Starting a Settlement inline-create captures the parent's draft
    Given a Prepayment Account form has draft values entered
    When it starts an inline Settlement create for the "prepaymentTransaction" field
    Then the workflow is active
    And the parent's draft is stored for later restoration
    And no Settlement has been created yet

  Scenario: Completing the Settlement inline-create records what was saved
    Given a Settlement inline-create is active
    When the Settlement form reports a saved Settlement
    Then the created Settlement is available to the parent form
    And the workflow is still active until explicitly consumed

  Scenario: Consuming the completion resets the workflow
    Given a Settlement inline-create has a completed Settlement
    When the parent form consumes the completion
    Then the workflow is no longer active
    And no draft or created Settlement remains

  Scenario: Cancelling an inline-create resets the workflow
    Given a Settlement inline-create is active
    When it is cancelled
    Then the workflow is no longer active
