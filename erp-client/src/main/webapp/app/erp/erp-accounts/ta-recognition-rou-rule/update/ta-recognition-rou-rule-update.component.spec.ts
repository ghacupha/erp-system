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

import { IFRS16LeaseContractService } from '../../../erp-leases/ifrs-16-lease-contract/service/ifrs-16-lease-contract.service';

jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { TARecognitionROURuleService } from '../service/ta-recognition-rou-rule.service';
import { ITARecognitionROURule, TARecognitionROURule } from '../ta-recognition-rou-rule.model';

import { TARecognitionROURuleUpdateComponent } from './ta-recognition-rou-rule-update.component';
import { IPlaceholder } from '../../../erp-pages/placeholder/placeholder.model';
import { ITransactionAccount } from '../../transaction-account/transaction-account.model';
import { IIFRS16LeaseContract } from '../../../erp-leases/ifrs-16-lease-contract/ifrs-16-lease-contract.model';
import { TransactionAccountService } from '../../transaction-account/service/transaction-account.service';
import { PlaceholderService } from '../../../erp-pages/placeholder/service/placeholder.service';

describe('TARecognitionROURule Management Update Component', () => {
  let comp: TARecognitionROURuleUpdateComponent;
  let fixture: ComponentFixture<TARecognitionROURuleUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let tARecognitionROURuleService: TARecognitionROURuleService;
  let iFRS16LeaseContractService: IFRS16LeaseContractService;
  let transactionAccountService: TransactionAccountService;
  let placeholderService: PlaceholderService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [TARecognitionROURuleUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(TARecognitionROURuleUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TARecognitionROURuleUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    tARecognitionROURuleService = TestBed.inject(TARecognitionROURuleService);
    iFRS16LeaseContractService = TestBed.inject(IFRS16LeaseContractService);
    transactionAccountService = TestBed.inject(TransactionAccountService);
    placeholderService = TestBed.inject(PlaceholderService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call leaseContract query and add missing value', () => {
      const tARecognitionROURule: ITARecognitionROURule = { id: 456 };
      const leaseContract: IIFRS16LeaseContract = { id: 29311 };
      tARecognitionROURule.leaseContract = leaseContract;

      const leaseContractCollection: IIFRS16LeaseContract[] = [{ id: 41863 }];
      jest.spyOn(iFRS16LeaseContractService, 'query').mockReturnValue(of(new HttpResponse({ body: leaseContractCollection })));
      const expectedCollection: IIFRS16LeaseContract[] = [leaseContract, ...leaseContractCollection];
      jest.spyOn(iFRS16LeaseContractService, 'addIFRS16LeaseContractToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ tARecognitionROURule });
      comp.ngOnInit();

      expect(iFRS16LeaseContractService.query).toHaveBeenCalled();
      expect(iFRS16LeaseContractService.addIFRS16LeaseContractToCollectionIfMissing).toHaveBeenCalledWith(
        leaseContractCollection,
        leaseContract
      );
      expect(comp.leaseContractsCollection).toEqual(expectedCollection);
    });

    it('Should call TransactionAccount query and add missing value', () => {
      const tARecognitionROURule: ITARecognitionROURule = { id: 456 };
      const debit: ITransactionAccount = { id: 49817 };
      tARecognitionROURule.debit = debit;
      const credit: ITransactionAccount = { id: 72514 };
      tARecognitionROURule.credit = credit;

      const transactionAccountCollection: ITransactionAccount[] = [{ id: 81958 }];
      jest.spyOn(transactionAccountService, 'query').mockReturnValue(of(new HttpResponse({ body: transactionAccountCollection })));
      const additionalTransactionAccounts = [debit, credit];
      const expectedCollection: ITransactionAccount[] = [...additionalTransactionAccounts, ...transactionAccountCollection];
      jest.spyOn(transactionAccountService, 'addTransactionAccountToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ tARecognitionROURule });
      comp.ngOnInit();

      expect(transactionAccountService.query).toHaveBeenCalled();
      expect(transactionAccountService.addTransactionAccountToCollectionIfMissing).toHaveBeenCalledWith(
        transactionAccountCollection,
        ...additionalTransactionAccounts
      );
      expect(comp.transactionAccountsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Placeholder query and add missing value', () => {
      const tARecognitionROURule: ITARecognitionROURule = { id: 456 };
      const placeholders: IPlaceholder[] = [{ id: 41077 }];
      tARecognitionROURule.placeholders = placeholders;

      const placeholderCollection: IPlaceholder[] = [{ id: 65440 }];
      jest.spyOn(placeholderService, 'query').mockReturnValue(of(new HttpResponse({ body: placeholderCollection })));
      const additionalPlaceholders = [...placeholders];
      const expectedCollection: IPlaceholder[] = [...additionalPlaceholders, ...placeholderCollection];
      jest.spyOn(placeholderService, 'addPlaceholderToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ tARecognitionROURule });
      comp.ngOnInit();

      expect(placeholderService.query).toHaveBeenCalled();
      expect(placeholderService.addPlaceholderToCollectionIfMissing).toHaveBeenCalledWith(placeholderCollection, ...additionalPlaceholders);
      expect(comp.placeholdersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const tARecognitionROURule: ITARecognitionROURule = { id: 456 };
      const leaseContract: IIFRS16LeaseContract = { id: 48428 };
      tARecognitionROURule.leaseContract = leaseContract;
      const debit: ITransactionAccount = { id: 54421 };
      tARecognitionROURule.debit = debit;
      const credit: ITransactionAccount = { id: 6447 };
      tARecognitionROURule.credit = credit;
      const placeholders: IPlaceholder = { id: 76813 };
      tARecognitionROURule.placeholders = [placeholders];

      activatedRoute.data = of({ tARecognitionROURule });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(tARecognitionROURule));
      expect(comp.leaseContractsCollection).toContain(leaseContract);
      expect(comp.transactionAccountsSharedCollection).toContain(debit);
      expect(comp.transactionAccountsSharedCollection).toContain(credit);
      expect(comp.placeholdersSharedCollection).toContain(placeholders);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<TARecognitionROURule>>();
      const tARecognitionROURule = { id: 123 };
      jest.spyOn(tARecognitionROURuleService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tARecognitionROURule });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: tARecognitionROURule }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(tARecognitionROURuleService.update).toHaveBeenCalledWith(tARecognitionROURule);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<TARecognitionROURule>>();
      const tARecognitionROURule = new TARecognitionROURule();
      jest.spyOn(tARecognitionROURuleService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tARecognitionROURule });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: tARecognitionROURule }));
      saveSubject.complete();

      // THEN
      expect(tARecognitionROURuleService.create).toHaveBeenCalledWith(tARecognitionROURule);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<TARecognitionROURule>>();
      const tARecognitionROURule = { id: 123 };
      jest.spyOn(tARecognitionROURuleService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tARecognitionROURule });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(tARecognitionROURuleService.update).toHaveBeenCalledWith(tARecognitionROURule);
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
