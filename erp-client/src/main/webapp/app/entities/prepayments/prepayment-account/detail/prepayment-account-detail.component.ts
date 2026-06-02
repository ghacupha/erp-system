import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPrepaymentAccount } from '../prepayment-account.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-prepayment-account-detail',
  templateUrl: './prepayment-account-detail.component.html',
})
export class PrepaymentAccountDetailComponent implements OnInit {
  prepaymentAccount: IPrepaymentAccount | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ prepaymentAccount }) => {
      this.prepaymentAccount = prepaymentAccount;
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
