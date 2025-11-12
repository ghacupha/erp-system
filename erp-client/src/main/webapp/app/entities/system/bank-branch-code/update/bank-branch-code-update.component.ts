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

import { IBankBranchCode, BankBranchCode } from '../bank-branch-code.model';
import { BankBranchCodeService } from '../service/bank-branch-code.service';
import { IPlaceholder } from 'app/entities/system/placeholder/placeholder.model';
import { PlaceholderService } from 'app/entities/system/placeholder/service/placeholder.service';

@Component({
  selector: 'jhi-bank-branch-code-update',
  templateUrl: './bank-branch-code-update.component.html',
})
export class BankBranchCodeUpdateComponent implements OnInit {
  isSaving = false;

  placeholdersSharedCollection: IPlaceholder[] = [];

  editForm = this.fb.group({
    id: [],
    bankCode: [],
    bankName: [null, [Validators.required]],
    branchCode: [null, [Validators.required]],
    branchName: [],
    notes: [],
    placeholders: [],
  });

  constructor(
    protected bankBranchCodeService: BankBranchCodeService,
    protected placeholderService: PlaceholderService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ bankBranchCode }) => {
      this.updateForm(bankBranchCode);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const bankBranchCode = this.createFromForm();
    if (bankBranchCode.id !== undefined) {
      this.subscribeToSaveResponse(this.bankBranchCodeService.update(bankBranchCode));
    } else {
      this.subscribeToSaveResponse(this.bankBranchCodeService.create(bankBranchCode));
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

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBankBranchCode>>): void {
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

  protected updateForm(bankBranchCode: IBankBranchCode): void {
    this.editForm.patchValue({
      id: bankBranchCode.id,
      bankCode: bankBranchCode.bankCode,
      bankName: bankBranchCode.bankName,
      branchCode: bankBranchCode.branchCode,
      branchName: bankBranchCode.branchName,
      notes: bankBranchCode.notes,
      placeholders: bankBranchCode.placeholders,
    });

    this.placeholdersSharedCollection = this.placeholderService.addPlaceholderToCollectionIfMissing(
      this.placeholdersSharedCollection,
      ...(bankBranchCode.placeholders ?? [])
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

  protected createFromForm(): IBankBranchCode {
    return {
      ...new BankBranchCode(),
      id: this.editForm.get(['id'])!.value,
      bankCode: this.editForm.get(['bankCode'])!.value,
      bankName: this.editForm.get(['bankName'])!.value,
      branchCode: this.editForm.get(['branchCode'])!.value,
      branchName: this.editForm.get(['branchName'])!.value,
      notes: this.editForm.get(['notes'])!.value,
      placeholders: this.editForm.get(['placeholders'])!.value,
    };
  }
}
