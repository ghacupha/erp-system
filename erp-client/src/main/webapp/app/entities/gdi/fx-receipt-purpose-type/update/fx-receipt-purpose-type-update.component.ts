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

import { IFxReceiptPurposeType, FxReceiptPurposeType } from '../fx-receipt-purpose-type.model';
import { FxReceiptPurposeTypeService } from '../service/fx-receipt-purpose-type.service';

@Component({
  selector: 'jhi-fx-receipt-purpose-type-update',
  templateUrl: './fx-receipt-purpose-type-update.component.html',
})
export class FxReceiptPurposeTypeUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    itemCode: [null, [Validators.required]],
    attribute1ReceiptPaymentPurposeCode: [],
    attribute1ReceiptPaymentPurposeType: [],
    attribute2ReceiptPaymentPurposeCode: [],
    attribute2ReceiptPaymentPurposeDescription: [],
    attribute3ReceiptPaymentPurposeCode: [],
    attribute3ReceiptPaymentPurposeDescription: [],
    attribute4ReceiptPaymentPurposeCode: [],
    attribute4ReceiptPaymentPurposeDescription: [],
    attribute5ReceiptPaymentPurposeCode: [],
    attribute5ReceiptPaymentPurposeDescription: [],
    lastChild: [],
  });

  constructor(
    protected fxReceiptPurposeTypeService: FxReceiptPurposeTypeService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ fxReceiptPurposeType }) => {
      this.updateForm(fxReceiptPurposeType);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const fxReceiptPurposeType = this.createFromForm();
    if (fxReceiptPurposeType.id !== undefined) {
      this.subscribeToSaveResponse(this.fxReceiptPurposeTypeService.update(fxReceiptPurposeType));
    } else {
      this.subscribeToSaveResponse(this.fxReceiptPurposeTypeService.create(fxReceiptPurposeType));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFxReceiptPurposeType>>): void {
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

  protected updateForm(fxReceiptPurposeType: IFxReceiptPurposeType): void {
    this.editForm.patchValue({
      id: fxReceiptPurposeType.id,
      itemCode: fxReceiptPurposeType.itemCode,
      attribute1ReceiptPaymentPurposeCode: fxReceiptPurposeType.attribute1ReceiptPaymentPurposeCode,
      attribute1ReceiptPaymentPurposeType: fxReceiptPurposeType.attribute1ReceiptPaymentPurposeType,
      attribute2ReceiptPaymentPurposeCode: fxReceiptPurposeType.attribute2ReceiptPaymentPurposeCode,
      attribute2ReceiptPaymentPurposeDescription: fxReceiptPurposeType.attribute2ReceiptPaymentPurposeDescription,
      attribute3ReceiptPaymentPurposeCode: fxReceiptPurposeType.attribute3ReceiptPaymentPurposeCode,
      attribute3ReceiptPaymentPurposeDescription: fxReceiptPurposeType.attribute3ReceiptPaymentPurposeDescription,
      attribute4ReceiptPaymentPurposeCode: fxReceiptPurposeType.attribute4ReceiptPaymentPurposeCode,
      attribute4ReceiptPaymentPurposeDescription: fxReceiptPurposeType.attribute4ReceiptPaymentPurposeDescription,
      attribute5ReceiptPaymentPurposeCode: fxReceiptPurposeType.attribute5ReceiptPaymentPurposeCode,
      attribute5ReceiptPaymentPurposeDescription: fxReceiptPurposeType.attribute5ReceiptPaymentPurposeDescription,
      lastChild: fxReceiptPurposeType.lastChild,
    });
  }

  protected createFromForm(): IFxReceiptPurposeType {
    return {
      ...new FxReceiptPurposeType(),
      id: this.editForm.get(['id'])!.value,
      itemCode: this.editForm.get(['itemCode'])!.value,
      attribute1ReceiptPaymentPurposeCode: this.editForm.get(['attribute1ReceiptPaymentPurposeCode'])!.value,
      attribute1ReceiptPaymentPurposeType: this.editForm.get(['attribute1ReceiptPaymentPurposeType'])!.value,
      attribute2ReceiptPaymentPurposeCode: this.editForm.get(['attribute2ReceiptPaymentPurposeCode'])!.value,
      attribute2ReceiptPaymentPurposeDescription: this.editForm.get(['attribute2ReceiptPaymentPurposeDescription'])!.value,
      attribute3ReceiptPaymentPurposeCode: this.editForm.get(['attribute3ReceiptPaymentPurposeCode'])!.value,
      attribute3ReceiptPaymentPurposeDescription: this.editForm.get(['attribute3ReceiptPaymentPurposeDescription'])!.value,
      attribute4ReceiptPaymentPurposeCode: this.editForm.get(['attribute4ReceiptPaymentPurposeCode'])!.value,
      attribute4ReceiptPaymentPurposeDescription: this.editForm.get(['attribute4ReceiptPaymentPurposeDescription'])!.value,
      attribute5ReceiptPaymentPurposeCode: this.editForm.get(['attribute5ReceiptPaymentPurposeCode'])!.value,
      attribute5ReceiptPaymentPurposeDescription: this.editForm.get(['attribute5ReceiptPaymentPurposeDescription'])!.value,
      lastChild: this.editForm.get(['lastChild'])!.value,
    };
  }
}
