import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CustomerIDDocumentTypeComponent } from './list/customer-id-document-type.component';
import { CustomerIDDocumentTypeDetailComponent } from './detail/customer-id-document-type-detail.component';
import { CustomerIDDocumentTypeUpdateComponent } from './update/customer-id-document-type-update.component';
import { CustomerIDDocumentTypeDeleteDialogComponent } from './delete/customer-id-document-type-delete-dialog.component';
import { CustomerIDDocumentTypeRoutingModule } from './route/customer-id-document-type-routing.module';

@NgModule({
  imports: [SharedModule, CustomerIDDocumentTypeRoutingModule],
  declarations: [
    CustomerIDDocumentTypeComponent,
    CustomerIDDocumentTypeDetailComponent,
    CustomerIDDocumentTypeUpdateComponent,
    CustomerIDDocumentTypeDeleteDialogComponent,
  ],
  entryComponents: [CustomerIDDocumentTypeDeleteDialogComponent],
})
export class CustomerIDDocumentTypeModule {}
