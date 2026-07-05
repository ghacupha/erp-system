import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { FxTransactionTypeComponent } from './list/fx-transaction-type.component';
import { FxTransactionTypeDetailComponent } from './detail/fx-transaction-type-detail.component';
import { FxTransactionTypeUpdateComponent } from './update/fx-transaction-type-update.component';
import { FxTransactionTypeDeleteDialogComponent } from './delete/fx-transaction-type-delete-dialog.component';
import { FxTransactionTypeRoutingModule } from './route/fx-transaction-type-routing.module';

@NgModule({
  imports: [SharedModule, FxTransactionTypeRoutingModule],
  declarations: [
    FxTransactionTypeComponent,
    FxTransactionTypeDetailComponent,
    FxTransactionTypeUpdateComponent,
    FxTransactionTypeDeleteDialogComponent,
  ],
  entryComponents: [FxTransactionTypeDeleteDialogComponent],
})
export class FxTransactionTypeModule {}
