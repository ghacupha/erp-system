---

### STRAIGHT LINE DEPRECIATION POLICY

1. **Depreciation Calculation Method:** The straight-line depreciation method will be used for calculating the
   depreciation of assets. Under this method, the cost of the asset will be evenly spread over its useful life,
   resulting in a consistent depreciation expense each period.

2. **Depreciation Rate:** The annual depreciation rate for each asset will be specified in basis points (bps). The
   depreciation rate will represent the percentage of the asset's cost that will be depreciated each year.

2. **Useful life:** The useful life of an asset is a consequence of the depreciation rate defined for the asset category
   to which the asset belongs calculated as ``` useful life (years) = 10000/depreciation-rate ```. For each unique
   instance of useful life definition, an asset-category is to be defined.

3. **Depreciation Period:** The depreciation period will be defined for each depreciation job, indicating the time span
   over which the asset's cost will be depreciated. The period will be expressed as a start date and an end date.

3. **Duration of the depreciation period and the fiscal month:** The durations of the depreciation period and the fiscal
   month entities once defined need to remain consistent through out the use of the depreciation module. Deviation from
   this consistency will result in undefined behavior.

4. **Maximum duration of depreciation period:** The duration of the depreciation period should not be longer than the
   shortest useful life of the asset categories. If the duration of the depreciation period is longer than the duration
   of the useful life of the asset categories, it will result in undefined behavior.

5. **Depreciation Start Date:** Depreciation will begin on the first day of the defined depreciation period. Any
   capitalization occurring before this start date will not result in depreciation for that period.

6. **Depreciation End Date:** Depreciation will cease on the last day of the defined depreciation period. Any
   capitalization occurring after this end date will not result in depreciation for that period.

7. **Minimum Depreciation Rate:** The minimum allowable annual depreciation rate is 0.01 basis points. This guarantees
   accuracy to 6 decimal places. Defining depreciation rates defined below this minimum will result in undefined
   behavior.

8. **Capitalization Date and Depreciation:** If an asset is capitalized within the defined depreciation period, the
   calculated depreciation amount will be the full depreciation for the depreciation period. The net book value of the
   asset prior to depreciation will be considered as the asset cost if the capitalization date is within the
   depreciation period, and the duration of the depreciation period is equal to or less than the duration of a fiscal
   month.

9. **Asset Fully Depreciated:** Once the net book value of an asset reaches zero, no further depreciation will be
   calculated, even if the asset remains in service beyond its useful life.

10. **Depreciation Recognition:** The calculated depreciation amount for each period will be recognized as an expense in
    the accounting records. This expense will be allocated to the appropriate expense account for reporting purposes.

11. **Consistency:** The same depreciation method, depreciation rate, and depreciation period will be applied
    consistently for all assets within the organization. Any changes to these parameters must be approved and documented
    by the relevant stakeholders.

12. **Documentation and Reporting:** All details related to the asset's depreciation, including the asset cost,
    depreciation rate, depreciation period, calculated depreciation amount, and net book value, will be documented and
    reported accurately in the organization's financial records and reports.

These policy statements will guide the implementation and usage of the straight-line depreciation method within the
implementation in ERP-System, ensuring consistency, accuracy, and compliance with accounting principles.

---
