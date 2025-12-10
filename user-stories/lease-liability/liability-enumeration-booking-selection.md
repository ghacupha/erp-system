# Liability enumeration present value navigation retains booking

**Persona:** Lease manager reviewing present value calculations.

**Goal:** Keep the lease booking id visible when moving from the enumeration list to the present value view.

**Flow:**

1. Open the Liability Enumeration list and locate a row with the desired lease.
2. Click "View present values"; the app dispatches the selection to the NgRx store and navigates to the present value screen.
3. Confirm the header shows the stored booking id immediately, even before the API response finishes loading, and remains consistent after data is fetched.
