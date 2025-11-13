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

jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { CardIssuerChargesService } from '../service/card-issuer-charges.service';
import { ICardIssuerCharges, CardIssuerCharges } from '../card-issuer-charges.model';
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

import { CardIssuerChargesUpdateComponent } from './card-issuer-charges-update.component';

describe('CardIssuerCharges Management Update Component', () => {
  let comp: CardIssuerChargesUpdateComponent;
  let fixture: ComponentFixture<CardIssuerChargesUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let cardIssuerChargesService: CardIssuerChargesService;
  let institutionCodeService: InstitutionCodeService;
  let cardCategoryTypeService: CardCategoryTypeService;
  let cardTypesService: CardTypesService;
  let cardBrandTypeService: CardBrandTypeService;
  let cardClassTypeService: CardClassTypeService;
  let cardChargesService: CardChargesService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [CardIssuerChargesUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(CardIssuerChargesUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CardIssuerChargesUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    cardIssuerChargesService = TestBed.inject(CardIssuerChargesService);
    institutionCodeService = TestBed.inject(InstitutionCodeService);
    cardCategoryTypeService = TestBed.inject(CardCategoryTypeService);
    cardTypesService = TestBed.inject(CardTypesService);
    cardBrandTypeService = TestBed.inject(CardBrandTypeService);
    cardClassTypeService = TestBed.inject(CardClassTypeService);
    cardChargesService = TestBed.inject(CardChargesService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call InstitutionCode query and add missing value', () => {
      const cardIssuerCharges: ICardIssuerCharges = { id: 456 };
      const bankCode: IInstitutionCode = { id: 9240 };
      cardIssuerCharges.bankCode = bankCode;

      const institutionCodeCollection: IInstitutionCode[] = [{ id: 232 }];
      jest.spyOn(institutionCodeService, 'query').mockReturnValue(of(new HttpResponse({ body: institutionCodeCollection })));
      const additionalInstitutionCodes = [bankCode];
      const expectedCollection: IInstitutionCode[] = [...additionalInstitutionCodes, ...institutionCodeCollection];
      jest.spyOn(institutionCodeService, 'addInstitutionCodeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ cardIssuerCharges });
      comp.ngOnInit();

      expect(institutionCodeService.query).toHaveBeenCalled();
      expect(institutionCodeService.addInstitutionCodeToCollectionIfMissing).toHaveBeenCalledWith(
        institutionCodeCollection,
        ...additionalInstitutionCodes
      );
      expect(comp.institutionCodesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call CardCategoryType query and add missing value', () => {
      const cardIssuerCharges: ICardIssuerCharges = { id: 456 };
      const cardCategory: ICardCategoryType = { id: 15958 };
      cardIssuerCharges.cardCategory = cardCategory;

      const cardCategoryTypeCollection: ICardCategoryType[] = [{ id: 49363 }];
      jest.spyOn(cardCategoryTypeService, 'query').mockReturnValue(of(new HttpResponse({ body: cardCategoryTypeCollection })));
      const additionalCardCategoryTypes = [cardCategory];
      const expectedCollection: ICardCategoryType[] = [...additionalCardCategoryTypes, ...cardCategoryTypeCollection];
      jest.spyOn(cardCategoryTypeService, 'addCardCategoryTypeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ cardIssuerCharges });
      comp.ngOnInit();

      expect(cardCategoryTypeService.query).toHaveBeenCalled();
      expect(cardCategoryTypeService.addCardCategoryTypeToCollectionIfMissing).toHaveBeenCalledWith(
        cardCategoryTypeCollection,
        ...additionalCardCategoryTypes
      );
      expect(comp.cardCategoryTypesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call CardTypes query and add missing value', () => {
      const cardIssuerCharges: ICardIssuerCharges = { id: 456 };
      const cardType: ICardTypes = { id: 97282 };
      cardIssuerCharges.cardType = cardType;

      const cardTypesCollection: ICardTypes[] = [{ id: 48862 }];
      jest.spyOn(cardTypesService, 'query').mockReturnValue(of(new HttpResponse({ body: cardTypesCollection })));
      const additionalCardTypes = [cardType];
      const expectedCollection: ICardTypes[] = [...additionalCardTypes, ...cardTypesCollection];
      jest.spyOn(cardTypesService, 'addCardTypesToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ cardIssuerCharges });
      comp.ngOnInit();

      expect(cardTypesService.query).toHaveBeenCalled();
      expect(cardTypesService.addCardTypesToCollectionIfMissing).toHaveBeenCalledWith(cardTypesCollection, ...additionalCardTypes);
      expect(comp.cardTypesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call CardBrandType query and add missing value', () => {
      const cardIssuerCharges: ICardIssuerCharges = { id: 456 };
      const cardBrand: ICardBrandType = { id: 75735 };
      cardIssuerCharges.cardBrand = cardBrand;

      const cardBrandTypeCollection: ICardBrandType[] = [{ id: 11149 }];
      jest.spyOn(cardBrandTypeService, 'query').mockReturnValue(of(new HttpResponse({ body: cardBrandTypeCollection })));
      const additionalCardBrandTypes = [cardBrand];
      const expectedCollection: ICardBrandType[] = [...additionalCardBrandTypes, ...cardBrandTypeCollection];
      jest.spyOn(cardBrandTypeService, 'addCardBrandTypeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ cardIssuerCharges });
      comp.ngOnInit();

      expect(cardBrandTypeService.query).toHaveBeenCalled();
      expect(cardBrandTypeService.addCardBrandTypeToCollectionIfMissing).toHaveBeenCalledWith(
        cardBrandTypeCollection,
        ...additionalCardBrandTypes
      );
      expect(comp.cardBrandTypesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call CardClassType query and add missing value', () => {
      const cardIssuerCharges: ICardIssuerCharges = { id: 456 };
      const cardClass: ICardClassType = { id: 72540 };
      cardIssuerCharges.cardClass = cardClass;

      const cardClassTypeCollection: ICardClassType[] = [{ id: 73550 }];
      jest.spyOn(cardClassTypeService, 'query').mockReturnValue(of(new HttpResponse({ body: cardClassTypeCollection })));
      const additionalCardClassTypes = [cardClass];
      const expectedCollection: ICardClassType[] = [...additionalCardClassTypes, ...cardClassTypeCollection];
      jest.spyOn(cardClassTypeService, 'addCardClassTypeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ cardIssuerCharges });
      comp.ngOnInit();

      expect(cardClassTypeService.query).toHaveBeenCalled();
      expect(cardClassTypeService.addCardClassTypeToCollectionIfMissing).toHaveBeenCalledWith(
        cardClassTypeCollection,
        ...additionalCardClassTypes
      );
      expect(comp.cardClassTypesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call CardCharges query and add missing value', () => {
      const cardIssuerCharges: ICardIssuerCharges = { id: 456 };
      const cardChargeType: ICardCharges = { id: 35311 };
      cardIssuerCharges.cardChargeType = cardChargeType;

      const cardChargesCollection: ICardCharges[] = [{ id: 12174 }];
      jest.spyOn(cardChargesService, 'query').mockReturnValue(of(new HttpResponse({ body: cardChargesCollection })));
      const additionalCardCharges = [cardChargeType];
      const expectedCollection: ICardCharges[] = [...additionalCardCharges, ...cardChargesCollection];
      jest.spyOn(cardChargesService, 'addCardChargesToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ cardIssuerCharges });
      comp.ngOnInit();

      expect(cardChargesService.query).toHaveBeenCalled();
      expect(cardChargesService.addCardChargesToCollectionIfMissing).toHaveBeenCalledWith(cardChargesCollection, ...additionalCardCharges);
      expect(comp.cardChargesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const cardIssuerCharges: ICardIssuerCharges = { id: 456 };
      const bankCode: IInstitutionCode = { id: 19837 };
      cardIssuerCharges.bankCode = bankCode;
      const cardCategory: ICardCategoryType = { id: 44341 };
      cardIssuerCharges.cardCategory = cardCategory;
      const cardType: ICardTypes = { id: 1208 };
      cardIssuerCharges.cardType = cardType;
      const cardBrand: ICardBrandType = { id: 35802 };
      cardIssuerCharges.cardBrand = cardBrand;
      const cardClass: ICardClassType = { id: 93193 };
      cardIssuerCharges.cardClass = cardClass;
      const cardChargeType: ICardCharges = { id: 23118 };
      cardIssuerCharges.cardChargeType = cardChargeType;

      activatedRoute.data = of({ cardIssuerCharges });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(cardIssuerCharges));
      expect(comp.institutionCodesSharedCollection).toContain(bankCode);
      expect(comp.cardCategoryTypesSharedCollection).toContain(cardCategory);
      expect(comp.cardTypesSharedCollection).toContain(cardType);
      expect(comp.cardBrandTypesSharedCollection).toContain(cardBrand);
      expect(comp.cardClassTypesSharedCollection).toContain(cardClass);
      expect(comp.cardChargesSharedCollection).toContain(cardChargeType);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CardIssuerCharges>>();
      const cardIssuerCharges = { id: 123 };
      jest.spyOn(cardIssuerChargesService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cardIssuerCharges });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: cardIssuerCharges }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(cardIssuerChargesService.update).toHaveBeenCalledWith(cardIssuerCharges);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CardIssuerCharges>>();
      const cardIssuerCharges = new CardIssuerCharges();
      jest.spyOn(cardIssuerChargesService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cardIssuerCharges });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: cardIssuerCharges }));
      saveSubject.complete();

      // THEN
      expect(cardIssuerChargesService.create).toHaveBeenCalledWith(cardIssuerCharges);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CardIssuerCharges>>();
      const cardIssuerCharges = { id: 123 };
      jest.spyOn(cardIssuerChargesService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cardIssuerCharges });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(cardIssuerChargesService.update).toHaveBeenCalledWith(cardIssuerCharges);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackInstitutionCodeById', () => {
      it('Should return tracked InstitutionCode primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackInstitutionCodeById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackCardCategoryTypeById', () => {
      it('Should return tracked CardCategoryType primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackCardCategoryTypeById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackCardTypesById', () => {
      it('Should return tracked CardTypes primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackCardTypesById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackCardBrandTypeById', () => {
      it('Should return tracked CardBrandType primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackCardBrandTypeById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackCardClassTypeById', () => {
      it('Should return tracked CardClassType primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackCardClassTypeById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackCardChargesById', () => {
      it('Should return tracked CardCharges primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackCardChargesById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
