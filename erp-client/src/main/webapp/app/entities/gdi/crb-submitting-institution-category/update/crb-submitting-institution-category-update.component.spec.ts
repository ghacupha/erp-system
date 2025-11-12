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

import { CrbSubmittingInstitutionCategoryService } from '../service/crb-submitting-institution-category.service';
import { ICrbSubmittingInstitutionCategory, CrbSubmittingInstitutionCategory } from '../crb-submitting-institution-category.model';

import { CrbSubmittingInstitutionCategoryUpdateComponent } from './crb-submitting-institution-category-update.component';

describe('CrbSubmittingInstitutionCategory Management Update Component', () => {
  let comp: CrbSubmittingInstitutionCategoryUpdateComponent;
  let fixture: ComponentFixture<CrbSubmittingInstitutionCategoryUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let crbSubmittingInstitutionCategoryService: CrbSubmittingInstitutionCategoryService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [CrbSubmittingInstitutionCategoryUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(CrbSubmittingInstitutionCategoryUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CrbSubmittingInstitutionCategoryUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    crbSubmittingInstitutionCategoryService = TestBed.inject(CrbSubmittingInstitutionCategoryService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const crbSubmittingInstitutionCategory: ICrbSubmittingInstitutionCategory = { id: 456 };

      activatedRoute.data = of({ crbSubmittingInstitutionCategory });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(crbSubmittingInstitutionCategory));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CrbSubmittingInstitutionCategory>>();
      const crbSubmittingInstitutionCategory = { id: 123 };
      jest.spyOn(crbSubmittingInstitutionCategoryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ crbSubmittingInstitutionCategory });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: crbSubmittingInstitutionCategory }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(crbSubmittingInstitutionCategoryService.update).toHaveBeenCalledWith(crbSubmittingInstitutionCategory);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CrbSubmittingInstitutionCategory>>();
      const crbSubmittingInstitutionCategory = new CrbSubmittingInstitutionCategory();
      jest.spyOn(crbSubmittingInstitutionCategoryService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ crbSubmittingInstitutionCategory });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: crbSubmittingInstitutionCategory }));
      saveSubject.complete();

      // THEN
      expect(crbSubmittingInstitutionCategoryService.create).toHaveBeenCalledWith(crbSubmittingInstitutionCategory);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CrbSubmittingInstitutionCategory>>();
      const crbSubmittingInstitutionCategory = { id: 123 };
      jest.spyOn(crbSubmittingInstitutionCategoryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ crbSubmittingInstitutionCategory });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(crbSubmittingInstitutionCategoryService.update).toHaveBeenCalledWith(crbSubmittingInstitutionCategory);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
