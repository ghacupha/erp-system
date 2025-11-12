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

import { DepreciationJobService } from '../service/depreciation-job.service';
import { IDepreciationJob, DepreciationJob } from '../depreciation-job.model';
import { IApplicationUser } from 'app/entities/people/application-user/application-user.model';
import { ApplicationUserService } from 'app/entities/people/application-user/service/application-user.service';
import { IDepreciationPeriod } from 'app/entities/assets/depreciation-period/depreciation-period.model';
import { DepreciationPeriodService } from 'app/entities/assets/depreciation-period/service/depreciation-period.service';

import { DepreciationJobUpdateComponent } from './depreciation-job-update.component';

describe('DepreciationJob Management Update Component', () => {
  let comp: DepreciationJobUpdateComponent;
  let fixture: ComponentFixture<DepreciationJobUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let depreciationJobService: DepreciationJobService;
  let applicationUserService: ApplicationUserService;
  let depreciationPeriodService: DepreciationPeriodService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [DepreciationJobUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(DepreciationJobUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DepreciationJobUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    depreciationJobService = TestBed.inject(DepreciationJobService);
    applicationUserService = TestBed.inject(ApplicationUserService);
    depreciationPeriodService = TestBed.inject(DepreciationPeriodService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call ApplicationUser query and add missing value', () => {
      const depreciationJob: IDepreciationJob = { id: 456 };
      const createdBy: IApplicationUser = { id: 23647 };
      depreciationJob.createdBy = createdBy;

      const applicationUserCollection: IApplicationUser[] = [{ id: 58597 }];
      jest.spyOn(applicationUserService, 'query').mockReturnValue(of(new HttpResponse({ body: applicationUserCollection })));
      const additionalApplicationUsers = [createdBy];
      const expectedCollection: IApplicationUser[] = [...additionalApplicationUsers, ...applicationUserCollection];
      jest.spyOn(applicationUserService, 'addApplicationUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ depreciationJob });
      comp.ngOnInit();

      expect(applicationUserService.query).toHaveBeenCalled();
      expect(applicationUserService.addApplicationUserToCollectionIfMissing).toHaveBeenCalledWith(
        applicationUserCollection,
        ...additionalApplicationUsers
      );
      expect(comp.applicationUsersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call depreciationPeriod query and add missing value', () => {
      const depreciationJob: IDepreciationJob = { id: 456 };
      const depreciationPeriod: IDepreciationPeriod = { id: 69591 };
      depreciationJob.depreciationPeriod = depreciationPeriod;

      const depreciationPeriodCollection: IDepreciationPeriod[] = [{ id: 11116 }];
      jest.spyOn(depreciationPeriodService, 'query').mockReturnValue(of(new HttpResponse({ body: depreciationPeriodCollection })));
      const expectedCollection: IDepreciationPeriod[] = [depreciationPeriod, ...depreciationPeriodCollection];
      jest.spyOn(depreciationPeriodService, 'addDepreciationPeriodToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ depreciationJob });
      comp.ngOnInit();

      expect(depreciationPeriodService.query).toHaveBeenCalled();
      expect(depreciationPeriodService.addDepreciationPeriodToCollectionIfMissing).toHaveBeenCalledWith(
        depreciationPeriodCollection,
        depreciationPeriod
      );
      expect(comp.depreciationPeriodsCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const depreciationJob: IDepreciationJob = { id: 456 };
      const createdBy: IApplicationUser = { id: 74259 };
      depreciationJob.createdBy = createdBy;
      const depreciationPeriod: IDepreciationPeriod = { id: 89320 };
      depreciationJob.depreciationPeriod = depreciationPeriod;

      activatedRoute.data = of({ depreciationJob });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(depreciationJob));
      expect(comp.applicationUsersSharedCollection).toContain(createdBy);
      expect(comp.depreciationPeriodsCollection).toContain(depreciationPeriod);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<DepreciationJob>>();
      const depreciationJob = { id: 123 };
      jest.spyOn(depreciationJobService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ depreciationJob });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: depreciationJob }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(depreciationJobService.update).toHaveBeenCalledWith(depreciationJob);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<DepreciationJob>>();
      const depreciationJob = new DepreciationJob();
      jest.spyOn(depreciationJobService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ depreciationJob });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: depreciationJob }));
      saveSubject.complete();

      // THEN
      expect(depreciationJobService.create).toHaveBeenCalledWith(depreciationJob);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<DepreciationJob>>();
      const depreciationJob = { id: 123 };
      jest.spyOn(depreciationJobService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ depreciationJob });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(depreciationJobService.update).toHaveBeenCalledWith(depreciationJob);
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

    describe('trackDepreciationPeriodById', () => {
      it('Should return tracked DepreciationPeriod primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackDepreciationPeriodById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
