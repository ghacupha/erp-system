# Lease Handling User Stories

## Story 1 – Capture an IFRS 16 lease contract
**As** an IFRS 16 accountant
**I want** to register a new lease with its supporting documentation
**So that** downstream liability schedules and postings use complete and auditable source data

### Acceptance Criteria
- **Create workflow:** From the *IFRS 16 Lease Contracts* list I can launch the creation form using the primary action button.【F:erp-client/src/main/webapp/app/entities/leases/ifrs-16-lease-contract/list/ifrs-16-lease-contract.component.html†L17-L44】
- **Mandatory identifiers:** The contract cannot be saved without a unique booking ID, descriptive title, and lifecycle dates that match the persistence rules on the entity.【F:erp-system/src/main/java/io/github/erp/domain/IFRS16LeaseContract.java†L41-L96】
- **Control relationships:** I must associate the contract with the superintendent service outlet, main dealer, and the first/last reporting periods before submission.【F:erp-system/src/main/java/io/github/erp/domain/IFRS16LeaseContract.java†L97-L115】
- **Document retention:** I can attach both the legal contract and the calculation workbook so that reviewers can open them from the record later.【F:erp-system/src/main/java/io/github/erp/domain/IFRS16LeaseContract.java†L117-L164】

## Story 2 – Define the lease liability profile
**As** a treasury analyst
**I want** to configure the liability amount and amortisation drivers for each lease
**So that** the system can derive the correct schedule of principal and interest

### Acceptance Criteria
- **Access point:** From the *Lease Liabilities* list I can create or edit a liability linked to the contract captured in Story 1.【F:erp-client/src/main/webapp/app/entities/leases/lease-liability/list/lease-liability.component.html†L17-L72】
- **Key inputs enforced:** The form requires the lease identifier, capitalised amount, start/end dates, and effective interest rate defined in the entity constraints.【F:erp-system/src/main/java/io/github/erp/domain/LeaseLiability.java†L41-L90】
- **Amortisation linkage:** Each liability must reference a lease amortisation calculation that stores the interest methodology, lease amount, and number of periods that the schedule generator consumes.【F:erp-system/src/main/java/io/github/erp/domain/LeaseLiability.java†L92-L108】【F:erp-system/src/main/java/io/github/erp/domain/LeaseAmortizationCalculation.java†L41-L103】
- **Schedule availability:** Once saved, the amortisation schedule associated with the liability exposes per-period balances and cashflows tied to the lease and repayment periods for reporting.【F:erp-system/src/main/java/io/github/erp/domain/LeaseAmortizationSchedule.java†L41-L104】【F:erp-system/src/main/java/io/github/erp/domain/LeaseLiabilityScheduleItem.java†L41-L164】【F:erp-system/src/main/java/io/github/erp/domain/LeaseRepaymentPeriod.java†L41-L108】

## Story 3 – Generate compliant postings and reports
**As** a financial controller
**I want** to map the lease to accounting rules and produce the statutory journals and reports each period
**So that** lease balances flow to the general ledger with full traceability

### Acceptance Criteria
- **Rule maintenance:** I can configure recognition, interest accrual, and ROU asset rules that bind debit and credit accounts to the lease contract via unique identifiers.【F:erp-system/src/main/java/io/github/erp/domain/TALeaseRecognitionRule.java†L41-L124】【F:erp-system/src/main/java/io/github/erp/domain/TALeaseInterestAccrualRule.java†L41-L124】【F:erp-system/src/main/java/io/github/erp/domain/TARecognitionROURule.java†L41-L124】 The front-end exposes creation actions and cross-navigation into related leases and accounts.【F:erp-client/src/main/webapp/app/entities/accounting/ta-recognition-rou-rule/list/ta-recognition-rou-rule.component.html†L17-L92】
- **Posting initiation:** During close I can raise a lease liability posting report, attach or download the generated file, and confirm it is locked to the intended lease period.【F:erp-client/src/main/webapp/app/entities/leases/lease-liability-posting-report/list/lease-liability-posting-report.component.html†L17-L112】【F:erp-system/src/main/java/io/github/erp/domain/LeaseLiabilityPostingReport.java†L41-L118】
- **Audit trail:** The report metadata records who requested it, the checksum, tamper flag, and the lease period to support reconciliations and compliance reviews.【F:erp-system/src/main/java/io/github/erp/domain/LeaseLiabilityPostingReport.java†L65-L118】【F:erp-system/src/main/java/io/github/erp/domain/LeaseLiabilityByAccountReport.java†L65-L118】

