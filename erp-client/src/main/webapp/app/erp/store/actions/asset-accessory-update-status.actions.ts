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
import { IAssetAccessory } from '../../erp-assets/asset-accessory/asset-accessory.model';

export const assetAccessoryCreationInitiatedFromList = createAction(
  '[AssetAccessory Creation: List] FA registration creation workflow initiated',
);

export const assetAccessoryCopyWorkflowInitiatedEnRoute = createAction(
  '[AssetAccessory Copy: Route] FA registration copy workflow initiated',
  props<{ copiedInstance: IAssetAccessory }>()
);

export const assetAccessoryCopyWorkflowInitiatedFromList = createAction(
  '[AssetAccessory Copy: List] FA registration copy workflow initiated',
  props<{ copiedInstance: IAssetAccessory }>()
);

export const assetAccessoryCopyWorkflowInitiatedFromView = createAction(
  '[AssetAccessory Copy: View] FA registration copy workflow initiated',
  props<{ copiedInstance: IAssetAccessory }>()
);

export const assetAccessoryEditWorkflowInitiatedEnRoute = createAction(
  '[AssetAccessory Edit: Route] FA registration edit workflow initiated',
  props<{ editedInstance: IAssetAccessory }>()
);

export const assetAccessoryEditWorkflowInitiatedFromList = createAction(
  '[AssetAccessory Edit: List] FA registration edit workflow initiated',
  props<{ editedInstance: IAssetAccessory }>()
);

export const assetAccessoryEditWorkflowInitiatedFromView = createAction(
  '[AssetAccessory Edit: View] FA registration edit workflow initiated',
  props<{ editedInstance: IAssetAccessory }>()
);

export const assetAccessoryCreationInitiatedEnRoute = createAction(
  '[AssetAccessory: Route] FA registration create workflow initiated',
);

export const assetAccessoryCreationWorkflowInitiatedFromList = createAction(
  '[AssetAccessory Create: List] FA registration create workflow initiated',
);

export const assetAccessoryUpdateFormHasBeenDestroyed = createAction(
  '[AssetAccessory Form] AssetReg form destroyed',
);

export const assetAccessoryDataHasMutated = createAction(
  '[AssetAccessory Form] AssetReg form data mutated',
);
