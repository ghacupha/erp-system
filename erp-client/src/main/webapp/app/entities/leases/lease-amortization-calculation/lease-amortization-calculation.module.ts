import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { LeaseAmortizationCalculationComponent } from './list/lease-amortization-calculation.component';
import { LeaseAmortizationCalculationDetailComponent } from './detail/lease-amortization-calculation-detail.component';
import { LeaseAmortizationCalculationUpdateComponent } from './update/lease-amortization-calculation-update.component';
import { LeaseAmortizationCalculationDeleteDialogComponent } from './delete/lease-amortization-calculation-delete-dialog.component';
import { LeaseAmortizationCalculationRoutingModule } from './route/lease-amortization-calculation-routing.module';

@NgModule({
  imports: [SharedModule, LeaseAmortizationCalculationRoutingModule],
  declarations: [
    LeaseAmortizationCalculationComponent,
    LeaseAmortizationCalculationDetailComponent,
    LeaseAmortizationCalculationUpdateComponent,
    LeaseAmortizationCalculationDeleteDialogComponent,
  ],
  entryComponents: [LeaseAmortizationCalculationDeleteDialogComponent],
})
export class LeaseAmortizationCalculationModule {}
