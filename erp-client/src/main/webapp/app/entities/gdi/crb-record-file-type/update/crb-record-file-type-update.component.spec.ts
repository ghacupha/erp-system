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

import { CrbRecordFileTypeService } from '../service/crb-record-file-type.service';
import { ICrbRecordFileType, CrbRecordFileType } from '../crb-record-file-type.model';

import { CrbRecordFileTypeUpdateComponent } from './crb-record-file-type-update.component';

describe('CrbRecordFileType Management Update Component', () => {
  let comp: CrbRecordFileTypeUpdateComponent;
  let fixture: ComponentFixture<CrbRecordFileTypeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let crbRecordFileTypeService: CrbRecordFileTypeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [CrbRecordFileTypeUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(CrbRecordFileTypeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CrbRecordFileTypeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    crbRecordFileTypeService = TestBed.inject(CrbRecordFileTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const crbRecordFileType: ICrbRecordFileType = { id: 456 };

      activatedRoute.data = of({ crbRecordFileType });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(crbRecordFileType));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CrbRecordFileType>>();
      const crbRecordFileType = { id: 123 };
      jest.spyOn(crbRecordFileTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ crbRecordFileType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: crbRecordFileType }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(crbRecordFileTypeService.update).toHaveBeenCalledWith(crbRecordFileType);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CrbRecordFileType>>();
      const crbRecordFileType = new CrbRecordFileType();
      jest.spyOn(crbRecordFileTypeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ crbRecordFileType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: crbRecordFileType }));
      saveSubject.complete();

      // THEN
      expect(crbRecordFileTypeService.create).toHaveBeenCalledWith(crbRecordFileType);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CrbRecordFileType>>();
      const crbRecordFileType = { id: 123 };
      jest.spyOn(crbRecordFileTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ crbRecordFileType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(crbRecordFileTypeService.update).toHaveBeenCalledWith(crbRecordFileType);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
