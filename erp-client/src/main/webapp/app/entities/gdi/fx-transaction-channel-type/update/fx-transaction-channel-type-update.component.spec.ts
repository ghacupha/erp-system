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

import { FxTransactionChannelTypeService } from '../service/fx-transaction-channel-type.service';
import { IFxTransactionChannelType, FxTransactionChannelType } from '../fx-transaction-channel-type.model';

import { FxTransactionChannelTypeUpdateComponent } from './fx-transaction-channel-type-update.component';

describe('FxTransactionChannelType Management Update Component', () => {
  let comp: FxTransactionChannelTypeUpdateComponent;
  let fixture: ComponentFixture<FxTransactionChannelTypeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let fxTransactionChannelTypeService: FxTransactionChannelTypeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [FxTransactionChannelTypeUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(FxTransactionChannelTypeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FxTransactionChannelTypeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    fxTransactionChannelTypeService = TestBed.inject(FxTransactionChannelTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const fxTransactionChannelType: IFxTransactionChannelType = { id: 456 };

      activatedRoute.data = of({ fxTransactionChannelType });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(fxTransactionChannelType));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<FxTransactionChannelType>>();
      const fxTransactionChannelType = { id: 123 };
      jest.spyOn(fxTransactionChannelTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ fxTransactionChannelType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: fxTransactionChannelType }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(fxTransactionChannelTypeService.update).toHaveBeenCalledWith(fxTransactionChannelType);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<FxTransactionChannelType>>();
      const fxTransactionChannelType = new FxTransactionChannelType();
      jest.spyOn(fxTransactionChannelTypeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ fxTransactionChannelType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: fxTransactionChannelType }));
      saveSubject.complete();

      // THEN
      expect(fxTransactionChannelTypeService.create).toHaveBeenCalledWith(fxTransactionChannelType);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<FxTransactionChannelType>>();
      const fxTransactionChannelType = { id: 123 };
      jest.spyOn(fxTransactionChannelTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ fxTransactionChannelType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(fxTransactionChannelTypeService.update).toHaveBeenCalledWith(fxTransactionChannelType);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
