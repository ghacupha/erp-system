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

import { AccountTypes } from 'app/entities/enumerations/account-types.model';
import { AccountSubTypes } from 'app/entities/enumerations/account-sub-types.model';
import { ITransactionAccount, TransactionAccount } from '../transaction-account.model';

import { TransactionAccountService } from './transaction-account.service';

describe('TransactionAccount Service', () => {
  let service: TransactionAccountService;
  let httpMock: HttpTestingController;
  let elemDefault: ITransactionAccount;
  let expectedResult: ITransactionAccount | ITransactionAccount[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(TransactionAccountService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      accountNumber: 'AAAAAAA',
      accountName: 'AAAAAAA',
      notesContentType: 'image/png',
      notes: 'AAAAAAA',
      accountType: AccountTypes.ASSET,
      accountSubType: AccountSubTypes.SETTLEMENT_ASSET,
      dummyAccount: false,
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

    it('should create a TransactionAccount', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new TransactionAccount()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a TransactionAccount', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          accountNumber: 'BBBBBB',
          accountName: 'BBBBBB',
          notes: 'BBBBBB',
          accountType: 'BBBBBB',
          accountSubType: 'BBBBBB',
          dummyAccount: true,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a TransactionAccount', () => {
      const patchObject = Object.assign(
        {
          accountNumber: 'BBBBBB',
          accountName: 'BBBBBB',
          accountType: 'BBBBBB',
        },
        new TransactionAccount()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of TransactionAccount', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          accountNumber: 'BBBBBB',
          accountName: 'BBBBBB',
          notes: 'BBBBBB',
          accountType: 'BBBBBB',
          accountSubType: 'BBBBBB',
          dummyAccount: true,
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

    it('should delete a TransactionAccount', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addTransactionAccountToCollectionIfMissing', () => {
      it('should add a TransactionAccount to an empty array', () => {
        const transactionAccount: ITransactionAccount = { id: 123 };
        expectedResult = service.addTransactionAccountToCollectionIfMissing([], transactionAccount);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(transactionAccount);
      });

      it('should not add a TransactionAccount to an array that contains it', () => {
        const transactionAccount: ITransactionAccount = { id: 123 };
        const transactionAccountCollection: ITransactionAccount[] = [
          {
            ...transactionAccount,
          },
          { id: 456 },
        ];
        expectedResult = service.addTransactionAccountToCollectionIfMissing(transactionAccountCollection, transactionAccount);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a TransactionAccount to an array that doesn't contain it", () => {
        const transactionAccount: ITransactionAccount = { id: 123 };
        const transactionAccountCollection: ITransactionAccount[] = [{ id: 456 }];
        expectedResult = service.addTransactionAccountToCollectionIfMissing(transactionAccountCollection, transactionAccount);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(transactionAccount);
      });

      it('should add only unique TransactionAccount to an array', () => {
        const transactionAccountArray: ITransactionAccount[] = [{ id: 123 }, { id: 456 }, { id: 45369 }];
        const transactionAccountCollection: ITransactionAccount[] = [{ id: 123 }];
        expectedResult = service.addTransactionAccountToCollectionIfMissing(transactionAccountCollection, ...transactionAccountArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const transactionAccount: ITransactionAccount = { id: 123 };
        const transactionAccount2: ITransactionAccount = { id: 456 };
        expectedResult = service.addTransactionAccountToCollectionIfMissing([], transactionAccount, transactionAccount2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(transactionAccount);
        expect(expectedResult).toContain(transactionAccount2);
      });

      it('should accept null and undefined values', () => {
        const transactionAccount: ITransactionAccount = { id: 123 };
        expectedResult = service.addTransactionAccountToCollectionIfMissing([], null, transactionAccount, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(transactionAccount);
      });

      it('should return initial array if no TransactionAccount is added', () => {
        const transactionAccountCollection: ITransactionAccount[] = [{ id: 123 }];
        expectedResult = service.addTransactionAccountToCollectionIfMissing(transactionAccountCollection, undefined, null);
        expect(expectedResult).toEqual(transactionAccountCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
