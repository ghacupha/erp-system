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
