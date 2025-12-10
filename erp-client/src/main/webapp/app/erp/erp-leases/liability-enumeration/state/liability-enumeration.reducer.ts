import { createReducer, on } from '@ngrx/store';
import { setSelectedLiabilityEnumeration } from './liability-enumeration.actions';

export const liabilityEnumerationFeatureKey = 'liabilityEnumeration';

export interface LiabilityEnumerationState {
  selectedLiabilityEnumerationId?: number | null;
  selectedBookingId?: string | null;
}

export const initialLiabilityEnumerationState: LiabilityEnumerationState = {
  selectedLiabilityEnumerationId: null,
  selectedBookingId: null,
};

export const liabilityEnumerationReducer = createReducer(
  initialLiabilityEnumerationState,
  on(setSelectedLiabilityEnumeration, (state, { liabilityEnumerationId, bookingId }) => ({
    ...state,
    selectedLiabilityEnumerationId: liabilityEnumerationId ?? null,
    selectedBookingId: bookingId ?? null,
  }))
);
