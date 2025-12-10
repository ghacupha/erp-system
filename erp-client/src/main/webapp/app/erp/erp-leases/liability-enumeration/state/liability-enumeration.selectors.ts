import { createFeatureSelector, createSelector } from '@ngrx/store';
import { LiabilityEnumerationState, liabilityEnumerationFeatureKey } from './liability-enumeration.reducer';

export const selectLiabilityEnumerationState = createFeatureSelector<LiabilityEnumerationState>(liabilityEnumerationFeatureKey);

export const selectSelectedLiabilityEnumerationId = createSelector(
  selectLiabilityEnumerationState,
  state => state.selectedLiabilityEnumerationId
);

export const selectSelectedLiabilityEnumerationBookingId = createSelector(
  selectLiabilityEnumerationState,
  state => state.selectedBookingId
);
