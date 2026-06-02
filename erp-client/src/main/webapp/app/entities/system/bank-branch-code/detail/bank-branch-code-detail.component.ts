import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IBankBranchCode } from '../bank-branch-code.model';

@Component({
  selector: 'jhi-bank-branch-code-detail',
  templateUrl: './bank-branch-code-detail.component.html',
})
export class BankBranchCodeDetailComponent implements OnInit {
  bankBranchCode: IBankBranchCode | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ bankBranchCode }) => {
      this.bankBranchCode = bankBranchCode;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
