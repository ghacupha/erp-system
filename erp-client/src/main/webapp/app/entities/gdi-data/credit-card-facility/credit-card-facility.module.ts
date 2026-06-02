import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CreditCardFacilityComponent } from './list/credit-card-facility.component';
import { CreditCardFacilityDetailComponent } from './detail/credit-card-facility-detail.component';
import { CreditCardFacilityUpdateComponent } from './update/credit-card-facility-update.component';
import { CreditCardFacilityDeleteDialogComponent } from './delete/credit-card-facility-delete-dialog.component';
import { CreditCardFacilityRoutingModule } from './route/credit-card-facility-routing.module';

@NgModule({
  imports: [SharedModule, CreditCardFacilityRoutingModule],
  declarations: [
    CreditCardFacilityComponent,
    CreditCardFacilityDetailComponent,
    CreditCardFacilityUpdateComponent,
    CreditCardFacilityDeleteDialogComponent,
  ],
  entryComponents: [CreditCardFacilityDeleteDialogComponent],
})
export class CreditCardFacilityModule {}
