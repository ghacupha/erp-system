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

import { ShareholderTypeService } from '../service/shareholder-type.service';
import { IShareholderType, ShareholderType } from '../shareholder-type.model';

import { ShareholderTypeUpdateComponent } from './shareholder-type-update.component';

describe('ShareholderType Management Update Component', () => {
  let comp: ShareholderTypeUpdateComponent;
  let fixture: ComponentFixture<ShareholderTypeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let shareholderTypeService: ShareholderTypeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [ShareholderTypeUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(ShareholderTypeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ShareholderTypeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    shareholderTypeService = TestBed.inject(ShareholderTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const shareholderType: IShareholderType = { id: 456 };

      activatedRoute.data = of({ shareholderType });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(shareholderType));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ShareholderType>>();
      const shareholderType = { id: 123 };
      jest.spyOn(shareholderTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ shareholderType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: shareholderType }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(shareholderTypeService.update).toHaveBeenCalledWith(shareholderType);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ShareholderType>>();
      const shareholderType = new ShareholderType();
      jest.spyOn(shareholderTypeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ shareholderType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: shareholderType }));
      saveSubject.complete();

      // THEN
      expect(shareholderTypeService.create).toHaveBeenCalledWith(shareholderType);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ShareholderType>>();
      const shareholderType = { id: 123 };
      jest.spyOn(shareholderTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ shareholderType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(shareholderTypeService.update).toHaveBeenCalledWith(shareholderType);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
