import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IReportDesign } from '../report-design.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-report-design-detail',
  templateUrl: './report-design-detail.component.html',
})
export class ReportDesignDetailComponent implements OnInit {
  reportDesign: IReportDesign | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ reportDesign }) => {
      this.reportDesign = reportDesign;
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
