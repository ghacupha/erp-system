import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { AssetDisposalComponent } from './list/asset-disposal.component';
import { AssetDisposalDetailComponent } from './detail/asset-disposal-detail.component';
import { AssetDisposalUpdateComponent } from './update/asset-disposal-update.component';
import { AssetDisposalDeleteDialogComponent } from './delete/asset-disposal-delete-dialog.component';
import { AssetDisposalRoutingModule } from './route/asset-disposal-routing.module';

@NgModule({
  imports: [SharedModule, AssetDisposalRoutingModule],
  declarations: [AssetDisposalComponent, AssetDisposalDetailComponent, AssetDisposalUpdateComponent, AssetDisposalDeleteDialogComponent],
  entryComponents: [AssetDisposalDeleteDialogComponent],
})
export class AssetDisposalModule {}
