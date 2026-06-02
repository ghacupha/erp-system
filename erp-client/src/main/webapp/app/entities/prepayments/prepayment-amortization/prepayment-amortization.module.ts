import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PrepaymentAmortizationComponent } from './list/prepayment-amortization.component';
import { PrepaymentAmortizationDetailComponent } from './detail/prepayment-amortization-detail.component';
import { PrepaymentAmortizationUpdateComponent } from './update/prepayment-amortization-update.component';
import { PrepaymentAmortizationDeleteDialogComponent } from './delete/prepayment-amortization-delete-dialog.component';
import { PrepaymentAmortizationRoutingModule } from './route/prepayment-amortization-routing.module';

@NgModule({
  imports: [SharedModule, PrepaymentAmortizationRoutingModule],
  declarations: [
    PrepaymentAmortizationComponent,
    PrepaymentAmortizationDetailComponent,
    PrepaymentAmortizationUpdateComponent,
    PrepaymentAmortizationDeleteDialogComponent,
  ],
  entryComponents: [PrepaymentAmortizationDeleteDialogComponent],
})
export class PrepaymentAmortizationModule {}
