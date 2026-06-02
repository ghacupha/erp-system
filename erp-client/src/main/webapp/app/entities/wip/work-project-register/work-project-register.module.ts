import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { WorkProjectRegisterComponent } from './list/work-project-register.component';
import { WorkProjectRegisterDetailComponent } from './detail/work-project-register-detail.component';
import { WorkProjectRegisterUpdateComponent } from './update/work-project-register-update.component';
import { WorkProjectRegisterDeleteDialogComponent } from './delete/work-project-register-delete-dialog.component';
import { WorkProjectRegisterRoutingModule } from './route/work-project-register-routing.module';

@NgModule({
  imports: [SharedModule, WorkProjectRegisterRoutingModule],
  declarations: [
    WorkProjectRegisterComponent,
    WorkProjectRegisterDetailComponent,
    WorkProjectRegisterUpdateComponent,
    WorkProjectRegisterDeleteDialogComponent,
  ],
  entryComponents: [WorkProjectRegisterDeleteDialogComponent],
})
export class WorkProjectRegisterModule {}
