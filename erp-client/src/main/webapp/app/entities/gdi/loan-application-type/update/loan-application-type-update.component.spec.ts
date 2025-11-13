///
/// Erp System - Mark X No 11 (Jehoiada Series) Client 1.7.9
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

import { LoanApplicationTypeService } from '../service/loan-application-type.service';
import { ILoanApplicationType, LoanApplicationType } from '../loan-application-type.model';

import { LoanApplicationTypeUpdateComponent } from './loan-application-type-update.component';

describe('LoanApplicationType Management Update Component', () => {
  let comp: LoanApplicationTypeUpdateComponent;
  let fixture: ComponentFixture<LoanApplicationTypeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let loanApplicationTypeService: LoanApplicationTypeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [LoanApplicationTypeUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(LoanApplicationTypeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(LoanApplicationTypeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    loanApplicationTypeService = TestBed.inject(LoanApplicationTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const loanApplicationType: ILoanApplicationType = { id: 456 };

      activatedRoute.data = of({ loanApplicationType });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(loanApplicationType));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<LoanApplicationType>>();
      const loanApplicationType = { id: 123 };
      jest.spyOn(loanApplicationTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ loanApplicationType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: loanApplicationType }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(loanApplicationTypeService.update).toHaveBeenCalledWith(loanApplicationType);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<LoanApplicationType>>();
      const loanApplicationType = new LoanApplicationType();
      jest.spyOn(loanApplicationTypeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ loanApplicationType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: loanApplicationType }));
      saveSubject.complete();

      // THEN
      expect(loanApplicationTypeService.create).toHaveBeenCalledWith(loanApplicationType);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<LoanApplicationType>>();
      const loanApplicationType = { id: 123 };
      jest.spyOn(loanApplicationTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ loanApplicationType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(loanApplicationTypeService.update).toHaveBeenCalledWith(loanApplicationType);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
