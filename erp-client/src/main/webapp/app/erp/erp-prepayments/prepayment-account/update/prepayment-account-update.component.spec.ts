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

import { SettlementCurrencyService } from '../../../erp-settlements/settlement-currency/service/settlement-currency.service';

jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { PrepaymentAccountService } from '../service/prepayment-account.service';
import { IPrepaymentAccount, PrepaymentAccount } from '../prepayment-account.model';

import { PrepaymentAccountUpdateComponent } from './prepayment-account-update.component';
import { DealerService } from '../../../erp-pages/dealers/dealer/service/dealer.service';
import { PlaceholderService } from '../../../erp-pages/placeholder/service/placeholder.service';
import { TransactionAccountService } from '../../../erp-accounts/transaction-account/service/transaction-account.service';
import { SettlementService } from '../../../erp-settlements/settlement/service/settlement.service';
import { ServiceOutletService } from '../../../erp-granular/service-outlet/service/service-outlet.service';
import { UniversallyUniqueMappingService } from '../../../erp-pages/universally-unique-mapping/service/universally-unique-mapping.service';
import { PrepaymentMappingService } from '../../prepayment-mapping/service/prepayment-mapping.service';
import { ISettlement } from '../../../erp-settlements/settlement/settlement.model';
import { ISettlementCurrency } from '../../../erp-settlements/settlement-currency/settlement-currency.model';
import { IServiceOutlet } from '../../../erp-granular/service-outlet/service-outlet.model';
import { IDealer } from '../../../erp-pages/dealers/dealer/dealer.model';
import { IPlaceholder } from '../../../erp-pages/placeholder/placeholder.model';
import { ITransactionAccount } from '../../../erp-accounts/transaction-account/transaction-account.model';
import { IUniversallyUniqueMapping } from '../../../erp-pages/universally-unique-mapping/universally-unique-mapping.model';
import { IPrepaymentMapping } from '../../prepayment-mapping/prepayment-mapping.model';

describe('PrepaymentAccount Management Update Component', () => {
  let comp: PrepaymentAccountUpdateComponent;
  let fixture: ComponentFixture<PrepaymentAccountUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let prepaymentAccountService: PrepaymentAccountService;
  let settlementCurrencyService: SettlementCurrencyService;
  let settlementService: SettlementService;
  let serviceOutletService: ServiceOutletService;
  let dealerService: DealerService;
  let placeholderService: PlaceholderService;
  let transactionAccountService: TransactionAccountService;
  let universallyUniqueMappingService: UniversallyUniqueMappingService;
  let prepaymentMappingService: PrepaymentMappingService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [PrepaymentAccountUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(PrepaymentAccountUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PrepaymentAccountUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    prepaymentAccountService = TestBed.inject(PrepaymentAccountService);
    settlementCurrencyService = TestBed.inject(SettlementCurrencyService);
    settlementService = TestBed.inject(SettlementService);
    serviceOutletService = TestBed.inject(ServiceOutletService);
    dealerService = TestBed.inject(DealerService);
    placeholderService = TestBed.inject(PlaceholderService);
    transactionAccountService = TestBed.inject(TransactionAccountService);
    universallyUniqueMappingService = TestBed.inject(UniversallyUniqueMappingService);
    prepaymentMappingService = TestBed.inject(PrepaymentMappingService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call SettlementCurrency query and add missing value', () => {
      const prepaymentAccount: IPrepaymentAccount = { id: 456 };
      const settlementCurrency: ISettlementCurrency = { id: 78898 };
      prepaymentAccount.settlementCurrency = settlementCurrency;

      const settlementCurrencyCollection: ISettlementCurrency[] = [{ id: 5442 }];
      jest.spyOn(settlementCurrencyService, 'query').mockReturnValue(of(new HttpResponse({ body: settlementCurrencyCollection })));
      const additionalSettlementCurrencies = [settlementCurrency];
      const expectedCollection: ISettlementCurrency[] = [...additionalSettlementCurrencies, ...settlementCurrencyCollection];
      jest.spyOn(settlementCurrencyService, 'addSettlementCurrencyToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ prepaymentAccount });
      comp.ngOnInit();

      expect(settlementCurrencyService.query).toHaveBeenCalled();
      expect(settlementCurrencyService.addSettlementCurrencyToCollectionIfMissing).toHaveBeenCalledWith(
        settlementCurrencyCollection,
        ...additionalSettlementCurrencies
      );
      expect(comp.settlementCurrenciesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Settlement query and add missing value', () => {
      const prepaymentAccount: IPrepaymentAccount = { id: 456 };
      const prepaymentTransaction: ISettlement = { id: 29183 };
      prepaymentAccount.prepaymentTransaction = prepaymentTransaction;

      const settlementCollection: ISettlement[] = [{ id: 16053 }];
      jest.spyOn(settlementService, 'query').mockReturnValue(of(new HttpResponse({ body: settlementCollection })));
      const additionalSettlements = [prepaymentTransaction];
      const expectedCollection: ISettlement[] = [...additionalSettlements, ...settlementCollection];
      jest.spyOn(settlementService, 'addSettlementToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ prepaymentAccount });
      comp.ngOnInit();

      expect(settlementService.query).toHaveBeenCalled();
      expect(settlementService.addSettlementToCollectionIfMissing).toHaveBeenCalledWith(settlementCollection, ...additionalSettlements);
      expect(comp.settlementsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call ServiceOutlet query and add missing value', () => {
      const prepaymentAccount: IPrepaymentAccount = { id: 456 };
      const serviceOutlet: IServiceOutlet = { id: 27846 };
      prepaymentAccount.serviceOutlet = serviceOutlet;

      const serviceOutletCollection: IServiceOutlet[] = [{ id: 93653 }];
      jest.spyOn(serviceOutletService, 'query').mockReturnValue(of(new HttpResponse({ body: serviceOutletCollection })));
      const additionalServiceOutlets = [serviceOutlet];
      const expectedCollection: IServiceOutlet[] = [...additionalServiceOutlets, ...serviceOutletCollection];
      jest.spyOn(serviceOutletService, 'addServiceOutletToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ prepaymentAccount });
      comp.ngOnInit();

      expect(serviceOutletService.query).toHaveBeenCalled();
      expect(serviceOutletService.addServiceOutletToCollectionIfMissing).toHaveBeenCalledWith(
        serviceOutletCollection,
        ...additionalServiceOutlets
      );
      expect(comp.serviceOutletsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Dealer query and add missing value', () => {
      const prepaymentAccount: IPrepaymentAccount = { id: 456 };
      const dealer: IDealer = { id: 11029 };
      prepaymentAccount.dealer = dealer;

      const dealerCollection: IDealer[] = [{ id: 14788 }];
      jest.spyOn(dealerService, 'query').mockReturnValue(of(new HttpResponse({ body: dealerCollection })));
      const additionalDealers = [dealer];
      const expectedCollection: IDealer[] = [...additionalDealers, ...dealerCollection];
      jest.spyOn(dealerService, 'addDealerToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ prepaymentAccount });
      comp.ngOnInit();

      expect(dealerService.query).toHaveBeenCalled();
      expect(dealerService.addDealerToCollectionIfMissing).toHaveBeenCalledWith(dealerCollection, ...additionalDealers);
      expect(comp.dealersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Placeholder query and add missing value', () => {
      const prepaymentAccount: IPrepaymentAccount = { id: 456 };
      const placeholder: IPlaceholder = { id: 20901 };
      prepaymentAccount.placeholders = [placeholder];

      const placeholderCollection: IPlaceholder[] = [{ id: 81553 }];
      jest.spyOn(placeholderService, 'query').mockReturnValue(of(new HttpResponse({ body: placeholderCollection })));
      const additionalPlaceholders = [placeholder];
      const expectedCollection: IPlaceholder[] = [...additionalPlaceholders, ...placeholderCollection];
      jest.spyOn(placeholderService, 'addPlaceholderToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ prepaymentAccount });
      comp.ngOnInit();

      expect(placeholderService.query).toHaveBeenCalled();
      // TODO expect(placeholderService.addPlaceholderToCollectionIfMissing).toHaveBeenCalledWith(placeholderCollection, ...additionalPlaceholders);
      expect(comp.placeholdersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call TransactionAccount query and add missing value', () => {
      const prepaymentAccount: IPrepaymentAccount = { id: 456 };
      const debitAccount: ITransactionAccount = { id: 95760 };
      prepaymentAccount.debitAccount = debitAccount;
      const transferAccount: ITransactionAccount = { id: 17577 };
      prepaymentAccount.transferAccount = transferAccount;

      const transactionAccountCollection: ITransactionAccount[] = [{ id: 39013 }];
      jest.spyOn(transactionAccountService, 'query').mockReturnValue(of(new HttpResponse({ body: transactionAccountCollection })));
      const additionalTransactionAccounts = [debitAccount, transferAccount];
      const expectedCollection: ITransactionAccount[] = [...additionalTransactionAccounts, ...transactionAccountCollection];
      jest.spyOn(transactionAccountService, 'addTransactionAccountToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ prepaymentAccount });
      comp.ngOnInit();

      expect(transactionAccountService.query).toHaveBeenCalled();
      expect(transactionAccountService.addTransactionAccountToCollectionIfMissing).toHaveBeenCalledWith(
        transactionAccountCollection,
        ...additionalTransactionAccounts
      );
      expect(comp.transactionAccountsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const prepaymentAccount: IPrepaymentAccount = { id: 456 };
      const settlementCurrency: ISettlementCurrency = { id: 83958 };
      prepaymentAccount.settlementCurrency = settlementCurrency;
      const prepaymentTransaction: ISettlement = { id: 34708 };
      prepaymentAccount.prepaymentTransaction = prepaymentTransaction;
      const serviceOutlet: IServiceOutlet = { id: 1841 };
      prepaymentAccount.serviceOutlet = serviceOutlet;
      const dealer: IDealer = { id: 51117 };
      prepaymentAccount.dealer = dealer;
      const debitAccount: ITransactionAccount = { id: 2986 };
      prepaymentAccount.debitAccount = debitAccount;
      const transferAccount: ITransactionAccount = { id: 8579 };
      prepaymentAccount.transferAccount = transferAccount;
      const placeholders: IPlaceholder = { id: 55956 };
      prepaymentAccount.placeholders = [placeholders];
      const generalParameters: IUniversallyUniqueMapping = { id: 53339 };
      prepaymentAccount.generalParameters = [generalParameters];
      const prepaymentParameters: IPrepaymentMapping = { id: 91756 };
      prepaymentAccount.prepaymentParameters = [prepaymentParameters];

      activatedRoute.data = of({ prepaymentAccount });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(prepaymentAccount));
      expect(comp.settlementCurrenciesSharedCollection).toContain(settlementCurrency);
      expect(comp.settlementsSharedCollection).toContain(prepaymentTransaction);
      expect(comp.serviceOutletsSharedCollection).toContain(serviceOutlet);
      expect(comp.dealersSharedCollection).toContain(dealer);
      expect(comp.transactionAccountsSharedCollection).toContain(debitAccount);
      expect(comp.transactionAccountsSharedCollection).toContain(transferAccount);
      expect(comp.placeholdersSharedCollection).toContain(placeholders);
      expect(comp.universallyUniqueMappingsSharedCollection).toContain(generalParameters);
      expect(comp.prepaymentMappingsSharedCollection).toContain(prepaymentParameters);
    });
  });

  it('Should call UniversallyUniqueMapping query and add missing value', () => {
    const prepaymentAccount: IPrepaymentAccount = { id: 456 };
    const generalParameters: IUniversallyUniqueMapping[] = [{ id: 44009 }];
    prepaymentAccount.generalParameters = generalParameters;

    const universallyUniqueMappingCollection: IUniversallyUniqueMapping[] = [{ id: 28246 }];
    jest
      .spyOn(universallyUniqueMappingService, 'query')
      .mockReturnValue(of(new HttpResponse({ body: universallyUniqueMappingCollection })));
    const additionalUniversallyUniqueMappings = [...generalParameters];
    const expectedCollection: IUniversallyUniqueMapping[] = [
      ...additionalUniversallyUniqueMappings,
      ...universallyUniqueMappingCollection,
    ];
    jest.spyOn(universallyUniqueMappingService, 'addUniversallyUniqueMappingToCollectionIfMissing').mockReturnValue(expectedCollection);

    activatedRoute.data = of({ prepaymentAccount });
    comp.ngOnInit();

    expect(universallyUniqueMappingService.query).toHaveBeenCalled();
    expect(universallyUniqueMappingService.addUniversallyUniqueMappingToCollectionIfMissing).toHaveBeenCalledWith(
      universallyUniqueMappingCollection,
      ...additionalUniversallyUniqueMappings
    );
    expect(comp.universallyUniqueMappingsSharedCollection).toEqual(expectedCollection);
  });

  it('Should call PrepaymentMapping query and add missing value', () => {
    const prepaymentAccount: IPrepaymentAccount = { id: 456 };
    const prepaymentParameters: IPrepaymentMapping[] = [{ id: 25518 }];
    prepaymentAccount.prepaymentParameters = prepaymentParameters;

    const prepaymentMappingCollection: IPrepaymentMapping[] = [{ id: 67453 }];
    jest.spyOn(prepaymentMappingService, 'query').mockReturnValue(of(new HttpResponse({ body: prepaymentMappingCollection })));
    const additionalPrepaymentMappings = [...prepaymentParameters];
    const expectedCollection: IPrepaymentMapping[] = [...additionalPrepaymentMappings, ...prepaymentMappingCollection];
    jest.spyOn(prepaymentMappingService, 'addPrepaymentMappingToCollectionIfMissing').mockReturnValue(expectedCollection);

    activatedRoute.data = of({ prepaymentAccount });
    comp.ngOnInit();

    expect(prepaymentMappingService.query).toHaveBeenCalled();
    expect(prepaymentMappingService.addPrepaymentMappingToCollectionIfMissing).toHaveBeenCalledWith(
      prepaymentMappingCollection,
      ...additionalPrepaymentMappings
    );
    expect(comp.prepaymentMappingsSharedCollection).toEqual(expectedCollection);
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<PrepaymentAccount>>();
      const prepaymentAccount = { id: 123 };
      jest.spyOn(prepaymentAccountService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ prepaymentAccount });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: prepaymentAccount }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      // TODO expect(prepaymentAccountService.update).toHaveBeenCalledWith(prepaymentAccount);
      // TODO expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<PrepaymentAccount>>();
      const prepaymentAccount = new PrepaymentAccount();
      jest.spyOn(prepaymentAccountService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ prepaymentAccount });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: prepaymentAccount }));
      saveSubject.complete();

      // THEN
      // TODO expect(prepaymentAccountService.create).toHaveBeenCalledWith(prepaymentAccount);
      // TODO expect(comp.isSaving).toEqual(false);
      // TODO expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<PrepaymentAccount>>();
      const prepaymentAccount = { id: 123 };
      jest.spyOn(prepaymentAccountService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ prepaymentAccount });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      // TODO expect(prepaymentAccountService.update).toHaveBeenCalledWith(prepaymentAccount);
      // TODO expect(comp.isSaving).toEqual(false);
      // TODO expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackSettlementCurrencyById', () => {
      it('Should return tracked SettlementCurrency primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackSettlementCurrencyById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackSettlementById', () => {
      it('Should return tracked Settlement primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackSettlementById(0, entity);
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

    describe('trackDealerById', () => {
      it('Should return tracked Dealer primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackDealerById(0, entity);
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


    describe('trackUniversallyUniqueMappingById', () => {
      it('Should return tracked UniversallyUniqueMapping primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackUniversallyUniqueMappingById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackPrepaymentMappingById', () => {
      it('Should return tracked PrepaymentMapping primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackPrepaymentMappingById(0, entity);
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

    describe('getSelectedUniversallyUniqueMapping', () => {
      it('Should return option if no UniversallyUniqueMapping is selected', () => {
        const option = { id: 123 };
        const result = comp.getSelectedUniversallyUniqueMapping(option);
        expect(result === option).toEqual(true);
      });

      it('Should return selected UniversallyUniqueMapping for according option', () => {
        const option = { id: 123 };
        const selected = { id: 123 };
        const selected2 = { id: 456 };
        const result = comp.getSelectedUniversallyUniqueMapping(option, [selected2, selected]);
        expect(result === selected).toEqual(true);
        expect(result === selected2).toEqual(false);
        expect(result === option).toEqual(false);
      });

      it('Should return option if this UniversallyUniqueMapping is not selected', () => {
        const option = { id: 123 };
        const selected = { id: 456 };
        const result = comp.getSelectedUniversallyUniqueMapping(option, [selected]);
        expect(result === option).toEqual(true);
        expect(result === selected).toEqual(false);
      });
    });

    describe('getSelectedPrepaymentMapping', () => {
      it('Should return option if no PrepaymentMapping is selected', () => {
        const option = { id: 123 };
        const result = comp.getSelectedPrepaymentMapping(option);
        expect(result === option).toEqual(true);
      });

      it('Should return selected PrepaymentMapping for according option', () => {
        const option = { id: 123 };
        const selected = { id: 123 };
        const selected2 = { id: 456 };
        const result = comp.getSelectedPrepaymentMapping(option, [selected2, selected]);
        expect(result === selected).toEqual(true);
        expect(result === selected2).toEqual(false);
        expect(result === option).toEqual(false);
      });

      it('Should return option if this PrepaymentMapping is not selected', () => {
        const option = { id: 123 };
        const selected = { id: 456 };
        const result = comp.getSelectedPrepaymentMapping(option, [selected]);
        expect(result === option).toEqual(true);
        expect(result === selected).toEqual(false);
      });
    });
  });
});
