import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IFxCustomerType, FxCustomerType } from '../fx-customer-type.model';
import { FxCustomerTypeService } from '../service/fx-customer-type.service';

@Component({
  selector: 'jhi-fx-customer-type-update',
  templateUrl: './fx-customer-type-update.component.html',
})
export class FxCustomerTypeUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    foreignExchangeCustomerTypeCode: [null, [Validators.required]],
    foreignCustomerType: [null, [Validators.required]],
  });

  constructor(
    protected fxCustomerTypeService: FxCustomerTypeService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ fxCustomerType }) => {
      this.updateForm(fxCustomerType);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const fxCustomerType = this.createFromForm();
    if (fxCustomerType.id !== undefined) {
      this.subscribeToSaveResponse(this.fxCustomerTypeService.update(fxCustomerType));
    } else {
      this.subscribeToSaveResponse(this.fxCustomerTypeService.create(fxCustomerType));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFxCustomerType>>): void {
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

  protected updateForm(fxCustomerType: IFxCustomerType): void {
    this.editForm.patchValue({
      id: fxCustomerType.id,
      foreignExchangeCustomerTypeCode: fxCustomerType.foreignExchangeCustomerTypeCode,
      foreignCustomerType: fxCustomerType.foreignCustomerType,
    });
  }

  protected createFromForm(): IFxCustomerType {
    return {
      ...new FxCustomerType(),
      id: this.editForm.get(['id'])!.value,
      foreignExchangeCustomerTypeCode: this.editForm.get(['foreignExchangeCustomerTypeCode'])!.value,
      foreignCustomerType: this.editForm.get(['foreignCustomerType'])!.value,
    };
  }
}
