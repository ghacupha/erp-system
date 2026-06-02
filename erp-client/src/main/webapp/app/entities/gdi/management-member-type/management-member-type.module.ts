import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ManagementMemberTypeComponent } from './list/management-member-type.component';
import { ManagementMemberTypeDetailComponent } from './detail/management-member-type-detail.component';
import { ManagementMemberTypeUpdateComponent } from './update/management-member-type-update.component';
import { ManagementMemberTypeDeleteDialogComponent } from './delete/management-member-type-delete-dialog.component';
import { ManagementMemberTypeRoutingModule } from './route/management-member-type-routing.module';

@NgModule({
  imports: [SharedModule, ManagementMemberTypeRoutingModule],
  declarations: [
    ManagementMemberTypeComponent,
    ManagementMemberTypeDetailComponent,
    ManagementMemberTypeUpdateComponent,
    ManagementMemberTypeDeleteDialogComponent,
  ],
  entryComponents: [ManagementMemberTypeDeleteDialogComponent],
})
export class ManagementMemberTypeModule {}
