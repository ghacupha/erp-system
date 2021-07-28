import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ErpGatewaySharedModule } from 'app/shared/shared.module';
import { TaxReferenceComponent } from './tax-reference.component';
import { TaxReferenceDetailComponent } from './tax-reference-detail.component';
import { TaxReferenceUpdateComponent } from './tax-reference-update.component';
import { TaxReferenceDeleteDialogComponent } from './tax-reference-delete-dialog.component';
import { taxReferenceRoute } from './tax-reference.route';

@NgModule({
  imports: [ErpGatewaySharedModule, RouterModule.forChild(taxReferenceRoute)],
  declarations: [TaxReferenceComponent, TaxReferenceDetailComponent, TaxReferenceUpdateComponent, TaxReferenceDeleteDialogComponent],
  entryComponents: [TaxReferenceDeleteDialogComponent],
})
export class ErpServiceTaxReferenceModule {}
