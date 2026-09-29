# Inline "Create New" for Settlement (from Prepayment Account) and Invoice (from Settlement)

## Why

Filling in a Prepayment Account's "Prepayment Transaction" field means picking an existing
`Settlement` by its payment number (e.g. `DC317`). When that Settlement doesn't exist yet, the
`jhi-m21-settlement-form-control` picker already offered a "Create New" link
(`M21SettlementFormControlComponent.createNew()`), but it did nothing more than
`router.navigate(['settlement/extension/new'])` - a bare navigation with no way back. Two problems
followed:

1. **The destination form was itself broken.** `settlement-new-routing-resolve.service.ts` only
   dispatched `settlementCreationWorkflowInitiatedEnRoute()` (which sets the NgRx
   `weAreCreating` flag that the update form's Save button is gated on -
   `[hidden]='!weAreCreatingAPayment'` in `settlement-update.component.html`) inside its `if (id)`
   branch. A genuinely new Settlement has no id, so that branch never ran, `weAreCreating` stayed
   `false`, and the Save button never rendered - the only option was Cancel.
2. **Nothing carried the newly-created Settlement back.** Even with a working Save button, the
   user would land back on a blank Settlement list, not the Prepayment Account they were editing,
   with no way to get the new Settlement into the field that sent them there.

The same two problems existed one level deeper: the Settlement form's own Invoices picker
(`jhi-m2m-payment-invoice-form-control`) had no "Create New" option at all, and
`payment-invoice-update.component.ts`'s `new` route has no resolver, so it depended entirely on
whichever action last happened to set `weAreCreating` - correct only when reached via the invoice
list page's own "Create" button.

## What changed

**A generic, entity-agnostic NgRx slice carries the round trip.** `inlineCreateReturnState`
(`store/reducers/inline-create-return.reducer.ts`) is a map keyed by `correlationId`, not by
entity type, specifically so a *nested* inline-create (create an Invoice while creating a
Settlement while editing a Prepayment Account) doesn't clobber the outer one:

```ts
interface InlineCreateReturnState {
  pending: { [correlationId: string]: PendingInlineCreate };   // { entityType, targetField, returnUrl }
  completed: { [correlationId: string]: CompletedInlineCreate }; // { entityType, targetField, createdEntity }
}
```

Four actions (`store/actions/inline-create-return.actions.ts`): `inlineCreateRequested`,
`inlineCreateCompleted`, `inlineCreateConsumed`, `inlineCreateCancelled`. `generateCorrelationId()`
(`store/util/correlation-id.util.ts`) wraps `crypto.randomUUID()` with a fallback for
environments without it.

**Picker side (producer of the request):** `M21SettlementFormControlComponent.createNew()` and the
newly-added `M2MPaymentInvoiceFormControlComponent.createNew()` both dispatch
`inlineCreateRequested` with a fresh correlationId, `entityType`, the owning form's
`targetFormField` (a new `@Input`, distinct from the display `inputControlLabel` - see
`prepayment-account-update.component.html`'s `targetFormField='prepaymentTransaction'` and
`settlement-update.component.html`'s `targetFormField='paymentInvoices'`), and `router.url` as the
`returnUrl`, then navigates to the create route with `?correlationId=...` in the query string (so
it survives a full page reload, which component-local state wouldn't).

**Create form side (consumer of the request, producer of the completion):**
`SettlementUpdateComponent` and `PaymentInvoiceUpdateComponent` both read `correlationId` from
`ActivatedRoute.snapshot.queryParamMap` in their constructor, and if present subscribe to
`selectPendingInlineCreate(correlationId)` to learn the pending record's `returnUrl`/`targetField`.
`subscribeToSaveResponse` now passes the saved entity through to `onSaveSuccess`, which - only when
an inline-create is in progress - dispatches `inlineCreateCompleted` with the saved entity and
`router.navigateByUrl(returnUrl)` instead of the normal `previousState()` (`window.history.back()`).

**Originating form side (consumer of the completion):** `PrepaymentAccountUpdateComponent` and
`SettlementUpdateComponent` (which plays both roles - producer for its own creation, consumer for
its Invoices field) subscribe to `selectCompletedInlineCreatesForField(targetField)` in their
constructor. On a completion: patch the field (`updateSettlement` for the single-valued Settlement
reference; `updatePaymentInvoices([...existing, created])` for the many-valued Invoices array,
since that field is a `@ManyToMany`), then dispatch `inlineCreateConsumed` to clear the slice
entry. Since navigating from the create route back to the originating route is a different Angular
route, the originating component is freshly reconstructed, so this subscription picks up the
still-in-store completion on its very first tick.

## Two pre-existing bugs fixed as part of this

- **`settlement-new-routing-resolve.service.ts`**: added the missing
  `settlementCreationWorkflowInitiatedEnRoute()` dispatch to the no-id branch. This is the actual
  fix for the reported "no Save button on `/settlement/extension/new`" bug - independent of
  the inline-create-and-return feature, any direct navigation to a new Settlement had this problem.
- **`payment-invoice-update.component.ts`**: the `new` route has no resolver at all, so the
  constructor now dispatches `paymentInvoiceCreationInitiatedEnRoute()` itself whenever there's no
  `:id` route param. `paymentInvoiceCreationInitiatedEnRoute` already existed in
  `payment-invoice-workflow-status.action.ts` and was already handled correctly by the reducer -
  it was simply never dispatched anywhere before this change.

## Verification

`npx tsc -p tsconfig.app.json --noEmit` and `npx eslint` both pass clean on every touched file.
**Could not run the Jest suite to verify runtime behavior** - confirmed this is a pre-existing,
unrelated environment problem, not something this change caused: even the original, untouched
`prepayment-account-update.component.spec.ts` fails with `SyntaxError: Cannot use import statement
outside a module` (the local `ts-jest` transform isn't engaging), and `npx tsc -p
tsconfig.spec.json` independently reports dozens of pre-existing type errors across unrelated spec
files throughout the codebase. The new BDD spec files (see the companion user story) type-check and
lint clean, and were written to mirror the exact same TestBed/provider patterns as the existing
(also-currently-unrunnable) specs, but their actual pass/fail status is unverified in this session.
