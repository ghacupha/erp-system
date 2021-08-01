jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { PaymentCategoryService } from '../service/payment-category.service';
import { IPaymentCategory, PaymentCategory } from '../payment-category.model';

import { PaymentCategoryUpdateComponent } from './payment-category-update.component';

describe('Component Tests', () => {
  describe('PaymentCategory Management Update Component', () => {
    let comp: PaymentCategoryUpdateComponent;
    let fixture: ComponentFixture<PaymentCategoryUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let paymentCategoryService: PaymentCategoryService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [PaymentCategoryUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(PaymentCategoryUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PaymentCategoryUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      paymentCategoryService = TestBed.inject(PaymentCategoryService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const paymentCategory: IPaymentCategory = { id: 456 };

        activatedRoute.data = of({ paymentCategory });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(paymentCategory));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<PaymentCategory>>();
        const paymentCategory = { id: 123 };
        jest.spyOn(paymentCategoryService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ paymentCategory });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: paymentCategory }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(paymentCategoryService.update).toHaveBeenCalledWith(paymentCategory);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<PaymentCategory>>();
        const paymentCategory = new PaymentCategory();
        jest.spyOn(paymentCategoryService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ paymentCategory });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: paymentCategory }));
        saveSubject.complete();

        // THEN
        expect(paymentCategoryService.create).toHaveBeenCalledWith(paymentCategory);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<PaymentCategory>>();
        const paymentCategory = { id: 123 };
        jest.spyOn(paymentCategoryService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ paymentCategory });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(paymentCategoryService.update).toHaveBeenCalledWith(paymentCategory);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
