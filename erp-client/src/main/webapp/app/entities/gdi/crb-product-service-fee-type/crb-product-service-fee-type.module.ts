import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CrbProductServiceFeeTypeComponent } from './list/crb-product-service-fee-type.component';
import { CrbProductServiceFeeTypeDetailComponent } from './detail/crb-product-service-fee-type-detail.component';
import { CrbProductServiceFeeTypeUpdateComponent } from './update/crb-product-service-fee-type-update.component';
import { CrbProductServiceFeeTypeDeleteDialogComponent } from './delete/crb-product-service-fee-type-delete-dialog.component';
import { CrbProductServiceFeeTypeRoutingModule } from './route/crb-product-service-fee-type-routing.module';

@NgModule({
  imports: [SharedModule, CrbProductServiceFeeTypeRoutingModule],
  declarations: [
    CrbProductServiceFeeTypeComponent,
    CrbProductServiceFeeTypeDetailComponent,
    CrbProductServiceFeeTypeUpdateComponent,
    CrbProductServiceFeeTypeDeleteDialogComponent,
  ],
  entryComponents: [CrbProductServiceFeeTypeDeleteDialogComponent],
})
export class CrbProductServiceFeeTypeModule {}
