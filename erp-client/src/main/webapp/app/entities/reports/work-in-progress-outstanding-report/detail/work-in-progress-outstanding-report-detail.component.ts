import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IWorkInProgressOutstandingReport } from '../work-in-progress-outstanding-report.model';

@Component({
  selector: 'jhi-work-in-progress-outstanding-report-detail',
  templateUrl: './work-in-progress-outstanding-report-detail.component.html',
})
export class WorkInProgressOutstandingReportDetailComponent implements OnInit {
  workInProgressOutstandingReport: IWorkInProgressOutstandingReport | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ workInProgressOutstandingReport }) => {
      this.workInProgressOutstandingReport = workInProgressOutstandingReport;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
