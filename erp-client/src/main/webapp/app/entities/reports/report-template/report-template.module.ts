import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ReportTemplateComponent } from './list/report-template.component';
import { ReportTemplateDetailComponent } from './detail/report-template-detail.component';
import { ReportTemplateUpdateComponent } from './update/report-template-update.component';
import { ReportTemplateDeleteDialogComponent } from './delete/report-template-delete-dialog.component';
import { ReportTemplateRoutingModule } from './route/report-template-routing.module';

@NgModule({
  imports: [SharedModule, ReportTemplateRoutingModule],
  declarations: [
    ReportTemplateComponent,
    ReportTemplateDetailComponent,
    ReportTemplateUpdateComponent,
    ReportTemplateDeleteDialogComponent,
  ],
  entryComponents: [ReportTemplateDeleteDialogComponent],
})
export class ReportTemplateModule {}
