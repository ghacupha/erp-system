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
import { finalize } from 'rxjs/operators';

import { IBankTransactionType, BankTransactionType } from '../bank-transaction-type.model';
import { BankTransactionTypeService } from '../service/bank-transaction-type.service';

@Component({
  selector: 'jhi-bank-transaction-type-update',
  templateUrl: './bank-transaction-type-update.component.html',
})
export class BankTransactionTypeUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    transactionTypeCode: [null, [Validators.required]],
    transactionTypeDetails: [null, [Validators.required]],
  });

  constructor(
    protected bankTransactionTypeService: BankTransactionTypeService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ bankTransactionType }) => {
      this.updateForm(bankTransactionType);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const bankTransactionType = this.createFromForm();
    if (bankTransactionType.id !== undefined) {
      this.subscribeToSaveResponse(this.bankTransactionTypeService.update(bankTransactionType));
    } else {
      this.subscribeToSaveResponse(this.bankTransactionTypeService.create(bankTransactionType));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBankTransactionType>>): void {
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

  protected updateForm(bankTransactionType: IBankTransactionType): void {
    this.editForm.patchValue({
      id: bankTransactionType.id,
      transactionTypeCode: bankTransactionType.transactionTypeCode,
      transactionTypeDetails: bankTransactionType.transactionTypeDetails,
    });
  }

  protected createFromForm(): IBankTransactionType {
    return {
      ...new BankTransactionType(),
      id: this.editForm.get(['id'])!.value,
      transactionTypeCode: this.editForm.get(['transactionTypeCode'])!.value,
      transactionTypeDetails: this.editForm.get(['transactionTypeDetails'])!.value,
    };
  }
}
