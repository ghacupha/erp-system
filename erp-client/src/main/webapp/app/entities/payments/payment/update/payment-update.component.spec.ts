jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { PaymentService } from '../service/payment.service';
import { IPayment, Payment } from '../payment.model';
import { IPaymentRequisition } from 'app/entities/payments/payment-requisition/payment-requisition.model';
import { PaymentRequisitionService } from 'app/entities/payments/payment-requisition/service/payment-requisition.service';
import { ITaxRule } from 'app/entities/payments/tax-rule/tax-rule.model';
import { TaxRuleService } from 'app/entities/payments/tax-rule/service/tax-rule.service';
import { IPaymentCategory } from 'app/entities/payments/payment-category/payment-category.model';
import { PaymentCategoryService } from 'app/entities/payments/payment-category/service/payment-category.service';
import { IPaymentCalculation } from 'app/entities/payments/payment-calculation/payment-calculation.model';
import { PaymentCalculationService } from 'app/entities/payments/payment-calculation/service/payment-calculation.service';

import { PaymentUpdateComponent } from './payment-update.component';

describe('Component Tests', () => {
  describe('Payment Management Update Component', () => {
    let comp: PaymentUpdateComponent;
    let fixture: ComponentFixture<PaymentUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let paymentService: PaymentService;
    let paymentRequisitionService: PaymentRequisitionService;
    let taxRuleService: TaxRuleService;
    let paymentCategoryService: PaymentCategoryService;
    let paymentCalculationService: PaymentCalculationService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [PaymentUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(PaymentUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PaymentUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      paymentService = TestBed.inject(PaymentService);
      paymentRequisitionService = TestBed.inject(PaymentRequisitionService);
      taxRuleService = TestBed.inject(TaxRuleService);
      paymentCategoryService = TestBed.inject(PaymentCategoryService);
      paymentCalculationService = TestBed.inject(PaymentCalculationService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call PaymentRequisition query and add missing value', () => {
        const payment: IPayment = { id: 456 };
        const paymentRequisition: IPaymentRequisition = { id: 36952 };
        payment.paymentRequisition = paymentRequisition;

        const paymentRequisitionCollection: IPaymentRequisition[] = [{ id: 71824 }];
        jest.spyOn(paymentRequisitionService, 'query').mockReturnValue(of(new HttpResponse({ body: paymentRequisitionCollection })));
        const additionalPaymentRequisitions = [paymentRequisition];
        const expectedCollection: IPaymentRequisition[] = [...additionalPaymentRequisitions, ...paymentRequisitionCollection];
        jest.spyOn(paymentRequisitionService, 'addPaymentRequisitionToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ payment });
        comp.ngOnInit();

        expect(paymentRequisitionService.query).toHaveBeenCalled();
        expect(paymentRequisitionService.addPaymentRequisitionToCollectionIfMissing).toHaveBeenCalledWith(
          paymentRequisitionCollection,
          ...additionalPaymentRequisitions
        );
        expect(comp.paymentRequisitionsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call taxRule query and add missing value', () => {
        const payment: IPayment = { id: 456 };
        const taxRule: ITaxRule = { id: 92001 };
        payment.taxRule = taxRule;

        const taxRuleCollection: ITaxRule[] = [{ id: 51866 }];
        jest.spyOn(taxRuleService, 'query').mockReturnValue(of(new HttpResponse({ body: taxRuleCollection })));
        const expectedCollection: ITaxRule[] = [taxRule, ...taxRuleCollection];
        jest.spyOn(taxRuleService, 'addTaxRuleToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ payment });
        comp.ngOnInit();

        expect(taxRuleService.query).toHaveBeenCalled();
        expect(taxRuleService.addTaxRuleToCollectionIfMissing).toHaveBeenCalledWith(taxRuleCollection, taxRule);
        expect(comp.taxRulesCollection).toEqual(expectedCollection);
      });

      it('Should call paymentCategory query and add missing value', () => {
        const payment: IPayment = { id: 456 };
        const paymentCategory: IPaymentCategory = { id: 30270 };
        payment.paymentCategory = paymentCategory;

        const paymentCategoryCollection: IPaymentCategory[] = [{ id: 97857 }];
        jest.spyOn(paymentCategoryService, 'query').mockReturnValue(of(new HttpResponse({ body: paymentCategoryCollection })));
        const expectedCollection: IPaymentCategory[] = [paymentCategory, ...paymentCategoryCollection];
        jest.spyOn(paymentCategoryService, 'addPaymentCategoryToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ payment });
        comp.ngOnInit();

        expect(paymentCategoryService.query).toHaveBeenCalled();
        expect(paymentCategoryService.addPaymentCategoryToCollectionIfMissing).toHaveBeenCalledWith(
          paymentCategoryCollection,
          paymentCategory
        );
        expect(comp.paymentCategoriesCollection).toEqual(expectedCollection);
      });

      it('Should call paymentCalculation query and add missing value', () => {
        const payment: IPayment = { id: 456 };
        const paymentCalculation: IPaymentCalculation = { id: 51007 };
        payment.paymentCalculation = paymentCalculation;

        const paymentCalculationCollection: IPaymentCalculation[] = [{ id: 47283 }];
        jest.spyOn(paymentCalculationService, 'query').mockReturnValue(of(new HttpResponse({ body: paymentCalculationCollection })));
        const expectedCollection: IPaymentCalculation[] = [paymentCalculation, ...paymentCalculationCollection];
        jest.spyOn(paymentCalculationService, 'addPaymentCalculationToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ payment });
        comp.ngOnInit();

        expect(paymentCalculationService.query).toHaveBeenCalled();
        expect(paymentCalculationService.addPaymentCalculationToCollectionIfMissing).toHaveBeenCalledWith(
          paymentCalculationCollection,
          paymentCalculation
        );
        expect(comp.paymentCalculationsCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const payment: IPayment = { id: 456 };
        const paymentRequisition: IPaymentRequisition = { id: 74321 };
        payment.paymentRequisition = paymentRequisition;
        const taxRule: ITaxRule = { id: 2708 };
        payment.taxRule = taxRule;
        const paymentCategory: IPaymentCategory = { id: 45196 };
        payment.paymentCategory = paymentCategory;
        const paymentCalculation: IPaymentCalculation = { id: 68581 };
        payment.paymentCalculation = paymentCalculation;

        activatedRoute.data = of({ payment });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(payment));
        expect(comp.paymentRequisitionsSharedCollection).toContain(paymentRequisition);
        expect(comp.taxRulesCollection).toContain(taxRule);
        expect(comp.paymentCategoriesCollection).toContain(paymentCategory);
        expect(comp.paymentCalculationsCollection).toContain(paymentCalculation);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Payment>>();
        const payment = { id: 123 };
        jest.spyOn(paymentService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ payment });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: payment }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(paymentService.update).toHaveBeenCalledWith(payment);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Payment>>();
        const payment = new Payment();
        jest.spyOn(paymentService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ payment });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: payment }));
        saveSubject.complete();

        // THEN
        expect(paymentService.create).toHaveBeenCalledWith(payment);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Payment>>();
        const payment = { id: 123 };
        jest.spyOn(paymentService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ payment });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(paymentService.update).toHaveBeenCalledWith(payment);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackPaymentRequisitionById', () => {
        it('Should return tracked PaymentRequisition primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackPaymentRequisitionById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackTaxRuleById', () => {
        it('Should return tracked TaxRule primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackTaxRuleById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackPaymentCategoryById', () => {
        it('Should return tracked PaymentCategory primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackPaymentCategoryById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackPaymentCalculationById', () => {
        it('Should return tracked PaymentCalculation primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackPaymentCalculationById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
