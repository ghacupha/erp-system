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
import { finalize } from 'rxjs/operators';

import { ICardCategoryType, CardCategoryType } from '../card-category-type.model';
import { CardCategoryTypeService } from '../service/card-category-type.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { CardCategoryFlag } from 'app/entities/enumerations/card-category-flag.model';

@Component({
  selector: 'jhi-card-category-type-update',
  templateUrl: './card-category-type-update.component.html',
})
export class CardCategoryTypeUpdateComponent implements OnInit {
  isSaving = false;
  cardCategoryFlagValues = Object.keys(CardCategoryFlag);

  editForm = this.fb.group({
    id: [],
    cardCategoryFlag: [null, [Validators.required]],
    cardCategoryDescription: [null, [Validators.required]],
    cardCategoryDetails: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected cardCategoryTypeService: CardCategoryTypeService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cardCategoryType }) => {
      this.updateForm(cardCategoryType);
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
    const cardCategoryType = this.createFromForm();
    if (cardCategoryType.id !== undefined) {
      this.subscribeToSaveResponse(this.cardCategoryTypeService.update(cardCategoryType));
    } else {
      this.subscribeToSaveResponse(this.cardCategoryTypeService.create(cardCategoryType));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICardCategoryType>>): void {
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

  protected updateForm(cardCategoryType: ICardCategoryType): void {
    this.editForm.patchValue({
      id: cardCategoryType.id,
      cardCategoryFlag: cardCategoryType.cardCategoryFlag,
      cardCategoryDescription: cardCategoryType.cardCategoryDescription,
      cardCategoryDetails: cardCategoryType.cardCategoryDetails,
    });
  }

  protected createFromForm(): ICardCategoryType {
    return {
      ...new CardCategoryType(),
      id: this.editForm.get(['id'])!.value,
      cardCategoryFlag: this.editForm.get(['cardCategoryFlag'])!.value,
      cardCategoryDescription: this.editForm.get(['cardCategoryDescription'])!.value,
      cardCategoryDetails: this.editForm.get(['cardCategoryDetails'])!.value,
    };
  }
}
