import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CardChargesComponent } from './list/card-charges.component';
import { CardChargesDetailComponent } from './detail/card-charges-detail.component';
import { CardChargesUpdateComponent } from './update/card-charges-update.component';
import { CardChargesDeleteDialogComponent } from './delete/card-charges-delete-dialog.component';
import { CardChargesRoutingModule } from './route/card-charges-routing.module';

@NgModule({
  imports: [SharedModule, CardChargesRoutingModule],
  declarations: [CardChargesComponent, CardChargesDetailComponent, CardChargesUpdateComponent, CardChargesDeleteDialogComponent],
  entryComponents: [CardChargesDeleteDialogComponent],
})
export class CardChargesModule {}
