import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CurrencyAuthenticityFlagComponent } from './list/currency-authenticity-flag.component';
import { CurrencyAuthenticityFlagDetailComponent } from './detail/currency-authenticity-flag-detail.component';
import { CurrencyAuthenticityFlagUpdateComponent } from './update/currency-authenticity-flag-update.component';
import { CurrencyAuthenticityFlagDeleteDialogComponent } from './delete/currency-authenticity-flag-delete-dialog.component';
import { CurrencyAuthenticityFlagRoutingModule } from './route/currency-authenticity-flag-routing.module';

@NgModule({
  imports: [SharedModule, CurrencyAuthenticityFlagRoutingModule],
  declarations: [
    CurrencyAuthenticityFlagComponent,
    CurrencyAuthenticityFlagDetailComponent,
    CurrencyAuthenticityFlagUpdateComponent,
    CurrencyAuthenticityFlagDeleteDialogComponent,
  ],
  entryComponents: [CurrencyAuthenticityFlagDeleteDialogComponent],
})
export class CurrencyAuthenticityFlagModule {}
