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

import { LeasePeriodService } from '../../lease-period/service/lease-period.service';

jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { LeaseLiabilityReportService } from '../service/lease-liability-report.service';
import { ILeaseLiabilityReport, LeaseLiabilityReport } from '../lease-liability-report.model';

import { LeaseLiabilityReportUpdateComponent } from './lease-liability-report-update.component';
import { ApplicationUserService } from '../../../erp-pages/application-user/service/application-user.service';
import { IApplicationUser } from '../../../erp-pages/application-user/application-user.model';
import { ILeasePeriod } from '../../lease-period/lease-period.model';

describe('LeaseLiabilityReport Management Update Component', () => {
  let comp: LeaseLiabilityReportUpdateComponent;
  let fixture: ComponentFixture<LeaseLiabilityReportUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let leaseLiabilityReportService: LeaseLiabilityReportService;
  let applicationUserService: ApplicationUserService;
  let leasePeriodService: LeasePeriodService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [LeaseLiabilityReportUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(LeaseLiabilityReportUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(LeaseLiabilityReportUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    leaseLiabilityReportService = TestBed.inject(LeaseLiabilityReportService);
    applicationUserService = TestBed.inject(ApplicationUserService);
    leasePeriodService = TestBed.inject(LeasePeriodService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call ApplicationUser query and add missing value', () => {
      const leaseLiabilityReport: ILeaseLiabilityReport = { id: 456 };
      const requestedBy: IApplicationUser = { id: 22889 };
      leaseLiabilityReport.requestedBy = requestedBy;

      const applicationUserCollection: IApplicationUser[] = [{ id: 29579 }];
      jest.spyOn(applicationUserService, 'query').mockReturnValue(of(new HttpResponse({ body: applicationUserCollection })));
      const additionalApplicationUsers = [requestedBy];
      const expectedCollection: IApplicationUser[] = [...additionalApplicationUsers, ...applicationUserCollection];
      jest.spyOn(applicationUserService, 'addApplicationUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ leaseLiabilityReport });
      comp.ngOnInit();

      expect(applicationUserService.query).toHaveBeenCalled();
      expect(applicationUserService.addApplicationUserToCollectionIfMissing).toHaveBeenCalledWith(
        applicationUserCollection,
        ...additionalApplicationUsers
      );
      expect(comp.applicationUsersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call LeasePeriod query and add missing value', () => {
      const leaseLiabilityReport: ILeaseLiabilityReport = { id: 456 };
      const leasePeriod: ILeasePeriod = { id: 24815 };
      leaseLiabilityReport.leasePeriod = leasePeriod;

      const leasePeriodCollection: ILeasePeriod[] = [{ id: 22174 }];
      jest.spyOn(leasePeriodService, 'query').mockReturnValue(of(new HttpResponse({ body: leasePeriodCollection })));
      const additionalLeasePeriods = [leasePeriod];
      const expectedCollection: ILeasePeriod[] = [...additionalLeasePeriods, ...leasePeriodCollection];
      jest.spyOn(leasePeriodService, 'addLeasePeriodToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ leaseLiabilityReport });
      comp.ngOnInit();

      expect(leasePeriodService.query).toHaveBeenCalled();
      expect(leasePeriodService.addLeasePeriodToCollectionIfMissing).toHaveBeenCalledWith(leasePeriodCollection, ...additionalLeasePeriods);
      expect(comp.leasePeriodsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const leaseLiabilityReport: ILeaseLiabilityReport = { id: 456 };
      const requestedBy: IApplicationUser = { id: 59027 };
      leaseLiabilityReport.requestedBy = requestedBy;
      const leasePeriod: ILeasePeriod = { id: 5601 };
      leaseLiabilityReport.leasePeriod = leasePeriod;

      activatedRoute.data = of({ leaseLiabilityReport });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(leaseLiabilityReport));
      expect(comp.applicationUsersSharedCollection).toContain(requestedBy);
      expect(comp.leasePeriodsSharedCollection).toContain(leasePeriod);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<LeaseLiabilityReport>>();
      const leaseLiabilityReport = { id: 123 };
      jest.spyOn(leaseLiabilityReportService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ leaseLiabilityReport });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: leaseLiabilityReport }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(leaseLiabilityReportService.update).toHaveBeenCalledWith(leaseLiabilityReport);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<LeaseLiabilityReport>>();
      const leaseLiabilityReport = new LeaseLiabilityReport();
      jest.spyOn(leaseLiabilityReportService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ leaseLiabilityReport });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: leaseLiabilityReport }));
      saveSubject.complete();

      // THEN
      expect(leaseLiabilityReportService.create).toHaveBeenCalledWith(leaseLiabilityReport);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<LeaseLiabilityReport>>();
      const leaseLiabilityReport = { id: 123 };
      jest.spyOn(leaseLiabilityReportService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ leaseLiabilityReport });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(leaseLiabilityReportService.update).toHaveBeenCalledWith(leaseLiabilityReport);
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
