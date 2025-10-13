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

import { ICollateralInformation, CollateralInformation } from '../collateral-information.model';
import { CollateralInformationService } from '../service/collateral-information.service';
import { IInstitutionCode } from 'app/entities/gdi/institution-code/institution-code.model';
import { InstitutionCodeService } from 'app/entities/gdi/institution-code/service/institution-code.service';
import { IBankBranchCode } from 'app/entities/system/bank-branch-code/bank-branch-code.model';
import { BankBranchCodeService } from 'app/entities/system/bank-branch-code/service/bank-branch-code.service';
import { ICollateralType } from 'app/entities/gdi/collateral-type/collateral-type.model';
import { CollateralTypeService } from 'app/entities/gdi/collateral-type/service/collateral-type.service';
import { ICountySubCountyCode } from 'app/entities/gdi-data/county-sub-county-code/county-sub-county-code.model';
import { CountySubCountyCodeService } from 'app/entities/gdi-data/county-sub-county-code/service/county-sub-county-code.service';
import { CollateralInsuredFlagTypes } from 'app/entities/enumerations/collateral-insured-flag-types.model';

@Component({
  selector: 'jhi-collateral-information-update',
  templateUrl: './collateral-information-update.component.html',
})
export class CollateralInformationUpdateComponent implements OnInit {
  isSaving = false;
  collateralInsuredFlagTypesValues = Object.keys(CollateralInsuredFlagTypes);

  institutionCodesSharedCollection: IInstitutionCode[] = [];
  bankBranchCodesSharedCollection: IBankBranchCode[] = [];
  collateralTypesSharedCollection: ICollateralType[] = [];
  countySubCountyCodesSharedCollection: ICountySubCountyCode[] = [];

  editForm = this.fb.group({
    id: [],
    reportingDate: [null, [Validators.required]],
    collateralId: [null, [Validators.required]],
    loanContractId: [null, [Validators.required, Validators.pattern('^\\d{15}$')]],
    customerId: [null, [Validators.required]],
    registrationPropertyNumber: [],
    collateralOMVInCCY: [null, [Validators.required, Validators.min(0)]],
    collateralFSVInLCY: [null, [Validators.required, Validators.min(0)]],
    collateralDiscountedValue: [null, [Validators.min(0)]],
    amountCharged: [null, [Validators.required, Validators.min(0)]],
    collateralDiscountRate: [null, [Validators.min(0)]],
    loanToValueRatio: [null, [Validators.min(0)]],
    nameOfPropertyValuer: [],
    collateralLastValuationDate: [],
    insuredFlag: [null, [Validators.required]],
    nameOfInsurer: [],
    amountInsured: [null, [Validators.min(0)]],
    insuranceExpiryDate: [],
    guaranteeInsurers: [],
    bankCode: [null, Validators.required],
    branchCode: [null, Validators.required],
    collateralType: [null, Validators.required],
    countyCode: [],
  });

  constructor(
    protected collateralInformationService: CollateralInformationService,
    protected institutionCodeService: InstitutionCodeService,
    protected bankBranchCodeService: BankBranchCodeService,
    protected collateralTypeService: CollateralTypeService,
    protected countySubCountyCodeService: CountySubCountyCodeService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ collateralInformation }) => {
      this.updateForm(collateralInformation);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const collateralInformation = this.createFromForm();
    if (collateralInformation.id !== undefined) {
      this.subscribeToSaveResponse(this.collateralInformationService.update(collateralInformation));
    } else {
      this.subscribeToSaveResponse(this.collateralInformationService.create(collateralInformation));
    }
  }

  trackInstitutionCodeById(index: number, item: IInstitutionCode): number {
    return item.id!;
  }

  trackBankBranchCodeById(index: number, item: IBankBranchCode): number {
    return item.id!;
  }

  trackCollateralTypeById(index: number, item: ICollateralType): number {
    return item.id!;
  }

  trackCountySubCountyCodeById(index: number, item: ICountySubCountyCode): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICollateralInformation>>): void {
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

  protected updateForm(collateralInformation: ICollateralInformation): void {
    this.editForm.patchValue({
      id: collateralInformation.id,
      reportingDate: collateralInformation.reportingDate,
      collateralId: collateralInformation.collateralId,
      loanContractId: collateralInformation.loanContractId,
      customerId: collateralInformation.customerId,
      registrationPropertyNumber: collateralInformation.registrationPropertyNumber,
      collateralOMVInCCY: collateralInformation.collateralOMVInCCY,
      collateralFSVInLCY: collateralInformation.collateralFSVInLCY,
      collateralDiscountedValue: collateralInformation.collateralDiscountedValue,
      amountCharged: collateralInformation.amountCharged,
      collateralDiscountRate: collateralInformation.collateralDiscountRate,
      loanToValueRatio: collateralInformation.loanToValueRatio,
      nameOfPropertyValuer: collateralInformation.nameOfPropertyValuer,
      collateralLastValuationDate: collateralInformation.collateralLastValuationDate,
      insuredFlag: collateralInformation.insuredFlag,
      nameOfInsurer: collateralInformation.nameOfInsurer,
      amountInsured: collateralInformation.amountInsured,
      insuranceExpiryDate: collateralInformation.insuranceExpiryDate,
      guaranteeInsurers: collateralInformation.guaranteeInsurers,
      bankCode: collateralInformation.bankCode,
      branchCode: collateralInformation.branchCode,
      collateralType: collateralInformation.collateralType,
      countyCode: collateralInformation.countyCode,
    });

    this.institutionCodesSharedCollection = this.institutionCodeService.addInstitutionCodeToCollectionIfMissing(
      this.institutionCodesSharedCollection,
      collateralInformation.bankCode
    );
    this.bankBranchCodesSharedCollection = this.bankBranchCodeService.addBankBranchCodeToCollectionIfMissing(
      this.bankBranchCodesSharedCollection,
      collateralInformation.branchCode
    );
    this.collateralTypesSharedCollection = this.collateralTypeService.addCollateralTypeToCollectionIfMissing(
      this.collateralTypesSharedCollection,
      collateralInformation.collateralType
    );
    this.countySubCountyCodesSharedCollection = this.countySubCountyCodeService.addCountySubCountyCodeToCollectionIfMissing(
      this.countySubCountyCodesSharedCollection,
      collateralInformation.countyCode
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
          this.bankBranchCodeService.addBankBranchCodeToCollectionIfMissing(bankBranchCodes, this.editForm.get('branchCode')!.value)
        )
      )
      .subscribe((bankBranchCodes: IBankBranchCode[]) => (this.bankBranchCodesSharedCollection = bankBranchCodes));

    this.collateralTypeService
      .query()
      .pipe(map((res: HttpResponse<ICollateralType[]>) => res.body ?? []))
      .pipe(
        map((collateralTypes: ICollateralType[]) =>
          this.collateralTypeService.addCollateralTypeToCollectionIfMissing(collateralTypes, this.editForm.get('collateralType')!.value)
        )
      )
      .subscribe((collateralTypes: ICollateralType[]) => (this.collateralTypesSharedCollection = collateralTypes));

    this.countySubCountyCodeService
      .query()
      .pipe(map((res: HttpResponse<ICountySubCountyCode[]>) => res.body ?? []))
      .pipe(
        map((countySubCountyCodes: ICountySubCountyCode[]) =>
          this.countySubCountyCodeService.addCountySubCountyCodeToCollectionIfMissing(
            countySubCountyCodes,
            this.editForm.get('countyCode')!.value
          )
        )
      )
      .subscribe((countySubCountyCodes: ICountySubCountyCode[]) => (this.countySubCountyCodesSharedCollection = countySubCountyCodes));
  }

  protected createFromForm(): ICollateralInformation {
    return {
      ...new CollateralInformation(),
      id: this.editForm.get(['id'])!.value,
      reportingDate: this.editForm.get(['reportingDate'])!.value,
      collateralId: this.editForm.get(['collateralId'])!.value,
      loanContractId: this.editForm.get(['loanContractId'])!.value,
      customerId: this.editForm.get(['customerId'])!.value,
      registrationPropertyNumber: this.editForm.get(['registrationPropertyNumber'])!.value,
      collateralOMVInCCY: this.editForm.get(['collateralOMVInCCY'])!.value,
      collateralFSVInLCY: this.editForm.get(['collateralFSVInLCY'])!.value,
      collateralDiscountedValue: this.editForm.get(['collateralDiscountedValue'])!.value,
      amountCharged: this.editForm.get(['amountCharged'])!.value,
      collateralDiscountRate: this.editForm.get(['collateralDiscountRate'])!.value,
      loanToValueRatio: this.editForm.get(['loanToValueRatio'])!.value,
      nameOfPropertyValuer: this.editForm.get(['nameOfPropertyValuer'])!.value,
      collateralLastValuationDate: this.editForm.get(['collateralLastValuationDate'])!.value,
      insuredFlag: this.editForm.get(['insuredFlag'])!.value,
      nameOfInsurer: this.editForm.get(['nameOfInsurer'])!.value,
      amountInsured: this.editForm.get(['amountInsured'])!.value,
      insuranceExpiryDate: this.editForm.get(['insuranceExpiryDate'])!.value,
      guaranteeInsurers: this.editForm.get(['guaranteeInsurers'])!.value,
      bankCode: this.editForm.get(['bankCode'])!.value,
      branchCode: this.editForm.get(['branchCode'])!.value,
      collateralType: this.editForm.get(['collateralType'])!.value,
      countyCode: this.editForm.get(['countyCode'])!.value,
    };
  }
}
