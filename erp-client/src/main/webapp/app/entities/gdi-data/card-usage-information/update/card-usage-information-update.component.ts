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
import { finalize, map } from 'rxjs/operators';

import { ICardUsageInformation, CardUsageInformation } from '../card-usage-information.model';
import { CardUsageInformationService } from '../service/card-usage-information.service';
import { IInstitutionCode } from 'app/entities/gdi/institution-code/institution-code.model';
import { InstitutionCodeService } from 'app/entities/gdi/institution-code/service/institution-code.service';
import { ICardTypes } from 'app/entities/gdi/card-types/card-types.model';
import { CardTypesService } from 'app/entities/gdi/card-types/service/card-types.service';
import { ICardBrandType } from 'app/entities/gdi/card-brand-type/card-brand-type.model';
import { CardBrandTypeService } from 'app/entities/gdi/card-brand-type/service/card-brand-type.service';
import { ICardCategoryType } from 'app/entities/gdi/card-category-type/card-category-type.model';
import { CardCategoryTypeService } from 'app/entities/gdi/card-category-type/service/card-category-type.service';
import { IBankTransactionType } from 'app/entities/gdi/bank-transaction-type/bank-transaction-type.model';
import { BankTransactionTypeService } from 'app/entities/gdi/bank-transaction-type/service/bank-transaction-type.service';
import { IChannelType } from 'app/entities/gdi/channel-type/channel-type.model';
import { ChannelTypeService } from 'app/entities/gdi/channel-type/service/channel-type.service';
import { ICardState } from 'app/entities/gdi-data/card-state/card-state.model';
import { CardStateService } from 'app/entities/gdi-data/card-state/service/card-state.service';

@Component({
  selector: 'jhi-card-usage-information-update',
  templateUrl: './card-usage-information-update.component.html',
})
export class CardUsageInformationUpdateComponent implements OnInit {
  isSaving = false;

  institutionCodesSharedCollection: IInstitutionCode[] = [];
  cardTypesSharedCollection: ICardTypes[] = [];
  cardBrandTypesSharedCollection: ICardBrandType[] = [];
  cardCategoryTypesSharedCollection: ICardCategoryType[] = [];
  bankTransactionTypesSharedCollection: IBankTransactionType[] = [];
  channelTypesSharedCollection: IChannelType[] = [];
  cardStatesSharedCollection: ICardState[] = [];

  editForm = this.fb.group({
    id: [],
    reportingDate: [null, [Validators.required]],
    totalNumberOfLiveCards: [null, [Validators.required, Validators.min(0)]],
    totalActiveCards: [null, [Validators.required, Validators.min(0)]],
    totalNumberOfTransactionsDone: [null, [Validators.required, Validators.min(0)]],
    totalValueOfTransactionsDoneInLCY: [null, [Validators.required, Validators.min(0)]],
    bankCode: [null, Validators.required],
    cardType: [null, Validators.required],
    cardBrand: [null, Validators.required],
    cardCategoryType: [null, Validators.required],
    transactionType: [null, Validators.required],
    channelType: [null, Validators.required],
    cardState: [null, Validators.required],
  });

  constructor(
    protected cardUsageInformationService: CardUsageInformationService,
    protected institutionCodeService: InstitutionCodeService,
    protected cardTypesService: CardTypesService,
    protected cardBrandTypeService: CardBrandTypeService,
    protected cardCategoryTypeService: CardCategoryTypeService,
    protected bankTransactionTypeService: BankTransactionTypeService,
    protected channelTypeService: ChannelTypeService,
    protected cardStateService: CardStateService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cardUsageInformation }) => {
      this.updateForm(cardUsageInformation);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const cardUsageInformation = this.createFromForm();
    if (cardUsageInformation.id !== undefined) {
      this.subscribeToSaveResponse(this.cardUsageInformationService.update(cardUsageInformation));
    } else {
      this.subscribeToSaveResponse(this.cardUsageInformationService.create(cardUsageInformation));
    }
  }

  trackInstitutionCodeById(index: number, item: IInstitutionCode): number {
    return item.id!;
  }

  trackCardTypesById(index: number, item: ICardTypes): number {
    return item.id!;
  }

  trackCardBrandTypeById(index: number, item: ICardBrandType): number {
    return item.id!;
  }

  trackCardCategoryTypeById(index: number, item: ICardCategoryType): number {
    return item.id!;
  }

  trackBankTransactionTypeById(index: number, item: IBankTransactionType): number {
    return item.id!;
  }

  trackChannelTypeById(index: number, item: IChannelType): number {
    return item.id!;
  }

  trackCardStateById(index: number, item: ICardState): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICardUsageInformation>>): void {
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

  protected updateForm(cardUsageInformation: ICardUsageInformation): void {
    this.editForm.patchValue({
      id: cardUsageInformation.id,
      reportingDate: cardUsageInformation.reportingDate,
      totalNumberOfLiveCards: cardUsageInformation.totalNumberOfLiveCards,
      totalActiveCards: cardUsageInformation.totalActiveCards,
      totalNumberOfTransactionsDone: cardUsageInformation.totalNumberOfTransactionsDone,
      totalValueOfTransactionsDoneInLCY: cardUsageInformation.totalValueOfTransactionsDoneInLCY,
      bankCode: cardUsageInformation.bankCode,
      cardType: cardUsageInformation.cardType,
      cardBrand: cardUsageInformation.cardBrand,
      cardCategoryType: cardUsageInformation.cardCategoryType,
      transactionType: cardUsageInformation.transactionType,
      channelType: cardUsageInformation.channelType,
      cardState: cardUsageInformation.cardState,
    });

    this.institutionCodesSharedCollection = this.institutionCodeService.addInstitutionCodeToCollectionIfMissing(
      this.institutionCodesSharedCollection,
      cardUsageInformation.bankCode
    );
    this.cardTypesSharedCollection = this.cardTypesService.addCardTypesToCollectionIfMissing(
      this.cardTypesSharedCollection,
      cardUsageInformation.cardType
    );
    this.cardBrandTypesSharedCollection = this.cardBrandTypeService.addCardBrandTypeToCollectionIfMissing(
      this.cardBrandTypesSharedCollection,
      cardUsageInformation.cardBrand
    );
    this.cardCategoryTypesSharedCollection = this.cardCategoryTypeService.addCardCategoryTypeToCollectionIfMissing(
      this.cardCategoryTypesSharedCollection,
      cardUsageInformation.cardCategoryType
    );
    this.bankTransactionTypesSharedCollection = this.bankTransactionTypeService.addBankTransactionTypeToCollectionIfMissing(
      this.bankTransactionTypesSharedCollection,
      cardUsageInformation.transactionType
    );
    this.channelTypesSharedCollection = this.channelTypeService.addChannelTypeToCollectionIfMissing(
      this.channelTypesSharedCollection,
      cardUsageInformation.channelType
    );
    this.cardStatesSharedCollection = this.cardStateService.addCardStateToCollectionIfMissing(
      this.cardStatesSharedCollection,
      cardUsageInformation.cardState
    );
  }

  protected loadRelationshipsOptions(): void {
    this.institutionCodeService
      .query()
      .pipe(map((res: HttpResponse<IInstitutionCode[]>) => res.body ?? []))
      .pipe(
        map((institutionCodes: IInstitutionCode[]) =>
          this.institutionCodeService.addInstitutionCodeToCollectionIfMissing(institutionCodes, this.editForm.get('bankCode')!.value)
        )
      )
      .subscribe((institutionCodes: IInstitutionCode[]) => (this.institutionCodesSharedCollection = institutionCodes));

    this.cardTypesService
      .query()
      .pipe(map((res: HttpResponse<ICardTypes[]>) => res.body ?? []))
      .pipe(
        map((cardTypes: ICardTypes[]) =>
          this.cardTypesService.addCardTypesToCollectionIfMissing(cardTypes, this.editForm.get('cardType')!.value)
        )
      )
      .subscribe((cardTypes: ICardTypes[]) => (this.cardTypesSharedCollection = cardTypes));

    this.cardBrandTypeService
      .query()
      .pipe(map((res: HttpResponse<ICardBrandType[]>) => res.body ?? []))
      .pipe(
        map((cardBrandTypes: ICardBrandType[]) =>
          this.cardBrandTypeService.addCardBrandTypeToCollectionIfMissing(cardBrandTypes, this.editForm.get('cardBrand')!.value)
        )
      )
      .subscribe((cardBrandTypes: ICardBrandType[]) => (this.cardBrandTypesSharedCollection = cardBrandTypes));

    this.cardCategoryTypeService
      .query()
      .pipe(map((res: HttpResponse<ICardCategoryType[]>) => res.body ?? []))
      .pipe(
        map((cardCategoryTypes: ICardCategoryType[]) =>
          this.cardCategoryTypeService.addCardCategoryTypeToCollectionIfMissing(
            cardCategoryTypes,
            this.editForm.get('cardCategoryType')!.value
          )
        )
      )
      .subscribe((cardCategoryTypes: ICardCategoryType[]) => (this.cardCategoryTypesSharedCollection = cardCategoryTypes));

    this.bankTransactionTypeService
      .query()
      .pipe(map((res: HttpResponse<IBankTransactionType[]>) => res.body ?? []))
      .pipe(
        map((bankTransactionTypes: IBankTransactionType[]) =>
          this.bankTransactionTypeService.addBankTransactionTypeToCollectionIfMissing(
            bankTransactionTypes,
            this.editForm.get('transactionType')!.value
          )
        )
      )
      .subscribe((bankTransactionTypes: IBankTransactionType[]) => (this.bankTransactionTypesSharedCollection = bankTransactionTypes));

    this.channelTypeService
      .query()
      .pipe(map((res: HttpResponse<IChannelType[]>) => res.body ?? []))
      .pipe(
        map((channelTypes: IChannelType[]) =>
          this.channelTypeService.addChannelTypeToCollectionIfMissing(channelTypes, this.editForm.get('channelType')!.value)
        )
      )
      .subscribe((channelTypes: IChannelType[]) => (this.channelTypesSharedCollection = channelTypes));

    this.cardStateService
      .query()
      .pipe(map((res: HttpResponse<ICardState[]>) => res.body ?? []))
      .pipe(
        map((cardStates: ICardState[]) =>
          this.cardStateService.addCardStateToCollectionIfMissing(cardStates, this.editForm.get('cardState')!.value)
        )
      )
      .subscribe((cardStates: ICardState[]) => (this.cardStatesSharedCollection = cardStates));
  }

  protected createFromForm(): ICardUsageInformation {
    return {
      ...new CardUsageInformation(),
      id: this.editForm.get(['id'])!.value,
      reportingDate: this.editForm.get(['reportingDate'])!.value,
      totalNumberOfLiveCards: this.editForm.get(['totalNumberOfLiveCards'])!.value,
      totalActiveCards: this.editForm.get(['totalActiveCards'])!.value,
      totalNumberOfTransactionsDone: this.editForm.get(['totalNumberOfTransactionsDone'])!.value,
      totalValueOfTransactionsDoneInLCY: this.editForm.get(['totalValueOfTransactionsDoneInLCY'])!.value,
      bankCode: this.editForm.get(['bankCode'])!.value,
      cardType: this.editForm.get(['cardType'])!.value,
      cardBrand: this.editForm.get(['cardBrand'])!.value,
      cardCategoryType: this.editForm.get(['cardCategoryType'])!.value,
      transactionType: this.editForm.get(['transactionType'])!.value,
      channelType: this.editForm.get(['channelType'])!.value,
      cardState: this.editForm.get(['cardState'])!.value,
    };
  }
}
