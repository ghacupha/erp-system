import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ICrbDataSubmittingInstitutions, CrbDataSubmittingInstitutions } from '../crb-data-submitting-institutions.model';
import { CrbDataSubmittingInstitutionsService } from '../service/crb-data-submitting-institutions.service';

@Component({
  selector: 'jhi-crb-data-submitting-institutions-update',
  templateUrl: './crb-data-submitting-institutions-update.component.html',
})
export class CrbDataSubmittingInstitutionsUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    institutionCode: [null, [Validators.required]],
    institutionName: [null, [Validators.required]],
    institutionCategory: [null, [Validators.required]],
  });

  constructor(
    protected crbDataSubmittingInstitutionsService: CrbDataSubmittingInstitutionsService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ crbDataSubmittingInstitutions }) => {
      this.updateForm(crbDataSubmittingInstitutions);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const crbDataSubmittingInstitutions = this.createFromForm();
    if (crbDataSubmittingInstitutions.id !== undefined) {
      this.subscribeToSaveResponse(this.crbDataSubmittingInstitutionsService.update(crbDataSubmittingInstitutions));
    } else {
      this.subscribeToSaveResponse(this.crbDataSubmittingInstitutionsService.create(crbDataSubmittingInstitutions));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICrbDataSubmittingInstitutions>>): void {
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

  protected updateForm(crbDataSubmittingInstitutions: ICrbDataSubmittingInstitutions): void {
    this.editForm.patchValue({
      id: crbDataSubmittingInstitutions.id,
      institutionCode: crbDataSubmittingInstitutions.institutionCode,
      institutionName: crbDataSubmittingInstitutions.institutionName,
      institutionCategory: crbDataSubmittingInstitutions.institutionCategory,
    });
  }

  protected createFromForm(): ICrbDataSubmittingInstitutions {
    return {
      ...new CrbDataSubmittingInstitutions(),
      id: this.editForm.get(['id'])!.value,
      institutionCode: this.editForm.get(['institutionCode'])!.value,
      institutionName: this.editForm.get(['institutionName'])!.value,
      institutionCategory: this.editForm.get(['institutionCategory'])!.value,
    };
  }
}
