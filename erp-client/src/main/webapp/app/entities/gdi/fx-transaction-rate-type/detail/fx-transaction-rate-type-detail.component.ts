import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IFxTransactionRateType } from '../fx-transaction-rate-type.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-fx-transaction-rate-type-detail',
  templateUrl: './fx-transaction-rate-type-detail.component.html',
})
export class FxTransactionRateTypeDetailComponent implements OnInit {
  fxTransactionRateType: IFxTransactionRateType | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ fxTransactionRateType }) => {
      this.fxTransactionRateType = fxTransactionRateType;
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
