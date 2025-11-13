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

import { ITAAmortizationRule, TAAmortizationRule } from '../ta-amortization-rule.model';
import { TAAmortizationRuleService } from '../service/ta-amortization-rule.service';
import {
  IFRS16LeaseContract,
  IIFRS16LeaseContract
} from '../../../erp-leases/ifrs-16-lease-contract/ifrs-16-lease-contract.model';
import { IPlaceholder } from '../../../erp-pages/placeholder/placeholder.model';
import { ITransactionAccount } from '../../transaction-account/transaction-account.model';
import { IFRS16LeaseContractService } from '../../../erp-leases/ifrs-16-lease-contract/service/ifrs-16-lease-contract.service';
import { TransactionAccountService } from '../../transaction-account/service/transaction-account.service';
import { PlaceholderService } from '../../../erp-pages/placeholder/service/placeholder.service';
import { uuidv7 } from 'uuidv7';
import { select, Store } from '@ngrx/store';
import {
  copyingTAAmortizationStatus,
  creatingTAAmortizationStatus,
  editingTAAmortizationStatus,
  taAmortizationUpdateSelectedInstance
} from '../../../store/selectors/ta-amortization-status.selectors';
import { State } from '../../../store/global-store.definition';

@Component({
  selector: 'jhi-ta-amortization-rule-update',
  templateUrl: './ta-amortization-rule-update.component.html',
})
export class TAAmortizationRuleUpdateComponent implements OnInit {
  isSaving = false;

  // Setting up default form states
  weAreCopying = false;
  weAreEditing = false;
  weAreCreating = false;
  selectedItem = {...new TAAmortizationRule()}

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
    protected tAAmortizationRuleService: TAAmortizationRuleService,
    protected iFRS16LeaseContractService: IFRS16LeaseContractService,
    protected transactionAccountService: TransactionAccountService,
    protected placeholderService: PlaceholderService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder,
    protected store: Store<State>,
  ) {
    this.store.pipe(select(copyingTAAmortizationStatus)).subscribe(stat => this.weAreCopying = stat);
    this.store.pipe(select(editingTAAmortizationStatus)).subscribe(stat => this.weAreEditing = stat);
    this.store.pipe(select(creatingTAAmortizationStatus)).subscribe(stat => this.weAreCreating = stat);
    this.store.pipe(select(taAmortizationUpdateSelectedInstance)).subscribe(copied => this.selectedItem = copied);
  }

  ngOnInit(): void {

    if (this.weAreEditing) {
      this.updateForm(this.selectedItem);
    }

    if (this.weAreCopying) {
      this.updateForm(this.selectedItem);

      this.editForm.patchValue({
        identifier: uuidv7(),
      })
    }

    if (this.weAreCreating) {

      this.editForm.patchValue({
        identifier: uuidv7(),
      })

      this.loadRelationshipsOptions();
    }
  }

  save(): void {
    this.isSaving = true;
    this.subscribeToSaveResponse(this.tAAmortizationRuleService.create(this.createFromForm()));
  }

  edit(): void {
    this.isSaving = true;
    this.subscribeToSaveResponse(this.tAAmortizationRuleService.update(this.createFromForm()));
  }

  copy(): void {
    this.isSaving = true;
    this.subscribeToSaveResponse(this.tAAmortizationRuleService.create(this.copyFromForm()));
  }

  updateDebitAccount(value: ITransactionAccount): void {
    this.editForm.patchValue({
      debit: value
    });
  }

  updateCreditAccount(value: ITransactionAccount): void {
    this.editForm.patchValue({
      credit: value
    });
  }

  updateLeaseContract(value: IFRS16LeaseContract): void {
    this.editForm.patchValue({
      leaseContract: value
    });
  }

  updatePlaceholders(value: IPlaceholder[]): void {
    this.editForm.patchValue({
      placeholders: [...value]
    });
  }

  previousState(): void {
    window.history.back();
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

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITAAmortizationRule>>): void {
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

  protected updateForm(tAAmortizationRule: ITAAmortizationRule): void {
    this.editForm.patchValue({
      id: tAAmortizationRule.id,
      name: tAAmortizationRule.name,
      identifier: tAAmortizationRule.identifier,
      leaseContract: tAAmortizationRule.leaseContract,
      debit: tAAmortizationRule.debit,
      credit: tAAmortizationRule.credit,
      placeholders: tAAmortizationRule.placeholders,
    });

    this.leaseContractsCollection = this.iFRS16LeaseContractService.addIFRS16LeaseContractToCollectionIfMissing(
      this.leaseContractsCollection,
      tAAmortizationRule.leaseContract
    );
    this.transactionAccountsSharedCollection = this.transactionAccountService.addTransactionAccountToCollectionIfMissing(
      this.transactionAccountsSharedCollection,
      tAAmortizationRule.debit,
      tAAmortizationRule.credit
    );
    this.placeholdersSharedCollection = this.placeholderService.addPlaceholderToCollectionIfMissing(
      this.placeholdersSharedCollection,
      ...(tAAmortizationRule.placeholders ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.iFRS16LeaseContractService
      .query({ 'tAAmortizationRuleId.specified': 'false' })
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

  protected createFromForm(): ITAAmortizationRule {
    return {
      ...new TAAmortizationRule(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      identifier: this.editForm.get(['identifier'])!.value,
      leaseContract: this.editForm.get(['leaseContract'])!.value,
      debit: this.editForm.get(['debit'])!.value,
      credit: this.editForm.get(['credit'])!.value,
      placeholders: this.editForm.get(['placeholders'])!.value,
    };
  }

  protected copyFromForm(): ITAAmortizationRule {
    return {
      ...new TAAmortizationRule(),
      name: this.editForm.get(['name'])!.value,
      identifier: this.editForm.get(['identifier'])!.value,
      leaseContract: this.editForm.get(['leaseContract'])!.value,
      debit: this.editForm.get(['debit'])!.value,
      credit: this.editForm.get(['credit'])!.value,
      placeholders: this.editForm.get(['placeholders'])!.value,
    };
  }
}
