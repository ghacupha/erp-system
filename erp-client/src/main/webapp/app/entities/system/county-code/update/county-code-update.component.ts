import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ICountyCode, CountyCode } from '../county-code.model';
import { CountyCodeService } from '../service/county-code.service';
import { IPlaceholder } from 'app/entities/system/placeholder/placeholder.model';
import { PlaceholderService } from 'app/entities/system/placeholder/service/placeholder.service';

@Component({
  selector: 'jhi-county-code-update',
  templateUrl: './county-code-update.component.html',
})
export class CountyCodeUpdateComponent implements OnInit {
  isSaving = false;

  placeholdersSharedCollection: IPlaceholder[] = [];

  editForm = this.fb.group({
    id: [],
    countyCode: [null, [Validators.required]],
    countyName: [null, [Validators.required]],
    subCountyCode: [null, [Validators.required]],
    subCountyName: [null, [Validators.required]],
    placeholders: [],
  });

  constructor(
    protected countyCodeService: CountyCodeService,
    protected placeholderService: PlaceholderService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ countyCode }) => {
      this.updateForm(countyCode);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const countyCode = this.createFromForm();
    if (countyCode.id !== undefined) {
      this.subscribeToSaveResponse(this.countyCodeService.update(countyCode));
    } else {
      this.subscribeToSaveResponse(this.countyCodeService.create(countyCode));
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

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICountyCode>>): void {
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

  protected updateForm(countyCode: ICountyCode): void {
    this.editForm.patchValue({
      id: countyCode.id,
      countyCode: countyCode.countyCode,
      countyName: countyCode.countyName,
      subCountyCode: countyCode.subCountyCode,
      subCountyName: countyCode.subCountyName,
      placeholders: countyCode.placeholders,
    });

    this.placeholdersSharedCollection = this.placeholderService.addPlaceholderToCollectionIfMissing(
      this.placeholdersSharedCollection,
      ...(countyCode.placeholders ?? [])
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

  protected createFromForm(): ICountyCode {
    return {
      ...new CountyCode(),
      id: this.editForm.get(['id'])!.value,
      countyCode: this.editForm.get(['countyCode'])!.value,
      countyName: this.editForm.get(['countyName'])!.value,
      subCountyCode: this.editForm.get(['subCountyCode'])!.value,
      subCountyName: this.editForm.get(['subCountyName'])!.value,
      placeholders: this.editForm.get(['placeholders'])!.value,
    };
  }
}
