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

import { ICardStatusFlag, CardStatusFlag } from '../card-status-flag.model';
import { CardStatusFlagService } from '../service/card-status-flag.service';
import { FlagCodes } from 'app/entities/enumerations/flag-codes.model';

@Component({
  selector: 'jhi-card-status-flag-update',
  templateUrl: './card-status-flag-update.component.html',
})
export class CardStatusFlagUpdateComponent implements OnInit {
  isSaving = false;
  flagCodesValues = Object.keys(FlagCodes);

  editForm = this.fb.group({
    id: [],
    cardStatusFlag: [null, [Validators.required]],
    cardStatusFlagDescription: [null, [Validators.required]],
    cardStatusFlagDetails: [],
  });

  constructor(
    protected cardStatusFlagService: CardStatusFlagService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cardStatusFlag }) => {
      this.updateForm(cardStatusFlag);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const cardStatusFlag = this.createFromForm();
    if (cardStatusFlag.id !== undefined) {
      this.subscribeToSaveResponse(this.cardStatusFlagService.update(cardStatusFlag));
    } else {
      this.subscribeToSaveResponse(this.cardStatusFlagService.create(cardStatusFlag));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICardStatusFlag>>): void {
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

  protected updateForm(cardStatusFlag: ICardStatusFlag): void {
    this.editForm.patchValue({
      id: cardStatusFlag.id,
      cardStatusFlag: cardStatusFlag.cardStatusFlag,
      cardStatusFlagDescription: cardStatusFlag.cardStatusFlagDescription,
      cardStatusFlagDetails: cardStatusFlag.cardStatusFlagDetails,
    });
  }

  protected createFromForm(): ICardStatusFlag {
    return {
      ...new CardStatusFlag(),
      id: this.editForm.get(['id'])!.value,
      cardStatusFlag: this.editForm.get(['cardStatusFlag'])!.value,
      cardStatusFlagDescription: this.editForm.get(['cardStatusFlagDescription'])!.value,
      cardStatusFlagDetails: this.editForm.get(['cardStatusFlagDetails'])!.value,
    };
  }
}
