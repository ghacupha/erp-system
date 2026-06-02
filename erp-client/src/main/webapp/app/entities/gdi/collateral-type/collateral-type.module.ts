import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CollateralTypeComponent } from './list/collateral-type.component';
import { CollateralTypeDetailComponent } from './detail/collateral-type-detail.component';
import { CollateralTypeUpdateComponent } from './update/collateral-type-update.component';
import { CollateralTypeDeleteDialogComponent } from './delete/collateral-type-delete-dialog.component';
import { CollateralTypeRoutingModule } from './route/collateral-type-routing.module';

@NgModule({
  imports: [SharedModule, CollateralTypeRoutingModule],
  declarations: [
    CollateralTypeComponent,
    CollateralTypeDetailComponent,
    CollateralTypeUpdateComponent,
    CollateralTypeDeleteDialogComponent,
  ],
  entryComponents: [CollateralTypeDeleteDialogComponent],
})
export class CollateralTypeModule {}
