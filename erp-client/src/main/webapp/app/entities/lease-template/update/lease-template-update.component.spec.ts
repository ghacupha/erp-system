jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { LeaseTemplateService } from '../service/lease-template.service';
import { ILeaseTemplate, LeaseTemplate } from '../lease-template.model';
import { ITransactionAccount } from 'app/entities/accounting/transaction-account/transaction-account.model';
import { TransactionAccountService } from 'app/entities/accounting/transaction-account/service/transaction-account.service';
import { IAssetCategory } from 'app/entities/assets/asset-category/asset-category.model';
import { AssetCategoryService } from 'app/entities/assets/asset-category/service/asset-category.service';
import { IServiceOutlet } from 'app/entities/system/service-outlet/service-outlet.model';
import { ServiceOutletService } from 'app/entities/system/service-outlet/service/service-outlet.service';
import { IDealer } from 'app/entities/people/dealer/dealer.model';
import { DealerService } from 'app/entities/people/dealer/service/dealer.service';

import { LeaseTemplateUpdateComponent } from './lease-template-update.component';

describe('LeaseTemplate Management Update Component', () => {
  let comp: LeaseTemplateUpdateComponent;
  let fixture: ComponentFixture<LeaseTemplateUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let leaseTemplateService: LeaseTemplateService;
  let transactionAccountService: TransactionAccountService;
  let assetCategoryService: AssetCategoryService;
  let serviceOutletService: ServiceOutletService;
  let dealerService: DealerService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [LeaseTemplateUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(LeaseTemplateUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(LeaseTemplateUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    leaseTemplateService = TestBed.inject(LeaseTemplateService);
    transactionAccountService = TestBed.inject(TransactionAccountService);
    assetCategoryService = TestBed.inject(AssetCategoryService);
    serviceOutletService = TestBed.inject(ServiceOutletService);
    dealerService = TestBed.inject(DealerService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call TransactionAccount query and add missing value', () => {
      const leaseTemplate: ILeaseTemplate = { id: 456 };
      const assetAccount: ITransactionAccount = { id: 99253 };
      leaseTemplate.assetAccount = assetAccount;
      const depreciationAccount: ITransactionAccount = { id: 64372 };
      leaseTemplate.depreciationAccount = depreciationAccount;
      const accruedDepreciationAccount: ITransactionAccount = { id: 59391 };
      leaseTemplate.accruedDepreciationAccount = accruedDepreciationAccount;
      const interestPaidTransferDebitAccount: ITransactionAccount = { id: 95141 };
      leaseTemplate.interestPaidTransferDebitAccount = interestPaidTransferDebitAccount;
      const interestPaidTransferCreditAccount: ITransactionAccount = { id: 19737 };
      leaseTemplate.interestPaidTransferCreditAccount = interestPaidTransferCreditAccount;
      const interestAccruedDebitAccount: ITransactionAccount = { id: 27362 };
      leaseTemplate.interestAccruedDebitAccount = interestAccruedDebitAccount;
      const interestAccruedCreditAccount: ITransactionAccount = { id: 97324 };
      leaseTemplate.interestAccruedCreditAccount = interestAccruedCreditAccount;
      const leaseRecognitionDebitAccount: ITransactionAccount = { id: 88343 };
      leaseTemplate.leaseRecognitionDebitAccount = leaseRecognitionDebitAccount;
      const leaseRecognitionCreditAccount: ITransactionAccount = { id: 33396 };
      leaseTemplate.leaseRecognitionCreditAccount = leaseRecognitionCreditAccount;
      const leaseRepaymentDebitAccount: ITransactionAccount = { id: 1151 };
      leaseTemplate.leaseRepaymentDebitAccount = leaseRepaymentDebitAccount;
      const leaseRepaymentCreditAccount: ITransactionAccount = { id: 64231 };
      leaseTemplate.leaseRepaymentCreditAccount = leaseRepaymentCreditAccount;
      const rouRecognitionCreditAccount: ITransactionAccount = { id: 12484 };
      leaseTemplate.rouRecognitionCreditAccount = rouRecognitionCreditAccount;
      const rouRecognitionDebitAccount: ITransactionAccount = { id: 82433 };
      leaseTemplate.rouRecognitionDebitAccount = rouRecognitionDebitAccount;

      const transactionAccountCollection: ITransactionAccount[] = [{ id: 69447 }];
      jest.spyOn(transactionAccountService, 'query').mockReturnValue(of(new HttpResponse({ body: transactionAccountCollection })));
      const additionalTransactionAccounts = [
        assetAccount,
        depreciationAccount,
        accruedDepreciationAccount,
        interestPaidTransferDebitAccount,
        interestPaidTransferCreditAccount,
        interestAccruedDebitAccount,
        interestAccruedCreditAccount,
        leaseRecognitionDebitAccount,
        leaseRecognitionCreditAccount,
        leaseRepaymentDebitAccount,
        leaseRepaymentCreditAccount,
        rouRecognitionCreditAccount,
        rouRecognitionDebitAccount,
      ];
      const expectedCollection: ITransactionAccount[] = [...additionalTransactionAccounts, ...transactionAccountCollection];
      jest.spyOn(transactionAccountService, 'addTransactionAccountToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ leaseTemplate });
      comp.ngOnInit();

      expect(transactionAccountService.query).toHaveBeenCalled();
      expect(transactionAccountService.addTransactionAccountToCollectionIfMissing).toHaveBeenCalledWith(
        transactionAccountCollection,
        ...additionalTransactionAccounts
      );
      expect(comp.transactionAccountsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call AssetCategory query and add missing value', () => {
      const leaseTemplate: ILeaseTemplate = { id: 456 };
      const assetCategory: IAssetCategory = { id: 26842 };
      leaseTemplate.assetCategory = assetCategory;

      const assetCategoryCollection: IAssetCategory[] = [{ id: 75906 }];
      jest.spyOn(assetCategoryService, 'query').mockReturnValue(of(new HttpResponse({ body: assetCategoryCollection })));
      const additionalAssetCategories = [assetCategory];
      const expectedCollection: IAssetCategory[] = [...additionalAssetCategories, ...assetCategoryCollection];
      jest.spyOn(assetCategoryService, 'addAssetCategoryToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ leaseTemplate });
      comp.ngOnInit();

      expect(assetCategoryService.query).toHaveBeenCalled();
      expect(assetCategoryService.addAssetCategoryToCollectionIfMissing).toHaveBeenCalledWith(
        assetCategoryCollection,
        ...additionalAssetCategories
      );
      expect(comp.assetCategoriesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call ServiceOutlet query and add missing value', () => {
      const leaseTemplate: ILeaseTemplate = { id: 456 };
      const serviceOutlet: IServiceOutlet = { id: 65910 };
      leaseTemplate.serviceOutlet = serviceOutlet;

      const serviceOutletCollection: IServiceOutlet[] = [{ id: 85038 }];
      jest.spyOn(serviceOutletService, 'query').mockReturnValue(of(new HttpResponse({ body: serviceOutletCollection })));
      const additionalServiceOutlets = [serviceOutlet];
      const expectedCollection: IServiceOutlet[] = [...additionalServiceOutlets, ...serviceOutletCollection];
      jest.spyOn(serviceOutletService, 'addServiceOutletToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ leaseTemplate });
      comp.ngOnInit();

      expect(serviceOutletService.query).toHaveBeenCalled();
      expect(serviceOutletService.addServiceOutletToCollectionIfMissing).toHaveBeenCalledWith(
        serviceOutletCollection,
        ...additionalServiceOutlets
      );
      expect(comp.serviceOutletsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Dealer query and add missing value', () => {
      const leaseTemplate: ILeaseTemplate = { id: 456 };
      const mainDealer: IDealer = { id: 51002 };
      leaseTemplate.mainDealer = mainDealer;

      const dealerCollection: IDealer[] = [{ id: 54495 }];
      jest.spyOn(dealerService, 'query').mockReturnValue(of(new HttpResponse({ body: dealerCollection })));
      const additionalDealers = [mainDealer];
      const expectedCollection: IDealer[] = [...additionalDealers, ...dealerCollection];
      jest.spyOn(dealerService, 'addDealerToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ leaseTemplate });
      comp.ngOnInit();

      expect(dealerService.query).toHaveBeenCalled();
      expect(dealerService.addDealerToCollectionIfMissing).toHaveBeenCalledWith(dealerCollection, ...additionalDealers);
      expect(comp.dealersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const leaseTemplate: ILeaseTemplate = { id: 456 };
      const assetAccount: ITransactionAccount = { id: 94209 };
      leaseTemplate.assetAccount = assetAccount;
      const depreciationAccount: ITransactionAccount = { id: 5952 };
      leaseTemplate.depreciationAccount = depreciationAccount;
      const accruedDepreciationAccount: ITransactionAccount = { id: 9172 };
      leaseTemplate.accruedDepreciationAccount = accruedDepreciationAccount;
      const interestPaidTransferDebitAccount: ITransactionAccount = { id: 78026 };
      leaseTemplate.interestPaidTransferDebitAccount = interestPaidTransferDebitAccount;
      const interestPaidTransferCreditAccount: ITransactionAccount = { id: 80058 };
      leaseTemplate.interestPaidTransferCreditAccount = interestPaidTransferCreditAccount;
      const interestAccruedDebitAccount: ITransactionAccount = { id: 92054 };
      leaseTemplate.interestAccruedDebitAccount = interestAccruedDebitAccount;
      const interestAccruedCreditAccount: ITransactionAccount = { id: 44048 };
      leaseTemplate.interestAccruedCreditAccount = interestAccruedCreditAccount;
      const leaseRecognitionDebitAccount: ITransactionAccount = { id: 35907 };
      leaseTemplate.leaseRecognitionDebitAccount = leaseRecognitionDebitAccount;
      const leaseRecognitionCreditAccount: ITransactionAccount = { id: 68485 };
      leaseTemplate.leaseRecognitionCreditAccount = leaseRecognitionCreditAccount;
      const leaseRepaymentDebitAccount: ITransactionAccount = { id: 87850 };
      leaseTemplate.leaseRepaymentDebitAccount = leaseRepaymentDebitAccount;
      const leaseRepaymentCreditAccount: ITransactionAccount = { id: 29978 };
      leaseTemplate.leaseRepaymentCreditAccount = leaseRepaymentCreditAccount;
      const rouRecognitionCreditAccount: ITransactionAccount = { id: 63262 };
      leaseTemplate.rouRecognitionCreditAccount = rouRecognitionCreditAccount;
      const rouRecognitionDebitAccount: ITransactionAccount = { id: 76890 };
      leaseTemplate.rouRecognitionDebitAccount = rouRecognitionDebitAccount;
      const assetCategory: IAssetCategory = { id: 1076 };
      leaseTemplate.assetCategory = assetCategory;
      const serviceOutlet: IServiceOutlet = { id: 89530 };
      leaseTemplate.serviceOutlet = serviceOutlet;
      const mainDealer: IDealer = { id: 22200 };
      leaseTemplate.mainDealer = mainDealer;

      activatedRoute.data = of({ leaseTemplate });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(leaseTemplate));
      expect(comp.transactionAccountsSharedCollection).toContain(assetAccount);
      expect(comp.transactionAccountsSharedCollection).toContain(depreciationAccount);
      expect(comp.transactionAccountsSharedCollection).toContain(accruedDepreciationAccount);
      expect(comp.transactionAccountsSharedCollection).toContain(interestPaidTransferDebitAccount);
      expect(comp.transactionAccountsSharedCollection).toContain(interestPaidTransferCreditAccount);
      expect(comp.transactionAccountsSharedCollection).toContain(interestAccruedDebitAccount);
      expect(comp.transactionAccountsSharedCollection).toContain(interestAccruedCreditAccount);
      expect(comp.transactionAccountsSharedCollection).toContain(leaseRecognitionDebitAccount);
      expect(comp.transactionAccountsSharedCollection).toContain(leaseRecognitionCreditAccount);
      expect(comp.transactionAccountsSharedCollection).toContain(leaseRepaymentDebitAccount);
      expect(comp.transactionAccountsSharedCollection).toContain(leaseRepaymentCreditAccount);
      expect(comp.transactionAccountsSharedCollection).toContain(rouRecognitionCreditAccount);
      expect(comp.transactionAccountsSharedCollection).toContain(rouRecognitionDebitAccount);
      expect(comp.assetCategoriesSharedCollection).toContain(assetCategory);
      expect(comp.serviceOutletsSharedCollection).toContain(serviceOutlet);
      expect(comp.dealersSharedCollection).toContain(mainDealer);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<LeaseTemplate>>();
      const leaseTemplate = { id: 123 };
      jest.spyOn(leaseTemplateService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ leaseTemplate });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: leaseTemplate }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(leaseTemplateService.update).toHaveBeenCalledWith(leaseTemplate);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<LeaseTemplate>>();
      const leaseTemplate = new LeaseTemplate();
      jest.spyOn(leaseTemplateService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ leaseTemplate });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: leaseTemplate }));
      saveSubject.complete();

      // THEN
      expect(leaseTemplateService.create).toHaveBeenCalledWith(leaseTemplate);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<LeaseTemplate>>();
      const leaseTemplate = { id: 123 };
      jest.spyOn(leaseTemplateService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ leaseTemplate });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(leaseTemplateService.update).toHaveBeenCalledWith(leaseTemplate);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackTransactionAccountById', () => {
      it('Should return tracked TransactionAccount primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackTransactionAccountById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackAssetCategoryById', () => {
      it('Should return tracked AssetCategory primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackAssetCategoryById(0, entity);
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
  });
});
