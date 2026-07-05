import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IReportTemplate } from '../report-template.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-report-template-detail',
  templateUrl: './report-template-detail.component.html',
})
export class ReportTemplateDetailComponent implements OnInit {
  reportTemplate: IReportTemplate | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ reportTemplate }) => {
      this.reportTemplate = reportTemplate;
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
