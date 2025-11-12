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
import { concat, Observable, of, Subject } from 'rxjs';
import { catchError, debounceTime, distinctUntilChanged, filter, finalize, map, switchMap, tap } from 'rxjs/operators';

import { IAssetCategory, AssetCategory } from '../asset-category.model';
import { AssetCategoryService } from '../service/asset-category.service';
import { IDepreciationMethod } from '../../depreciation-method/depreciation-method.model';
import { DepreciationMethodService } from '../../depreciation-method/service/depreciation-method.service';
import { PlaceholderSuggestionService } from '../../../erp-common/suggestion/placeholder-suggestion.service';
import { DepreciationMethodSuggestionService } from '../../../erp-common/suggestion/depreciation-method-suggestion.service';
import { IPlaceholder } from '../../../erp-pages/placeholder/placeholder.model';
import { PlaceholderService } from '../../../erp-pages/placeholder/service/placeholder.service';
import { IPaymentLabel } from '../../../erp-pages/payment-label/payment-label.model';

@Component({
  selector: 'jhi-asset-category-update',
  templateUrl: './asset-category-update.component.html',
})
export class AssetCategoryUpdateComponent implements OnInit {
  isSaving = false;

  depreciationMethodsSharedCollection: IDepreciationMethod[] = [];
  placeholdersSharedCollection: IPlaceholder[] = [];

  editForm = this.fb.group({
    id: [],
    assetCategoryName: [null, [Validators.required]],
    description: [],
    notes: [],
    depreciationRateYearly: [],
    depreciationMethod: [null, Validators.required],
    placeholders: [],
  });

  minAccountLengthTerm = 3;

  placeholdersLoading = false;
  placeholderControlInput$ = new Subject<string>();
  placeholderLookups$: Observable<IPlaceholder[]> = of([]);

  depreciationMethodsLoading = false;
  depreciationMethodsControlInput$ = new Subject<string>();
  depreciationMethodLookups$: Observable<IDepreciationMethod[]> = of([]);

  constructor(
    protected assetCategoryService: AssetCategoryService,
    protected depreciationMethodService: DepreciationMethodService,
    protected placeholderService: PlaceholderService,
    protected activatedRoute: ActivatedRoute,
    protected placeholderSuggestionService: PlaceholderSuggestionService,
    protected depreciationMethodSuggestionService: DepreciationMethodSuggestionService,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ assetCategory }) => {
      this.updateForm(assetCategory);

      this.loadRelationshipsOptions();
      this.loadDepreciationMethods();
    });

    this.loadPlaceholders();
  }

  loadPlaceholders(): void {
    this.placeholderLookups$ = concat(
      of([]), // default items
      this.placeholderControlInput$.pipe(
        /* filter(res => res.length >= this.minAccountLengthTerm), */
        // eslint-disable-next-line @typescript-eslint/no-unnecessary-condition
        filter(res => res !== null),
        distinctUntilChanged(),
        debounceTime(800),
        tap(() => this.placeholdersLoading = true),
        switchMap(term => this.placeholderSuggestionService.search(term).pipe(
          catchError(() => of([])),
          tap(() => this.placeholdersLoading = false)
        ))
      ),
      of([...this.placeholdersSharedCollection])
    );
  }

  loadDepreciationMethods(): void {
    this.depreciationMethodLookups$ = concat(
      of([]), // default items
      this.depreciationMethodsControlInput$.pipe(
        /* filter(res => res.length >= this.minAccountLengthTerm), */
        // eslint-disable-next-line @typescript-eslint/no-unnecessary-condition
        filter(res => res !== null),
        distinctUntilChanged(),
        debounceTime(800),
        tap(() => this.depreciationMethodsLoading = true),
        switchMap(term => this.depreciationMethodSuggestionService.search(term).pipe(
          catchError(() => of([])),
          tap(() => this.depreciationMethodsLoading = false)
        ))
      ),
      of([...this.depreciationMethodsSharedCollection])
    );
  }

  trackPlaceholdersByFn(item: IPaymentLabel): number {
    return item.id!;
  }

  trackDepreciationMethodsByFn(item: IPaymentLabel): number {
    return item.id!;
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const assetCategory = this.createFromForm();
    if (assetCategory.id !== undefined) {
      this.subscribeToSaveResponse(this.assetCategoryService.update(assetCategory));
    } else {
      this.subscribeToSaveResponse(this.assetCategoryService.create(assetCategory));
    }
  }

  trackDepreciationMethodById(index: number, item: IDepreciationMethod): number {
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

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAssetCategory>>): void {
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

  protected updateForm(assetCategory: IAssetCategory): void {
    this.editForm.patchValue({
      id: assetCategory.id,
      assetCategoryName: assetCategory.assetCategoryName,
      description: assetCategory.description,
      notes: assetCategory.notes,
      depreciationRateYearly: assetCategory.depreciationRateYearly,
      depreciationMethod: assetCategory.depreciationMethod,
      placeholders: assetCategory.placeholders,
    });

    this.depreciationMethodsSharedCollection = this.depreciationMethodService.addDepreciationMethodToCollectionIfMissing(
      this.depreciationMethodsSharedCollection,
      assetCategory.depreciationMethod
    );
    this.placeholdersSharedCollection = this.placeholderService.addPlaceholderToCollectionIfMissing(
      this.placeholdersSharedCollection,
      ...(assetCategory.placeholders ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.depreciationMethodService
      .query()
      .pipe(map((res: HttpResponse<IDepreciationMethod[]>) => res.body ?? []))
      .pipe(
        map((depreciationMethods: IDepreciationMethod[]) =>
          this.depreciationMethodService.addDepreciationMethodToCollectionIfMissing(
            depreciationMethods,
            this.editForm.get('depreciationMethod')!.value
          )
        )
      )
      .subscribe((depreciationMethods: IDepreciationMethod[]) => (this.depreciationMethodsSharedCollection = depreciationMethods));

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

  protected createFromForm(): IAssetCategory {
    return {
      ...new AssetCategory(),
      id: this.editForm.get(['id'])!.value,
      assetCategoryName: this.editForm.get(['assetCategoryName'])!.value,
      description: this.editForm.get(['description'])!.value,
      notes: this.editForm.get(['notes'])!.value,
      depreciationRateYearly: this.editForm.get(['depreciationRateYearly'])!.value,
      depreciationMethod: this.editForm.get(['depreciationMethod'])!.value,
      placeholders: this.editForm.get(['placeholders'])!.value,
    };
  }
}
