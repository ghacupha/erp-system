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

jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ExchangeRateService } from '../service/exchange-rate.service';
import { IExchangeRate, ExchangeRate } from '../exchange-rate.model';
import { IInstitutionCode } from 'app/entities/gdi/institution-code/institution-code.model';
import { InstitutionCodeService } from 'app/entities/gdi/institution-code/service/institution-code.service';
import { IIsoCurrencyCode } from 'app/entities/gdi/iso-currency-code/iso-currency-code.model';
import { IsoCurrencyCodeService } from 'app/entities/gdi/iso-currency-code/service/iso-currency-code.service';

import { ExchangeRateUpdateComponent } from './exchange-rate-update.component';

describe('ExchangeRate Management Update Component', () => {
  let comp: ExchangeRateUpdateComponent;
  let fixture: ComponentFixture<ExchangeRateUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let exchangeRateService: ExchangeRateService;
  let institutionCodeService: InstitutionCodeService;
  let isoCurrencyCodeService: IsoCurrencyCodeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [ExchangeRateUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(ExchangeRateUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ExchangeRateUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    exchangeRateService = TestBed.inject(ExchangeRateService);
    institutionCodeService = TestBed.inject(InstitutionCodeService);
    isoCurrencyCodeService = TestBed.inject(IsoCurrencyCodeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call InstitutionCode query and add missing value', () => {
      const exchangeRate: IExchangeRate = { id: 456 };
      const institutionCode: IInstitutionCode = { id: 16888 };
      exchangeRate.institutionCode = institutionCode;

      const institutionCodeCollection: IInstitutionCode[] = [{ id: 52477 }];
      jest.spyOn(institutionCodeService, 'query').mockReturnValue(of(new HttpResponse({ body: institutionCodeCollection })));
      const additionalInstitutionCodes = [institutionCode];
      const expectedCollection: IInstitutionCode[] = [...additionalInstitutionCodes, ...institutionCodeCollection];
      jest.spyOn(institutionCodeService, 'addInstitutionCodeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ exchangeRate });
      comp.ngOnInit();

      expect(institutionCodeService.query).toHaveBeenCalled();
      expect(institutionCodeService.addInstitutionCodeToCollectionIfMissing).toHaveBeenCalledWith(
        institutionCodeCollection,
        ...additionalInstitutionCodes
      );
      expect(comp.institutionCodesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call IsoCurrencyCode query and add missing value', () => {
      const exchangeRate: IExchangeRate = { id: 456 };
      const currencyCode: IIsoCurrencyCode = { id: 48454 };
      exchangeRate.currencyCode = currencyCode;

      const isoCurrencyCodeCollection: IIsoCurrencyCode[] = [{ id: 54544 }];
      jest.spyOn(isoCurrencyCodeService, 'query').mockReturnValue(of(new HttpResponse({ body: isoCurrencyCodeCollection })));
      const additionalIsoCurrencyCodes = [currencyCode];
      const expectedCollection: IIsoCurrencyCode[] = [...additionalIsoCurrencyCodes, ...isoCurrencyCodeCollection];
      jest.spyOn(isoCurrencyCodeService, 'addIsoCurrencyCodeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ exchangeRate });
      comp.ngOnInit();

      expect(isoCurrencyCodeService.query).toHaveBeenCalled();
      expect(isoCurrencyCodeService.addIsoCurrencyCodeToCollectionIfMissing).toHaveBeenCalledWith(
        isoCurrencyCodeCollection,
        ...additionalIsoCurrencyCodes
      );
      expect(comp.isoCurrencyCodesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const exchangeRate: IExchangeRate = { id: 456 };
      const institutionCode: IInstitutionCode = { id: 15877 };
      exchangeRate.institutionCode = institutionCode;
      const currencyCode: IIsoCurrencyCode = { id: 22253 };
      exchangeRate.currencyCode = currencyCode;

      activatedRoute.data = of({ exchangeRate });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(exchangeRate));
      expect(comp.institutionCodesSharedCollection).toContain(institutionCode);
      expect(comp.isoCurrencyCodesSharedCollection).toContain(currencyCode);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ExchangeRate>>();
      const exchangeRate = { id: 123 };
      jest.spyOn(exchangeRateService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ exchangeRate });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: exchangeRate }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(exchangeRateService.update).toHaveBeenCalledWith(exchangeRate);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ExchangeRate>>();
      const exchangeRate = new ExchangeRate();
      jest.spyOn(exchangeRateService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ exchangeRate });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: exchangeRate }));
      saveSubject.complete();

      // THEN
      expect(exchangeRateService.create).toHaveBeenCalledWith(exchangeRate);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ExchangeRate>>();
      const exchangeRate = { id: 123 };
      jest.spyOn(exchangeRateService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ exchangeRate });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(exchangeRateService.update).toHaveBeenCalledWith(exchangeRate);
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

    describe('trackIsoCurrencyCodeById', () => {
      it('Should return tracked IsoCurrencyCode primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackIsoCurrencyCodeById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
