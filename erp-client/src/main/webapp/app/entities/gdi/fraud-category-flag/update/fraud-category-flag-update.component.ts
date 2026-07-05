import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IFraudCategoryFlag, FraudCategoryFlag } from '../fraud-category-flag.model';
import { FraudCategoryFlagService } from '../service/fraud-category-flag.service';
import { FlagCodes } from 'app/entities/enumerations/flag-codes.model';

@Component({
  selector: 'jhi-fraud-category-flag-update',
  templateUrl: './fraud-category-flag-update.component.html',
})
export class FraudCategoryFlagUpdateComponent implements OnInit {
  isSaving = false;
  flagCodesValues = Object.keys(FlagCodes);

  editForm = this.fb.group({
    id: [],
    fraudCategoryFlag: [null, [Validators.required]],
    fraudCategoryTypeDetails: [],
  });

  constructor(
    protected fraudCategoryFlagService: FraudCategoryFlagService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ fraudCategoryFlag }) => {
      this.updateForm(fraudCategoryFlag);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const fraudCategoryFlag = this.createFromForm();
    if (fraudCategoryFlag.id !== undefined) {
      this.subscribeToSaveResponse(this.fraudCategoryFlagService.update(fraudCategoryFlag));
    } else {
      this.subscribeToSaveResponse(this.fraudCategoryFlagService.create(fraudCategoryFlag));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFraudCategoryFlag>>): void {
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

  protected updateForm(fraudCategoryFlag: IFraudCategoryFlag): void {
    this.editForm.patchValue({
      id: fraudCategoryFlag.id,
      fraudCategoryFlag: fraudCategoryFlag.fraudCategoryFlag,
      fraudCategoryTypeDetails: fraudCategoryFlag.fraudCategoryTypeDetails,
    });
  }

  protected createFromForm(): IFraudCategoryFlag {
    return {
      ...new FraudCategoryFlag(),
      id: this.editForm.get(['id'])!.value,
      fraudCategoryFlag: this.editForm.get(['fraudCategoryFlag'])!.value,
      fraudCategoryTypeDetails: this.editForm.get(['fraudCategoryTypeDetails'])!.value,
    };
  }
}
