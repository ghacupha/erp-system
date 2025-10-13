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

import { ITransactionAccountLedger, TransactionAccountLedger } from '../transaction-account-ledger.model';
import { TransactionAccountLedgerService } from '../service/transaction-account-ledger.service';
import { IPlaceholder } from '../../../erp-pages/placeholder/placeholder.model';
import { PlaceholderService } from '../../../erp-pages/placeholder/service/placeholder.service';

@Component({
  selector: 'jhi-transaction-account-ledger-update',
  templateUrl: './transaction-account-ledger-update.component.html',
})
export class TransactionAccountLedgerUpdateComponent implements OnInit {
  isSaving = false;

  placeholdersSharedCollection: IPlaceholder[] = [];

  editForm = this.fb.group({
    id: [],
    ledgerCode: [null, [Validators.required]],
    ledgerName: [null, [Validators.required]],
    placeholders: [],
  });

  constructor(
    protected transactionAccountLedgerService: TransactionAccountLedgerService,
    protected placeholderService: PlaceholderService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ transactionAccountLedger }) => {
      this.updateForm(transactionAccountLedger);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const transactionAccountLedger = this.createFromForm();
    if (transactionAccountLedger.id !== undefined) {
      this.subscribeToSaveResponse(this.transactionAccountLedgerService.update(transactionAccountLedger));
    } else {
      this.subscribeToSaveResponse(this.transactionAccountLedgerService.create(transactionAccountLedger));
    }
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

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITransactionAccountLedger>>): void {
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

  protected updateForm(transactionAccountLedger: ITransactionAccountLedger): void {
    this.editForm.patchValue({
      id: transactionAccountLedger.id,
      ledgerCode: transactionAccountLedger.ledgerCode,
      ledgerName: transactionAccountLedger.ledgerName,
      placeholders: transactionAccountLedger.placeholders,
    });

    this.placeholdersSharedCollection = this.placeholderService.addPlaceholderToCollectionIfMissing(
      this.placeholdersSharedCollection,
      ...(transactionAccountLedger.placeholders ?? [])
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
  }

  protected createFromForm(): ITransactionAccountLedger {
    return {
      ...new TransactionAccountLedger(),
      id: this.editForm.get(['id'])!.value,
      ledgerCode: this.editForm.get(['ledgerCode'])!.value,
      ledgerName: this.editForm.get(['ledgerName'])!.value,
      placeholders: this.editForm.get(['placeholders'])!.value,
    };
  }
}
