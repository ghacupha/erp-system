import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { SettlementComponent } from './list/settlement.component';
import { SettlementDetailComponent } from './detail/settlement-detail.component';
import { SettlementUpdateComponent } from './update/settlement-update.component';
import { SettlementDeleteDialogComponent } from './delete/settlement-delete-dialog.component';
import { SettlementRoutingModule } from './route/settlement-routing.module';

@NgModule({
  imports: [SharedModule, SettlementRoutingModule],
  declarations: [SettlementComponent, SettlementDetailComponent, SettlementUpdateComponent, SettlementDeleteDialogComponent],
  entryComponents: [SettlementDeleteDialogComponent],
})
export class SettlementModule {}
