import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAccountBalance } from '../account-balance.model';

@Component({
  selector: 'jhi-account-balance-detail',
  templateUrl: './account-balance-detail.component.html',
})
export class AccountBalanceDetailComponent implements OnInit {
  accountBalance: IAccountBalance | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ accountBalance }) => {
      this.accountBalance = accountBalance;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
