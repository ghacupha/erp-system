import {PaymentLabelService} from '../../../payment-label/service/payment-label.service';

jest.mock('@angular/router');

import {ComponentFixture, TestBed} from '@angular/core/testing';
import {HttpResponse} from '@angular/common/http';
import {HttpClientTestingModule} from '@angular/common/http/testing';
import {FormBuilder} from '@angular/forms';
import {ActivatedRoute} from '@angular/router';
import {of, Subject} from 'rxjs';

import {PaymentService} from '../service/payment.service';
import {IPayment, Payment} from '../payment.model';
import {DealerService} from 'app/entities/dealers/dealer/service/dealer.service';
import {IPlaceholder} from 'app/entities/erpService/placeholder/placeholder.model';
import {PlaceholderService} from 'app/entities/erpService/placeholder/service/placeholder.service';

import {PaymentUpdateComponent} from './payment-update.component';
import {PaymentCategoryService} from '../../payment-category/service/payment-category.service';
import {TaxRuleService} from '../../tax-rule/service/tax-rule.service';
import {PaymentCalculationService} from '../../payment-calculation/service/payment-calculation.service';
import {IPaymentLabel} from '../../../payment-label/payment-label.model';
import {IPaymentCategory} from '../../payment-category/payment-category.model';
import {ITaxRule} from '../../tax-rule/tax-rule.model';
import {IPaymentCalculation} from '../../payment-calculation/payment-calculation.model';
import {initialState} from '../../../../store/global-store.definition';
import {MockStore, provideMockStore} from '@ngrx/store/testing';

describe('Component Tests', () => {
  describe('Payment Management Update Component', () => {
    let comp: PaymentUpdateComponent;
    let fixture: ComponentFixture<PaymentUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let paymentService: PaymentService;
    let paymentLabelService: PaymentLabelService;
    let dealerService: DealerService;
    let paymentCategoryService: PaymentCategoryService;
    let taxRuleService: TaxRuleService;
    let paymentCalculationService: PaymentCalculationService;
    let placeholderService: PlaceholderService;
    let store: MockStore;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [PaymentUpdateComponent],
        providers: [
          FormBuilder,
          ActivatedRoute,
          provideMockStore({initialState})
        ],
      })
        .overrideTemplate(PaymentUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PaymentUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      paymentService = TestBed.inject(PaymentService);
      paymentLabelService = TestBed.inject(PaymentLabelService);
      dealerService = TestBed.inject(DealerService);
      paymentCategoryService = TestBed.inject(PaymentCategoryService);
      taxRuleService = TestBed.inject(TaxRuleService);
      paymentCalculationService = TestBed.inject(PaymentCalculationService);
      placeholderService = TestBed.inject(PlaceholderService);
      store = TestBed.inject(MockStore);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call PaymentLabel query and add missing value', () => {
        const payment: IPayment = {id: 456};
        const paymentLabels: IPaymentLabel[] = [{id: 58999}];
        payment.paymentLabels = paymentLabels;

        const paymentLabelCollection: IPaymentLabel[] = [{id: 50464}];
        jest.spyOn(paymentLabelService, 'query').mockReturnValue(of(new HttpResponse({body: paymentLabelCollection})));
        const additionalPaymentLabels = [...paymentLabels];
        const expectedCollection: IPaymentLabel[] = [...additionalPaymentLabels, ...paymentLabelCollection];
        jest.spyOn(paymentLabelService, 'addPaymentLabelToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({payment});
        comp.ngOnInit();

        expect(paymentLabelService.query).toHaveBeenCalled();
        expect(paymentLabelService.addPaymentLabelToCollectionIfMissing).toHaveBeenCalledWith(
          paymentLabelCollection,
          ...additionalPaymentLabels
        );
        expect(comp.paymentLabelsSharedCollection).toEqual(expectedCollection);
      });

      // it('Should call Dealer query and add missing value', () => {
      //   const payment: IPayment = {id: 456};
      //   const dealer: IDealer = {id: 90172};
      //   payment.dealer = dealer;
      //
      //   const dealerCollection: IDealer[] = [{id: 41765}];
      //   jest.spyOn(dealerService, 'query').mockReturnValue(of(new HttpResponse({body: dealerCollection})));
      //   const additionalDealers = [dealer];
      //   const expectedCollection: IDealer[] = [...additionalDealers, ...dealerCollection];
      //   jest.spyOn(dealerService, 'addDealerToCollectionIfMissing').mockReturnValue(expectedCollection);
      //
      //   activatedRoute.data = of({payment});
      //   comp.ngOnInit();
      //
      //   expect(dealerService.query).toHaveBeenCalled();
      //   expect(dealerService.addDealerToCollectionIfMissing).toHaveBeenCalledWith(dealerCollection, ...additionalDealers);
      //   expect(comp.dealersSharedCollection).toEqual(expectedCollection);
      // });

      it('Should call PaymentCategory query and add missing value', () => {
        const payment: IPayment = {id: 456};
        const paymentCategory: IPaymentCategory = {id: 30270};
        payment.paymentCategory = paymentCategory;

        const paymentCategoryCollection: IPaymentCategory[] = [{id: 97857}];
        jest.spyOn(paymentCategoryService, 'query').mockReturnValue(of(new HttpResponse({body: paymentCategoryCollection})));
        const additionalPaymentCategories = [paymentCategory];
        const expectedCollection: IPaymentCategory[] = [...additionalPaymentCategories, ...paymentCategoryCollection];
        jest.spyOn(paymentCategoryService, 'addPaymentCategoryToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({payment});
        comp.ngOnInit();

        expect(paymentCategoryService.query).toHaveBeenCalled();
        // expect(paymentCategoryService.addPaymentCategoryToCollectionIfMissing).toHaveBeenCalledWith(
        //   paymentCategoryCollection,
        //   ...additionalPaymentCategories
        // );
        expect(comp.paymentCategoriesSharedCollection).toEqual(expectedCollection);
      });

      it('Should call TaxRule query and add missing value', () => {
        const payment: IPayment = {id: 456};
        const taxRule: ITaxRule = {id: 92001};
        payment.taxRule = taxRule;

        const taxRuleCollection: ITaxRule[] = [{id: 51866}];
        jest.spyOn(taxRuleService, 'query').mockReturnValue(of(new HttpResponse({body: taxRuleCollection})));
        const additionalTaxRules = [taxRule];
        const expectedCollection: ITaxRule[] = [...additionalTaxRules, ...taxRuleCollection];
        jest.spyOn(taxRuleService, 'addTaxRuleToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({payment});
        comp.ngOnInit();

        expect(taxRuleService.query).toHaveBeenCalled();
        expect(taxRuleService.addTaxRuleToCollectionIfMissing).toHaveBeenCalledWith(taxRuleCollection, ...additionalTaxRules);
        expect(comp.taxRulesSharedCollection).toEqual(expectedCollection);
      });

      it('Should call paymentCalculation query and add missing value', () => {
        const payment: IPayment = {id: 456};
        const paymentCalculation: IPaymentCalculation = {id: 51007};
        payment.paymentCalculation = paymentCalculation;

        const paymentCalculationCollection: IPaymentCalculation[] = [{id: 47283}];
        jest.spyOn(paymentCalculationService, 'query').mockReturnValue(of(new HttpResponse({body: paymentCalculationCollection})));
        const expectedCollection: IPaymentCalculation[] = [paymentCalculation, ...paymentCalculationCollection];
        jest.spyOn(paymentCalculationService, 'addPaymentCalculationToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({payment});
        comp.ngOnInit();

        expect(paymentCalculationService.query).toHaveBeenCalled();
        expect(paymentCalculationService.addPaymentCalculationToCollectionIfMissing).toHaveBeenCalledWith(
          paymentCalculationCollection,
          paymentCalculation
        );
        expect(comp.paymentCalculationsCollection).toEqual(expectedCollection);
      });

      it('Should call Placeholder query and add missing value', () => {
        const payment: IPayment = {id: 456};
        const placeholders: IPlaceholder[] = [{id: 68435}];
        payment.placeholders = placeholders;

        const placeholderCollection: IPlaceholder[] = [{id: 61478}];
        jest.spyOn(placeholderService, 'query').mockReturnValue(of(new HttpResponse({body: placeholderCollection})));
        const additionalPlaceholders = [...placeholders];
        const expectedCollection: IPlaceholder[] = [...additionalPlaceholders, ...placeholderCollection];
        jest.spyOn(placeholderService, 'addPlaceholderToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({payment});
        comp.ngOnInit();

        expect(placeholderService.query).toHaveBeenCalled();
        expect(placeholderService.addPlaceholderToCollectionIfMissing).toHaveBeenCalledWith(
          placeholderCollection,
          ...additionalPlaceholders
        );
        expect(comp.placeholdersSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Payment query and add missing value', () => {
        const payment: IPayment = {id: 456};
        const paymentGroup: IPayment = {id: 69844};
        payment.paymentGroup = paymentGroup;

        const paymentCollection: IPayment[] = [{id: 41984}];
        jest.spyOn(paymentService, 'query').mockReturnValue(of(new HttpResponse({body: paymentCollection})));
        const additionalPayments = [paymentGroup];
        const expectedCollection: IPayment[] = [...additionalPayments, ...paymentCollection];
        jest.spyOn(paymentService, 'addPaymentToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({payment});
        comp.ngOnInit();

        expect(paymentService.query).toHaveBeenCalled();
        expect(paymentService.addPaymentToCollectionIfMissing).toHaveBeenCalledWith(paymentCollection, ...additionalPayments);
        expect(comp.paymentsSharedCollection).toEqual(expectedCollection);
      });

      // it('Should update editForm', () => {
      //   const payment: IPayment = {id: 456};
      //   const paymentLabels: IPaymentLabel = {id: 75140};
      //   payment.paymentLabels = [paymentLabels];
      //   const dealer: IDealer = {id: 78923};
      //   payment.dealer = dealer;
      //   const paymentCategory: IPaymentCategory = {id: 45196};
      //   payment.paymentCategory = paymentCategory;
      //   const taxRule: ITaxRule = {id: 2708};
      //   payment.taxRule = taxRule;
      //   const paymentCalculation: IPaymentCalculation = {id: 68581};
      //   payment.paymentCalculation = paymentCalculation;
      //   const placeholders: IPlaceholder = {id: 22052};
      //   payment.placeholders = [placeholders];
      //   const paymentGroup: IPayment = {id: 39977};
      //   payment.paymentGroup = paymentGroup;
      //
      //   activatedRoute.data = of({payment});
      //   comp.ngOnInit();
      //
      //   expect(comp.editForm.value).toEqual(expect.objectContaining(payment));
      //   expect(comp.paymentLabelsSharedCollection).toContain(paymentLabels);
      //   expect(comp.dealersSharedCollection).toContain(dealer);
      //   expect(comp.paymentCategoriesSharedCollection).toContain(paymentCategory);
      //   expect(comp.taxRulesSharedCollection).toContain(taxRule);
      //   expect(comp.paymentCalculationsCollection).toContain(paymentCalculation);
      //   expect(comp.placeholdersSharedCollection).toContain(placeholders);
      //   expect(comp.paymentsSharedCollection).toContain(paymentGroup);
      // });
    });

    describe('persistence', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Payment>>();
        const payment = {id: 123};
        jest.spyOn(paymentService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({payment});
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({body: payment}));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        // expect(paymentService.update).toHaveBeenCalledWith(payment);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Payment>>();
        const payment = new Payment();
        jest.spyOn(paymentService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({payment});
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({body: payment}));
        saveSubject.complete();

        // THEN
        // expect(paymentService.create).toHaveBeenCalledWith(payment);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Payment>>();
        const payment = {id: 123};
        jest.spyOn(paymentService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({payment});
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        // expect(paymentService.update).toHaveBeenCalledWith(payment);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackPaymentLabelById', () => {
        it('Should return tracked PaymentLabel primary key', () => {
          const entity = {id: 123};
          const trackResult = comp.trackPaymentLabelById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackDealerById', () => {
        it('Should return tracked Dealer primary key', () => {
          const entity = {id: 123};
          const trackResult = comp.trackDealerById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackPaymentCategoryById', () => {
        it('Should return tracked PaymentCategory primary key', () => {
          const entity = {id: 123};
          const trackResult = comp.trackPaymentCategoryById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackTaxRuleById', () => {
        it('Should return tracked TaxRule primary key', () => {
          const entity = {id: 123};
          const trackResult = comp.trackTaxRuleById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackPaymentCalculationById', () => {
        it('Should return tracked PaymentCalculation primary key', () => {
          const entity = {id: 123};
          const trackResult = comp.trackPaymentCalculationById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackPlaceholderById', () => {
        it('Should return tracked Placeholder primary key', () => {
          const entity = {id: 123};
          const trackResult = comp.trackPlaceholderById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackPaymentById', () => {
        it('Should return tracked Payment primary key', () => {
          const entity = {id: 123};
          const trackResult = comp.trackPaymentById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });

    describe('Getting selected relationships', () => {
      describe('getSelectedPaymentLabel', () => {
        it('Should return option if no PaymentLabel is selected', () => {
          const option = {id: 123};
          const result = comp.getSelectedPaymentLabel(option);
          expect(result === option).toEqual(true);
        });

        it('Should return selected PaymentLabel for according option', () => {
          const option = {id: 123};
          const selected = {id: 123};
          const selected2 = {id: 456};
          const result = comp.getSelectedPaymentLabel(option, [selected2, selected]);
          expect(result === selected).toEqual(true);
          expect(result === selected2).toEqual(false);
          expect(result === option).toEqual(false);
        });

        it('Should return option if this PaymentLabel is not selected', () => {
          const option = {id: 123};
          const selected = {id: 456};
          const result = comp.getSelectedPaymentLabel(option, [selected]);
          expect(result === option).toEqual(true);
          expect(result === selected).toEqual(false);
        });
      });

      describe('getSelectedPlaceholder', () => {
        it('Should return option if no Placeholder is selected', () => {
          const option = {id: 123};
          const result = comp.getSelectedPlaceholder(option);
          expect(result === option).toEqual(true);
        });

        it('Should return selected Placeholder for according option', () => {
          const option = {id: 123};
          const selected = {id: 123};
          const selected2 = {id: 456};
          const result = comp.getSelectedPlaceholder(option, [selected2, selected]);
          expect(result === selected).toEqual(true);
          expect(result === selected2).toEqual(false);
          expect(result === option).toEqual(false);
        });

        it('Should return option if this Placeholder is not selected', () => {
          const option = {id: 123};
          const selected = {id: 456};
          const result = comp.getSelectedPlaceholder(option, [selected]);
          expect(result === option).toEqual(true);
          expect(result === selected).toEqual(false);
        });
      });
    });
  });
});
