import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CardUsageInformationComponent } from './list/card-usage-information.component';
import { CardUsageInformationDetailComponent } from './detail/card-usage-information-detail.component';
import { CardUsageInformationUpdateComponent } from './update/card-usage-information-update.component';
import { CardUsageInformationDeleteDialogComponent } from './delete/card-usage-information-delete-dialog.component';
import { CardUsageInformationRoutingModule } from './route/card-usage-information-routing.module';

@NgModule({
  imports: [SharedModule, CardUsageInformationRoutingModule],
  declarations: [
    CardUsageInformationComponent,
    CardUsageInformationDetailComponent,
    CardUsageInformationUpdateComponent,
    CardUsageInformationDeleteDialogComponent,
  ],
  entryComponents: [CardUsageInformationDeleteDialogComponent],
})
export class CardUsageInformationModule {}
