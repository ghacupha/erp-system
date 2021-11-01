import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { FixedAssetNetBookValueComponent } from './list/fixed-asset-net-book-value.component';
import { FixedAssetNetBookValueDetailComponent } from './detail/fixed-asset-net-book-value-detail.component';
import { FixedAssetNetBookValueUpdateComponent } from './update/fixed-asset-net-book-value-update.component';
import { FixedAssetNetBookValueDeleteDialogComponent } from './delete/fixed-asset-net-book-value-delete-dialog.component';
import { FixedAssetNetBookValueRoutingModule } from './route/fixed-asset-net-book-value-routing.module';

@NgModule({
  imports: [SharedModule, FixedAssetNetBookValueRoutingModule],
  declarations: [
    FixedAssetNetBookValueComponent,
    FixedAssetNetBookValueDetailComponent,
    FixedAssetNetBookValueUpdateComponent,
    FixedAssetNetBookValueDeleteDialogComponent,
  ],
  entryComponents: [FixedAssetNetBookValueDeleteDialogComponent],
})
export class ErpServiceFixedAssetNetBookValueModule {}
