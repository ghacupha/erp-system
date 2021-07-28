///
/// Copyright © 2021 Edwin Njeru (mailnjeru@gmail.com)
///
/// Licensed under the Apache License, Version 2.0 (the "License");
/// you may not use this file except in compliance with the License.
/// You may obtain a copy of the License at
///
///     http://www.apache.org/licenses/LICENSE-2.0
///
/// Unless required by applicable law or agreed to in writing, software
/// distributed under the License is distributed on an "AS IS" BASIS,
/// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
/// See the License for the specific language governing permissions and
/// limitations under the License.
///

import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { ITaxReference, TaxReference } from 'app/shared/model/payments/tax-reference.model';
import { TaxReferenceService } from './tax-reference.service';

@Component({
  selector: 'jhi-tax-reference-update',
  templateUrl: './tax-reference-update.component.html',
})
export class TaxReferenceUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    taxName: [null, []],
    taxDescription: [],
    taxPercentage: [null, [Validators.required]],
    taxReferenceType: [null, [Validators.required]],
  });

  constructor(protected taxReferenceService: TaxReferenceService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ taxReference }) => {
      this.updateForm(taxReference);
    });
  }

  updateForm(taxReference: ITaxReference): void {
    this.editForm.patchValue({
      id: taxReference.id,
      taxName: taxReference.taxName,
      taxDescription: taxReference.taxDescription,
      taxPercentage: taxReference.taxPercentage,
      taxReferenceType: taxReference.taxReferenceType,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const taxReference = this.createFromForm();
    if (taxReference.id !== undefined) {
      this.subscribeToSaveResponse(this.taxReferenceService.update(taxReference));
    } else {
      this.subscribeToSaveResponse(this.taxReferenceService.create(taxReference));
    }
  }

  private createFromForm(): ITaxReference {
    return {
      ...new TaxReference(),
      id: this.editForm.get(['id'])!.value,
      taxName: this.editForm.get(['taxName'])!.value,
      taxDescription: this.editForm.get(['taxDescription'])!.value,
      taxPercentage: this.editForm.get(['taxPercentage'])!.value,
      taxReferenceType: this.editForm.get(['taxReferenceType'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITaxReference>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }
}
