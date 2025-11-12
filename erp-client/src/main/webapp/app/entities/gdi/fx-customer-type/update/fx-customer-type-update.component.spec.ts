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

import { FxCustomerTypeService } from '../service/fx-customer-type.service';
import { IFxCustomerType, FxCustomerType } from '../fx-customer-type.model';

import { FxCustomerTypeUpdateComponent } from './fx-customer-type-update.component';

describe('FxCustomerType Management Update Component', () => {
  let comp: FxCustomerTypeUpdateComponent;
  let fixture: ComponentFixture<FxCustomerTypeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let fxCustomerTypeService: FxCustomerTypeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [FxCustomerTypeUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(FxCustomerTypeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FxCustomerTypeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    fxCustomerTypeService = TestBed.inject(FxCustomerTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const fxCustomerType: IFxCustomerType = { id: 456 };

      activatedRoute.data = of({ fxCustomerType });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(fxCustomerType));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<FxCustomerType>>();
      const fxCustomerType = { id: 123 };
      jest.spyOn(fxCustomerTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ fxCustomerType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: fxCustomerType }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(fxCustomerTypeService.update).toHaveBeenCalledWith(fxCustomerType);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<FxCustomerType>>();
      const fxCustomerType = new FxCustomerType();
      jest.spyOn(fxCustomerTypeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ fxCustomerType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: fxCustomerType }));
      saveSubject.complete();

      // THEN
      expect(fxCustomerTypeService.create).toHaveBeenCalledWith(fxCustomerType);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<FxCustomerType>>();
      const fxCustomerType = { id: 123 };
      jest.spyOn(fxCustomerTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ fxCustomerType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(fxCustomerTypeService.update).toHaveBeenCalledWith(fxCustomerType);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
