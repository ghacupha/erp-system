import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CustomerComplaintStatusTypeComponent } from './list/customer-complaint-status-type.component';
import { CustomerComplaintStatusTypeDetailComponent } from './detail/customer-complaint-status-type-detail.component';
import { CustomerComplaintStatusTypeUpdateComponent } from './update/customer-complaint-status-type-update.component';
import { CustomerComplaintStatusTypeDeleteDialogComponent } from './delete/customer-complaint-status-type-delete-dialog.component';
import { CustomerComplaintStatusTypeRoutingModule } from './route/customer-complaint-status-type-routing.module';

@NgModule({
  imports: [SharedModule, CustomerComplaintStatusTypeRoutingModule],
  declarations: [
    CustomerComplaintStatusTypeComponent,
    CustomerComplaintStatusTypeDetailComponent,
    CustomerComplaintStatusTypeUpdateComponent,
    CustomerComplaintStatusTypeDeleteDialogComponent,
  ],
  entryComponents: [CustomerComplaintStatusTypeDeleteDialogComponent],
})
export class CustomerComplaintStatusTypeModule {}
