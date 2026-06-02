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
