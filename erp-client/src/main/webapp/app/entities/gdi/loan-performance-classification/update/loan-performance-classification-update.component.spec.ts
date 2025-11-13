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

import { LoanPerformanceClassificationService } from '../service/loan-performance-classification.service';
import { ILoanPerformanceClassification, LoanPerformanceClassification } from '../loan-performance-classification.model';

import { LoanPerformanceClassificationUpdateComponent } from './loan-performance-classification-update.component';

describe('LoanPerformanceClassification Management Update Component', () => {
  let comp: LoanPerformanceClassificationUpdateComponent;
  let fixture: ComponentFixture<LoanPerformanceClassificationUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let loanPerformanceClassificationService: LoanPerformanceClassificationService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [LoanPerformanceClassificationUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(LoanPerformanceClassificationUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(LoanPerformanceClassificationUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    loanPerformanceClassificationService = TestBed.inject(LoanPerformanceClassificationService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const loanPerformanceClassification: ILoanPerformanceClassification = { id: 456 };

      activatedRoute.data = of({ loanPerformanceClassification });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(loanPerformanceClassification));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<LoanPerformanceClassification>>();
      const loanPerformanceClassification = { id: 123 };
      jest.spyOn(loanPerformanceClassificationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ loanPerformanceClassification });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: loanPerformanceClassification }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(loanPerformanceClassificationService.update).toHaveBeenCalledWith(loanPerformanceClassification);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<LoanPerformanceClassification>>();
      const loanPerformanceClassification = new LoanPerformanceClassification();
      jest.spyOn(loanPerformanceClassificationService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ loanPerformanceClassification });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: loanPerformanceClassification }));
      saveSubject.complete();

      // THEN
      expect(loanPerformanceClassificationService.create).toHaveBeenCalledWith(loanPerformanceClassification);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<LoanPerformanceClassification>>();
      const loanPerformanceClassification = { id: 123 };
      jest.spyOn(loanPerformanceClassificationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ loanPerformanceClassification });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(loanPerformanceClassificationService.update).toHaveBeenCalledWith(loanPerformanceClassification);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
