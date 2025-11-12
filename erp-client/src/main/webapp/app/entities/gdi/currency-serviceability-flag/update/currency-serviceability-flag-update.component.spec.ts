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

import { CurrencyServiceabilityFlagService } from '../service/currency-serviceability-flag.service';
import { ICurrencyServiceabilityFlag, CurrencyServiceabilityFlag } from '../currency-serviceability-flag.model';

import { CurrencyServiceabilityFlagUpdateComponent } from './currency-serviceability-flag-update.component';

describe('CurrencyServiceabilityFlag Management Update Component', () => {
  let comp: CurrencyServiceabilityFlagUpdateComponent;
  let fixture: ComponentFixture<CurrencyServiceabilityFlagUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let currencyServiceabilityFlagService: CurrencyServiceabilityFlagService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [CurrencyServiceabilityFlagUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(CurrencyServiceabilityFlagUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CurrencyServiceabilityFlagUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    currencyServiceabilityFlagService = TestBed.inject(CurrencyServiceabilityFlagService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const currencyServiceabilityFlag: ICurrencyServiceabilityFlag = { id: 456 };

      activatedRoute.data = of({ currencyServiceabilityFlag });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(currencyServiceabilityFlag));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CurrencyServiceabilityFlag>>();
      const currencyServiceabilityFlag = { id: 123 };
      jest.spyOn(currencyServiceabilityFlagService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ currencyServiceabilityFlag });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: currencyServiceabilityFlag }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(currencyServiceabilityFlagService.update).toHaveBeenCalledWith(currencyServiceabilityFlag);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CurrencyServiceabilityFlag>>();
      const currencyServiceabilityFlag = new CurrencyServiceabilityFlag();
      jest.spyOn(currencyServiceabilityFlagService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ currencyServiceabilityFlag });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: currencyServiceabilityFlag }));
      saveSubject.complete();

      // THEN
      expect(currencyServiceabilityFlagService.create).toHaveBeenCalledWith(currencyServiceabilityFlag);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CurrencyServiceabilityFlag>>();
      const currencyServiceabilityFlag = { id: 123 };
      jest.spyOn(currencyServiceabilityFlagService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ currencyServiceabilityFlag });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(currencyServiceabilityFlagService.update).toHaveBeenCalledWith(currencyServiceabilityFlag);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
