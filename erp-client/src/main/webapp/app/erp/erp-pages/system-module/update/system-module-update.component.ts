///
/// Erp System - Mark X No 10 (Jehoiada Series) Client 1.7.8
/// Copyright © 2021 - 2024 Edwin Njeru (mailnjeru@gmail.com)
///
/// This program is free software: you can redistribute it and/or modify
/// it under the terms of the GNU General Public License as published by
/// the Free Software Foundation, either version 3 of the License, or
/// (at your option) any later version.
///
/// This program is distributed in the hope that it will be useful,
/// but WITHOUT ANY WARRANTY; without even the implied warranty of
/// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
/// GNU General Public License for more details.
///
/// You should have received a copy of the GNU General Public License
/// along with this program. If not, see <http://www.gnu.org/licenses/>.
///

import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ISystemModule, SystemModule } from '../system-module.model';
import { SystemModuleService } from '../service/system-module.service';
import { PlaceholderService } from '../../placeholder/service/placeholder.service';
import { IPlaceholder } from '../../placeholder/placeholder.model';

@Component({
  selector: 'jhi-system-module-update',
  templateUrl: './system-module-update.component.html',
})
export class SystemModuleUpdateComponent implements OnInit {
  isSaving = false;

  placeholdersSharedCollection: IPlaceholder[] = [];

  editForm = this.fb.group({
    id: [],
    moduleName: [null, [Validators.required]],
    placeholders: [],
  });

  constructor(
    protected systemModuleService: SystemModuleService,
    protected placeholderService: PlaceholderService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ systemModule }) => {
      this.updateForm(systemModule);
      this.loadRelationshipsOptions();
    });
  }

  updatePlaceholders(updates: IPlaceholder[]): void {
    this.editForm.patchValue({
      placeholders: [...updates]
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

  protected loadRelationshipsOptions(): void {
    this.placeholderService
      .query()
      .pipe(map((res: HttpResponse<IPlaceholder[]>) => res.body ?? []))
      .pipe(
        map((placeholders: IPlaceholder[]) =>
          this.placeholderService.addPlaceholderToCollectionIfMissing(
            placeholders,
            ...(this.editForm.get('placeholders')!.value ?? [])
          )
        )
      )
      .subscribe(
        (placeholders: IPlaceholder[]) =>
          (this.placeholdersSharedCollection = placeholders)
      );
  }

  protected updateForm(systemModule: ISystemModule): void {
    this.editForm.patchValue({
      id: systemModule.id,
      moduleName: systemModule.moduleName,
      placeholders: systemModule.placeholders,
    });
    this.placeholdersSharedCollection = this.placeholderService.addPlaceholderToCollectionIfMissing(
      this.placeholdersSharedCollection,
      ...(systemModule.placeholders ?? [])
    );
  }

  protected createFromForm(): ISystemModule {
    return {
      ...new SystemModule(),
      id: this.editForm.get(['id'])!.value,
      moduleName: this.editForm.get(['moduleName'])!.value,
      placeholders: this.editForm.get(['placeholders'])!.value,
    };
  }
}
