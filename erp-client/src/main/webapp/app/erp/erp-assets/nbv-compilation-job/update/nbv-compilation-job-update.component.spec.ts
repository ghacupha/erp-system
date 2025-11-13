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

import { DepreciationPeriodService } from '../../depreciation-period/service/depreciation-period.service';

jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { NbvCompilationJobService } from '../service/nbv-compilation-job.service';
import { INbvCompilationJob, NbvCompilationJob } from '../nbv-compilation-job.model';

import { NbvCompilationJobUpdateComponent } from './nbv-compilation-job-update.component';
import { IDepreciationPeriod } from '../../depreciation-period/depreciation-period.model';
import { IApplicationUser } from '../../../erp-pages/application-user/application-user.model';
import { ApplicationUserService } from '../../../erp-pages/application-user/service/application-user.service';

describe('NbvCompilationJob Management Update Component', () => {
  let comp: NbvCompilationJobUpdateComponent;
  let fixture: ComponentFixture<NbvCompilationJobUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let nbvCompilationJobService: NbvCompilationJobService;
  let depreciationPeriodService: DepreciationPeriodService;
  let applicationUserService: ApplicationUserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [NbvCompilationJobUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(NbvCompilationJobUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(NbvCompilationJobUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    nbvCompilationJobService = TestBed.inject(NbvCompilationJobService);
    depreciationPeriodService = TestBed.inject(DepreciationPeriodService);
    applicationUserService = TestBed.inject(ApplicationUserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call DepreciationPeriod query and add missing value', () => {
      const nbvCompilationJob: INbvCompilationJob = { id: 456 };
      const activePeriod: IDepreciationPeriod = { id: 71035 };
      nbvCompilationJob.activePeriod = activePeriod;

      const depreciationPeriodCollection: IDepreciationPeriod[] = [{ id: 67986 }];
      jest.spyOn(depreciationPeriodService, 'query').mockReturnValue(of(new HttpResponse({ body: depreciationPeriodCollection })));
      const additionalDepreciationPeriods = [activePeriod];
      const expectedCollection: IDepreciationPeriod[] = [...additionalDepreciationPeriods, ...depreciationPeriodCollection];
      jest.spyOn(depreciationPeriodService, 'addDepreciationPeriodToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ nbvCompilationJob });
      comp.ngOnInit();

      expect(depreciationPeriodService.query).toHaveBeenCalled();
      expect(depreciationPeriodService.addDepreciationPeriodToCollectionIfMissing).toHaveBeenCalledWith(
        depreciationPeriodCollection,
        ...additionalDepreciationPeriods
      );
      expect(comp.depreciationPeriodsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call ApplicationUser query and add missing value', () => {
      const nbvCompilationJob: INbvCompilationJob = { id: 456 };
      const initiatedBy: IApplicationUser = { id: 31292 };
      nbvCompilationJob.initiatedBy = initiatedBy;

      const applicationUserCollection: IApplicationUser[] = [{ id: 17494 }];
      jest.spyOn(applicationUserService, 'query').mockReturnValue(of(new HttpResponse({ body: applicationUserCollection })));
      const additionalApplicationUsers = [initiatedBy];
      const expectedCollection: IApplicationUser[] = [...additionalApplicationUsers, ...applicationUserCollection];
      jest.spyOn(applicationUserService, 'addApplicationUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ nbvCompilationJob });
      comp.ngOnInit();

      expect(applicationUserService.query).toHaveBeenCalled();
      expect(applicationUserService.addApplicationUserToCollectionIfMissing).toHaveBeenCalledWith(
        applicationUserCollection,
        ...additionalApplicationUsers
      );
      expect(comp.applicationUsersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const nbvCompilationJob: INbvCompilationJob = { id: 456 };
      const activePeriod: IDepreciationPeriod = { id: 27985 };
      nbvCompilationJob.activePeriod = activePeriod;
      const initiatedBy: IApplicationUser = { id: 94430 };
      nbvCompilationJob.initiatedBy = initiatedBy;

      activatedRoute.data = of({ nbvCompilationJob });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(nbvCompilationJob));
      expect(comp.depreciationPeriodsSharedCollection).toContain(activePeriod);
      expect(comp.applicationUsersSharedCollection).toContain(initiatedBy);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<NbvCompilationJob>>();
      const nbvCompilationJob = { id: 123 };
      jest.spyOn(nbvCompilationJobService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ nbvCompilationJob });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: nbvCompilationJob }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(nbvCompilationJobService.update).toHaveBeenCalledWith(nbvCompilationJob);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<NbvCompilationJob>>();
      const nbvCompilationJob = new NbvCompilationJob();
      jest.spyOn(nbvCompilationJobService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ nbvCompilationJob });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: nbvCompilationJob }));
      saveSubject.complete();

      // THEN
      expect(nbvCompilationJobService.create).toHaveBeenCalledWith(nbvCompilationJob);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<NbvCompilationJob>>();
      const nbvCompilationJob = { id: 123 };
      jest.spyOn(nbvCompilationJobService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ nbvCompilationJob });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(nbvCompilationJobService.update).toHaveBeenCalledWith(nbvCompilationJob);
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
  });
});
