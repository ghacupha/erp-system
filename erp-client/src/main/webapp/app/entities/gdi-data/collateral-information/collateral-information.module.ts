import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CollateralInformationComponent } from './list/collateral-information.component';
import { CollateralInformationDetailComponent } from './detail/collateral-information-detail.component';
import { CollateralInformationUpdateComponent } from './update/collateral-information-update.component';
import { CollateralInformationDeleteDialogComponent } from './delete/collateral-information-delete-dialog.component';
import { CollateralInformationRoutingModule } from './route/collateral-information-routing.module';

@NgModule({
  imports: [SharedModule, CollateralInformationRoutingModule],
  declarations: [
    CollateralInformationComponent,
    CollateralInformationDetailComponent,
    CollateralInformationUpdateComponent,
    CollateralInformationDeleteDialogComponent,
  ],
  entryComponents: [CollateralInformationDeleteDialogComponent],
})
export class CollateralInformationModule {}
