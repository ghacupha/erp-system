import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ErpGatewaySharedModule } from 'app/shared/shared.module';
import { FixedAssetNetBookValueComponent } from './fixed-asset-net-book-value.component';
import { FixedAssetNetBookValueDetailComponent } from './fixed-asset-net-book-value-detail.component';
import { FixedAssetNetBookValueUpdateComponent } from './fixed-asset-net-book-value-update.component';
import { FixedAssetNetBookValueDeleteDialogComponent } from './fixed-asset-net-book-value-delete-dialog.component';
import { fixedAssetNetBookValueRoute } from './fixed-asset-net-book-value.route';

@NgModule({
  imports: [ErpGatewaySharedModule, RouterModule.forChild(fixedAssetNetBookValueRoute)],
  declarations: [
    FixedAssetNetBookValueComponent,
    FixedAssetNetBookValueDetailComponent,
    FixedAssetNetBookValueUpdateComponent,
    FixedAssetNetBookValueDeleteDialogComponent,
  ],
  entryComponents: [FixedAssetNetBookValueDeleteDialogComponent],
})
export class ErpServiceFixedAssetNetBookValueModule {}
