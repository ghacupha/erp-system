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
