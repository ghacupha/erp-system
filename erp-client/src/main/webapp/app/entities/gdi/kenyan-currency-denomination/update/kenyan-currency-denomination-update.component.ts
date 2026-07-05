import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IKenyanCurrencyDenomination, KenyanCurrencyDenomination } from '../kenyan-currency-denomination.model';
import { KenyanCurrencyDenominationService } from '../service/kenyan-currency-denomination.service';

@Component({
  selector: 'jhi-kenyan-currency-denomination-update',
  templateUrl: './kenyan-currency-denomination-update.component.html',
})
export class KenyanCurrencyDenominationUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    currencyDenominationCode: [null, [Validators.required]],
    currencyDenominationType: [null, [Validators.required]],
    currencyDenominationTypeDetails: [],
  });

  constructor(
    protected kenyanCurrencyDenominationService: KenyanCurrencyDenominationService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ kenyanCurrencyDenomination }) => {
      this.updateForm(kenyanCurrencyDenomination);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const kenyanCurrencyDenomination = this.createFromForm();
    if (kenyanCurrencyDenomination.id !== undefined) {
      this.subscribeToSaveResponse(this.kenyanCurrencyDenominationService.update(kenyanCurrencyDenomination));
    } else {
      this.subscribeToSaveResponse(this.kenyanCurrencyDenominationService.create(kenyanCurrencyDenomination));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IKenyanCurrencyDenomination>>): void {
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

  protected updateForm(kenyanCurrencyDenomination: IKenyanCurrencyDenomination): void {
    this.editForm.patchValue({
      id: kenyanCurrencyDenomination.id,
      currencyDenominationCode: kenyanCurrencyDenomination.currencyDenominationCode,
      currencyDenominationType: kenyanCurrencyDenomination.currencyDenominationType,
      currencyDenominationTypeDetails: kenyanCurrencyDenomination.currencyDenominationTypeDetails,
    });
  }

  protected createFromForm(): IKenyanCurrencyDenomination {
    return {
      ...new KenyanCurrencyDenomination(),
      id: this.editForm.get(['id'])!.value,
      currencyDenominationCode: this.editForm.get(['currencyDenominationCode'])!.value,
      currencyDenominationType: this.editForm.get(['currencyDenominationType'])!.value,
      currencyDenominationTypeDetails: this.editForm.get(['currencyDenominationTypeDetails'])!.value,
    };
  }
}
