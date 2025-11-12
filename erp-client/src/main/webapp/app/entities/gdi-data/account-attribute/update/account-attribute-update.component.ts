///
/// Erp System - Mark X No 11 (Jehoiada Series) Client 1.7.8
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

import { IAccountAttribute, AccountAttribute } from '../account-attribute.model';
import { AccountAttributeService } from '../service/account-attribute.service';
import { IInstitutionCode } from 'app/entities/gdi/institution-code/institution-code.model';
import { InstitutionCodeService } from 'app/entities/gdi/institution-code/service/institution-code.service';
import { IBankBranchCode } from 'app/entities/system/bank-branch-code/bank-branch-code.model';
import { BankBranchCodeService } from 'app/entities/system/bank-branch-code/service/bank-branch-code.service';
import { IAccountOwnershipType } from 'app/entities/gdi/account-ownership-type/account-ownership-type.model';
import { AccountOwnershipTypeService } from 'app/entities/gdi/account-ownership-type/service/account-ownership-type.service';

@Component({
  selector: 'jhi-account-attribute-update',
  templateUrl: './account-attribute-update.component.html',
})
export class AccountAttributeUpdateComponent implements OnInit {
  isSaving = false;

  institutionCodesSharedCollection: IInstitutionCode[] = [];
  bankBranchCodesSharedCollection: IBankBranchCode[] = [];
  accountOwnershipTypesSharedCollection: IAccountOwnershipType[] = [];

  editForm = this.fb.group({
    id: [],
    reportingDate: [null, [Validators.required]],
    customerNumber: [null, [Validators.required]],
    accountContractNumber: [null, [Validators.required]],
    accountName: [null, [Validators.required]],
    accountOpeningDate: [],
    accountClosingDate: [],
    debitInterestRate: [null, [Validators.required]],
    creditInterestRate: [null, [Validators.required]],
    sanctionedAccountLimitFcy: [null, [Validators.required]],
    sanctionedAccountLimitLcy: [null, [Validators.required]],
    accountStatusChangeDate: [],
    expiryDate: [],
    bankCode: [null, Validators.required],
    branchCode: [null, Validators.required],
    accountOwnershipType: [null, Validators.required],
  });

  constructor(
    protected accountAttributeService: AccountAttributeService,
    protected institutionCodeService: InstitutionCodeService,
    protected bankBranchCodeService: BankBranchCodeService,
    protected accountOwnershipTypeService: AccountOwnershipTypeService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ accountAttribute }) => {
      this.updateForm(accountAttribute);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const accountAttribute = this.createFromForm();
    if (accountAttribute.id !== undefined) {
      this.subscribeToSaveResponse(this.accountAttributeService.update(accountAttribute));
    } else {
      this.subscribeToSaveResponse(this.accountAttributeService.create(accountAttribute));
    }
  }

  trackInstitutionCodeById(index: number, item: IInstitutionCode): number {
    return item.id!;
  }

  trackBankBranchCodeById(index: number, item: IBankBranchCode): number {
    return item.id!;
  }

  trackAccountOwnershipTypeById(index: number, item: IAccountOwnershipType): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAccountAttribute>>): void {
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

  protected updateForm(accountAttribute: IAccountAttribute): void {
    this.editForm.patchValue({
      id: accountAttribute.id,
      reportingDate: accountAttribute.reportingDate,
      customerNumber: accountAttribute.customerNumber,
      accountContractNumber: accountAttribute.accountContractNumber,
      accountName: accountAttribute.accountName,
      accountOpeningDate: accountAttribute.accountOpeningDate,
      accountClosingDate: accountAttribute.accountClosingDate,
      debitInterestRate: accountAttribute.debitInterestRate,
      creditInterestRate: accountAttribute.creditInterestRate,
      sanctionedAccountLimitFcy: accountAttribute.sanctionedAccountLimitFcy,
      sanctionedAccountLimitLcy: accountAttribute.sanctionedAccountLimitLcy,
      accountStatusChangeDate: accountAttribute.accountStatusChangeDate,
      expiryDate: accountAttribute.expiryDate,
      bankCode: accountAttribute.bankCode,
      branchCode: accountAttribute.branchCode,
      accountOwnershipType: accountAttribute.accountOwnershipType,
    });

    this.institutionCodesSharedCollection = this.institutionCodeService.addInstitutionCodeToCollectionIfMissing(
      this.institutionCodesSharedCollection,
      accountAttribute.bankCode
    );
    this.bankBranchCodesSharedCollection = this.bankBranchCodeService.addBankBranchCodeToCollectionIfMissing(
      this.bankBranchCodesSharedCollection,
      accountAttribute.branchCode
    );
    this.accountOwnershipTypesSharedCollection = this.accountOwnershipTypeService.addAccountOwnershipTypeToCollectionIfMissing(
      this.accountOwnershipTypesSharedCollection,
      accountAttribute.accountOwnershipType
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

    this.accountOwnershipTypeService
      .query()
      .pipe(map((res: HttpResponse<IAccountOwnershipType[]>) => res.body ?? []))
      .pipe(
        map((accountOwnershipTypes: IAccountOwnershipType[]) =>
          this.accountOwnershipTypeService.addAccountOwnershipTypeToCollectionIfMissing(
            accountOwnershipTypes,
            this.editForm.get('accountOwnershipType')!.value
          )
        )
      )
      .subscribe((accountOwnershipTypes: IAccountOwnershipType[]) => (this.accountOwnershipTypesSharedCollection = accountOwnershipTypes));
  }

  protected createFromForm(): IAccountAttribute {
    return {
      ...new AccountAttribute(),
      id: this.editForm.get(['id'])!.value,
      reportingDate: this.editForm.get(['reportingDate'])!.value,
      customerNumber: this.editForm.get(['customerNumber'])!.value,
      accountContractNumber: this.editForm.get(['accountContractNumber'])!.value,
      accountName: this.editForm.get(['accountName'])!.value,
      accountOpeningDate: this.editForm.get(['accountOpeningDate'])!.value,
      accountClosingDate: this.editForm.get(['accountClosingDate'])!.value,
      debitInterestRate: this.editForm.get(['debitInterestRate'])!.value,
      creditInterestRate: this.editForm.get(['creditInterestRate'])!.value,
      sanctionedAccountLimitFcy: this.editForm.get(['sanctionedAccountLimitFcy'])!.value,
      sanctionedAccountLimitLcy: this.editForm.get(['sanctionedAccountLimitLcy'])!.value,
      accountStatusChangeDate: this.editForm.get(['accountStatusChangeDate'])!.value,
      expiryDate: this.editForm.get(['expiryDate'])!.value,
      bankCode: this.editForm.get(['bankCode'])!.value,
      branchCode: this.editForm.get(['branchCode'])!.value,
      accountOwnershipType: this.editForm.get(['accountOwnershipType'])!.value,
    };
  }
}
