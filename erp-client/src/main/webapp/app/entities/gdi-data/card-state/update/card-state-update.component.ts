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

import { ICardState, CardState } from '../card-state.model';
import { CardStateService } from '../service/card-state.service';
import { CardStateFlagTypes } from 'app/entities/enumerations/card-state-flag-types.model';

@Component({
  selector: 'jhi-card-state-update',
  templateUrl: './card-state-update.component.html',
})
export class CardStateUpdateComponent implements OnInit {
  isSaving = false;
  cardStateFlagTypesValues = Object.keys(CardStateFlagTypes);

  editForm = this.fb.group({
    id: [],
    cardStateFlag: [null, [Validators.required]],
    cardStateFlagDetails: [null, [Validators.required]],
    cardStateFlagDescription: [],
  });

  constructor(protected cardStateService: CardStateService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cardState }) => {
      this.updateForm(cardState);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const cardState = this.createFromForm();
    if (cardState.id !== undefined) {
      this.subscribeToSaveResponse(this.cardStateService.update(cardState));
    } else {
      this.subscribeToSaveResponse(this.cardStateService.create(cardState));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICardState>>): void {
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

  protected updateForm(cardState: ICardState): void {
    this.editForm.patchValue({
      id: cardState.id,
      cardStateFlag: cardState.cardStateFlag,
      cardStateFlagDetails: cardState.cardStateFlagDetails,
      cardStateFlagDescription: cardState.cardStateFlagDescription,
    });
  }

  protected createFromForm(): ICardState {
    return {
      ...new CardState(),
      id: this.editForm.get(['id'])!.value,
      cardStateFlag: this.editForm.get(['cardStateFlag'])!.value,
      cardStateFlagDetails: this.editForm.get(['cardStateFlagDetails'])!.value,
      cardStateFlagDescription: this.editForm.get(['cardStateFlagDescription'])!.value,
    };
  }
}
