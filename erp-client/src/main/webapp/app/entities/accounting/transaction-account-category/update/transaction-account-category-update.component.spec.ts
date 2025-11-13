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

jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { TransactionAccountCategoryService } from '../service/transaction-account-category.service';
import { ITransactionAccountCategory, TransactionAccountCategory } from '../transaction-account-category.model';
import { IPlaceholder } from 'app/entities/system/placeholder/placeholder.model';
import { PlaceholderService } from 'app/entities/system/placeholder/service/placeholder.service';
import { ITransactionAccountLedger } from 'app/entities/accounting/transaction-account-ledger/transaction-account-ledger.model';
import { TransactionAccountLedgerService } from 'app/entities/accounting/transaction-account-ledger/service/transaction-account-ledger.service';

import { TransactionAccountCategoryUpdateComponent } from './transaction-account-category-update.component';

describe('TransactionAccountCategory Management Update Component', () => {
  let comp: TransactionAccountCategoryUpdateComponent;
  let fixture: ComponentFixture<TransactionAccountCategoryUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let transactionAccountCategoryService: TransactionAccountCategoryService;
  let placeholderService: PlaceholderService;
  let transactionAccountLedgerService: TransactionAccountLedgerService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [TransactionAccountCategoryUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(TransactionAccountCategoryUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TransactionAccountCategoryUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    transactionAccountCategoryService = TestBed.inject(TransactionAccountCategoryService);
    placeholderService = TestBed.inject(PlaceholderService);
    transactionAccountLedgerService = TestBed.inject(TransactionAccountLedgerService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Placeholder query and add missing value', () => {
      const transactionAccountCategory: ITransactionAccountCategory = { id: 456 };
      const placeholders: IPlaceholder[] = [{ id: 86718 }];
      transactionAccountCategory.placeholders = placeholders;

      const placeholderCollection: IPlaceholder[] = [{ id: 89612 }];
      jest.spyOn(placeholderService, 'query').mockReturnValue(of(new HttpResponse({ body: placeholderCollection })));
      const additionalPlaceholders = [...placeholders];
      const expectedCollection: IPlaceholder[] = [...additionalPlaceholders, ...placeholderCollection];
      jest.spyOn(placeholderService, 'addPlaceholderToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ transactionAccountCategory });
      comp.ngOnInit();

      expect(placeholderService.query).toHaveBeenCalled();
      expect(placeholderService.addPlaceholderToCollectionIfMissing).toHaveBeenCalledWith(placeholderCollection, ...additionalPlaceholders);
      expect(comp.placeholdersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call TransactionAccountLedger query and add missing value', () => {
      const transactionAccountCategory: ITransactionAccountCategory = { id: 456 };
      const accountLedger: ITransactionAccountLedger = { id: 68451 };
      transactionAccountCategory.accountLedger = accountLedger;

      const transactionAccountLedgerCollection: ITransactionAccountLedger[] = [{ id: 81664 }];
      jest
        .spyOn(transactionAccountLedgerService, 'query')
        .mockReturnValue(of(new HttpResponse({ body: transactionAccountLedgerCollection })));
      const additionalTransactionAccountLedgers = [accountLedger];
      const expectedCollection: ITransactionAccountLedger[] = [
        ...additionalTransactionAccountLedgers,
        ...transactionAccountLedgerCollection,
      ];
      jest.spyOn(transactionAccountLedgerService, 'addTransactionAccountLedgerToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ transactionAccountCategory });
      comp.ngOnInit();

      expect(transactionAccountLedgerService.query).toHaveBeenCalled();
      expect(transactionAccountLedgerService.addTransactionAccountLedgerToCollectionIfMissing).toHaveBeenCalledWith(
        transactionAccountLedgerCollection,
        ...additionalTransactionAccountLedgers
      );
      expect(comp.transactionAccountLedgersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const transactionAccountCategory: ITransactionAccountCategory = { id: 456 };
      const placeholders: IPlaceholder = { id: 40687 };
      transactionAccountCategory.placeholders = [placeholders];
      const accountLedger: ITransactionAccountLedger = { id: 34583 };
      transactionAccountCategory.accountLedger = accountLedger;

      activatedRoute.data = of({ transactionAccountCategory });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(transactionAccountCategory));
      expect(comp.placeholdersSharedCollection).toContain(placeholders);
      expect(comp.transactionAccountLedgersSharedCollection).toContain(accountLedger);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<TransactionAccountCategory>>();
      const transactionAccountCategory = { id: 123 };
      jest.spyOn(transactionAccountCategoryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ transactionAccountCategory });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: transactionAccountCategory }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(transactionAccountCategoryService.update).toHaveBeenCalledWith(transactionAccountCategory);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<TransactionAccountCategory>>();
      const transactionAccountCategory = new TransactionAccountCategory();
      jest.spyOn(transactionAccountCategoryService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ transactionAccountCategory });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: transactionAccountCategory }));
      saveSubject.complete();

      // THEN
      expect(transactionAccountCategoryService.create).toHaveBeenCalledWith(transactionAccountCategory);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<TransactionAccountCategory>>();
      const transactionAccountCategory = { id: 123 };
      jest.spyOn(transactionAccountCategoryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ transactionAccountCategory });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(transactionAccountCategoryService.update).toHaveBeenCalledWith(transactionAccountCategory);
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

    describe('trackTransactionAccountLedgerById', () => {
      it('Should return tracked TransactionAccountLedger primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackTransactionAccountLedgerById(0, entity);
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
