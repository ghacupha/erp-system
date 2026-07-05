import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ILeaseAmortizationCalculation } from '../lease-amortization-calculation.model';

@Component({
  selector: 'jhi-lease-amortization-calculation-detail',
  templateUrl: './lease-amortization-calculation-detail.component.html',
})
export class LeaseAmortizationCalculationDetailComponent implements OnInit {
  leaseAmortizationCalculation: ILeaseAmortizationCalculation | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ leaseAmortizationCalculation }) => {
      this.leaseAmortizationCalculation = leaseAmortizationCalculation;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
