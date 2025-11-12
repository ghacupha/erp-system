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

import { LeaseLiabilityPostingReportService } from '../service/lease-liability-posting-report.service';
import { ILeaseLiabilityPostingReport, LeaseLiabilityPostingReport } from '../lease-liability-posting-report.model';
import { IApplicationUser } from 'app/entities/people/application-user/application-user.model';
import { ApplicationUserService } from 'app/entities/people/application-user/service/application-user.service';
import { ILeasePeriod } from 'app/entities/leases/lease-period/lease-period.model';
import { LeasePeriodService } from 'app/entities/leases/lease-period/service/lease-period.service';

import { LeaseLiabilityPostingReportUpdateComponent } from './lease-liability-posting-report-update.component';

describe('LeaseLiabilityPostingReport Management Update Component', () => {
  let comp: LeaseLiabilityPostingReportUpdateComponent;
  let fixture: ComponentFixture<LeaseLiabilityPostingReportUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let leaseLiabilityPostingReportService: LeaseLiabilityPostingReportService;
  let applicationUserService: ApplicationUserService;
  let leasePeriodService: LeasePeriodService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [LeaseLiabilityPostingReportUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(LeaseLiabilityPostingReportUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(LeaseLiabilityPostingReportUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    leaseLiabilityPostingReportService = TestBed.inject(LeaseLiabilityPostingReportService);
    applicationUserService = TestBed.inject(ApplicationUserService);
    leasePeriodService = TestBed.inject(LeasePeriodService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call ApplicationUser query and add missing value', () => {
      const leaseLiabilityPostingReport: ILeaseLiabilityPostingReport = { id: 456 };
      const requestedBy: IApplicationUser = { id: 77823 };
      leaseLiabilityPostingReport.requestedBy = requestedBy;

      const applicationUserCollection: IApplicationUser[] = [{ id: 49835 }];
      jest.spyOn(applicationUserService, 'query').mockReturnValue(of(new HttpResponse({ body: applicationUserCollection })));
      const additionalApplicationUsers = [requestedBy];
      const expectedCollection: IApplicationUser[] = [...additionalApplicationUsers, ...applicationUserCollection];
      jest.spyOn(applicationUserService, 'addApplicationUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ leaseLiabilityPostingReport });
      comp.ngOnInit();

      expect(applicationUserService.query).toHaveBeenCalled();
      expect(applicationUserService.addApplicationUserToCollectionIfMissing).toHaveBeenCalledWith(
        applicationUserCollection,
        ...additionalApplicationUsers
      );
      expect(comp.applicationUsersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call LeasePeriod query and add missing value', () => {
      const leaseLiabilityPostingReport: ILeaseLiabilityPostingReport = { id: 456 };
      const leasePeriod: ILeasePeriod = { id: 19992 };
      leaseLiabilityPostingReport.leasePeriod = leasePeriod;

      const leasePeriodCollection: ILeasePeriod[] = [{ id: 69758 }];
      jest.spyOn(leasePeriodService, 'query').mockReturnValue(of(new HttpResponse({ body: leasePeriodCollection })));
      const additionalLeasePeriods = [leasePeriod];
      const expectedCollection: ILeasePeriod[] = [...additionalLeasePeriods, ...leasePeriodCollection];
      jest.spyOn(leasePeriodService, 'addLeasePeriodToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ leaseLiabilityPostingReport });
      comp.ngOnInit();

      expect(leasePeriodService.query).toHaveBeenCalled();
      expect(leasePeriodService.addLeasePeriodToCollectionIfMissing).toHaveBeenCalledWith(leasePeriodCollection, ...additionalLeasePeriods);
      expect(comp.leasePeriodsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const leaseLiabilityPostingReport: ILeaseLiabilityPostingReport = { id: 456 };
      const requestedBy: IApplicationUser = { id: 84113 };
      leaseLiabilityPostingReport.requestedBy = requestedBy;
      const leasePeriod: ILeasePeriod = { id: 9782 };
      leaseLiabilityPostingReport.leasePeriod = leasePeriod;

      activatedRoute.data = of({ leaseLiabilityPostingReport });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(leaseLiabilityPostingReport));
      expect(comp.applicationUsersSharedCollection).toContain(requestedBy);
      expect(comp.leasePeriodsSharedCollection).toContain(leasePeriod);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<LeaseLiabilityPostingReport>>();
      const leaseLiabilityPostingReport = { id: 123 };
      jest.spyOn(leaseLiabilityPostingReportService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ leaseLiabilityPostingReport });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: leaseLiabilityPostingReport }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(leaseLiabilityPostingReportService.update).toHaveBeenCalledWith(leaseLiabilityPostingReport);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<LeaseLiabilityPostingReport>>();
      const leaseLiabilityPostingReport = new LeaseLiabilityPostingReport();
      jest.spyOn(leaseLiabilityPostingReportService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ leaseLiabilityPostingReport });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: leaseLiabilityPostingReport }));
      saveSubject.complete();

      // THEN
      expect(leaseLiabilityPostingReportService.create).toHaveBeenCalledWith(leaseLiabilityPostingReport);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<LeaseLiabilityPostingReport>>();
      const leaseLiabilityPostingReport = { id: 123 };
      jest.spyOn(leaseLiabilityPostingReportService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ leaseLiabilityPostingReport });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(leaseLiabilityPostingReportService.update).toHaveBeenCalledWith(leaseLiabilityPostingReport);
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

    describe('trackLeasePeriodById', () => {
      it('Should return tracked LeasePeriod primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackLeasePeriodById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
