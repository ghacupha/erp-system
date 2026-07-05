import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IFinancialDerivativeTypeCode } from '../financial-derivative-type-code.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-financial-derivative-type-code-detail',
  templateUrl: './financial-derivative-type-code-detail.component.html',
})
export class FinancialDerivativeTypeCodeDetailComponent implements OnInit {
  financialDerivativeTypeCode: IFinancialDerivativeTypeCode | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ financialDerivativeTypeCode }) => {
      this.financialDerivativeTypeCode = financialDerivativeTypeCode;
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
