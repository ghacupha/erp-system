Updated 2024-03-27

**Depreciation Module Handling of Disposed and Written-off Assets in the ERP System**

``` 
Implementation in the milestone marker Mark X No.6 (Jehoiada Series) 
```

**Introduction:**
The depreciation module within our ERP system plays a crucial role in accurately calculating asset depreciation over time. It supports point-in-time calculations, meaning that previous net book value or depreciation amounts are not required. Instead, it relies on asset cost and capitalization date to compute depreciation.

**Handling Disposed and Written-off Assets:**
In our system, depreciation is not implemented by modifying the asset registration entity itself. Instead, depreciation amounts are calculated for each instance and stored as depreciation entries based on the requested depreciation period. This approach ensures that asset registration remains pseudo-immutable, preserving the integrity of transactional relationships affecting asset values.

Similarly, for disposed and written-off assets, we adopt a non-destructive approach. Rather than deleting the asset registration instance, we create instances in the asset write-off or disposal entities, specifying the effective period of disposal or write-off. The amount of these instances is subtracted from the asset cost during depreciation calculation, depending on whether the requested period falls within the disposal or write-off effective period.

**Advantages of the Approach:**
1. **Accuracy of Previous Periods:** By retaining historical data and applying depreciation calculations based on effective periods, our approach ensures the accuracy of previous depreciation periods. This is vital for financial reporting and compliance requirements.

2. **Support for Professional Standards:** Our method aligns with professional standards that mandate the preservation of unaltered records for a specified period, typically 10 years. This ensures that organizations using our system can meet regulatory obligations and maintain trust with stakeholders.

**Demerits of Alternative Approaches:**
1. **Inaccuracy of Previous Periods:** Deleting asset records or modifying asset costs would compromise the accuracy of previous depreciation periods, making historical financial data unreliable.

2. **Non-compliance with Professional Standards:** Adopting a policy that does not support the preservation of unaltered records contradicts the principles upheld by many professional organizations. This could lead to regulatory issues and undermine organizational credibility.

**Application in Ongoing Development:**
The same principle of non-destructive handling applies to ongoing developments in asset adjustment and revaluation entities. These entities allow recording adjustments resulting from audits, revaluations, or asset damage without necessarily writing off the asset. Effective periods are applied to ensure accurate calculation based on the asset's original cost and capitalization date.

**Conclusion:**
In conclusion, the depreciation module's handling of disposed and written-off assets within our ERP system ensures the accuracy and integrity of depreciation calculations. By adopting a non-destructive approach and adhering to professional standards, we enable organizations to maintain accurate financial records and comply with regulatory requirements while supporting ongoing development and adjustments in asset management.
