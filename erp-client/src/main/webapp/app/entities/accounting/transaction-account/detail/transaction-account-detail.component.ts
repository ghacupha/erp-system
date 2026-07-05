import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITransactionAccount } from '../transaction-account.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-transaction-account-detail',
  templateUrl: './transaction-account-detail.component.html',
})
export class TransactionAccountDetailComponent implements OnInit {
  transactionAccount: ITransactionAccount | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ transactionAccount }) => {
      this.transactionAccount = transactionAccount;
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  previousState(): void {
    window.history.back();
  }
}
