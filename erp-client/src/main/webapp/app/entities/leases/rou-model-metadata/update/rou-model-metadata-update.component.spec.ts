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

import { RouModelMetadataService } from '../service/rou-model-metadata.service';
import { IRouModelMetadata, RouModelMetadata } from '../rou-model-metadata.model';
import { IIFRS16LeaseContract } from 'app/entities/leases/ifrs-16-lease-contract/ifrs-16-lease-contract.model';
import { IFRS16LeaseContractService } from 'app/entities/leases/ifrs-16-lease-contract/service/ifrs-16-lease-contract.service';
import { ITransactionAccount } from 'app/entities/accounting/transaction-account/transaction-account.model';
import { TransactionAccountService } from 'app/entities/accounting/transaction-account/service/transaction-account.service';
import { IAssetCategory } from 'app/entities/assets/asset-category/asset-category.model';
import { AssetCategoryService } from 'app/entities/assets/asset-category/service/asset-category.service';
import { IBusinessDocument } from 'app/entities/documentation/business-document/business-document.model';
import { BusinessDocumentService } from 'app/entities/documentation/business-document/service/business-document.service';

import { RouModelMetadataUpdateComponent } from './rou-model-metadata-update.component';

describe('RouModelMetadata Management Update Component', () => {
  let comp: RouModelMetadataUpdateComponent;
  let fixture: ComponentFixture<RouModelMetadataUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let rouModelMetadataService: RouModelMetadataService;
  let iFRS16LeaseContractService: IFRS16LeaseContractService;
  let transactionAccountService: TransactionAccountService;
  let assetCategoryService: AssetCategoryService;
  let businessDocumentService: BusinessDocumentService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [RouModelMetadataUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(RouModelMetadataUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(RouModelMetadataUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    rouModelMetadataService = TestBed.inject(RouModelMetadataService);
    iFRS16LeaseContractService = TestBed.inject(IFRS16LeaseContractService);
    transactionAccountService = TestBed.inject(TransactionAccountService);
    assetCategoryService = TestBed.inject(AssetCategoryService);
    businessDocumentService = TestBed.inject(BusinessDocumentService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call IFRS16LeaseContract query and add missing value', () => {
      const rouModelMetadata: IRouModelMetadata = { id: 456 };
      const ifrs16LeaseContract: IIFRS16LeaseContract = { id: 57758 };
      rouModelMetadata.ifrs16LeaseContract = ifrs16LeaseContract;

      const iFRS16LeaseContractCollection: IIFRS16LeaseContract[] = [{ id: 26577 }];
      jest.spyOn(iFRS16LeaseContractService, 'query').mockReturnValue(of(new HttpResponse({ body: iFRS16LeaseContractCollection })));
      const additionalIFRS16LeaseContracts = [ifrs16LeaseContract];
      const expectedCollection: IIFRS16LeaseContract[] = [...additionalIFRS16LeaseContracts, ...iFRS16LeaseContractCollection];
      jest.spyOn(iFRS16LeaseContractService, 'addIFRS16LeaseContractToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ rouModelMetadata });
      comp.ngOnInit();

      expect(iFRS16LeaseContractService.query).toHaveBeenCalled();
      expect(iFRS16LeaseContractService.addIFRS16LeaseContractToCollectionIfMissing).toHaveBeenCalledWith(
        iFRS16LeaseContractCollection,
        ...additionalIFRS16LeaseContracts
      );
      expect(comp.iFRS16LeaseContractsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call TransactionAccount query and add missing value', () => {
      const rouModelMetadata: IRouModelMetadata = { id: 456 };
      const assetAccount: ITransactionAccount = { id: 3332 };
      rouModelMetadata.assetAccount = assetAccount;
      const depreciationAccount: ITransactionAccount = { id: 30393 };
      rouModelMetadata.depreciationAccount = depreciationAccount;
      const accruedDepreciationAccount: ITransactionAccount = { id: 12488 };
      rouModelMetadata.accruedDepreciationAccount = accruedDepreciationAccount;

      const transactionAccountCollection: ITransactionAccount[] = [{ id: 73104 }];
      jest.spyOn(transactionAccountService, 'query').mockReturnValue(of(new HttpResponse({ body: transactionAccountCollection })));
      const additionalTransactionAccounts = [assetAccount, depreciationAccount, accruedDepreciationAccount];
      const expectedCollection: ITransactionAccount[] = [...additionalTransactionAccounts, ...transactionAccountCollection];
      jest.spyOn(transactionAccountService, 'addTransactionAccountToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ rouModelMetadata });
      comp.ngOnInit();

      expect(transactionAccountService.query).toHaveBeenCalled();
      expect(transactionAccountService.addTransactionAccountToCollectionIfMissing).toHaveBeenCalledWith(
        transactionAccountCollection,
        ...additionalTransactionAccounts
      );
      expect(comp.transactionAccountsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call AssetCategory query and add missing value', () => {
      const rouModelMetadata: IRouModelMetadata = { id: 456 };
      const assetCategory: IAssetCategory = { id: 27691 };
      rouModelMetadata.assetCategory = assetCategory;

      const assetCategoryCollection: IAssetCategory[] = [{ id: 75225 }];
      jest.spyOn(assetCategoryService, 'query').mockReturnValue(of(new HttpResponse({ body: assetCategoryCollection })));
      const additionalAssetCategories = [assetCategory];
      const expectedCollection: IAssetCategory[] = [...additionalAssetCategories, ...assetCategoryCollection];
      jest.spyOn(assetCategoryService, 'addAssetCategoryToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ rouModelMetadata });
      comp.ngOnInit();

      expect(assetCategoryService.query).toHaveBeenCalled();
      expect(assetCategoryService.addAssetCategoryToCollectionIfMissing).toHaveBeenCalledWith(
        assetCategoryCollection,
        ...additionalAssetCategories
      );
      expect(comp.assetCategoriesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call BusinessDocument query and add missing value', () => {
      const rouModelMetadata: IRouModelMetadata = { id: 456 };
      const documentAttachments: IBusinessDocument[] = [{ id: 49560 }];
      rouModelMetadata.documentAttachments = documentAttachments;

      const businessDocumentCollection: IBusinessDocument[] = [{ id: 36821 }];
      jest.spyOn(businessDocumentService, 'query').mockReturnValue(of(new HttpResponse({ body: businessDocumentCollection })));
      const additionalBusinessDocuments = [...documentAttachments];
      const expectedCollection: IBusinessDocument[] = [...additionalBusinessDocuments, ...businessDocumentCollection];
      jest.spyOn(businessDocumentService, 'addBusinessDocumentToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ rouModelMetadata });
      comp.ngOnInit();

      expect(businessDocumentService.query).toHaveBeenCalled();
      expect(businessDocumentService.addBusinessDocumentToCollectionIfMissing).toHaveBeenCalledWith(
        businessDocumentCollection,
        ...additionalBusinessDocuments
      );
      expect(comp.businessDocumentsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const rouModelMetadata: IRouModelMetadata = { id: 456 };
      const ifrs16LeaseContract: IIFRS16LeaseContract = { id: 59313 };
      rouModelMetadata.ifrs16LeaseContract = ifrs16LeaseContract;
      const assetAccount: ITransactionAccount = { id: 6344 };
      rouModelMetadata.assetAccount = assetAccount;
      const depreciationAccount: ITransactionAccount = { id: 43120 };
      rouModelMetadata.depreciationAccount = depreciationAccount;
      const accruedDepreciationAccount: ITransactionAccount = { id: 52176 };
      rouModelMetadata.accruedDepreciationAccount = accruedDepreciationAccount;
      const assetCategory: IAssetCategory = { id: 36223 };
      rouModelMetadata.assetCategory = assetCategory;
      const documentAttachments: IBusinessDocument = { id: 73622 };
      rouModelMetadata.documentAttachments = [documentAttachments];

      activatedRoute.data = of({ rouModelMetadata });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(rouModelMetadata));
      expect(comp.iFRS16LeaseContractsSharedCollection).toContain(ifrs16LeaseContract);
      expect(comp.transactionAccountsSharedCollection).toContain(assetAccount);
      expect(comp.transactionAccountsSharedCollection).toContain(depreciationAccount);
      expect(comp.transactionAccountsSharedCollection).toContain(accruedDepreciationAccount);
      expect(comp.assetCategoriesSharedCollection).toContain(assetCategory);
      expect(comp.businessDocumentsSharedCollection).toContain(documentAttachments);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<RouModelMetadata>>();
      const rouModelMetadata = { id: 123 };
      jest.spyOn(rouModelMetadataService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ rouModelMetadata });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: rouModelMetadata }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(rouModelMetadataService.update).toHaveBeenCalledWith(rouModelMetadata);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<RouModelMetadata>>();
      const rouModelMetadata = new RouModelMetadata();
      jest.spyOn(rouModelMetadataService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ rouModelMetadata });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: rouModelMetadata }));
      saveSubject.complete();

      // THEN
      expect(rouModelMetadataService.create).toHaveBeenCalledWith(rouModelMetadata);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<RouModelMetadata>>();
      const rouModelMetadata = { id: 123 };
      jest.spyOn(rouModelMetadataService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ rouModelMetadata });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(rouModelMetadataService.update).toHaveBeenCalledWith(rouModelMetadata);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackIFRS16LeaseContractById', () => {
      it('Should return tracked IFRS16LeaseContract primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackIFRS16LeaseContractById(0, entity);
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

    describe('trackAssetCategoryById', () => {
      it('Should return tracked AssetCategory primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackAssetCategoryById(0, entity);
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
  });

  describe('Getting selected relationships', () => {
    describe('getSelectedBusinessDocument', () => {
      it('Should return option if no BusinessDocument is selected', () => {
        const option = { id: 123 };
        const result = comp.getSelectedBusinessDocument(option);
        expect(result === option).toEqual(true);
      });

      it('Should return selected BusinessDocument for according option', () => {
        const option = { id: 123 };
        const selected = { id: 123 };
        const selected2 = { id: 456 };
        const result = comp.getSelectedBusinessDocument(option, [selected2, selected]);
        expect(result === selected).toEqual(true);
        expect(result === selected2).toEqual(false);
        expect(result === option).toEqual(false);
      });

      it('Should return option if this BusinessDocument is not selected', () => {
        const option = { id: 123 };
        const selected = { id: 456 };
        const result = comp.getSelectedBusinessDocument(option, [selected]);
        expect(result === option).toEqual(true);
        expect(result === selected).toEqual(false);
      });
    });
  });
});
