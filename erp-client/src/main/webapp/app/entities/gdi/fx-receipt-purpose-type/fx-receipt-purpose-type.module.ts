import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { FxReceiptPurposeTypeComponent } from './list/fx-receipt-purpose-type.component';
import { FxReceiptPurposeTypeDetailComponent } from './detail/fx-receipt-purpose-type-detail.component';
import { FxReceiptPurposeTypeUpdateComponent } from './update/fx-receipt-purpose-type-update.component';
import { FxReceiptPurposeTypeDeleteDialogComponent } from './delete/fx-receipt-purpose-type-delete-dialog.component';
import { FxReceiptPurposeTypeRoutingModule } from './route/fx-receipt-purpose-type-routing.module';

@NgModule({
  imports: [SharedModule, FxReceiptPurposeTypeRoutingModule],
  declarations: [
    FxReceiptPurposeTypeComponent,
    FxReceiptPurposeTypeDetailComponent,
    FxReceiptPurposeTypeUpdateComponent,
    FxReceiptPurposeTypeDeleteDialogComponent,
  ],
  entryComponents: [FxReceiptPurposeTypeDeleteDialogComponent],
})
export class FxReceiptPurposeTypeModule {}
