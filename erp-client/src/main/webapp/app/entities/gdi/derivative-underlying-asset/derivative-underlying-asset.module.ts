import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { DerivativeUnderlyingAssetComponent } from './list/derivative-underlying-asset.component';
import { DerivativeUnderlyingAssetDetailComponent } from './detail/derivative-underlying-asset-detail.component';
import { DerivativeUnderlyingAssetUpdateComponent } from './update/derivative-underlying-asset-update.component';
import { DerivativeUnderlyingAssetDeleteDialogComponent } from './delete/derivative-underlying-asset-delete-dialog.component';
import { DerivativeUnderlyingAssetRoutingModule } from './route/derivative-underlying-asset-routing.module';

@NgModule({
  imports: [SharedModule, DerivativeUnderlyingAssetRoutingModule],
  declarations: [
    DerivativeUnderlyingAssetComponent,
    DerivativeUnderlyingAssetDetailComponent,
    DerivativeUnderlyingAssetUpdateComponent,
    DerivativeUnderlyingAssetDeleteDialogComponent,
  ],
  entryComponents: [DerivativeUnderlyingAssetDeleteDialogComponent],
})
export class DerivativeUnderlyingAssetModule {}
