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
