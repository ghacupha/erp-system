import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDepreciationJob } from '../depreciation-job.model';

@Component({
  selector: 'jhi-depreciation-job-detail',
  templateUrl: './depreciation-job-detail.component.html',
})
export class DepreciationJobDetailComponent implements OnInit {
  depreciationJob: IDepreciationJob | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ depreciationJob }) => {
      this.depreciationJob = depreciationJob;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
