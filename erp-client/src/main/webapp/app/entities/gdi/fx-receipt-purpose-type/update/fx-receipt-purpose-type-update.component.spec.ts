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

import { FxReceiptPurposeTypeService } from '../service/fx-receipt-purpose-type.service';
import { IFxReceiptPurposeType, FxReceiptPurposeType } from '../fx-receipt-purpose-type.model';

import { FxReceiptPurposeTypeUpdateComponent } from './fx-receipt-purpose-type-update.component';

describe('FxReceiptPurposeType Management Update Component', () => {
  let comp: FxReceiptPurposeTypeUpdateComponent;
  let fixture: ComponentFixture<FxReceiptPurposeTypeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let fxReceiptPurposeTypeService: FxReceiptPurposeTypeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [FxReceiptPurposeTypeUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(FxReceiptPurposeTypeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FxReceiptPurposeTypeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    fxReceiptPurposeTypeService = TestBed.inject(FxReceiptPurposeTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const fxReceiptPurposeType: IFxReceiptPurposeType = { id: 456 };

      activatedRoute.data = of({ fxReceiptPurposeType });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(fxReceiptPurposeType));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<FxReceiptPurposeType>>();
      const fxReceiptPurposeType = { id: 123 };
      jest.spyOn(fxReceiptPurposeTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ fxReceiptPurposeType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: fxReceiptPurposeType }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(fxReceiptPurposeTypeService.update).toHaveBeenCalledWith(fxReceiptPurposeType);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<FxReceiptPurposeType>>();
      const fxReceiptPurposeType = new FxReceiptPurposeType();
      jest.spyOn(fxReceiptPurposeTypeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ fxReceiptPurposeType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: fxReceiptPurposeType }));
      saveSubject.complete();

      // THEN
      expect(fxReceiptPurposeTypeService.create).toHaveBeenCalledWith(fxReceiptPurposeType);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<FxReceiptPurposeType>>();
      const fxReceiptPurposeType = { id: 123 };
      jest.spyOn(fxReceiptPurposeTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ fxReceiptPurposeType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(fxReceiptPurposeTypeService.update).toHaveBeenCalledWith(fxReceiptPurposeType);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
