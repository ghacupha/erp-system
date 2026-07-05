import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IEmploymentTerms, EmploymentTerms } from '../employment-terms.model';
import { EmploymentTermsService } from '../service/employment-terms.service';

@Component({
  selector: 'jhi-employment-terms-update',
  templateUrl: './employment-terms-update.component.html',
})
export class EmploymentTermsUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    employmentTermsCode: [null, [Validators.required]],
    employmentTermsStatus: [null, [Validators.required]],
  });

  constructor(
    protected employmentTermsService: EmploymentTermsService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ employmentTerms }) => {
      this.updateForm(employmentTerms);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const employmentTerms = this.createFromForm();
    if (employmentTerms.id !== undefined) {
      this.subscribeToSaveResponse(this.employmentTermsService.update(employmentTerms));
    } else {
      this.subscribeToSaveResponse(this.employmentTermsService.create(employmentTerms));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEmploymentTerms>>): void {
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

  protected updateForm(employmentTerms: IEmploymentTerms): void {
    this.editForm.patchValue({
      id: employmentTerms.id,
      employmentTermsCode: employmentTerms.employmentTermsCode,
      employmentTermsStatus: employmentTerms.employmentTermsStatus,
    });
  }

  protected createFromForm(): IEmploymentTerms {
    return {
      ...new EmploymentTerms(),
      id: this.editForm.get(['id'])!.value,
      employmentTermsCode: this.editForm.get(['employmentTermsCode'])!.value,
      employmentTermsStatus: this.editForm.get(['employmentTermsStatus'])!.value,
    };
  }
}
