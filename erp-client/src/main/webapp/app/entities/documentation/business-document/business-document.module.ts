import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { BusinessDocumentComponent } from './list/business-document.component';
import { BusinessDocumentDetailComponent } from './detail/business-document-detail.component';
import { BusinessDocumentUpdateComponent } from './update/business-document-update.component';
import { BusinessDocumentDeleteDialogComponent } from './delete/business-document-delete-dialog.component';
import { BusinessDocumentRoutingModule } from './route/business-document-routing.module';

@NgModule({
  imports: [SharedModule, BusinessDocumentRoutingModule],
  declarations: [
    BusinessDocumentComponent,
    BusinessDocumentDetailComponent,
    BusinessDocumentUpdateComponent,
    BusinessDocumentDeleteDialogComponent,
  ],
  entryComponents: [BusinessDocumentDeleteDialogComponent],
})
export class BusinessDocumentModule {}
