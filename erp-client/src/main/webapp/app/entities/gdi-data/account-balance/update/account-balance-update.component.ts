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

import { IAccountBalance, AccountBalance } from '../account-balance.model';
import { AccountBalanceService } from '../service/account-balance.service';
import { IInstitutionCode } from 'app/entities/gdi/institution-code/institution-code.model';
import { InstitutionCodeService } from 'app/entities/gdi/institution-code/service/institution-code.service';
import { IBankBranchCode } from 'app/entities/system/bank-branch-code/bank-branch-code.model';
import { BankBranchCodeService } from 'app/entities/system/bank-branch-code/service/bank-branch-code.service';
import { IIsoCurrencyCode } from 'app/entities/gdi/iso-currency-code/iso-currency-code.model';
import { IsoCurrencyCodeService } from 'app/entities/gdi/iso-currency-code/service/iso-currency-code.service';

@Component({
  selector: 'jhi-account-balance-update',
  templateUrl: './account-balance-update.component.html',
})
export class AccountBalanceUpdateComponent implements OnInit {
  isSaving = false;

  institutionCodesSharedCollection: IInstitutionCode[] = [];
  bankBranchCodesSharedCollection: IBankBranchCode[] = [];
  isoCurrencyCodesSharedCollection: IIsoCurrencyCode[] = [];

  editForm = this.fb.group({
    id: [],
    reportingDate: [null, [Validators.required]],
    customerId: [null, [Validators.required]],
    accountContractNumber: [
      null,
      [Validators.required, Validators.minLength(12), Validators.maxLength(16), Validators.pattern('^\\d{15}$')],
    ],
    accruedInterestBalanceFCY: [null, [Validators.required]],
    accruedInterestBalanceLCY: [null, [Validators.required]],
    accountBalanceFCY: [null, [Validators.required]],
    accountBalanceLCY: [null, [Validators.required]],
    bankCode: [null, Validators.required],
    branchId: [null, Validators.required],
    currencyCode: [null, Validators.required],
  });

  constructor(
    protected accountBalanceService: AccountBalanceService,
    protected institutionCodeService: InstitutionCodeService,
    protected bankBranchCodeService: BankBranchCodeService,
    protected isoCurrencyCodeService: IsoCurrencyCodeService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ accountBalance }) => {
      this.updateForm(accountBalance);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const accountBalance = this.createFromForm();
    if (accountBalance.id !== undefined) {
      this.subscribeToSaveResponse(this.accountBalanceService.update(accountBalance));
    } else {
      this.subscribeToSaveResponse(this.accountBalanceService.create(accountBalance));
    }
  }

  trackInstitutionCodeById(index: number, item: IInstitutionCode): number {
    return item.id!;
  }

  trackBankBranchCodeById(index: number, item: IBankBranchCode): number {
    return item.id!;
  }

  trackIsoCurrencyCodeById(index: number, item: IIsoCurrencyCode): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAccountBalance>>): void {
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

  protected updateForm(accountBalance: IAccountBalance): void {
    this.editForm.patchValue({
      id: accountBalance.id,
      reportingDate: accountBalance.reportingDate,
      customerId: accountBalance.customerId,
      accountContractNumber: accountBalance.accountContractNumber,
      accruedInterestBalanceFCY: accountBalance.accruedInterestBalanceFCY,
      accruedInterestBalanceLCY: accountBalance.accruedInterestBalanceLCY,
      accountBalanceFCY: accountBalance.accountBalanceFCY,
      accountBalanceLCY: accountBalance.accountBalanceLCY,
      bankCode: accountBalance.bankCode,
      branchId: accountBalance.branchId,
      currencyCode: accountBalance.currencyCode,
    });

    this.institutionCodesSharedCollection = this.institutionCodeService.addInstitutionCodeToCollectionIfMissing(
      this.institutionCodesSharedCollection,
      accountBalance.bankCode
    );
    this.bankBranchCodesSharedCollection = this.bankBranchCodeService.addBankBranchCodeToCollectionIfMissing(
      this.bankBranchCodesSharedCollection,
      accountBalance.branchId
    );
    this.isoCurrencyCodesSharedCollection = this.isoCurrencyCodeService.addIsoCurrencyCodeToCollectionIfMissing(
      this.isoCurrencyCodesSharedCollection,
      accountBalance.currencyCode
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

  protected createFromForm(): IAccountBalance {
    return {
      ...new AccountBalance(),
      id: this.editForm.get(['id'])!.value,
      reportingDate: this.editForm.get(['reportingDate'])!.value,
      customerId: this.editForm.get(['customerId'])!.value,
      accountContractNumber: this.editForm.get(['accountContractNumber'])!.value,
      accruedInterestBalanceFCY: this.editForm.get(['accruedInterestBalanceFCY'])!.value,
      accruedInterestBalanceLCY: this.editForm.get(['accruedInterestBalanceLCY'])!.value,
      accountBalanceFCY: this.editForm.get(['accountBalanceFCY'])!.value,
      accountBalanceLCY: this.editForm.get(['accountBalanceLCY'])!.value,
      bankCode: this.editForm.get(['bankCode'])!.value,
      branchId: this.editForm.get(['branchId'])!.value,
      currencyCode: this.editForm.get(['currencyCode'])!.value,
    };
  }
}
