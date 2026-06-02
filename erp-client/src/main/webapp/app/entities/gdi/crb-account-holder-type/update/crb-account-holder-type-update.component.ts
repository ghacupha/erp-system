import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ICrbAccountHolderType, CrbAccountHolderType } from '../crb-account-holder-type.model';
import { CrbAccountHolderTypeService } from '../service/crb-account-holder-type.service';

@Component({
  selector: 'jhi-crb-account-holder-type-update',
  templateUrl: './crb-account-holder-type-update.component.html',
})
export class CrbAccountHolderTypeUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    accountHolderCategoryTypeCode: [null, [Validators.required]],
    accountHolderCategoryType: [null, [Validators.required]],
  });

  constructor(
    protected crbAccountHolderTypeService: CrbAccountHolderTypeService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ crbAccountHolderType }) => {
      this.updateForm(crbAccountHolderType);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const crbAccountHolderType = this.createFromForm();
    if (crbAccountHolderType.id !== undefined) {
      this.subscribeToSaveResponse(this.crbAccountHolderTypeService.update(crbAccountHolderType));
    } else {
      this.subscribeToSaveResponse(this.crbAccountHolderTypeService.create(crbAccountHolderType));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICrbAccountHolderType>>): void {
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

  protected updateForm(crbAccountHolderType: ICrbAccountHolderType): void {
    this.editForm.patchValue({
      id: crbAccountHolderType.id,
      accountHolderCategoryTypeCode: crbAccountHolderType.accountHolderCategoryTypeCode,
      accountHolderCategoryType: crbAccountHolderType.accountHolderCategoryType,
    });
  }

  protected createFromForm(): ICrbAccountHolderType {
    return {
      ...new CrbAccountHolderType(),
      id: this.editForm.get(['id'])!.value,
      accountHolderCategoryTypeCode: this.editForm.get(['accountHolderCategoryTypeCode'])!.value,
      accountHolderCategoryType: this.editForm.get(['accountHolderCategoryType'])!.value,
    };
  }
}
