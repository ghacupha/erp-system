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

import { IDepreciationBatchSequence } from '../../depreciation-batch-sequence/depreciation-batch-sequence.model';

jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { DepreciationJobNoticeService } from '../service/depreciation-job-notice.service';
import { IDepreciationJobNotice, DepreciationJobNotice } from '../depreciation-job-notice.model';

import { DepreciationJobNoticeUpdateComponent } from './depreciation-job-notice-update.component';
import { IPlaceholder } from '../../../erp-pages/placeholder/placeholder.model';
import { DepreciationPeriodService } from '../../depreciation-period/service/depreciation-period.service';
import { IDepreciationJob } from '../../depreciation-job/depreciation-job.model';
import { DepreciationJobService } from '../../depreciation-job/service/depreciation-job.service';
import { IDepreciationPeriod } from '../../depreciation-period/depreciation-period.model';
import { UniversallyUniqueMappingService } from '../../../erp-pages/universally-unique-mapping/service/universally-unique-mapping.service';
import { IApplicationUser } from '../../../erp-pages/application-user/application-user.model';
import { ApplicationUserService } from '../../../erp-pages/application-user/service/application-user.service';
import { DepreciationBatchSequenceService } from '../../depreciation-batch-sequence/service/depreciation-batch-sequence.service';
import { PlaceholderService } from '../../../erp-pages/placeholder/service/placeholder.service';
import { IUniversallyUniqueMapping } from '../../../erp-pages/universally-unique-mapping/universally-unique-mapping.model';

describe('DepreciationJobNotice Management Update Component', () => {
  let comp: DepreciationJobNoticeUpdateComponent;
  let fixture: ComponentFixture<DepreciationJobNoticeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let depreciationJobNoticeService: DepreciationJobNoticeService;
  let depreciationJobService: DepreciationJobService;
  let depreciationBatchSequenceService: DepreciationBatchSequenceService;
  let depreciationPeriodService: DepreciationPeriodService;
  let placeholderService: PlaceholderService;
  let universallyUniqueMappingService: UniversallyUniqueMappingService;
  let applicationUserService: ApplicationUserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [DepreciationJobNoticeUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(DepreciationJobNoticeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DepreciationJobNoticeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    depreciationJobNoticeService = TestBed.inject(DepreciationJobNoticeService);
    depreciationJobService = TestBed.inject(DepreciationJobService);
    depreciationBatchSequenceService = TestBed.inject(DepreciationBatchSequenceService);
    depreciationPeriodService = TestBed.inject(DepreciationPeriodService);
    placeholderService = TestBed.inject(PlaceholderService);
    universallyUniqueMappingService = TestBed.inject(UniversallyUniqueMappingService);
    applicationUserService = TestBed.inject(ApplicationUserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call DepreciationJob query and add missing value', () => {
      const depreciationJobNotice: IDepreciationJobNotice = { id: 456 };
      const depreciationJob: IDepreciationJob = { id: 63358 };
      depreciationJobNotice.depreciationJob = depreciationJob;

      const depreciationJobCollection: IDepreciationJob[] = [{ id: 13416 }];
      jest.spyOn(depreciationJobService, 'query').mockReturnValue(of(new HttpResponse({ body: depreciationJobCollection })));
      const additionalDepreciationJobs = [depreciationJob];
      const expectedCollection: IDepreciationJob[] = [...additionalDepreciationJobs, ...depreciationJobCollection];
      jest.spyOn(depreciationJobService, 'addDepreciationJobToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ depreciationJobNotice });
      comp.ngOnInit();

      expect(depreciationJobService.query).toHaveBeenCalled();
      expect(depreciationJobService.addDepreciationJobToCollectionIfMissing).toHaveBeenCalledWith(
        depreciationJobCollection,
        ...additionalDepreciationJobs
      );
      expect(comp.depreciationJobsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call DepreciationBatchSequence query and add missing value', () => {
      const depreciationJobNotice: IDepreciationJobNotice = { id: 456 };
      const depreciationBatchSequence: IDepreciationBatchSequence = { id: 91663 };
      depreciationJobNotice.depreciationBatchSequence = depreciationBatchSequence;

      const depreciationBatchSequenceCollection: IDepreciationBatchSequence[] = [{ id: 82522 }];
      jest
        .spyOn(depreciationBatchSequenceService, 'query')
        .mockReturnValue(of(new HttpResponse({ body: depreciationBatchSequenceCollection })));
      const additionalDepreciationBatchSequences = [depreciationBatchSequence];
      const expectedCollection: IDepreciationBatchSequence[] = [
        ...additionalDepreciationBatchSequences,
        ...depreciationBatchSequenceCollection,
      ];
      jest.spyOn(depreciationBatchSequenceService, 'addDepreciationBatchSequenceToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ depreciationJobNotice });
      comp.ngOnInit();

      expect(depreciationBatchSequenceService.query).toHaveBeenCalled();
      expect(depreciationBatchSequenceService.addDepreciationBatchSequenceToCollectionIfMissing).toHaveBeenCalledWith(
        depreciationBatchSequenceCollection,
        ...additionalDepreciationBatchSequences
      );
      expect(comp.depreciationBatchSequencesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call DepreciationPeriod query and add missing value', () => {
      const depreciationJobNotice: IDepreciationJobNotice = { id: 456 };
      const depreciationPeriod: IDepreciationPeriod = { id: 56384 };
      depreciationJobNotice.depreciationPeriod = depreciationPeriod;

      const depreciationPeriodCollection: IDepreciationPeriod[] = [{ id: 22383 }];
      jest.spyOn(depreciationPeriodService, 'query').mockReturnValue(of(new HttpResponse({ body: depreciationPeriodCollection })));
      const additionalDepreciationPeriods = [depreciationPeriod];
      const expectedCollection: IDepreciationPeriod[] = [...additionalDepreciationPeriods, ...depreciationPeriodCollection];
      jest.spyOn(depreciationPeriodService, 'addDepreciationPeriodToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ depreciationJobNotice });
      comp.ngOnInit();

      expect(depreciationPeriodService.query).toHaveBeenCalled();
      expect(depreciationPeriodService.addDepreciationPeriodToCollectionIfMissing).toHaveBeenCalledWith(
        depreciationPeriodCollection,
        ...additionalDepreciationPeriods
      );
      expect(comp.depreciationPeriodsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Placeholder query and add missing value', () => {
      const depreciationJobNotice: IDepreciationJobNotice = { id: 456 };
      const placeholders: IPlaceholder[] = [{ id: 69239 }];
      depreciationJobNotice.placeholders = placeholders;

      const placeholderCollection: IPlaceholder[] = [{ id: 8721 }];
      jest.spyOn(placeholderService, 'query').mockReturnValue(of(new HttpResponse({ body: placeholderCollection })));
      const additionalPlaceholders = [...placeholders];
      const expectedCollection: IPlaceholder[] = [...additionalPlaceholders, ...placeholderCollection];
      jest.spyOn(placeholderService, 'addPlaceholderToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ depreciationJobNotice });
      comp.ngOnInit();

      expect(placeholderService.query).toHaveBeenCalled();
      expect(placeholderService.addPlaceholderToCollectionIfMissing).toHaveBeenCalledWith(placeholderCollection, ...additionalPlaceholders);
      expect(comp.placeholdersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call UniversallyUniqueMapping query and add missing value', () => {
      const depreciationJobNotice: IDepreciationJobNotice = { id: 456 };
      const universallyUniqueMappings: IUniversallyUniqueMapping[] = [{ id: 33976 }];
      depreciationJobNotice.universallyUniqueMappings = universallyUniqueMappings;

      const universallyUniqueMappingCollection: IUniversallyUniqueMapping[] = [{ id: 96495 }];
      jest
        .spyOn(universallyUniqueMappingService, 'query')
        .mockReturnValue(of(new HttpResponse({ body: universallyUniqueMappingCollection })));
      const additionalUniversallyUniqueMappings = [...universallyUniqueMappings];
      const expectedCollection: IUniversallyUniqueMapping[] = [
        ...additionalUniversallyUniqueMappings,
        ...universallyUniqueMappingCollection,
      ];
      jest.spyOn(universallyUniqueMappingService, 'addUniversallyUniqueMappingToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ depreciationJobNotice });
      comp.ngOnInit();

      expect(universallyUniqueMappingService.query).toHaveBeenCalled();
      expect(universallyUniqueMappingService.addUniversallyUniqueMappingToCollectionIfMissing).toHaveBeenCalledWith(
        universallyUniqueMappingCollection,
        ...additionalUniversallyUniqueMappings
      );
      expect(comp.universallyUniqueMappingsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call ApplicationUser query and add missing value', () => {
      const depreciationJobNotice: IDepreciationJobNotice = { id: 456 };
      const superintended: IApplicationUser = { id: 29535 };
      depreciationJobNotice.superintended = superintended;

      const applicationUserCollection: IApplicationUser[] = [{ id: 59149 }];
      jest.spyOn(applicationUserService, 'query').mockReturnValue(of(new HttpResponse({ body: applicationUserCollection })));
      const additionalApplicationUsers = [superintended];
      const expectedCollection: IApplicationUser[] = [...additionalApplicationUsers, ...applicationUserCollection];
      jest.spyOn(applicationUserService, 'addApplicationUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ depreciationJobNotice });
      comp.ngOnInit();

      expect(applicationUserService.query).toHaveBeenCalled();
      expect(applicationUserService.addApplicationUserToCollectionIfMissing).toHaveBeenCalledWith(
        applicationUserCollection,
        ...additionalApplicationUsers
      );
      expect(comp.applicationUsersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const depreciationJobNotice: IDepreciationJobNotice = { id: 456 };
      const depreciationJob: IDepreciationJob = { id: 22108 };
      depreciationJobNotice.depreciationJob = depreciationJob;
      const depreciationBatchSequence: IDepreciationBatchSequence = { id: 26601 };
      depreciationJobNotice.depreciationBatchSequence = depreciationBatchSequence;
      const depreciationPeriod: IDepreciationPeriod = { id: 54771 };
      depreciationJobNotice.depreciationPeriod = depreciationPeriod;
      const placeholders: IPlaceholder = { id: 34515 };
      depreciationJobNotice.placeholders = [placeholders];
      const universallyUniqueMappings: IUniversallyUniqueMapping = { id: 61494 };
      depreciationJobNotice.universallyUniqueMappings = [universallyUniqueMappings];
      const superintended: IApplicationUser = { id: 40586 };
      depreciationJobNotice.superintended = superintended;

      activatedRoute.data = of({ depreciationJobNotice });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(depreciationJobNotice));
      expect(comp.depreciationJobsSharedCollection).toContain(depreciationJob);
      expect(comp.depreciationBatchSequencesSharedCollection).toContain(depreciationBatchSequence);
      expect(comp.depreciationPeriodsSharedCollection).toContain(depreciationPeriod);
      expect(comp.placeholdersSharedCollection).toContain(placeholders);
      expect(comp.universallyUniqueMappingsSharedCollection).toContain(universallyUniqueMappings);
      expect(comp.applicationUsersSharedCollection).toContain(superintended);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<DepreciationJobNotice>>();
      const depreciationJobNotice = { id: 123 };
      jest.spyOn(depreciationJobNoticeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ depreciationJobNotice });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: depreciationJobNotice }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(depreciationJobNoticeService.update).toHaveBeenCalledWith(depreciationJobNotice);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<DepreciationJobNotice>>();
      const depreciationJobNotice = new DepreciationJobNotice();
      jest.spyOn(depreciationJobNoticeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ depreciationJobNotice });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: depreciationJobNotice }));
      saveSubject.complete();

      // THEN
      expect(depreciationJobNoticeService.create).toHaveBeenCalledWith(depreciationJobNotice);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<DepreciationJobNotice>>();
      const depreciationJobNotice = { id: 123 };
      jest.spyOn(depreciationJobNoticeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ depreciationJobNotice });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(depreciationJobNoticeService.update).toHaveBeenCalledWith(depreciationJobNotice);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackDepreciationJobById', () => {
      it('Should return tracked DepreciationJob primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackDepreciationJobById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackDepreciationBatchSequenceById', () => {
      it('Should return tracked DepreciationBatchSequence primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackDepreciationBatchSequenceById(0, entity);
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

    describe('trackPlaceholderById', () => {
      it('Should return tracked Placeholder primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackPlaceholderById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackUniversallyUniqueMappingById', () => {
      it('Should return tracked UniversallyUniqueMapping primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackUniversallyUniqueMappingById(0, entity);
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

  describe('Getting selected relationships', () => {
    describe('getSelectedPlaceholder', () => {
      it('Should return option if no Placeholder is selected', () => {
        const option = { id: 123 };
        const result = comp.getSelectedPlaceholder(option);
        expect(result === option).toEqual(true);
      });

      it('Should return selected Placeholder for according option', () => {
        const option = { id: 123 };
        const selected = { id: 123 };
        const selected2 = { id: 456 };
        const result = comp.getSelectedPlaceholder(option, [selected2, selected]);
        expect(result === selected).toEqual(true);
        expect(result === selected2).toEqual(false);
        expect(result === option).toEqual(false);
      });

      it('Should return option if this Placeholder is not selected', () => {
        const option = { id: 123 };
        const selected = { id: 456 };
        const result = comp.getSelectedPlaceholder(option, [selected]);
        expect(result === option).toEqual(true);
        expect(result === selected).toEqual(false);
      });
    });

    describe('getSelectedUniversallyUniqueMapping', () => {
      it('Should return option if no UniversallyUniqueMapping is selected', () => {
        const option = { id: 123 };
        const result = comp.getSelectedUniversallyUniqueMapping(option);
        expect(result === option).toEqual(true);
      });

      it('Should return selected UniversallyUniqueMapping for according option', () => {
        const option = { id: 123 };
        const selected = { id: 123 };
        const selected2 = { id: 456 };
        const result = comp.getSelectedUniversallyUniqueMapping(option, [selected2, selected]);
        expect(result === selected).toEqual(true);
        expect(result === selected2).toEqual(false);
        expect(result === option).toEqual(false);
      });

      it('Should return option if this UniversallyUniqueMapping is not selected', () => {
        const option = { id: 123 };
        const selected = { id: 456 };
        const result = comp.getSelectedUniversallyUniqueMapping(option, [selected]);
        expect(result === option).toEqual(true);
        expect(result === selected).toEqual(false);
      });
    });
  });
});
