import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { FinancialDerivativeTypeCodeComponent } from './list/financial-derivative-type-code.component';
import { FinancialDerivativeTypeCodeDetailComponent } from './detail/financial-derivative-type-code-detail.component';
import { FinancialDerivativeTypeCodeUpdateComponent } from './update/financial-derivative-type-code-update.component';
import { FinancialDerivativeTypeCodeDeleteDialogComponent } from './delete/financial-derivative-type-code-delete-dialog.component';
import { FinancialDerivativeTypeCodeRoutingModule } from './route/financial-derivative-type-code-routing.module';

@NgModule({
  imports: [SharedModule, FinancialDerivativeTypeCodeRoutingModule],
  declarations: [
    FinancialDerivativeTypeCodeComponent,
    FinancialDerivativeTypeCodeDetailComponent,
    FinancialDerivativeTypeCodeUpdateComponent,
    FinancialDerivativeTypeCodeDeleteDialogComponent,
  ],
  entryComponents: [FinancialDerivativeTypeCodeDeleteDialogComponent],
})
export class FinancialDerivativeTypeCodeModule {}
