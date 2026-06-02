import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ReportContentTypeComponent } from './list/report-content-type.component';
import { ReportContentTypeDetailComponent } from './detail/report-content-type-detail.component';
import { ReportContentTypeUpdateComponent } from './update/report-content-type-update.component';
import { ReportContentTypeDeleteDialogComponent } from './delete/report-content-type-delete-dialog.component';
import { ReportContentTypeRoutingModule } from './route/report-content-type-routing.module';

@NgModule({
  imports: [SharedModule, ReportContentTypeRoutingModule],
  declarations: [
    ReportContentTypeComponent,
    ReportContentTypeDetailComponent,
    ReportContentTypeUpdateComponent,
    ReportContentTypeDeleteDialogComponent,
  ],
  entryComponents: [ReportContentTypeDeleteDialogComponent],
})
export class ReportContentTypeModule {}
