import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CardIssuerChargesComponent } from './list/card-issuer-charges.component';
import { CardIssuerChargesDetailComponent } from './detail/card-issuer-charges-detail.component';
import { CardIssuerChargesUpdateComponent } from './update/card-issuer-charges-update.component';
import { CardIssuerChargesDeleteDialogComponent } from './delete/card-issuer-charges-delete-dialog.component';
import { CardIssuerChargesRoutingModule } from './route/card-issuer-charges-routing.module';

@NgModule({
  imports: [SharedModule, CardIssuerChargesRoutingModule],
  declarations: [
    CardIssuerChargesComponent,
    CardIssuerChargesDetailComponent,
    CardIssuerChargesUpdateComponent,
    CardIssuerChargesDeleteDialogComponent,
  ],
  entryComponents: [CardIssuerChargesDeleteDialogComponent],
})
export class CardIssuerChargesModule {}
