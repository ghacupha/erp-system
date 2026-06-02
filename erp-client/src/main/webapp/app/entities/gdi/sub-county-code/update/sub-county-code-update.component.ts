import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ISubCountyCode, SubCountyCode } from '../sub-county-code.model';
import { SubCountyCodeService } from '../service/sub-county-code.service';
import { IPlaceholder } from 'app/entities/system/placeholder/placeholder.model';
import { PlaceholderService } from 'app/entities/system/placeholder/service/placeholder.service';

@Component({
  selector: 'jhi-sub-county-code-update',
  templateUrl: './sub-county-code-update.component.html',
})
export class SubCountyCodeUpdateComponent implements OnInit {
  isSaving = false;

  placeholdersSharedCollection: IPlaceholder[] = [];

  editForm = this.fb.group({
    id: [],
    countyCode: [],
    countyName: [],
    subCountyCode: [],
    subCountyName: [],
    placeholders: [],
  });

  constructor(
    protected subCountyCodeService: SubCountyCodeService,
    protected placeholderService: PlaceholderService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ subCountyCode }) => {
      this.updateForm(subCountyCode);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const subCountyCode = this.createFromForm();
    if (subCountyCode.id !== undefined) {
      this.subscribeToSaveResponse(this.subCountyCodeService.update(subCountyCode));
    } else {
      this.subscribeToSaveResponse(this.subCountyCodeService.create(subCountyCode));
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

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISubCountyCode>>): void {
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

  protected updateForm(subCountyCode: ISubCountyCode): void {
    this.editForm.patchValue({
      id: subCountyCode.id,
      countyCode: subCountyCode.countyCode,
      countyName: subCountyCode.countyName,
      subCountyCode: subCountyCode.subCountyCode,
      subCountyName: subCountyCode.subCountyName,
      placeholders: subCountyCode.placeholders,
    });

    this.placeholdersSharedCollection = this.placeholderService.addPlaceholderToCollectionIfMissing(
      this.placeholdersSharedCollection,
      ...(subCountyCode.placeholders ?? [])
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

  protected createFromForm(): ISubCountyCode {
    return {
      ...new SubCountyCode(),
      id: this.editForm.get(['id'])!.value,
      countyCode: this.editForm.get(['countyCode'])!.value,
      countyName: this.editForm.get(['countyName'])!.value,
      subCountyCode: this.editForm.get(['subCountyCode'])!.value,
      subCountyName: this.editForm.get(['subCountyName'])!.value,
      placeholders: this.editForm.get(['placeholders'])!.value,
    };
  }
}
