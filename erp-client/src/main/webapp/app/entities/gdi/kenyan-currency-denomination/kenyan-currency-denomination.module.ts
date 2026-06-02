import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { KenyanCurrencyDenominationComponent } from './list/kenyan-currency-denomination.component';
import { KenyanCurrencyDenominationDetailComponent } from './detail/kenyan-currency-denomination-detail.component';
import { KenyanCurrencyDenominationUpdateComponent } from './update/kenyan-currency-denomination-update.component';
import { KenyanCurrencyDenominationDeleteDialogComponent } from './delete/kenyan-currency-denomination-delete-dialog.component';
import { KenyanCurrencyDenominationRoutingModule } from './route/kenyan-currency-denomination-routing.module';

@NgModule({
  imports: [SharedModule, KenyanCurrencyDenominationRoutingModule],
  declarations: [
    KenyanCurrencyDenominationComponent,
    KenyanCurrencyDenominationDetailComponent,
    KenyanCurrencyDenominationUpdateComponent,
    KenyanCurrencyDenominationDeleteDialogComponent,
  ],
  entryComponents: [KenyanCurrencyDenominationDeleteDialogComponent],
})
export class KenyanCurrencyDenominationModule {}
