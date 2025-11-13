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

import { ITALeaseRepaymentRule, TALeaseRepaymentRule } from '../ta-lease-repayment-rule.model';
import { TALeaseRepaymentRuleService } from '../service/ta-lease-repayment-rule.service';
import { IIFRS16LeaseContract } from 'app/entities/leases/ifrs-16-lease-contract/ifrs-16-lease-contract.model';
import { IFRS16LeaseContractService } from 'app/entities/leases/ifrs-16-lease-contract/service/ifrs-16-lease-contract.service';
import { ITransactionAccount } from 'app/entities/accounting/transaction-account/transaction-account.model';
import { TransactionAccountService } from 'app/entities/accounting/transaction-account/service/transaction-account.service';
import { IPlaceholder } from 'app/entities/system/placeholder/placeholder.model';
import { PlaceholderService } from 'app/entities/system/placeholder/service/placeholder.service';

@Component({
  selector: 'jhi-ta-lease-repayment-rule-update',
  templateUrl: './ta-lease-repayment-rule-update.component.html',
})
export class TALeaseRepaymentRuleUpdateComponent implements OnInit {
  isSaving = false;

  leaseContractsCollection: IIFRS16LeaseContract[] = [];
  transactionAccountsSharedCollection: ITransactionAccount[] = [];
  placeholdersSharedCollection: IPlaceholder[] = [];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    identifier: [null, [Validators.required]],
    leaseContract: [null, Validators.required],
    debit: [null, Validators.required],
    credit: [null, Validators.required],
    placeholders: [],
  });

  constructor(
    protected tALeaseRepaymentRuleService: TALeaseRepaymentRuleService,
    protected iFRS16LeaseContractService: IFRS16LeaseContractService,
    protected transactionAccountService: TransactionAccountService,
    protected placeholderService: PlaceholderService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tALeaseRepaymentRule }) => {
      this.updateForm(tALeaseRepaymentRule);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const tALeaseRepaymentRule = this.createFromForm();
    if (tALeaseRepaymentRule.id !== undefined) {
      this.subscribeToSaveResponse(this.tALeaseRepaymentRuleService.update(tALeaseRepaymentRule));
    } else {
      this.subscribeToSaveResponse(this.tALeaseRepaymentRuleService.create(tALeaseRepaymentRule));
    }
  }

  trackIFRS16LeaseContractById(index: number, item: IIFRS16LeaseContract): number {
    return item.id!;
  }

  trackTransactionAccountById(index: number, item: ITransactionAccount): number {
    return item.id!;
  }

  trackPlaceholderById(index: number, item: IPlaceholder): number {
    return item.id!;
  }

  getSelectedPlaceholder(option: IPlaceholder, selectedVals?: IPlaceholder[]): IPlaceholder {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITALeaseRepaymentRule>>): void {
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

  protected updateForm(tALeaseRepaymentRule: ITALeaseRepaymentRule): void {
    this.editForm.patchValue({
      id: tALeaseRepaymentRule.id,
      name: tALeaseRepaymentRule.name,
      identifier: tALeaseRepaymentRule.identifier,
      leaseContract: tALeaseRepaymentRule.leaseContract,
      debit: tALeaseRepaymentRule.debit,
      credit: tALeaseRepaymentRule.credit,
      placeholders: tALeaseRepaymentRule.placeholders,
    });

    this.leaseContractsCollection = this.iFRS16LeaseContractService.addIFRS16LeaseContractToCollectionIfMissing(
      this.leaseContractsCollection,
      tALeaseRepaymentRule.leaseContract
    );
    this.transactionAccountsSharedCollection = this.transactionAccountService.addTransactionAccountToCollectionIfMissing(
      this.transactionAccountsSharedCollection,
      tALeaseRepaymentRule.debit,
      tALeaseRepaymentRule.credit
    );
    this.placeholdersSharedCollection = this.placeholderService.addPlaceholderToCollectionIfMissing(
      this.placeholdersSharedCollection,
      ...(tALeaseRepaymentRule.placeholders ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.iFRS16LeaseContractService
      .query({ 'tALeaseRepaymentRuleId.specified': 'false' })
      .pipe(map((res: HttpResponse<IIFRS16LeaseContract[]>) => res.body ?? []))
      .pipe(
        map((iFRS16LeaseContracts: IIFRS16LeaseContract[]) =>
          this.iFRS16LeaseContractService.addIFRS16LeaseContractToCollectionIfMissing(
            iFRS16LeaseContracts,
            this.editForm.get('leaseContract')!.value
          )
        )
      )
      .subscribe((iFRS16LeaseContracts: IIFRS16LeaseContract[]) => (this.leaseContractsCollection = iFRS16LeaseContracts));

    this.transactionAccountService
      .query()
      .pipe(map((res: HttpResponse<ITransactionAccount[]>) => res.body ?? []))
      .pipe(
        map((transactionAccounts: ITransactionAccount[]) =>
          this.transactionAccountService.addTransactionAccountToCollectionIfMissing(
            transactionAccounts,
            this.editForm.get('debit')!.value,
            this.editForm.get('credit')!.value
          )
        )
      )
      .subscribe((transactionAccounts: ITransactionAccount[]) => (this.transactionAccountsSharedCollection = transactionAccounts));

    this.placeholderService
      .query()
      .pipe(map((res: HttpResponse<IPlaceholder[]>) => res.body ?? []))
      .pipe(
        map((placeholders: IPlaceholder[]) =>
          this.placeholderService.addPlaceholderToCollectionIfMissing(placeholders, ...(this.editForm.get('placeholders')!.value ?? []))
        )
      )
      .subscribe((placeholders: IPlaceholder[]) => (this.placeholdersSharedCollection = placeholders));
  }

  protected createFromForm(): ITALeaseRepaymentRule {
    return {
      ...new TALeaseRepaymentRule(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      identifier: this.editForm.get(['identifier'])!.value,
      leaseContract: this.editForm.get(['leaseContract'])!.value,
      debit: this.editForm.get(['debit'])!.value,
      credit: this.editForm.get(['credit'])!.value,
      placeholders: this.editForm.get(['placeholders'])!.value,
    };
  }
}
