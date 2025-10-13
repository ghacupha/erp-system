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

import { ITransactionAccountCategory, TransactionAccountCategory } from '../transaction-account-category.model';
import { TransactionAccountCategoryService } from '../service/transaction-account-category.service';
import { IPlaceholder } from 'app/entities/system/placeholder/placeholder.model';
import { PlaceholderService } from 'app/entities/system/placeholder/service/placeholder.service';
import { ITransactionAccountLedger } from 'app/entities/accounting/transaction-account-ledger/transaction-account-ledger.model';
import { TransactionAccountLedgerService } from 'app/entities/accounting/transaction-account-ledger/service/transaction-account-ledger.service';
import { transactionAccountPostingTypes } from 'app/entities/enumerations/transaction-account-posting-types.model';

@Component({
  selector: 'jhi-transaction-account-category-update',
  templateUrl: './transaction-account-category-update.component.html',
})
export class TransactionAccountCategoryUpdateComponent implements OnInit {
  isSaving = false;
  transactionAccountPostingTypesValues = Object.keys(transactionAccountPostingTypes);

  placeholdersSharedCollection: IPlaceholder[] = [];
  transactionAccountLedgersSharedCollection: ITransactionAccountLedger[] = [];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    transactionAccountPostingType: [null, [Validators.required]],
    placeholders: [],
    accountLedger: [null, Validators.required],
  });

  constructor(
    protected transactionAccountCategoryService: TransactionAccountCategoryService,
    protected placeholderService: PlaceholderService,
    protected transactionAccountLedgerService: TransactionAccountLedgerService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ transactionAccountCategory }) => {
      this.updateForm(transactionAccountCategory);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const transactionAccountCategory = this.createFromForm();
    if (transactionAccountCategory.id !== undefined) {
      this.subscribeToSaveResponse(this.transactionAccountCategoryService.update(transactionAccountCategory));
    } else {
      this.subscribeToSaveResponse(this.transactionAccountCategoryService.create(transactionAccountCategory));
    }
  }

  trackPlaceholderById(index: number, item: IPlaceholder): number {
    return item.id!;
  }

  trackTransactionAccountLedgerById(index: number, item: ITransactionAccountLedger): number {
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

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITransactionAccountCategory>>): void {
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

  protected updateForm(transactionAccountCategory: ITransactionAccountCategory): void {
    this.editForm.patchValue({
      id: transactionAccountCategory.id,
      name: transactionAccountCategory.name,
      transactionAccountPostingType: transactionAccountCategory.transactionAccountPostingType,
      placeholders: transactionAccountCategory.placeholders,
      accountLedger: transactionAccountCategory.accountLedger,
    });

    this.placeholdersSharedCollection = this.placeholderService.addPlaceholderToCollectionIfMissing(
      this.placeholdersSharedCollection,
      ...(transactionAccountCategory.placeholders ?? [])
    );
    this.transactionAccountLedgersSharedCollection = this.transactionAccountLedgerService.addTransactionAccountLedgerToCollectionIfMissing(
      this.transactionAccountLedgersSharedCollection,
      transactionAccountCategory.accountLedger
    );
  }

  protected loadRelationshipsOptions(): void {
    this.placeholderService
      .query()
      .pipe(map((res: HttpResponse<IPlaceholder[]>) => res.body ?? []))
      .pipe(
        map((placeholders: IPlaceholder[]) =>
          this.placeholderService.addPlaceholderToCollectionIfMissing(placeholders, ...(this.editForm.get('placeholders')!.value ?? []))
        )
      )
      .subscribe((placeholders: IPlaceholder[]) => (this.placeholdersSharedCollection = placeholders));

    this.transactionAccountLedgerService
      .query()
      .pipe(map((res: HttpResponse<ITransactionAccountLedger[]>) => res.body ?? []))
      .pipe(
        map((transactionAccountLedgers: ITransactionAccountLedger[]) =>
          this.transactionAccountLedgerService.addTransactionAccountLedgerToCollectionIfMissing(
            transactionAccountLedgers,
            this.editForm.get('accountLedger')!.value
          )
        )
      )
      .subscribe(
        (transactionAccountLedgers: ITransactionAccountLedger[]) =>
          (this.transactionAccountLedgersSharedCollection = transactionAccountLedgers)
      );
  }

  protected createFromForm(): ITransactionAccountCategory {
    return {
      ...new TransactionAccountCategory(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      transactionAccountPostingType: this.editForm.get(['transactionAccountPostingType'])!.value,
      placeholders: this.editForm.get(['placeholders'])!.value,
      accountLedger: this.editForm.get(['accountLedger'])!.value,
    };
  }
}
