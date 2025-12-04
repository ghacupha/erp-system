# Liability enumeration form control updates

## Overview

The liability enumeration create form now relies on reusable NgSelect-based controls instead of manual numeric IDs. Lease contracts reuse the IFRS16 lease control, while lease payment uploads use a new M21 form control backed by a search endpoint that filters by the selected contract.

## Behaviour

- Selecting a lease contract updates the request payload and clears any incompatible upload selection.
- Lease payment uploads load via search suggestions (with optional contract filtering) and push the upload ID back into the form once chosen.
- The time granularity selector now includes a bi-annual option and clarifies that discounting always happens monthly, inserting zero-value months between non-monthly payments.

## Usage

1. Open **Maintenance > Liability enumeration > Create**.
2. Search and select an IFRS16 lease contract using the NgSelect control.
3. Search for the relevant lease payment upload (filtered by the chosen contract) and select it.
4. Enter the annual interest rate; keep the payment frequency that matches settlements.
5. Submit to launch enumeration; monthly discounting will be applied automatically.
