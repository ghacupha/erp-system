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

import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormArray, FormBuilder, Validators } from '@angular/forms';
import { HttpResponse } from '@angular/common/http';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import { Store } from '@ngrx/store';
import { uuidv7 } from 'uuidv7';

import { TransactionAccountPostingRuleService } from '../transaction-account-posting-rule/service/transaction-account-posting-rule.service';
import {
  ITransactionAccountPostingRule,
  ITransactionAccountPostingRuleCondition,
  ITransactionAccountPostingRuleTemplate,
  TransactionAccountPostingRule,
} from '../transaction-account-posting-rule/transaction-account-posting-rule.model';
import { IIFRS16LeaseContract } from '../../erp-leases/ifrs-16-lease-contract/ifrs-16-lease-contract.model';
import { IFRS16LeaseContractService } from '../../erp-leases/ifrs-16-lease-contract/service/ifrs-16-lease-contract.service';
import { ITransactionAccount } from '../transaction-account/transaction-account.model';
import { TransactionAccountService } from '../transaction-account/service/transaction-account.service';
import { ITransactionAccountCategory } from 'app/entities/accounting/transaction-account-category/transaction-account-category.model';
import { IPlaceholder } from '../../erp-pages/placeholder/placeholder.model';
import { PlaceholderService } from '../../erp-pages/placeholder/service/placeholder.service';
import { State } from '../../store/global-store.definition';
import {
  leasePostingRuleFormUpdated,
  leasePostingRuleLeaseContractSelected,
  leasePostingRuleResetDraft,
} from '../../store/actions/lease-posting-rule-config.actions';
import { selectLeasePostingRuleSuggestions } from '../../store/selectors/lease-posting-rule-config.selectors';

@Component({
  selector: 'jhi-lease-posting-rule-config',
  templateUrl: './lease-posting-rule-config.component.html',
})
export class LeasePostingRuleConfigComponent implements OnInit, OnDestroy {
  isSaving = false;
  leaseContractsCollection: IIFRS16LeaseContract[] = [];
  transactionAccountsSharedCollection: ITransactionAccount[] = [];
  placeholdersSharedCollection: IPlaceholder[] = [];

  eventTypes = [
    { value: 'LEASE_LIABILITY_RECOGNITION', label: 'Lease Liability Recognition' },
    { value: 'LEASE_REPAYMENT', label: 'Lease Repayment' },
    { value: 'LEASE_INTEREST_ACCRUAL', label: 'Lease Interest Accrual' },
    { value: 'LEASE_INTEREST_PAID_TRANSFER', label: 'Lease Interest Paid Transfer' },
    { value: 'LEASE_ROU_RECOGNITION', label: 'ROU Recognition' },
    { value: 'LEASE_ROU_AMORTIZATION', label: 'ROU Amortization' },
  ];

  conditionOperators = ['EQUALS', 'NOT_EQUALS', 'CONTAINS'];

  editForm = this.fb.group({
    name: [null, [Validators.required]],
    identifier: [uuidv7(), [Validators.required]],
    module: [{ value: 'LEASE', disabled: true }, [Validators.required]],
    eventType: [null, [Validators.required]],
    leaseContract: [null, [Validators.required]],
    debitAccountType: [null, [Validators.required]],
    creditAccountType: [null, [Validators.required]],
    transactionContext: [],
    varianceType: [],
    invoiceTiming: [],
    postingRuleTemplates: this.fb.array([]),
    postingRuleConditions: this.fb.array([]),
  });

  private readonly destroy$ = new Subject<void>();

  constructor(
    protected fb: FormBuilder,
    protected postingRuleService: TransactionAccountPostingRuleService,
    protected leaseContractService: IFRS16LeaseContractService,
    protected transactionAccountService: TransactionAccountService,
    protected placeholderService: PlaceholderService,
    protected store: Store<State>
  ) {}

  ngOnInit(): void {
    this.addTemplate();
    this.addCondition();
    this.loadRelationshipsOptions();
    this.registerLeaseContractListener();

    this.store
      .select(selectLeasePostingRuleSuggestions)
      .pipe(takeUntil(this.destroy$))
      .subscribe(suggestions => {
        if (!suggestions) {
          return;
        }
        this.applyAccountSuggestions(suggestions.debitAccount ?? undefined, suggestions.creditAccount ?? undefined);
        this.applyAccountTypeSuggestions(suggestions.debitAccountType ?? undefined, suggestions.creditAccountType ?? undefined);
      });

    this.editForm.valueChanges.pipe(takeUntil(this.destroy$)).subscribe(() => {
      this.store.dispatch(leasePostingRuleFormUpdated({ draft: this.createFromForm() }));
    });
  }

  ngOnDestroy(): void {
    this.store.dispatch(leasePostingRuleResetDraft());
    this.destroy$.next();
    this.destroy$.complete();
  }

  get postingRuleTemplates(): FormArray {
    return this.editForm.get('postingRuleTemplates') as FormArray;
  }

  get postingRuleConditions(): FormArray {
    return this.editForm.get('postingRuleConditions') as FormArray;
  }

  addTemplate(): void {
    const templateGroup = this.fb.group({
      lineDescription: [],
      amountMultiplier: [],
      debitAccount: [null, Validators.required],
      creditAccount: [null, Validators.required],
    });
    this.postingRuleTemplates.push(templateGroup);
  }

  removeTemplate(index: number): void {
    this.postingRuleTemplates.removeAt(index);
  }

  addCondition(): void {
    const conditionGroup = this.fb.group({
      conditionKey: ['leaseContractId', Validators.required],
      conditionOperator: ['EQUALS', Validators.required],
      conditionValue: [null, Validators.required],
    });
    this.postingRuleConditions.push(conditionGroup);
  }

  removeCondition(index: number): void {
    this.postingRuleConditions.removeAt(index);
  }

  save(): void {
    this.isSaving = true;
    this.postingRuleService.create(this.createFromForm()).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  previousState(): void {
    window.history.back();
  }

  trackLeaseContractById(index: number, item: IIFRS16LeaseContract): number {
    return item.id!;
  }

  trackTransactionAccountById(index: number, item: ITransactionAccount): number {
    return item.id!;
  }

  trackAccountCategoryById(index: number, item: ITransactionAccountCategory): number {
    return item.id!;
  }

  trackPlaceholderById(index: number, item: IPlaceholder): number {
    return item.id!;
  }

  updateLeaseContract(leaseContract: IIFRS16LeaseContract): void {
    this.editForm.patchValue({ leaseContract });
  }

  updateDebitAccountType(accountType: ITransactionAccountCategory): void {
    this.editForm.patchValue({ debitAccountType: accountType });
  }

  updateCreditAccountType(accountType: ITransactionAccountCategory): void {
    this.editForm.patchValue({ creditAccountType: accountType });
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  protected createFromForm(): ITransactionAccountPostingRule {
    return {
      ...new TransactionAccountPostingRule(),
      name: this.editForm.get(['name'])!.value,
      identifier: this.editForm.get(['identifier'])!.value,
      module: 'LEASE',
      eventType: this.editForm.get(['eventType'])!.value,
      debitAccountType: this.editForm.get(['debitAccountType'])!.value,
      creditAccountType: this.editForm.get(['creditAccountType'])!.value,
      transactionContext: this.editForm.get(['transactionContext'])!.value,
      varianceType: this.editForm.get(['varianceType'])!.value,
      invoiceTiming: this.editForm.get(['invoiceTiming'])!.value,
      postingRuleTemplates: this.postingRuleTemplates.value as ITransactionAccountPostingRuleTemplate[],
      postingRuleConditions: this.postingRuleConditions.value as ITransactionAccountPostingRuleCondition[],
    };
  }

  protected loadRelationshipsOptions(): void {
    this.leaseContractService
      .query()
      .subscribe((res: HttpResponse<IIFRS16LeaseContract[]>) => (this.leaseContractsCollection = res.body ?? []));

    this.transactionAccountService
      .query()
      .subscribe((res: HttpResponse<ITransactionAccount[]>) => (this.transactionAccountsSharedCollection = res.body ?? []));

    this.placeholderService
      .query()
      .subscribe((res: HttpResponse<IPlaceholder[]>) => (this.placeholdersSharedCollection = res.body ?? []));
  }

  protected registerLeaseContractListener(): void {
    this.editForm.get('leaseContract')?.valueChanges.pipe(takeUntil(this.destroy$)).subscribe(selectedLease => {
      if (!selectedLease?.id) {
        return;
      }
      this.store.dispatch(leasePostingRuleLeaseContractSelected({ leaseContract: selectedLease }));
      this.applyLeaseContractCondition(selectedLease);
    });
  }

  protected applyLeaseContractCondition(leaseContract: IIFRS16LeaseContract): void {
    const conditionIndex = this.postingRuleConditions.controls.findIndex(control => control.get('conditionKey')?.value === 'leaseContractId');
    const conditionGroup = conditionIndex >= 0 ? this.postingRuleConditions.at(conditionIndex) : null;
    if (conditionGroup) {
      conditionGroup.patchValue({ conditionValue: leaseContract.id?.toString() });
    } else {
      this.addCondition();
      this.postingRuleConditions.at(this.postingRuleConditions.length - 1).patchValue({
        conditionKey: 'leaseContractId',
        conditionOperator: 'EQUALS',
        conditionValue: leaseContract.id?.toString(),
      });
    }
  }

  protected applyAccountSuggestions(debitAccount?: ITransactionAccount, creditAccount?: ITransactionAccount): void {
    if (this.postingRuleTemplates.length === 0) {
      this.addTemplate();
    }
    const firstTemplate = this.postingRuleTemplates.at(0);
    if (debitAccount) {
      firstTemplate.patchValue({ debitAccount });
    }
    if (creditAccount) {
      firstTemplate.patchValue({ creditAccount });
    }
  }

  protected applyAccountTypeSuggestions(
    debitAccountType?: ITransactionAccountCategory,
    creditAccountType?: ITransactionAccountCategory
  ): void {
    if (debitAccountType) {
      this.editForm.patchValue({ debitAccountType });
    }
    if (creditAccountType) {
      this.editForm.patchValue({ creditAccountType });
    }
  }
}
