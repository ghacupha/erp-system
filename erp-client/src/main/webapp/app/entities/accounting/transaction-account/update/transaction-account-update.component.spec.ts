///
/// Erp System - Mark X No 11 (Jehoiada Series) Client 1.7.8
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

import { TransactionAccountService } from '../service/transaction-account.service';
import { ITransactionAccount, TransactionAccount } from '../transaction-account.model';
import { ITransactionAccountLedger } from 'app/entities/accounting/transaction-account-ledger/transaction-account-ledger.model';
import { TransactionAccountLedgerService } from 'app/entities/accounting/transaction-account-ledger/service/transaction-account-ledger.service';
import { ITransactionAccountCategory } from 'app/entities/accounting/transaction-account-category/transaction-account-category.model';
import { TransactionAccountCategoryService } from 'app/entities/accounting/transaction-account-category/service/transaction-account-category.service';
import { IPlaceholder } from 'app/entities/system/placeholder/placeholder.model';
import { PlaceholderService } from 'app/entities/system/placeholder/service/placeholder.service';
import { IServiceOutlet } from 'app/entities/system/service-outlet/service-outlet.model';
import { ServiceOutletService } from 'app/entities/system/service-outlet/service/service-outlet.service';
import { ISettlementCurrency } from 'app/entities/system/settlement-currency/settlement-currency.model';
import { SettlementCurrencyService } from 'app/entities/system/settlement-currency/service/settlement-currency.service';
import { IReportingEntity } from 'app/entities/admin/reporting-entity/reporting-entity.model';
import { ReportingEntityService } from 'app/entities/admin/reporting-entity/service/reporting-entity.service';

import { TransactionAccountUpdateComponent } from './transaction-account-update.component';

describe('TransactionAccount Management Update Component', () => {
  let comp: TransactionAccountUpdateComponent;
  let fixture: ComponentFixture<TransactionAccountUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let transactionAccountService: TransactionAccountService;
  let transactionAccountLedgerService: TransactionAccountLedgerService;
  let transactionAccountCategoryService: TransactionAccountCategoryService;
  let placeholderService: PlaceholderService;
  let serviceOutletService: ServiceOutletService;
  let settlementCurrencyService: SettlementCurrencyService;
  let reportingEntityService: ReportingEntityService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [TransactionAccountUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(TransactionAccountUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TransactionAccountUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    transactionAccountService = TestBed.inject(TransactionAccountService);
    transactionAccountLedgerService = TestBed.inject(TransactionAccountLedgerService);
    transactionAccountCategoryService = TestBed.inject(TransactionAccountCategoryService);
    placeholderService = TestBed.inject(PlaceholderService);
    serviceOutletService = TestBed.inject(ServiceOutletService);
    settlementCurrencyService = TestBed.inject(SettlementCurrencyService);
    reportingEntityService = TestBed.inject(ReportingEntityService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call TransactionAccountLedger query and add missing value', () => {
      const transactionAccount: ITransactionAccount = { id: 456 };
      const accountLedger: ITransactionAccountLedger = { id: 48225 };
      transactionAccount.accountLedger = accountLedger;

      const transactionAccountLedgerCollection: ITransactionAccountLedger[] = [{ id: 88130 }];
      jest
        .spyOn(transactionAccountLedgerService, 'query')
        .mockReturnValue(of(new HttpResponse({ body: transactionAccountLedgerCollection })));
      const additionalTransactionAccountLedgers = [accountLedger];
      const expectedCollection: ITransactionAccountLedger[] = [
        ...additionalTransactionAccountLedgers,
        ...transactionAccountLedgerCollection,
      ];
      jest.spyOn(transactionAccountLedgerService, 'addTransactionAccountLedgerToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ transactionAccount });
      comp.ngOnInit();

      expect(transactionAccountLedgerService.query).toHaveBeenCalled();
      expect(transactionAccountLedgerService.addTransactionAccountLedgerToCollectionIfMissing).toHaveBeenCalledWith(
        transactionAccountLedgerCollection,
        ...additionalTransactionAccountLedgers
      );
      expect(comp.transactionAccountLedgersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call TransactionAccountCategory query and add missing value', () => {
      const transactionAccount: ITransactionAccount = { id: 456 };
      const accountCategory: ITransactionAccountCategory = { id: 66811 };
      transactionAccount.accountCategory = accountCategory;

      const transactionAccountCategoryCollection: ITransactionAccountCategory[] = [{ id: 99751 }];
      jest
        .spyOn(transactionAccountCategoryService, 'query')
        .mockReturnValue(of(new HttpResponse({ body: transactionAccountCategoryCollection })));
      const additionalTransactionAccountCategories = [accountCategory];
      const expectedCollection: ITransactionAccountCategory[] = [
        ...additionalTransactionAccountCategories,
        ...transactionAccountCategoryCollection,
      ];
      jest
        .spyOn(transactionAccountCategoryService, 'addTransactionAccountCategoryToCollectionIfMissing')
        .mockReturnValue(expectedCollection);

      activatedRoute.data = of({ transactionAccount });
      comp.ngOnInit();

      expect(transactionAccountCategoryService.query).toHaveBeenCalled();
      expect(transactionAccountCategoryService.addTransactionAccountCategoryToCollectionIfMissing).toHaveBeenCalledWith(
        transactionAccountCategoryCollection,
        ...additionalTransactionAccountCategories
      );
      expect(comp.transactionAccountCategoriesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Placeholder query and add missing value', () => {
      const transactionAccount: ITransactionAccount = { id: 456 };
      const placeholders: IPlaceholder[] = [{ id: 9276 }];
      transactionAccount.placeholders = placeholders;

      const placeholderCollection: IPlaceholder[] = [{ id: 18792 }];
      jest.spyOn(placeholderService, 'query').mockReturnValue(of(new HttpResponse({ body: placeholderCollection })));
      const additionalPlaceholders = [...placeholders];
      const expectedCollection: IPlaceholder[] = [...additionalPlaceholders, ...placeholderCollection];
      jest.spyOn(placeholderService, 'addPlaceholderToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ transactionAccount });
      comp.ngOnInit();

      expect(placeholderService.query).toHaveBeenCalled();
      expect(placeholderService.addPlaceholderToCollectionIfMissing).toHaveBeenCalledWith(placeholderCollection, ...additionalPlaceholders);
      expect(comp.placeholdersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call ServiceOutlet query and add missing value', () => {
      const transactionAccount: ITransactionAccount = { id: 456 };
      const serviceOutlet: IServiceOutlet = { id: 698 };
      transactionAccount.serviceOutlet = serviceOutlet;

      const serviceOutletCollection: IServiceOutlet[] = [{ id: 39440 }];
      jest.spyOn(serviceOutletService, 'query').mockReturnValue(of(new HttpResponse({ body: serviceOutletCollection })));
      const additionalServiceOutlets = [serviceOutlet];
      const expectedCollection: IServiceOutlet[] = [...additionalServiceOutlets, ...serviceOutletCollection];
      jest.spyOn(serviceOutletService, 'addServiceOutletToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ transactionAccount });
      comp.ngOnInit();

      expect(serviceOutletService.query).toHaveBeenCalled();
      expect(serviceOutletService.addServiceOutletToCollectionIfMissing).toHaveBeenCalledWith(
        serviceOutletCollection,
        ...additionalServiceOutlets
      );
      expect(comp.serviceOutletsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call SettlementCurrency query and add missing value', () => {
      const transactionAccount: ITransactionAccount = { id: 456 };
      const settlementCurrency: ISettlementCurrency = { id: 41390 };
      transactionAccount.settlementCurrency = settlementCurrency;

      const settlementCurrencyCollection: ISettlementCurrency[] = [{ id: 87363 }];
      jest.spyOn(settlementCurrencyService, 'query').mockReturnValue(of(new HttpResponse({ body: settlementCurrencyCollection })));
      const additionalSettlementCurrencies = [settlementCurrency];
      const expectedCollection: ISettlementCurrency[] = [...additionalSettlementCurrencies, ...settlementCurrencyCollection];
      jest.spyOn(settlementCurrencyService, 'addSettlementCurrencyToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ transactionAccount });
      comp.ngOnInit();

      expect(settlementCurrencyService.query).toHaveBeenCalled();
      expect(settlementCurrencyService.addSettlementCurrencyToCollectionIfMissing).toHaveBeenCalledWith(
        settlementCurrencyCollection,
        ...additionalSettlementCurrencies
      );
      expect(comp.settlementCurrenciesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call ReportingEntity query and add missing value', () => {
      const transactionAccount: ITransactionAccount = { id: 456 };
      const institution: IReportingEntity = { id: 99684 };
      transactionAccount.institution = institution;

      const reportingEntityCollection: IReportingEntity[] = [{ id: 14712 }];
      jest.spyOn(reportingEntityService, 'query').mockReturnValue(of(new HttpResponse({ body: reportingEntityCollection })));
      const additionalReportingEntities = [institution];
      const expectedCollection: IReportingEntity[] = [...additionalReportingEntities, ...reportingEntityCollection];
      jest.spyOn(reportingEntityService, 'addReportingEntityToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ transactionAccount });
      comp.ngOnInit();

      expect(reportingEntityService.query).toHaveBeenCalled();
      expect(reportingEntityService.addReportingEntityToCollectionIfMissing).toHaveBeenCalledWith(
        reportingEntityCollection,
        ...additionalReportingEntities
      );
      expect(comp.reportingEntitiesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const transactionAccount: ITransactionAccount = { id: 456 };
      const accountLedger: ITransactionAccountLedger = { id: 23853 };
      transactionAccount.accountLedger = accountLedger;
      const accountCategory: ITransactionAccountCategory = { id: 49119 };
      transactionAccount.accountCategory = accountCategory;
      const placeholders: IPlaceholder = { id: 58708 };
      transactionAccount.placeholders = [placeholders];
      const serviceOutlet: IServiceOutlet = { id: 75997 };
      transactionAccount.serviceOutlet = serviceOutlet;
      const settlementCurrency: ISettlementCurrency = { id: 38549 };
      transactionAccount.settlementCurrency = settlementCurrency;
      const institution: IReportingEntity = { id: 43092 };
      transactionAccount.institution = institution;

      activatedRoute.data = of({ transactionAccount });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(transactionAccount));
      expect(comp.transactionAccountLedgersSharedCollection).toContain(accountLedger);
      expect(comp.transactionAccountCategoriesSharedCollection).toContain(accountCategory);
      expect(comp.placeholdersSharedCollection).toContain(placeholders);
      expect(comp.serviceOutletsSharedCollection).toContain(serviceOutlet);
      expect(comp.settlementCurrenciesSharedCollection).toContain(settlementCurrency);
      expect(comp.reportingEntitiesSharedCollection).toContain(institution);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<TransactionAccount>>();
      const transactionAccount = { id: 123 };
      jest.spyOn(transactionAccountService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ transactionAccount });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: transactionAccount }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(transactionAccountService.update).toHaveBeenCalledWith(transactionAccount);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<TransactionAccount>>();
      const transactionAccount = new TransactionAccount();
      jest.spyOn(transactionAccountService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ transactionAccount });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: transactionAccount }));
      saveSubject.complete();

      // THEN
      expect(transactionAccountService.create).toHaveBeenCalledWith(transactionAccount);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<TransactionAccount>>();
      const transactionAccount = { id: 123 };
      jest.spyOn(transactionAccountService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ transactionAccount });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(transactionAccountService.update).toHaveBeenCalledWith(transactionAccount);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackTransactionAccountLedgerById', () => {
      it('Should return tracked TransactionAccountLedger primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackTransactionAccountLedgerById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackTransactionAccountCategoryById', () => {
      it('Should return tracked TransactionAccountCategory primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackTransactionAccountCategoryById(0, entity);
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

    describe('trackServiceOutletById', () => {
      it('Should return tracked ServiceOutlet primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackServiceOutletById(0, entity);
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

    describe('trackReportingEntityById', () => {
      it('Should return tracked ReportingEntity primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackReportingEntityById(0, entity);
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
