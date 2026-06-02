import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { LeaseAmortizationScheduleComponent } from './list/lease-amortization-schedule.component';
import { LeaseAmortizationScheduleDetailComponent } from './detail/lease-amortization-schedule-detail.component';
import { LeaseAmortizationScheduleUpdateComponent } from './update/lease-amortization-schedule-update.component';
import { LeaseAmortizationScheduleDeleteDialogComponent } from './delete/lease-amortization-schedule-delete-dialog.component';
import { LeaseAmortizationScheduleRoutingModule } from './route/lease-amortization-schedule-routing.module';

@NgModule({
  imports: [SharedModule, LeaseAmortizationScheduleRoutingModule],
  declarations: [
    LeaseAmortizationScheduleComponent,
    LeaseAmortizationScheduleDetailComponent,
    LeaseAmortizationScheduleUpdateComponent,
    LeaseAmortizationScheduleDeleteDialogComponent,
  ],
  entryComponents: [LeaseAmortizationScheduleDeleteDialogComponent],
})
export class LeaseAmortizationScheduleModule {}
