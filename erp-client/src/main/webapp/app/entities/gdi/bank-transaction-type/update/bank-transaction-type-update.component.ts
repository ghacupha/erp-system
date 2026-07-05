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
