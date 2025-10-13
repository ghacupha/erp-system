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

import { LoanRepaymentFrequencyService } from '../service/loan-repayment-frequency.service';
import { ILoanRepaymentFrequency, LoanRepaymentFrequency } from '../loan-repayment-frequency.model';

import { LoanRepaymentFrequencyUpdateComponent } from './loan-repayment-frequency-update.component';

describe('LoanRepaymentFrequency Management Update Component', () => {
  let comp: LoanRepaymentFrequencyUpdateComponent;
  let fixture: ComponentFixture<LoanRepaymentFrequencyUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let loanRepaymentFrequencyService: LoanRepaymentFrequencyService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [LoanRepaymentFrequencyUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(LoanRepaymentFrequencyUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(LoanRepaymentFrequencyUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    loanRepaymentFrequencyService = TestBed.inject(LoanRepaymentFrequencyService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const loanRepaymentFrequency: ILoanRepaymentFrequency = { id: 456 };

      activatedRoute.data = of({ loanRepaymentFrequency });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(loanRepaymentFrequency));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<LoanRepaymentFrequency>>();
      const loanRepaymentFrequency = { id: 123 };
      jest.spyOn(loanRepaymentFrequencyService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ loanRepaymentFrequency });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: loanRepaymentFrequency }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(loanRepaymentFrequencyService.update).toHaveBeenCalledWith(loanRepaymentFrequency);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<LoanRepaymentFrequency>>();
      const loanRepaymentFrequency = new LoanRepaymentFrequency();
      jest.spyOn(loanRepaymentFrequencyService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ loanRepaymentFrequency });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: loanRepaymentFrequency }));
      saveSubject.complete();

      // THEN
      expect(loanRepaymentFrequencyService.create).toHaveBeenCalledWith(loanRepaymentFrequency);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<LoanRepaymentFrequency>>();
      const loanRepaymentFrequency = { id: 123 };
      jest.spyOn(loanRepaymentFrequencyService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ loanRepaymentFrequency });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(loanRepaymentFrequencyService.update).toHaveBeenCalledWith(loanRepaymentFrequency);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
