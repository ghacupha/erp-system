///
/// Erp System - Mark X No 11 (Jehoiada Series) Client 1.7.8
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

import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { FixedAssetAcquisitionComponent } from './list/fixed-asset-acquisition.component';
import { FixedAssetAcquisitionDetailComponent } from './detail/fixed-asset-acquisition-detail.component';
import { FixedAssetAcquisitionUpdateComponent } from './update/fixed-asset-acquisition-update.component';
import { FixedAssetAcquisitionDeleteDialogComponent } from './delete/fixed-asset-acquisition-delete-dialog.component';
import { FixedAssetAcquisitionRoutingModule } from './route/fixed-asset-acquisition-routing.module';

@NgModule({
  imports: [SharedModule, FixedAssetAcquisitionRoutingModule],
  declarations: [
    FixedAssetAcquisitionComponent,
    FixedAssetAcquisitionDetailComponent,
    FixedAssetAcquisitionUpdateComponent,
    FixedAssetAcquisitionDeleteDialogComponent,
  ],
  entryComponents: [FixedAssetAcquisitionDeleteDialogComponent],
})
export class ErpServiceFixedAssetAcquisitionModule {}
