///
/// Erp System - Mark X No 11 (Jehoiada Series) Client 1.7.9
/// Copyright © 2021 - 2024 Edwin Njeru (mailnjeru@gmail.com)
///
/// This program is free software: you can redistribute it and/or modify
/// it under the terms of the GNU General Public License as published by
/// the Free Software Foundation, either version 3 of the License, or
/// (at your option) any later version.
///
/// This program is distributed in the hope that it will be useful,
/// but WITHOUT ANY WARRANTY; without even the implied warranty of
/// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
/// GNU General Public License for more details.
///
/// You should have received a copy of the GNU General Public License
/// along with this program. If not, see <http://www.gnu.org/licenses/>.
///

import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IExchangeRate, ExchangeRate } from '../exchange-rate.model';
import { ExchangeRateService } from '../service/exchange-rate.service';
import { IInstitutionCode } from 'app/entities/gdi/institution-code/institution-code.model';
import { InstitutionCodeService } from 'app/entities/gdi/institution-code/service/institution-code.service';
import { IIsoCurrencyCode } from 'app/entities/gdi/iso-currency-code/iso-currency-code.model';
import { IsoCurrencyCodeService } from 'app/entities/gdi/iso-currency-code/service/iso-currency-code.service';

@Component({
  selector: 'jhi-exchange-rate-update',
  templateUrl: './exchange-rate-update.component.html',
})
export class ExchangeRateUpdateComponent implements OnInit {
  isSaving = false;

  institutionCodesSharedCollection: IInstitutionCode[] = [];
  isoCurrencyCodesSharedCollection: IIsoCurrencyCode[] = [];

  editForm = this.fb.group({
    id: [],
    businessReportingDay: [null, [Validators.required]],
    buyingRate: [null, [Validators.required]],
    sellingRate: [null, [Validators.required]],
    meanRate: [null, [Validators.required]],
    closingBidRate: [null, [Validators.required]],
    closingOfferRate: [null, [Validators.required]],
    usdCrossRate: [null, [Validators.required]],
    institutionCode: [null, Validators.required],
    currencyCode: [null, Validators.required],
  });

  constructor(
    protected exchangeRateService: ExchangeRateService,
    protected institutionCodeService: InstitutionCodeService,
    protected isoCurrencyCodeService: IsoCurrencyCodeService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ exchangeRate }) => {
      this.updateForm(exchangeRate);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const exchangeRate = this.createFromForm();
    if (exchangeRate.id !== undefined) {
      this.subscribeToSaveResponse(this.exchangeRateService.update(exchangeRate));
    } else {
      this.subscribeToSaveResponse(this.exchangeRateService.create(exchangeRate));
    }
  }

  trackInstitutionCodeById(index: number, item: IInstitutionCode): number {
    return item.id!;
  }

  trackIsoCurrencyCodeById(index: number, item: IIsoCurrencyCode): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IExchangeRate>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(exchangeRate: IExchangeRate): void {
    this.editForm.patchValue({
      id: exchangeRate.id,
      businessReportingDay: exchangeRate.businessReportingDay,
      buyingRate: exchangeRate.buyingRate,
      sellingRate: exchangeRate.sellingRate,
      meanRate: exchangeRate.meanRate,
      closingBidRate: exchangeRate.closingBidRate,
      closingOfferRate: exchangeRate.closingOfferRate,
      usdCrossRate: exchangeRate.usdCrossRate,
      institutionCode: exchangeRate.institutionCode,
      currencyCode: exchangeRate.currencyCode,
    });

    this.institutionCodesSharedCollection = this.institutionCodeService.addInstitutionCodeToCollectionIfMissing(
      this.institutionCodesSharedCollection,
      exchangeRate.institutionCode
    );
    this.isoCurrencyCodesSharedCollection = this.isoCurrencyCodeService.addIsoCurrencyCodeToCollectionIfMissing(
      this.isoCurrencyCodesSharedCollection,
      exchangeRate.currencyCode
    );
  }

  protected loadRelationshipsOptions(): void {
    this.institutionCodeService
      .query()
      .pipe(map((res: HttpResponse<IInstitutionCode[]>) => res.body ?? []))
      .pipe(
        map((institutionCodes: IInstitutionCode[]) =>
          this.institutionCodeService.addInstitutionCodeToCollectionIfMissing(institutionCodes, this.editForm.get('institutionCode')!.value)
        )
      )
      .subscribe((institutionCodes: IInstitutionCode[]) => (this.institutionCodesSharedCollection = institutionCodes));

    this.isoCurrencyCodeService
      .query()
      .pipe(map((res: HttpResponse<IIsoCurrencyCode[]>) => res.body ?? []))
      .pipe(
        map((isoCurrencyCodes: IIsoCurrencyCode[]) =>
          this.isoCurrencyCodeService.addIsoCurrencyCodeToCollectionIfMissing(isoCurrencyCodes, this.editForm.get('currencyCode')!.value)
        )
      )
      .subscribe((isoCurrencyCodes: IIsoCurrencyCode[]) => (this.isoCurrencyCodesSharedCollection = isoCurrencyCodes));
  }

  protected createFromForm(): IExchangeRate {
    return {
      ...new ExchangeRate(),
      id: this.editForm.get(['id'])!.value,
      businessReportingDay: this.editForm.get(['businessReportingDay'])!.value,
      buyingRate: this.editForm.get(['buyingRate'])!.value,
      sellingRate: this.editForm.get(['sellingRate'])!.value,
      meanRate: this.editForm.get(['meanRate'])!.value,
      closingBidRate: this.editForm.get(['closingBidRate'])!.value,
      closingOfferRate: this.editForm.get(['closingOfferRate'])!.value,
      usdCrossRate: this.editForm.get(['usdCrossRate'])!.value,
      institutionCode: this.editForm.get(['institutionCode'])!.value,
      currencyCode: this.editForm.get(['currencyCode'])!.value,
    };
  }
}
