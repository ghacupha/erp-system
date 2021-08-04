jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { PaymentCalculationService } from '../service/payment-calculation.service';
import { IPaymentCalculation, PaymentCalculation } from '../payment-calculation.model';
import { IPaymentCategory } from 'app/entities/payments/payment-category/payment-category.model';
import { PaymentCategoryService } from 'app/entities/payments/payment-category/service/payment-category.service';

import { PaymentCalculationUpdateComponent } from './payment-calculation-update.component';

describe('Component Tests', () => {
  describe('PaymentCalculation Management Update Component', () => {
    let comp: PaymentCalculationUpdateComponent;
    let fixture: ComponentFixture<PaymentCalculationUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let paymentCalculationService: PaymentCalculationService;
    let paymentCategoryService: PaymentCategoryService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [PaymentCalculationUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(PaymentCalculationUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PaymentCalculationUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      paymentCalculationService = TestBed.inject(PaymentCalculationService);
      paymentCategoryService = TestBed.inject(PaymentCategoryService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call PaymentCategory query and add missing value', () => {
        const paymentCalculation: IPaymentCalculation = { id: 456 };
        const paymentCategory: IPaymentCategory = { id: 26408 };
        paymentCalculation.paymentCategory = paymentCategory;

        const paymentCategoryCollection: IPaymentCategory[] = [{ id: 15897 }];
        jest.spyOn(paymentCategoryService, 'query').mockReturnValue(of(new HttpResponse({ body: paymentCategoryCollection })));
        const additionalPaymentCategories = [paymentCategory];
        const expectedCollection: IPaymentCategory[] = [...additionalPaymentCategories, ...paymentCategoryCollection];
        jest.spyOn(paymentCategoryService, 'addPaymentCategoryToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ paymentCalculation });
        comp.ngOnInit();

        expect(paymentCategoryService.query).toHaveBeenCalled();
        expect(paymentCategoryService.addPaymentCategoryToCollectionIfMissing).toHaveBeenCalledWith(
          paymentCategoryCollection,
          ...additionalPaymentCategories
        );
        expect(comp.paymentCategoriesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const paymentCalculation: IPaymentCalculation = { id: 456 };
        const paymentCategory: IPaymentCategory = { id: 94444 };
        paymentCalculation.paymentCategory = paymentCategory;

        activatedRoute.data = of({ paymentCalculation });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(paymentCalculation));
        expect(comp.paymentCategoriesSharedCollection).toContain(paymentCategory);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<PaymentCalculation>>();
        const paymentCalculation = { id: 123 };
        jest.spyOn(paymentCalculationService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ paymentCalculation });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: paymentCalculation }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(paymentCalculationService.update).toHaveBeenCalledWith(paymentCalculation);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<PaymentCalculation>>();
        const paymentCalculation = new PaymentCalculation();
        jest.spyOn(paymentCalculationService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ paymentCalculation });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: paymentCalculation }));
        saveSubject.complete();

        // THEN
        expect(paymentCalculationService.create).toHaveBeenCalledWith(paymentCalculation);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<PaymentCalculation>>();
        const paymentCalculation = { id: 123 };
        jest.spyOn(paymentCalculationService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ paymentCalculation });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(paymentCalculationService.update).toHaveBeenCalledWith(paymentCalculation);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackPaymentCategoryById', () => {
        it('Should return tracked PaymentCategory primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackPaymentCategoryById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
