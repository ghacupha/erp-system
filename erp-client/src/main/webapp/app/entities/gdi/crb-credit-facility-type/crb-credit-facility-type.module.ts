import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CrbCreditFacilityTypeComponent } from './list/crb-credit-facility-type.component';
import { CrbCreditFacilityTypeDetailComponent } from './detail/crb-credit-facility-type-detail.component';
import { CrbCreditFacilityTypeUpdateComponent } from './update/crb-credit-facility-type-update.component';
import { CrbCreditFacilityTypeDeleteDialogComponent } from './delete/crb-credit-facility-type-delete-dialog.component';
import { CrbCreditFacilityTypeRoutingModule } from './route/crb-credit-facility-type-routing.module';

@NgModule({
  imports: [SharedModule, CrbCreditFacilityTypeRoutingModule],
  declarations: [
    CrbCreditFacilityTypeComponent,
    CrbCreditFacilityTypeDetailComponent,
    CrbCreditFacilityTypeUpdateComponent,
    CrbCreditFacilityTypeDeleteDialogComponent,
  ],
  entryComponents: [CrbCreditFacilityTypeDeleteDialogComponent],
})
export class CrbCreditFacilityTypeModule {}
