import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CardFraudInformationComponent } from './list/card-fraud-information.component';
import { CardFraudInformationDetailComponent } from './detail/card-fraud-information-detail.component';
import { CardFraudInformationUpdateComponent } from './update/card-fraud-information-update.component';
import { CardFraudInformationDeleteDialogComponent } from './delete/card-fraud-information-delete-dialog.component';
import { CardFraudInformationRoutingModule } from './route/card-fraud-information-routing.module';

@NgModule({
  imports: [SharedModule, CardFraudInformationRoutingModule],
  declarations: [
    CardFraudInformationComponent,
    CardFraudInformationDetailComponent,
    CardFraudInformationUpdateComponent,
    CardFraudInformationDeleteDialogComponent,
  ],
  entryComponents: [CardFraudInformationDeleteDialogComponent],
})
export class CardFraudInformationModule {}
