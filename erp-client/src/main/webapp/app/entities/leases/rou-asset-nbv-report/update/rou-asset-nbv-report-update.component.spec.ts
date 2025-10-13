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

jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { RouAssetNBVReportService } from '../service/rou-asset-nbv-report.service';
import { IRouAssetNBVReport, RouAssetNBVReport } from '../rou-asset-nbv-report.model';
import { ILeasePeriod } from 'app/entities/leases/lease-period/lease-period.model';
import { LeasePeriodService } from 'app/entities/leases/lease-period/service/lease-period.service';
import { IApplicationUser } from 'app/entities/people/application-user/application-user.model';
import { ApplicationUserService } from 'app/entities/people/application-user/service/application-user.service';

import { RouAssetNBVReportUpdateComponent } from './rou-asset-nbv-report-update.component';

describe('RouAssetNBVReport Management Update Component', () => {
  let comp: RouAssetNBVReportUpdateComponent;
  let fixture: ComponentFixture<RouAssetNBVReportUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let rouAssetNBVReportService: RouAssetNBVReportService;
  let leasePeriodService: LeasePeriodService;
  let applicationUserService: ApplicationUserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [RouAssetNBVReportUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(RouAssetNBVReportUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(RouAssetNBVReportUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    rouAssetNBVReportService = TestBed.inject(RouAssetNBVReportService);
    leasePeriodService = TestBed.inject(LeasePeriodService);
    applicationUserService = TestBed.inject(ApplicationUserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call LeasePeriod query and add missing value', () => {
      const rouAssetNBVReport: IRouAssetNBVReport = { id: 456 };
      const leasePeriod: ILeasePeriod = { id: 29294 };
      rouAssetNBVReport.leasePeriod = leasePeriod;

      const leasePeriodCollection: ILeasePeriod[] = [{ id: 66611 }];
      jest.spyOn(leasePeriodService, 'query').mockReturnValue(of(new HttpResponse({ body: leasePeriodCollection })));
      const additionalLeasePeriods = [leasePeriod];
      const expectedCollection: ILeasePeriod[] = [...additionalLeasePeriods, ...leasePeriodCollection];
      jest.spyOn(leasePeriodService, 'addLeasePeriodToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ rouAssetNBVReport });
      comp.ngOnInit();

      expect(leasePeriodService.query).toHaveBeenCalled();
      expect(leasePeriodService.addLeasePeriodToCollectionIfMissing).toHaveBeenCalledWith(leasePeriodCollection, ...additionalLeasePeriods);
      expect(comp.leasePeriodsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call ApplicationUser query and add missing value', () => {
      const rouAssetNBVReport: IRouAssetNBVReport = { id: 456 };
      const requestedBy: IApplicationUser = { id: 65132 };
      rouAssetNBVReport.requestedBy = requestedBy;

      const applicationUserCollection: IApplicationUser[] = [{ id: 57109 }];
      jest.spyOn(applicationUserService, 'query').mockReturnValue(of(new HttpResponse({ body: applicationUserCollection })));
      const additionalApplicationUsers = [requestedBy];
      const expectedCollection: IApplicationUser[] = [...additionalApplicationUsers, ...applicationUserCollection];
      jest.spyOn(applicationUserService, 'addApplicationUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ rouAssetNBVReport });
      comp.ngOnInit();

      expect(applicationUserService.query).toHaveBeenCalled();
      expect(applicationUserService.addApplicationUserToCollectionIfMissing).toHaveBeenCalledWith(
        applicationUserCollection,
        ...additionalApplicationUsers
      );
      expect(comp.applicationUsersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const rouAssetNBVReport: IRouAssetNBVReport = { id: 456 };
      const leasePeriod: ILeasePeriod = { id: 23195 };
      rouAssetNBVReport.leasePeriod = leasePeriod;
      const requestedBy: IApplicationUser = { id: 64913 };
      rouAssetNBVReport.requestedBy = requestedBy;

      activatedRoute.data = of({ rouAssetNBVReport });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(rouAssetNBVReport));
      expect(comp.leasePeriodsSharedCollection).toContain(leasePeriod);
      expect(comp.applicationUsersSharedCollection).toContain(requestedBy);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<RouAssetNBVReport>>();
      const rouAssetNBVReport = { id: 123 };
      jest.spyOn(rouAssetNBVReportService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ rouAssetNBVReport });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: rouAssetNBVReport }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(rouAssetNBVReportService.update).toHaveBeenCalledWith(rouAssetNBVReport);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<RouAssetNBVReport>>();
      const rouAssetNBVReport = new RouAssetNBVReport();
      jest.spyOn(rouAssetNBVReportService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ rouAssetNBVReport });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: rouAssetNBVReport }));
      saveSubject.complete();

      // THEN
      expect(rouAssetNBVReportService.create).toHaveBeenCalledWith(rouAssetNBVReport);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<RouAssetNBVReport>>();
      const rouAssetNBVReport = { id: 123 };
      jest.spyOn(rouAssetNBVReportService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ rouAssetNBVReport });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(rouAssetNBVReportService.update).toHaveBeenCalledWith(rouAssetNBVReport);
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
