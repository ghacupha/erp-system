---

## Work-in-Progress Module Manual

### Introduction

This manual is focused on the usage of the Work-in-Progress (WIP) module in our ERP system. The WIP module allows for the tracking and management of work in progress within our organization.

### Workflow Overview

1. **Work-in-Progress Registration:**
    - Record the existence of a work-in-progress item by creating an entry in the `WorkInProgressRegistration` entity.

2. **Work-in-Progress Transfers:**
    - Record transfers of work-in-progress items by creating instances in the `WorkInProgressTransfer` entity.
    - Specify the related Work-in-Progress Registration item for each transfer.

3. **WIP Report Calculation:**
    - The WIP report calculates the outstanding amount on a registration instance by:
        - Taking the amount of the registration item.
        - Subtracting the sum of the amounts of related transfers.

### Steps for Usage

#### 1. Work-in-Progress Registration

To acknowledge the existence of a work-in-progress item:
- Access the Work-in-Progress Registration interface.
- Create a new entry with details:
    - Sequence number.
        - Particulars.
        - Currency code.
        - Payment Invoice.
        - Service Outlet.
        - Settlement (Details of the payment to the Supplier/Dealer payment).
        - Purchase order.
        - Delivery Note.
        - Job Sheet.
        - Dealer.
        - Asset Accessories (If any).
        - Asset Warranty (If Any).
        - Work Project (If configured).
        - Others....

#### 2. Recording Work-in-Progress Transfers

To record transfers related to a Work-in-Progress Registration item:
- Access the Work-in-Progress Transfer interface.
- Create a new transfer entry:
    - Specify the related Work-in-Progress Registration item.
    - Input transfer details:
        - Description.
        - Asset Number.
        - Transfer Amount.
        - Transfer Date.
        - Transfer Type (DEBIT_TRANSFER, REVERSAL, CREDIT_NOTE, CAPITALIZATION).
        - Asset Category
        - WIP Registration (configured above)
        - Others...

#### 3. Generating WIP Reports

To view the outstanding amounts on Work-in-Progress items:
- Access the WIP Report interface.
- Retrieve the report for:
    - Individual Work-in-Progress Registration items.
    - Calculate outstanding amounts based on registrations and related transfers.

### Conclusion

The Work-in-Progress module in our ERP system simplifies the management of work in progress items. By registering items and recording transfers, users can generate reports to track outstanding amounts effectively.

---
