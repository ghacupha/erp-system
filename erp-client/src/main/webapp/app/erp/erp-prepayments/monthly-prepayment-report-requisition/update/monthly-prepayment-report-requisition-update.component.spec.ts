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

import { FiscalYearService } from '../../../erp-pages/fiscal-year/service/fiscal-year.service';

jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { MonthlyPrepaymentReportRequisitionService } from '../service/monthly-prepayment-report-requisition.service';
import { IMonthlyPrepaymentReportRequisition, MonthlyPrepaymentReportRequisition } from '../monthly-prepayment-report-requisition.model';

import { MonthlyPrepaymentReportRequisitionUpdateComponent } from './monthly-prepayment-report-requisition-update.component';
import { IFiscalYear } from '../../../erp-pages/fiscal-year/fiscal-year.model';

describe('MonthlyPrepaymentReportRequisition Management Update Component', () => {
  let comp: MonthlyPrepaymentReportRequisitionUpdateComponent;
  let fixture: ComponentFixture<MonthlyPrepaymentReportRequisitionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let monthlyPrepaymentReportRequisitionService: MonthlyPrepaymentReportRequisitionService;
  let fiscalYearService: FiscalYearService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [MonthlyPrepaymentReportRequisitionUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(MonthlyPrepaymentReportRequisitionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MonthlyPrepaymentReportRequisitionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    monthlyPrepaymentReportRequisitionService = TestBed.inject(MonthlyPrepaymentReportRequisitionService);
    fiscalYearService = TestBed.inject(FiscalYearService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call FiscalYear query and add missing value', () => {
      const monthlyPrepaymentReportRequisition: IMonthlyPrepaymentReportRequisition = { id: 456 };
      const fiscalYear: IFiscalYear = { id: 22793 };
      monthlyPrepaymentReportRequisition.fiscalYear = fiscalYear;

      const fiscalYearCollection: IFiscalYear[] = [{ id: 40252 }];
      jest.spyOn(fiscalYearService, 'query').mockReturnValue(of(new HttpResponse({ body: fiscalYearCollection })));
      const additionalFiscalYears = [fiscalYear];
      const expectedCollection: IFiscalYear[] = [...additionalFiscalYears, ...fiscalYearCollection];
      jest.spyOn(fiscalYearService, 'addFiscalYearToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ monthlyPrepaymentReportRequisition });
      comp.ngOnInit();

      expect(fiscalYearService.query).toHaveBeenCalled();
      expect(fiscalYearService.addFiscalYearToCollectionIfMissing).toHaveBeenCalledWith(fiscalYearCollection, ...additionalFiscalYears);
      expect(comp.fiscalYearsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const monthlyPrepaymentReportRequisition: IMonthlyPrepaymentReportRequisition = { id: 456 };
      const fiscalYear: IFiscalYear = { id: 45341 };
      monthlyPrepaymentReportRequisition.fiscalYear = fiscalYear;

      activatedRoute.data = of({ monthlyPrepaymentReportRequisition });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(monthlyPrepaymentReportRequisition));
      expect(comp.fiscalYearsSharedCollection).toContain(fiscalYear);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<MonthlyPrepaymentReportRequisition>>();
      const monthlyPrepaymentReportRequisition = { id: 123 };
      jest.spyOn(monthlyPrepaymentReportRequisitionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ monthlyPrepaymentReportRequisition });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: monthlyPrepaymentReportRequisition }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(monthlyPrepaymentReportRequisitionService.update).toHaveBeenCalledWith(monthlyPrepaymentReportRequisition);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<MonthlyPrepaymentReportRequisition>>();
      const monthlyPrepaymentReportRequisition = new MonthlyPrepaymentReportRequisition();
      jest.spyOn(monthlyPrepaymentReportRequisitionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ monthlyPrepaymentReportRequisition });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: monthlyPrepaymentReportRequisition }));
      saveSubject.complete();

      // THEN
      expect(monthlyPrepaymentReportRequisitionService.create).toHaveBeenCalledWith(monthlyPrepaymentReportRequisition);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<MonthlyPrepaymentReportRequisition>>();
      const monthlyPrepaymentReportRequisition = { id: 123 };
      jest.spyOn(monthlyPrepaymentReportRequisitionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ monthlyPrepaymentReportRequisition });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(monthlyPrepaymentReportRequisitionService.update).toHaveBeenCalledWith(monthlyPrepaymentReportRequisition);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackFiscalYearById', () => {
      it('Should return tracked FiscalYear primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackFiscalYearById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
