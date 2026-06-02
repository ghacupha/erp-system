import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITALeaseRepaymentRule } from '../ta-lease-repayment-rule.model';

@Component({
  selector: 'jhi-ta-lease-repayment-rule-detail',
  templateUrl: './ta-lease-repayment-rule-detail.component.html',
})
export class TALeaseRepaymentRuleDetailComponent implements OnInit {
  tALeaseRepaymentRule: ITALeaseRepaymentRule | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tALeaseRepaymentRule }) => {
      this.tALeaseRepaymentRule = tALeaseRepaymentRule;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
