///
/// Erp System - Mark X No 11 (Jehoiada Series) Client 1.7.9
/// Copyright © 2021 - 2024 Edwin Njeru (mailnjeru@gmail.com)
///
/// This program is free software: you can redistribute it and/or modify
/// it under the terms of the GNU General Public License as published by
/// the Free Software Foundation, either version 3 of the License, or
/// (at your option) any later version.
///
/// This program is distributed in the hope that it will be useful,
/// but WITHOUT ANY WARRANTY; without even the implied warranty of
/// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
/// GNU General Public License for more details.
///
/// You should have received a copy of the GNU General Public License
/// along with this program. If not, see <http://www.gnu.org/licenses/>.
///

import { Action, createReducer, on } from '@ngrx/store';
import { initialState, State } from '../global-store.definition';
import {
  newSettlementCreationSequenceInitiatedFomList,
  settlementCopyWorkflowInitiatedEnRoute,
  settlementCopyWorkflowInitiatedFromDetails,
  settlementCopyWorkflowInitiatedFromList,
  settlementCreationWorkflowInitiatedEnRoute,
  settlementCreationWorkflowInitiatedFromList,
  settlementCreationWorkflowInitiatedFromUpdateFormOnInit,
  settlementCreationWorkflowRefreshedFromForm,
  settlementEditWorkflowInitiatedFromDetails,
  settlementEditWorkflowInitiatedFromList,
  settlementUpdateCancelButtonClicked,
  settlementUpdateCopyHasBeenFinalized,
  settlementUpdateEditHasBeenFinalized,
  settlementUpdateErrorHasOccurred, settlementUpdateFormHasBeenDestroyed,
  settlementUpdateInstanceAcquiredFromBackend,
  settlementUpdateInstanceAcquisitionFromBackendFailed,
  settlementUpdatePreviousStateMethodCalled,
  settlementUpdateSaveHasBeenFinalized
} from '../actions/settlement-update-menu.actions';
import { ISettlement } from '../../erp-settlements/settlement/settlement.model';

export const settlementUpdateFormStateSelector = 'settlementUpdateForm';

export interface SettlementsFormState {
  backEndFetchComplete: boolean;
  browserHasBeenRefreshed: boolean;
  selectedSettlement: ISettlement;
  weAreCopying: boolean;
  weAreEditing: boolean;
  weAreCreating: boolean;
}

const _settlementUpdateStateReducer = createReducer(
  initialState,

  on(settlementCreationWorkflowInitiatedFromList, (state) => ({
    ...state,
    settlementsFormState: {
      ...state.settlementsFormState,
      weAreCopying: false,
      weAreEditing: false,
      weAreCreating: true,
    }
  })),

  on(settlementCreationWorkflowInitiatedEnRoute, (state) => ({
    ...state,
    settlementsFormState: {
      ...state.settlementsFormState,
      weAreCopying: false,
      weAreEditing: false,
      weAreCreating: true,
    }
  })),

  on(settlementCreationWorkflowInitiatedFromUpdateFormOnInit, (state, {copiedPartialSettlement}) => ({
    ...state,
    settlementsFormState: {
      ...state.settlementsFormState,
      selectedSettlement: copiedPartialSettlement,
      weAreCopying: false,
      weAreEditing: false,
      weAreCreating: true,
      browserHasBeenRefreshed: true,
    }
  })),

  on(settlementCreationWorkflowRefreshedFromForm, (state, {copiedPartialSettlement}) => ({
    ...state,
    settlementsFormState: {
      ...state.settlementsFormState,
      selectedSettlement: copiedPartialSettlement,
      weAreCopying: false,
      weAreEditing: false,
      weAreCreating: true,
      browserHasBeenRefreshed: true,
    }
  })),

  on(settlementUpdateFormHasBeenDestroyed, (state, {copiedPartialSettlement}) => ({
    ...state,
    settlementsFormState: {
      ...state.settlementsFormState,
      selectedSettlement: copiedPartialSettlement,
      weAreCopying: false,
      weAreEditing: false,
      weAreCreating: false,
      browserHasBeenRefreshed: true,
    }
  })),

  on(settlementCopyWorkflowInitiatedFromDetails, (state, {copiedSettlement}) => ({
    ...state,
    settlementsFormState: {
      ...state.settlementsFormState,
      selectedSettlement: copiedSettlement,
      weAreCopying: true,
      weAreEditing: false
    }
  })),

  on(settlementCopyWorkflowInitiatedEnRoute, (state) => ({
    ...state,
    settlementsFormState: {
      ...state.settlementsFormState,
      // selectedSettlement: copiedSettlement,
      weAreCopying: true,
      weAreEditing: false,
      weAreCreating: false,
    }
  })),

  on(settlementCopyWorkflowInitiatedFromList, (state, {copiedSettlement}) => ({
    ...state,
    settlementsFormState: {
      ...state.settlementsFormState,
      selectedSettlement: copiedSettlement,
      weAreCopying: true,
      weAreEditing: false
    }
  })),

  on(settlementEditWorkflowInitiatedFromDetails, (state, {editedSettlement}) => ({
    ...state,
    settlementsFormState: {
      ...state.settlementsFormState,
      selectedSettlement: editedSettlement,
      weAreCopying: false,
      weAreEditing: true
    }
  })),

  on(settlementEditWorkflowInitiatedFromList, (state, {editedSettlement}) => ({
    ...state,
    settlementsFormState: {
      ...state.settlementsFormState,
      selectedSettlement: editedSettlement,
      weAreCopying: false,
      weAreEditing: true
    }
  })),

  on(newSettlementCreationSequenceInitiatedFomList, (state, {newSettlement}) => ({
      ...state,
      settlementsFormState: {
        ...state.settlementsFormState,
        selectedPayment: newSettlement,
        weAreCreating: true,
        weAreCopying: false,
        weAreEditing: false
      }
    }
  )),

  on(settlementUpdateInstanceAcquiredFromBackend, (state, { backendAcquiredSettlement }) => ({
    ...state,
    settlementsFormState: {
      ...state.settlementsFormState,
      selectedPayment: backendAcquiredSettlement,
      backEndFetchComplete: true,
    }
  })),

  on(settlementUpdateInstanceAcquisitionFromBackendFailed, (state, {error}) => ({
    ...state,
    settlementsFormState: {
      ...state.settlementsFormState,
      selectedPayment: {},
      errorMessage: error,
      backEndFetchComplete: false,
    }
  })),

  // TODO IMPLEMENT REMAINING SETTLEMENT STATES
  // on(paymentCopyButtonClicked, (state) => ({
  //   ...state,
  //   paymentsFormState: {
  //     ...state.paymentsFormState,
  //     weAreCopying: false
  //   }
  // })),

  on(settlementUpdatePreviousStateMethodCalled, state => ({
    ...state,
    settlementsFormState: {
      ...state.settlementsFormState,
      selectedPayment: {},
      weAreEditing: false,
      weAreCopying: false,
      weAreCreating: false,
      browserHasBeenRefreshed: false,
    }
  })),


  on(settlementUpdateSaveHasBeenFinalized, state => ({
      ...state,
      settlementsFormState: {
        ...state.settlementsFormState,
        selectedPayment: {},
        weAreCreating: false,
        weAreEditing: false,
        weAreCopying: false,
        browserHasBeenRefreshed: false,
      }
    }
  )),

  on(settlementUpdateCopyHasBeenFinalized, state => ({
      ...state,
      settlementsFormState: {
        ...state.settlementsFormState,
        selectedPayment: {},
        weAreCreating: false,
        weAreEditing: false,
        weAreCopying: false,
        browserHasBeenRefreshed: false,
      }
    }
  )),

  on(settlementUpdateEditHasBeenFinalized, state => ({
      ...state,
      settlementsFormState: {
        ...state.settlementsFormState,
        selectedPayment: {},
        weAreCreating: false,
        weAreEditing: false,
        weAreCopying: false,
        browserHasBeenRefreshed: false,
      }
    }
  )),

  on(settlementUpdateCancelButtonClicked, state => ({
      ...state,
      settlementsFormState: {
        ...state.settlementsFormState,
        selectedPayment: {},
        weAreCreating: false,
        weAreEditing: false,
        weAreCopying: false,
        browserHasBeenRefreshed: false,
      }
    }
  )),

  on(settlementUpdateErrorHasOccurred, state => ({
      ...state,
      settlementsFormState: {
        ...state.settlementsFormState,
        selectedPayment: {},
        weAreCreating: false,
        weAreEditing: false,
        weAreCopying: false,
        browserHasBeenRefreshed: false,
      }
    }
  )),
);

export function settlementUpdateStateReducer(state: State = initialState, action: Action): State {

  return _settlementUpdateStateReducer(state, action);
}
