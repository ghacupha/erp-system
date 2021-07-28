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
