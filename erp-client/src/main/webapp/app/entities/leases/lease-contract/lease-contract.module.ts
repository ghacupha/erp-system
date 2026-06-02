import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { LeaseContractComponent } from './list/lease-contract.component';
import { LeaseContractDetailComponent } from './detail/lease-contract-detail.component';
import { LeaseContractUpdateComponent } from './update/lease-contract-update.component';
import { LeaseContractDeleteDialogComponent } from './delete/lease-contract-delete-dialog.component';
import { LeaseContractRoutingModule } from './route/lease-contract-routing.module';

@NgModule({
  imports: [SharedModule, LeaseContractRoutingModule],
  declarations: [LeaseContractComponent, LeaseContractDetailComponent, LeaseContractUpdateComponent, LeaseContractDeleteDialogComponent],
  entryComponents: [LeaseContractDeleteDialogComponent],
})
export class LeaseContractModule {}
