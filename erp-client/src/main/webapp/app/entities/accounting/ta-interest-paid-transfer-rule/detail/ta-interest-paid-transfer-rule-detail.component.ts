import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITAInterestPaidTransferRule } from '../ta-interest-paid-transfer-rule.model';

@Component({
  selector: 'jhi-ta-interest-paid-transfer-rule-detail',
  templateUrl: './ta-interest-paid-transfer-rule-detail.component.html',
})
export class TAInterestPaidTransferRuleDetailComponent implements OnInit {
  tAInterestPaidTransferRule: ITAInterestPaidTransferRule | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tAInterestPaidTransferRule }) => {
      this.tAInterestPaidTransferRule = tAInterestPaidTransferRule;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
