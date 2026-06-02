import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IReportStatus } from '../report-status.model';

@Component({
  selector: 'jhi-report-status-detail',
  templateUrl: './report-status-detail.component.html',
})
export class ReportStatusDetailComponent implements OnInit {
  reportStatus: IReportStatus | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ reportStatus }) => {
      this.reportStatus = reportStatus;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
