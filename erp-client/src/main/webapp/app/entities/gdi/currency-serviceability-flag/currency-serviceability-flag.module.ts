import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CurrencyServiceabilityFlagComponent } from './list/currency-serviceability-flag.component';
import { CurrencyServiceabilityFlagDetailComponent } from './detail/currency-serviceability-flag-detail.component';
import { CurrencyServiceabilityFlagUpdateComponent } from './update/currency-serviceability-flag-update.component';
import { CurrencyServiceabilityFlagDeleteDialogComponent } from './delete/currency-serviceability-flag-delete-dialog.component';
import { CurrencyServiceabilityFlagRoutingModule } from './route/currency-serviceability-flag-routing.module';

@NgModule({
  imports: [SharedModule, CurrencyServiceabilityFlagRoutingModule],
  declarations: [
    CurrencyServiceabilityFlagComponent,
    CurrencyServiceabilityFlagDetailComponent,
    CurrencyServiceabilityFlagUpdateComponent,
    CurrencyServiceabilityFlagDeleteDialogComponent,
  ],
  entryComponents: [CurrencyServiceabilityFlagDeleteDialogComponent],
})
export class CurrencyServiceabilityFlagModule {}
