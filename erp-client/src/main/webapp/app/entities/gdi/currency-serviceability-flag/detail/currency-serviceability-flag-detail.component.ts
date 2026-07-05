import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICurrencyServiceabilityFlag } from '../currency-serviceability-flag.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-currency-serviceability-flag-detail',
  templateUrl: './currency-serviceability-flag-detail.component.html',
})
export class CurrencyServiceabilityFlagDetailComponent implements OnInit {
  currencyServiceabilityFlag: ICurrencyServiceabilityFlag | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ currencyServiceabilityFlag }) => {
      this.currencyServiceabilityFlag = currencyServiceabilityFlag;
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
