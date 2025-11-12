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

import { IApplicationUser } from '../../../erp-pages/application-user/application-user.model';

jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { RouAccountBalanceReportService } from '../service/rou-account-balance-report.service';
import { IRouAccountBalanceReport, RouAccountBalanceReport } from '../rou-account-balance-report.model';

import { RouAccountBalanceReportUpdateComponent } from './rou-account-balance-report-update.component';
import { ApplicationUserService } from '../../../erp-pages/application-user/service/application-user.service';
import { ILeasePeriod } from '../../lease-period/lease-period.model';
import { LeasePeriodService } from '../../lease-period/service/lease-period.service';

describe('RouAccountBalanceReport Management Update Component', () => {
  let comp: RouAccountBalanceReportUpdateComponent;
  let fixture: ComponentFixture<RouAccountBalanceReportUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let rouAccountBalanceReportService: RouAccountBalanceReportService;
  let leasePeriodService: LeasePeriodService;
  let applicationUserService: ApplicationUserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [RouAccountBalanceReportUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(RouAccountBalanceReportUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(RouAccountBalanceReportUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    rouAccountBalanceReportService = TestBed.inject(RouAccountBalanceReportService);
    leasePeriodService = TestBed.inject(LeasePeriodService);
    applicationUserService = TestBed.inject(ApplicationUserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call LeasePeriod query and add missing value', () => {
      const rouAccountBalanceReport: IRouAccountBalanceReport = { id: 456 };
      const leasePeriod: ILeasePeriod = { id: 22064 };
      rouAccountBalanceReport.leasePeriod = leasePeriod;

      const leasePeriodCollection: ILeasePeriod[] = [{ id: 88188 }];
      jest.spyOn(leasePeriodService, 'query').mockReturnValue(of(new HttpResponse({ body: leasePeriodCollection })));
      const additionalLeasePeriods = [leasePeriod];
      const expectedCollection: ILeasePeriod[] = [...additionalLeasePeriods, ...leasePeriodCollection];
      jest.spyOn(leasePeriodService, 'addLeasePeriodToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ rouAccountBalanceReport });
      comp.ngOnInit();

      expect(leasePeriodService.query).toHaveBeenCalled();
      expect(leasePeriodService.addLeasePeriodToCollectionIfMissing).toHaveBeenCalledWith(leasePeriodCollection, ...additionalLeasePeriods);
      expect(comp.leasePeriodsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call ApplicationUser query and add missing value', () => {
      const rouAccountBalanceReport: IRouAccountBalanceReport = { id: 456 };
      const requestedBy: IApplicationUser = { id: 35202 };
      rouAccountBalanceReport.requestedBy = requestedBy;

      const applicationUserCollection: IApplicationUser[] = [{ id: 60001 }];
      jest.spyOn(applicationUserService, 'query').mockReturnValue(of(new HttpResponse({ body: applicationUserCollection })));
      const additionalApplicationUsers = [requestedBy];
      const expectedCollection: IApplicationUser[] = [...additionalApplicationUsers, ...applicationUserCollection];
      jest.spyOn(applicationUserService, 'addApplicationUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ rouAccountBalanceReport });
      comp.ngOnInit();

      expect(applicationUserService.query).toHaveBeenCalled();
      expect(applicationUserService.addApplicationUserToCollectionIfMissing).toHaveBeenCalledWith(
        applicationUserCollection,
        ...additionalApplicationUsers
      );
      expect(comp.applicationUsersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const rouAccountBalanceReport: IRouAccountBalanceReport = { id: 456 };
      const leasePeriod: ILeasePeriod = { id: 85849 };
      rouAccountBalanceReport.leasePeriod = leasePeriod;
      const requestedBy: IApplicationUser = { id: 10321 };
      rouAccountBalanceReport.requestedBy = requestedBy;

      activatedRoute.data = of({ rouAccountBalanceReport });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(rouAccountBalanceReport));
      expect(comp.leasePeriodsSharedCollection).toContain(leasePeriod);
      expect(comp.applicationUsersSharedCollection).toContain(requestedBy);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<RouAccountBalanceReport>>();
      const rouAccountBalanceReport = { id: 123 };
      jest.spyOn(rouAccountBalanceReportService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ rouAccountBalanceReport });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: rouAccountBalanceReport }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(rouAccountBalanceReportService.update).toHaveBeenCalledWith(rouAccountBalanceReport);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<RouAccountBalanceReport>>();
      const rouAccountBalanceReport = new RouAccountBalanceReport();
      jest.spyOn(rouAccountBalanceReportService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ rouAccountBalanceReport });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: rouAccountBalanceReport }));
      saveSubject.complete();

      // THEN
      expect(rouAccountBalanceReportService.create).toHaveBeenCalledWith(rouAccountBalanceReport);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<RouAccountBalanceReport>>();
      const rouAccountBalanceReport = { id: 123 };
      jest.spyOn(rouAccountBalanceReportService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ rouAccountBalanceReport });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(rouAccountBalanceReportService.update).toHaveBeenCalledWith(rouAccountBalanceReport);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackLeasePeriodById', () => {
      it('Should return tracked LeasePeriod primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackLeasePeriodById(0, entity);
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
  });
});
