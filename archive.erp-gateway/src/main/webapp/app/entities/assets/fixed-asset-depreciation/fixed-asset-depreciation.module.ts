import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ErpGatewaySharedModule } from 'app/shared/shared.module';
import { FixedAssetDepreciationComponent } from './fixed-asset-depreciation.component';
import { FixedAssetDepreciationDetailComponent } from './fixed-asset-depreciation-detail.component';
import { FixedAssetDepreciationUpdateComponent } from './fixed-asset-depreciation-update.component';
import { FixedAssetDepreciationDeleteDialogComponent } from './fixed-asset-depreciation-delete-dialog.component';
import { fixedAssetDepreciationRoute } from './fixed-asset-depreciation.route';

@NgModule({
  imports: [ErpGatewaySharedModule, RouterModule.forChild(fixedAssetDepreciationRoute)],
  declarations: [
    FixedAssetDepreciationComponent,
    FixedAssetDepreciationDetailComponent,
    FixedAssetDepreciationUpdateComponent,
    FixedAssetDepreciationDeleteDialogComponent,
  ],
  entryComponents: [FixedAssetDepreciationDeleteDialogComponent],
})
export class ErpServiceFixedAssetDepreciationModule {}
