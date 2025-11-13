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

import { WeeklyCashHoldingService } from '../service/weekly-cash-holding.service';
import { IWeeklyCashHolding, WeeklyCashHolding } from '../weekly-cash-holding.model';
import { IInstitutionCode } from 'app/entities/gdi/institution-code/institution-code.model';
import { InstitutionCodeService } from 'app/entities/gdi/institution-code/service/institution-code.service';
import { IBankBranchCode } from 'app/entities/system/bank-branch-code/bank-branch-code.model';
import { BankBranchCodeService } from 'app/entities/system/bank-branch-code/service/bank-branch-code.service';
import { ICountySubCountyCode } from 'app/entities/gdi-data/county-sub-county-code/county-sub-county-code.model';
import { CountySubCountyCodeService } from 'app/entities/gdi-data/county-sub-county-code/service/county-sub-county-code.service';
import { IKenyanCurrencyDenomination } from 'app/entities/gdi/kenyan-currency-denomination/kenyan-currency-denomination.model';
import { KenyanCurrencyDenominationService } from 'app/entities/gdi/kenyan-currency-denomination/service/kenyan-currency-denomination.service';

import { WeeklyCashHoldingUpdateComponent } from './weekly-cash-holding-update.component';

describe('WeeklyCashHolding Management Update Component', () => {
  let comp: WeeklyCashHoldingUpdateComponent;
  let fixture: ComponentFixture<WeeklyCashHoldingUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let weeklyCashHoldingService: WeeklyCashHoldingService;
  let institutionCodeService: InstitutionCodeService;
  let bankBranchCodeService: BankBranchCodeService;
  let countySubCountyCodeService: CountySubCountyCodeService;
  let kenyanCurrencyDenominationService: KenyanCurrencyDenominationService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [WeeklyCashHoldingUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(WeeklyCashHoldingUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(WeeklyCashHoldingUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    weeklyCashHoldingService = TestBed.inject(WeeklyCashHoldingService);
    institutionCodeService = TestBed.inject(InstitutionCodeService);
    bankBranchCodeService = TestBed.inject(BankBranchCodeService);
    countySubCountyCodeService = TestBed.inject(CountySubCountyCodeService);
    kenyanCurrencyDenominationService = TestBed.inject(KenyanCurrencyDenominationService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call InstitutionCode query and add missing value', () => {
      const weeklyCashHolding: IWeeklyCashHolding = { id: 456 };
      const bankCode: IInstitutionCode = { id: 85278 };
      weeklyCashHolding.bankCode = bankCode;

      const institutionCodeCollection: IInstitutionCode[] = [{ id: 85101 }];
      jest.spyOn(institutionCodeService, 'query').mockReturnValue(of(new HttpResponse({ body: institutionCodeCollection })));
      const additionalInstitutionCodes = [bankCode];
      const expectedCollection: IInstitutionCode[] = [...additionalInstitutionCodes, ...institutionCodeCollection];
      jest.spyOn(institutionCodeService, 'addInstitutionCodeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ weeklyCashHolding });
      comp.ngOnInit();

      expect(institutionCodeService.query).toHaveBeenCalled();
      expect(institutionCodeService.addInstitutionCodeToCollectionIfMissing).toHaveBeenCalledWith(
        institutionCodeCollection,
        ...additionalInstitutionCodes
      );
      expect(comp.institutionCodesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call BankBranchCode query and add missing value', () => {
      const weeklyCashHolding: IWeeklyCashHolding = { id: 456 };
      const branchId: IBankBranchCode = { id: 48166 };
      weeklyCashHolding.branchId = branchId;

      const bankBranchCodeCollection: IBankBranchCode[] = [{ id: 58362 }];
      jest.spyOn(bankBranchCodeService, 'query').mockReturnValue(of(new HttpResponse({ body: bankBranchCodeCollection })));
      const additionalBankBranchCodes = [branchId];
      const expectedCollection: IBankBranchCode[] = [...additionalBankBranchCodes, ...bankBranchCodeCollection];
      jest.spyOn(bankBranchCodeService, 'addBankBranchCodeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ weeklyCashHolding });
      comp.ngOnInit();

      expect(bankBranchCodeService.query).toHaveBeenCalled();
      expect(bankBranchCodeService.addBankBranchCodeToCollectionIfMissing).toHaveBeenCalledWith(
        bankBranchCodeCollection,
        ...additionalBankBranchCodes
      );
      expect(comp.bankBranchCodesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call CountySubCountyCode query and add missing value', () => {
      const weeklyCashHolding: IWeeklyCashHolding = { id: 456 };
      const subCountyCode: ICountySubCountyCode = { id: 2051 };
      weeklyCashHolding.subCountyCode = subCountyCode;

      const countySubCountyCodeCollection: ICountySubCountyCode[] = [{ id: 91186 }];
      jest.spyOn(countySubCountyCodeService, 'query').mockReturnValue(of(new HttpResponse({ body: countySubCountyCodeCollection })));
      const additionalCountySubCountyCodes = [subCountyCode];
      const expectedCollection: ICountySubCountyCode[] = [...additionalCountySubCountyCodes, ...countySubCountyCodeCollection];
      jest.spyOn(countySubCountyCodeService, 'addCountySubCountyCodeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ weeklyCashHolding });
      comp.ngOnInit();

      expect(countySubCountyCodeService.query).toHaveBeenCalled();
      expect(countySubCountyCodeService.addCountySubCountyCodeToCollectionIfMissing).toHaveBeenCalledWith(
        countySubCountyCodeCollection,
        ...additionalCountySubCountyCodes
      );
      expect(comp.countySubCountyCodesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call KenyanCurrencyDenomination query and add missing value', () => {
      const weeklyCashHolding: IWeeklyCashHolding = { id: 456 };
      const denomination: IKenyanCurrencyDenomination = { id: 33824 };
      weeklyCashHolding.denomination = denomination;

      const kenyanCurrencyDenominationCollection: IKenyanCurrencyDenomination[] = [{ id: 95641 }];
      jest
        .spyOn(kenyanCurrencyDenominationService, 'query')
        .mockReturnValue(of(new HttpResponse({ body: kenyanCurrencyDenominationCollection })));
      const additionalKenyanCurrencyDenominations = [denomination];
      const expectedCollection: IKenyanCurrencyDenomination[] = [
        ...additionalKenyanCurrencyDenominations,
        ...kenyanCurrencyDenominationCollection,
      ];
      jest
        .spyOn(kenyanCurrencyDenominationService, 'addKenyanCurrencyDenominationToCollectionIfMissing')
        .mockReturnValue(expectedCollection);

      activatedRoute.data = of({ weeklyCashHolding });
      comp.ngOnInit();

      expect(kenyanCurrencyDenominationService.query).toHaveBeenCalled();
      expect(kenyanCurrencyDenominationService.addKenyanCurrencyDenominationToCollectionIfMissing).toHaveBeenCalledWith(
        kenyanCurrencyDenominationCollection,
        ...additionalKenyanCurrencyDenominations
      );
      expect(comp.kenyanCurrencyDenominationsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const weeklyCashHolding: IWeeklyCashHolding = { id: 456 };
      const bankCode: IInstitutionCode = { id: 74367 };
      weeklyCashHolding.bankCode = bankCode;
      const branchId: IBankBranchCode = { id: 61226 };
      weeklyCashHolding.branchId = branchId;
      const subCountyCode: ICountySubCountyCode = { id: 85836 };
      weeklyCashHolding.subCountyCode = subCountyCode;
      const denomination: IKenyanCurrencyDenomination = { id: 84149 };
      weeklyCashHolding.denomination = denomination;

      activatedRoute.data = of({ weeklyCashHolding });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(weeklyCashHolding));
      expect(comp.institutionCodesSharedCollection).toContain(bankCode);
      expect(comp.bankBranchCodesSharedCollection).toContain(branchId);
      expect(comp.countySubCountyCodesSharedCollection).toContain(subCountyCode);
      expect(comp.kenyanCurrencyDenominationsSharedCollection).toContain(denomination);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<WeeklyCashHolding>>();
      const weeklyCashHolding = { id: 123 };
      jest.spyOn(weeklyCashHoldingService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ weeklyCashHolding });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: weeklyCashHolding }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(weeklyCashHoldingService.update).toHaveBeenCalledWith(weeklyCashHolding);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<WeeklyCashHolding>>();
      const weeklyCashHolding = new WeeklyCashHolding();
      jest.spyOn(weeklyCashHoldingService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ weeklyCashHolding });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: weeklyCashHolding }));
      saveSubject.complete();

      // THEN
      expect(weeklyCashHoldingService.create).toHaveBeenCalledWith(weeklyCashHolding);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<WeeklyCashHolding>>();
      const weeklyCashHolding = { id: 123 };
      jest.spyOn(weeklyCashHoldingService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ weeklyCashHolding });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(weeklyCashHoldingService.update).toHaveBeenCalledWith(weeklyCashHolding);
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

    describe('trackBankBranchCodeById', () => {
      it('Should return tracked BankBranchCode primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackBankBranchCodeById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackCountySubCountyCodeById', () => {
      it('Should return tracked CountySubCountyCode primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackCountySubCountyCodeById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackKenyanCurrencyDenominationById', () => {
      it('Should return tracked KenyanCurrencyDenomination primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackKenyanCurrencyDenominationById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
