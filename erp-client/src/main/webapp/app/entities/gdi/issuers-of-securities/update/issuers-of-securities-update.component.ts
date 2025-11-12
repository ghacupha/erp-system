///
/// Erp System - Mark X No 11 (Jehoiada Series) Client 1.7.8
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

import { IIssuersOfSecurities, IssuersOfSecurities } from '../issuers-of-securities.model';
import { IssuersOfSecuritiesService } from '../service/issuers-of-securities.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-issuers-of-securities-update',
  templateUrl: './issuers-of-securities-update.component.html',
})
export class IssuersOfSecuritiesUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    issuerOfSecuritiesCode: [null, [Validators.required]],
    issuerOfSecurities: [null, [Validators.required]],
    issuerOfSecuritiesDescription: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected issuersOfSecuritiesService: IssuersOfSecuritiesService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ issuersOfSecurities }) => {
      this.updateForm(issuersOfSecurities);
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
    const issuersOfSecurities = this.createFromForm();
    if (issuersOfSecurities.id !== undefined) {
      this.subscribeToSaveResponse(this.issuersOfSecuritiesService.update(issuersOfSecurities));
    } else {
      this.subscribeToSaveResponse(this.issuersOfSecuritiesService.create(issuersOfSecurities));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IIssuersOfSecurities>>): void {
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

  protected updateForm(issuersOfSecurities: IIssuersOfSecurities): void {
    this.editForm.patchValue({
      id: issuersOfSecurities.id,
      issuerOfSecuritiesCode: issuersOfSecurities.issuerOfSecuritiesCode,
      issuerOfSecurities: issuersOfSecurities.issuerOfSecurities,
      issuerOfSecuritiesDescription: issuersOfSecurities.issuerOfSecuritiesDescription,
    });
  }

  protected createFromForm(): IIssuersOfSecurities {
    return {
      ...new IssuersOfSecurities(),
      id: this.editForm.get(['id'])!.value,
      issuerOfSecuritiesCode: this.editForm.get(['issuerOfSecuritiesCode'])!.value,
      issuerOfSecurities: this.editForm.get(['issuerOfSecurities'])!.value,
      issuerOfSecuritiesDescription: this.editForm.get(['issuerOfSecuritiesDescription'])!.value,
    };
  }
}
