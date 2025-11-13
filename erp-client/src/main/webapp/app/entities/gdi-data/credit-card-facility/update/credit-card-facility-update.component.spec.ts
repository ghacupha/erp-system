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

import { CreditCardFacilityService } from '../service/credit-card-facility.service';
import { ICreditCardFacility, CreditCardFacility } from '../credit-card-facility.model';
import { IInstitutionCode } from 'app/entities/gdi/institution-code/institution-code.model';
import { InstitutionCodeService } from 'app/entities/gdi/institution-code/service/institution-code.service';
import { ICreditCardOwnership } from 'app/entities/gdi/credit-card-ownership/credit-card-ownership.model';
import { CreditCardOwnershipService } from 'app/entities/gdi/credit-card-ownership/service/credit-card-ownership.service';
import { IIsoCurrencyCode } from 'app/entities/gdi/iso-currency-code/iso-currency-code.model';
import { IsoCurrencyCodeService } from 'app/entities/gdi/iso-currency-code/service/iso-currency-code.service';

import { CreditCardFacilityUpdateComponent } from './credit-card-facility-update.component';

describe('CreditCardFacility Management Update Component', () => {
  let comp: CreditCardFacilityUpdateComponent;
  let fixture: ComponentFixture<CreditCardFacilityUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let creditCardFacilityService: CreditCardFacilityService;
  let institutionCodeService: InstitutionCodeService;
  let creditCardOwnershipService: CreditCardOwnershipService;
  let isoCurrencyCodeService: IsoCurrencyCodeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [CreditCardFacilityUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(CreditCardFacilityUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CreditCardFacilityUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    creditCardFacilityService = TestBed.inject(CreditCardFacilityService);
    institutionCodeService = TestBed.inject(InstitutionCodeService);
    creditCardOwnershipService = TestBed.inject(CreditCardOwnershipService);
    isoCurrencyCodeService = TestBed.inject(IsoCurrencyCodeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call InstitutionCode query and add missing value', () => {
      const creditCardFacility: ICreditCardFacility = { id: 456 };
      const bankCode: IInstitutionCode = { id: 90143 };
      creditCardFacility.bankCode = bankCode;

      const institutionCodeCollection: IInstitutionCode[] = [{ id: 41372 }];
      jest.spyOn(institutionCodeService, 'query').mockReturnValue(of(new HttpResponse({ body: institutionCodeCollection })));
      const additionalInstitutionCodes = [bankCode];
      const expectedCollection: IInstitutionCode[] = [...additionalInstitutionCodes, ...institutionCodeCollection];
      jest.spyOn(institutionCodeService, 'addInstitutionCodeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ creditCardFacility });
      comp.ngOnInit();

      expect(institutionCodeService.query).toHaveBeenCalled();
      expect(institutionCodeService.addInstitutionCodeToCollectionIfMissing).toHaveBeenCalledWith(
        institutionCodeCollection,
        ...additionalInstitutionCodes
      );
      expect(comp.institutionCodesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call CreditCardOwnership query and add missing value', () => {
      const creditCardFacility: ICreditCardFacility = { id: 456 };
      const customerCategory: ICreditCardOwnership = { id: 6691 };
      creditCardFacility.customerCategory = customerCategory;

      const creditCardOwnershipCollection: ICreditCardOwnership[] = [{ id: 62369 }];
      jest.spyOn(creditCardOwnershipService, 'query').mockReturnValue(of(new HttpResponse({ body: creditCardOwnershipCollection })));
      const additionalCreditCardOwnerships = [customerCategory];
      const expectedCollection: ICreditCardOwnership[] = [...additionalCreditCardOwnerships, ...creditCardOwnershipCollection];
      jest.spyOn(creditCardOwnershipService, 'addCreditCardOwnershipToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ creditCardFacility });
      comp.ngOnInit();

      expect(creditCardOwnershipService.query).toHaveBeenCalled();
      expect(creditCardOwnershipService.addCreditCardOwnershipToCollectionIfMissing).toHaveBeenCalledWith(
        creditCardOwnershipCollection,
        ...additionalCreditCardOwnerships
      );
      expect(comp.creditCardOwnershipsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call IsoCurrencyCode query and add missing value', () => {
      const creditCardFacility: ICreditCardFacility = { id: 456 };
      const currencyCode: IIsoCurrencyCode = { id: 66431 };
      creditCardFacility.currencyCode = currencyCode;

      const isoCurrencyCodeCollection: IIsoCurrencyCode[] = [{ id: 22472 }];
      jest.spyOn(isoCurrencyCodeService, 'query').mockReturnValue(of(new HttpResponse({ body: isoCurrencyCodeCollection })));
      const additionalIsoCurrencyCodes = [currencyCode];
      const expectedCollection: IIsoCurrencyCode[] = [...additionalIsoCurrencyCodes, ...isoCurrencyCodeCollection];
      jest.spyOn(isoCurrencyCodeService, 'addIsoCurrencyCodeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ creditCardFacility });
      comp.ngOnInit();

      expect(isoCurrencyCodeService.query).toHaveBeenCalled();
      expect(isoCurrencyCodeService.addIsoCurrencyCodeToCollectionIfMissing).toHaveBeenCalledWith(
        isoCurrencyCodeCollection,
        ...additionalIsoCurrencyCodes
      );
      expect(comp.isoCurrencyCodesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const creditCardFacility: ICreditCardFacility = { id: 456 };
      const bankCode: IInstitutionCode = { id: 41697 };
      creditCardFacility.bankCode = bankCode;
      const customerCategory: ICreditCardOwnership = { id: 19758 };
      creditCardFacility.customerCategory = customerCategory;
      const currencyCode: IIsoCurrencyCode = { id: 54796 };
      creditCardFacility.currencyCode = currencyCode;

      activatedRoute.data = of({ creditCardFacility });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(creditCardFacility));
      expect(comp.institutionCodesSharedCollection).toContain(bankCode);
      expect(comp.creditCardOwnershipsSharedCollection).toContain(customerCategory);
      expect(comp.isoCurrencyCodesSharedCollection).toContain(currencyCode);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CreditCardFacility>>();
      const creditCardFacility = { id: 123 };
      jest.spyOn(creditCardFacilityService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ creditCardFacility });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: creditCardFacility }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(creditCardFacilityService.update).toHaveBeenCalledWith(creditCardFacility);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CreditCardFacility>>();
      const creditCardFacility = new CreditCardFacility();
      jest.spyOn(creditCardFacilityService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ creditCardFacility });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: creditCardFacility }));
      saveSubject.complete();

      // THEN
      expect(creditCardFacilityService.create).toHaveBeenCalledWith(creditCardFacility);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CreditCardFacility>>();
      const creditCardFacility = { id: 123 };
      jest.spyOn(creditCardFacilityService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ creditCardFacility });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(creditCardFacilityService.update).toHaveBeenCalledWith(creditCardFacility);
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

    describe('trackCreditCardOwnershipById', () => {
      it('Should return tracked CreditCardOwnership primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackCreditCardOwnershipById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackIsoCurrencyCodeById', () => {
      it('Should return tracked IsoCurrencyCode primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackIsoCurrencyCodeById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
