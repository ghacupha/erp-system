# User Story: Discover and View Dynamic Lease Reports

## Narrative
As a lease accountant I want to search for specialised lease liability and ROU asset summaries from anywhere in the ERP so that I
can review balances and interest movements without memorising individual report URLs.

## Acceptance criteria
1. **Global discovery**
   - Given I am signed in, when I focus the navbar search input and type at least two characters, then I should see a dropdown of
     report titles sourced from the dynamic report catalogue.
   - Search results must exclude inactive metadata records and display titles, descriptions, and router slugs.
2. **Navigation**
   - When I select a report from the dropdown, the navbar collapses and the router navigates to `/reports/view/<slug>` without
     reloading the application.
   - If the metadata path is invalid, I am shown an error explaining that the configuration was not found.
3. **Filter awareness**
   - If a report requires a lease period, the summary view loads a list of recent periods, defaults to the latest, and enables me
     to pick another period via search.
   - If a report is granular to a specific lease liability, a second selector surfaces the available liabilities.
4. **Summary response**
   - After defaults are applied, the component calls the configured backend API, passing query parameters as defined by metadata.
   - A loading spinner is displayed while data is fetched. When results return, the table renders with dynamic columns derived
     from the payload.
   - If the API responds with no data, a message explains that no summary items matched the selected criteria.
5. **Error handling**
   - If metadata lookup fails, a descriptive message is shown and no API call is attempted.
   - If filter retrieval fails, the error is surfaced and the summary is not refreshed until the issue is resolved.
   - If the backend API cannot be reached, an alert informs the user to verify their selections and retry.

## Workflow trace
1. Navbar component emits debounced search terms to `ReportMetadataService.search`.
2. Selecting a result triggers `navigateToReport`, collapses the navbar, and routes to the report view module.
3. `ReportSummaryViewComponent` resolves metadata by `pagePath`, initialises filter services, and once complete, requests
   summary data through `ReportSummaryDataService`.
4. The service resolves the API endpoint, appends lease period/liability query parameters, and returns a normalised array of
   summary records for display.
