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

import { ApplicationUserService } from '../../../erp-pages/application-user/service/application-user.service';

jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { LeaseLiabilityScheduleReportService } from '../service/lease-liability-schedule-report.service';
import { ILeaseLiabilityScheduleReport, LeaseLiabilityScheduleReport } from '../lease-liability-schedule-report.model';

import { LeaseLiabilityScheduleReportUpdateComponent } from './lease-liability-schedule-report-update.component';
import {
  LeaseAmortizationScheduleService
} from '../../lease-amortization-schedule/service/lease-amortization-schedule.service';
import { IApplicationUser } from '../../../erp-pages/application-user/application-user.model';
import { ILeaseAmortizationSchedule } from '../../lease-amortization-schedule/lease-amortization-schedule.model';

describe('LeaseLiabilityScheduleReport Management Update Component', () => {
  let comp: LeaseLiabilityScheduleReportUpdateComponent;
  let fixture: ComponentFixture<LeaseLiabilityScheduleReportUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let leaseLiabilityScheduleReportService: LeaseLiabilityScheduleReportService;
  let applicationUserService: ApplicationUserService;
  let leaseAmortizationScheduleService: LeaseAmortizationScheduleService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [LeaseLiabilityScheduleReportUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(LeaseLiabilityScheduleReportUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(LeaseLiabilityScheduleReportUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    leaseLiabilityScheduleReportService = TestBed.inject(LeaseLiabilityScheduleReportService);
    applicationUserService = TestBed.inject(ApplicationUserService);
    leaseAmortizationScheduleService = TestBed.inject(LeaseAmortizationScheduleService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call ApplicationUser query and add missing value', () => {
      const leaseLiabilityScheduleReport: ILeaseLiabilityScheduleReport = { id: 456 };
      const requestedBy: IApplicationUser = { id: 1474 };
      leaseLiabilityScheduleReport.requestedBy = requestedBy;

      const applicationUserCollection: IApplicationUser[] = [{ id: 86100 }];
      jest.spyOn(applicationUserService, 'query').mockReturnValue(of(new HttpResponse({ body: applicationUserCollection })));
      const additionalApplicationUsers = [requestedBy];
      const expectedCollection: IApplicationUser[] = [...additionalApplicationUsers, ...applicationUserCollection];
      jest.spyOn(applicationUserService, 'addApplicationUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ leaseLiabilityScheduleReport });
      comp.ngOnInit();

      expect(applicationUserService.query).toHaveBeenCalled();
      expect(applicationUserService.addApplicationUserToCollectionIfMissing).toHaveBeenCalledWith(
        applicationUserCollection,
        ...additionalApplicationUsers
      );
      expect(comp.applicationUsersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call LeaseAmortizationSchedule query and add missing value', () => {
      const leaseLiabilityScheduleReport: ILeaseLiabilityScheduleReport = { id: 456 };
      const leaseAmortizationSchedule: ILeaseAmortizationSchedule = { id: 12271 };
      leaseLiabilityScheduleReport.leaseAmortizationSchedule = leaseAmortizationSchedule;

      const leaseAmortizationScheduleCollection: ILeaseAmortizationSchedule[] = [{ id: 28589 }];
      jest
        .spyOn(leaseAmortizationScheduleService, 'query')
        .mockReturnValue(of(new HttpResponse({ body: leaseAmortizationScheduleCollection })));
      const additionalLeaseAmortizationSchedules = [leaseAmortizationSchedule];
      const expectedCollection: ILeaseAmortizationSchedule[] = [
        ...additionalLeaseAmortizationSchedules,
        ...leaseAmortizationScheduleCollection,
      ];
      jest.spyOn(leaseAmortizationScheduleService, 'addLeaseAmortizationScheduleToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ leaseLiabilityScheduleReport });
      comp.ngOnInit();

      expect(leaseAmortizationScheduleService.query).toHaveBeenCalled();
      expect(leaseAmortizationScheduleService.addLeaseAmortizationScheduleToCollectionIfMissing).toHaveBeenCalledWith(
        leaseAmortizationScheduleCollection,
        ...additionalLeaseAmortizationSchedules
      );
      expect(comp.leaseAmortizationSchedulesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const leaseLiabilityScheduleReport: ILeaseLiabilityScheduleReport = { id: 456 };
      const requestedBy: IApplicationUser = { id: 65714 };
      leaseLiabilityScheduleReport.requestedBy = requestedBy;
      const leaseAmortizationSchedule: ILeaseAmortizationSchedule = { id: 80503 };
      leaseLiabilityScheduleReport.leaseAmortizationSchedule = leaseAmortizationSchedule;

      activatedRoute.data = of({ leaseLiabilityScheduleReport });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(leaseLiabilityScheduleReport));
      expect(comp.applicationUsersSharedCollection).toContain(requestedBy);
      expect(comp.leaseAmortizationSchedulesSharedCollection).toContain(leaseAmortizationSchedule);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<LeaseLiabilityScheduleReport>>();
      const leaseLiabilityScheduleReport = { id: 123 };
      jest.spyOn(leaseLiabilityScheduleReportService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ leaseLiabilityScheduleReport });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: leaseLiabilityScheduleReport }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(leaseLiabilityScheduleReportService.update).toHaveBeenCalledWith(leaseLiabilityScheduleReport);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<LeaseLiabilityScheduleReport>>();
      const leaseLiabilityScheduleReport = new LeaseLiabilityScheduleReport();
      jest.spyOn(leaseLiabilityScheduleReportService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ leaseLiabilityScheduleReport });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: leaseLiabilityScheduleReport }));
      saveSubject.complete();

      // THEN
      expect(leaseLiabilityScheduleReportService.create).toHaveBeenCalledWith(leaseLiabilityScheduleReport);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<LeaseLiabilityScheduleReport>>();
      const leaseLiabilityScheduleReport = { id: 123 };
      jest.spyOn(leaseLiabilityScheduleReportService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ leaseLiabilityScheduleReport });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(leaseLiabilityScheduleReportService.update).toHaveBeenCalledWith(leaseLiabilityScheduleReport);
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

    describe('trackLeaseAmortizationScheduleById', () => {
      it('Should return tracked LeaseAmortizationSchedule primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackLeaseAmortizationScheduleById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
