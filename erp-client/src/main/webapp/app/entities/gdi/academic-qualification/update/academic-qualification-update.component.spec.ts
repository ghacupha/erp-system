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

import { AcademicQualificationService } from '../service/academic-qualification.service';
import { IAcademicQualification, AcademicQualification } from '../academic-qualification.model';

import { AcademicQualificationUpdateComponent } from './academic-qualification-update.component';

describe('AcademicQualification Management Update Component', () => {
  let comp: AcademicQualificationUpdateComponent;
  let fixture: ComponentFixture<AcademicQualificationUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let academicQualificationService: AcademicQualificationService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [AcademicQualificationUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(AcademicQualificationUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AcademicQualificationUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    academicQualificationService = TestBed.inject(AcademicQualificationService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const academicQualification: IAcademicQualification = { id: 456 };

      activatedRoute.data = of({ academicQualification });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(academicQualification));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<AcademicQualification>>();
      const academicQualification = { id: 123 };
      jest.spyOn(academicQualificationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ academicQualification });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: academicQualification }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(academicQualificationService.update).toHaveBeenCalledWith(academicQualification);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<AcademicQualification>>();
      const academicQualification = new AcademicQualification();
      jest.spyOn(academicQualificationService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ academicQualification });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: academicQualification }));
      saveSubject.complete();

      // THEN
      expect(academicQualificationService.create).toHaveBeenCalledWith(academicQualification);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<AcademicQualification>>();
      const academicQualification = { id: 123 };
      jest.spyOn(academicQualificationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ academicQualification });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(academicQualificationService.update).toHaveBeenCalledWith(academicQualification);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
