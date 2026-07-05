import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IFraudCategoryFlag } from '../fraud-category-flag.model';

@Component({
  selector: 'jhi-fraud-category-flag-detail',
  templateUrl: './fraud-category-flag-detail.component.html',
})
export class FraudCategoryFlagDetailComponent implements OnInit {
  fraudCategoryFlag: IFraudCategoryFlag | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ fraudCategoryFlag }) => {
      this.fraudCategoryFlag = fraudCategoryFlag;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
