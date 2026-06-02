import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { AcademicQualificationComponent } from './list/academic-qualification.component';
import { AcademicQualificationDetailComponent } from './detail/academic-qualification-detail.component';
import { AcademicQualificationUpdateComponent } from './update/academic-qualification-update.component';
import { AcademicQualificationDeleteDialogComponent } from './delete/academic-qualification-delete-dialog.component';
import { AcademicQualificationRoutingModule } from './route/academic-qualification-routing.module';

@NgModule({
  imports: [SharedModule, AcademicQualificationRoutingModule],
  declarations: [
    AcademicQualificationComponent,
    AcademicQualificationDetailComponent,
    AcademicQualificationUpdateComponent,
    AcademicQualificationDeleteDialogComponent,
  ],
  entryComponents: [AcademicQualificationDeleteDialogComponent],
})
export class AcademicQualificationModule {}
