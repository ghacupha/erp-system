import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { AmortizationRecurrenceComponent } from './list/amortization-recurrence.component';
import { AmortizationRecurrenceDetailComponent } from './detail/amortization-recurrence-detail.component';
import { AmortizationRecurrenceUpdateComponent } from './update/amortization-recurrence-update.component';
import { AmortizationRecurrenceDeleteDialogComponent } from './delete/amortization-recurrence-delete-dialog.component';
import { AmortizationRecurrenceRoutingModule } from './route/amortization-recurrence-routing.module';

@NgModule({
  imports: [SharedModule, AmortizationRecurrenceRoutingModule],
  declarations: [
    AmortizationRecurrenceComponent,
    AmortizationRecurrenceDetailComponent,
    AmortizationRecurrenceUpdateComponent,
    AmortizationRecurrenceDeleteDialogComponent,
  ],
  entryComponents: [AmortizationRecurrenceDeleteDialogComponent],
})
export class AmortizationRecurrenceModule {}
