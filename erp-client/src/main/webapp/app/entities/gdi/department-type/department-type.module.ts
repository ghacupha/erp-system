import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { DepartmentTypeComponent } from './list/department-type.component';
import { DepartmentTypeDetailComponent } from './detail/department-type-detail.component';
import { DepartmentTypeUpdateComponent } from './update/department-type-update.component';
import { DepartmentTypeDeleteDialogComponent } from './delete/department-type-delete-dialog.component';
import { DepartmentTypeRoutingModule } from './route/department-type-routing.module';

@NgModule({
  imports: [SharedModule, DepartmentTypeRoutingModule],
  declarations: [
    DepartmentTypeComponent,
    DepartmentTypeDetailComponent,
    DepartmentTypeUpdateComponent,
    DepartmentTypeDeleteDialogComponent,
  ],
  entryComponents: [DepartmentTypeDeleteDialogComponent],
})
export class DepartmentTypeModule {}
