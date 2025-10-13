///
/// Erp System - Mark X No 10 (Jehoiada Series) Client 1.7.8
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

import { ApplicationUserService } from '../../../erp-pages/application-user/service/application-user.service';

jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { RouMonthlyDepreciationReportService } from '../service/rou-monthly-depreciation-report.service';
import { IRouMonthlyDepreciationReport, RouMonthlyDepreciationReport } from '../rou-monthly-depreciation-report.model';

import { RouMonthlyDepreciationReportUpdateComponent } from './rou-monthly-depreciation-report-update.component';
import { FiscalYearService } from '../../../erp-pages/fiscal-year/service/fiscal-year.service';
import { IApplicationUser } from '../../../erp-pages/application-user/application-user.model';
import { IFiscalYear } from '../../../erp-pages/fiscal-year/fiscal-year.model';

describe('RouMonthlyDepreciationReport Management Update Component', () => {
  let comp: RouMonthlyDepreciationReportUpdateComponent;
  let fixture: ComponentFixture<RouMonthlyDepreciationReportUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let rouMonthlyDepreciationReportService: RouMonthlyDepreciationReportService;
  let applicationUserService: ApplicationUserService;
  let fiscalYearService: FiscalYearService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [RouMonthlyDepreciationReportUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(RouMonthlyDepreciationReportUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(RouMonthlyDepreciationReportUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    rouMonthlyDepreciationReportService = TestBed.inject(RouMonthlyDepreciationReportService);
    applicationUserService = TestBed.inject(ApplicationUserService);
    fiscalYearService = TestBed.inject(FiscalYearService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call ApplicationUser query and add missing value', () => {
      const rouMonthlyDepreciationReport: IRouMonthlyDepreciationReport = { id: 456 };
      const requestedBy: IApplicationUser = { id: 51833 };
      rouMonthlyDepreciationReport.requestedBy = requestedBy;

      const applicationUserCollection: IApplicationUser[] = [{ id: 6327 }];
      jest.spyOn(applicationUserService, 'query').mockReturnValue(of(new HttpResponse({ body: applicationUserCollection })));
      const additionalApplicationUsers = [requestedBy];
      const expectedCollection: IApplicationUser[] = [...additionalApplicationUsers, ...applicationUserCollection];
      jest.spyOn(applicationUserService, 'addApplicationUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ rouMonthlyDepreciationReport });
      comp.ngOnInit();

      expect(applicationUserService.query).toHaveBeenCalled();
      expect(applicationUserService.addApplicationUserToCollectionIfMissing).toHaveBeenCalledWith(
        applicationUserCollection,
        ...additionalApplicationUsers
      );
      expect(comp.applicationUsersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call FiscalYear query and add missing value', () => {
      const rouMonthlyDepreciationReport: IRouMonthlyDepreciationReport = { id: 456 };
      const reportingYear: IFiscalYear = { id: 68034 };
      rouMonthlyDepreciationReport.reportingYear = reportingYear;

      const fiscalYearCollection: IFiscalYear[] = [{ id: 84116 }];
      jest.spyOn(fiscalYearService, 'query').mockReturnValue(of(new HttpResponse({ body: fiscalYearCollection })));
      const additionalFiscalYears = [reportingYear];
      const expectedCollection: IFiscalYear[] = [...additionalFiscalYears, ...fiscalYearCollection];
      jest.spyOn(fiscalYearService, 'addFiscalYearToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ rouMonthlyDepreciationReport });
      comp.ngOnInit();

      expect(fiscalYearService.query).toHaveBeenCalled();
      expect(fiscalYearService.addFiscalYearToCollectionIfMissing).toHaveBeenCalledWith(fiscalYearCollection, ...additionalFiscalYears);
      expect(comp.fiscalYearsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const rouMonthlyDepreciationReport: IRouMonthlyDepreciationReport = { id: 456 };
      const requestedBy: IApplicationUser = { id: 25056 };
      rouMonthlyDepreciationReport.requestedBy = requestedBy;
      const reportingYear: IFiscalYear = { id: 66767 };
      rouMonthlyDepreciationReport.reportingYear = reportingYear;

      activatedRoute.data = of({ rouMonthlyDepreciationReport });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(rouMonthlyDepreciationReport));
      expect(comp.applicationUsersSharedCollection).toContain(requestedBy);
      expect(comp.fiscalYearsSharedCollection).toContain(reportingYear);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<RouMonthlyDepreciationReport>>();
      const rouMonthlyDepreciationReport = { id: 123 };
      jest.spyOn(rouMonthlyDepreciationReportService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ rouMonthlyDepreciationReport });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: rouMonthlyDepreciationReport }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(rouMonthlyDepreciationReportService.update).toHaveBeenCalledWith(rouMonthlyDepreciationReport);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<RouMonthlyDepreciationReport>>();
      const rouMonthlyDepreciationReport = new RouMonthlyDepreciationReport();
      jest.spyOn(rouMonthlyDepreciationReportService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ rouMonthlyDepreciationReport });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: rouMonthlyDepreciationReport }));
      saveSubject.complete();

      // THEN
      expect(rouMonthlyDepreciationReportService.create).toHaveBeenCalledWith(rouMonthlyDepreciationReport);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<RouMonthlyDepreciationReport>>();
      const rouMonthlyDepreciationReport = { id: 123 };
      jest.spyOn(rouMonthlyDepreciationReportService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ rouMonthlyDepreciationReport });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(rouMonthlyDepreciationReportService.update).toHaveBeenCalledWith(rouMonthlyDepreciationReport);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackApplicationUserById', () => {
      it('Should return tracked ApplicationUser primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackApplicationUserById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackFiscalYearById', () => {
      it('Should return tracked FiscalYear primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackFiscalYearById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
