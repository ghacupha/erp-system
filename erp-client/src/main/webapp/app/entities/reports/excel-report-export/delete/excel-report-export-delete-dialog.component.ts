import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IExcelReportExport } from '../excel-report-export.model';
import { ExcelReportExportService } from '../service/excel-report-export.service';

@Component({
  templateUrl: './excel-report-export-delete-dialog.component.html',
})
export class ExcelReportExportDeleteDialogComponent {
  excelReportExport?: IExcelReportExport;

  constructor(protected excelReportExportService: ExcelReportExportService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.excelReportExportService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
