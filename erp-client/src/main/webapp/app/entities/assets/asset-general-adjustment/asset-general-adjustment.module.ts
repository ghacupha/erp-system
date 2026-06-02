import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { AssetGeneralAdjustmentComponent } from './list/asset-general-adjustment.component';
import { AssetGeneralAdjustmentDetailComponent } from './detail/asset-general-adjustment-detail.component';
import { AssetGeneralAdjustmentUpdateComponent } from './update/asset-general-adjustment-update.component';
import { AssetGeneralAdjustmentDeleteDialogComponent } from './delete/asset-general-adjustment-delete-dialog.component';
import { AssetGeneralAdjustmentRoutingModule } from './route/asset-general-adjustment-routing.module';

@NgModule({
  imports: [SharedModule, AssetGeneralAdjustmentRoutingModule],
  declarations: [
    AssetGeneralAdjustmentComponent,
    AssetGeneralAdjustmentDetailComponent,
    AssetGeneralAdjustmentUpdateComponent,
    AssetGeneralAdjustmentDeleteDialogComponent,
  ],
  entryComponents: [AssetGeneralAdjustmentDeleteDialogComponent],
})
export class AssetGeneralAdjustmentModule {}
