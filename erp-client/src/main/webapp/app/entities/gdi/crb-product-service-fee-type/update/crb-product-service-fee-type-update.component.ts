import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ICrbProductServiceFeeType, CrbProductServiceFeeType } from '../crb-product-service-fee-type.model';
import { CrbProductServiceFeeTypeService } from '../service/crb-product-service-fee-type.service';

@Component({
  selector: 'jhi-crb-product-service-fee-type-update',
  templateUrl: './crb-product-service-fee-type-update.component.html',
})
export class CrbProductServiceFeeTypeUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    chargeTypeCode: [null, [Validators.required]],
    chargeTypeDescription: [null, []],
    chargeTypeCategory: [null, [Validators.required]],
  });

  constructor(
    protected crbProductServiceFeeTypeService: CrbProductServiceFeeTypeService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ crbProductServiceFeeType }) => {
      this.updateForm(crbProductServiceFeeType);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const crbProductServiceFeeType = this.createFromForm();
    if (crbProductServiceFeeType.id !== undefined) {
      this.subscribeToSaveResponse(this.crbProductServiceFeeTypeService.update(crbProductServiceFeeType));
    } else {
      this.subscribeToSaveResponse(this.crbProductServiceFeeTypeService.create(crbProductServiceFeeType));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICrbProductServiceFeeType>>): void {
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

  protected updateForm(crbProductServiceFeeType: ICrbProductServiceFeeType): void {
    this.editForm.patchValue({
      id: crbProductServiceFeeType.id,
      chargeTypeCode: crbProductServiceFeeType.chargeTypeCode,
      chargeTypeDescription: crbProductServiceFeeType.chargeTypeDescription,
      chargeTypeCategory: crbProductServiceFeeType.chargeTypeCategory,
    });
  }

  protected createFromForm(): ICrbProductServiceFeeType {
    return {
      ...new CrbProductServiceFeeType(),
      id: this.editForm.get(['id'])!.value,
      chargeTypeCode: this.editForm.get(['chargeTypeCode'])!.value,
      chargeTypeDescription: this.editForm.get(['chargeTypeDescription'])!.value,
      chargeTypeCategory: this.editForm.get(['chargeTypeCategory'])!.value,
    };
  }
}
