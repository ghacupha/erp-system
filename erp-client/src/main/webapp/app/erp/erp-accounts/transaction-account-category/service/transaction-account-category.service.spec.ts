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

import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { transactionAccountPostingTypes } from 'app/entities/enumerations/transaction-account-posting-types.model';
import { ITransactionAccountCategory, TransactionAccountCategory } from '../transaction-account-category.model';

import { TransactionAccountCategoryService } from './transaction-account-category.service';

describe('TransactionAccountCategory Service', () => {
  let service: TransactionAccountCategoryService;
  let httpMock: HttpTestingController;
  let elemDefault: ITransactionAccountCategory;
  let expectedResult: ITransactionAccountCategory | ITransactionAccountCategory[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(TransactionAccountCategoryService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      name: 'AAAAAAA',
      transactionAccountPostingType: transactionAccountPostingTypes.DEBIT,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign({}, elemDefault);

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a TransactionAccountCategory', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new TransactionAccountCategory()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a TransactionAccountCategory', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
          transactionAccountPostingType: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a TransactionAccountCategory', () => {
      const patchObject = Object.assign(
        {
          name: 'BBBBBB',
        },
        new TransactionAccountCategory()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of TransactionAccountCategory', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
          transactionAccountPostingType: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a TransactionAccountCategory', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addTransactionAccountCategoryToCollectionIfMissing', () => {
      it('should add a TransactionAccountCategory to an empty array', () => {
        const transactionAccountCategory: ITransactionAccountCategory = { id: 123 };
        expectedResult = service.addTransactionAccountCategoryToCollectionIfMissing([], transactionAccountCategory);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(transactionAccountCategory);
      });

      it('should not add a TransactionAccountCategory to an array that contains it', () => {
        const transactionAccountCategory: ITransactionAccountCategory = { id: 123 };
        const transactionAccountCategoryCollection: ITransactionAccountCategory[] = [
          {
            ...transactionAccountCategory,
          },
          { id: 456 },
        ];
        expectedResult = service.addTransactionAccountCategoryToCollectionIfMissing(
          transactionAccountCategoryCollection,
          transactionAccountCategory
        );
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a TransactionAccountCategory to an array that doesn't contain it", () => {
        const transactionAccountCategory: ITransactionAccountCategory = { id: 123 };
        const transactionAccountCategoryCollection: ITransactionAccountCategory[] = [{ id: 456 }];
        expectedResult = service.addTransactionAccountCategoryToCollectionIfMissing(
          transactionAccountCategoryCollection,
          transactionAccountCategory
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(transactionAccountCategory);
      });

      it('should add only unique TransactionAccountCategory to an array', () => {
        const transactionAccountCategoryArray: ITransactionAccountCategory[] = [{ id: 123 }, { id: 456 }, { id: 31660 }];
        const transactionAccountCategoryCollection: ITransactionAccountCategory[] = [{ id: 123 }];
        expectedResult = service.addTransactionAccountCategoryToCollectionIfMissing(
          transactionAccountCategoryCollection,
          ...transactionAccountCategoryArray
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const transactionAccountCategory: ITransactionAccountCategory = { id: 123 };
        const transactionAccountCategory2: ITransactionAccountCategory = { id: 456 };
        expectedResult = service.addTransactionAccountCategoryToCollectionIfMissing(
          [],
          transactionAccountCategory,
          transactionAccountCategory2
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(transactionAccountCategory);
        expect(expectedResult).toContain(transactionAccountCategory2);
      });

      it('should accept null and undefined values', () => {
        const transactionAccountCategory: ITransactionAccountCategory = { id: 123 };
        expectedResult = service.addTransactionAccountCategoryToCollectionIfMissing([], null, transactionAccountCategory, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(transactionAccountCategory);
      });

      it('should return initial array if no TransactionAccountCategory is added', () => {
        const transactionAccountCategoryCollection: ITransactionAccountCategory[] = [{ id: 123 }];
        expectedResult = service.addTransactionAccountCategoryToCollectionIfMissing(transactionAccountCategoryCollection, undefined, null);
        expect(expectedResult).toEqual(transactionAccountCategoryCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
