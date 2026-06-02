import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { TaxReferenceComponent } from './list/tax-reference.component';
import { TaxReferenceDetailComponent } from './detail/tax-reference-detail.component';
import { TaxReferenceUpdateComponent } from './update/tax-reference-update.component';
import { TaxReferenceDeleteDialogComponent } from './delete/tax-reference-delete-dialog.component';
import { TaxReferenceRoutingModule } from './route/tax-reference-routing.module';

@NgModule({
  imports: [SharedModule, TaxReferenceRoutingModule],
  declarations: [TaxReferenceComponent, TaxReferenceDetailComponent, TaxReferenceUpdateComponent, TaxReferenceDeleteDialogComponent],
  entryComponents: [TaxReferenceDeleteDialogComponent],
})
export class ErpServiceTaxReferenceModule {}
