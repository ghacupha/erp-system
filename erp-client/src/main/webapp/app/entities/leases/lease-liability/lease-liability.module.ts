import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { LeaseLiabilityComponent } from './list/lease-liability.component';
import { LeaseLiabilityDetailComponent } from './detail/lease-liability-detail.component';
import { LeaseLiabilityUpdateComponent } from './update/lease-liability-update.component';
import { LeaseLiabilityDeleteDialogComponent } from './delete/lease-liability-delete-dialog.component';
import { LeaseLiabilityRoutingModule } from './route/lease-liability-routing.module';

@NgModule({
  imports: [SharedModule, LeaseLiabilityRoutingModule],
  declarations: [
    LeaseLiabilityComponent,
    LeaseLiabilityDetailComponent,
    LeaseLiabilityUpdateComponent,
    LeaseLiabilityDeleteDialogComponent,
  ],
  entryComponents: [LeaseLiabilityDeleteDialogComponent],
})
export class LeaseLiabilityModule {}
