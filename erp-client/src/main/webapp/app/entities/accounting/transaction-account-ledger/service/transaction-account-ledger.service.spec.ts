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

import { ITransactionAccountLedger, TransactionAccountLedger } from '../transaction-account-ledger.model';

import { TransactionAccountLedgerService } from './transaction-account-ledger.service';

describe('TransactionAccountLedger Service', () => {
  let service: TransactionAccountLedgerService;
  let httpMock: HttpTestingController;
  let elemDefault: ITransactionAccountLedger;
  let expectedResult: ITransactionAccountLedger | ITransactionAccountLedger[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(TransactionAccountLedgerService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      ledgerCode: 'AAAAAAA',
      ledgerName: 'AAAAAAA',
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

    it('should create a TransactionAccountLedger', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new TransactionAccountLedger()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a TransactionAccountLedger', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          ledgerCode: 'BBBBBB',
          ledgerName: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a TransactionAccountLedger', () => {
      const patchObject = Object.assign(
        {
          ledgerCode: 'BBBBBB',
          ledgerName: 'BBBBBB',
        },
        new TransactionAccountLedger()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of TransactionAccountLedger', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          ledgerCode: 'BBBBBB',
          ledgerName: 'BBBBBB',
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

    it('should delete a TransactionAccountLedger', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addTransactionAccountLedgerToCollectionIfMissing', () => {
      it('should add a TransactionAccountLedger to an empty array', () => {
        const transactionAccountLedger: ITransactionAccountLedger = { id: 123 };
        expectedResult = service.addTransactionAccountLedgerToCollectionIfMissing([], transactionAccountLedger);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(transactionAccountLedger);
      });

      it('should not add a TransactionAccountLedger to an array that contains it', () => {
        const transactionAccountLedger: ITransactionAccountLedger = { id: 123 };
        const transactionAccountLedgerCollection: ITransactionAccountLedger[] = [
          {
            ...transactionAccountLedger,
          },
          { id: 456 },
        ];
        expectedResult = service.addTransactionAccountLedgerToCollectionIfMissing(
          transactionAccountLedgerCollection,
          transactionAccountLedger
        );
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a TransactionAccountLedger to an array that doesn't contain it", () => {
        const transactionAccountLedger: ITransactionAccountLedger = { id: 123 };
        const transactionAccountLedgerCollection: ITransactionAccountLedger[] = [{ id: 456 }];
        expectedResult = service.addTransactionAccountLedgerToCollectionIfMissing(
          transactionAccountLedgerCollection,
          transactionAccountLedger
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(transactionAccountLedger);
      });

      it('should add only unique TransactionAccountLedger to an array', () => {
        const transactionAccountLedgerArray: ITransactionAccountLedger[] = [{ id: 123 }, { id: 456 }, { id: 92744 }];
        const transactionAccountLedgerCollection: ITransactionAccountLedger[] = [{ id: 123 }];
        expectedResult = service.addTransactionAccountLedgerToCollectionIfMissing(
          transactionAccountLedgerCollection,
          ...transactionAccountLedgerArray
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const transactionAccountLedger: ITransactionAccountLedger = { id: 123 };
        const transactionAccountLedger2: ITransactionAccountLedger = { id: 456 };
        expectedResult = service.addTransactionAccountLedgerToCollectionIfMissing([], transactionAccountLedger, transactionAccountLedger2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(transactionAccountLedger);
        expect(expectedResult).toContain(transactionAccountLedger2);
      });

      it('should accept null and undefined values', () => {
        const transactionAccountLedger: ITransactionAccountLedger = { id: 123 };
        expectedResult = service.addTransactionAccountLedgerToCollectionIfMissing([], null, transactionAccountLedger, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(transactionAccountLedger);
      });

      it('should return initial array if no TransactionAccountLedger is added', () => {
        const transactionAccountLedgerCollection: ITransactionAccountLedger[] = [{ id: 123 }];
        expectedResult = service.addTransactionAccountLedgerToCollectionIfMissing(transactionAccountLedgerCollection, undefined, null);
        expect(expectedResult).toEqual(transactionAccountLedgerCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
