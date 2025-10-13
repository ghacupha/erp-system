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

import { ICreditCardFacility, CreditCardFacility } from '../credit-card-facility.model';
import { CreditCardFacilityService } from '../service/credit-card-facility.service';
import { IInstitutionCode } from 'app/entities/gdi/institution-code/institution-code.model';
import { InstitutionCodeService } from 'app/entities/gdi/institution-code/service/institution-code.service';
import { ICreditCardOwnership } from 'app/entities/gdi/credit-card-ownership/credit-card-ownership.model';
import { CreditCardOwnershipService } from 'app/entities/gdi/credit-card-ownership/service/credit-card-ownership.service';
import { IIsoCurrencyCode } from 'app/entities/gdi/iso-currency-code/iso-currency-code.model';
import { IsoCurrencyCodeService } from 'app/entities/gdi/iso-currency-code/service/iso-currency-code.service';

@Component({
  selector: 'jhi-credit-card-facility-update',
  templateUrl: './credit-card-facility-update.component.html',
})
export class CreditCardFacilityUpdateComponent implements OnInit {
  isSaving = false;

  institutionCodesSharedCollection: IInstitutionCode[] = [];
  creditCardOwnershipsSharedCollection: ICreditCardOwnership[] = [];
  isoCurrencyCodesSharedCollection: IIsoCurrencyCode[] = [];

  editForm = this.fb.group({
    id: [],
    reportingDate: [null, [Validators.required]],
    totalNumberOfActiveCreditCards: [null, [Validators.required, Validators.min(0)]],
    totalCreditCardLimitsInCCY: [null, [Validators.required, Validators.min(0)]],
    totalCreditCardLimitsInLCY: [null, [Validators.required, Validators.min(0)]],
    totalCreditCardAmountUtilisedInCCY: [null, [Validators.required, Validators.min(0)]],
    totalCreditCardAmountUtilisedInLcy: [null, [Validators.required, Validators.min(0)]],
    totalNPACreditCardAmountInFCY: [null, [Validators.required, Validators.min(0)]],
    totalNPACreditCardAmountInLCY: [null, [Validators.required, Validators.min(0)]],
    bankCode: [null, Validators.required],
    customerCategory: [null, Validators.required],
    currencyCode: [null, Validators.required],
  });

  constructor(
    protected creditCardFacilityService: CreditCardFacilityService,
    protected institutionCodeService: InstitutionCodeService,
    protected creditCardOwnershipService: CreditCardOwnershipService,
    protected isoCurrencyCodeService: IsoCurrencyCodeService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ creditCardFacility }) => {
      this.updateForm(creditCardFacility);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const creditCardFacility = this.createFromForm();
    if (creditCardFacility.id !== undefined) {
      this.subscribeToSaveResponse(this.creditCardFacilityService.update(creditCardFacility));
    } else {
      this.subscribeToSaveResponse(this.creditCardFacilityService.create(creditCardFacility));
    }
  }

  trackInstitutionCodeById(index: number, item: IInstitutionCode): number {
    return item.id!;
  }

  trackCreditCardOwnershipById(index: number, item: ICreditCardOwnership): number {
    return item.id!;
  }

  trackIsoCurrencyCodeById(index: number, item: IIsoCurrencyCode): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICreditCardFacility>>): void {
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

  protected updateForm(creditCardFacility: ICreditCardFacility): void {
    this.editForm.patchValue({
      id: creditCardFacility.id,
      reportingDate: creditCardFacility.reportingDate,
      totalNumberOfActiveCreditCards: creditCardFacility.totalNumberOfActiveCreditCards,
      totalCreditCardLimitsInCCY: creditCardFacility.totalCreditCardLimitsInCCY,
      totalCreditCardLimitsInLCY: creditCardFacility.totalCreditCardLimitsInLCY,
      totalCreditCardAmountUtilisedInCCY: creditCardFacility.totalCreditCardAmountUtilisedInCCY,
      totalCreditCardAmountUtilisedInLcy: creditCardFacility.totalCreditCardAmountUtilisedInLcy,
      totalNPACreditCardAmountInFCY: creditCardFacility.totalNPACreditCardAmountInFCY,
      totalNPACreditCardAmountInLCY: creditCardFacility.totalNPACreditCardAmountInLCY,
      bankCode: creditCardFacility.bankCode,
      customerCategory: creditCardFacility.customerCategory,
      currencyCode: creditCardFacility.currencyCode,
    });

    this.institutionCodesSharedCollection = this.institutionCodeService.addInstitutionCodeToCollectionIfMissing(
      this.institutionCodesSharedCollection,
      creditCardFacility.bankCode
    );
    this.creditCardOwnershipsSharedCollection = this.creditCardOwnershipService.addCreditCardOwnershipToCollectionIfMissing(
      this.creditCardOwnershipsSharedCollection,
      creditCardFacility.customerCategory
    );
    this.isoCurrencyCodesSharedCollection = this.isoCurrencyCodeService.addIsoCurrencyCodeToCollectionIfMissing(
      this.isoCurrencyCodesSharedCollection,
      creditCardFacility.currencyCode
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

    this.creditCardOwnershipService
      .query()
      .pipe(map((res: HttpResponse<ICreditCardOwnership[]>) => res.body ?? []))
      .pipe(
        map((creditCardOwnerships: ICreditCardOwnership[]) =>
          this.creditCardOwnershipService.addCreditCardOwnershipToCollectionIfMissing(
            creditCardOwnerships,
            this.editForm.get('customerCategory')!.value
          )
        )
      )
      .subscribe((creditCardOwnerships: ICreditCardOwnership[]) => (this.creditCardOwnershipsSharedCollection = creditCardOwnerships));

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

  protected createFromForm(): ICreditCardFacility {
    return {
      ...new CreditCardFacility(),
      id: this.editForm.get(['id'])!.value,
      reportingDate: this.editForm.get(['reportingDate'])!.value,
      totalNumberOfActiveCreditCards: this.editForm.get(['totalNumberOfActiveCreditCards'])!.value,
      totalCreditCardLimitsInCCY: this.editForm.get(['totalCreditCardLimitsInCCY'])!.value,
      totalCreditCardLimitsInLCY: this.editForm.get(['totalCreditCardLimitsInLCY'])!.value,
      totalCreditCardAmountUtilisedInCCY: this.editForm.get(['totalCreditCardAmountUtilisedInCCY'])!.value,
      totalCreditCardAmountUtilisedInLcy: this.editForm.get(['totalCreditCardAmountUtilisedInLcy'])!.value,
      totalNPACreditCardAmountInFCY: this.editForm.get(['totalNPACreditCardAmountInFCY'])!.value,
      totalNPACreditCardAmountInLCY: this.editForm.get(['totalNPACreditCardAmountInLCY'])!.value,
      bankCode: this.editForm.get(['bankCode'])!.value,
      customerCategory: this.editForm.get(['customerCategory'])!.value,
      currencyCode: this.editForm.get(['currencyCode'])!.value,
    };
  }
}
