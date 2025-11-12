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

import { CrbSourceOfInformationTypeService } from '../service/crb-source-of-information-type.service';
import { ICrbSourceOfInformationType, CrbSourceOfInformationType } from '../crb-source-of-information-type.model';

import { CrbSourceOfInformationTypeUpdateComponent } from './crb-source-of-information-type-update.component';

describe('CrbSourceOfInformationType Management Update Component', () => {
  let comp: CrbSourceOfInformationTypeUpdateComponent;
  let fixture: ComponentFixture<CrbSourceOfInformationTypeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let crbSourceOfInformationTypeService: CrbSourceOfInformationTypeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [CrbSourceOfInformationTypeUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(CrbSourceOfInformationTypeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CrbSourceOfInformationTypeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    crbSourceOfInformationTypeService = TestBed.inject(CrbSourceOfInformationTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const crbSourceOfInformationType: ICrbSourceOfInformationType = { id: 456 };

      activatedRoute.data = of({ crbSourceOfInformationType });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(crbSourceOfInformationType));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CrbSourceOfInformationType>>();
      const crbSourceOfInformationType = { id: 123 };
      jest.spyOn(crbSourceOfInformationTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ crbSourceOfInformationType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: crbSourceOfInformationType }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(crbSourceOfInformationTypeService.update).toHaveBeenCalledWith(crbSourceOfInformationType);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CrbSourceOfInformationType>>();
      const crbSourceOfInformationType = new CrbSourceOfInformationType();
      jest.spyOn(crbSourceOfInformationTypeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ crbSourceOfInformationType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: crbSourceOfInformationType }));
      saveSubject.complete();

      // THEN
      expect(crbSourceOfInformationTypeService.create).toHaveBeenCalledWith(crbSourceOfInformationType);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CrbSourceOfInformationType>>();
      const crbSourceOfInformationType = { id: 123 };
      jest.spyOn(crbSourceOfInformationTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ crbSourceOfInformationType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(crbSourceOfInformationTypeService.update).toHaveBeenCalledWith(crbSourceOfInformationType);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
