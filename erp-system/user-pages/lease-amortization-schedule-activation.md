# Lease amortization schedule activation guide

- Navigate to **Leases → Lease Amortization Schedules**.
- New schedules are created as **Active** and automatically linked to the lease liability compilation that produced them (CSV upload or batch run).
- Use **Activate** or **Deactivate** to toggle the schedule. The system now updates three layers immediately: the schedule row, all compiled schedule items, and the parent compilation status. Buttons show a spinner until the request completes and surface inline errors if something fails.
- The edit form includes an **Active** checkbox (checked by default for new records) so you can pre-set the state when creating or updating a schedule.
