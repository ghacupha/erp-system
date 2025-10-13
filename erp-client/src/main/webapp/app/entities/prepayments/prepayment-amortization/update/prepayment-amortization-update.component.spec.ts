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

jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { PrepaymentAmortizationService } from '../service/prepayment-amortization.service';
import { IPrepaymentAmortization, PrepaymentAmortization } from '../prepayment-amortization.model';
import { IPrepaymentAccount } from 'app/entities/prepayments/prepayment-account/prepayment-account.model';
import { PrepaymentAccountService } from 'app/entities/prepayments/prepayment-account/service/prepayment-account.service';
import { ISettlementCurrency } from 'app/entities/system/settlement-currency/settlement-currency.model';
import { SettlementCurrencyService } from 'app/entities/system/settlement-currency/service/settlement-currency.service';
import { ITransactionAccount } from 'app/entities/accounting/transaction-account/transaction-account.model';
import { TransactionAccountService } from 'app/entities/accounting/transaction-account/service/transaction-account.service';
import { IPlaceholder } from 'app/entities/system/placeholder/placeholder.model';
import { PlaceholderService } from 'app/entities/system/placeholder/service/placeholder.service';
import { IFiscalMonth } from 'app/entities/system/fiscal-month/fiscal-month.model';
import { FiscalMonthService } from 'app/entities/system/fiscal-month/service/fiscal-month.service';
import { IPrepaymentCompilationRequest } from 'app/entities/prepayments/prepayment-compilation-request/prepayment-compilation-request.model';
import { PrepaymentCompilationRequestService } from 'app/entities/prepayments/prepayment-compilation-request/service/prepayment-compilation-request.service';
import { IAmortizationPeriod } from 'app/entities/prepayments/amortization-period/amortization-period.model';
import { AmortizationPeriodService } from 'app/entities/prepayments/amortization-period/service/amortization-period.service';

import { PrepaymentAmortizationUpdateComponent } from './prepayment-amortization-update.component';

describe('PrepaymentAmortization Management Update Component', () => {
  let comp: PrepaymentAmortizationUpdateComponent;
  let fixture: ComponentFixture<PrepaymentAmortizationUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let prepaymentAmortizationService: PrepaymentAmortizationService;
  let prepaymentAccountService: PrepaymentAccountService;
  let settlementCurrencyService: SettlementCurrencyService;
  let transactionAccountService: TransactionAccountService;
  let placeholderService: PlaceholderService;
  let fiscalMonthService: FiscalMonthService;
  let prepaymentCompilationRequestService: PrepaymentCompilationRequestService;
  let amortizationPeriodService: AmortizationPeriodService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [PrepaymentAmortizationUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(PrepaymentAmortizationUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PrepaymentAmortizationUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    prepaymentAmortizationService = TestBed.inject(PrepaymentAmortizationService);
    prepaymentAccountService = TestBed.inject(PrepaymentAccountService);
    settlementCurrencyService = TestBed.inject(SettlementCurrencyService);
    transactionAccountService = TestBed.inject(TransactionAccountService);
    placeholderService = TestBed.inject(PlaceholderService);
    fiscalMonthService = TestBed.inject(FiscalMonthService);
    prepaymentCompilationRequestService = TestBed.inject(PrepaymentCompilationRequestService);
    amortizationPeriodService = TestBed.inject(AmortizationPeriodService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call PrepaymentAccount query and add missing value', () => {
      const prepaymentAmortization: IPrepaymentAmortization = { id: 456 };
      const prepaymentAccount: IPrepaymentAccount = { id: 99710 };
      prepaymentAmortization.prepaymentAccount = prepaymentAccount;

      const prepaymentAccountCollection: IPrepaymentAccount[] = [{ id: 44525 }];
      jest.spyOn(prepaymentAccountService, 'query').mockReturnValue(of(new HttpResponse({ body: prepaymentAccountCollection })));
      const additionalPrepaymentAccounts = [prepaymentAccount];
      const expectedCollection: IPrepaymentAccount[] = [...additionalPrepaymentAccounts, ...prepaymentAccountCollection];
      jest.spyOn(prepaymentAccountService, 'addPrepaymentAccountToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ prepaymentAmortization });
      comp.ngOnInit();

      expect(prepaymentAccountService.query).toHaveBeenCalled();
      expect(prepaymentAccountService.addPrepaymentAccountToCollectionIfMissing).toHaveBeenCalledWith(
        prepaymentAccountCollection,
        ...additionalPrepaymentAccounts
      );
      expect(comp.prepaymentAccountsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call SettlementCurrency query and add missing value', () => {
      const prepaymentAmortization: IPrepaymentAmortization = { id: 456 };
      const settlementCurrency: ISettlementCurrency = { id: 58816 };
      prepaymentAmortization.settlementCurrency = settlementCurrency;

      const settlementCurrencyCollection: ISettlementCurrency[] = [{ id: 45808 }];
      jest.spyOn(settlementCurrencyService, 'query').mockReturnValue(of(new HttpResponse({ body: settlementCurrencyCollection })));
      const additionalSettlementCurrencies = [settlementCurrency];
      const expectedCollection: ISettlementCurrency[] = [...additionalSettlementCurrencies, ...settlementCurrencyCollection];
      jest.spyOn(settlementCurrencyService, 'addSettlementCurrencyToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ prepaymentAmortization });
      comp.ngOnInit();

      expect(settlementCurrencyService.query).toHaveBeenCalled();
      expect(settlementCurrencyService.addSettlementCurrencyToCollectionIfMissing).toHaveBeenCalledWith(
        settlementCurrencyCollection,
        ...additionalSettlementCurrencies
      );
      expect(comp.settlementCurrenciesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call TransactionAccount query and add missing value', () => {
      const prepaymentAmortization: IPrepaymentAmortization = { id: 456 };
      const debitAccount: ITransactionAccount = { id: 44106 };
      prepaymentAmortization.debitAccount = debitAccount;
      const creditAccount: ITransactionAccount = { id: 61881 };
      prepaymentAmortization.creditAccount = creditAccount;

      const transactionAccountCollection: ITransactionAccount[] = [{ id: 99394 }];
      jest.spyOn(transactionAccountService, 'query').mockReturnValue(of(new HttpResponse({ body: transactionAccountCollection })));
      const additionalTransactionAccounts = [debitAccount, creditAccount];
      const expectedCollection: ITransactionAccount[] = [...additionalTransactionAccounts, ...transactionAccountCollection];
      jest.spyOn(transactionAccountService, 'addTransactionAccountToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ prepaymentAmortization });
      comp.ngOnInit();

      expect(transactionAccountService.query).toHaveBeenCalled();
      expect(transactionAccountService.addTransactionAccountToCollectionIfMissing).toHaveBeenCalledWith(
        transactionAccountCollection,
        ...additionalTransactionAccounts
      );
      expect(comp.transactionAccountsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Placeholder query and add missing value', () => {
      const prepaymentAmortization: IPrepaymentAmortization = { id: 456 };
      const placeholders: IPlaceholder[] = [{ id: 50427 }];
      prepaymentAmortization.placeholders = placeholders;

      const placeholderCollection: IPlaceholder[] = [{ id: 26795 }];
      jest.spyOn(placeholderService, 'query').mockReturnValue(of(new HttpResponse({ body: placeholderCollection })));
      const additionalPlaceholders = [...placeholders];
      const expectedCollection: IPlaceholder[] = [...additionalPlaceholders, ...placeholderCollection];
      jest.spyOn(placeholderService, 'addPlaceholderToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ prepaymentAmortization });
      comp.ngOnInit();

      expect(placeholderService.query).toHaveBeenCalled();
      expect(placeholderService.addPlaceholderToCollectionIfMissing).toHaveBeenCalledWith(placeholderCollection, ...additionalPlaceholders);
      expect(comp.placeholdersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call FiscalMonth query and add missing value', () => {
      const prepaymentAmortization: IPrepaymentAmortization = { id: 456 };
      const fiscalMonth: IFiscalMonth = { id: 97025 };
      prepaymentAmortization.fiscalMonth = fiscalMonth;

      const fiscalMonthCollection: IFiscalMonth[] = [{ id: 8719 }];
      jest.spyOn(fiscalMonthService, 'query').mockReturnValue(of(new HttpResponse({ body: fiscalMonthCollection })));
      const additionalFiscalMonths = [fiscalMonth];
      const expectedCollection: IFiscalMonth[] = [...additionalFiscalMonths, ...fiscalMonthCollection];
      jest.spyOn(fiscalMonthService, 'addFiscalMonthToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ prepaymentAmortization });
      comp.ngOnInit();

      expect(fiscalMonthService.query).toHaveBeenCalled();
      expect(fiscalMonthService.addFiscalMonthToCollectionIfMissing).toHaveBeenCalledWith(fiscalMonthCollection, ...additionalFiscalMonths);
      expect(comp.fiscalMonthsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call PrepaymentCompilationRequest query and add missing value', () => {
      const prepaymentAmortization: IPrepaymentAmortization = { id: 456 };
      const prepaymentCompilationRequest: IPrepaymentCompilationRequest = { id: 36110 };
      prepaymentAmortization.prepaymentCompilationRequest = prepaymentCompilationRequest;

      const prepaymentCompilationRequestCollection: IPrepaymentCompilationRequest[] = [{ id: 36228 }];
      jest
        .spyOn(prepaymentCompilationRequestService, 'query')
        .mockReturnValue(of(new HttpResponse({ body: prepaymentCompilationRequestCollection })));
      const additionalPrepaymentCompilationRequests = [prepaymentCompilationRequest];
      const expectedCollection: IPrepaymentCompilationRequest[] = [
        ...additionalPrepaymentCompilationRequests,
        ...prepaymentCompilationRequestCollection,
      ];
      jest
        .spyOn(prepaymentCompilationRequestService, 'addPrepaymentCompilationRequestToCollectionIfMissing')
        .mockReturnValue(expectedCollection);

      activatedRoute.data = of({ prepaymentAmortization });
      comp.ngOnInit();

      expect(prepaymentCompilationRequestService.query).toHaveBeenCalled();
      expect(prepaymentCompilationRequestService.addPrepaymentCompilationRequestToCollectionIfMissing).toHaveBeenCalledWith(
        prepaymentCompilationRequestCollection,
        ...additionalPrepaymentCompilationRequests
      );
      expect(comp.prepaymentCompilationRequestsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call AmortizationPeriod query and add missing value', () => {
      const prepaymentAmortization: IPrepaymentAmortization = { id: 456 };
      const amortizationPeriod: IAmortizationPeriod = { id: 95682 };
      prepaymentAmortization.amortizationPeriod = amortizationPeriod;

      const amortizationPeriodCollection: IAmortizationPeriod[] = [{ id: 24883 }];
      jest.spyOn(amortizationPeriodService, 'query').mockReturnValue(of(new HttpResponse({ body: amortizationPeriodCollection })));
      const additionalAmortizationPeriods = [amortizationPeriod];
      const expectedCollection: IAmortizationPeriod[] = [...additionalAmortizationPeriods, ...amortizationPeriodCollection];
      jest.spyOn(amortizationPeriodService, 'addAmortizationPeriodToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ prepaymentAmortization });
      comp.ngOnInit();

      expect(amortizationPeriodService.query).toHaveBeenCalled();
      expect(amortizationPeriodService.addAmortizationPeriodToCollectionIfMissing).toHaveBeenCalledWith(
        amortizationPeriodCollection,
        ...additionalAmortizationPeriods
      );
      expect(comp.amortizationPeriodsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const prepaymentAmortization: IPrepaymentAmortization = { id: 456 };
      const prepaymentAccount: IPrepaymentAccount = { id: 29845 };
      prepaymentAmortization.prepaymentAccount = prepaymentAccount;
      const settlementCurrency: ISettlementCurrency = { id: 68197 };
      prepaymentAmortization.settlementCurrency = settlementCurrency;
      const debitAccount: ITransactionAccount = { id: 3438 };
      prepaymentAmortization.debitAccount = debitAccount;
      const creditAccount: ITransactionAccount = { id: 39156 };
      prepaymentAmortization.creditAccount = creditAccount;
      const placeholders: IPlaceholder = { id: 68088 };
      prepaymentAmortization.placeholders = [placeholders];
      const fiscalMonth: IFiscalMonth = { id: 60534 };
      prepaymentAmortization.fiscalMonth = fiscalMonth;
      const prepaymentCompilationRequest: IPrepaymentCompilationRequest = { id: 2753 };
      prepaymentAmortization.prepaymentCompilationRequest = prepaymentCompilationRequest;
      const amortizationPeriod: IAmortizationPeriod = { id: 85145 };
      prepaymentAmortization.amortizationPeriod = amortizationPeriod;

      activatedRoute.data = of({ prepaymentAmortization });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(prepaymentAmortization));
      expect(comp.prepaymentAccountsSharedCollection).toContain(prepaymentAccount);
      expect(comp.settlementCurrenciesSharedCollection).toContain(settlementCurrency);
      expect(comp.transactionAccountsSharedCollection).toContain(debitAccount);
      expect(comp.transactionAccountsSharedCollection).toContain(creditAccount);
      expect(comp.placeholdersSharedCollection).toContain(placeholders);
      expect(comp.fiscalMonthsSharedCollection).toContain(fiscalMonth);
      expect(comp.prepaymentCompilationRequestsSharedCollection).toContain(prepaymentCompilationRequest);
      expect(comp.amortizationPeriodsSharedCollection).toContain(amortizationPeriod);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<PrepaymentAmortization>>();
      const prepaymentAmortization = { id: 123 };
      jest.spyOn(prepaymentAmortizationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ prepaymentAmortization });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: prepaymentAmortization }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(prepaymentAmortizationService.update).toHaveBeenCalledWith(prepaymentAmortization);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<PrepaymentAmortization>>();
      const prepaymentAmortization = new PrepaymentAmortization();
      jest.spyOn(prepaymentAmortizationService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ prepaymentAmortization });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: prepaymentAmortization }));
      saveSubject.complete();

      // THEN
      expect(prepaymentAmortizationService.create).toHaveBeenCalledWith(prepaymentAmortization);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<PrepaymentAmortization>>();
      const prepaymentAmortization = { id: 123 };
      jest.spyOn(prepaymentAmortizationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ prepaymentAmortization });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(prepaymentAmortizationService.update).toHaveBeenCalledWith(prepaymentAmortization);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackPrepaymentAccountById', () => {
      it('Should return tracked PrepaymentAccount primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackPrepaymentAccountById(0, entity);
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

    describe('trackTransactionAccountById', () => {
      it('Should return tracked TransactionAccount primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackTransactionAccountById(0, entity);
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

    describe('trackFiscalMonthById', () => {
      it('Should return tracked FiscalMonth primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackFiscalMonthById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackPrepaymentCompilationRequestById', () => {
      it('Should return tracked PrepaymentCompilationRequest primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackPrepaymentCompilationRequestById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackAmortizationPeriodById', () => {
      it('Should return tracked AmortizationPeriod primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackAmortizationPeriodById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });

  describe('Getting selected relationships', () => {
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
