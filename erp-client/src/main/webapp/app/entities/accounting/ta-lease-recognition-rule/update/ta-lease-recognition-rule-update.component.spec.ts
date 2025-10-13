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

import { TALeaseRecognitionRuleService } from '../service/ta-lease-recognition-rule.service';
import { ITALeaseRecognitionRule, TALeaseRecognitionRule } from '../ta-lease-recognition-rule.model';
import { IIFRS16LeaseContract } from 'app/entities/leases/ifrs-16-lease-contract/ifrs-16-lease-contract.model';
import { IFRS16LeaseContractService } from 'app/entities/leases/ifrs-16-lease-contract/service/ifrs-16-lease-contract.service';
import { ITransactionAccount } from 'app/entities/accounting/transaction-account/transaction-account.model';
import { TransactionAccountService } from 'app/entities/accounting/transaction-account/service/transaction-account.service';
import { IPlaceholder } from 'app/entities/system/placeholder/placeholder.model';
import { PlaceholderService } from 'app/entities/system/placeholder/service/placeholder.service';

import { TALeaseRecognitionRuleUpdateComponent } from './ta-lease-recognition-rule-update.component';

describe('TALeaseRecognitionRule Management Update Component', () => {
  let comp: TALeaseRecognitionRuleUpdateComponent;
  let fixture: ComponentFixture<TALeaseRecognitionRuleUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let tALeaseRecognitionRuleService: TALeaseRecognitionRuleService;
  let iFRS16LeaseContractService: IFRS16LeaseContractService;
  let transactionAccountService: TransactionAccountService;
  let placeholderService: PlaceholderService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [TALeaseRecognitionRuleUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(TALeaseRecognitionRuleUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TALeaseRecognitionRuleUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    tALeaseRecognitionRuleService = TestBed.inject(TALeaseRecognitionRuleService);
    iFRS16LeaseContractService = TestBed.inject(IFRS16LeaseContractService);
    transactionAccountService = TestBed.inject(TransactionAccountService);
    placeholderService = TestBed.inject(PlaceholderService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call leaseContract query and add missing value', () => {
      const tALeaseRecognitionRule: ITALeaseRecognitionRule = { id: 456 };
      const leaseContract: IIFRS16LeaseContract = { id: 43557 };
      tALeaseRecognitionRule.leaseContract = leaseContract;

      const leaseContractCollection: IIFRS16LeaseContract[] = [{ id: 26481 }];
      jest.spyOn(iFRS16LeaseContractService, 'query').mockReturnValue(of(new HttpResponse({ body: leaseContractCollection })));
      const expectedCollection: IIFRS16LeaseContract[] = [leaseContract, ...leaseContractCollection];
      jest.spyOn(iFRS16LeaseContractService, 'addIFRS16LeaseContractToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ tALeaseRecognitionRule });
      comp.ngOnInit();

      expect(iFRS16LeaseContractService.query).toHaveBeenCalled();
      expect(iFRS16LeaseContractService.addIFRS16LeaseContractToCollectionIfMissing).toHaveBeenCalledWith(
        leaseContractCollection,
        leaseContract
      );
      expect(comp.leaseContractsCollection).toEqual(expectedCollection);
    });

    it('Should call TransactionAccount query and add missing value', () => {
      const tALeaseRecognitionRule: ITALeaseRecognitionRule = { id: 456 };
      const debit: ITransactionAccount = { id: 64533 };
      tALeaseRecognitionRule.debit = debit;
      const credit: ITransactionAccount = { id: 39931 };
      tALeaseRecognitionRule.credit = credit;

      const transactionAccountCollection: ITransactionAccount[] = [{ id: 20107 }];
      jest.spyOn(transactionAccountService, 'query').mockReturnValue(of(new HttpResponse({ body: transactionAccountCollection })));
      const additionalTransactionAccounts = [debit, credit];
      const expectedCollection: ITransactionAccount[] = [...additionalTransactionAccounts, ...transactionAccountCollection];
      jest.spyOn(transactionAccountService, 'addTransactionAccountToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ tALeaseRecognitionRule });
      comp.ngOnInit();

      expect(transactionAccountService.query).toHaveBeenCalled();
      expect(transactionAccountService.addTransactionAccountToCollectionIfMissing).toHaveBeenCalledWith(
        transactionAccountCollection,
        ...additionalTransactionAccounts
      );
      expect(comp.transactionAccountsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Placeholder query and add missing value', () => {
      const tALeaseRecognitionRule: ITALeaseRecognitionRule = { id: 456 };
      const placeholders: IPlaceholder[] = [{ id: 22011 }];
      tALeaseRecognitionRule.placeholders = placeholders;

      const placeholderCollection: IPlaceholder[] = [{ id: 52788 }];
      jest.spyOn(placeholderService, 'query').mockReturnValue(of(new HttpResponse({ body: placeholderCollection })));
      const additionalPlaceholders = [...placeholders];
      const expectedCollection: IPlaceholder[] = [...additionalPlaceholders, ...placeholderCollection];
      jest.spyOn(placeholderService, 'addPlaceholderToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ tALeaseRecognitionRule });
      comp.ngOnInit();

      expect(placeholderService.query).toHaveBeenCalled();
      expect(placeholderService.addPlaceholderToCollectionIfMissing).toHaveBeenCalledWith(placeholderCollection, ...additionalPlaceholders);
      expect(comp.placeholdersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const tALeaseRecognitionRule: ITALeaseRecognitionRule = { id: 456 };
      const leaseContract: IIFRS16LeaseContract = { id: 45759 };
      tALeaseRecognitionRule.leaseContract = leaseContract;
      const debit: ITransactionAccount = { id: 20744 };
      tALeaseRecognitionRule.debit = debit;
      const credit: ITransactionAccount = { id: 70208 };
      tALeaseRecognitionRule.credit = credit;
      const placeholders: IPlaceholder = { id: 11481 };
      tALeaseRecognitionRule.placeholders = [placeholders];

      activatedRoute.data = of({ tALeaseRecognitionRule });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(tALeaseRecognitionRule));
      expect(comp.leaseContractsCollection).toContain(leaseContract);
      expect(comp.transactionAccountsSharedCollection).toContain(debit);
      expect(comp.transactionAccountsSharedCollection).toContain(credit);
      expect(comp.placeholdersSharedCollection).toContain(placeholders);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<TALeaseRecognitionRule>>();
      const tALeaseRecognitionRule = { id: 123 };
      jest.spyOn(tALeaseRecognitionRuleService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tALeaseRecognitionRule });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: tALeaseRecognitionRule }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(tALeaseRecognitionRuleService.update).toHaveBeenCalledWith(tALeaseRecognitionRule);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<TALeaseRecognitionRule>>();
      const tALeaseRecognitionRule = new TALeaseRecognitionRule();
      jest.spyOn(tALeaseRecognitionRuleService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tALeaseRecognitionRule });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: tALeaseRecognitionRule }));
      saveSubject.complete();

      // THEN
      expect(tALeaseRecognitionRuleService.create).toHaveBeenCalledWith(tALeaseRecognitionRule);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<TALeaseRecognitionRule>>();
      const tALeaseRecognitionRule = { id: 123 };
      jest.spyOn(tALeaseRecognitionRuleService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tALeaseRecognitionRule });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(tALeaseRecognitionRuleService.update).toHaveBeenCalledWith(tALeaseRecognitionRule);
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
