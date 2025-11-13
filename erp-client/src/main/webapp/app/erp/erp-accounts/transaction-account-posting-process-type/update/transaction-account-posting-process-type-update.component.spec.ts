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

import { PlaceholderService } from '../../../erp-pages/placeholder/service/placeholder.service';

jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { TransactionAccountPostingProcessTypeService } from '../service/transaction-account-posting-process-type.service';
import {
  ITransactionAccountPostingProcessType,
  TransactionAccountPostingProcessType,
} from '../transaction-account-posting-process-type.model';

import { TransactionAccountPostingProcessTypeUpdateComponent } from './transaction-account-posting-process-type-update.component';
import { IPlaceholder } from '../../../erp-pages/placeholder/placeholder.model';
import { ITransactionAccountCategory } from '../../transaction-account-category/transaction-account-category.model';
import { TransactionAccountCategoryService } from '../../transaction-account-category/service/transaction-account-category.service';

describe('TransactionAccountPostingProcessType Management Update Component', () => {
  let comp: TransactionAccountPostingProcessTypeUpdateComponent;
  let fixture: ComponentFixture<TransactionAccountPostingProcessTypeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let transactionAccountPostingProcessTypeService: TransactionAccountPostingProcessTypeService;
  let transactionAccountCategoryService: TransactionAccountCategoryService;
  let placeholderService: PlaceholderService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [TransactionAccountPostingProcessTypeUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(TransactionAccountPostingProcessTypeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TransactionAccountPostingProcessTypeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    transactionAccountPostingProcessTypeService = TestBed.inject(TransactionAccountPostingProcessTypeService);
    transactionAccountCategoryService = TestBed.inject(TransactionAccountCategoryService);
    placeholderService = TestBed.inject(PlaceholderService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call TransactionAccountCategory query and add missing value', () => {
      const transactionAccountPostingProcessType: ITransactionAccountPostingProcessType = { id: 456 };
      const debitAccountType: ITransactionAccountCategory = { id: 57374 };
      transactionAccountPostingProcessType.debitAccountType = debitAccountType;
      const creditAccountType: ITransactionAccountCategory = { id: 6588 };
      transactionAccountPostingProcessType.creditAccountType = creditAccountType;

      const transactionAccountCategoryCollection: ITransactionAccountCategory[] = [{ id: 24343 }];
      jest
        .spyOn(transactionAccountCategoryService, 'query')
        .mockReturnValue(of(new HttpResponse({ body: transactionAccountCategoryCollection })));
      const additionalTransactionAccountCategories = [debitAccountType, creditAccountType];
      const expectedCollection: ITransactionAccountCategory[] = [
        ...additionalTransactionAccountCategories,
        ...transactionAccountCategoryCollection,
      ];
      jest
        .spyOn(transactionAccountCategoryService, 'addTransactionAccountCategoryToCollectionIfMissing')
        .mockReturnValue(expectedCollection);

      activatedRoute.data = of({ transactionAccountPostingProcessType });
      comp.ngOnInit();

      expect(transactionAccountCategoryService.query).toHaveBeenCalled();
      expect(transactionAccountCategoryService.addTransactionAccountCategoryToCollectionIfMissing).toHaveBeenCalledWith(
        transactionAccountCategoryCollection,
        ...additionalTransactionAccountCategories
      );
      expect(comp.transactionAccountCategoriesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Placeholder query and add missing value', () => {
      const transactionAccountPostingProcessType: ITransactionAccountPostingProcessType = { id: 456 };
      const placeholders: IPlaceholder[] = [{ id: 35143 }];
      transactionAccountPostingProcessType.placeholders = placeholders;

      const placeholderCollection: IPlaceholder[] = [{ id: 68059 }];
      jest.spyOn(placeholderService, 'query').mockReturnValue(of(new HttpResponse({ body: placeholderCollection })));
      const additionalPlaceholders = [...placeholders];
      const expectedCollection: IPlaceholder[] = [...additionalPlaceholders, ...placeholderCollection];
      jest.spyOn(placeholderService, 'addPlaceholderToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ transactionAccountPostingProcessType });
      comp.ngOnInit();

      expect(placeholderService.query).toHaveBeenCalled();
      expect(placeholderService.addPlaceholderToCollectionIfMissing).toHaveBeenCalledWith(placeholderCollection, ...additionalPlaceholders);
      expect(comp.placeholdersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const transactionAccountPostingProcessType: ITransactionAccountPostingProcessType = { id: 456 };
      const debitAccountType: ITransactionAccountCategory = { id: 33177 };
      transactionAccountPostingProcessType.debitAccountType = debitAccountType;
      const creditAccountType: ITransactionAccountCategory = { id: 9027 };
      transactionAccountPostingProcessType.creditAccountType = creditAccountType;
      const placeholders: IPlaceholder = { id: 92020 };
      transactionAccountPostingProcessType.placeholders = [placeholders];

      activatedRoute.data = of({ transactionAccountPostingProcessType });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(transactionAccountPostingProcessType));
      expect(comp.transactionAccountCategoriesSharedCollection).toContain(debitAccountType);
      expect(comp.transactionAccountCategoriesSharedCollection).toContain(creditAccountType);
      expect(comp.placeholdersSharedCollection).toContain(placeholders);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<TransactionAccountPostingProcessType>>();
      const transactionAccountPostingProcessType = { id: 123 };
      jest.spyOn(transactionAccountPostingProcessTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ transactionAccountPostingProcessType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: transactionAccountPostingProcessType }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(transactionAccountPostingProcessTypeService.update).toHaveBeenCalledWith(transactionAccountPostingProcessType);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<TransactionAccountPostingProcessType>>();
      const transactionAccountPostingProcessType = new TransactionAccountPostingProcessType();
      jest.spyOn(transactionAccountPostingProcessTypeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ transactionAccountPostingProcessType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: transactionAccountPostingProcessType }));
      saveSubject.complete();

      // THEN
      expect(transactionAccountPostingProcessTypeService.create).toHaveBeenCalledWith(transactionAccountPostingProcessType);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<TransactionAccountPostingProcessType>>();
      const transactionAccountPostingProcessType = { id: 123 };
      jest.spyOn(transactionAccountPostingProcessTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ transactionAccountPostingProcessType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(transactionAccountPostingProcessTypeService.update).toHaveBeenCalledWith(transactionAccountPostingProcessType);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
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
