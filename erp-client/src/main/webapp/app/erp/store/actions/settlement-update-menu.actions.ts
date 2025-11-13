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

import { createAction, props } from '@ngrx/store';
import { ISettlement } from '../../erp-settlements/settlement/settlement.model';

export const settlementCopyWorkflowInitiatedEnRoute = createAction(
  '[Settlements Copy Route] settlement copy workflow initiated',
  props<{ copiedSettlement: ISettlement }>()
);

export const settlementCreationWorkflowInitiatedFromList = createAction(
  '[Settlements Page] settlement creation workflow initiated'
);

export const settlementCreationWorkflowInitiatedEnRoute = createAction(
  '[Settlements AddNew Route] settlement creation workflow initiated en route'
);

export const settlementCreationWorkflowInitiatedFromUpdateFormOnInit = createAction(
  '[Settlements Update From: OnInit] settlement creation workflow initiated on form initialization',
  props<{ copiedPartialSettlement: ISettlement }>()
);

export const settlementCreationWorkflowRefreshedFromForm = createAction(
  '[Settlements Update Refreshed] settlement creation workflow initiated on form initialization',
  props<{ copiedPartialSettlement: ISettlement }>()
);

export const settlementUpdateFormHasBeenDestroyed = createAction(
  '[Settlements Update Form Destroyed Event] settlement creation workflow interrupted by refresh event',
  props<{ copiedPartialSettlement: ISettlement }>()
);

export const settlementCopyWorkflowInitiatedFromList = createAction(
  '[Settlements Page] settlement copy workflow initiated',
  props<{ copiedSettlement: ISettlement }>()
);

export const settlementCopyWorkflowInitiatedFromDetails = createAction(
  '[Settlement Detail] settlement copy workflow initiated',
  props<{ copiedSettlement: ISettlement }>()
);

export const settlementEditWorkflowInitiatedFromList = createAction(
  '[Settlements Page] settlement edit workflow initiated',
  props<{ editedSettlement: ISettlement }>()
);

export const settlementEditWorkflowInitiatedFromDetails = createAction(
  '[Settlement Detail] settlement edit workflow initiated',
  props<{editedSettlement: ISettlement}>()
);

// TODO check workflows from list
export const newSettlementCreationSequenceInitiatedFomList = createAction(
  '[Settlement Detail] new settlement creation workflow initiated',
  props<{newSettlement: ISettlement}>()
);

export const settlementUpdateInstanceAcquiredFromBackend = createAction(
  '[SettlementUpdateEffects: Copied-Settlement-effects] settlement-update instance acquired for copy',
  props<{backendAcquiredSettlement: ISettlement}>()
);

export const settlementUpdateInstanceAcquisitionFromBackendFailed = createAction(
  '[SettlementUpdateEffects: Copied-Settlement-effects] settlement-update instance acquisition failed',
  props<{error: string}>()
);

export const settlementUpdatePreviousStateMethodCalled = createAction(
  '[Settlement Update Form: Previous State] Previous state method triggered'
);

export const settlementUpdateErrorHasOccurred = createAction(
  '[Settlement Update Form: Error] Settlement update error encountered'
);

export const settlementUpdateSaveHasBeenFinalized = createAction(
  '[Settlement Update Form: Save Finalized] Settlement update save sequence finalized'
);

export const settlementUpdateCopyHasBeenFinalized = createAction(
  '[Settlement Update Form: Copy Sequence Finalized] Settlement update copying sequence finalized'
);

export const settlementUpdateEditHasBeenFinalized = createAction(
  '[Settlement Update Form: Editing Sequence Finalized] Settlement update editing sequence finalized'
);

export const settlementUpdateCancelButtonClicked = createAction(
  '[Settlement Update Form: Cancellation Initialized] Settlement update cancellation initialized'
);
