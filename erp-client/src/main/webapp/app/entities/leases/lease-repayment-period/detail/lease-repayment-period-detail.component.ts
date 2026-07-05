import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ILeaseRepaymentPeriod } from '../lease-repayment-period.model';

@Component({
  selector: 'jhi-lease-repayment-period-detail',
  templateUrl: './lease-repayment-period-detail.component.html',
})
export class LeaseRepaymentPeriodDetailComponent implements OnInit {
  leaseRepaymentPeriod: ILeaseRepaymentPeriod | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ leaseRepaymentPeriod }) => {
      this.leaseRepaymentPeriod = leaseRepaymentPeriod;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
