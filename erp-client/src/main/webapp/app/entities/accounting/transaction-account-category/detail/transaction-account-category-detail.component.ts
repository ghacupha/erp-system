import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITransactionAccountCategory } from '../transaction-account-category.model';

@Component({
  selector: 'jhi-transaction-account-category-detail',
  templateUrl: './transaction-account-category-detail.component.html',
})
export class TransactionAccountCategoryDetailComponent implements OnInit {
  transactionAccountCategory: ITransactionAccountCategory | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ transactionAccountCategory }) => {
      this.transactionAccountCategory = transactionAccountCategory;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
