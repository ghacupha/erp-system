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
import { ITransactionAccount } from '../../erp-accounts/transaction-account/transaction-account.model';

export const transactionAccountCreationInitiatedFromList = createAction(
  '[transactionAccount Creation: List] Transaction Account creation workflow initiated',
);

export const transactionAccountCopyWorkflowInitiatedEnRoute = createAction(
  '[transactionAccount Copy: Route] Transaction Account copy workflow initiated',
  props<{ copiedInstance: ITransactionAccount }>()
);

export const transactionAccountCopyWorkflowInitiatedFromList = createAction(
  '[transactionAccount Copy: List] Transaction Account copy workflow initiated',
  props<{ copiedInstance: ITransactionAccount }>()
);

export const transactionAccountCopyWorkflowInitiatedFromView = createAction(
  '[transactionAccount Copy: View] Transaction Account copy workflow initiated',
  props<{ copiedInstance: ITransactionAccount }>()
);

export const transactionAccountEditWorkflowInitiatedEnRoute = createAction(
  '[transactionAccount Edit: Route] Transaction Account edit workflow initiated',
  props<{ editedInstance: ITransactionAccount }>()
);

export const transactionAccountEditWorkflowInitiatedFromList = createAction(
  '[transactionAccount Edit: List] Transaction Account edit workflow initiated',
  props<{ editedInstance: ITransactionAccount }>()
);

export const transactionAccountEditWorkflowInitiatedFromView = createAction(
  '[transactionAccount Edit: View] Transaction Account edit workflow initiated',
  props<{ editedInstance: ITransactionAccount }>()
);

export const transactionAccountCreationInitiatedEnRoute = createAction(
  '[transactionAccount: Route] Transaction Account create workflow initiated',
);

export const transactionAccountCreationWorkflowInitiatedFromList = createAction(
  '[transactionAccount Create: List] Transaction Account create workflow initiated',
);

export const transactionAccountUpdateFormHasBeenDestroyed = createAction(
  '[transactionAccount Form] Transaction Account form destroyed',
);

export const transactionAccountDataHasMutated = createAction(
  '[transactionAccount Form] Transaction Account form data mutated',
);

export const transactionAccountUpdateInstanceAcquiredFromBackend = createAction(
  '[transactionAccountEffects: Copied-Transaction-Account-effects] Transaction-Account-update instance acquired for copy',
  props<{backendAcquiredInstance: ITransactionAccount}>()
);

export const transactionAccountUpdateInstanceAcquisitionFromBackendFailed = createAction(
  '[transactionAccountEffects: Copied-Transaction-Account-effects] Transaction-Account-update instance acquisition failed',
  props<{error: string}>()
);
