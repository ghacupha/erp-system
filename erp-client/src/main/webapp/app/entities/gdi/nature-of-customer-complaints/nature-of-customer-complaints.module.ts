import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { NatureOfCustomerComplaintsComponent } from './list/nature-of-customer-complaints.component';
import { NatureOfCustomerComplaintsDetailComponent } from './detail/nature-of-customer-complaints-detail.component';
import { NatureOfCustomerComplaintsUpdateComponent } from './update/nature-of-customer-complaints-update.component';
import { NatureOfCustomerComplaintsDeleteDialogComponent } from './delete/nature-of-customer-complaints-delete-dialog.component';
import { NatureOfCustomerComplaintsRoutingModule } from './route/nature-of-customer-complaints-routing.module';

@NgModule({
  imports: [SharedModule, NatureOfCustomerComplaintsRoutingModule],
  declarations: [
    NatureOfCustomerComplaintsComponent,
    NatureOfCustomerComplaintsDetailComponent,
    NatureOfCustomerComplaintsUpdateComponent,
    NatureOfCustomerComplaintsDeleteDialogComponent,
  ],
  entryComponents: [NatureOfCustomerComplaintsDeleteDialogComponent],
})
export class NatureOfCustomerComplaintsModule {}
