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
import { IPrepaymentAccount } from '../../erp-prepayments/prepayment-account/prepayment-account.model';

export const prepaymentAccountCreationInitiatedFromList = createAction(
  '[PrepaymentAccount Creation: List] Prepayment Account creation workflow initiated',
);

export const prepaymentAccountCopyWorkflowInitiatedEnRoute = createAction(
  '[PrepaymentAccount Copy: Route] Prepayment Account copy workflow initiated',
  props<{ copiedInstance: IPrepaymentAccount }>()
);

export const prepaymentAccountCopyWorkflowInitiatedFromList = createAction(
  '[PrepaymentAccount Copy: List] Prepayment Account copy workflow initiated',
  props<{ copiedInstance: IPrepaymentAccount }>()
);

export const prepaymentAccountCopyWorkflowInitiatedFromView = createAction(
  '[PrepaymentAccount Copy: View] Prepayment Account copy workflow initiated',
  props<{ copiedInstance: IPrepaymentAccount }>()
);

export const prepaymentAccountEditWorkflowInitiatedEnRoute = createAction(
  '[PrepaymentAccount Edit: Route] Prepayment Account edit workflow initiated',
  props<{ editedInstance: IPrepaymentAccount }>()
);

export const prepaymentAccountEditWorkflowInitiatedFromList = createAction(
  '[PrepaymentAccount Edit: List] Prepayment Account edit workflow initiated',
  props<{ editedInstance: IPrepaymentAccount }>()
);

export const prepaymentAccountEditWorkflowInitiatedFromView = createAction(
  '[PrepaymentAccount Edit: View] Prepayment Account edit workflow initiated',
  props<{ editedInstance: IPrepaymentAccount }>()
);

export const prepaymentAccountCreationInitiatedEnRoute = createAction(
  '[PrepaymentAccount: Route] Prepayment Account create workflow initiated',
);

export const prepaymentAccountCreationWorkflowInitiatedFromList = createAction(
  '[PrepaymentAccount Create: List] Prepayment Account create workflow initiated',
);

export const prepaymentAccountUpdateFormHasBeenDestroyed = createAction(
  '[PrepaymentAccount Form] Prepayment Account form destroyed',
);

export const prepaymentAccountDataHasMutated = createAction(
  '[PrepaymentAccount Form] Prepayment Account form data mutated',
);

export const prepaymentAccountUpdateInstanceAcquiredFromBackend = createAction(
  '[PrepaymentAccountEffects: Copied-Prepayment-Account-effects] prepayment-account-update instance acquired for copy',
  props<{backendAcquiredInstance: IPrepaymentAccount}>()
);

export const prepaymentAccountUpdateInstanceAcquisitionFromBackendFailed = createAction(
  '[PrepaymentAccountEffects: Copied-Prepayment-Account-effects] prepayment-account-update instance acquisition failed',
  props<{error: string}>()
);
