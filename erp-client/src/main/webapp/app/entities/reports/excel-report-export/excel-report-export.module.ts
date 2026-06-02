import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ExcelReportExportComponent } from './list/excel-report-export.component';
import { ExcelReportExportDetailComponent } from './detail/excel-report-export-detail.component';
import { ExcelReportExportUpdateComponent } from './update/excel-report-export-update.component';
import { ExcelReportExportDeleteDialogComponent } from './delete/excel-report-export-delete-dialog.component';
import { ExcelReportExportRoutingModule } from './route/excel-report-export-routing.module';

@NgModule({
  imports: [SharedModule, ExcelReportExportRoutingModule],
  declarations: [
    ExcelReportExportComponent,
    ExcelReportExportDetailComponent,
    ExcelReportExportUpdateComponent,
    ExcelReportExportDeleteDialogComponent,
  ],
  entryComponents: [ExcelReportExportDeleteDialogComponent],
})
export class ExcelReportExportModule {}
