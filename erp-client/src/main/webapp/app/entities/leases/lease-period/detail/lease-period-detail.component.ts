import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ILeasePeriod } from '../lease-period.model';

@Component({
  selector: 'jhi-lease-period-detail',
  templateUrl: './lease-period-detail.component.html',
})
export class LeasePeriodDetailComponent implements OnInit {
  leasePeriod: ILeasePeriod | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ leasePeriod }) => {
      this.leasePeriod = leasePeriod;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
