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

import { IPlaceholder } from '../../../placeholder/placeholder.model';
import { PlaceholderService } from '../../../placeholder/service/placeholder.service';
import { TaxReferenceService } from '../service/tax-reference.service';
import { ITaxReference, TaxReference } from '../tax-reference.model';

@Component({
  selector: 'jhi-tax-reference-update',
  templateUrl: './tax-reference-update.component.html',
})
export class TaxReferenceUpdateComponent implements OnInit {
  isSaving = false;

  placeholdersSharedCollection: IPlaceholder[] = [];

  editForm = this.fb.group({
    id: [],
    taxName: [null, []],
    taxDescription: [],
    taxPercentage: [null, [Validators.required]],
    taxReferenceType: [null, [Validators.required]],
    placeholders: [],
  });

  constructor(
    protected taxReferenceService: TaxReferenceService,
    protected placeholderService: PlaceholderService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ taxReference }) => {
      this.updateForm(taxReference);

      this.loadRelationshipsOptions();
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

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITaxReference>>): void {
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

  protected updateForm(taxReference: ITaxReference): void {
    this.editForm.patchValue({
      id: taxReference.id,
      taxName: taxReference.taxName,
      taxDescription: taxReference.taxDescription,
      taxPercentage: taxReference.taxPercentage,
      taxReferenceType: taxReference.taxReferenceType,
      placeholders: taxReference.placeholders,
    });

    this.placeholdersSharedCollection = this.placeholderService.addPlaceholderToCollectionIfMissing(
      this.placeholdersSharedCollection,
      ...(taxReference.placeholders ?? [])
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

  protected createFromForm(): ITaxReference {
    return {
      ...new TaxReference(),
      id: this.editForm.get(['id'])!.value,
      taxName: this.editForm.get(['taxName'])!.value,
      taxDescription: this.editForm.get(['taxDescription'])!.value,
      taxPercentage: this.editForm.get(['taxPercentage'])!.value,
      taxReferenceType: this.editForm.get(['taxReferenceType'])!.value,
      placeholders: this.editForm.get(['placeholders'])!.value,
    };
  }
}
