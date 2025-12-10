---

## Prepayments Module Manual

### Introduction

This manual is a comprehensive guide to using the Prepayments Module within our ERP system. The Prepayments Module assists in managing and amortizing prepaid expenses efficiently.

### Key Concepts

#### Prepayment
- Definition: Expense items paid in advance.
- Purpose: Handling amortization and tracking outstanding balances.

#### Entities Involved
- **Prepayment Account:** Record prepayment details like particulars, settlement, dealer information, and accounts.
- **Prepayment Marshalling:** Define amortization specifics for prepayment accounts.
- **Compilation Request:** Generate monthly amortization entries based on marshalled data.
- **Prepayment Amortization:** Monthly amortization entities for outstanding balance calculations.
- **Fiscal Month:** Define periods for amortization calculations.

### Workflow Overview

1. **Prepayment Account Creation:**
    - Access the Prepayment Account module to create a new account.
    - Input details: catalogue number, particulars, settlement, dealer information, and related accounts.

2. **Prepayment Marshalling:**
    - Define amortization details for a prepayment account.
    - Specify the number of monthly instalments and initial amortization period using Fiscal Months.
    - Save and compile to generate monthly instalments.

3. **Compilation Process:**
    - Access Compilation Request module.
    - Create amortization entities from the first defined fiscal month to the final one.
    - Compilation ensures accurate amortization calculations for reports.

4. **Generating Outstanding Balance Reports:**
    - Query reports using the end date of a specific period.
    - Retrieve outstanding balance details at the end of a chosen fiscal period.

5. **Managing Compilation and Marshalling:**
    - If issues occur with amortization entries, delete the compilation item.
    - Adjust marshalling configuration as needed and rerun the compilation.
    - Mark testing marshalling items as inactive after testing to avoid unexpected entries.

### Conclusion

The Prepayments Module in our ERP system simplifies the tracking and amortization of prepaid expenses. By managing prepayment accounts, defining amortization details, and generating reports, users can effectively handle prepayments and outstanding balances.
