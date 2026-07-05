import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CrbCustomerTypeComponent } from './list/crb-customer-type.component';
import { CrbCustomerTypeDetailComponent } from './detail/crb-customer-type-detail.component';
import { CrbCustomerTypeUpdateComponent } from './update/crb-customer-type-update.component';
import { CrbCustomerTypeDeleteDialogComponent } from './delete/crb-customer-type-delete-dialog.component';
import { CrbCustomerTypeRoutingModule } from './route/crb-customer-type-routing.module';

@NgModule({
  imports: [SharedModule, CrbCustomerTypeRoutingModule],
  declarations: [
    CrbCustomerTypeComponent,
    CrbCustomerTypeDetailComponent,
    CrbCustomerTypeUpdateComponent,
    CrbCustomerTypeDeleteDialogComponent,
  ],
  entryComponents: [CrbCustomerTypeDeleteDialogComponent],
})
export class CrbCustomerTypeModule {}
