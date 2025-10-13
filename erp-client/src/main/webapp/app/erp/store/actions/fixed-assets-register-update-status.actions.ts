///
/// Erp System - Mark X No 10 (Jehoiada Series) Client 1.7.8
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
import { IAssetRegistration } from '../../erp-assets/asset-registration/asset-registration.model';

export const assetRegistrationCreationInitiatedFromList = createAction(
  '[FA Registration Creation: List] FA registration creation workflow initiated',
);

export const assetRegistrationCopyWorkflowInitiatedEnRoute = createAction(
  '[FA Registration Copy: Route] FA registration copy workflow initiated',
  props<{ copiedInstance: IAssetRegistration }>()
);

export const assetRegistrationCopyWorkflowInitiatedFromList = createAction(
  '[FA Registration Copy: List] FA registration copy workflow initiated',
  props<{ copiedInstance: IAssetRegistration }>()
);

export const assetRegistrationCopyWorkflowInitiatedFromView = createAction(
  '[FA Registration Copy: View] FA registration copy workflow initiated',
  props<{ copiedInstance: IAssetRegistration }>()
);

export const assetRegistrationEditWorkflowInitiatedEnRoute = createAction(
  '[FA Registration Edit: Route] FA registration edit workflow initiated',
  props<{ editedInstance: IAssetRegistration }>()
);

export const assetRegistrationEditWorkflowInitiatedFromList = createAction(
  '[FA Registration Edit: List] FA registration edit workflow initiated',
  props<{ editedInstance: IAssetRegistration }>()
);

export const assetRegistrationEditWorkflowInitiatedFromView = createAction(
  '[FA Registration Edit: View] FA registration edit workflow initiated',
  props<{ editedInstance: IAssetRegistration }>()
);

export const assetRegistrationCreationInitiatedEnRoute = createAction(
  '[FA Registration: Route] FA registration create workflow initiated',
);

export const assetRegistrationCreationWorkflowInitiatedFromList = createAction(
  '[FA Registration Create: List] FA registration create workflow initiated',
);

export const assetRegistrationUpdateFormHasBeenDestroyed = createAction(
  '[AssetReg Form] AssetReg form destroyed',
);

export const assetRegistrationDataHasMutated = createAction(
  '[AssetReg Form] AssetReg form data mutated',
);
