import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { OutletStatusComponent } from './list/outlet-status.component';
import { OutletStatusDetailComponent } from './detail/outlet-status-detail.component';
import { OutletStatusUpdateComponent } from './update/outlet-status-update.component';
import { OutletStatusDeleteDialogComponent } from './delete/outlet-status-delete-dialog.component';
import { OutletStatusRoutingModule } from './route/outlet-status-routing.module';

@NgModule({
  imports: [SharedModule, OutletStatusRoutingModule],
  declarations: [OutletStatusComponent, OutletStatusDetailComponent, OutletStatusUpdateComponent, OutletStatusDeleteDialogComponent],
  entryComponents: [OutletStatusDeleteDialogComponent],
})
export class OutletStatusModule {}
