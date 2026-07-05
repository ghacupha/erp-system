import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { AssetAccessoryComponent } from './list/asset-accessory.component';
import { AssetAccessoryDetailComponent } from './detail/asset-accessory-detail.component';
import { AssetAccessoryUpdateComponent } from './update/asset-accessory-update.component';
import { AssetAccessoryDeleteDialogComponent } from './delete/asset-accessory-delete-dialog.component';
import { AssetAccessoryRoutingModule } from './route/asset-accessory-routing.module';

@NgModule({
  imports: [SharedModule, AssetAccessoryRoutingModule],
  declarations: [
    AssetAccessoryComponent,
    AssetAccessoryDetailComponent,
    AssetAccessoryUpdateComponent,
    AssetAccessoryDeleteDialogComponent,
  ],
  entryComponents: [AssetAccessoryDeleteDialogComponent],
})
export class AssetAccessoryModule {}
