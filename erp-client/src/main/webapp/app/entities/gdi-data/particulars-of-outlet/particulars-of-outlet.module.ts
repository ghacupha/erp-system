import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ParticularsOfOutletComponent } from './list/particulars-of-outlet.component';
import { ParticularsOfOutletDetailComponent } from './detail/particulars-of-outlet-detail.component';
import { ParticularsOfOutletUpdateComponent } from './update/particulars-of-outlet-update.component';
import { ParticularsOfOutletDeleteDialogComponent } from './delete/particulars-of-outlet-delete-dialog.component';
import { ParticularsOfOutletRoutingModule } from './route/particulars-of-outlet-routing.module';

@NgModule({
  imports: [SharedModule, ParticularsOfOutletRoutingModule],
  declarations: [
    ParticularsOfOutletComponent,
    ParticularsOfOutletDetailComponent,
    ParticularsOfOutletUpdateComponent,
    ParticularsOfOutletDeleteDialogComponent,
  ],
  entryComponents: [ParticularsOfOutletDeleteDialogComponent],
})
export class ParticularsOfOutletModule {}
