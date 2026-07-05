import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IWorkInProgressOverview } from '../work-in-progress-overview.model';

@Component({
  selector: 'jhi-work-in-progress-overview-detail',
  templateUrl: './work-in-progress-overview-detail.component.html',
})
export class WorkInProgressOverviewDetailComponent implements OnInit {
  workInProgressOverview: IWorkInProgressOverview | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ workInProgressOverview }) => {
      this.workInProgressOverview = workInProgressOverview;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
