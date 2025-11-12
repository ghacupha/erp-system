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

import { AmortizationPeriodService } from '../service/amortization-period.service';
import { IAmortizationPeriod, AmortizationPeriod } from '../amortization-period.model';
import { IFiscalMonth } from 'app/entities/system/fiscal-month/fiscal-month.model';
import { FiscalMonthService } from 'app/entities/system/fiscal-month/service/fiscal-month.service';

import { AmortizationPeriodUpdateComponent } from './amortization-period-update.component';

describe('AmortizationPeriod Management Update Component', () => {
  let comp: AmortizationPeriodUpdateComponent;
  let fixture: ComponentFixture<AmortizationPeriodUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let amortizationPeriodService: AmortizationPeriodService;
  let fiscalMonthService: FiscalMonthService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [AmortizationPeriodUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(AmortizationPeriodUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AmortizationPeriodUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    amortizationPeriodService = TestBed.inject(AmortizationPeriodService);
    fiscalMonthService = TestBed.inject(FiscalMonthService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call FiscalMonth query and add missing value', () => {
      const amortizationPeriod: IAmortizationPeriod = { id: 456 };
      const fiscalMonth: IFiscalMonth = { id: 88354 };
      amortizationPeriod.fiscalMonth = fiscalMonth;

      const fiscalMonthCollection: IFiscalMonth[] = [{ id: 44755 }];
      jest.spyOn(fiscalMonthService, 'query').mockReturnValue(of(new HttpResponse({ body: fiscalMonthCollection })));
      const additionalFiscalMonths = [fiscalMonth];
      const expectedCollection: IFiscalMonth[] = [...additionalFiscalMonths, ...fiscalMonthCollection];
      jest.spyOn(fiscalMonthService, 'addFiscalMonthToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ amortizationPeriod });
      comp.ngOnInit();

      expect(fiscalMonthService.query).toHaveBeenCalled();
      expect(fiscalMonthService.addFiscalMonthToCollectionIfMissing).toHaveBeenCalledWith(fiscalMonthCollection, ...additionalFiscalMonths);
      expect(comp.fiscalMonthsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call AmortizationPeriod query and add missing value', () => {
      const amortizationPeriod: IAmortizationPeriod = { id: 456 };
      const amortizationPeriod: IAmortizationPeriod = { id: 80278 };
      amortizationPeriod.amortizationPeriod = amortizationPeriod;

      const amortizationPeriodCollection: IAmortizationPeriod[] = [{ id: 45822 }];
      jest.spyOn(amortizationPeriodService, 'query').mockReturnValue(of(new HttpResponse({ body: amortizationPeriodCollection })));
      const additionalAmortizationPeriods = [amortizationPeriod];
      const expectedCollection: IAmortizationPeriod[] = [...additionalAmortizationPeriods, ...amortizationPeriodCollection];
      jest.spyOn(amortizationPeriodService, 'addAmortizationPeriodToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ amortizationPeriod });
      comp.ngOnInit();

      expect(amortizationPeriodService.query).toHaveBeenCalled();
      expect(amortizationPeriodService.addAmortizationPeriodToCollectionIfMissing).toHaveBeenCalledWith(
        amortizationPeriodCollection,
        ...additionalAmortizationPeriods
      );
      expect(comp.amortizationPeriodsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const amortizationPeriod: IAmortizationPeriod = { id: 456 };
      const fiscalMonth: IFiscalMonth = { id: 67395 };
      amortizationPeriod.fiscalMonth = fiscalMonth;
      const amortizationPeriod: IAmortizationPeriod = { id: 36232 };
      amortizationPeriod.amortizationPeriod = amortizationPeriod;

      activatedRoute.data = of({ amortizationPeriod });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(amortizationPeriod));
      expect(comp.fiscalMonthsSharedCollection).toContain(fiscalMonth);
      expect(comp.amortizationPeriodsSharedCollection).toContain(amortizationPeriod);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<AmortizationPeriod>>();
      const amortizationPeriod = { id: 123 };
      jest.spyOn(amortizationPeriodService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ amortizationPeriod });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: amortizationPeriod }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(amortizationPeriodService.update).toHaveBeenCalledWith(amortizationPeriod);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<AmortizationPeriod>>();
      const amortizationPeriod = new AmortizationPeriod();
      jest.spyOn(amortizationPeriodService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ amortizationPeriod });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: amortizationPeriod }));
      saveSubject.complete();

      // THEN
      expect(amortizationPeriodService.create).toHaveBeenCalledWith(amortizationPeriod);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<AmortizationPeriod>>();
      const amortizationPeriod = { id: 123 };
      jest.spyOn(amortizationPeriodService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ amortizationPeriod });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(amortizationPeriodService.update).toHaveBeenCalledWith(amortizationPeriod);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackFiscalMonthById', () => {
      it('Should return tracked FiscalMonth primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackFiscalMonthById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackAmortizationPeriodById', () => {
      it('Should return tracked AmortizationPeriod primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackAmortizationPeriodById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
