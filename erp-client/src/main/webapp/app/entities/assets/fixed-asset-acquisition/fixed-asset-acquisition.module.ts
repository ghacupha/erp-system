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
