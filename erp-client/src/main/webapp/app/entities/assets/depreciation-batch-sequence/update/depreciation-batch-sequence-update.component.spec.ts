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

jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { DepreciationBatchSequenceService } from '../service/depreciation-batch-sequence.service';
import { IDepreciationBatchSequence, DepreciationBatchSequence } from '../depreciation-batch-sequence.model';
import { IDepreciationJob } from 'app/entities/assets/depreciation-job/depreciation-job.model';
import { DepreciationJobService } from 'app/entities/assets/depreciation-job/service/depreciation-job.service';

import { DepreciationBatchSequenceUpdateComponent } from './depreciation-batch-sequence-update.component';

describe('DepreciationBatchSequence Management Update Component', () => {
  let comp: DepreciationBatchSequenceUpdateComponent;
  let fixture: ComponentFixture<DepreciationBatchSequenceUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let depreciationBatchSequenceService: DepreciationBatchSequenceService;
  let depreciationJobService: DepreciationJobService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [DepreciationBatchSequenceUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(DepreciationBatchSequenceUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DepreciationBatchSequenceUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    depreciationBatchSequenceService = TestBed.inject(DepreciationBatchSequenceService);
    depreciationJobService = TestBed.inject(DepreciationJobService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call DepreciationJob query and add missing value', () => {
      const depreciationBatchSequence: IDepreciationBatchSequence = { id: 456 };
      const depreciationJob: IDepreciationJob = { id: 11286 };
      depreciationBatchSequence.depreciationJob = depreciationJob;

      const depreciationJobCollection: IDepreciationJob[] = [{ id: 32255 }];
      jest.spyOn(depreciationJobService, 'query').mockReturnValue(of(new HttpResponse({ body: depreciationJobCollection })));
      const additionalDepreciationJobs = [depreciationJob];
      const expectedCollection: IDepreciationJob[] = [...additionalDepreciationJobs, ...depreciationJobCollection];
      jest.spyOn(depreciationJobService, 'addDepreciationJobToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ depreciationBatchSequence });
      comp.ngOnInit();

      expect(depreciationJobService.query).toHaveBeenCalled();
      expect(depreciationJobService.addDepreciationJobToCollectionIfMissing).toHaveBeenCalledWith(
        depreciationJobCollection,
        ...additionalDepreciationJobs
      );
      expect(comp.depreciationJobsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const depreciationBatchSequence: IDepreciationBatchSequence = { id: 456 };
      const depreciationJob: IDepreciationJob = { id: 2752 };
      depreciationBatchSequence.depreciationJob = depreciationJob;

      activatedRoute.data = of({ depreciationBatchSequence });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(depreciationBatchSequence));
      expect(comp.depreciationJobsSharedCollection).toContain(depreciationJob);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<DepreciationBatchSequence>>();
      const depreciationBatchSequence = { id: 123 };
      jest.spyOn(depreciationBatchSequenceService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ depreciationBatchSequence });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: depreciationBatchSequence }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(depreciationBatchSequenceService.update).toHaveBeenCalledWith(depreciationBatchSequence);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<DepreciationBatchSequence>>();
      const depreciationBatchSequence = new DepreciationBatchSequence();
      jest.spyOn(depreciationBatchSequenceService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ depreciationBatchSequence });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: depreciationBatchSequence }));
      saveSubject.complete();

      // THEN
      expect(depreciationBatchSequenceService.create).toHaveBeenCalledWith(depreciationBatchSequence);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<DepreciationBatchSequence>>();
      const depreciationBatchSequence = { id: 123 };
      jest.spyOn(depreciationBatchSequenceService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ depreciationBatchSequence });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(depreciationBatchSequenceService.update).toHaveBeenCalledWith(depreciationBatchSequence);
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
  });
});
