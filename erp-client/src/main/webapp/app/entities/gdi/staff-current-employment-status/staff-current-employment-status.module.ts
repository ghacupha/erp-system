import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { StaffCurrentEmploymentStatusComponent } from './list/staff-current-employment-status.component';
import { StaffCurrentEmploymentStatusDetailComponent } from './detail/staff-current-employment-status-detail.component';
import { StaffCurrentEmploymentStatusUpdateComponent } from './update/staff-current-employment-status-update.component';
import { StaffCurrentEmploymentStatusDeleteDialogComponent } from './delete/staff-current-employment-status-delete-dialog.component';
import { StaffCurrentEmploymentStatusRoutingModule } from './route/staff-current-employment-status-routing.module';

@NgModule({
  imports: [SharedModule, StaffCurrentEmploymentStatusRoutingModule],
  declarations: [
    StaffCurrentEmploymentStatusComponent,
    StaffCurrentEmploymentStatusDetailComponent,
    StaffCurrentEmploymentStatusUpdateComponent,
    StaffCurrentEmploymentStatusDeleteDialogComponent,
  ],
  entryComponents: [StaffCurrentEmploymentStatusDeleteDialogComponent],
})
export class StaffCurrentEmploymentStatusModule {}
