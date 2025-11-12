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

import { ICardFraudInformation, CardFraudInformation } from '../card-fraud-information.model';
import { CardFraudInformationService } from '../service/card-fraud-information.service';

@Component({
  selector: 'jhi-card-fraud-information-update',
  templateUrl: './card-fraud-information-update.component.html',
})
export class CardFraudInformationUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    reportingDate: [null, [Validators.required]],
    totalNumberOfFraudIncidents: [null, [Validators.required, Validators.min(0)]],
    valueOfFraudIncedentsInLCY: [null, [Validators.required, Validators.min(0)]],
  });

  constructor(
    protected cardFraudInformationService: CardFraudInformationService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cardFraudInformation }) => {
      this.updateForm(cardFraudInformation);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const cardFraudInformation = this.createFromForm();
    if (cardFraudInformation.id !== undefined) {
      this.subscribeToSaveResponse(this.cardFraudInformationService.update(cardFraudInformation));
    } else {
      this.subscribeToSaveResponse(this.cardFraudInformationService.create(cardFraudInformation));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICardFraudInformation>>): void {
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

  protected updateForm(cardFraudInformation: ICardFraudInformation): void {
    this.editForm.patchValue({
      id: cardFraudInformation.id,
      reportingDate: cardFraudInformation.reportingDate,
      totalNumberOfFraudIncidents: cardFraudInformation.totalNumberOfFraudIncidents,
      valueOfFraudIncedentsInLCY: cardFraudInformation.valueOfFraudIncedentsInLCY,
    });
  }

  protected createFromForm(): ICardFraudInformation {
    return {
      ...new CardFraudInformation(),
      id: this.editForm.get(['id'])!.value,
      reportingDate: this.editForm.get(['reportingDate'])!.value,
      totalNumberOfFraudIncidents: this.editForm.get(['totalNumberOfFraudIncidents'])!.value,
      valueOfFraudIncedentsInLCY: this.editForm.get(['valueOfFraudIncedentsInLCY'])!.value,
    };
  }
}
