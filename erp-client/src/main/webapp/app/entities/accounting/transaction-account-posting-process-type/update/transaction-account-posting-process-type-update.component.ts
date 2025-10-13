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

import {
  ITransactionAccountPostingProcessType,
  TransactionAccountPostingProcessType,
} from '../transaction-account-posting-process-type.model';
import { TransactionAccountPostingProcessTypeService } from '../service/transaction-account-posting-process-type.service';
import { ITransactionAccountCategory } from 'app/entities/accounting/transaction-account-category/transaction-account-category.model';
import { TransactionAccountCategoryService } from 'app/entities/accounting/transaction-account-category/service/transaction-account-category.service';
import { IPlaceholder } from 'app/entities/system/placeholder/placeholder.model';
import { PlaceholderService } from 'app/entities/system/placeholder/service/placeholder.service';

@Component({
  selector: 'jhi-transaction-account-posting-process-type-update',
  templateUrl: './transaction-account-posting-process-type-update.component.html',
})
export class TransactionAccountPostingProcessTypeUpdateComponent implements OnInit {
  isSaving = false;

  transactionAccountCategoriesSharedCollection: ITransactionAccountCategory[] = [];
  placeholdersSharedCollection: IPlaceholder[] = [];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    debitAccountType: [null, Validators.required],
    creditAccountType: [null, Validators.required],
    placeholders: [],
  });

  constructor(
    protected transactionAccountPostingProcessTypeService: TransactionAccountPostingProcessTypeService,
    protected transactionAccountCategoryService: TransactionAccountCategoryService,
    protected placeholderService: PlaceholderService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ transactionAccountPostingProcessType }) => {
      this.updateForm(transactionAccountPostingProcessType);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const transactionAccountPostingProcessType = this.createFromForm();
    if (transactionAccountPostingProcessType.id !== undefined) {
      this.subscribeToSaveResponse(this.transactionAccountPostingProcessTypeService.update(transactionAccountPostingProcessType));
    } else {
      this.subscribeToSaveResponse(this.transactionAccountPostingProcessTypeService.create(transactionAccountPostingProcessType));
    }
  }

  trackTransactionAccountCategoryById(index: number, item: ITransactionAccountCategory): number {
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

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITransactionAccountPostingProcessType>>): void {
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

  protected updateForm(transactionAccountPostingProcessType: ITransactionAccountPostingProcessType): void {
    this.editForm.patchValue({
      id: transactionAccountPostingProcessType.id,
      name: transactionAccountPostingProcessType.name,
      debitAccountType: transactionAccountPostingProcessType.debitAccountType,
      creditAccountType: transactionAccountPostingProcessType.creditAccountType,
      placeholders: transactionAccountPostingProcessType.placeholders,
    });

    this.transactionAccountCategoriesSharedCollection =
      this.transactionAccountCategoryService.addTransactionAccountCategoryToCollectionIfMissing(
        this.transactionAccountCategoriesSharedCollection,
        transactionAccountPostingProcessType.debitAccountType,
        transactionAccountPostingProcessType.creditAccountType
      );
    this.placeholdersSharedCollection = this.placeholderService.addPlaceholderToCollectionIfMissing(
      this.placeholdersSharedCollection,
      ...(transactionAccountPostingProcessType.placeholders ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.transactionAccountCategoryService
      .query()
      .pipe(map((res: HttpResponse<ITransactionAccountCategory[]>) => res.body ?? []))
      .pipe(
        map((transactionAccountCategories: ITransactionAccountCategory[]) =>
          this.transactionAccountCategoryService.addTransactionAccountCategoryToCollectionIfMissing(
            transactionAccountCategories,
            this.editForm.get('debitAccountType')!.value,
            this.editForm.get('creditAccountType')!.value
          )
        )
      )
      .subscribe(
        (transactionAccountCategories: ITransactionAccountCategory[]) =>
          (this.transactionAccountCategoriesSharedCollection = transactionAccountCategories)
      );

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

  protected createFromForm(): ITransactionAccountPostingProcessType {
    return {
      ...new TransactionAccountPostingProcessType(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      debitAccountType: this.editForm.get(['debitAccountType'])!.value,
      creditAccountType: this.editForm.get(['creditAccountType'])!.value,
      placeholders: this.editForm.get(['placeholders'])!.value,
    };
  }
}
