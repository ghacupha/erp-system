import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITAAmortizationRule } from '../ta-amortization-rule.model';

@Component({
  selector: 'jhi-ta-amortization-rule-detail',
  templateUrl: './ta-amortization-rule-detail.component.html',
})
export class TAAmortizationRuleDetailComponent implements OnInit {
  tAAmortizationRule: ITAAmortizationRule | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tAAmortizationRule }) => {
      this.tAAmortizationRule = tAAmortizationRule;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
