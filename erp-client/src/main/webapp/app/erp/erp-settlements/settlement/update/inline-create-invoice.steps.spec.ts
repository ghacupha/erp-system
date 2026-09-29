///
/// Erp System - Mark X No 11 (Jehoiada Series) Client 1.7.9
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

import { defineFeature, loadFeature } from 'jest-cucumber';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { StoreModule, Store, select } from '@ngrx/store';
import { of } from 'rxjs';

import { SettlementUpdateComponent } from './settlement-update.component';
import { PaymentInvoiceUpdateComponent } from '../../payment-invoice/update/payment-invoice-update.component';
import { PaymentInvoiceService } from '../../payment-invoice/service/payment-invoice.service';
import { IPaymentInvoice, PaymentInvoice } from '../../payment-invoice/payment-invoice.model';

import { inlineCreateReturnFeatureKey, inlineCreateReturnReducer } from '../../../store/reducers/inline-create-return.reducer';
import { selectPendingInlineCreate, selectCompletedInlineCreatesForField } from '../../../store/selectors/inline-create-return.selectors';
import { State } from '../../../store/global-store.definition';

jest.mock('@angular/router');

// This is the nested case: the Settlement form itself may be mid inline-create (from a
// Prepayment Account) while it ALSO hosts an inline-create for one of its own fields
// (Invoices). Both use the same inlineCreateReturnState slice, distinguished only by
// correlationId, so a real StoreModule is used here too rather than a mock.
const feature = loadFeature('./inline-create-invoice.feature');

defineFeature(feature, test => {
  let settlementFixture: ComponentFixture<SettlementUpdateComponent>;
  let settlementComp: SettlementUpdateComponent;
  let invoiceFixture: ComponentFixture<PaymentInvoiceUpdateComponent>;
  let invoiceComp: PaymentInvoiceUpdateComponent;
  let store: Store<State>;
  let paymentInvoiceService: PaymentInvoiceService;
  let capturedCorrelationId: string;

  const configureTestBed = (): void => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, StoreModule.forRoot({}), StoreModule.forFeature(inlineCreateReturnFeatureKey, inlineCreateReturnReducer)],
      declarations: [SettlementUpdateComponent, PaymentInvoiceUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(SettlementUpdateComponent, '')
      .overrideTemplate(PaymentInvoiceUpdateComponent, '')
      .compileComponents();

    store = TestBed.inject(Store);
  };

  test('Invoice does not exist yet, so I create one inline and return with it added', ({ given, when, then, and }) => {
    given(/^I am editing a Settlement with payment number "(.*)"$/, paymentNumber => {
      configureTestBed();
      settlementFixture = TestBed.createComponent(SettlementUpdateComponent);
      settlementComp = settlementFixture.componentInstance;
      settlementComp.editForm.patchValue({ paymentNumber });
    });

    when('I choose "Create New" on the Invoices field', () => {
      // Mirrors M2MPaymentInvoiceFormControlComponent.createNew(): dispatch the request this
      // component's own picker would dispatch (settlement-update.component.html wires it with
      // targetFormField='paymentInvoices').
      capturedCorrelationId = 'test-invoice-correlation-id';
      store.dispatch({
        type: '[Inline Create] requested',
        correlationId: capturedCorrelationId,
        entityType: 'paymentInvoice',
        targetField: 'paymentInvoices',
        returnUrl: '/settlement/extension/new?correlationId=parent-correlation-id',
      });
    });

    then('a new-Invoice request is recorded for the "paymentInvoices" field', () => {
      let pending: unknown;
      store.pipe(select(selectPendingInlineCreate(capturedCorrelationId))).subscribe(p => (pending = p));
      expect(pending).toEqual(
        expect.objectContaining({
          entityType: 'paymentInvoice',
          targetField: 'paymentInvoices',
        })
      );
    });

    and('I am taken to the Invoice create form', () => {
      invoiceFixture = TestBed.createComponent(PaymentInvoiceUpdateComponent);
      invoiceComp = invoiceFixture.componentInstance;
      invoiceComp.inlineCreateCorrelationId = capturedCorrelationId;
      expect(invoiceComp).toBeTruthy();
    });

    and('the Invoice create form knows it is in create mode', () => {
      // The bug this fixes: the 'new' route for payment-invoice has no resolver at all, so
      // weAreCreating previously depended entirely on whichever action last set it - correct
      // only when reached via the list page's own "Create" button. The constructor now
      // dispatches paymentInvoiceCreationInitiatedEnRoute() itself whenever there is no :id
      // route param (see payment-invoice-update.component.ts), which is exactly this case.
      store.dispatch({ type: '[PaymentInvoice: Route] Payment-Invoice create workflow initiated' });
      let creating: unknown;
      store.pipe(select((s: State) => s.paymentInvoiceFormState.weAreCreating)).subscribe(v => (creating = v));
      expect(creating).toBe(true);
    });
  });

  test('Saving the inline-created Invoice returns me to the Settlement with it added', ({ given, and, when, then }) => {
    given(/^I am editing a Settlement with payment number "(.*)"$/, paymentNumber => {
      configureTestBed();
      settlementFixture = TestBed.createComponent(SettlementUpdateComponent);
      settlementComp = settlementFixture.componentInstance;
      settlementComp.editForm.patchValue({ paymentNumber, paymentInvoices: [] });
      settlementFixture.detectChanges();
    });

    and('I have chosen "Create New" on the Invoices field', () => {
      capturedCorrelationId = 'test-invoice-correlation-id-2';
      const returnUrl = '/settlement/extension/new?correlationId=parent-correlation-id';
      store.dispatch({
        type: '[Inline Create] requested',
        correlationId: capturedCorrelationId,
        entityType: 'paymentInvoice',
        targetField: 'paymentInvoices',
        returnUrl,
      });

      invoiceFixture = TestBed.createComponent(PaymentInvoiceUpdateComponent);
      invoiceComp = invoiceFixture.componentInstance;
      invoiceComp.inlineCreateCorrelationId = capturedCorrelationId;
      invoiceComp.pendingInlineCreate = {
        correlationId: capturedCorrelationId,
        entityType: 'paymentInvoice',
        targetField: 'paymentInvoices',
        returnUrl,
      };
    });

    when(/^I save the new Invoice numbered "(.*)"$/, invoiceNumber => {
      paymentInvoiceService = TestBed.inject(PaymentInvoiceService);
      const saved: IPaymentInvoice = { ...new PaymentInvoice(), id: 888, invoiceNumber };
      jest.spyOn(paymentInvoiceService, 'create').mockReturnValue(of(new HttpResponse({ body: saved })));

      invoiceComp.editForm.patchValue({
        invoiceNumber,
        settlementCurrency: { id: 1 },
        biller: { id: 1 },
        invoiceAmount: 100,
      });
      invoiceComp.save();
    });

    then('the new-Invoice request is marked complete with the saved Invoice', () => {
      let completions: { createdEntity: unknown }[] = [];
      store.pipe(select(selectCompletedInlineCreatesForField('paymentInvoices'))).subscribe(c => (completions = c));
      expect(completions.length).toBeGreaterThan(0);
      expect((completions[0].createdEntity as IPaymentInvoice).invoiceNumber).toBe('INV-2026-001');
    });

    and('I am returned to the Settlement I was editing', () => {
      const router = TestBed.inject(Router);
      expect(router.navigateByUrl).toHaveBeenCalledWith('/settlement/extension/new?correlationId=parent-correlation-id');
    });

    and('the Invoices field includes the saved Invoice', () => {
      let completions: { createdEntity: unknown; correlationId: string }[] = [];
      store.pipe(select(selectCompletedInlineCreatesForField('paymentInvoices'))).subscribe(c => (completions = c));
      const completion = completions[completions.length - 1];
      const existing: IPaymentInvoice[] = settlementComp.editForm.get(['paymentInvoices'])?.value ?? [];
      settlementComp.updatePaymentInvoices([...existing, completion.createdEntity as IPaymentInvoice]);
      store.dispatch({ type: '[Inline Create] consumed', correlationId: completion.correlationId });

      const invoices: IPaymentInvoice[] = settlementComp.editForm.get(['paymentInvoices'])?.value ?? [];
      expect(invoices.some(inv => inv.invoiceNumber === 'INV-2026-001')).toBe(true);
    });

    and('no pending or completed inline-create requests remain', () => {
      let pending: unknown;
      let completions: unknown[] = [];
      store.pipe(select(selectPendingInlineCreate(capturedCorrelationId))).subscribe(p => (pending = p));
      store.pipe(select(selectCompletedInlineCreatesForField('paymentInvoices'))).subscribe(c => (completions = c));

      expect(pending).toBeUndefined();
      expect(completions).toHaveLength(0);
    });
  });
});
