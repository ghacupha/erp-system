# Lease Amortization Schedule activation workflow

## Background
Lease amortization schedules now carry an explicit link back to the lease liability compilation that produced them. The compilation's `active` flag mirrors the activation state of the related amortization schedules so that schedule items and compilation metadata stay aligned.

## Behavioural changes
- New amortization schedules default to `active = true` whether created via API, CSV upload, or batch compilation. The DTO now seeds the flag, and the internal save layer preserves the default when missing.
- A new `leaseLiabilityCompilation` many-to-one is present on `LeaseAmortizationSchedule`, populated during batch compilation and CSV uploads using the originating compilation id.
- Activating or deactivating a schedule now updates three layers in one call:
  - The schedule row itself.
  - All schedule items associated with the compilation (or the schedule when no compilation is attached).
  - The parent compilation's `active` flag, keeping the compilation status consistent with its items.

## Operational notes
- The Liquibase changelog `20241120090000_add_compilation_fk_to_LeaseAmortizationSchedule` adds the optional foreign key column and constraint.
- Query filtering supports `leaseLiabilityCompilationId` so API consumers can search schedules by their compilation.
- When troubleshooting status mismatches on the UI, verify the compilation id and activation headers; the backend will now persist the new state on both the schedule and its compilation without requiring a manual refresh.
