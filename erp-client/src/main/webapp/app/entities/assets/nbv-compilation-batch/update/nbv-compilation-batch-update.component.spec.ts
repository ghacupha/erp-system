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

import { NbvCompilationBatchService } from '../service/nbv-compilation-batch.service';
import { INbvCompilationBatch, NbvCompilationBatch } from '../nbv-compilation-batch.model';
import { INbvCompilationJob } from 'app/entities/assets/nbv-compilation-job/nbv-compilation-job.model';
import { NbvCompilationJobService } from 'app/entities/assets/nbv-compilation-job/service/nbv-compilation-job.service';

import { NbvCompilationBatchUpdateComponent } from './nbv-compilation-batch-update.component';

describe('NbvCompilationBatch Management Update Component', () => {
  let comp: NbvCompilationBatchUpdateComponent;
  let fixture: ComponentFixture<NbvCompilationBatchUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let nbvCompilationBatchService: NbvCompilationBatchService;
  let nbvCompilationJobService: NbvCompilationJobService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [NbvCompilationBatchUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(NbvCompilationBatchUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(NbvCompilationBatchUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    nbvCompilationBatchService = TestBed.inject(NbvCompilationBatchService);
    nbvCompilationJobService = TestBed.inject(NbvCompilationJobService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call NbvCompilationJob query and add missing value', () => {
      const nbvCompilationBatch: INbvCompilationBatch = { id: 456 };
      const nbvCompilationJob: INbvCompilationJob = { id: 93355 };
      nbvCompilationBatch.nbvCompilationJob = nbvCompilationJob;

      const nbvCompilationJobCollection: INbvCompilationJob[] = [{ id: 74590 }];
      jest.spyOn(nbvCompilationJobService, 'query').mockReturnValue(of(new HttpResponse({ body: nbvCompilationJobCollection })));
      const additionalNbvCompilationJobs = [nbvCompilationJob];
      const expectedCollection: INbvCompilationJob[] = [...additionalNbvCompilationJobs, ...nbvCompilationJobCollection];
      jest.spyOn(nbvCompilationJobService, 'addNbvCompilationJobToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ nbvCompilationBatch });
      comp.ngOnInit();

      expect(nbvCompilationJobService.query).toHaveBeenCalled();
      expect(nbvCompilationJobService.addNbvCompilationJobToCollectionIfMissing).toHaveBeenCalledWith(
        nbvCompilationJobCollection,
        ...additionalNbvCompilationJobs
      );
      expect(comp.nbvCompilationJobsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const nbvCompilationBatch: INbvCompilationBatch = { id: 456 };
      const nbvCompilationJob: INbvCompilationJob = { id: 451 };
      nbvCompilationBatch.nbvCompilationJob = nbvCompilationJob;

      activatedRoute.data = of({ nbvCompilationBatch });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(nbvCompilationBatch));
      expect(comp.nbvCompilationJobsSharedCollection).toContain(nbvCompilationJob);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<NbvCompilationBatch>>();
      const nbvCompilationBatch = { id: 123 };
      jest.spyOn(nbvCompilationBatchService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ nbvCompilationBatch });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: nbvCompilationBatch }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(nbvCompilationBatchService.update).toHaveBeenCalledWith(nbvCompilationBatch);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<NbvCompilationBatch>>();
      const nbvCompilationBatch = new NbvCompilationBatch();
      jest.spyOn(nbvCompilationBatchService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ nbvCompilationBatch });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: nbvCompilationBatch }));
      saveSubject.complete();

      // THEN
      expect(nbvCompilationBatchService.create).toHaveBeenCalledWith(nbvCompilationBatch);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<NbvCompilationBatch>>();
      const nbvCompilationBatch = { id: 123 };
      jest.spyOn(nbvCompilationBatchService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ nbvCompilationBatch });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(nbvCompilationBatchService.update).toHaveBeenCalledWith(nbvCompilationBatch);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackNbvCompilationJobById', () => {
      it('Should return tracked NbvCompilationJob primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackNbvCompilationJobById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
