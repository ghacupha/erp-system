import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { FixedAssetDepreciationComponent } from './list/fixed-asset-depreciation.component';
import { FixedAssetDepreciationDetailComponent } from './detail/fixed-asset-depreciation-detail.component';
import { FixedAssetDepreciationUpdateComponent } from './update/fixed-asset-depreciation-update.component';
import { FixedAssetDepreciationDeleteDialogComponent } from './delete/fixed-asset-depreciation-delete-dialog.component';
import { FixedAssetDepreciationRoutingModule } from './route/fixed-asset-depreciation-routing.module';

@NgModule({
  imports: [SharedModule, FixedAssetDepreciationRoutingModule],
  declarations: [
    FixedAssetDepreciationComponent,
    FixedAssetDepreciationDetailComponent,
    FixedAssetDepreciationUpdateComponent,
    FixedAssetDepreciationDeleteDialogComponent,
  ],
  entryComponents: [FixedAssetDepreciationDeleteDialogComponent],
})
export class ErpServiceFixedAssetDepreciationModule {}
