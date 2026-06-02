import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IBouncedChequeCategories, BouncedChequeCategories } from '../bounced-cheque-categories.model';
import { BouncedChequeCategoriesService } from '../service/bounced-cheque-categories.service';

@Component({
  selector: 'jhi-bounced-cheque-categories-update',
  templateUrl: './bounced-cheque-categories-update.component.html',
})
export class BouncedChequeCategoriesUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    bouncedChequeCategoryTypeCode: [null, [Validators.required]],
    bouncedChequeCategoryType: [null, [Validators.required]],
  });

  constructor(
    protected bouncedChequeCategoriesService: BouncedChequeCategoriesService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ bouncedChequeCategories }) => {
      this.updateForm(bouncedChequeCategories);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const bouncedChequeCategories = this.createFromForm();
    if (bouncedChequeCategories.id !== undefined) {
      this.subscribeToSaveResponse(this.bouncedChequeCategoriesService.update(bouncedChequeCategories));
    } else {
      this.subscribeToSaveResponse(this.bouncedChequeCategoriesService.create(bouncedChequeCategories));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBouncedChequeCategories>>): void {
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

  protected updateForm(bouncedChequeCategories: IBouncedChequeCategories): void {
    this.editForm.patchValue({
      id: bouncedChequeCategories.id,
      bouncedChequeCategoryTypeCode: bouncedChequeCategories.bouncedChequeCategoryTypeCode,
      bouncedChequeCategoryType: bouncedChequeCategories.bouncedChequeCategoryType,
    });
  }

  protected createFromForm(): IBouncedChequeCategories {
    return {
      ...new BouncedChequeCategories(),
      id: this.editForm.get(['id'])!.value,
      bouncedChequeCategoryTypeCode: this.editForm.get(['bouncedChequeCategoryTypeCode'])!.value,
      bouncedChequeCategoryType: this.editForm.get(['bouncedChequeCategoryType'])!.value,
    };
  }
}
