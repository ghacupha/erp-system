import { createAction, props } from '@ngrx/store';

export const setSelectedLiabilityEnumeration = createAction(
  '[Liability Enumeration] Set Selected Liability Enumeration',
  props<{ liabilityEnumerationId?: number | null; bookingId?: string | null }>()
);
