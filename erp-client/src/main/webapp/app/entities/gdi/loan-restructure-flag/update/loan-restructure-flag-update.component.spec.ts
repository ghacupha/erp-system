jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { LoanRestructureFlagService } from '../service/loan-restructure-flag.service';
import { ILoanRestructureFlag, LoanRestructureFlag } from '../loan-restructure-flag.model';

import { LoanRestructureFlagUpdateComponent } from './loan-restructure-flag-update.component';

describe('LoanRestructureFlag Management Update Component', () => {
  let comp: LoanRestructureFlagUpdateComponent;
  let fixture: ComponentFixture<LoanRestructureFlagUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let loanRestructureFlagService: LoanRestructureFlagService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [LoanRestructureFlagUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(LoanRestructureFlagUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(LoanRestructureFlagUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    loanRestructureFlagService = TestBed.inject(LoanRestructureFlagService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const loanRestructureFlag: ILoanRestructureFlag = { id: 456 };

      activatedRoute.data = of({ loanRestructureFlag });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(loanRestructureFlag));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<LoanRestructureFlag>>();
      const loanRestructureFlag = { id: 123 };
      jest.spyOn(loanRestructureFlagService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ loanRestructureFlag });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: loanRestructureFlag }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(loanRestructureFlagService.update).toHaveBeenCalledWith(loanRestructureFlag);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<LoanRestructureFlag>>();
      const loanRestructureFlag = new LoanRestructureFlag();
      jest.spyOn(loanRestructureFlagService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ loanRestructureFlag });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: loanRestructureFlag }));
      saveSubject.complete();

      // THEN
      expect(loanRestructureFlagService.create).toHaveBeenCalledWith(loanRestructureFlag);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<LoanRestructureFlag>>();
      const loanRestructureFlag = { id: 123 };
      jest.spyOn(loanRestructureFlagService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ loanRestructureFlag });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(loanRestructureFlagService.update).toHaveBeenCalledWith(loanRestructureFlag);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
