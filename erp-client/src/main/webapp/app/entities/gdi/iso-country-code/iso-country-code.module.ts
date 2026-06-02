import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { IsoCountryCodeComponent } from './list/iso-country-code.component';
import { IsoCountryCodeDetailComponent } from './detail/iso-country-code-detail.component';
import { IsoCountryCodeUpdateComponent } from './update/iso-country-code-update.component';
import { IsoCountryCodeDeleteDialogComponent } from './delete/iso-country-code-delete-dialog.component';
import { IsoCountryCodeRoutingModule } from './route/iso-country-code-routing.module';

@NgModule({
  imports: [SharedModule, IsoCountryCodeRoutingModule],
  declarations: [
    IsoCountryCodeComponent,
    IsoCountryCodeDetailComponent,
    IsoCountryCodeUpdateComponent,
    IsoCountryCodeDeleteDialogComponent,
  ],
  entryComponents: [IsoCountryCodeDeleteDialogComponent],
})
export class IsoCountryCodeModule {}
