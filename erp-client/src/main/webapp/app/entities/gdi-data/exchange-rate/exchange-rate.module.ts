import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ExchangeRateComponent } from './list/exchange-rate.component';
import { ExchangeRateDetailComponent } from './detail/exchange-rate-detail.component';
import { ExchangeRateUpdateComponent } from './update/exchange-rate-update.component';
import { ExchangeRateDeleteDialogComponent } from './delete/exchange-rate-delete-dialog.component';
import { ExchangeRateRoutingModule } from './route/exchange-rate-routing.module';

@NgModule({
  imports: [SharedModule, ExchangeRateRoutingModule],
  declarations: [ExchangeRateComponent, ExchangeRateDetailComponent, ExchangeRateUpdateComponent, ExchangeRateDeleteDialogComponent],
  entryComponents: [ExchangeRateDeleteDialogComponent],
})
export class ExchangeRateModule {}
