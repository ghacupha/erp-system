import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ContractStatusComponent } from './list/contract-status.component';
import { ContractStatusDetailComponent } from './detail/contract-status-detail.component';
import { ContractStatusUpdateComponent } from './update/contract-status-update.component';
import { ContractStatusDeleteDialogComponent } from './delete/contract-status-delete-dialog.component';
import { ContractStatusRoutingModule } from './route/contract-status-routing.module';

@NgModule({
  imports: [SharedModule, ContractStatusRoutingModule],
  declarations: [
    ContractStatusComponent,
    ContractStatusDetailComponent,
    ContractStatusUpdateComponent,
    ContractStatusDeleteDialogComponent,
  ],
  entryComponents: [ContractStatusDeleteDialogComponent],
})
export class ContractStatusModule {}
