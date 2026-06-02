import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IExcelReportExport } from '../excel-report-export.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-excel-report-export-detail',
  templateUrl: './excel-report-export-detail.component.html',
})
export class ExcelReportExportDetailComponent implements OnInit {
  excelReportExport: IExcelReportExport | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ excelReportExport }) => {
      this.excelReportExport = excelReportExport;
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  previousState(): void {
    window.history.back();
  }
}
