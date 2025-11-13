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

import { Component, Input } from '@angular/core';
import { IAssetRegistration } from '../../../erp-assets/asset-registration/asset-registration.model';

@Component({
  selector: 'jhi-asset-registration-option-view',
  template: `
    # {{item.id}} # {{ item.assetNumber }} | {{ item.assetDetails }} CUs: {{ item.assetCost | currency }} | {{ item.assetCategory?.assetCategoryName }} | {{ item.mainServiceOutlet?.outletCode }}
  `
})
export class AssetRegistrationOptionViewComponent {

  @Input() item: IAssetRegistration = {};
}
