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
import { Observable, of } from 'rxjs';
import { finalize, map, switchMap } from 'rxjs/operators';

import { ITALeaseRecognitionRule, TALeaseRecognitionRule } from '../ta-lease-recognition-rule.model';
import { TALeaseRecognitionRuleService } from '../service/ta-lease-recognition-rule.service';
import { IPlaceholder } from '../../../erp-pages/placeholder/placeholder.model';
import { ITransactionAccount } from '../../transaction-account/transaction-account.model';
import { IFRS16LeaseContractService } from '../../../erp-leases/ifrs-16-lease-contract/service/ifrs-16-lease-contract.service';
import { IIFRS16LeaseContract } from '../../../erp-leases/ifrs-16-lease-contract/ifrs-16-lease-contract.model';
import { TransactionAccountService } from '../../transaction-account/service/transaction-account.service';
import { PlaceholderService } from '../../../erp-pages/placeholder/service/placeholder.service';
import { ILeaseTemplate } from '../../../erp-leases/lease-template/lease-template.model';
import { LeaseTemplateService } from '../../../erp-leases/lease-template/service/lease-template.service';
import { uuidv7 } from 'uuidv7';
import { select, Store } from '@ngrx/store';
import { State } from '../../../store/global-store.definition';
import {
  copyingTALeaseRecognitionRuleStatus,
  creatingTALeaseRecognitionRuleStatus,
  editingTALeaseRecognitionRuleStatus,
  taLeaseRecognitionRuleSelectedInstance
} from '../../../store/selectors/ta-lease-recognition-rule-status.selectors';

@Component({
  selector: 'jhi-ta-lease-recognition-rule-update',
  templateUrl: './ta-lease-recognition-rule-update.component.html'
})
export class TALeaseRecognitionRuleUpdateComponent implements OnInit {
  isSaving = false;

  weAreCopying = false;
  weAreEditing = false;
  weAreCreating = false;
  selectedItem = { ...new TALeaseRecognitionRule() };

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
    placeholders: []
  });

  constructor(
    protected tALeaseRecognitionRuleService: TALeaseRecognitionRuleService,
    protected iFRS16LeaseContractService: IFRS16LeaseContractService,
    protected transactionAccountService: TransactionAccountService,
    protected placeholderService: PlaceholderService,
    protected leaseTemplateService: LeaseTemplateService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder,
    protected store: Store<State>,
  ) {
    this.store.pipe(select(copyingTALeaseRecognitionRuleStatus)).subscribe(status => (this.weAreCopying = status));
    this.store.pipe(select(editingTALeaseRecognitionRuleStatus)).subscribe(status => (this.weAreEditing = status));
    this.store.pipe(select(creatingTALeaseRecognitionRuleStatus)).subscribe(status => (this.weAreCreating = status));
    this.store.pipe(select(taLeaseRecognitionRuleSelectedInstance)).subscribe(selected => (this.selectedItem = selected));
  }

  ngOnInit(): void {
    if (this.weAreEditing) {
      this.updateForm(this.selectedItem);
      this.loadRelationshipsOptions();
      this.registerLeaseContractSelectionListener();
    }

    if (this.weAreCopying) {
      this.updateForm(this.selectedItem);
      this.editForm.patchValue({
        identifier: uuidv7(),
      });
      this.loadRelationshipsOptions();
      this.registerLeaseContractSelectionListener();
    }

    if (this.weAreCreating) {
      this.editForm.patchValue({
        identifier: uuidv7(),
      });
      this.loadRelationshipsOptions();
      this.registerLeaseContractSelectionListener();
    }

    if (!this.weAreCopying && !this.weAreEditing && !this.weAreCreating) {
      this.activatedRoute.data.subscribe(({ tALeaseRecognitionRule }) => {
      if (tALeaseRecognitionRule.id) {
        this.updateForm(tALeaseRecognitionRule);
        this.registerLeaseContractSelectionListener();
      }

      if (!tALeaseRecognitionRule.id) {
        this.editForm.patchValue({
          identifier: uuidv7(),
        });
        this.registerLeaseContractSelectionListener();
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

  save(): void {
    this.isSaving = true;
    this.subscribeToSaveResponse(this.tALeaseRecognitionRuleService.create(this.createFromForm()));
  }

  edit(): void {
    this.isSaving = true;
    this.subscribeToSaveResponse(this.tALeaseRecognitionRuleService.update(this.createFromForm()));
  }

  copy(): void {
    this.isSaving = true;
    this.subscribeToSaveResponse(this.tALeaseRecognitionRuleService.create(this.copyFromForm()));
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

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITALeaseRecognitionRule>>): void {
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

  protected updateForm(tALeaseRecognitionRule: ITALeaseRecognitionRule): void {
    this.editForm.patchValue({
      id: tALeaseRecognitionRule.id,
      name: tALeaseRecognitionRule.name,
      identifier: tALeaseRecognitionRule.identifier,
      leaseContract: tALeaseRecognitionRule.leaseContract,
      debit: tALeaseRecognitionRule.debit,
      credit: tALeaseRecognitionRule.credit,
      placeholders: tALeaseRecognitionRule.placeholders
    });

    this.leaseContractsCollection = this.iFRS16LeaseContractService.addIFRS16LeaseContractToCollectionIfMissing(
      this.leaseContractsCollection,
      tALeaseRecognitionRule.leaseContract
    );
    this.transactionAccountsSharedCollection = this.transactionAccountService.addTransactionAccountToCollectionIfMissing(
      this.transactionAccountsSharedCollection,
      tALeaseRecognitionRule.debit,
      tALeaseRecognitionRule.credit
    );
    this.placeholdersSharedCollection = this.placeholderService.addPlaceholderToCollectionIfMissing(
      this.placeholdersSharedCollection,
      ...(tALeaseRecognitionRule.placeholders ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.iFRS16LeaseContractService
      .query({ 'tALeaseRecognitionRuleId.specified': 'false' })
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

  protected registerLeaseContractSelectionListener(): void {
    this.editForm.get('leaseContract')?.valueChanges.subscribe(selectedLease => {
      if (!selectedLease?.id) {
        return;
      }

      this.iFRS16LeaseContractService
        .find(selectedLease.id)
        .pipe(
          switchMap(response => {
            const leaseTemplate = response.body?.leaseTemplate;
            const leaseTemplateId = leaseTemplate?.id;

            if (!leaseTemplateId) {
              return of(undefined);
            }

            return this.leaseTemplateService
              .find(leaseTemplateId)
              .pipe(map(templateResponse => templateResponse.body ?? (leaseTemplate as ILeaseTemplate)));
          })
        )
        .subscribe(leaseTemplate => {
          const debitAccount = leaseTemplate?.leaseRecognitionDebitAccount;
          const creditAccount = leaseTemplate?.leaseRecognitionCreditAccount;

          if (!debitAccount && !creditAccount) {
            return;
          }

          this.transactionAccountsSharedCollection = this.transactionAccountService.addTransactionAccountToCollectionIfMissing(
            this.transactionAccountsSharedCollection,
            debitAccount ?? undefined,
            creditAccount ?? undefined
          );

          this.editForm.patchValue({
            debit: debitAccount ?? this.editForm.get('debit')!.value,
            credit: creditAccount ?? this.editForm.get('credit')!.value
          });
        });
    });
  }

  protected createFromForm(): ITALeaseRecognitionRule {
    return {
      ...new TALeaseRecognitionRule(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      identifier: this.editForm.get(['identifier'])!.value,
      leaseContract: this.editForm.get(['leaseContract'])!.value,
      debit: this.editForm.get(['debit'])!.value,
      credit: this.editForm.get(['credit'])!.value,
      placeholders: this.editForm.get(['placeholders'])!.value
    };
  }

  protected copyFromForm(): ITALeaseRecognitionRule {
    return {
      ...new TALeaseRecognitionRule(),
      name: this.editForm.get(['name'])!.value,
      identifier: this.editForm.get(['identifier'])!.value,
      leaseContract: this.editForm.get(['leaseContract'])!.value,
      debit: this.editForm.get(['debit'])!.value,
      credit: this.editForm.get(['credit'])!.value,
      placeholders: this.editForm.get(['placeholders'])!.value
    };
  }
}
