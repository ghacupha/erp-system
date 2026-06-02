import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CreditCardOwnershipComponent } from './list/credit-card-ownership.component';
import { CreditCardOwnershipDetailComponent } from './detail/credit-card-ownership-detail.component';
import { CreditCardOwnershipUpdateComponent } from './update/credit-card-ownership-update.component';
import { CreditCardOwnershipDeleteDialogComponent } from './delete/credit-card-ownership-delete-dialog.component';
import { CreditCardOwnershipRoutingModule } from './route/credit-card-ownership-routing.module';

@NgModule({
  imports: [SharedModule, CreditCardOwnershipRoutingModule],
  declarations: [
    CreditCardOwnershipComponent,
    CreditCardOwnershipDetailComponent,
    CreditCardOwnershipUpdateComponent,
    CreditCardOwnershipDeleteDialogComponent,
  ],
  entryComponents: [CreditCardOwnershipDeleteDialogComponent],
})
export class CreditCardOwnershipModule {}
