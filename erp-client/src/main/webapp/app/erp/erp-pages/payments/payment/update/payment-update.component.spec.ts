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

import { PaymentLabelService } from '../../../payment-label/service/payment-label.service';

jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { of, Subject } from 'rxjs';

import { PaymentUpdateComponent } from './payment-update.component';
import { initialState } from '../../../../store/global-store.definition';
import { MockStore, provideMockStore } from '@ngrx/store/testing';
import { LoggerTestingModule } from 'ngx-logger/testing';
import { ErpCommonModule } from '../../../../erp-common/erp-common.module';
import { PaymentService } from '../service/payment.service';
import { IPayment, Payment } from '../payment.model';
import { IPaymentLabel } from '../../../payment-label/payment-label.model';
import { IPlaceholder } from '../../../placeholder/placeholder.model';
import { IPaymentCategory } from '../../../../erp-settlements/payments/payment-category/payment-category.model';
import { PaymentCategoryService } from '../../../../erp-settlements/payments/payment-category/service/payment-category.service';
import { PlaceholderService } from '../../../placeholder/service/placeholder.service';

describe('Component Tests', () => {
  describe('Payment Management Update Component', () => {
    let comp: PaymentUpdateComponent;
    let fixture: ComponentFixture<PaymentUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let paymentService: PaymentService;
    let paymentLabelService: PaymentLabelService;
    let paymentCategoryService: PaymentCategoryService;
    let placeholderService: PlaceholderService;
    let store: MockStore;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ErpCommonModule, HttpClientTestingModule, LoggerTestingModule],
        declarations: [PaymentUpdateComponent],
        providers: [
          Router,
          FormBuilder,
          ActivatedRoute,
          provideMockStore({ initialState })
        ]
      })
        .overrideTemplate(PaymentUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PaymentUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      paymentService = TestBed.inject(PaymentService);
      paymentLabelService = TestBed.inject(PaymentLabelService);
      paymentCategoryService = TestBed.inject(PaymentCategoryService);
      placeholderService = TestBed.inject(PlaceholderService);
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      store = TestBed.inject(MockStore);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call PaymentLabel query and add missing value', () => {
        const payment: IPayment = { id: 456 };
        const paymentLabels: IPaymentLabel[] = [{ id: 58999 }];
        payment.paymentLabels = paymentLabels;

        const paymentLabelCollection: IPaymentLabel[] = [{ id: 50464 }];
        jest.spyOn(paymentLabelService, 'query').mockReturnValue(of(new HttpResponse({ body: paymentLabelCollection })));
        const additionalPaymentLabels = [...paymentLabels];
        const expectedCollection: IPaymentLabel[] = [...additionalPaymentLabels, ...paymentLabelCollection];
        jest.spyOn(paymentLabelService, 'addPaymentLabelToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ payment });
        comp.ngOnInit();

        expect(paymentLabelService.query).toHaveBeenCalled();
        expect(paymentLabelService.addPaymentLabelToCollectionIfMissing).toHaveBeenCalledWith(
          paymentLabelCollection,
          ...additionalPaymentLabels
        );
        expect(comp.paymentLabelsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call PaymentCategory query and add missing value', () => {
        const payment: IPayment = { id: 456 };
        const paymentCategory: IPaymentCategory = { id: 30270 };
        payment.paymentCategory = paymentCategory;

        const paymentCategoryCollection: IPaymentCategory[] = [{ id: 97857 }];
        jest.spyOn(paymentCategoryService, 'query').mockReturnValue(of(new HttpResponse({ body: paymentCategoryCollection })));
        const additionalPaymentCategories = [paymentCategory];
        const expectedCollection: IPaymentCategory[] = [...additionalPaymentCategories, ...paymentCategoryCollection];
        jest.spyOn(paymentCategoryService, 'addPaymentCategoryToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ payment });
        comp.ngOnInit();

        expect(paymentCategoryService.query).toHaveBeenCalled();
        // expect(paymentCategoryService.addPaymentCategoryToCollectionIfMissing).toHaveBeenCalledWith(
        //   paymentCategoryCollection,
        //   ...additionalPaymentCategories
        // );
        expect(comp.paymentCategoriesSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Placeholder query and add missing value', () => {
        const payment: IPayment = { id: 456 };
        const placeholders: IPlaceholder[] = [{ id: 68435 }];
        payment.placeholders = placeholders;

        const placeholderCollection: IPlaceholder[] = [{ id: 61478 }];
        jest.spyOn(placeholderService, 'query').mockReturnValue(of(new HttpResponse({ body: placeholderCollection })));
        const additionalPlaceholders = [...placeholders];
        const expectedCollection: IPlaceholder[] = [...additionalPlaceholders, ...placeholderCollection];
        jest.spyOn(placeholderService, 'addPlaceholderToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ payment });
        comp.ngOnInit();

        expect(placeholderService.query).toHaveBeenCalled();
        expect(placeholderService.addPlaceholderToCollectionIfMissing).toHaveBeenCalledWith(placeholderCollection, ...additionalPlaceholders);
        expect(comp.placeholdersSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Payment query and add missing value', () => {
        const payment: IPayment = { id: 456 };
        const paymentGroup: IPayment = { id: 69844 };
        payment.paymentGroup = paymentGroup;

        const paymentCollection: IPayment[] = [{ id: 41984 }];
        jest.spyOn(paymentService, 'query').mockReturnValue(of(new HttpResponse({ body: paymentCollection })));
        const additionalPayments = [paymentGroup];
        const expectedCollection: IPayment[] = [...additionalPayments, ...paymentCollection];
        jest.spyOn(paymentService, 'addPaymentToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ payment });
        comp.ngOnInit();

        expect(paymentService.query).toHaveBeenCalled();
        expect(paymentService.addPaymentToCollectionIfMissing).toHaveBeenCalledWith(paymentCollection, ...additionalPayments);
        expect(comp.paymentsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const payment: IPayment = { id: 456 };
        const paymentLabels: IPaymentLabel = { id: 75140 };
        payment.paymentLabels = [paymentLabels];
        const paymentCategory: IPaymentCategory = { id: 45196 };
        payment.paymentCategory = paymentCategory;
        const placeholders: IPlaceholder = { id: 22052 };
        payment.placeholders = [placeholders];
        const paymentGroup: IPayment = { id: 39977 };
        payment.paymentGroup = paymentGroup;

        activatedRoute.data = of({ payment });
        comp.ngOnInit();

        // expect(comp.editForm.value).toEqual(expect.objectContaining(payment));
        expect(comp.paymentLabelsSharedCollection).toContain(paymentLabels);
        expect(comp.paymentCategoriesSharedCollection).toContain(paymentCategory);
        expect(comp.placeholdersSharedCollection).toContain(placeholders);
        expect(comp.paymentsSharedCollection).toContain(paymentGroup);
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
       // expect(paymentService.update).toHaveBeenCalledWith(payment);
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
        // expect(paymentService.create).toHaveBeenCalledWith(payment);
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
        // expect(paymentService.update).toHaveBeenCalledWith(payment);
        expect(comp.isSaving).toEqual(false);
        // expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackPaymentLabelById', () => {
        it('Should return tracked PaymentLabel primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackPaymentLabelById(0, entity);
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

      describe('trackPlaceholderById', () => {
        it('Should return tracked Placeholder primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackPlaceholderById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackPaymentById', () => {
        it('Should return tracked Payment primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackPaymentById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });

    describe('Getting selected relationships', () => {
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
    });
  });
});
