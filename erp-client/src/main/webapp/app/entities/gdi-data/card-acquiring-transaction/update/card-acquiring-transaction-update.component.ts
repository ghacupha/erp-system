import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ICardAcquiringTransaction, CardAcquiringTransaction } from '../card-acquiring-transaction.model';
import { CardAcquiringTransactionService } from '../service/card-acquiring-transaction.service';
import { IInstitutionCode } from 'app/entities/gdi/institution-code/institution-code.model';
import { InstitutionCodeService } from 'app/entities/gdi/institution-code/service/institution-code.service';
import { IChannelType } from 'app/entities/gdi/channel-type/channel-type.model';
import { ChannelTypeService } from 'app/entities/gdi/channel-type/service/channel-type.service';
import { ICardBrandType } from 'app/entities/gdi/card-brand-type/card-brand-type.model';
import { CardBrandTypeService } from 'app/entities/gdi/card-brand-type/service/card-brand-type.service';
import { IIsoCurrencyCode } from 'app/entities/gdi/iso-currency-code/iso-currency-code.model';
import { IsoCurrencyCodeService } from 'app/entities/gdi/iso-currency-code/service/iso-currency-code.service';
import { ICardCategoryType } from 'app/entities/gdi/card-category-type/card-category-type.model';
import { CardCategoryTypeService } from 'app/entities/gdi/card-category-type/service/card-category-type.service';

@Component({
  selector: 'jhi-card-acquiring-transaction-update',
  templateUrl: './card-acquiring-transaction-update.component.html',
})
export class CardAcquiringTransactionUpdateComponent implements OnInit {
  isSaving = false;

  institutionCodesSharedCollection: IInstitutionCode[] = [];
  channelTypesSharedCollection: IChannelType[] = [];
  cardBrandTypesSharedCollection: ICardBrandType[] = [];
  isoCurrencyCodesSharedCollection: IIsoCurrencyCode[] = [];
  cardCategoryTypesSharedCollection: ICardCategoryType[] = [];

  editForm = this.fb.group({
    id: [],
    reportingDate: [null, [Validators.required]],
    terminalId: [null, [Validators.required]],
    numberOfTransactions: [null, [Validators.required, Validators.min(0)]],
    valueOfTransactionsInLCY: [null, [Validators.required, Validators.min(0)]],
    bankCode: [null, Validators.required],
    channelType: [null, Validators.required],
    cardBrandType: [null, Validators.required],
    currencyOfTransaction: [null, Validators.required],
    cardIssuerCategory: [null, Validators.required],
  });

  constructor(
    protected cardAcquiringTransactionService: CardAcquiringTransactionService,
    protected institutionCodeService: InstitutionCodeService,
    protected channelTypeService: ChannelTypeService,
    protected cardBrandTypeService: CardBrandTypeService,
    protected isoCurrencyCodeService: IsoCurrencyCodeService,
    protected cardCategoryTypeService: CardCategoryTypeService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cardAcquiringTransaction }) => {
      this.updateForm(cardAcquiringTransaction);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const cardAcquiringTransaction = this.createFromForm();
    if (cardAcquiringTransaction.id !== undefined) {
      this.subscribeToSaveResponse(this.cardAcquiringTransactionService.update(cardAcquiringTransaction));
    } else {
      this.subscribeToSaveResponse(this.cardAcquiringTransactionService.create(cardAcquiringTransaction));
    }
  }

  trackInstitutionCodeById(index: number, item: IInstitutionCode): number {
    return item.id!;
  }

  trackChannelTypeById(index: number, item: IChannelType): number {
    return item.id!;
  }

  trackCardBrandTypeById(index: number, item: ICardBrandType): number {
    return item.id!;
  }

  trackIsoCurrencyCodeById(index: number, item: IIsoCurrencyCode): number {
    return item.id!;
  }

  trackCardCategoryTypeById(index: number, item: ICardCategoryType): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICardAcquiringTransaction>>): void {
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

  protected updateForm(cardAcquiringTransaction: ICardAcquiringTransaction): void {
    this.editForm.patchValue({
      id: cardAcquiringTransaction.id,
      reportingDate: cardAcquiringTransaction.reportingDate,
      terminalId: cardAcquiringTransaction.terminalId,
      numberOfTransactions: cardAcquiringTransaction.numberOfTransactions,
      valueOfTransactionsInLCY: cardAcquiringTransaction.valueOfTransactionsInLCY,
      bankCode: cardAcquiringTransaction.bankCode,
      channelType: cardAcquiringTransaction.channelType,
      cardBrandType: cardAcquiringTransaction.cardBrandType,
      currencyOfTransaction: cardAcquiringTransaction.currencyOfTransaction,
      cardIssuerCategory: cardAcquiringTransaction.cardIssuerCategory,
    });

    this.institutionCodesSharedCollection = this.institutionCodeService.addInstitutionCodeToCollectionIfMissing(
      this.institutionCodesSharedCollection,
      cardAcquiringTransaction.bankCode
    );
    this.channelTypesSharedCollection = this.channelTypeService.addChannelTypeToCollectionIfMissing(
      this.channelTypesSharedCollection,
      cardAcquiringTransaction.channelType
    );
    this.cardBrandTypesSharedCollection = this.cardBrandTypeService.addCardBrandTypeToCollectionIfMissing(
      this.cardBrandTypesSharedCollection,
      cardAcquiringTransaction.cardBrandType
    );
    this.isoCurrencyCodesSharedCollection = this.isoCurrencyCodeService.addIsoCurrencyCodeToCollectionIfMissing(
      this.isoCurrencyCodesSharedCollection,
      cardAcquiringTransaction.currencyOfTransaction
    );
    this.cardCategoryTypesSharedCollection = this.cardCategoryTypeService.addCardCategoryTypeToCollectionIfMissing(
      this.cardCategoryTypesSharedCollection,
      cardAcquiringTransaction.cardIssuerCategory
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

    this.channelTypeService
      .query()
      .pipe(map((res: HttpResponse<IChannelType[]>) => res.body ?? []))
      .pipe(
        map((channelTypes: IChannelType[]) =>
          this.channelTypeService.addChannelTypeToCollectionIfMissing(channelTypes, this.editForm.get('channelType')!.value)
        )
      )
      .subscribe((channelTypes: IChannelType[]) => (this.channelTypesSharedCollection = channelTypes));

    this.cardBrandTypeService
      .query()
      .pipe(map((res: HttpResponse<ICardBrandType[]>) => res.body ?? []))
      .pipe(
        map((cardBrandTypes: ICardBrandType[]) =>
          this.cardBrandTypeService.addCardBrandTypeToCollectionIfMissing(cardBrandTypes, this.editForm.get('cardBrandType')!.value)
        )
      )
      .subscribe((cardBrandTypes: ICardBrandType[]) => (this.cardBrandTypesSharedCollection = cardBrandTypes));

    this.isoCurrencyCodeService
      .query()
      .pipe(map((res: HttpResponse<IIsoCurrencyCode[]>) => res.body ?? []))
      .pipe(
        map((isoCurrencyCodes: IIsoCurrencyCode[]) =>
          this.isoCurrencyCodeService.addIsoCurrencyCodeToCollectionIfMissing(
            isoCurrencyCodes,
            this.editForm.get('currencyOfTransaction')!.value
          )
        )
      )
      .subscribe((isoCurrencyCodes: IIsoCurrencyCode[]) => (this.isoCurrencyCodesSharedCollection = isoCurrencyCodes));

    this.cardCategoryTypeService
      .query()
      .pipe(map((res: HttpResponse<ICardCategoryType[]>) => res.body ?? []))
      .pipe(
        map((cardCategoryTypes: ICardCategoryType[]) =>
          this.cardCategoryTypeService.addCardCategoryTypeToCollectionIfMissing(
            cardCategoryTypes,
            this.editForm.get('cardIssuerCategory')!.value
          )
        )
      )
      .subscribe((cardCategoryTypes: ICardCategoryType[]) => (this.cardCategoryTypesSharedCollection = cardCategoryTypes));
  }

  protected createFromForm(): ICardAcquiringTransaction {
    return {
      ...new CardAcquiringTransaction(),
      id: this.editForm.get(['id'])!.value,
      reportingDate: this.editForm.get(['reportingDate'])!.value,
      terminalId: this.editForm.get(['terminalId'])!.value,
      numberOfTransactions: this.editForm.get(['numberOfTransactions'])!.value,
      valueOfTransactionsInLCY: this.editForm.get(['valueOfTransactionsInLCY'])!.value,
      bankCode: this.editForm.get(['bankCode'])!.value,
      channelType: this.editForm.get(['channelType'])!.value,
      cardBrandType: this.editForm.get(['cardBrandType'])!.value,
      currencyOfTransaction: this.editForm.get(['currencyOfTransaction'])!.value,
      cardIssuerCategory: this.editForm.get(['cardIssuerCategory'])!.value,
    };
  }
}
