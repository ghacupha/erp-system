import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { AmortizationPeriodComponent } from './list/amortization-period.component';
import { AmortizationPeriodDetailComponent } from './detail/amortization-period-detail.component';
import { AmortizationPeriodUpdateComponent } from './update/amortization-period-update.component';
import { AmortizationPeriodDeleteDialogComponent } from './delete/amortization-period-delete-dialog.component';
import { AmortizationPeriodRoutingModule } from './route/amortization-period-routing.module';

@NgModule({
  imports: [SharedModule, AmortizationPeriodRoutingModule],
  declarations: [
    AmortizationPeriodComponent,
    AmortizationPeriodDetailComponent,
    AmortizationPeriodUpdateComponent,
    AmortizationPeriodDeleteDialogComponent,
  ],
  entryComponents: [AmortizationPeriodDeleteDialogComponent],
})
export class AmortizationPeriodModule {}
