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

import { LeaseModelMetadataService } from '../service/lease-model-metadata.service';
import { ILeaseModelMetadata, LeaseModelMetadata } from '../lease-model-metadata.model';
import { IPlaceholder } from 'app/entities/system/placeholder/placeholder.model';
import { PlaceholderService } from 'app/entities/system/placeholder/service/placeholder.service';
import { IUniversallyUniqueMapping } from 'app/entities/system/universally-unique-mapping/universally-unique-mapping.model';
import { UniversallyUniqueMappingService } from 'app/entities/system/universally-unique-mapping/service/universally-unique-mapping.service';
import { ILeaseContract } from 'app/entities/leases/lease-contract/lease-contract.model';
import { LeaseContractService } from 'app/entities/leases/lease-contract/service/lease-contract.service';
import { ISettlementCurrency } from 'app/entities/system/settlement-currency/settlement-currency.model';
import { SettlementCurrencyService } from 'app/entities/system/settlement-currency/service/settlement-currency.service';
import { IBusinessDocument } from 'app/entities/documentation/business-document/business-document.model';
import { BusinessDocumentService } from 'app/entities/documentation/business-document/service/business-document.service';
import { ISecurityClearance } from 'app/entities/people/security-clearance/security-clearance.model';
import { SecurityClearanceService } from 'app/entities/people/security-clearance/service/security-clearance.service';
import { ITransactionAccount } from 'app/entities/accounting/transaction-account/transaction-account.model';
import { TransactionAccountService } from 'app/entities/accounting/transaction-account/service/transaction-account.service';

import { LeaseModelMetadataUpdateComponent } from './lease-model-metadata-update.component';

describe('LeaseModelMetadata Management Update Component', () => {
  let comp: LeaseModelMetadataUpdateComponent;
  let fixture: ComponentFixture<LeaseModelMetadataUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let leaseModelMetadataService: LeaseModelMetadataService;
  let placeholderService: PlaceholderService;
  let universallyUniqueMappingService: UniversallyUniqueMappingService;
  let leaseContractService: LeaseContractService;
  let settlementCurrencyService: SettlementCurrencyService;
  let businessDocumentService: BusinessDocumentService;
  let securityClearanceService: SecurityClearanceService;
  let transactionAccountService: TransactionAccountService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [LeaseModelMetadataUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(LeaseModelMetadataUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(LeaseModelMetadataUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    leaseModelMetadataService = TestBed.inject(LeaseModelMetadataService);
    placeholderService = TestBed.inject(PlaceholderService);
    universallyUniqueMappingService = TestBed.inject(UniversallyUniqueMappingService);
    leaseContractService = TestBed.inject(LeaseContractService);
    settlementCurrencyService = TestBed.inject(SettlementCurrencyService);
    businessDocumentService = TestBed.inject(BusinessDocumentService);
    securityClearanceService = TestBed.inject(SecurityClearanceService);
    transactionAccountService = TestBed.inject(TransactionAccountService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Placeholder query and add missing value', () => {
      const leaseModelMetadata: ILeaseModelMetadata = { id: 456 };
      const placeholders: IPlaceholder[] = [{ id: 7718 }];
      leaseModelMetadata.placeholders = placeholders;

      const placeholderCollection: IPlaceholder[] = [{ id: 48170 }];
      jest.spyOn(placeholderService, 'query').mockReturnValue(of(new HttpResponse({ body: placeholderCollection })));
      const additionalPlaceholders = [...placeholders];
      const expectedCollection: IPlaceholder[] = [...additionalPlaceholders, ...placeholderCollection];
      jest.spyOn(placeholderService, 'addPlaceholderToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ leaseModelMetadata });
      comp.ngOnInit();

      expect(placeholderService.query).toHaveBeenCalled();
      expect(placeholderService.addPlaceholderToCollectionIfMissing).toHaveBeenCalledWith(placeholderCollection, ...additionalPlaceholders);
      expect(comp.placeholdersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call UniversallyUniqueMapping query and add missing value', () => {
      const leaseModelMetadata: ILeaseModelMetadata = { id: 456 };
      const leaseMappings: IUniversallyUniqueMapping[] = [{ id: 94935 }];
      leaseModelMetadata.leaseMappings = leaseMappings;

      const universallyUniqueMappingCollection: IUniversallyUniqueMapping[] = [{ id: 27733 }];
      jest
        .spyOn(universallyUniqueMappingService, 'query')
        .mockReturnValue(of(new HttpResponse({ body: universallyUniqueMappingCollection })));
      const additionalUniversallyUniqueMappings = [...leaseMappings];
      const expectedCollection: IUniversallyUniqueMapping[] = [
        ...additionalUniversallyUniqueMappings,
        ...universallyUniqueMappingCollection,
      ];
      jest.spyOn(universallyUniqueMappingService, 'addUniversallyUniqueMappingToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ leaseModelMetadata });
      comp.ngOnInit();

      expect(universallyUniqueMappingService.query).toHaveBeenCalled();
      expect(universallyUniqueMappingService.addUniversallyUniqueMappingToCollectionIfMissing).toHaveBeenCalledWith(
        universallyUniqueMappingCollection,
        ...additionalUniversallyUniqueMappings
      );
      expect(comp.universallyUniqueMappingsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call LeaseContract query and add missing value', () => {
      const leaseModelMetadata: ILeaseModelMetadata = { id: 456 };
      const leaseContract: ILeaseContract = { id: 95223 };
      leaseModelMetadata.leaseContract = leaseContract;

      const leaseContractCollection: ILeaseContract[] = [{ id: 37422 }];
      jest.spyOn(leaseContractService, 'query').mockReturnValue(of(new HttpResponse({ body: leaseContractCollection })));
      const additionalLeaseContracts = [leaseContract];
      const expectedCollection: ILeaseContract[] = [...additionalLeaseContracts, ...leaseContractCollection];
      jest.spyOn(leaseContractService, 'addLeaseContractToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ leaseModelMetadata });
      comp.ngOnInit();

      expect(leaseContractService.query).toHaveBeenCalled();
      expect(leaseContractService.addLeaseContractToCollectionIfMissing).toHaveBeenCalledWith(
        leaseContractCollection,
        ...additionalLeaseContracts
      );
      expect(comp.leaseContractsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call predecessor query and add missing value', () => {
      const leaseModelMetadata: ILeaseModelMetadata = { id: 456 };
      const predecessor: ILeaseModelMetadata = { id: 8236 };
      leaseModelMetadata.predecessor = predecessor;

      const predecessorCollection: ILeaseModelMetadata[] = [{ id: 96347 }];
      jest.spyOn(leaseModelMetadataService, 'query').mockReturnValue(of(new HttpResponse({ body: predecessorCollection })));
      const expectedCollection: ILeaseModelMetadata[] = [predecessor, ...predecessorCollection];
      jest.spyOn(leaseModelMetadataService, 'addLeaseModelMetadataToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ leaseModelMetadata });
      comp.ngOnInit();

      expect(leaseModelMetadataService.query).toHaveBeenCalled();
      expect(leaseModelMetadataService.addLeaseModelMetadataToCollectionIfMissing).toHaveBeenCalledWith(predecessorCollection, predecessor);
      expect(comp.predecessorsCollection).toEqual(expectedCollection);
    });

    it('Should call SettlementCurrency query and add missing value', () => {
      const leaseModelMetadata: ILeaseModelMetadata = { id: 456 };
      const liabilityCurrency: ISettlementCurrency = { id: 81770 };
      leaseModelMetadata.liabilityCurrency = liabilityCurrency;
      const rouAssetCurrency: ISettlementCurrency = { id: 60581 };
      leaseModelMetadata.rouAssetCurrency = rouAssetCurrency;

      const settlementCurrencyCollection: ISettlementCurrency[] = [{ id: 38837 }];
      jest.spyOn(settlementCurrencyService, 'query').mockReturnValue(of(new HttpResponse({ body: settlementCurrencyCollection })));
      const additionalSettlementCurrencies = [liabilityCurrency, rouAssetCurrency];
      const expectedCollection: ISettlementCurrency[] = [...additionalSettlementCurrencies, ...settlementCurrencyCollection];
      jest.spyOn(settlementCurrencyService, 'addSettlementCurrencyToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ leaseModelMetadata });
      comp.ngOnInit();

      expect(settlementCurrencyService.query).toHaveBeenCalled();
      expect(settlementCurrencyService.addSettlementCurrencyToCollectionIfMissing).toHaveBeenCalledWith(
        settlementCurrencyCollection,
        ...additionalSettlementCurrencies
      );
      expect(comp.settlementCurrenciesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call BusinessDocument query and add missing value', () => {
      const leaseModelMetadata: ILeaseModelMetadata = { id: 456 };
      const modelAttachments: IBusinessDocument = { id: 99346 };
      leaseModelMetadata.modelAttachments = modelAttachments;

      const businessDocumentCollection: IBusinessDocument[] = [{ id: 79350 }];
      jest.spyOn(businessDocumentService, 'query').mockReturnValue(of(new HttpResponse({ body: businessDocumentCollection })));
      const additionalBusinessDocuments = [modelAttachments];
      const expectedCollection: IBusinessDocument[] = [...additionalBusinessDocuments, ...businessDocumentCollection];
      jest.spyOn(businessDocumentService, 'addBusinessDocumentToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ leaseModelMetadata });
      comp.ngOnInit();

      expect(businessDocumentService.query).toHaveBeenCalled();
      expect(businessDocumentService.addBusinessDocumentToCollectionIfMissing).toHaveBeenCalledWith(
        businessDocumentCollection,
        ...additionalBusinessDocuments
      );
      expect(comp.businessDocumentsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call SecurityClearance query and add missing value', () => {
      const leaseModelMetadata: ILeaseModelMetadata = { id: 456 };
      const securityClearance: ISecurityClearance = { id: 6844 };
      leaseModelMetadata.securityClearance = securityClearance;

      const securityClearanceCollection: ISecurityClearance[] = [{ id: 54162 }];
      jest.spyOn(securityClearanceService, 'query').mockReturnValue(of(new HttpResponse({ body: securityClearanceCollection })));
      const additionalSecurityClearances = [securityClearance];
      const expectedCollection: ISecurityClearance[] = [...additionalSecurityClearances, ...securityClearanceCollection];
      jest.spyOn(securityClearanceService, 'addSecurityClearanceToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ leaseModelMetadata });
      comp.ngOnInit();

      expect(securityClearanceService.query).toHaveBeenCalled();
      expect(securityClearanceService.addSecurityClearanceToCollectionIfMissing).toHaveBeenCalledWith(
        securityClearanceCollection,
        ...additionalSecurityClearances
      );
      expect(comp.securityClearancesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call TransactionAccount query and add missing value', () => {
      const leaseModelMetadata: ILeaseModelMetadata = { id: 456 };
      const leaseLiabilityAccount: ITransactionAccount = { id: 86266 };
      leaseModelMetadata.leaseLiabilityAccount = leaseLiabilityAccount;
      const interestPayableAccount: ITransactionAccount = { id: 10419 };
      leaseModelMetadata.interestPayableAccount = interestPayableAccount;
      const interestExpenseAccount: ITransactionAccount = { id: 59442 };
      leaseModelMetadata.interestExpenseAccount = interestExpenseAccount;
      const rouAssetAccount: ITransactionAccount = { id: 36757 };
      leaseModelMetadata.rouAssetAccount = rouAssetAccount;
      const rouDepreciationAccount: ITransactionAccount = { id: 32893 };
      leaseModelMetadata.rouDepreciationAccount = rouDepreciationAccount;
      const accruedDepreciationAccount: ITransactionAccount = { id: 91478 };
      leaseModelMetadata.accruedDepreciationAccount = accruedDepreciationAccount;

      const transactionAccountCollection: ITransactionAccount[] = [{ id: 38893 }];
      jest.spyOn(transactionAccountService, 'query').mockReturnValue(of(new HttpResponse({ body: transactionAccountCollection })));
      const additionalTransactionAccounts = [
        leaseLiabilityAccount,
        interestPayableAccount,
        interestExpenseAccount,
        rouAssetAccount,
        rouDepreciationAccount,
        accruedDepreciationAccount,
      ];
      const expectedCollection: ITransactionAccount[] = [...additionalTransactionAccounts, ...transactionAccountCollection];
      jest.spyOn(transactionAccountService, 'addTransactionAccountToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ leaseModelMetadata });
      comp.ngOnInit();

      expect(transactionAccountService.query).toHaveBeenCalled();
      expect(transactionAccountService.addTransactionAccountToCollectionIfMissing).toHaveBeenCalledWith(
        transactionAccountCollection,
        ...additionalTransactionAccounts
      );
      expect(comp.transactionAccountsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const leaseModelMetadata: ILeaseModelMetadata = { id: 456 };
      const placeholders: IPlaceholder = { id: 62706 };
      leaseModelMetadata.placeholders = [placeholders];
      const leaseMappings: IUniversallyUniqueMapping = { id: 52084 };
      leaseModelMetadata.leaseMappings = [leaseMappings];
      const leaseContract: ILeaseContract = { id: 44132 };
      leaseModelMetadata.leaseContract = leaseContract;
      const predecessor: ILeaseModelMetadata = { id: 75245 };
      leaseModelMetadata.predecessor = predecessor;
      const liabilityCurrency: ISettlementCurrency = { id: 53609 };
      leaseModelMetadata.liabilityCurrency = liabilityCurrency;
      const rouAssetCurrency: ISettlementCurrency = { id: 34519 };
      leaseModelMetadata.rouAssetCurrency = rouAssetCurrency;
      const modelAttachments: IBusinessDocument = { id: 7360 };
      leaseModelMetadata.modelAttachments = modelAttachments;
      const securityClearance: ISecurityClearance = { id: 95339 };
      leaseModelMetadata.securityClearance = securityClearance;
      const leaseLiabilityAccount: ITransactionAccount = { id: 38026 };
      leaseModelMetadata.leaseLiabilityAccount = leaseLiabilityAccount;
      const interestPayableAccount: ITransactionAccount = { id: 67813 };
      leaseModelMetadata.interestPayableAccount = interestPayableAccount;
      const interestExpenseAccount: ITransactionAccount = { id: 35990 };
      leaseModelMetadata.interestExpenseAccount = interestExpenseAccount;
      const rouAssetAccount: ITransactionAccount = { id: 54688 };
      leaseModelMetadata.rouAssetAccount = rouAssetAccount;
      const rouDepreciationAccount: ITransactionAccount = { id: 19365 };
      leaseModelMetadata.rouDepreciationAccount = rouDepreciationAccount;
      const accruedDepreciationAccount: ITransactionAccount = { id: 21954 };
      leaseModelMetadata.accruedDepreciationAccount = accruedDepreciationAccount;

      activatedRoute.data = of({ leaseModelMetadata });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(leaseModelMetadata));
      expect(comp.placeholdersSharedCollection).toContain(placeholders);
      expect(comp.universallyUniqueMappingsSharedCollection).toContain(leaseMappings);
      expect(comp.leaseContractsSharedCollection).toContain(leaseContract);
      expect(comp.predecessorsCollection).toContain(predecessor);
      expect(comp.settlementCurrenciesSharedCollection).toContain(liabilityCurrency);
      expect(comp.settlementCurrenciesSharedCollection).toContain(rouAssetCurrency);
      expect(comp.businessDocumentsSharedCollection).toContain(modelAttachments);
      expect(comp.securityClearancesSharedCollection).toContain(securityClearance);
      expect(comp.transactionAccountsSharedCollection).toContain(leaseLiabilityAccount);
      expect(comp.transactionAccountsSharedCollection).toContain(interestPayableAccount);
      expect(comp.transactionAccountsSharedCollection).toContain(interestExpenseAccount);
      expect(comp.transactionAccountsSharedCollection).toContain(rouAssetAccount);
      expect(comp.transactionAccountsSharedCollection).toContain(rouDepreciationAccount);
      expect(comp.transactionAccountsSharedCollection).toContain(accruedDepreciationAccount);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<LeaseModelMetadata>>();
      const leaseModelMetadata = { id: 123 };
      jest.spyOn(leaseModelMetadataService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ leaseModelMetadata });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: leaseModelMetadata }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(leaseModelMetadataService.update).toHaveBeenCalledWith(leaseModelMetadata);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<LeaseModelMetadata>>();
      const leaseModelMetadata = new LeaseModelMetadata();
      jest.spyOn(leaseModelMetadataService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ leaseModelMetadata });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: leaseModelMetadata }));
      saveSubject.complete();

      // THEN
      expect(leaseModelMetadataService.create).toHaveBeenCalledWith(leaseModelMetadata);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<LeaseModelMetadata>>();
      const leaseModelMetadata = { id: 123 };
      jest.spyOn(leaseModelMetadataService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ leaseModelMetadata });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(leaseModelMetadataService.update).toHaveBeenCalledWith(leaseModelMetadata);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
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

    describe('trackLeaseContractById', () => {
      it('Should return tracked LeaseContract primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackLeaseContractById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackLeaseModelMetadataById', () => {
      it('Should return tracked LeaseModelMetadata primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackLeaseModelMetadataById(0, entity);
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

    describe('trackBusinessDocumentById', () => {
      it('Should return tracked BusinessDocument primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackBusinessDocumentById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackSecurityClearanceById', () => {
      it('Should return tracked SecurityClearance primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackSecurityClearanceById(0, entity);
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
  });
});
