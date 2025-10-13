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

import { PaymentInvoiceService } from '../service/payment-invoice.service';
import { IPaymentInvoice, PaymentInvoice } from '../payment-invoice.model';
import { IPurchaseOrder } from 'app/erp/erp-settlements/purchase-order/purchase-order.model';
import { PurchaseOrderService } from 'app/erp/erp-settlements/purchase-order/service/purchase-order.service';
import { IPlaceholder } from '../../../erp-pages/placeholder/placeholder.model';
import { IPaymentLabel } from '../../../erp-pages/payment-label/payment-label.model';
import { ISettlementCurrency } from 'app/erp/erp-settlements/settlement-currency/settlement-currency.model';
import { SettlementCurrencyService } from 'app/erp/erp-settlements/settlement-currency/service/settlement-currency.service';
import { PlaceholderService } from '../../../erp-pages/placeholder/service/placeholder.service';

import { PaymentLabelService } from '../../../erp-pages/payment-label/service/payment-label.service';

import { PaymentInvoiceUpdateComponent } from './payment-invoice-update.component';
import { DealerService } from '../../../erp-pages/dealers/dealer/service/dealer.service';
import { IDealer } from '../../../erp-pages/dealers/dealer/dealer.model';

describe('PaymentInvoice Management Update Component', () => {
  let comp: PaymentInvoiceUpdateComponent;
  let fixture: ComponentFixture<PaymentInvoiceUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let paymentInvoiceService: PaymentInvoiceService;
  let purchaseOrderService: PurchaseOrderService;
  let placeholderService: PlaceholderService;
  let paymentLabelService: PaymentLabelService;
  let settlementCurrencyService: SettlementCurrencyService;
  let dealerService: DealerService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ErpCommonModule, HttpClientTestingModule],
      declarations: [PaymentInvoiceUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(PaymentInvoiceUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PaymentInvoiceUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    paymentInvoiceService = TestBed.inject(PaymentInvoiceService);
    purchaseOrderService = TestBed.inject(PurchaseOrderService);
    placeholderService = TestBed.inject(PlaceholderService);
    paymentLabelService = TestBed.inject(PaymentLabelService);
    settlementCurrencyService = TestBed.inject(SettlementCurrencyService);
    dealerService = TestBed.inject(DealerService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call PurchaseOrder query and add missing value', () => {
      const paymentInvoice: IPaymentInvoice = { id: 456 };
      const purchaseOrders: IPurchaseOrder[] = [{ id: 37390 }];
      paymentInvoice.purchaseOrders = purchaseOrders;

      const purchaseOrderCollection: IPurchaseOrder[] = [{ id: 64230 }];
      jest.spyOn(purchaseOrderService, 'query').mockReturnValue(of(new HttpResponse({ body: purchaseOrderCollection })));
      const additionalPurchaseOrders = [...purchaseOrders];
      const expectedCollection: IPurchaseOrder[] = [...additionalPurchaseOrders, ...purchaseOrderCollection];
      jest.spyOn(purchaseOrderService, 'addPurchaseOrderToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ paymentInvoice });
      comp.ngOnInit();

      expect(purchaseOrderService.query).toHaveBeenCalled();
      expect(purchaseOrderService.addPurchaseOrderToCollectionIfMissing).toHaveBeenCalledWith(
        purchaseOrderCollection,
        ...additionalPurchaseOrders
      );
      expect(comp.purchaseOrdersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Placeholder query and add missing value', () => {
      const paymentInvoice: IPaymentInvoice = { id: 456 };
      const placeholders: IPlaceholder[] = [{ id: 35975 }];
      paymentInvoice.placeholders = placeholders;

      const placeholderCollection: IPlaceholder[] = [{ id: 99217 }];
      jest.spyOn(placeholderService, 'query').mockReturnValue(of(new HttpResponse({ body: placeholderCollection })));
      const additionalPlaceholders = [...placeholders];
      const expectedCollection: IPlaceholder[] = [...additionalPlaceholders, ...placeholderCollection];
      jest.spyOn(placeholderService, 'addPlaceholderToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ paymentInvoice });
      comp.ngOnInit();

      expect(placeholderService.query).toHaveBeenCalled();
      expect(placeholderService.addPlaceholderToCollectionIfMissing).toHaveBeenCalledWith(placeholderCollection, ...additionalPlaceholders);
      expect(comp.placeholdersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call PaymentLabel query and add missing value', () => {
      const paymentInvoice: IPaymentInvoice = { id: 456 };
      const paymentLabels: IPaymentLabel[] = [{ id: 8658 }];
      paymentInvoice.paymentLabels = paymentLabels;

      const paymentLabelCollection: IPaymentLabel[] = [{ id: 94266 }];
      jest.spyOn(paymentLabelService, 'query').mockReturnValue(of(new HttpResponse({ body: paymentLabelCollection })));
      const additionalPaymentLabels = [...paymentLabels];
      const expectedCollection: IPaymentLabel[] = [...additionalPaymentLabels, ...paymentLabelCollection];
      jest.spyOn(paymentLabelService, 'addPaymentLabelToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ paymentInvoice });
      comp.ngOnInit();

      expect(paymentLabelService.query).toHaveBeenCalled();
      expect(paymentLabelService.addPaymentLabelToCollectionIfMissing).toHaveBeenCalledWith(
        paymentLabelCollection,
        ...additionalPaymentLabels
      );
      expect(comp.paymentLabelsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call SettlementCurrency query and add missing value', () => {
      const paymentInvoice: IPaymentInvoice = { id: 456 };
      const settlementCurrency: ISettlementCurrency = { id: 90456 };
      paymentInvoice.settlementCurrency = settlementCurrency;

      const settlementCurrencyCollection: ISettlementCurrency[] = [{ id: 37541 }];
      jest.spyOn(settlementCurrencyService, 'query').mockReturnValue(of(new HttpResponse({ body: settlementCurrencyCollection })));
      const additionalSettlementCurrencies = [settlementCurrency];
      const expectedCollection: ISettlementCurrency[] = [...additionalSettlementCurrencies, ...settlementCurrencyCollection];
      jest.spyOn(settlementCurrencyService, 'addSettlementCurrencyToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ paymentInvoice });
      comp.ngOnInit();

      expect(settlementCurrencyService.query).toHaveBeenCalled();
      expect(settlementCurrencyService.addSettlementCurrencyToCollectionIfMissing).toHaveBeenCalledWith(
        settlementCurrencyCollection,
        ...additionalSettlementCurrencies
      );
      expect(comp.settlementCurrenciesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Dealer query and add missing value', () => {
      const paymentInvoice: IPaymentInvoice = { id: 456 };
      const biller: IDealer = { id: 60282 };
      paymentInvoice.biller = biller;

      const dealerCollection: IDealer[] = [{ id: 2613 }];
      jest.spyOn(dealerService, 'query').mockReturnValue(of(new HttpResponse({ body: dealerCollection })));
      const additionalDealers = [biller];
      const expectedCollection: IDealer[] = [...additionalDealers, ...dealerCollection];
      jest.spyOn(dealerService, 'addDealerToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ paymentInvoice });
      comp.ngOnInit();

      expect(dealerService.query).toHaveBeenCalled();
      expect(dealerService.addDealerToCollectionIfMissing).toHaveBeenCalledWith(dealerCollection, ...additionalDealers);
      expect(comp.dealersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const paymentInvoice: IPaymentInvoice = { id: 456 };
      const purchaseOrders: IPurchaseOrder = { id: 92226 };
      paymentInvoice.purchaseOrders = [purchaseOrders];
      const placeholders: IPlaceholder = { id: 83467 };
      paymentInvoice.placeholders = [placeholders];
      const paymentLabels: IPaymentLabel = { id: 58006 };
      paymentInvoice.paymentLabels = [paymentLabels];
      const settlementCurrency: ISettlementCurrency = { id: 20996 };
      paymentInvoice.settlementCurrency = settlementCurrency;
      const biller: IDealer = { id: 64409 };
      paymentInvoice.biller = biller;

      activatedRoute.data = of({ paymentInvoice });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(paymentInvoice));
      expect(comp.purchaseOrdersSharedCollection).toContain(purchaseOrders);
      expect(comp.placeholdersSharedCollection).toContain(placeholders);
      expect(comp.paymentLabelsSharedCollection).toContain(paymentLabels);
      expect(comp.settlementCurrenciesSharedCollection).toContain(settlementCurrency);
      expect(comp.dealersSharedCollection).toContain(biller);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<PaymentInvoice>>();
      const paymentInvoice = { id: 123 };
      jest.spyOn(paymentInvoiceService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ paymentInvoice });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: paymentInvoice }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(paymentInvoiceService.update).toHaveBeenCalledWith(paymentInvoice);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<PaymentInvoice>>();
      const paymentInvoice = new PaymentInvoice();
      jest.spyOn(paymentInvoiceService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ paymentInvoice });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: paymentInvoice }));
      saveSubject.complete();

      // THEN
      expect(paymentInvoiceService.create).toHaveBeenCalledWith(paymentInvoice);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<PaymentInvoice>>();
      const paymentInvoice = { id: 123 };
      jest.spyOn(paymentInvoiceService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ paymentInvoice });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(paymentInvoiceService.update).toHaveBeenCalledWith(paymentInvoice);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackPurchaseOrderById', () => {
      it('Should return tracked PurchaseOrder primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackPurchaseOrderById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackPlaceholderById', () => {
      it('Should return tracked Placeholder primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackPlaceholderById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackPaymentLabelById', () => {
      it('Should return tracked PaymentLabel primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackPaymentLabelById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackSettlementCurrencyById', () => {
      it('Should return tracked SettlementCurrency primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackSettlementCurrencyById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackDealerById', () => {
      it('Should return tracked Dealer primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackDealerById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });

  describe('Getting selected relationships', () => {
    describe('getSelectedPurchaseOrder', () => {
      it('Should return option if no PurchaseOrder is selected', () => {
        const option = { id: 123 };
        const result = comp.getSelectedPurchaseOrder(option);
        expect(result === option).toEqual(true);
      });

      it('Should return selected PurchaseOrder for according option', () => {
        const option = { id: 123 };
        const selected = { id: 123 };
        const selected2 = { id: 456 };
        const result = comp.getSelectedPurchaseOrder(option, [selected2, selected]);
        expect(result === selected).toEqual(true);
        expect(result === selected2).toEqual(false);
        expect(result === option).toEqual(false);
      });

      it('Should return option if this PurchaseOrder is not selected', () => {
        const option = { id: 123 };
        const selected = { id: 456 };
        const result = comp.getSelectedPurchaseOrder(option, [selected]);
        expect(result === option).toEqual(true);
        expect(result === selected).toEqual(false);
      });
    });

    describe('getSelectedPlaceholder', () => {
      it('Should return option if no Placeholder is selected', () => {
        const option = { id: 123 };
        const result = comp.getSelectedPlaceholder(option);
        expect(result === option).toEqual(true);
      });

      it('Should return selected Placeholder for according option', () => {
        const option = { id: 123 };
        const selected = { id: 123 };
        const selected2 = { id: 456 };
        const result = comp.getSelectedPlaceholder(option, [selected2, selected]);
        expect(result === selected).toEqual(true);
        expect(result === selected2).toEqual(false);
        expect(result === option).toEqual(false);
      });

      it('Should return option if this Placeholder is not selected', () => {
        const option = { id: 123 };
        const selected = { id: 456 };
        const result = comp.getSelectedPlaceholder(option, [selected]);
        expect(result === option).toEqual(true);
        expect(result === selected).toEqual(false);
      });
    });

    describe('getSelectedPaymentLabel', () => {
      it('Should return option if no PaymentLabel is selected', () => {
        const option = { id: 123 };
        const result = comp.getSelectedPaymentLabel(option);
        expect(result === option).toEqual(true);
      });

      it('Should return selected PaymentLabel for according option', () => {
        const option = { id: 123 };
        const selected = { id: 123 };
        const selected2 = { id: 456 };
        const result = comp.getSelectedPaymentLabel(option, [selected2, selected]);
        expect(result === selected).toEqual(true);
        expect(result === selected2).toEqual(false);
        expect(result === option).toEqual(false);
      });

      it('Should return option if this PaymentLabel is not selected', () => {
        const option = { id: 123 };
        const selected = { id: 456 };
        const result = comp.getSelectedPaymentLabel(option, [selected]);
        expect(result === option).toEqual(true);
        expect(result === selected).toEqual(false);
      });
    });
  });
});
