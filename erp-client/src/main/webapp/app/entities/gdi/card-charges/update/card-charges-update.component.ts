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

import { ICardCharges, CardCharges } from '../card-charges.model';
import { CardChargesService } from '../service/card-charges.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-card-charges-update',
  templateUrl: './card-charges-update.component.html',
})
export class CardChargesUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    cardChargeType: [null, [Validators.required]],
    cardChargeTypeName: [null, [Validators.required]],
    cardChargeDetails: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected cardChargesService: CardChargesService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cardCharges }) => {
      this.updateForm(cardCharges);
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
    const cardCharges = this.createFromForm();
    if (cardCharges.id !== undefined) {
      this.subscribeToSaveResponse(this.cardChargesService.update(cardCharges));
    } else {
      this.subscribeToSaveResponse(this.cardChargesService.create(cardCharges));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICardCharges>>): void {
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

  protected updateForm(cardCharges: ICardCharges): void {
    this.editForm.patchValue({
      id: cardCharges.id,
      cardChargeType: cardCharges.cardChargeType,
      cardChargeTypeName: cardCharges.cardChargeTypeName,
      cardChargeDetails: cardCharges.cardChargeDetails,
    });
  }

  protected createFromForm(): ICardCharges {
    return {
      ...new CardCharges(),
      id: this.editForm.get(['id'])!.value,
      cardChargeType: this.editForm.get(['cardChargeType'])!.value,
      cardChargeTypeName: this.editForm.get(['cardChargeTypeName'])!.value,
      cardChargeDetails: this.editForm.get(['cardChargeDetails'])!.value,
    };
  }
}
