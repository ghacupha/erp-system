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

import { ApplicationUserService } from '../../../erp-pages/application-user/service/application-user.service';

jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { AmortizationPostingReportRequisitionService } from '../service/amortization-posting-report-requisition.service';
import {
  IAmortizationPostingReportRequisition,
  AmortizationPostingReportRequisition,
} from '../amortization-posting-report-requisition.model';

import { AmortizationPostingReportRequisitionUpdateComponent } from './amortization-posting-report-requisition-update.component';
import { IApplicationUser } from '../../../erp-pages/application-user/application-user.model';
import { AmortizationPeriodService } from '../../amortization-period/service/amortization-period.service';
import { IAmortizationPeriod } from '../../amortization-period/amortization-period.model';

describe('AmortizationPostingReportRequisition Management Update Component', () => {
  let comp: AmortizationPostingReportRequisitionUpdateComponent;
  let fixture: ComponentFixture<AmortizationPostingReportRequisitionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let amortizationPostingReportRequisitionService: AmortizationPostingReportRequisitionService;
  let amortizationPeriodService: AmortizationPeriodService;
  let applicationUserService: ApplicationUserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [AmortizationPostingReportRequisitionUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(AmortizationPostingReportRequisitionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AmortizationPostingReportRequisitionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    amortizationPostingReportRequisitionService = TestBed.inject(AmortizationPostingReportRequisitionService);
    amortizationPeriodService = TestBed.inject(AmortizationPeriodService);
    applicationUserService = TestBed.inject(ApplicationUserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call AmortizationPeriod query and add missing value', () => {
      const amortizationPostingReportRequisition: IAmortizationPostingReportRequisition = { id: 456 };
      const amortizationPeriod: IAmortizationPeriod = { id: 30942 };
      amortizationPostingReportRequisition.amortizationPeriod = amortizationPeriod;

      const amortizationPeriodCollection: IAmortizationPeriod[] = [{ id: 26331 }];
      jest.spyOn(amortizationPeriodService, 'query').mockReturnValue(of(new HttpResponse({ body: amortizationPeriodCollection })));
      const additionalAmortizationPeriods = [amortizationPeriod];
      const expectedCollection: IAmortizationPeriod[] = [...additionalAmortizationPeriods, ...amortizationPeriodCollection];
      jest.spyOn(amortizationPeriodService, 'addAmortizationPeriodToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ amortizationPostingReportRequisition });
      comp.ngOnInit();

      expect(amortizationPeriodService.query).toHaveBeenCalled();
      expect(amortizationPeriodService.addAmortizationPeriodToCollectionIfMissing).toHaveBeenCalledWith(
        amortizationPeriodCollection,
        ...additionalAmortizationPeriods
      );
      expect(comp.amortizationPeriodsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call ApplicationUser query and add missing value', () => {
      const amortizationPostingReportRequisition: IAmortizationPostingReportRequisition = { id: 456 };
      const requestedBy: IApplicationUser = { id: 13858 };
      amortizationPostingReportRequisition.requestedBy = requestedBy;
      const lastAccessedBy: IApplicationUser = { id: 75851 };
      amortizationPostingReportRequisition.lastAccessedBy = lastAccessedBy;

      const applicationUserCollection: IApplicationUser[] = [{ id: 38490 }];
      jest.spyOn(applicationUserService, 'query').mockReturnValue(of(new HttpResponse({ body: applicationUserCollection })));
      const additionalApplicationUsers = [requestedBy, lastAccessedBy];
      const expectedCollection: IApplicationUser[] = [...additionalApplicationUsers, ...applicationUserCollection];
      jest.spyOn(applicationUserService, 'addApplicationUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ amortizationPostingReportRequisition });
      comp.ngOnInit();

      expect(applicationUserService.query).toHaveBeenCalled();
      expect(applicationUserService.addApplicationUserToCollectionIfMissing).toHaveBeenCalledWith(
        applicationUserCollection,
        ...additionalApplicationUsers
      );
      expect(comp.applicationUsersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const amortizationPostingReportRequisition: IAmortizationPostingReportRequisition = { id: 456 };
      const amortizationPeriod: IAmortizationPeriod = { id: 40371 };
      amortizationPostingReportRequisition.amortizationPeriod = amortizationPeriod;
      const requestedBy: IApplicationUser = { id: 79968 };
      amortizationPostingReportRequisition.requestedBy = requestedBy;
      const lastAccessedBy: IApplicationUser = { id: 31718 };
      amortizationPostingReportRequisition.lastAccessedBy = lastAccessedBy;

      activatedRoute.data = of({ amortizationPostingReportRequisition });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(amortizationPostingReportRequisition));
      expect(comp.amortizationPeriodsSharedCollection).toContain(amortizationPeriod);
      expect(comp.applicationUsersSharedCollection).toContain(requestedBy);
      expect(comp.applicationUsersSharedCollection).toContain(lastAccessedBy);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<AmortizationPostingReportRequisition>>();
      const amortizationPostingReportRequisition = { id: 123 };
      jest.spyOn(amortizationPostingReportRequisitionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ amortizationPostingReportRequisition });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: amortizationPostingReportRequisition }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(amortizationPostingReportRequisitionService.update).toHaveBeenCalledWith(amortizationPostingReportRequisition);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<AmortizationPostingReportRequisition>>();
      const amortizationPostingReportRequisition = new AmortizationPostingReportRequisition();
      jest.spyOn(amortizationPostingReportRequisitionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ amortizationPostingReportRequisition });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: amortizationPostingReportRequisition }));
      saveSubject.complete();

      // THEN
      expect(amortizationPostingReportRequisitionService.create).toHaveBeenCalledWith(amortizationPostingReportRequisition);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<AmortizationPostingReportRequisition>>();
      const amortizationPostingReportRequisition = { id: 123 };
      jest.spyOn(amortizationPostingReportRequisitionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ amortizationPostingReportRequisition });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(amortizationPostingReportRequisitionService.update).toHaveBeenCalledWith(amortizationPostingReportRequisition);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackAmortizationPeriodById', () => {
      it('Should return tracked AmortizationPeriod primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackAmortizationPeriodById(0, entity);
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
