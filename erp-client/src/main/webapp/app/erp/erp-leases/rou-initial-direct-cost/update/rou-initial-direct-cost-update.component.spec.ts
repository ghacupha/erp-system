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

import { IPlaceholder } from '../../../erp-pages/placeholder/placeholder.model';

jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { RouInitialDirectCostService } from '../service/rou-initial-direct-cost.service';
import { IRouInitialDirectCost, RouInitialDirectCost } from '../rou-initial-direct-cost.model';

import { RouInitialDirectCostUpdateComponent } from './rou-initial-direct-cost-update.component';
import { ITransactionAccount } from '../../../erp-accounts/transaction-account/transaction-account.model';
import { IFRS16LeaseContractService } from '../../ifrs-16-lease-contract/service/ifrs-16-lease-contract.service';
import { SettlementService } from '../../../erp-settlements/settlement/service/settlement.service';
import { IIFRS16LeaseContract } from '../../ifrs-16-lease-contract/ifrs-16-lease-contract.model';
import { TransactionAccountService } from '../../../erp-accounts/transaction-account/service/transaction-account.service';
import { PlaceholderService } from '../../../erp-pages/placeholder/service/placeholder.service';
import { ISettlement } from '../../../erp-settlements/settlement/settlement.model';

describe('RouInitialDirectCost Management Update Component', () => {
  let comp: RouInitialDirectCostUpdateComponent;
  let fixture: ComponentFixture<RouInitialDirectCostUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let rouInitialDirectCostService: RouInitialDirectCostService;
  let iFRS16LeaseContractService: IFRS16LeaseContractService;
  let settlementService: SettlementService;
  let transactionAccountService: TransactionAccountService;
  let placeholderService: PlaceholderService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [RouInitialDirectCostUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(RouInitialDirectCostUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(RouInitialDirectCostUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    rouInitialDirectCostService = TestBed.inject(RouInitialDirectCostService);
    iFRS16LeaseContractService = TestBed.inject(IFRS16LeaseContractService);
    settlementService = TestBed.inject(SettlementService);
    transactionAccountService = TestBed.inject(TransactionAccountService);
    placeholderService = TestBed.inject(PlaceholderService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call IFRS16LeaseContract query and add missing value', () => {
      const rouInitialDirectCost: IRouInitialDirectCost = { id: 456 };
      const leaseContract: IIFRS16LeaseContract = { id: 54413 };
      rouInitialDirectCost.leaseContract = leaseContract;

      const iFRS16LeaseContractCollection: IIFRS16LeaseContract[] = [{ id: 5347 }];
      jest.spyOn(iFRS16LeaseContractService, 'query').mockReturnValue(of(new HttpResponse({ body: iFRS16LeaseContractCollection })));
      const additionalIFRS16LeaseContracts = [leaseContract];
      const expectedCollection: IIFRS16LeaseContract[] = [...additionalIFRS16LeaseContracts, ...iFRS16LeaseContractCollection];
      jest.spyOn(iFRS16LeaseContractService, 'addIFRS16LeaseContractToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ rouInitialDirectCost });
      comp.ngOnInit();

      expect(iFRS16LeaseContractService.query).toHaveBeenCalled();
      expect(iFRS16LeaseContractService.addIFRS16LeaseContractToCollectionIfMissing).toHaveBeenCalledWith(
        iFRS16LeaseContractCollection,
        ...additionalIFRS16LeaseContracts
      );
      expect(comp.iFRS16LeaseContractsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Settlement query and add missing value', () => {
      const rouInitialDirectCost: IRouInitialDirectCost = { id: 456 };
      const settlementDetails: ISettlement = { id: 72407 };
      rouInitialDirectCost.settlementDetails = settlementDetails;

      const settlementCollection: ISettlement[] = [{ id: 95156 }];
      jest.spyOn(settlementService, 'query').mockReturnValue(of(new HttpResponse({ body: settlementCollection })));
      const additionalSettlements = [settlementDetails];
      const expectedCollection: ISettlement[] = [...additionalSettlements, ...settlementCollection];
      jest.spyOn(settlementService, 'addSettlementToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ rouInitialDirectCost });
      comp.ngOnInit();

      expect(settlementService.query).toHaveBeenCalled();
      expect(settlementService.addSettlementToCollectionIfMissing).toHaveBeenCalledWith(settlementCollection, ...additionalSettlements);
      expect(comp.settlementsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call TransactionAccount query and add missing value', () => {
      const rouInitialDirectCost: IRouInitialDirectCost = { id: 456 };
      const targetROUAccount: ITransactionAccount = { id: 72959 };
      rouInitialDirectCost.targetROUAccount = targetROUAccount;
      const transferAccount: ITransactionAccount = { id: 5038 };
      rouInitialDirectCost.transferAccount = transferAccount;

      const transactionAccountCollection: ITransactionAccount[] = [{ id: 69069 }];
      jest.spyOn(transactionAccountService, 'query').mockReturnValue(of(new HttpResponse({ body: transactionAccountCollection })));
      const additionalTransactionAccounts = [targetROUAccount, transferAccount];
      const expectedCollection: ITransactionAccount[] = [...additionalTransactionAccounts, ...transactionAccountCollection];
      jest.spyOn(transactionAccountService, 'addTransactionAccountToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ rouInitialDirectCost });
      comp.ngOnInit();

      expect(transactionAccountService.query).toHaveBeenCalled();
      expect(transactionAccountService.addTransactionAccountToCollectionIfMissing).toHaveBeenCalledWith(
        transactionAccountCollection,
        ...additionalTransactionAccounts
      );
      expect(comp.transactionAccountsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Placeholder query and add missing value', () => {
      const rouInitialDirectCost: IRouInitialDirectCost = { id: 456 };
      const placeholders: IPlaceholder[] = [{ id: 97125 }];
      rouInitialDirectCost.placeholders = placeholders;

      const placeholderCollection: IPlaceholder[] = [{ id: 54108 }];
      jest.spyOn(placeholderService, 'query').mockReturnValue(of(new HttpResponse({ body: placeholderCollection })));
      const additionalPlaceholders = [...placeholders];
      const expectedCollection: IPlaceholder[] = [...additionalPlaceholders, ...placeholderCollection];
      jest.spyOn(placeholderService, 'addPlaceholderToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ rouInitialDirectCost });
      comp.ngOnInit();

      expect(placeholderService.query).toHaveBeenCalled();
      expect(placeholderService.addPlaceholderToCollectionIfMissing).toHaveBeenCalledWith(placeholderCollection, ...additionalPlaceholders);
      expect(comp.placeholdersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const rouInitialDirectCost: IRouInitialDirectCost = { id: 456 };
      const leaseContract: IIFRS16LeaseContract = { id: 1508 };
      rouInitialDirectCost.leaseContract = leaseContract;
      const settlementDetails: ISettlement = { id: 20679 };
      rouInitialDirectCost.settlementDetails = settlementDetails;
      const targetROUAccount: ITransactionAccount = { id: 52923 };
      rouInitialDirectCost.targetROUAccount = targetROUAccount;
      const transferAccount: ITransactionAccount = { id: 62051 };
      rouInitialDirectCost.transferAccount = transferAccount;
      const placeholders: IPlaceholder = { id: 49883 };
      rouInitialDirectCost.placeholders = [placeholders];

      activatedRoute.data = of({ rouInitialDirectCost });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(rouInitialDirectCost));
      expect(comp.iFRS16LeaseContractsSharedCollection).toContain(leaseContract);
      expect(comp.settlementsSharedCollection).toContain(settlementDetails);
      expect(comp.transactionAccountsSharedCollection).toContain(targetROUAccount);
      expect(comp.transactionAccountsSharedCollection).toContain(transferAccount);
      expect(comp.placeholdersSharedCollection).toContain(placeholders);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<RouInitialDirectCost>>();
      const rouInitialDirectCost = { id: 123 };
      jest.spyOn(rouInitialDirectCostService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ rouInitialDirectCost });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: rouInitialDirectCost }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(rouInitialDirectCostService.update).toHaveBeenCalledWith(rouInitialDirectCost);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<RouInitialDirectCost>>();
      const rouInitialDirectCost = new RouInitialDirectCost();
      jest.spyOn(rouInitialDirectCostService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ rouInitialDirectCost });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: rouInitialDirectCost }));
      saveSubject.complete();

      // THEN
      expect(rouInitialDirectCostService.create).toHaveBeenCalledWith(rouInitialDirectCost);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<RouInitialDirectCost>>();
      const rouInitialDirectCost = { id: 123 };
      jest.spyOn(rouInitialDirectCostService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ rouInitialDirectCost });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(rouInitialDirectCostService.update).toHaveBeenCalledWith(rouInitialDirectCost);
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

    describe('trackSettlementById', () => {
      it('Should return tracked Settlement primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackSettlementById(0, entity);
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
