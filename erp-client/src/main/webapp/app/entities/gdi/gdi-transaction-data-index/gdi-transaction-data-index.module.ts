import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { GdiTransactionDataIndexComponent } from './list/gdi-transaction-data-index.component';
import { GdiTransactionDataIndexDetailComponent } from './detail/gdi-transaction-data-index-detail.component';
import { GdiTransactionDataIndexUpdateComponent } from './update/gdi-transaction-data-index-update.component';
import { GdiTransactionDataIndexDeleteDialogComponent } from './delete/gdi-transaction-data-index-delete-dialog.component';
import { GdiTransactionDataIndexRoutingModule } from './route/gdi-transaction-data-index-routing.module';

@NgModule({
  imports: [SharedModule, GdiTransactionDataIndexRoutingModule],
  declarations: [
    GdiTransactionDataIndexComponent,
    GdiTransactionDataIndexDetailComponent,
    GdiTransactionDataIndexUpdateComponent,
    GdiTransactionDataIndexDeleteDialogComponent,
  ],
  entryComponents: [GdiTransactionDataIndexDeleteDialogComponent],
})
export class GdiTransactionDataIndexModule {}
