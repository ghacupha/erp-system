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

import { ExecutiveCategoryTypeService } from '../service/executive-category-type.service';
import { IExecutiveCategoryType, ExecutiveCategoryType } from '../executive-category-type.model';

import { ExecutiveCategoryTypeUpdateComponent } from './executive-category-type-update.component';

describe('ExecutiveCategoryType Management Update Component', () => {
  let comp: ExecutiveCategoryTypeUpdateComponent;
  let fixture: ComponentFixture<ExecutiveCategoryTypeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let executiveCategoryTypeService: ExecutiveCategoryTypeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [ExecutiveCategoryTypeUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(ExecutiveCategoryTypeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ExecutiveCategoryTypeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    executiveCategoryTypeService = TestBed.inject(ExecutiveCategoryTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const executiveCategoryType: IExecutiveCategoryType = { id: 456 };

      activatedRoute.data = of({ executiveCategoryType });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(executiveCategoryType));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ExecutiveCategoryType>>();
      const executiveCategoryType = { id: 123 };
      jest.spyOn(executiveCategoryTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ executiveCategoryType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: executiveCategoryType }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(executiveCategoryTypeService.update).toHaveBeenCalledWith(executiveCategoryType);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ExecutiveCategoryType>>();
      const executiveCategoryType = new ExecutiveCategoryType();
      jest.spyOn(executiveCategoryTypeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ executiveCategoryType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: executiveCategoryType }));
      saveSubject.complete();

      // THEN
      expect(executiveCategoryTypeService.create).toHaveBeenCalledWith(executiveCategoryType);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ExecutiveCategoryType>>();
      const executiveCategoryType = { id: 123 };
      jest.spyOn(executiveCategoryTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ executiveCategoryType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(executiveCategoryTypeService.update).toHaveBeenCalledWith(executiveCategoryType);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
