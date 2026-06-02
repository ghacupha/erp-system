import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { StaffRoleTypeComponent } from './list/staff-role-type.component';
import { StaffRoleTypeDetailComponent } from './detail/staff-role-type-detail.component';
import { StaffRoleTypeUpdateComponent } from './update/staff-role-type-update.component';
import { StaffRoleTypeDeleteDialogComponent } from './delete/staff-role-type-delete-dialog.component';
import { StaffRoleTypeRoutingModule } from './route/staff-role-type-routing.module';

@NgModule({
  imports: [SharedModule, StaffRoleTypeRoutingModule],
  declarations: [StaffRoleTypeComponent, StaffRoleTypeDetailComponent, StaffRoleTypeUpdateComponent, StaffRoleTypeDeleteDialogComponent],
  entryComponents: [StaffRoleTypeDeleteDialogComponent],
})
export class StaffRoleTypeModule {}
