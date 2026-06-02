import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ICrbAgingBands, CrbAgingBands } from '../crb-aging-bands.model';
import { CrbAgingBandsService } from '../service/crb-aging-bands.service';

@Component({
  selector: 'jhi-crb-aging-bands-update',
  templateUrl: './crb-aging-bands-update.component.html',
})
export class CrbAgingBandsUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    agingBandCategoryCode: [null, [Validators.required]],
    agingBandCategory: [null, [Validators.required]],
    agingBandCategoryDetails: [null, [Validators.required]],
  });

  constructor(protected crbAgingBandsService: CrbAgingBandsService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ crbAgingBands }) => {
      this.updateForm(crbAgingBands);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const crbAgingBands = this.createFromForm();
    if (crbAgingBands.id !== undefined) {
      this.subscribeToSaveResponse(this.crbAgingBandsService.update(crbAgingBands));
    } else {
      this.subscribeToSaveResponse(this.crbAgingBandsService.create(crbAgingBands));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICrbAgingBands>>): void {
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

  protected updateForm(crbAgingBands: ICrbAgingBands): void {
    this.editForm.patchValue({
      id: crbAgingBands.id,
      agingBandCategoryCode: crbAgingBands.agingBandCategoryCode,
      agingBandCategory: crbAgingBands.agingBandCategory,
      agingBandCategoryDetails: crbAgingBands.agingBandCategoryDetails,
    });
  }

  protected createFromForm(): ICrbAgingBands {
    return {
      ...new CrbAgingBands(),
      id: this.editForm.get(['id'])!.value,
      agingBandCategoryCode: this.editForm.get(['agingBandCategoryCode'])!.value,
      agingBandCategory: this.editForm.get(['agingBandCategory'])!.value,
      agingBandCategoryDetails: this.editForm.get(['agingBandCategoryDetails'])!.value,
    };
  }
}
