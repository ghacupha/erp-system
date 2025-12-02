# Liability enumeration screens

## Persona
- Lease accountant validating IFRS 16 cash flows.

## Flow
1. From the ERP leases navigation choose **Liability Enumeration** to open the list of past runs.
2. Click **Run Enumeration** to launch the form and supply lease contract ID, payment upload ID, annual interest rate text, granularity, and active flag.
3. Submit the form; if the batch detects invalid inputs the alert banner shows the error on the form or list view.
4. After a successful submission, return to the list and open the **Present values** link to review the discounted payment lines for that run.

## Expected outcome
- Validation failures (missing payments, wrong contract/upload pairing, negative rate) surface immediately as alerts so the accountant can retry.
- Present value rows reflect the calculated discount rate per period and payment amounts and match the batch output for auditing.
