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

import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import {
  ITransactionAccountPostingProcessType,
  TransactionAccountPostingProcessType,
} from '../transaction-account-posting-process-type.model';

import { TransactionAccountPostingProcessTypeService } from './transaction-account-posting-process-type.service';

describe('TransactionAccountPostingProcessType Service', () => {
  let service: TransactionAccountPostingProcessTypeService;
  let httpMock: HttpTestingController;
  let elemDefault: ITransactionAccountPostingProcessType;
  let expectedResult: ITransactionAccountPostingProcessType | ITransactionAccountPostingProcessType[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(TransactionAccountPostingProcessTypeService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      name: 'AAAAAAA',
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

    it('should create a TransactionAccountPostingProcessType', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new TransactionAccountPostingProcessType()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a TransactionAccountPostingProcessType', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a TransactionAccountPostingProcessType', () => {
      const patchObject = Object.assign(
        {
          name: 'BBBBBB',
        },
        new TransactionAccountPostingProcessType()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of TransactionAccountPostingProcessType', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
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

    it('should delete a TransactionAccountPostingProcessType', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addTransactionAccountPostingProcessTypeToCollectionIfMissing', () => {
      it('should add a TransactionAccountPostingProcessType to an empty array', () => {
        const transactionAccountPostingProcessType: ITransactionAccountPostingProcessType = { id: 123 };
        expectedResult = service.addTransactionAccountPostingProcessTypeToCollectionIfMissing([], transactionAccountPostingProcessType);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(transactionAccountPostingProcessType);
      });

      it('should not add a TransactionAccountPostingProcessType to an array that contains it', () => {
        const transactionAccountPostingProcessType: ITransactionAccountPostingProcessType = { id: 123 };
        const transactionAccountPostingProcessTypeCollection: ITransactionAccountPostingProcessType[] = [
          {
            ...transactionAccountPostingProcessType,
          },
          { id: 456 },
        ];
        expectedResult = service.addTransactionAccountPostingProcessTypeToCollectionIfMissing(
          transactionAccountPostingProcessTypeCollection,
          transactionAccountPostingProcessType
        );
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a TransactionAccountPostingProcessType to an array that doesn't contain it", () => {
        const transactionAccountPostingProcessType: ITransactionAccountPostingProcessType = { id: 123 };
        const transactionAccountPostingProcessTypeCollection: ITransactionAccountPostingProcessType[] = [{ id: 456 }];
        expectedResult = service.addTransactionAccountPostingProcessTypeToCollectionIfMissing(
          transactionAccountPostingProcessTypeCollection,
          transactionAccountPostingProcessType
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(transactionAccountPostingProcessType);
      });

      it('should add only unique TransactionAccountPostingProcessType to an array', () => {
        const transactionAccountPostingProcessTypeArray: ITransactionAccountPostingProcessType[] = [
          { id: 123 },
          { id: 456 },
          { id: 45886 },
        ];
        const transactionAccountPostingProcessTypeCollection: ITransactionAccountPostingProcessType[] = [{ id: 123 }];
        expectedResult = service.addTransactionAccountPostingProcessTypeToCollectionIfMissing(
          transactionAccountPostingProcessTypeCollection,
          ...transactionAccountPostingProcessTypeArray
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const transactionAccountPostingProcessType: ITransactionAccountPostingProcessType = { id: 123 };
        const transactionAccountPostingProcessType2: ITransactionAccountPostingProcessType = { id: 456 };
        expectedResult = service.addTransactionAccountPostingProcessTypeToCollectionIfMissing(
          [],
          transactionAccountPostingProcessType,
          transactionAccountPostingProcessType2
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(transactionAccountPostingProcessType);
        expect(expectedResult).toContain(transactionAccountPostingProcessType2);
      });

      it('should accept null and undefined values', () => {
        const transactionAccountPostingProcessType: ITransactionAccountPostingProcessType = { id: 123 };
        expectedResult = service.addTransactionAccountPostingProcessTypeToCollectionIfMissing(
          [],
          null,
          transactionAccountPostingProcessType,
          undefined
        );
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(transactionAccountPostingProcessType);
      });

      it('should return initial array if no TransactionAccountPostingProcessType is added', () => {
        const transactionAccountPostingProcessTypeCollection: ITransactionAccountPostingProcessType[] = [{ id: 123 }];
        expectedResult = service.addTransactionAccountPostingProcessTypeToCollectionIfMissing(
          transactionAccountPostingProcessTypeCollection,
          undefined,
          null
        );
        expect(expectedResult).toEqual(transactionAccountPostingProcessTypeCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
