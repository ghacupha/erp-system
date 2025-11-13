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
import { finalize, map } from 'rxjs/operators';

import { ICustomerIDDocumentType, CustomerIDDocumentType } from '../customer-id-document-type.model';
import { CustomerIDDocumentTypeService } from '../service/customer-id-document-type.service';
import { IPlaceholder } from 'app/entities/system/placeholder/placeholder.model';
import { PlaceholderService } from 'app/entities/system/placeholder/service/placeholder.service';

@Component({
  selector: 'jhi-customer-id-document-type-update',
  templateUrl: './customer-id-document-type-update.component.html',
})
export class CustomerIDDocumentTypeUpdateComponent implements OnInit {
  isSaving = false;

  placeholdersSharedCollection: IPlaceholder[] = [];

  editForm = this.fb.group({
    id: [],
    documentCode: [null, [Validators.required]],
    documentType: [null, [Validators.required]],
    documentTypeDescription: [],
    placeholders: [],
  });

  constructor(
    protected customerIDDocumentTypeService: CustomerIDDocumentTypeService,
    protected placeholderService: PlaceholderService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ customerIDDocumentType }) => {
      this.updateForm(customerIDDocumentType);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const customerIDDocumentType = this.createFromForm();
    if (customerIDDocumentType.id !== undefined) {
      this.subscribeToSaveResponse(this.customerIDDocumentTypeService.update(customerIDDocumentType));
    } else {
      this.subscribeToSaveResponse(this.customerIDDocumentTypeService.create(customerIDDocumentType));
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

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICustomerIDDocumentType>>): void {
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

  protected updateForm(customerIDDocumentType: ICustomerIDDocumentType): void {
    this.editForm.patchValue({
      id: customerIDDocumentType.id,
      documentCode: customerIDDocumentType.documentCode,
      documentType: customerIDDocumentType.documentType,
      documentTypeDescription: customerIDDocumentType.documentTypeDescription,
      placeholders: customerIDDocumentType.placeholders,
    });

    this.placeholdersSharedCollection = this.placeholderService.addPlaceholderToCollectionIfMissing(
      this.placeholdersSharedCollection,
      ...(customerIDDocumentType.placeholders ?? [])
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

  protected createFromForm(): ICustomerIDDocumentType {
    return {
      ...new CustomerIDDocumentType(),
      id: this.editForm.get(['id'])!.value,
      documentCode: this.editForm.get(['documentCode'])!.value,
      documentType: this.editForm.get(['documentType'])!.value,
      documentTypeDescription: this.editForm.get(['documentTypeDescription'])!.value,
      placeholders: this.editForm.get(['placeholders'])!.value,
    };
  }
}
