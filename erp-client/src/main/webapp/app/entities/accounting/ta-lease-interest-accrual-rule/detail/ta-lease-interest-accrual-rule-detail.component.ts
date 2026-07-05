import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITALeaseInterestAccrualRule } from '../ta-lease-interest-accrual-rule.model';

@Component({
  selector: 'jhi-ta-lease-interest-accrual-rule-detail',
  templateUrl: './ta-lease-interest-accrual-rule-detail.component.html',
})
export class TALeaseInterestAccrualRuleDetailComponent implements OnInit {
  tALeaseInterestAccrualRule: ITALeaseInterestAccrualRule | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tALeaseInterestAccrualRule }) => {
      this.tALeaseInterestAccrualRule = tALeaseInterestAccrualRule;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
