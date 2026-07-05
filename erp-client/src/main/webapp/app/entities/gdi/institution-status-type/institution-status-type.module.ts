import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { InstitutionStatusTypeComponent } from './list/institution-status-type.component';
import { InstitutionStatusTypeDetailComponent } from './detail/institution-status-type-detail.component';
import { InstitutionStatusTypeUpdateComponent } from './update/institution-status-type-update.component';
import { InstitutionStatusTypeDeleteDialogComponent } from './delete/institution-status-type-delete-dialog.component';
import { InstitutionStatusTypeRoutingModule } from './route/institution-status-type-routing.module';

@NgModule({
  imports: [SharedModule, InstitutionStatusTypeRoutingModule],
  declarations: [
    InstitutionStatusTypeComponent,
    InstitutionStatusTypeDetailComponent,
    InstitutionStatusTypeUpdateComponent,
    InstitutionStatusTypeDeleteDialogComponent,
  ],
  entryComponents: [InstitutionStatusTypeDeleteDialogComponent],
})
export class InstitutionStatusTypeModule {}
