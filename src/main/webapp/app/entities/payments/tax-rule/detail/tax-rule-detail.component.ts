import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITaxRule } from '../tax-rule.model';

@Component({
  selector: 'jhi-tax-rule-detail',
  templateUrl: './tax-rule-detail.component.html',
})
export class TaxRuleDetailComponent implements OnInit {
  taxRule: ITaxRule | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ taxRule }) => {
      this.taxRule = taxRule;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
