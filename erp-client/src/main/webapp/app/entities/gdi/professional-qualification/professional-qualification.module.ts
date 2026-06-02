import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ProfessionalQualificationComponent } from './list/professional-qualification.component';
import { ProfessionalQualificationDetailComponent } from './detail/professional-qualification-detail.component';
import { ProfessionalQualificationUpdateComponent } from './update/professional-qualification-update.component';
import { ProfessionalQualificationDeleteDialogComponent } from './delete/professional-qualification-delete-dialog.component';
import { ProfessionalQualificationRoutingModule } from './route/professional-qualification-routing.module';

@NgModule({
  imports: [SharedModule, ProfessionalQualificationRoutingModule],
  declarations: [
    ProfessionalQualificationComponent,
    ProfessionalQualificationDetailComponent,
    ProfessionalQualificationUpdateComponent,
    ProfessionalQualificationDeleteDialogComponent,
  ],
  entryComponents: [ProfessionalQualificationDeleteDialogComponent],
})
export class ProfessionalQualificationModule {}
