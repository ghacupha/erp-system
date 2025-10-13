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

import { FinancialDerivativeTypeCodeService } from '../service/financial-derivative-type-code.service';
import { IFinancialDerivativeTypeCode, FinancialDerivativeTypeCode } from '../financial-derivative-type-code.model';

import { FinancialDerivativeTypeCodeUpdateComponent } from './financial-derivative-type-code-update.component';

describe('FinancialDerivativeTypeCode Management Update Component', () => {
  let comp: FinancialDerivativeTypeCodeUpdateComponent;
  let fixture: ComponentFixture<FinancialDerivativeTypeCodeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let financialDerivativeTypeCodeService: FinancialDerivativeTypeCodeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [FinancialDerivativeTypeCodeUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(FinancialDerivativeTypeCodeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FinancialDerivativeTypeCodeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    financialDerivativeTypeCodeService = TestBed.inject(FinancialDerivativeTypeCodeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const financialDerivativeTypeCode: IFinancialDerivativeTypeCode = { id: 456 };

      activatedRoute.data = of({ financialDerivativeTypeCode });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(financialDerivativeTypeCode));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<FinancialDerivativeTypeCode>>();
      const financialDerivativeTypeCode = { id: 123 };
      jest.spyOn(financialDerivativeTypeCodeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ financialDerivativeTypeCode });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: financialDerivativeTypeCode }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(financialDerivativeTypeCodeService.update).toHaveBeenCalledWith(financialDerivativeTypeCode);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<FinancialDerivativeTypeCode>>();
      const financialDerivativeTypeCode = new FinancialDerivativeTypeCode();
      jest.spyOn(financialDerivativeTypeCodeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ financialDerivativeTypeCode });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: financialDerivativeTypeCode }));
      saveSubject.complete();

      // THEN
      expect(financialDerivativeTypeCodeService.create).toHaveBeenCalledWith(financialDerivativeTypeCode);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<FinancialDerivativeTypeCode>>();
      const financialDerivativeTypeCode = { id: 123 };
      jest.spyOn(financialDerivativeTypeCodeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ financialDerivativeTypeCode });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(financialDerivativeTypeCodeService.update).toHaveBeenCalledWith(financialDerivativeTypeCode);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
