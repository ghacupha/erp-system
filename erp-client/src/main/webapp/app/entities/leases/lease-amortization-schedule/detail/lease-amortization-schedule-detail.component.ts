import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ILeaseAmortizationSchedule } from '../lease-amortization-schedule.model';

@Component({
  selector: 'jhi-lease-amortization-schedule-detail',
  templateUrl: './lease-amortization-schedule-detail.component.html',
})
export class LeaseAmortizationScheduleDetailComponent implements OnInit {
  leaseAmortizationSchedule: ILeaseAmortizationSchedule | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ leaseAmortizationSchedule }) => {
      this.leaseAmortizationSchedule = leaseAmortizationSchedule;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
