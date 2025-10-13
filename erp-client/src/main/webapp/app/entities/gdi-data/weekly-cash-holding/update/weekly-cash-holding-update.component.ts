///
/// Erp System - Mark X No 10 (Jehoiada Series) Client 1.7.8
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

import { IWeeklyCashHolding, WeeklyCashHolding } from '../weekly-cash-holding.model';
import { WeeklyCashHoldingService } from '../service/weekly-cash-holding.service';
import { IInstitutionCode } from 'app/entities/gdi/institution-code/institution-code.model';
import { InstitutionCodeService } from 'app/entities/gdi/institution-code/service/institution-code.service';
import { IBankBranchCode } from 'app/entities/system/bank-branch-code/bank-branch-code.model';
import { BankBranchCodeService } from 'app/entities/system/bank-branch-code/service/bank-branch-code.service';
import { ICountySubCountyCode } from 'app/entities/gdi-data/county-sub-county-code/county-sub-county-code.model';
import { CountySubCountyCodeService } from 'app/entities/gdi-data/county-sub-county-code/service/county-sub-county-code.service';
import { IKenyanCurrencyDenomination } from 'app/entities/gdi/kenyan-currency-denomination/kenyan-currency-denomination.model';
import { KenyanCurrencyDenominationService } from 'app/entities/gdi/kenyan-currency-denomination/service/kenyan-currency-denomination.service';

@Component({
  selector: 'jhi-weekly-cash-holding-update',
  templateUrl: './weekly-cash-holding-update.component.html',
})
export class WeeklyCashHoldingUpdateComponent implements OnInit {
  isSaving = false;

  institutionCodesSharedCollection: IInstitutionCode[] = [];
  bankBranchCodesSharedCollection: IBankBranchCode[] = [];
  countySubCountyCodesSharedCollection: ICountySubCountyCode[] = [];
  kenyanCurrencyDenominationsSharedCollection: IKenyanCurrencyDenomination[] = [];

  editForm = this.fb.group({
    id: [],
    reportingDate: [null, [Validators.required]],
    fitUnits: [null, [Validators.required]],
    unfitUnits: [null, [Validators.required]],
    bankCode: [null, Validators.required],
    branchId: [null, Validators.required],
    subCountyCode: [null, Validators.required],
    denomination: [null, Validators.required],
  });

  constructor(
    protected weeklyCashHoldingService: WeeklyCashHoldingService,
    protected institutionCodeService: InstitutionCodeService,
    protected bankBranchCodeService: BankBranchCodeService,
    protected countySubCountyCodeService: CountySubCountyCodeService,
    protected kenyanCurrencyDenominationService: KenyanCurrencyDenominationService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ weeklyCashHolding }) => {
      this.updateForm(weeklyCashHolding);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const weeklyCashHolding = this.createFromForm();
    if (weeklyCashHolding.id !== undefined) {
      this.subscribeToSaveResponse(this.weeklyCashHoldingService.update(weeklyCashHolding));
    } else {
      this.subscribeToSaveResponse(this.weeklyCashHoldingService.create(weeklyCashHolding));
    }
  }

  trackInstitutionCodeById(index: number, item: IInstitutionCode): number {
    return item.id!;
  }

  trackBankBranchCodeById(index: number, item: IBankBranchCode): number {
    return item.id!;
  }

  trackCountySubCountyCodeById(index: number, item: ICountySubCountyCode): number {
    return item.id!;
  }

  trackKenyanCurrencyDenominationById(index: number, item: IKenyanCurrencyDenomination): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IWeeklyCashHolding>>): void {
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

  protected updateForm(weeklyCashHolding: IWeeklyCashHolding): void {
    this.editForm.patchValue({
      id: weeklyCashHolding.id,
      reportingDate: weeklyCashHolding.reportingDate,
      fitUnits: weeklyCashHolding.fitUnits,
      unfitUnits: weeklyCashHolding.unfitUnits,
      bankCode: weeklyCashHolding.bankCode,
      branchId: weeklyCashHolding.branchId,
      subCountyCode: weeklyCashHolding.subCountyCode,
      denomination: weeklyCashHolding.denomination,
    });

    this.institutionCodesSharedCollection = this.institutionCodeService.addInstitutionCodeToCollectionIfMissing(
      this.institutionCodesSharedCollection,
      weeklyCashHolding.bankCode
    );
    this.bankBranchCodesSharedCollection = this.bankBranchCodeService.addBankBranchCodeToCollectionIfMissing(
      this.bankBranchCodesSharedCollection,
      weeklyCashHolding.branchId
    );
    this.countySubCountyCodesSharedCollection = this.countySubCountyCodeService.addCountySubCountyCodeToCollectionIfMissing(
      this.countySubCountyCodesSharedCollection,
      weeklyCashHolding.subCountyCode
    );
    this.kenyanCurrencyDenominationsSharedCollection =
      this.kenyanCurrencyDenominationService.addKenyanCurrencyDenominationToCollectionIfMissing(
        this.kenyanCurrencyDenominationsSharedCollection,
        weeklyCashHolding.denomination
      );
  }

  protected loadRelationshipsOptions(): void {
    this.institutionCodeService
      .query()
      .pipe(map((res: HttpResponse<IInstitutionCode[]>) => res.body ?? []))
      .pipe(
        map((institutionCodes: IInstitutionCode[]) =>
          this.institutionCodeService.addInstitutionCodeToCollectionIfMissing(institutionCodes, this.editForm.get('bankCode')!.value)
        )
      )
      .subscribe((institutionCodes: IInstitutionCode[]) => (this.institutionCodesSharedCollection = institutionCodes));

    this.bankBranchCodeService
      .query()
      .pipe(map((res: HttpResponse<IBankBranchCode[]>) => res.body ?? []))
      .pipe(
        map((bankBranchCodes: IBankBranchCode[]) =>
          this.bankBranchCodeService.addBankBranchCodeToCollectionIfMissing(bankBranchCodes, this.editForm.get('branchId')!.value)
        )
      )
      .subscribe((bankBranchCodes: IBankBranchCode[]) => (this.bankBranchCodesSharedCollection = bankBranchCodes));

    this.countySubCountyCodeService
      .query()
      .pipe(map((res: HttpResponse<ICountySubCountyCode[]>) => res.body ?? []))
      .pipe(
        map((countySubCountyCodes: ICountySubCountyCode[]) =>
          this.countySubCountyCodeService.addCountySubCountyCodeToCollectionIfMissing(
            countySubCountyCodes,
            this.editForm.get('subCountyCode')!.value
          )
        )
      )
      .subscribe((countySubCountyCodes: ICountySubCountyCode[]) => (this.countySubCountyCodesSharedCollection = countySubCountyCodes));

    this.kenyanCurrencyDenominationService
      .query()
      .pipe(map((res: HttpResponse<IKenyanCurrencyDenomination[]>) => res.body ?? []))
      .pipe(
        map((kenyanCurrencyDenominations: IKenyanCurrencyDenomination[]) =>
          this.kenyanCurrencyDenominationService.addKenyanCurrencyDenominationToCollectionIfMissing(
            kenyanCurrencyDenominations,
            this.editForm.get('denomination')!.value
          )
        )
      )
      .subscribe(
        (kenyanCurrencyDenominations: IKenyanCurrencyDenomination[]) =>
          (this.kenyanCurrencyDenominationsSharedCollection = kenyanCurrencyDenominations)
      );
  }

  protected createFromForm(): IWeeklyCashHolding {
    return {
      ...new WeeklyCashHolding(),
      id: this.editForm.get(['id'])!.value,
      reportingDate: this.editForm.get(['reportingDate'])!.value,
      fitUnits: this.editForm.get(['fitUnits'])!.value,
      unfitUnits: this.editForm.get(['unfitUnits'])!.value,
      bankCode: this.editForm.get(['bankCode'])!.value,
      branchId: this.editForm.get(['branchId'])!.value,
      subCountyCode: this.editForm.get(['subCountyCode'])!.value,
      denomination: this.editForm.get(['denomination'])!.value,
    };
  }
}
