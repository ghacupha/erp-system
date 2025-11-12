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

import { RouDepreciationEntryService } from '../service/rou-depreciation-entry.service';
import { IRouDepreciationEntry, RouDepreciationEntry } from '../rou-depreciation-entry.model';
import { ITransactionAccount } from 'app/entities/accounting/transaction-account/transaction-account.model';
import { TransactionAccountService } from 'app/entities/accounting/transaction-account/service/transaction-account.service';
import { IAssetCategory } from 'app/entities/assets/asset-category/asset-category.model';
import { AssetCategoryService } from 'app/entities/assets/asset-category/service/asset-category.service';
import { IIFRS16LeaseContract } from 'app/entities/leases/ifrs-16-lease-contract/ifrs-16-lease-contract.model';
import { IFRS16LeaseContractService } from 'app/entities/leases/ifrs-16-lease-contract/service/ifrs-16-lease-contract.service';
import { IRouModelMetadata } from 'app/entities/leases/rou-model-metadata/rou-model-metadata.model';
import { RouModelMetadataService } from 'app/entities/leases/rou-model-metadata/service/rou-model-metadata.service';

import { RouDepreciationEntryUpdateComponent } from './rou-depreciation-entry-update.component';

describe('RouDepreciationEntry Management Update Component', () => {
  let comp: RouDepreciationEntryUpdateComponent;
  let fixture: ComponentFixture<RouDepreciationEntryUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let rouDepreciationEntryService: RouDepreciationEntryService;
  let transactionAccountService: TransactionAccountService;
  let assetCategoryService: AssetCategoryService;
  let iFRS16LeaseContractService: IFRS16LeaseContractService;
  let rouModelMetadataService: RouModelMetadataService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [RouDepreciationEntryUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(RouDepreciationEntryUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(RouDepreciationEntryUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    rouDepreciationEntryService = TestBed.inject(RouDepreciationEntryService);
    transactionAccountService = TestBed.inject(TransactionAccountService);
    assetCategoryService = TestBed.inject(AssetCategoryService);
    iFRS16LeaseContractService = TestBed.inject(IFRS16LeaseContractService);
    rouModelMetadataService = TestBed.inject(RouModelMetadataService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call TransactionAccount query and add missing value', () => {
      const rouDepreciationEntry: IRouDepreciationEntry = { id: 456 };
      const debitAccount: ITransactionAccount = { id: 64744 };
      rouDepreciationEntry.debitAccount = debitAccount;
      const creditAccount: ITransactionAccount = { id: 30997 };
      rouDepreciationEntry.creditAccount = creditAccount;

      const transactionAccountCollection: ITransactionAccount[] = [{ id: 7335 }];
      jest.spyOn(transactionAccountService, 'query').mockReturnValue(of(new HttpResponse({ body: transactionAccountCollection })));
      const additionalTransactionAccounts = [debitAccount, creditAccount];
      const expectedCollection: ITransactionAccount[] = [...additionalTransactionAccounts, ...transactionAccountCollection];
      jest.spyOn(transactionAccountService, 'addTransactionAccountToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ rouDepreciationEntry });
      comp.ngOnInit();

      expect(transactionAccountService.query).toHaveBeenCalled();
      expect(transactionAccountService.addTransactionAccountToCollectionIfMissing).toHaveBeenCalledWith(
        transactionAccountCollection,
        ...additionalTransactionAccounts
      );
      expect(comp.transactionAccountsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call AssetCategory query and add missing value', () => {
      const rouDepreciationEntry: IRouDepreciationEntry = { id: 456 };
      const assetCategory: IAssetCategory = { id: 97816 };
      rouDepreciationEntry.assetCategory = assetCategory;

      const assetCategoryCollection: IAssetCategory[] = [{ id: 21206 }];
      jest.spyOn(assetCategoryService, 'query').mockReturnValue(of(new HttpResponse({ body: assetCategoryCollection })));
      const additionalAssetCategories = [assetCategory];
      const expectedCollection: IAssetCategory[] = [...additionalAssetCategories, ...assetCategoryCollection];
      jest.spyOn(assetCategoryService, 'addAssetCategoryToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ rouDepreciationEntry });
      comp.ngOnInit();

      expect(assetCategoryService.query).toHaveBeenCalled();
      expect(assetCategoryService.addAssetCategoryToCollectionIfMissing).toHaveBeenCalledWith(
        assetCategoryCollection,
        ...additionalAssetCategories
      );
      expect(comp.assetCategoriesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call IFRS16LeaseContract query and add missing value', () => {
      const rouDepreciationEntry: IRouDepreciationEntry = { id: 456 };
      const leaseContract: IIFRS16LeaseContract = { id: 53413 };
      rouDepreciationEntry.leaseContract = leaseContract;

      const iFRS16LeaseContractCollection: IIFRS16LeaseContract[] = [{ id: 48693 }];
      jest.spyOn(iFRS16LeaseContractService, 'query').mockReturnValue(of(new HttpResponse({ body: iFRS16LeaseContractCollection })));
      const additionalIFRS16LeaseContracts = [leaseContract];
      const expectedCollection: IIFRS16LeaseContract[] = [...additionalIFRS16LeaseContracts, ...iFRS16LeaseContractCollection];
      jest.spyOn(iFRS16LeaseContractService, 'addIFRS16LeaseContractToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ rouDepreciationEntry });
      comp.ngOnInit();

      expect(iFRS16LeaseContractService.query).toHaveBeenCalled();
      expect(iFRS16LeaseContractService.addIFRS16LeaseContractToCollectionIfMissing).toHaveBeenCalledWith(
        iFRS16LeaseContractCollection,
        ...additionalIFRS16LeaseContracts
      );
      expect(comp.iFRS16LeaseContractsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call RouModelMetadata query and add missing value', () => {
      const rouDepreciationEntry: IRouDepreciationEntry = { id: 456 };
      const rouMetadata: IRouModelMetadata = { id: 73332 };
      rouDepreciationEntry.rouMetadata = rouMetadata;

      const rouModelMetadataCollection: IRouModelMetadata[] = [{ id: 97480 }];
      jest.spyOn(rouModelMetadataService, 'query').mockReturnValue(of(new HttpResponse({ body: rouModelMetadataCollection })));
      const additionalRouModelMetadata = [rouMetadata];
      const expectedCollection: IRouModelMetadata[] = [...additionalRouModelMetadata, ...rouModelMetadataCollection];
      jest.spyOn(rouModelMetadataService, 'addRouModelMetadataToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ rouDepreciationEntry });
      comp.ngOnInit();

      expect(rouModelMetadataService.query).toHaveBeenCalled();
      expect(rouModelMetadataService.addRouModelMetadataToCollectionIfMissing).toHaveBeenCalledWith(
        rouModelMetadataCollection,
        ...additionalRouModelMetadata
      );
      expect(comp.rouModelMetadataSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const rouDepreciationEntry: IRouDepreciationEntry = { id: 456 };
      const debitAccount: ITransactionAccount = { id: 17324 };
      rouDepreciationEntry.debitAccount = debitAccount;
      const creditAccount: ITransactionAccount = { id: 62947 };
      rouDepreciationEntry.creditAccount = creditAccount;
      const assetCategory: IAssetCategory = { id: 65536 };
      rouDepreciationEntry.assetCategory = assetCategory;
      const leaseContract: IIFRS16LeaseContract = { id: 77282 };
      rouDepreciationEntry.leaseContract = leaseContract;
      const rouMetadata: IRouModelMetadata = { id: 61652 };
      rouDepreciationEntry.rouMetadata = rouMetadata;

      activatedRoute.data = of({ rouDepreciationEntry });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(rouDepreciationEntry));
      expect(comp.transactionAccountsSharedCollection).toContain(debitAccount);
      expect(comp.transactionAccountsSharedCollection).toContain(creditAccount);
      expect(comp.assetCategoriesSharedCollection).toContain(assetCategory);
      expect(comp.iFRS16LeaseContractsSharedCollection).toContain(leaseContract);
      expect(comp.rouModelMetadataSharedCollection).toContain(rouMetadata);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<RouDepreciationEntry>>();
      const rouDepreciationEntry = { id: 123 };
      jest.spyOn(rouDepreciationEntryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ rouDepreciationEntry });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: rouDepreciationEntry }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(rouDepreciationEntryService.update).toHaveBeenCalledWith(rouDepreciationEntry);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<RouDepreciationEntry>>();
      const rouDepreciationEntry = new RouDepreciationEntry();
      jest.spyOn(rouDepreciationEntryService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ rouDepreciationEntry });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: rouDepreciationEntry }));
      saveSubject.complete();

      // THEN
      expect(rouDepreciationEntryService.create).toHaveBeenCalledWith(rouDepreciationEntry);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<RouDepreciationEntry>>();
      const rouDepreciationEntry = { id: 123 };
      jest.spyOn(rouDepreciationEntryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ rouDepreciationEntry });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(rouDepreciationEntryService.update).toHaveBeenCalledWith(rouDepreciationEntry);
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

    describe('trackIFRS16LeaseContractById', () => {
      it('Should return tracked IFRS16LeaseContract primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackIFRS16LeaseContractById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackRouModelMetadataById', () => {
      it('Should return tracked RouModelMetadata primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackRouModelMetadataById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
