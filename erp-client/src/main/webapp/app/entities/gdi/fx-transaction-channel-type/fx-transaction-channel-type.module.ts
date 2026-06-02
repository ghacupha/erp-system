import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { FxTransactionChannelTypeComponent } from './list/fx-transaction-channel-type.component';
import { FxTransactionChannelTypeDetailComponent } from './detail/fx-transaction-channel-type-detail.component';
import { FxTransactionChannelTypeUpdateComponent } from './update/fx-transaction-channel-type-update.component';
import { FxTransactionChannelTypeDeleteDialogComponent } from './delete/fx-transaction-channel-type-delete-dialog.component';
import { FxTransactionChannelTypeRoutingModule } from './route/fx-transaction-channel-type-routing.module';

@NgModule({
  imports: [SharedModule, FxTransactionChannelTypeRoutingModule],
  declarations: [
    FxTransactionChannelTypeComponent,
    FxTransactionChannelTypeDetailComponent,
    FxTransactionChannelTypeUpdateComponent,
    FxTransactionChannelTypeDeleteDialogComponent,
  ],
  entryComponents: [FxTransactionChannelTypeDeleteDialogComponent],
})
export class FxTransactionChannelTypeModule {}
