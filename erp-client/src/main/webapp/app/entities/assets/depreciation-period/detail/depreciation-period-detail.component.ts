import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDepreciationPeriod } from '../depreciation-period.model';

@Component({
  selector: 'jhi-depreciation-period-detail',
  templateUrl: './depreciation-period-detail.component.html',
})
export class DepreciationPeriodDetailComponent implements OnInit {
  depreciationPeriod: IDepreciationPeriod | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ depreciationPeriod }) => {
      this.depreciationPeriod = depreciationPeriod;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
