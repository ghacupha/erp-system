import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ICrbSourceOfInformationType, CrbSourceOfInformationType } from '../crb-source-of-information-type.model';
import { CrbSourceOfInformationTypeService } from '../service/crb-source-of-information-type.service';

@Component({
  selector: 'jhi-crb-source-of-information-type-update',
  templateUrl: './crb-source-of-information-type-update.component.html',
})
export class CrbSourceOfInformationTypeUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    sourceOfInformationTypeCode: [null, [Validators.required]],
    sourceOfInformationTypeDescription: [null, []],
  });

  constructor(
    protected crbSourceOfInformationTypeService: CrbSourceOfInformationTypeService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ crbSourceOfInformationType }) => {
      this.updateForm(crbSourceOfInformationType);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const crbSourceOfInformationType = this.createFromForm();
    if (crbSourceOfInformationType.id !== undefined) {
      this.subscribeToSaveResponse(this.crbSourceOfInformationTypeService.update(crbSourceOfInformationType));
    } else {
      this.subscribeToSaveResponse(this.crbSourceOfInformationTypeService.create(crbSourceOfInformationType));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICrbSourceOfInformationType>>): void {
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

  protected updateForm(crbSourceOfInformationType: ICrbSourceOfInformationType): void {
    this.editForm.patchValue({
      id: crbSourceOfInformationType.id,
      sourceOfInformationTypeCode: crbSourceOfInformationType.sourceOfInformationTypeCode,
      sourceOfInformationTypeDescription: crbSourceOfInformationType.sourceOfInformationTypeDescription,
    });
  }

  protected createFromForm(): ICrbSourceOfInformationType {
    return {
      ...new CrbSourceOfInformationType(),
      id: this.editForm.get(['id'])!.value,
      sourceOfInformationTypeCode: this.editForm.get(['sourceOfInformationTypeCode'])!.value,
      sourceOfInformationTypeDescription: this.editForm.get(['sourceOfInformationTypeDescription'])!.value,
    };
  }
}
