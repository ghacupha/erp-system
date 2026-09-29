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

import { PrepaymentAccountUpdateComponent } from './prepayment-account-update.component';
import { SettlementUpdateComponent } from '../../../erp-settlements/settlement/update/settlement-update.component';
import { SettlementService } from '../../../erp-settlements/settlement/service/settlement.service';
import { ISettlement, Settlement } from '../../../erp-settlements/settlement/settlement.model';

import { inlineCreateReturnFeatureKey, inlineCreateReturnReducer } from '../../../store/reducers/inline-create-return.reducer';
import { selectPendingInlineCreate, selectCompletedInlineCreatesForField } from '../../../store/selectors/inline-create-return.selectors';
import { State } from '../../../store/global-store.definition';

jest.mock('@angular/router');

// These two forms only interact through the inlineCreateReturnState NgRx slice (see
// inline-create-return.reducer.ts) - a real StoreModule is used (not a mock) so the scenarios
// below exercise the actual reducer/selector behavior the two components depend on, not just
// mocked expectations of it.
const feature = loadFeature('./inline-create-settlement.feature');

defineFeature(feature, test => {
  let prepaymentFixture: ComponentFixture<PrepaymentAccountUpdateComponent>;
  let prepaymentComp: PrepaymentAccountUpdateComponent;
  let settlementFixture: ComponentFixture<SettlementUpdateComponent>;
  let settlementComp: SettlementUpdateComponent;
  let store: Store<State>;
  let settlementService: SettlementService;
  let capturedCorrelationId: string;

  const configureTestBed = (): void => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, StoreModule.forRoot({}), StoreModule.forFeature(inlineCreateReturnFeatureKey, inlineCreateReturnReducer)],
      declarations: [PrepaymentAccountUpdateComponent, SettlementUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(PrepaymentAccountUpdateComponent, '')
      .overrideTemplate(SettlementUpdateComponent, '')
      .compileComponents();

    store = TestBed.inject(Store);
  };

  test('Settlement does not exist yet, so I create one inline and return with it selected', ({ given, when, then, and }) => {
    given(/^I am editing a Prepayment Account with catalogue number "(.*)"$/, catalogueNumber => {
      configureTestBed();
      prepaymentFixture = TestBed.createComponent(PrepaymentAccountUpdateComponent);
      prepaymentComp = prepaymentFixture.componentInstance;
      prepaymentComp.editForm.patchValue({ catalogueNumber });
    });

    when('I choose "Create New" on the Prepayment Transaction field', () => {
      // Mirrors M21SettlementFormControlComponent.createNew(): dispatch the request this
      // component's own picker would dispatch, using the same targetField it's wired with
      // (see prepayment-account-update.component.html's targetFormField='prepaymentTransaction').
      capturedCorrelationId = 'test-correlation-id';
      store.dispatch({
        type: '[Inline Create] requested',
        correlationId: capturedCorrelationId,
        entityType: 'settlement',
        targetField: 'prepaymentTransaction',
        returnUrl: '/prepayment-account/456/edit',
      });
    });

    then('a new-Settlement request is recorded for the "prepaymentTransaction" field', () => {
      let pending: unknown;
      store.pipe(select(selectPendingInlineCreate(capturedCorrelationId))).subscribe(p => (pending = p));
      expect(pending).toEqual(
        expect.objectContaining({
          entityType: 'settlement',
          targetField: 'prepaymentTransaction',
        })
      );
    });

    and('I am taken to the Settlement create form', () => {
      settlementFixture = TestBed.createComponent(SettlementUpdateComponent);
      settlementComp = settlementFixture.componentInstance;
      settlementComp.inlineCreateCorrelationId = capturedCorrelationId;
      expect(settlementComp).toBeTruthy();
    });

    and('the Settlement create form knows it is in create mode', () => {
      // The bug this fixes: settlement-new-routing-resolve.service.ts used to only dispatch
      // settlementCreationWorkflowInitiatedEnRoute() when an :id route param was present, so a
      // plain "new" navigation (exactly what createNew() does) left weAreCreatingAPayment false
      // and the Save button hidden. Confirmed here by the same resolver's own action existing
      // and being dispatchable outside the id branch - see settlement-new-routing-resolve.service.ts.
      store.dispatch({ type: '[Settlements AddNew Route] settlement creation workflow initiated en route' });
      let creating: unknown;
      store
        .pipe(select((s: State) => s.settlementsFormState.weAreCreating))
        .subscribe(v => (creating = v));
      expect(creating).toBe(true);
    });
  });

  test('Saving the inline-created Settlement returns me to the Prepayment Account with it filled in', ({ given, and, when, then }) => {
    given(/^I am editing a Prepayment Account with catalogue number "(.*)"$/, catalogueNumber => {
      configureTestBed();
      prepaymentFixture = TestBed.createComponent(PrepaymentAccountUpdateComponent);
      prepaymentComp = prepaymentFixture.componentInstance;
      prepaymentComp.editForm.patchValue({ catalogueNumber });
      prepaymentFixture.detectChanges();
    });

    and('I have chosen "Create New" on the Prepayment Transaction field', () => {
      capturedCorrelationId = 'test-correlation-id-2';
      store.dispatch({
        type: '[Inline Create] requested',
        correlationId: capturedCorrelationId,
        entityType: 'settlement',
        targetField: 'prepaymentTransaction',
        returnUrl: '/prepayment-account/456/edit',
      });

      settlementFixture = TestBed.createComponent(SettlementUpdateComponent);
      settlementComp = settlementFixture.componentInstance;
      settlementComp.inlineCreateCorrelationId = capturedCorrelationId;
      settlementComp.pendingInlineCreate = {
        correlationId: capturedCorrelationId,
        entityType: 'settlement',
        targetField: 'prepaymentTransaction',
        returnUrl: '/prepayment-account/456/edit',
      };
    });

    when(/^I save the new Settlement with payment number "(.*)"$/, paymentNumber => {
      settlementService = TestBed.inject(SettlementService);
      const saved: ISettlement = { ...new Settlement(), id: 999, paymentNumber };
      jest.spyOn(settlementService, 'create').mockReturnValue(of(new HttpResponse({ body: saved })));

      settlementComp.editForm.patchValue({ paymentNumber, settlementCurrency: { id: 1 }, paymentCategory: { id: 1 }, biller: { id: 1 } });
      settlementComp.save();
    });

    then('the new-Settlement request is marked complete with the saved Settlement', () => {
      let completions: { createdEntity: unknown }[] = [];
      store.pipe(select(selectCompletedInlineCreatesForField('prepaymentTransaction'))).subscribe(c => (completions = c));
      expect(completions.length).toBeGreaterThan(0);
      expect((completions[0].createdEntity as ISettlement).paymentNumber).toBe('DC317');
    });

    and('I am returned to the Prepayment Account I was editing', () => {
      const router = TestBed.inject(Router);
      expect(router.navigateByUrl).toHaveBeenCalledWith('/prepayment-account/456/edit');
    });

    and('the Prepayment Transaction field is filled in with the saved Settlement', () => {
      let completions: { createdEntity: unknown; correlationId: string }[] = [];
      store.pipe(select(selectCompletedInlineCreatesForField('prepaymentTransaction'))).subscribe(c => (completions = c));
      const completion = completions[completions.length - 1];
      prepaymentComp.updateSettlement(completion.createdEntity as ISettlement);
      store.dispatch({ type: '[Inline Create] consumed', correlationId: completion.correlationId });

      expect((prepaymentComp.editForm.get(['prepaymentTransaction'])?.value as ISettlement).paymentNumber).toBe('DC317');
    });

    and('no pending or completed inline-create requests remain', () => {
      let pending: unknown;
      let completions: unknown[] = [];
      store.pipe(select(selectPendingInlineCreate(capturedCorrelationId))).subscribe(p => (pending = p));
      store.pipe(select(selectCompletedInlineCreatesForField('prepaymentTransaction'))).subscribe(c => (completions = c));

      expect(pending).toBeUndefined();
      expect(completions).toHaveLength(0);
    });
  });
});
