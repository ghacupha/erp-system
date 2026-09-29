Feature: Invoice inline-create workflow state

  The Settlement form (whether or not it is itself mid inline-create from a Prepayment
  Account) captures its own draft and starts this workflow before navigating to the Invoice
  create form; the Invoice form reports what it saved back into this same slice and
  navigates back. This slice is the only handoff between the two forms.

  Scenario: Starting an Invoice inline-create captures the Settlement's draft
    Given a Settlement form has draft values entered
    When it starts an inline Invoice create for the "paymentInvoices" field
    Then the workflow is active
    And the Settlement's draft is stored for later restoration
    And no Invoice has been created yet

  Scenario: Completing the Invoice inline-create records what was saved
    Given a PaymentInvoice inline-create is active
    When the Invoice form reports a saved Invoice
    Then the created Invoice is available to the Settlement form
    And the workflow is still active until explicitly consumed

  Scenario: Consuming the completion resets the workflow
    Given a PaymentInvoice inline-create has a completed Invoice
    When the Settlement form consumes the completion
    Then the workflow is no longer active
    And no draft or created Invoice remains

  Scenario: Cancelling an inline-create resets the workflow
    Given a PaymentInvoice inline-create is active
    When it is cancelled
    Then the workflow is no longer active
