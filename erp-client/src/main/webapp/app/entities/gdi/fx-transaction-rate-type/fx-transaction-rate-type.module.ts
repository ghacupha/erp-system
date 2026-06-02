import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { FxTransactionRateTypeComponent } from './list/fx-transaction-rate-type.component';
import { FxTransactionRateTypeDetailComponent } from './detail/fx-transaction-rate-type-detail.component';
import { FxTransactionRateTypeUpdateComponent } from './update/fx-transaction-rate-type-update.component';
import { FxTransactionRateTypeDeleteDialogComponent } from './delete/fx-transaction-rate-type-delete-dialog.component';
import { FxTransactionRateTypeRoutingModule } from './route/fx-transaction-rate-type-routing.module';

@NgModule({
  imports: [SharedModule, FxTransactionRateTypeRoutingModule],
  declarations: [
    FxTransactionRateTypeComponent,
    FxTransactionRateTypeDetailComponent,
    FxTransactionRateTypeUpdateComponent,
    FxTransactionRateTypeDeleteDialogComponent,
  ],
  entryComponents: [FxTransactionRateTypeDeleteDialogComponent],
})
export class FxTransactionRateTypeModule {}
