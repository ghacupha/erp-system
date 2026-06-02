jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { LoanProductTypeService } from '../service/loan-product-type.service';
import { ILoanProductType, LoanProductType } from '../loan-product-type.model';

import { LoanProductTypeUpdateComponent } from './loan-product-type-update.component';

describe('LoanProductType Management Update Component', () => {
  let comp: LoanProductTypeUpdateComponent;
  let fixture: ComponentFixture<LoanProductTypeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let loanProductTypeService: LoanProductTypeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [LoanProductTypeUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(LoanProductTypeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(LoanProductTypeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    loanProductTypeService = TestBed.inject(LoanProductTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const loanProductType: ILoanProductType = { id: 456 };

      activatedRoute.data = of({ loanProductType });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(loanProductType));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<LoanProductType>>();
      const loanProductType = { id: 123 };
      jest.spyOn(loanProductTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ loanProductType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: loanProductType }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(loanProductTypeService.update).toHaveBeenCalledWith(loanProductType);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<LoanProductType>>();
      const loanProductType = new LoanProductType();
      jest.spyOn(loanProductTypeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ loanProductType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: loanProductType }));
      saveSubject.complete();

      // THEN
      expect(loanProductTypeService.create).toHaveBeenCalledWith(loanProductType);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<LoanProductType>>();
      const loanProductType = { id: 123 };
      jest.spyOn(loanProductTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ loanProductType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(loanProductTypeService.update).toHaveBeenCalledWith(loanProductType);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
