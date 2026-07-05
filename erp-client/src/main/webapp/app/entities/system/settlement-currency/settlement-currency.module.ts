import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { SettlementCurrencyComponent } from './list/settlement-currency.component';
import { SettlementCurrencyDetailComponent } from './detail/settlement-currency-detail.component';
import { SettlementCurrencyUpdateComponent } from './update/settlement-currency-update.component';
import { SettlementCurrencyDeleteDialogComponent } from './delete/settlement-currency-delete-dialog.component';
import { SettlementCurrencyRoutingModule } from './route/settlement-currency-routing.module';

@NgModule({
  imports: [SharedModule, SettlementCurrencyRoutingModule],
  declarations: [
    SettlementCurrencyComponent,
    SettlementCurrencyDetailComponent,
    SettlementCurrencyUpdateComponent,
    SettlementCurrencyDeleteDialogComponent,
  ],
  entryComponents: [SettlementCurrencyDeleteDialogComponent],
})
export class SettlementCurrencyModule {}
