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

import { ITALeaseInterestAccrualRule, TALeaseInterestAccrualRule } from '../ta-lease-interest-accrual-rule.model';
import { TALeaseInterestAccrualRuleService } from '../service/ta-lease-interest-accrual-rule.service';
import { ITransactionAccount } from '../../transaction-account/transaction-account.model';
import { IPlaceholder } from '../../../erp-pages/placeholder/placeholder.model';
import { IFRS16LeaseContractService } from '../../../erp-leases/ifrs-16-lease-contract/service/ifrs-16-lease-contract.service';
import { IIFRS16LeaseContract } from '../../../erp-leases/ifrs-16-lease-contract/ifrs-16-lease-contract.model';
import { TransactionAccountService } from '../../transaction-account/service/transaction-account.service';
import { PlaceholderService } from '../../../erp-pages/placeholder/service/placeholder.service';
import { uuidv7 } from 'uuidv7';
import { select, Store } from '@ngrx/store';
import { State } from '../../../store/global-store.definition';
import {
  copyingTALeaseInterestAccrualRuleStatus,
  creatingTALeaseInterestAccrualRuleStatus,
  editingTALeaseInterestAccrualRuleStatus,
  taLeaseInterestAccrualRuleSelectedInstance
} from '../../../store/selectors/ta-lease-interest-accrual-rule-status.selectors';

@Component({
  selector: 'jhi-ta-lease-interest-accrual-rule-update',
  templateUrl: './ta-lease-interest-accrual-rule-update.component.html',
})
export class TALeaseInterestAccrualRuleUpdateComponent implements OnInit {
  isSaving = false;

  weAreCopying = false;
  weAreEditing = false;
  weAreCreating = false;
  selectedItem = { ...new TALeaseInterestAccrualRule() };

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
    protected tALeaseInterestAccrualRuleService: TALeaseInterestAccrualRuleService,
    protected iFRS16LeaseContractService: IFRS16LeaseContractService,
    protected transactionAccountService: TransactionAccountService,
    protected placeholderService: PlaceholderService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder,
    protected store: Store<State>,
  ) {
    this.store.pipe(select(copyingTALeaseInterestAccrualRuleStatus)).subscribe(status => (this.weAreCopying = status));
    this.store.pipe(select(editingTALeaseInterestAccrualRuleStatus)).subscribe(status => (this.weAreEditing = status));
    this.store.pipe(select(creatingTALeaseInterestAccrualRuleStatus)).subscribe(status => (this.weAreCreating = status));
    this.store.pipe(select(taLeaseInterestAccrualRuleSelectedInstance)).subscribe(selected => (this.selectedItem = selected));
  }

  ngOnInit(): void {
    if (this.weAreEditing) {
      this.updateForm(this.selectedItem);
      this.loadRelationshipsOptions();
    }

    if (this.weAreCopying) {
      this.updateForm(this.selectedItem);
      this.editForm.patchValue({
        identifier: uuidv7(),
      });
      this.loadRelationshipsOptions();
    }

    if (this.weAreCreating) {
      this.editForm.patchValue({
        identifier: uuidv7(),
      });
      this.loadRelationshipsOptions();
    }

    if (!this.weAreCopying && !this.weAreEditing && !this.weAreCreating) {
      this.activatedRoute.data.subscribe(({ tALeaseInterestAccrualRule }) => {
        if (tALeaseInterestAccrualRule.id) {
          this.updateForm(tALeaseInterestAccrualRule);
        }

        if (!tALeaseInterestAccrualRule.id) {
          this.editForm.patchValue({
            identifier: uuidv7(),
          });
        }

        this.loadRelationshipsOptions();
      });
    }
  }

  updateDebitAccount($event: ITransactionAccount): void {
    this.editForm.patchValue({
      debit: $event
    });
  }

  updateCreditAccount($event: ITransactionAccount): void {
    this.editForm.patchValue({
      credit: $event
    });
  }

  updateLeaseContract($event: IIFRS16LeaseContract): void {
    this.editForm.patchValue({
      leaseContract: $event
    })
  }

  updatePlaceholders(value: IPlaceholder[]): void {
    this.editForm.patchValue({
      placeholders: [...value]
    });
  }

  previousState(): void {
    window.history.back();
  }

  onSubmit(action?: 'save' | 'edit' | 'copy'): void {
    if (this.isSaving) {
      return;
    }

    if (action === 'edit') {
      this.edit();
      return;
    }

    if (action === 'copy') {
      this.copy();
      return;
    }

    if (action === 'save') {
      this.save();
      return;
    }

    if (this.weAreEditing) {
      this.edit();
    } else if (this.weAreCopying) {
      this.copy();
    } else {
      this.save();
    }
  }

  save(): void {
    this.isSaving = true;
    this.subscribeToSaveResponse(this.tALeaseInterestAccrualRuleService.create(this.createFromForm()));
  }

  edit(): void {
    this.isSaving = true;
    this.subscribeToSaveResponse(this.tALeaseInterestAccrualRuleService.update(this.createFromForm()));
  }

  copy(): void {
    this.isSaving = true;
    this.subscribeToSaveResponse(this.tALeaseInterestAccrualRuleService.create(this.copyFromForm()));
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

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITALeaseInterestAccrualRule>>): void {
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

  protected updateForm(tALeaseInterestAccrualRule: ITALeaseInterestAccrualRule): void {
    this.editForm.patchValue({
      id: tALeaseInterestAccrualRule.id,
      name: tALeaseInterestAccrualRule.name,
      identifier: tALeaseInterestAccrualRule.identifier,
      leaseContract: tALeaseInterestAccrualRule.leaseContract,
      debit: tALeaseInterestAccrualRule.debit,
      credit: tALeaseInterestAccrualRule.credit,
      placeholders: tALeaseInterestAccrualRule.placeholders,
    });

    this.leaseContractsCollection = this.iFRS16LeaseContractService.addIFRS16LeaseContractToCollectionIfMissing(
      this.leaseContractsCollection,
      tALeaseInterestAccrualRule.leaseContract
    );
    this.transactionAccountsSharedCollection = this.transactionAccountService.addTransactionAccountToCollectionIfMissing(
      this.transactionAccountsSharedCollection,
      tALeaseInterestAccrualRule.debit,
      tALeaseInterestAccrualRule.credit
    );
    this.placeholdersSharedCollection = this.placeholderService.addPlaceholderToCollectionIfMissing(
      this.placeholdersSharedCollection,
      ...(tALeaseInterestAccrualRule.placeholders ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.iFRS16LeaseContractService
      .query({ 'tALeaseInterestAccrualRuleId.specified': 'false' })
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

  protected createFromForm(): ITALeaseInterestAccrualRule {
    return {
      ...new TALeaseInterestAccrualRule(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      identifier: this.editForm.get(['identifier'])!.value,
      leaseContract: this.editForm.get(['leaseContract'])!.value,
      debit: this.editForm.get(['debit'])!.value,
      credit: this.editForm.get(['credit'])!.value,
      placeholders: this.editForm.get(['placeholders'])!.value,
    };
  }

  protected copyFromForm(): ITALeaseInterestAccrualRule {
    return {
      ...new TALeaseInterestAccrualRule(),
      name: this.editForm.get(['name'])!.value,
      identifier: this.editForm.get(['identifier'])!.value,
      leaseContract: this.editForm.get(['leaseContract'])!.value,
      debit: this.editForm.get(['debit'])!.value,
      credit: this.editForm.get(['credit'])!.value,
      placeholders: this.editForm.get(['placeholders'])!.value,
    };
  }
}
