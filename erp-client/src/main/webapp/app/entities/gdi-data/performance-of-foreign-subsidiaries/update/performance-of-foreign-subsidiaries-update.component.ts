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

import { IPerformanceOfForeignSubsidiaries, PerformanceOfForeignSubsidiaries } from '../performance-of-foreign-subsidiaries.model';
import { PerformanceOfForeignSubsidiariesService } from '../service/performance-of-foreign-subsidiaries.service';
import { IInstitutionCode } from 'app/entities/gdi/institution-code/institution-code.model';
import { InstitutionCodeService } from 'app/entities/gdi/institution-code/service/institution-code.service';
import { IIsoCountryCode } from 'app/entities/gdi/iso-country-code/iso-country-code.model';
import { IsoCountryCodeService } from 'app/entities/gdi/iso-country-code/service/iso-country-code.service';

@Component({
  selector: 'jhi-performance-of-foreign-subsidiaries-update',
  templateUrl: './performance-of-foreign-subsidiaries-update.component.html',
})
export class PerformanceOfForeignSubsidiariesUpdateComponent implements OnInit {
  isSaving = false;

  institutionCodesSharedCollection: IInstitutionCode[] = [];
  isoCountryCodesSharedCollection: IIsoCountryCode[] = [];

  editForm = this.fb.group({
    id: [],
    subsidiaryName: [null, [Validators.required]],
    reportingDate: [null, [Validators.required]],
    subsidiaryId: [null, [Validators.required]],
    grossLoansAmount: [null, [Validators.required]],
    grossNPALoanAmount: [null, [Validators.required]],
    grossAssetsAmount: [null, [Validators.required]],
    grossDepositsAmount: [null, [Validators.required]],
    profitBeforeTax: [null, [Validators.required]],
    totalCapitalAdequacyRatio: [null, [Validators.required]],
    liquidityRatio: [null, [Validators.required]],
    generalProvisions: [null, [Validators.required]],
    specificProvisions: [null, [Validators.required]],
    interestInSuspenseAmount: [null, [Validators.required]],
    totalNumberOfStaff: [null, [Validators.required, Validators.min(1)]],
    numberOfBranches: [null, [Validators.required, Validators.min(1)]],
    bankCode: [null, Validators.required],
    subsidiaryCountryCode: [null, Validators.required],
  });

  constructor(
    protected performanceOfForeignSubsidiariesService: PerformanceOfForeignSubsidiariesService,
    protected institutionCodeService: InstitutionCodeService,
    protected isoCountryCodeService: IsoCountryCodeService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ performanceOfForeignSubsidiaries }) => {
      this.updateForm(performanceOfForeignSubsidiaries);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const performanceOfForeignSubsidiaries = this.createFromForm();
    if (performanceOfForeignSubsidiaries.id !== undefined) {
      this.subscribeToSaveResponse(this.performanceOfForeignSubsidiariesService.update(performanceOfForeignSubsidiaries));
    } else {
      this.subscribeToSaveResponse(this.performanceOfForeignSubsidiariesService.create(performanceOfForeignSubsidiaries));
    }
  }

  trackInstitutionCodeById(index: number, item: IInstitutionCode): number {
    return item.id!;
  }

  trackIsoCountryCodeById(index: number, item: IIsoCountryCode): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPerformanceOfForeignSubsidiaries>>): void {
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

  protected updateForm(performanceOfForeignSubsidiaries: IPerformanceOfForeignSubsidiaries): void {
    this.editForm.patchValue({
      id: performanceOfForeignSubsidiaries.id,
      subsidiaryName: performanceOfForeignSubsidiaries.subsidiaryName,
      reportingDate: performanceOfForeignSubsidiaries.reportingDate,
      subsidiaryId: performanceOfForeignSubsidiaries.subsidiaryId,
      grossLoansAmount: performanceOfForeignSubsidiaries.grossLoansAmount,
      grossNPALoanAmount: performanceOfForeignSubsidiaries.grossNPALoanAmount,
      grossAssetsAmount: performanceOfForeignSubsidiaries.grossAssetsAmount,
      grossDepositsAmount: performanceOfForeignSubsidiaries.grossDepositsAmount,
      profitBeforeTax: performanceOfForeignSubsidiaries.profitBeforeTax,
      totalCapitalAdequacyRatio: performanceOfForeignSubsidiaries.totalCapitalAdequacyRatio,
      liquidityRatio: performanceOfForeignSubsidiaries.liquidityRatio,
      generalProvisions: performanceOfForeignSubsidiaries.generalProvisions,
      specificProvisions: performanceOfForeignSubsidiaries.specificProvisions,
      interestInSuspenseAmount: performanceOfForeignSubsidiaries.interestInSuspenseAmount,
      totalNumberOfStaff: performanceOfForeignSubsidiaries.totalNumberOfStaff,
      numberOfBranches: performanceOfForeignSubsidiaries.numberOfBranches,
      bankCode: performanceOfForeignSubsidiaries.bankCode,
      subsidiaryCountryCode: performanceOfForeignSubsidiaries.subsidiaryCountryCode,
    });

    this.institutionCodesSharedCollection = this.institutionCodeService.addInstitutionCodeToCollectionIfMissing(
      this.institutionCodesSharedCollection,
      performanceOfForeignSubsidiaries.bankCode
    );
    this.isoCountryCodesSharedCollection = this.isoCountryCodeService.addIsoCountryCodeToCollectionIfMissing(
      this.isoCountryCodesSharedCollection,
      performanceOfForeignSubsidiaries.subsidiaryCountryCode
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

    this.isoCountryCodeService
      .query()
      .pipe(map((res: HttpResponse<IIsoCountryCode[]>) => res.body ?? []))
      .pipe(
        map((isoCountryCodes: IIsoCountryCode[]) =>
          this.isoCountryCodeService.addIsoCountryCodeToCollectionIfMissing(
            isoCountryCodes,
            this.editForm.get('subsidiaryCountryCode')!.value
          )
        )
      )
      .subscribe((isoCountryCodes: IIsoCountryCode[]) => (this.isoCountryCodesSharedCollection = isoCountryCodes));
  }

  protected createFromForm(): IPerformanceOfForeignSubsidiaries {
    return {
      ...new PerformanceOfForeignSubsidiaries(),
      id: this.editForm.get(['id'])!.value,
      subsidiaryName: this.editForm.get(['subsidiaryName'])!.value,
      reportingDate: this.editForm.get(['reportingDate'])!.value,
      subsidiaryId: this.editForm.get(['subsidiaryId'])!.value,
      grossLoansAmount: this.editForm.get(['grossLoansAmount'])!.value,
      grossNPALoanAmount: this.editForm.get(['grossNPALoanAmount'])!.value,
      grossAssetsAmount: this.editForm.get(['grossAssetsAmount'])!.value,
      grossDepositsAmount: this.editForm.get(['grossDepositsAmount'])!.value,
      profitBeforeTax: this.editForm.get(['profitBeforeTax'])!.value,
      totalCapitalAdequacyRatio: this.editForm.get(['totalCapitalAdequacyRatio'])!.value,
      liquidityRatio: this.editForm.get(['liquidityRatio'])!.value,
      generalProvisions: this.editForm.get(['generalProvisions'])!.value,
      specificProvisions: this.editForm.get(['specificProvisions'])!.value,
      interestInSuspenseAmount: this.editForm.get(['interestInSuspenseAmount'])!.value,
      totalNumberOfStaff: this.editForm.get(['totalNumberOfStaff'])!.value,
      numberOfBranches: this.editForm.get(['numberOfBranches'])!.value,
      bankCode: this.editForm.get(['bankCode'])!.value,
      subsidiaryCountryCode: this.editForm.get(['subsidiaryCountryCode'])!.value,
    };
  }
}
