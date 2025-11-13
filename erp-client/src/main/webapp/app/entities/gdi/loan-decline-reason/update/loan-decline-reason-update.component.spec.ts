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

import { LoanDeclineReasonService } from '../service/loan-decline-reason.service';
import { ILoanDeclineReason, LoanDeclineReason } from '../loan-decline-reason.model';

import { LoanDeclineReasonUpdateComponent } from './loan-decline-reason-update.component';

describe('LoanDeclineReason Management Update Component', () => {
  let comp: LoanDeclineReasonUpdateComponent;
  let fixture: ComponentFixture<LoanDeclineReasonUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let loanDeclineReasonService: LoanDeclineReasonService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [LoanDeclineReasonUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(LoanDeclineReasonUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(LoanDeclineReasonUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    loanDeclineReasonService = TestBed.inject(LoanDeclineReasonService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const loanDeclineReason: ILoanDeclineReason = { id: 456 };

      activatedRoute.data = of({ loanDeclineReason });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(loanDeclineReason));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<LoanDeclineReason>>();
      const loanDeclineReason = { id: 123 };
      jest.spyOn(loanDeclineReasonService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ loanDeclineReason });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: loanDeclineReason }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(loanDeclineReasonService.update).toHaveBeenCalledWith(loanDeclineReason);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<LoanDeclineReason>>();
      const loanDeclineReason = new LoanDeclineReason();
      jest.spyOn(loanDeclineReasonService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ loanDeclineReason });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: loanDeclineReason }));
      saveSubject.complete();

      // THEN
      expect(loanDeclineReasonService.create).toHaveBeenCalledWith(loanDeclineReason);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<LoanDeclineReason>>();
      const loanDeclineReason = { id: 123 };
      jest.spyOn(loanDeclineReasonService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ loanDeclineReason });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(loanDeclineReasonService.update).toHaveBeenCalledWith(loanDeclineReason);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
