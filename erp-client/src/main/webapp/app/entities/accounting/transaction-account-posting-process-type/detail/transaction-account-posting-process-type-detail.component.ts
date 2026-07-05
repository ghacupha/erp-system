import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITransactionAccountPostingProcessType } from '../transaction-account-posting-process-type.model';

@Component({
  selector: 'jhi-transaction-account-posting-process-type-detail',
  templateUrl: './transaction-account-posting-process-type-detail.component.html',
})
export class TransactionAccountPostingProcessTypeDetailComponent implements OnInit {
  transactionAccountPostingProcessType: ITransactionAccountPostingProcessType | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ transactionAccountPostingProcessType }) => {
      this.transactionAccountPostingProcessType = transactionAccountPostingProcessType;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
