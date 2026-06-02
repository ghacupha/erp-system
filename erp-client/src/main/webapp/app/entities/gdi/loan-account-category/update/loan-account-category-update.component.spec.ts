jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { LoanAccountCategoryService } from '../service/loan-account-category.service';
import { ILoanAccountCategory, LoanAccountCategory } from '../loan-account-category.model';

import { LoanAccountCategoryUpdateComponent } from './loan-account-category-update.component';

describe('LoanAccountCategory Management Update Component', () => {
  let comp: LoanAccountCategoryUpdateComponent;
  let fixture: ComponentFixture<LoanAccountCategoryUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let loanAccountCategoryService: LoanAccountCategoryService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [LoanAccountCategoryUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(LoanAccountCategoryUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(LoanAccountCategoryUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    loanAccountCategoryService = TestBed.inject(LoanAccountCategoryService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const loanAccountCategory: ILoanAccountCategory = { id: 456 };

      activatedRoute.data = of({ loanAccountCategory });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(loanAccountCategory));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<LoanAccountCategory>>();
      const loanAccountCategory = { id: 123 };
      jest.spyOn(loanAccountCategoryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ loanAccountCategory });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: loanAccountCategory }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(loanAccountCategoryService.update).toHaveBeenCalledWith(loanAccountCategory);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<LoanAccountCategory>>();
      const loanAccountCategory = new LoanAccountCategory();
      jest.spyOn(loanAccountCategoryService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ loanAccountCategory });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: loanAccountCategory }));
      saveSubject.complete();

      // THEN
      expect(loanAccountCategoryService.create).toHaveBeenCalledWith(loanAccountCategory);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<LoanAccountCategory>>();
      const loanAccountCategory = { id: 123 };
      jest.spyOn(loanAccountCategoryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ loanAccountCategory });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(loanAccountCategoryService.update).toHaveBeenCalledWith(loanAccountCategory);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
