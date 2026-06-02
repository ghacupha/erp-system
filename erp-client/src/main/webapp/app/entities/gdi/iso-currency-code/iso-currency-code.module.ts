import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { IsoCurrencyCodeComponent } from './list/iso-currency-code.component';
import { IsoCurrencyCodeDetailComponent } from './detail/iso-currency-code-detail.component';
import { IsoCurrencyCodeUpdateComponent } from './update/iso-currency-code-update.component';
import { IsoCurrencyCodeDeleteDialogComponent } from './delete/iso-currency-code-delete-dialog.component';
import { IsoCurrencyCodeRoutingModule } from './route/iso-currency-code-routing.module';

@NgModule({
  imports: [SharedModule, IsoCurrencyCodeRoutingModule],
  declarations: [
    IsoCurrencyCodeComponent,
    IsoCurrencyCodeDetailComponent,
    IsoCurrencyCodeUpdateComponent,
    IsoCurrencyCodeDeleteDialogComponent,
  ],
  entryComponents: [IsoCurrencyCodeDeleteDialogComponent],
})
export class IsoCurrencyCodeModule {}
