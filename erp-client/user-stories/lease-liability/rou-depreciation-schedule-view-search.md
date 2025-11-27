# Story: Accountant opens a depreciation schedule via contract search

**Persona:** Lease accountant preparing month-end journals.

**Steps**
1. Navigate to the ROU depreciation schedule view.
2. Use the lease contract search field to type part of a booking ID or title and pick the matching lease from the suggestions.
3. The page navigates to `/rou-depreciation-schedule-view/<contractId>` and reloads the depreciation rows.

**Expected result**
- The selected lease remains visible in the search control even after the route reloads.
- The depreciation table and export actions reflect the newly selected contract.
