# Inline "Create New" for Settlement (from Prepayment Account) and Invoice (from Settlement)

## Why

Filling in a Prepayment Account's "Prepayment Transaction" field means picking an existing
`Settlement` by its payment number (e.g. `DC317`). When that Settlement doesn't exist yet, the
`jhi-m21-settlement-form-control` picker already offered a "Create New" link, but it did nothing
more than `router.navigate(['settlement/extension/new'])` - a bare navigation with no way back.
The Settlement form's own Invoices picker had no "Create New" option at all. Two problems needed
fixing before either was usable:

1. **The destination forms were themselves broken.** `settlement-new-routing-resolve.service.ts`
   only dispatched the action that sets the Settlement form's `weAreCreating` flag (which its Save
   button is gated on) inside its `if (id)` branch - a genuinely new Settlement has no id, so the
   Save button never rendered. `payment-invoice-update.component.ts`'s `new` route has no resolver
   at all, so it depended entirely on whichever action last happened to set its own
   `weAreCreating` - correct only when reached via that entity's own list-page "Create" button.
   Both are fixed directly in each component now, independent of the inline-create mechanism.
2. **Nothing carried the newly-created record back to the field that asked for it.**

## First attempt: a generic, entity-agnostic NgRx slice (superseded)

The first implementation used one shared `inlineCreateReturnState` slice keyed by a generated
`correlationId`, carrying a `returnUrl` string to `router.navigateByUrl()` back to once the child
form saved. It worked for the Invoice-from-Settlement case in testing, but a live report surfaced
the same "lands on the dashboard instead of the parent form" failure for the Settlement-from-
Prepayment-Account case specifically, with the exact mechanism not fully isolated (the
`correlationId` round-tripped correctly per the visible URL; the most likely explanation was some
still-unproven timing/subscription-order gap specific to that path). Given a live, hard-to-pin-
down bug in a shared mechanism, and given this project's own preference for explicit, traceable
NgRx state over compact-but-opaque generic mechanisms, the whole thing was rebuilt rather than
patched further.

## Current design: one dedicated slice per child entity type

`settlementInlineCreateState` and `paymentInvoiceInlineCreateState`
(`store/reducers/settlement-inline-create.reducer.ts`,
`store/reducers/payment-invoice-inline-create.reducer.ts`) are separate, near-identical slices -
one per child entity type, not one shared slice keyed by a correlation id. Each holds:

```ts
interface XInlineCreateState {
  active: boolean;
  targetField: string;
  parentRoute: string;
  parentFormSnapshot: unknown;   // the PARENT's own full form draft, not just a URL
  createdX: IX | null;
}
```

Storing the parent's own full draft (via its existing `createFromForm()`) rather than just a
return URL is the key difference from the first attempt: returning to a route only gets Angular to
mount the right component - it does nothing about whatever the user had already typed into that
form before navigating away to create the child record. Without capturing and restoring that
draft, the parent form reconstructs empty (or from whatever the route's own resolver provides),
regardless of whether the return-navigation itself succeeds.

**The picker components own nothing.** `M21SettlementFormControlComponent` and
`M2MPaymentInvoiceFormControlComponent` no longer inject `Store` or `Router` at all - `createNew()`
just emits `@Output() createNewRequested`. They have no way to know what the owning form's other
fields currently hold, so they cannot meaningfully be the ones to snapshot-and-navigate.

**The owning parent form drives everything.** E.g.
`PrepaymentAccountUpdateComponent.createSettlementInline()`:

```ts
createSettlementInline(): void {
  this.store.dispatch(settlementInlineCreateStarted({
    targetField: 'prepaymentTransaction',
    parentRoute: this.router.url,
    parentFormSnapshot: this.createFromForm(),
  }));
  this.router.navigate(['settlement/extension/new']);
}
```

**The child create form reports back and navigates to the stored parent route** (not
`window.history.back()`) on successful save:

```ts
protected onSaveSuccess(saved: ISettlement | null): void {
  if (this.weAreInlineCreating && saved) {
    this.store.dispatch(settlementInlineCreateCompleted({ createdSettlement: saved }));
    this.router.navigateByUrl(this.inlineParentRoute || '/');
    return;
  }
  this.previousState();
}
```

**On return, the parent restores its own draft, then patches in the new entity, in that order**:

```ts
this.store.pipe(select(settlementInlineCreateActive), take(1)).subscribe(active => {
  if (!active) { return; }
  combineLatest([
    this.store.pipe(select(settlementInlineCreateParentFormSnapshot)),
    this.store.pipe(select(settlementInlineCreateCreatedSettlement)),
  ]).pipe(take(1)).subscribe(([snapshot, createdSettlement]) => {
    if (snapshot) { this.updateForm(snapshot as IPrepaymentAccount); }
    if (createdSettlement) { this.updateSettlement(createdSettlement); }
    this.store.dispatch(settlementInlineCreateConsumed());
  });
});
```

This restore call is deliberately placed *after* `updateDetailsGivenTransaction()` (the
`valueChanges` listener that auto-fills dealer/currency/amount from whatever Settlement ends up in
`prepaymentTransaction`) in `ngOnInit()`, not in the constructor. NgRx selectors emit synchronously
on subscribe when the store already holds a matching value - exactly the case here, since the
completion was dispatched moments before this component was reconstructed - so a subscription any
earlier fires its `patchValue()` before that listener exists, and the resulting auto-fill silently
never happens. This exact ordering bug was caught (and fixed the same way) in both directions: the
Prepayment Account side for its Settlement field, and the Settlement side for its own Invoices
field, which recalculates the payment amount from the sum of its invoices.

**Settlement plays both roles.** It is a *child* of Prepayment Account (created inline, reports
back via `settlementInlineCreate*`) and, independently, a *parent* of Invoice (drives
`createPaymentInvoiceInline()`, restores its own draft via `paymentInvoiceInlineCreate*` on
return). The two slices never interact with each other; nesting works simply because each level
uses its own dedicated slice.

## Not yet covered

- Purchase Order (from Invoice) and Business Document pickers don't have this treatment yet -
  same pattern, not yet built.
- Clicking Cancel on an inline-created child form doesn't restore/return anything - only a
  successful Save completes the round trip. The `xInlineCreateCancelled` action exists in each
  reducer for this but nothing dispatches it yet.

## Verification

`npx tsc -p tsconfig.app.json --noEmit` and `npx eslint` pass clean on every touched file. Jest
still cannot run at all in this local environment (confirmed pre-existing and unrelated - even
original, untouched spec files fail to parse). The BDD coverage for this redesign
(`store/reducers/settlement-inline-create.steps.spec.ts`,
`store/reducers/payment-invoice-inline-create.steps.spec.ts`) tests the reducers directly - pure
functions, no Angular TestBed/DI involved - specifically because that removes the DI-resolution
risk that made the previous attempt's component-level TestBed tests unverifiable in this
environment. The component-level orchestration (capture-snapshot-and-navigate,
restore-then-patch-in-correct-order) is verified by direct code review of the exact methods shown
above, not by an executable test.
