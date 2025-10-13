///
/// Erp System - Mark X No 10 (Jehoiada Series) Client 1.7.8
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

import { ErpCommonModule } from '../../../erp-common/erp-common.module';

jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { SettlementService } from '../service/settlement.service';
import { ISettlement, Settlement } from '../settlement.model';
import { IPlaceholder } from '../../../erp-pages/placeholder/placeholder.model';
import { PlaceholderService } from '../../../erp-pages/placeholder/service/placeholder.service';
import { ISettlementCurrency } from 'app/erp/erp-settlements/settlement-currency/settlement-currency.model';
import { SettlementCurrencyService } from 'app/erp/erp-settlements/settlement-currency/service/settlement-currency.service';
import { IPaymentLabel } from '../../../erp-pages/payment-label/payment-label.model';
import { PaymentLabelService } from '../../../erp-pages/payment-label/service/payment-label.service';
import { IPaymentCategory } from 'app/erp/erp-settlements/payments/payment-category/payment-category.model';
import { PaymentCategoryService } from 'app/erp/erp-settlements/payments/payment-category/service/payment-category.service';
import { IPaymentInvoice } from 'app/erp/erp-settlements/payment-invoice/payment-invoice.model';
import { PaymentInvoiceService } from 'app/erp/erp-settlements/payment-invoice/service/payment-invoice.service';
import { DealerService } from '../../../erp-pages/dealers/dealer/service/dealer.service';

import { SettlementUpdateComponent } from './settlement-update.component';
import { IDealer } from '../../../erp-pages/dealers/dealer/dealer.model';

describe('Settlement Management Update Component', () => {
  let comp: SettlementUpdateComponent;
  let fixture: ComponentFixture<SettlementUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let settlementService: SettlementService;
  let placeholderService: PlaceholderService;
  let settlementCurrencyService: SettlementCurrencyService;
  let paymentLabelService: PaymentLabelService;
  let paymentCategoryService: PaymentCategoryService;
  let dealerService: DealerService;
  let paymentInvoiceService: PaymentInvoiceService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ErpCommonModule, HttpClientTestingModule],
      declarations: [SettlementUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(SettlementUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SettlementUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    settlementService = TestBed.inject(SettlementService);
    placeholderService = TestBed.inject(PlaceholderService);
    settlementCurrencyService = TestBed.inject(SettlementCurrencyService);
    paymentLabelService = TestBed.inject(PaymentLabelService);
    paymentCategoryService = TestBed.inject(PaymentCategoryService);
    dealerService = TestBed.inject(DealerService);
    paymentInvoiceService = TestBed.inject(PaymentInvoiceService);

    comp = fixture.componentInstance;

    TestBed.compileComponents();
  });

  describe('ngOnInit', () => {
    it('Should call Placeholder query and add missing value', () => {
      const settlement: ISettlement = { id: 456 };
      const placeholders: IPlaceholder[] = [{ id: 9050 }];
      settlement.placeholders = placeholders;

      const placeholderCollection: IPlaceholder[] = [{ id: 4974 }];
      jest.spyOn(placeholderService, 'query').mockReturnValue(of(new HttpResponse({ body: placeholderCollection })));
      const additionalPlaceholders = [...placeholders];
      const expectedCollection: IPlaceholder[] = [...additionalPlaceholders, ...placeholderCollection];
      jest.spyOn(placeholderService, 'addPlaceholderToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ settlement });
      comp.ngOnInit();

      expect(placeholderService.query).toHaveBeenCalled();
      expect(placeholderService.addPlaceholderToCollectionIfMissing).toHaveBeenCalledWith(placeholderCollection, ...additionalPlaceholders);
      expect(comp.placeholdersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call SettlementCurrency query and add missing value', () => {
      const settlement: ISettlement = { id: 456 };
      const settlementCurrency: ISettlementCurrency = { id: 80549 };
      settlement.settlementCurrency = settlementCurrency;

      const settlementCurrencyCollection: ISettlementCurrency[] = [{ id: 40289 }];
      jest.spyOn(settlementCurrencyService, 'query').mockReturnValue(of(new HttpResponse({ body: settlementCurrencyCollection })));
      const additionalSettlementCurrencies = [settlementCurrency];
      const expectedCollection: ISettlementCurrency[] = [...additionalSettlementCurrencies, ...settlementCurrencyCollection];
      jest.spyOn(settlementCurrencyService, 'addSettlementCurrencyToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ settlement });
      comp.ngOnInit();

      expect(settlementCurrencyService.query).toHaveBeenCalled();
      expect(settlementCurrencyService.addSettlementCurrencyToCollectionIfMissing).toHaveBeenCalledWith(
        settlementCurrencyCollection,
        ...additionalSettlementCurrencies
      );
      expect(comp.settlementCurrenciesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call PaymentLabel query and add missing value', () => {
      const settlement: ISettlement = { id: 456 };
      const paymentLabels: IPaymentLabel[] = [{ id: 39337 }];
      settlement.paymentLabels = paymentLabels;

      const paymentLabelCollection: IPaymentLabel[] = [{ id: 8138 }];
      jest.spyOn(paymentLabelService, 'query').mockReturnValue(of(new HttpResponse({ body: paymentLabelCollection })));
      const additionalPaymentLabels = [...paymentLabels];
      const expectedCollection: IPaymentLabel[] = [...additionalPaymentLabels, ...paymentLabelCollection];
      jest.spyOn(paymentLabelService, 'addPaymentLabelToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ settlement });
      comp.ngOnInit();

      expect(paymentLabelService.query).toHaveBeenCalled();
      expect(paymentLabelService.addPaymentLabelToCollectionIfMissing).toHaveBeenCalledWith(
        paymentLabelCollection,
        ...additionalPaymentLabels
      );
      expect(comp.paymentLabelsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call PaymentCategory query and add missing value', () => {
      const settlement: ISettlement = { id: 456 };
      const paymentCategory: IPaymentCategory = { id: 96211 };
      settlement.paymentCategory = paymentCategory;

      const paymentCategoryCollection: IPaymentCategory[] = [{ id: 28357 }];
      jest.spyOn(paymentCategoryService, 'query').mockReturnValue(of(new HttpResponse({ body: paymentCategoryCollection })));
      const additionalPaymentCategories = [paymentCategory];
      const expectedCollection: IPaymentCategory[] = [...additionalPaymentCategories, ...paymentCategoryCollection];
      jest.spyOn(paymentCategoryService, 'addPaymentCategoryToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ settlement });
      comp.ngOnInit();

      expect(paymentCategoryService.query).toHaveBeenCalled();
      expect(paymentCategoryService.addPaymentCategoryToCollectionIfMissing).toHaveBeenCalledWith(
        paymentCategoryCollection,
        ...additionalPaymentCategories
      );
      expect(comp.paymentCategoriesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Settlement query and add missing value', () => {
      const settlement: ISettlement = { id: 456 };
      const groupSettlement: ISettlement = { id: 39563 };
      settlement.groupSettlement = groupSettlement;

      const settlementCollection: ISettlement[] = [{ id: 24641 }];
      jest.spyOn(settlementService, 'query').mockReturnValue(of(new HttpResponse({ body: settlementCollection })));
      const additionalSettlements = [groupSettlement];
      const expectedCollection: ISettlement[] = [...additionalSettlements, ...settlementCollection];
      jest.spyOn(settlementService, 'addSettlementToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ settlement });
      comp.ngOnInit();

      expect(settlementService.query).toHaveBeenCalled();
      expect(settlementService.addSettlementToCollectionIfMissing).toHaveBeenCalledWith(settlementCollection, ...additionalSettlements);
      expect(comp.settlementsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Dealer query and add missing value', () => {
      const settlement: ISettlement = { id: 456 };
      const biller: IDealer = { id: 1844 };
      settlement.biller = biller;
      const signatories: IDealer[] = [{ id: 38725 }];
      settlement.signatories = signatories;

      const dealerCollection: IDealer[] = [{ id: 99671 }];
      jest.spyOn(dealerService, 'query').mockReturnValue(of(new HttpResponse({ body: dealerCollection })));
      const additionalDealers = [biller, ...signatories];
      const expectedCollection: IDealer[] = [...additionalDealers, ...dealerCollection];
      jest.spyOn(dealerService, 'addDealerToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ settlement });
      comp.ngOnInit();

      expect(dealerService.query).toHaveBeenCalled();
      expect(dealerService.addDealerToCollectionIfMissing).toHaveBeenCalledWith(dealerCollection, ...additionalDealers);
      expect(comp.dealersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call PaymentInvoice query and add missing value', () => {
      const settlement: ISettlement = { id: 456 };
      const paymentInvoices: IPaymentInvoice[] = [{ id: 435 }];
      settlement.paymentInvoices = paymentInvoices;

      const paymentInvoiceCollection: IPaymentInvoice[] = [{ id: 67209 }];
      jest.spyOn(paymentInvoiceService, 'query').mockReturnValue(of(new HttpResponse({ body: paymentInvoiceCollection })));
      const additionalPaymentInvoices = [...paymentInvoices];
      const expectedCollection: IPaymentInvoice[] = [...additionalPaymentInvoices, ...paymentInvoiceCollection];
      jest.spyOn(paymentInvoiceService, 'addPaymentInvoiceToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ settlement });
      comp.ngOnInit();

      expect(paymentInvoiceService.query).toHaveBeenCalled();
      expect(paymentInvoiceService.addPaymentInvoiceToCollectionIfMissing).toHaveBeenCalledWith(
        paymentInvoiceCollection,
        ...additionalPaymentInvoices
      );
      expect(comp.paymentInvoicesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const settlement: ISettlement = { id: 456 };
      const placeholders: IPlaceholder = { id: 41050 };
      settlement.placeholders = [placeholders];
      const settlementCurrency: ISettlementCurrency = { id: 46314 };
      settlement.settlementCurrency = settlementCurrency;
      const paymentLabels: IPaymentLabel = { id: 89836 };
      settlement.paymentLabels = [paymentLabels];
      const paymentCategory: IPaymentCategory = { id: 41328 };
      settlement.paymentCategory = paymentCategory;
      const groupSettlement: ISettlement = { id: 295 };
      settlement.groupSettlement = groupSettlement;
      const biller: IDealer = { id: 9124 };
      settlement.biller = biller;
      const signatories: IDealer = { id: 26842 };
      settlement.signatories = [signatories];
      const paymentInvoices: IPaymentInvoice = { id: 35911 };
      settlement.paymentInvoices = [paymentInvoices];

      activatedRoute.data = of({ settlement });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(settlement));
      expect(comp.placeholdersSharedCollection).toContain(placeholders);
      expect(comp.settlementCurrenciesSharedCollection).toContain(settlementCurrency);
      expect(comp.paymentLabelsSharedCollection).toContain(paymentLabels);
      expect(comp.paymentCategoriesSharedCollection).toContain(paymentCategory);
      expect(comp.settlementsSharedCollection).toContain(groupSettlement);
      expect(comp.dealersSharedCollection).toContain(biller);
      expect(comp.dealersSharedCollection).toContain(signatories);
      expect(comp.paymentInvoicesSharedCollection).toContain(paymentInvoices);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Settlement>>();
      const settlement = { id: 123 };
      jest.spyOn(settlementService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ settlement });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: settlement }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      // TODO expect(settlementService.update).toHaveBeenCalledWith(settlement);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Settlement>>();
      const settlement = new Settlement();
      jest.spyOn(settlementService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ settlement });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: settlement }));
      saveSubject.complete();

      // THEN
      // TODO expect(settlementService.create).toHaveBeenCalledWith(settlement);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Settlement>>();
      const settlement = { id: 123 };
      jest.spyOn(settlementService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ settlement });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      // TODO expect(settlementService.update).toHaveBeenCalledWith(settlement);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    // describe('trackPlaceholderById', () => {
    //   it('Should return tracked Placeholder primary key', () => {
    //     const entity = { id: 123 };
    //     const trackResult = comp.trackPlaceholderById(0, entity);
    //     expect(trackResult).toEqual(entity.id);
    //   });
    // });

    // describe('trackSettlementCurrencyById', () => {
    //   it('Should return tracked SettlementCurrency primary key', () => {
    //     const entity = { id: 123 };
    //     const trackResult = comp.trackSettlementCurrencyById(0, entity);
    //     expect(trackResult).toEqual(entity.id);
    //   });
    // });

    // describe('trackPaymentLabelById', () => {
    //   it('Should return tracked PaymentLabel primary key', () => {
    //     const entity = { id: 123 };
    //     const trackResult = comp.trackPaymentLabelById(0, entity);
    //     expect(trackResult).toEqual(entity.id);
    //   });
    // });

    // describe('trackPaymentCategoryById', () => {
    //   it('Should return tracked PaymentCategory primary key', () => {
    //     const entity = { id: 123 };
    //     const trackResult = comp.trackPaymentCategoryById(0, entity);
    //     expect(trackResult).toEqual(entity.id);
    //   });
    // });

    // describe('trackSettlementById', () => {
    //   it('Should return tracked Settlement primary key', () => {
    //     const entity = { id: 123 };
    //     const trackResult = comp.trackSettlementById(0, entity);
    //     expect(trackResult).toEqual(entity.id);
    //   });
    // });

    // describe('trackDealerById', () => {
    //   it('Should return tracked Dealer primary key', () => {
    //     const entity = { id: 123 };
    //     const trackResult = comp.trackDealerById(0, entity);
    //     expect(trackResult).toEqual(entity.id);
    //   });
    // });

    // describe('trackPaymentInvoiceById', () => {
    //   it('Should return tracked PaymentInvoice primary key', () => {
    //     const entity = { id: 123 };
    //     const trackResult = comp.trackPaymentInvoiceById(0, entity);
    //     expect(trackResult).toEqual(entity.id);
    //   });
    // });
  });

  describe('Getting selected relationships', () => {
    describe('getSelectedPlaceholder', () => {
      // it('Should return option if no Placeholder is selected', () => {
      //   const option = { id: 123 };
      //   const result = comp.getSelectedPlaceholder(option);
      //   expect(result === option).toEqual(true);
      // });

      // it('Should return selected Placeholder for according option', () => {
      //   const option = { id: 123 };
      //   const selected = { id: 123 };
      //   const selected2 = { id: 456 };
      //   const result = comp.getSelectedPlaceholder(option, [selected2, selected]);
      //   expect(result === selected).toEqual(true);
      //   expect(result === selected2).toEqual(false);
      //   expect(result === option).toEqual(false);
      // });

      // it('Should return option if this Placeholder is not selected', () => {
      //   const option = { id: 123 };
      //   const selected = { id: 456 };
      //   const result = comp.getSelectedPlaceholder(option, [selected]);
      //   expect(result === option).toEqual(true);
      //   expect(result === selected).toEqual(false);
      // });
    });

    describe('getSelectedPaymentLabel', () => {
      // it('Should return option if no PaymentLabel is selected', () => {
      //   const option = { id: 123 };
      //   const result = comp.getSelectedPaymentLabel(option);
      //   expect(result === option).toEqual(true);
      // });

      // it('Should return selected PaymentLabel for according option', () => {
      //   const option = { id: 123 };
      //   const selected = { id: 123 };
      //   const selected2 = { id: 456 };
      //   const result = comp.getSelectedPaymentLabel(option, [selected2, selected]);
      //   expect(result === selected).toEqual(true);
      //   expect(result === selected2).toEqual(false);
      //   expect(result === option).toEqual(false);
      // });

      // it('Should return option if this PaymentLabel is not selected', () => {
      //   const option = { id: 123 };
      //   const selected = { id: 456 };
      //   const result = comp.getSelectedPaymentLabel(option, [selected]);
      //   expect(result === option).toEqual(true);
      //   expect(result === selected).toEqual(false);
      // });
    });

    describe('getSelectedDealer', () => {
      // it('Should return option if no Dealer is selected', () => {
      //   const option = { id: 123 };
      //   const result = comp.getSelectedDealer(option);
      //   expect(result === option).toEqual(true);
      // });

      // it('Should return selected Dealer for according option', () => {
      //   const option = { id: 123 };
      //   const selected = { id: 123 };
      //   const selected2 = { id: 456 };
      //   const result = comp.getSelectedDealer(option, [selected2, selected]);
      //   expect(result === selected).toEqual(true);
      //   expect(result === selected2).toEqual(false);
      //   expect(result === option).toEqual(false);
      // });

      // it('Should return option if this Dealer is not selected', () => {
      //   const option = { id: 123 };
      //   const selected = { id: 456 };
      //   const result = comp.getSelectedDealer(option, [selected]);
      //   expect(result === option).toEqual(true);
      //   expect(result === selected).toEqual(false);
      // });
    });

    describe('getSelectedPaymentInvoice', () => {
      // it('Should return option if no PaymentInvoice is selected', () => {
      //   const option = { id: 123 };
      //   const result = comp.getSelectedPaymentInvoice(option);
      //   expect(result === option).toEqual(true);
      // });

      // it('Should return selected PaymentInvoice for according option', () => {
      //   const option = { id: 123 };
      //   const selected = { id: 123 };
      //   const selected2 = { id: 456 };
      //   const result = comp.getSelectedPaymentInvoice(option, [selected2, selected]);
      //   expect(result === selected).toEqual(true);
      //   expect(result === selected2).toEqual(false);
      //   expect(result === option).toEqual(false);
      // });

      // it('Should return option if this PaymentInvoice is not selected', () => {
      //   const option = { id: 123 };
      //   const selected = { id: 456 };
      //   const result = comp.getSelectedPaymentInvoice(option, [selected]);
      //   expect(result === option).toEqual(true);
      //   expect(result === selected).toEqual(false);
      // });
    });
  });
});
