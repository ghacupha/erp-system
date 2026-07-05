import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IWorkInProgressReport } from '../work-in-progress-report.model';

@Component({
  selector: 'jhi-work-in-progress-report-detail',
  templateUrl: './work-in-progress-report-detail.component.html',
})
export class WorkInProgressReportDetailComponent implements OnInit {
  workInProgressReport: IWorkInProgressReport | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ workInProgressReport }) => {
      this.workInProgressReport = workInProgressReport;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
