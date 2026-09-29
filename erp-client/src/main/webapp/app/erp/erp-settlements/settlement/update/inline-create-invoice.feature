Feature: Create an Invoice inline from the Settlement form

  As a finance user filling in a Settlement, when the Invoice I need to attach
  does not exist yet, I want to create it without losing the Settlement I am
  already editing - even when that Settlement is itself being created inline
  from a Prepayment Account.

  Scenario: Invoice does not exist yet, so I create one inline and return with it added
    Given I am editing a Settlement with payment number "DC317"
    When I choose "Create New" on the Invoices field
    Then a new-Invoice request is recorded for the "paymentInvoices" field
    And I am taken to the Invoice create form
    And the Invoice create form knows it is in create mode

  Scenario: Saving the inline-created Invoice returns me to the Settlement with it added
    Given I am editing a Settlement with payment number "DC317"
    And I have chosen "Create New" on the Invoices field
    When I save the new Invoice numbered "INV-2026-001"
    Then the new-Invoice request is marked complete with the saved Invoice
    And I am returned to the Settlement I was editing
    And the Invoices field includes the saved Invoice
    And no pending or completed inline-create requests remain
