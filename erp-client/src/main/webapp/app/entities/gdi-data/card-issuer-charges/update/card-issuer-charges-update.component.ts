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
import { finalize, map } from 'rxjs/operators';

import { ICardIssuerCharges, CardIssuerCharges } from '../card-issuer-charges.model';
import { CardIssuerChargesService } from '../service/card-issuer-charges.service';
import { IInstitutionCode } from 'app/entities/gdi/institution-code/institution-code.model';
import { InstitutionCodeService } from 'app/entities/gdi/institution-code/service/institution-code.service';
import { ICardCategoryType } from 'app/entities/gdi/card-category-type/card-category-type.model';
import { CardCategoryTypeService } from 'app/entities/gdi/card-category-type/service/card-category-type.service';
import { ICardTypes } from 'app/entities/gdi/card-types/card-types.model';
import { CardTypesService } from 'app/entities/gdi/card-types/service/card-types.service';
import { ICardBrandType } from 'app/entities/gdi/card-brand-type/card-brand-type.model';
import { CardBrandTypeService } from 'app/entities/gdi/card-brand-type/service/card-brand-type.service';
import { ICardClassType } from 'app/entities/gdi/card-class-type/card-class-type.model';
import { CardClassTypeService } from 'app/entities/gdi/card-class-type/service/card-class-type.service';
import { ICardCharges } from 'app/entities/gdi/card-charges/card-charges.model';
import { CardChargesService } from 'app/entities/gdi/card-charges/service/card-charges.service';

@Component({
  selector: 'jhi-card-issuer-charges-update',
  templateUrl: './card-issuer-charges-update.component.html',
})
export class CardIssuerChargesUpdateComponent implements OnInit {
  isSaving = false;

  institutionCodesSharedCollection: IInstitutionCode[] = [];
  cardCategoryTypesSharedCollection: ICardCategoryType[] = [];
  cardTypesSharedCollection: ICardTypes[] = [];
  cardBrandTypesSharedCollection: ICardBrandType[] = [];
  cardClassTypesSharedCollection: ICardClassType[] = [];
  cardChargesSharedCollection: ICardCharges[] = [];

  editForm = this.fb.group({
    id: [],
    reportingDate: [null, [Validators.required]],
    cardFeeChargeInLCY: [null, [Validators.required, Validators.min(0)]],
    bankCode: [null, Validators.required],
    cardCategory: [null, Validators.required],
    cardType: [null, Validators.required],
    cardBrand: [null, Validators.required],
    cardClass: [null, Validators.required],
    cardChargeType: [null, Validators.required],
  });

  constructor(
    protected cardIssuerChargesService: CardIssuerChargesService,
    protected institutionCodeService: InstitutionCodeService,
    protected cardCategoryTypeService: CardCategoryTypeService,
    protected cardTypesService: CardTypesService,
    protected cardBrandTypeService: CardBrandTypeService,
    protected cardClassTypeService: CardClassTypeService,
    protected cardChargesService: CardChargesService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cardIssuerCharges }) => {
      this.updateForm(cardIssuerCharges);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const cardIssuerCharges = this.createFromForm();
    if (cardIssuerCharges.id !== undefined) {
      this.subscribeToSaveResponse(this.cardIssuerChargesService.update(cardIssuerCharges));
    } else {
      this.subscribeToSaveResponse(this.cardIssuerChargesService.create(cardIssuerCharges));
    }
  }

  trackInstitutionCodeById(index: number, item: IInstitutionCode): number {
    return item.id!;
  }

  trackCardCategoryTypeById(index: number, item: ICardCategoryType): number {
    return item.id!;
  }

  trackCardTypesById(index: number, item: ICardTypes): number {
    return item.id!;
  }

  trackCardBrandTypeById(index: number, item: ICardBrandType): number {
    return item.id!;
  }

  trackCardClassTypeById(index: number, item: ICardClassType): number {
    return item.id!;
  }

  trackCardChargesById(index: number, item: ICardCharges): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICardIssuerCharges>>): void {
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

  protected updateForm(cardIssuerCharges: ICardIssuerCharges): void {
    this.editForm.patchValue({
      id: cardIssuerCharges.id,
      reportingDate: cardIssuerCharges.reportingDate,
      cardFeeChargeInLCY: cardIssuerCharges.cardFeeChargeInLCY,
      bankCode: cardIssuerCharges.bankCode,
      cardCategory: cardIssuerCharges.cardCategory,
      cardType: cardIssuerCharges.cardType,
      cardBrand: cardIssuerCharges.cardBrand,
      cardClass: cardIssuerCharges.cardClass,
      cardChargeType: cardIssuerCharges.cardChargeType,
    });

    this.institutionCodesSharedCollection = this.institutionCodeService.addInstitutionCodeToCollectionIfMissing(
      this.institutionCodesSharedCollection,
      cardIssuerCharges.bankCode
    );
    this.cardCategoryTypesSharedCollection = this.cardCategoryTypeService.addCardCategoryTypeToCollectionIfMissing(
      this.cardCategoryTypesSharedCollection,
      cardIssuerCharges.cardCategory
    );
    this.cardTypesSharedCollection = this.cardTypesService.addCardTypesToCollectionIfMissing(
      this.cardTypesSharedCollection,
      cardIssuerCharges.cardType
    );
    this.cardBrandTypesSharedCollection = this.cardBrandTypeService.addCardBrandTypeToCollectionIfMissing(
      this.cardBrandTypesSharedCollection,
      cardIssuerCharges.cardBrand
    );
    this.cardClassTypesSharedCollection = this.cardClassTypeService.addCardClassTypeToCollectionIfMissing(
      this.cardClassTypesSharedCollection,
      cardIssuerCharges.cardClass
    );
    this.cardChargesSharedCollection = this.cardChargesService.addCardChargesToCollectionIfMissing(
      this.cardChargesSharedCollection,
      cardIssuerCharges.cardChargeType
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

    this.cardCategoryTypeService
      .query()
      .pipe(map((res: HttpResponse<ICardCategoryType[]>) => res.body ?? []))
      .pipe(
        map((cardCategoryTypes: ICardCategoryType[]) =>
          this.cardCategoryTypeService.addCardCategoryTypeToCollectionIfMissing(cardCategoryTypes, this.editForm.get('cardCategory')!.value)
        )
      )
      .subscribe((cardCategoryTypes: ICardCategoryType[]) => (this.cardCategoryTypesSharedCollection = cardCategoryTypes));

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

    this.cardClassTypeService
      .query()
      .pipe(map((res: HttpResponse<ICardClassType[]>) => res.body ?? []))
      .pipe(
        map((cardClassTypes: ICardClassType[]) =>
          this.cardClassTypeService.addCardClassTypeToCollectionIfMissing(cardClassTypes, this.editForm.get('cardClass')!.value)
        )
      )
      .subscribe((cardClassTypes: ICardClassType[]) => (this.cardClassTypesSharedCollection = cardClassTypes));

    this.cardChargesService
      .query()
      .pipe(map((res: HttpResponse<ICardCharges[]>) => res.body ?? []))
      .pipe(
        map((cardCharges: ICardCharges[]) =>
          this.cardChargesService.addCardChargesToCollectionIfMissing(cardCharges, this.editForm.get('cardChargeType')!.value)
        )
      )
      .subscribe((cardCharges: ICardCharges[]) => (this.cardChargesSharedCollection = cardCharges));
  }

  protected createFromForm(): ICardIssuerCharges {
    return {
      ...new CardIssuerCharges(),
      id: this.editForm.get(['id'])!.value,
      reportingDate: this.editForm.get(['reportingDate'])!.value,
      cardFeeChargeInLCY: this.editForm.get(['cardFeeChargeInLCY'])!.value,
      bankCode: this.editForm.get(['bankCode'])!.value,
      cardCategory: this.editForm.get(['cardCategory'])!.value,
      cardType: this.editForm.get(['cardType'])!.value,
      cardBrand: this.editForm.get(['cardBrand'])!.value,
      cardClass: this.editForm.get(['cardClass'])!.value,
      cardChargeType: this.editForm.get(['cardChargeType'])!.value,
    };
  }
}
