import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ISystemModule, SystemModule } from '../system-module.model';
import { SystemModuleService } from '../service/system-module.service';

@Component({
  selector: 'jhi-system-module-update',
  templateUrl: './system-module-update.component.html',
})
export class SystemModuleUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    moduleName: [null, [Validators.required]],
  });

  constructor(protected systemModuleService: SystemModuleService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ systemModule }) => {
      this.updateForm(systemModule);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const systemModule = this.createFromForm();
    if (systemModule.id !== undefined) {
      this.subscribeToSaveResponse(this.systemModuleService.update(systemModule));
    } else {
      this.subscribeToSaveResponse(this.systemModuleService.create(systemModule));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISystemModule>>): void {
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

  protected updateForm(systemModule: ISystemModule): void {
    this.editForm.patchValue({
      id: systemModule.id,
      moduleName: systemModule.moduleName,
    });
  }

  protected createFromForm(): ISystemModule {
    return {
      ...new SystemModule(),
      id: this.editForm.get(['id'])!.value,
      moduleName: this.editForm.get(['moduleName'])!.value,
    };
  }
}
