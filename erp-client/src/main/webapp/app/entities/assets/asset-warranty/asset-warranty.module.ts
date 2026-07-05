import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { AssetWarrantyComponent } from './list/asset-warranty.component';
import { AssetWarrantyDetailComponent } from './detail/asset-warranty-detail.component';
import { AssetWarrantyUpdateComponent } from './update/asset-warranty-update.component';
import { AssetWarrantyDeleteDialogComponent } from './delete/asset-warranty-delete-dialog.component';
import { AssetWarrantyRoutingModule } from './route/asset-warranty-routing.module';

@NgModule({
  imports: [SharedModule, AssetWarrantyRoutingModule],
  declarations: [AssetWarrantyComponent, AssetWarrantyDetailComponent, AssetWarrantyUpdateComponent, AssetWarrantyDeleteDialogComponent],
  entryComponents: [AssetWarrantyDeleteDialogComponent],
})
export class AssetWarrantyModule {}
