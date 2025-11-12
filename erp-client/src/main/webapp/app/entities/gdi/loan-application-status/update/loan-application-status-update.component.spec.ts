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

import { LoanApplicationStatusService } from '../service/loan-application-status.service';
import { ILoanApplicationStatus, LoanApplicationStatus } from '../loan-application-status.model';

import { LoanApplicationStatusUpdateComponent } from './loan-application-status-update.component';

describe('LoanApplicationStatus Management Update Component', () => {
  let comp: LoanApplicationStatusUpdateComponent;
  let fixture: ComponentFixture<LoanApplicationStatusUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let loanApplicationStatusService: LoanApplicationStatusService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [LoanApplicationStatusUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(LoanApplicationStatusUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(LoanApplicationStatusUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    loanApplicationStatusService = TestBed.inject(LoanApplicationStatusService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const loanApplicationStatus: ILoanApplicationStatus = { id: 456 };

      activatedRoute.data = of({ loanApplicationStatus });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(loanApplicationStatus));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<LoanApplicationStatus>>();
      const loanApplicationStatus = { id: 123 };
      jest.spyOn(loanApplicationStatusService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ loanApplicationStatus });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: loanApplicationStatus }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(loanApplicationStatusService.update).toHaveBeenCalledWith(loanApplicationStatus);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<LoanApplicationStatus>>();
      const loanApplicationStatus = new LoanApplicationStatus();
      jest.spyOn(loanApplicationStatusService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ loanApplicationStatus });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: loanApplicationStatus }));
      saveSubject.complete();

      // THEN
      expect(loanApplicationStatusService.create).toHaveBeenCalledWith(loanApplicationStatus);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<LoanApplicationStatus>>();
      const loanApplicationStatus = { id: 123 };
      jest.spyOn(loanApplicationStatusService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ loanApplicationStatus });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(loanApplicationStatusService.update).toHaveBeenCalledWith(loanApplicationStatus);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
