import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ICrbGlCode, CrbGlCode } from '../crb-gl-code.model';
import { CrbGlCodeService } from '../service/crb-gl-code.service';

@Component({
  selector: 'jhi-crb-gl-code-update',
  templateUrl: './crb-gl-code-update.component.html',
})
export class CrbGlCodeUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    glCode: [null, [Validators.required]],
    glDescription: [null, [Validators.required]],
    glType: [null, [Validators.required]],
    institutionCategory: [null, [Validators.required]],
  });

  constructor(protected crbGlCodeService: CrbGlCodeService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ crbGlCode }) => {
      this.updateForm(crbGlCode);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const crbGlCode = this.createFromForm();
    if (crbGlCode.id !== undefined) {
      this.subscribeToSaveResponse(this.crbGlCodeService.update(crbGlCode));
    } else {
      this.subscribeToSaveResponse(this.crbGlCodeService.create(crbGlCode));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICrbGlCode>>): void {
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

  protected updateForm(crbGlCode: ICrbGlCode): void {
    this.editForm.patchValue({
      id: crbGlCode.id,
      glCode: crbGlCode.glCode,
      glDescription: crbGlCode.glDescription,
      glType: crbGlCode.glType,
      institutionCategory: crbGlCode.institutionCategory,
    });
  }

  protected createFromForm(): ICrbGlCode {
    return {
      ...new CrbGlCode(),
      id: this.editForm.get(['id'])!.value,
      glCode: this.editForm.get(['glCode'])!.value,
      glDescription: this.editForm.get(['glDescription'])!.value,
      glType: this.editForm.get(['glType'])!.value,
      institutionCategory: this.editForm.get(['institutionCategory'])!.value,
    };
  }
}
