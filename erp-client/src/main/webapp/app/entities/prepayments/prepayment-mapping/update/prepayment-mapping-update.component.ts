import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IPrepaymentMapping, PrepaymentMapping } from '../prepayment-mapping.model';
import { PrepaymentMappingService } from '../service/prepayment-mapping.service';
import { IPlaceholder } from 'app/entities/system/placeholder/placeholder.model';
import { PlaceholderService } from 'app/entities/system/placeholder/service/placeholder.service';

@Component({
  selector: 'jhi-prepayment-mapping-update',
  templateUrl: './prepayment-mapping-update.component.html',
})
export class PrepaymentMappingUpdateComponent implements OnInit {
  isSaving = false;

  placeholdersSharedCollection: IPlaceholder[] = [];

  editForm = this.fb.group({
    id: [],
    parameterKey: [null, [Validators.required]],
    parameterGuid: [null, [Validators.required]],
    parameter: [null, [Validators.required]],
    placeholders: [],
  });

  constructor(
    protected prepaymentMappingService: PrepaymentMappingService,
    protected placeholderService: PlaceholderService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ prepaymentMapping }) => {
      this.updateForm(prepaymentMapping);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const prepaymentMapping = this.createFromForm();
    if (prepaymentMapping.id !== undefined) {
      this.subscribeToSaveResponse(this.prepaymentMappingService.update(prepaymentMapping));
    } else {
      this.subscribeToSaveResponse(this.prepaymentMappingService.create(prepaymentMapping));
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

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPrepaymentMapping>>): void {
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

  protected updateForm(prepaymentMapping: IPrepaymentMapping): void {
    this.editForm.patchValue({
      id: prepaymentMapping.id,
      parameterKey: prepaymentMapping.parameterKey,
      parameterGuid: prepaymentMapping.parameterGuid,
      parameter: prepaymentMapping.parameter,
      placeholders: prepaymentMapping.placeholders,
    });

    this.placeholdersSharedCollection = this.placeholderService.addPlaceholderToCollectionIfMissing(
      this.placeholdersSharedCollection,
      ...(prepaymentMapping.placeholders ?? [])
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

  protected createFromForm(): IPrepaymentMapping {
    return {
      ...new PrepaymentMapping(),
      id: this.editForm.get(['id'])!.value,
      parameterKey: this.editForm.get(['parameterKey'])!.value,
      parameterGuid: this.editForm.get(['parameterGuid'])!.value,
      parameter: this.editForm.get(['parameter'])!.value,
      placeholders: this.editForm.get(['placeholders'])!.value,
    };
  }
}
