import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { AssetWriteOffComponent } from './list/asset-write-off.component';
import { AssetWriteOffDetailComponent } from './detail/asset-write-off-detail.component';
import { AssetWriteOffUpdateComponent } from './update/asset-write-off-update.component';
import { AssetWriteOffDeleteDialogComponent } from './delete/asset-write-off-delete-dialog.component';
import { AssetWriteOffRoutingModule } from './route/asset-write-off-routing.module';

@NgModule({
  imports: [SharedModule, AssetWriteOffRoutingModule],
  declarations: [AssetWriteOffComponent, AssetWriteOffDetailComponent, AssetWriteOffUpdateComponent, AssetWriteOffDeleteDialogComponent],
  entryComponents: [AssetWriteOffDeleteDialogComponent],
})
export class AssetWriteOffModule {}
