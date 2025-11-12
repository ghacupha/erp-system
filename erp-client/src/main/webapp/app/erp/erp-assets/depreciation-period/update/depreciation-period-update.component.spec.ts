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

import { IFiscalMonth } from '../../../erp-pages/fiscal-month/fiscal-month.model';

jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { DepreciationPeriodService } from '../service/depreciation-period.service';
import { IDepreciationPeriod, DepreciationPeriod } from '../depreciation-period.model';

import { DepreciationPeriodUpdateComponent } from './depreciation-period-update.component';
import { FiscalMonthService } from '../../../erp-pages/fiscal-month/service/fiscal-month.service';
import { IApplicationUser } from '../../../erp-pages/application-user/application-user.model';
import { ApplicationUserService } from '../../../erp-pages/application-user/service/application-user.service';

describe('DepreciationPeriod Management Update Component', () => {
  let comp: DepreciationPeriodUpdateComponent;
  let fixture: ComponentFixture<DepreciationPeriodUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let depreciationPeriodService: DepreciationPeriodService;
  let applicationUserService: ApplicationUserService;
  let fiscalMonthService: FiscalMonthService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [DepreciationPeriodUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(DepreciationPeriodUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DepreciationPeriodUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    depreciationPeriodService = TestBed.inject(DepreciationPeriodService);
    applicationUserService = TestBed.inject(ApplicationUserService);
    fiscalMonthService = TestBed.inject(FiscalMonthService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call previousPeriod query and add missing value', () => {
      const depreciationPeriod: IDepreciationPeriod = { id: 456 };
      const previousPeriod: IDepreciationPeriod = { id: 74845 };
      depreciationPeriod.previousPeriod = previousPeriod;

      const previousPeriodCollection: IDepreciationPeriod[] = [{ id: 64689 }];
      jest.spyOn(depreciationPeriodService, 'query').mockReturnValue(of(new HttpResponse({ body: previousPeriodCollection })));
      const expectedCollection: IDepreciationPeriod[] = [previousPeriod, ...previousPeriodCollection];
      jest.spyOn(depreciationPeriodService, 'addDepreciationPeriodToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ depreciationPeriod });
      comp.ngOnInit();

      expect(depreciationPeriodService.query).toHaveBeenCalled();
      expect(depreciationPeriodService.addDepreciationPeriodToCollectionIfMissing).toHaveBeenCalledWith(
        previousPeriodCollection,
        previousPeriod
      );
      expect(comp.previousPeriodsCollection).toEqual(expectedCollection);
    });

    it('Should call ApplicationUser query and add missing value', () => {
      const depreciationPeriod: IDepreciationPeriod = { id: 456 };
      const createdBy: IApplicationUser = { id: 40330 };
      depreciationPeriod.createdBy = createdBy;

      const applicationUserCollection: IApplicationUser[] = [{ id: 30397 }];
      jest.spyOn(applicationUserService, 'query').mockReturnValue(of(new HttpResponse({ body: applicationUserCollection })));
      const additionalApplicationUsers = [createdBy];
      const expectedCollection: IApplicationUser[] = [...additionalApplicationUsers, ...applicationUserCollection];
      jest.spyOn(applicationUserService, 'addApplicationUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ depreciationPeriod });
      comp.ngOnInit();

      expect(applicationUserService.query).toHaveBeenCalled();
      expect(applicationUserService.addApplicationUserToCollectionIfMissing).toHaveBeenCalledWith(
        applicationUserCollection,
        ...additionalApplicationUsers
      );
      expect(comp.applicationUsersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call FiscalMonth query and add missing value', () => {
      const depreciationPeriod: IDepreciationPeriod = { id: 456 };
      const fiscalMonth: IFiscalMonth = { id: 15456 };
      depreciationPeriod.fiscalMonth = fiscalMonth;

      const fiscalMonthCollection: IFiscalMonth[] = [{ id: 4425 }];
      jest.spyOn(fiscalMonthService, 'query').mockReturnValue(of(new HttpResponse({ body: fiscalMonthCollection })));
      const additionalFiscalMonths = [fiscalMonth];
      const expectedCollection: IFiscalMonth[] = [...additionalFiscalMonths, ...fiscalMonthCollection];
      jest.spyOn(fiscalMonthService, 'addFiscalMonthToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ depreciationPeriod });
      comp.ngOnInit();

      expect(fiscalMonthService.query).toHaveBeenCalled();
      expect(fiscalMonthService.addFiscalMonthToCollectionIfMissing).toHaveBeenCalledWith(fiscalMonthCollection, ...additionalFiscalMonths);
      expect(comp.fiscalMonthsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const depreciationPeriod: IDepreciationPeriod = { id: 456 };
      const previousPeriod: IDepreciationPeriod = { id: 31832 };
      depreciationPeriod.previousPeriod = previousPeriod;
      const createdBy: IApplicationUser = { id: 77078 };
      depreciationPeriod.createdBy = createdBy;
      const fiscalMonth: IFiscalMonth = { id: 80430 };
      depreciationPeriod.fiscalMonth = fiscalMonth;

      activatedRoute.data = of({ depreciationPeriod });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(depreciationPeriod));
      expect(comp.previousPeriodsCollection).toContain(previousPeriod);
      expect(comp.applicationUsersSharedCollection).toContain(createdBy);
      expect(comp.fiscalMonthsSharedCollection).toContain(fiscalMonth);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<DepreciationPeriod>>();
      const depreciationPeriod = { id: 123 };
      jest.spyOn(depreciationPeriodService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ depreciationPeriod });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: depreciationPeriod }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(depreciationPeriodService.update).toHaveBeenCalledWith(depreciationPeriod);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<DepreciationPeriod>>();
      const depreciationPeriod = new DepreciationPeriod();
      jest.spyOn(depreciationPeriodService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ depreciationPeriod });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: depreciationPeriod }));
      saveSubject.complete();

      // THEN
      expect(depreciationPeriodService.create).toHaveBeenCalledWith(depreciationPeriod);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<DepreciationPeriod>>();
      const depreciationPeriod = { id: 123 };
      jest.spyOn(depreciationPeriodService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ depreciationPeriod });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(depreciationPeriodService.update).toHaveBeenCalledWith(depreciationPeriod);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackDepreciationPeriodById', () => {
      it('Should return tracked DepreciationPeriod primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackDepreciationPeriodById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

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

    describe('trackFiscalMonthById', () => {
      it('Should return tracked FiscalMonth primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackFiscalMonthById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackFiscalQuarterById', () => {
      it('Should return tracked FiscalQuarter primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackFiscalQuarterById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
