///
/// Erp System - Mark X No 11 (Jehoiada Series) Client 1.7.9
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
import { finalize } from 'rxjs/operators';

import { ISecurityType, SecurityType } from '../security-type.model';
import { SecurityTypeService } from '../service/security-type.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-security-type-update',
  templateUrl: './security-type-update.component.html',
})
export class SecurityTypeUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    securityTypeCode: [null, [Validators.required]],
    securityType: [null, [Validators.required]],
    securityTypeDetails: [],
    securityTypeDescription: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected securityTypeService: SecurityTypeService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ securityType }) => {
      this.updateForm(securityType);
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('erpSystemApp.error', { message: err.message })),
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const securityType = this.createFromForm();
    if (securityType.id !== undefined) {
      this.subscribeToSaveResponse(this.securityTypeService.update(securityType));
    } else {
      this.subscribeToSaveResponse(this.securityTypeService.create(securityType));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISecurityType>>): void {
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

  protected updateForm(securityType: ISecurityType): void {
    this.editForm.patchValue({
      id: securityType.id,
      securityTypeCode: securityType.securityTypeCode,
      securityType: securityType.securityType,
      securityTypeDetails: securityType.securityTypeDetails,
      securityTypeDescription: securityType.securityTypeDescription,
    });
  }

  protected createFromForm(): ISecurityType {
    return {
      ...new SecurityType(),
      id: this.editForm.get(['id'])!.value,
      securityTypeCode: this.editForm.get(['securityTypeCode'])!.value,
      securityType: this.editForm.get(['securityType'])!.value,
      securityTypeDetails: this.editForm.get(['securityTypeDetails'])!.value,
      securityTypeDescription: this.editForm.get(['securityTypeDescription'])!.value,
    };
  }
}
