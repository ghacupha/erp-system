import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IReportContentType } from '../report-content-type.model';

@Component({
  selector: 'jhi-report-content-type-detail',
  templateUrl: './report-content-type-detail.component.html',
})
export class ReportContentTypeDetailComponent implements OnInit {
  reportContentType: IReportContentType | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ reportContentType }) => {
      this.reportContentType = reportContentType;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
