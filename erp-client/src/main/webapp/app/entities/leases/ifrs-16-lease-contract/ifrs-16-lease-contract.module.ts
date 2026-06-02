import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { IFRS16LeaseContractComponent } from './list/ifrs-16-lease-contract.component';
import { IFRS16LeaseContractDetailComponent } from './detail/ifrs-16-lease-contract-detail.component';
import { IFRS16LeaseContractUpdateComponent } from './update/ifrs-16-lease-contract-update.component';
import { IFRS16LeaseContractDeleteDialogComponent } from './delete/ifrs-16-lease-contract-delete-dialog.component';
import { IFRS16LeaseContractRoutingModule } from './route/ifrs-16-lease-contract-routing.module';

@NgModule({
  imports: [SharedModule, IFRS16LeaseContractRoutingModule],
  declarations: [
    IFRS16LeaseContractComponent,
    IFRS16LeaseContractDetailComponent,
    IFRS16LeaseContractUpdateComponent,
    IFRS16LeaseContractDeleteDialogComponent,
  ],
  entryComponents: [IFRS16LeaseContractDeleteDialogComponent],
})
export class IFRS16LeaseContractModule {}
