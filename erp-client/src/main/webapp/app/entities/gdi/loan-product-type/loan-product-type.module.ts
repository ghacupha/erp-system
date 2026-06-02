import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { LoanProductTypeComponent } from './list/loan-product-type.component';
import { LoanProductTypeDetailComponent } from './detail/loan-product-type-detail.component';
import { LoanProductTypeUpdateComponent } from './update/loan-product-type-update.component';
import { LoanProductTypeDeleteDialogComponent } from './delete/loan-product-type-delete-dialog.component';
import { LoanProductTypeRoutingModule } from './route/loan-product-type-routing.module';

@NgModule({
  imports: [SharedModule, LoanProductTypeRoutingModule],
  declarations: [
    LoanProductTypeComponent,
    LoanProductTypeDetailComponent,
    LoanProductTypeUpdateComponent,
    LoanProductTypeDeleteDialogComponent,
  ],
  entryComponents: [LoanProductTypeDeleteDialogComponent],
})
export class LoanProductTypeModule {}
