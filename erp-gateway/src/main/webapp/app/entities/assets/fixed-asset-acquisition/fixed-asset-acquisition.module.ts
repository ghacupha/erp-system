///
/// Copyright © 2021 Edwin Njeru (mailnjeru@gmail.com)
///
/// Licensed under the Apache License, Version 2.0 (the "License");
/// you may not use this file except in compliance with the License.
/// You may obtain a copy of the License at
///
///     http://www.apache.org/licenses/LICENSE-2.0
///
/// Unless required by applicable law or agreed to in writing, software
/// distributed under the License is distributed on an "AS IS" BASIS,
/// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
/// See the License for the specific language governing permissions and
/// limitations under the License.
///

import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ErpGatewaySharedModule } from 'app/shared/shared.module';
import { FixedAssetAcquisitionComponent } from './fixed-asset-acquisition.component';
import { FixedAssetAcquisitionDetailComponent } from './fixed-asset-acquisition-detail.component';
import { FixedAssetAcquisitionUpdateComponent } from './fixed-asset-acquisition-update.component';
import { FixedAssetAcquisitionDeleteDialogComponent } from './fixed-asset-acquisition-delete-dialog.component';
import { fixedAssetAcquisitionRoute } from './fixed-asset-acquisition.route';

@NgModule({
  imports: [ErpGatewaySharedModule, RouterModule.forChild(fixedAssetAcquisitionRoute)],
  declarations: [
    FixedAssetAcquisitionComponent,
    FixedAssetAcquisitionDetailComponent,
    FixedAssetAcquisitionUpdateComponent,
    FixedAssetAcquisitionDeleteDialogComponent,
  ],
  entryComponents: [FixedAssetAcquisitionDeleteDialogComponent],
})
export class ErpServiceFixedAssetAcquisitionModule {}
