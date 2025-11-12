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

import { CollateralTypeService } from '../service/collateral-type.service';
import { ICollateralType, CollateralType } from '../collateral-type.model';

import { CollateralTypeUpdateComponent } from './collateral-type-update.component';

describe('CollateralType Management Update Component', () => {
  let comp: CollateralTypeUpdateComponent;
  let fixture: ComponentFixture<CollateralTypeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let collateralTypeService: CollateralTypeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [CollateralTypeUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(CollateralTypeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CollateralTypeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    collateralTypeService = TestBed.inject(CollateralTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const collateralType: ICollateralType = { id: 456 };

      activatedRoute.data = of({ collateralType });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(collateralType));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CollateralType>>();
      const collateralType = { id: 123 };
      jest.spyOn(collateralTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ collateralType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: collateralType }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(collateralTypeService.update).toHaveBeenCalledWith(collateralType);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CollateralType>>();
      const collateralType = new CollateralType();
      jest.spyOn(collateralTypeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ collateralType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: collateralType }));
      saveSubject.complete();

      // THEN
      expect(collateralTypeService.create).toHaveBeenCalledWith(collateralType);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CollateralType>>();
      const collateralType = { id: 123 };
      jest.spyOn(collateralTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ collateralType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(collateralTypeService.update).toHaveBeenCalledWith(collateralType);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
