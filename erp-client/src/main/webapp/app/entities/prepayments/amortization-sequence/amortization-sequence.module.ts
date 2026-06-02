import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { AmortizationSequenceComponent } from './list/amortization-sequence.component';
import { AmortizationSequenceDetailComponent } from './detail/amortization-sequence-detail.component';
import { AmortizationSequenceUpdateComponent } from './update/amortization-sequence-update.component';
import { AmortizationSequenceDeleteDialogComponent } from './delete/amortization-sequence-delete-dialog.component';
import { AmortizationSequenceRoutingModule } from './route/amortization-sequence-routing.module';

@NgModule({
  imports: [SharedModule, AmortizationSequenceRoutingModule],
  declarations: [
    AmortizationSequenceComponent,
    AmortizationSequenceDetailComponent,
    AmortizationSequenceUpdateComponent,
    AmortizationSequenceDeleteDialogComponent,
  ],
  entryComponents: [AmortizationSequenceDeleteDialogComponent],
})
export class AmortizationSequenceModule {}
