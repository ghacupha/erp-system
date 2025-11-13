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
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { ICardAcquiringTransaction, CardAcquiringTransaction } from '../card-acquiring-transaction.model';

import { CardAcquiringTransactionService } from './card-acquiring-transaction.service';

describe('CardAcquiringTransaction Service', () => {
  let service: CardAcquiringTransactionService;
  let httpMock: HttpTestingController;
  let elemDefault: ICardAcquiringTransaction;
  let expectedResult: ICardAcquiringTransaction | ICardAcquiringTransaction[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CardAcquiringTransactionService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      reportingDate: currentDate,
      terminalId: 'AAAAAAA',
      numberOfTransactions: 0,
      valueOfTransactionsInLCY: 0,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          reportingDate: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a CardAcquiringTransaction', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          reportingDate: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          reportingDate: currentDate,
        },
        returnedFromService
      );

      service.create(new CardAcquiringTransaction()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CardAcquiringTransaction', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          reportingDate: currentDate.format(DATE_FORMAT),
          terminalId: 'BBBBBB',
          numberOfTransactions: 1,
          valueOfTransactionsInLCY: 1,
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          reportingDate: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a CardAcquiringTransaction', () => {
      const patchObject = Object.assign(
        {
          reportingDate: currentDate.format(DATE_FORMAT),
          numberOfTransactions: 1,
          valueOfTransactionsInLCY: 1,
        },
        new CardAcquiringTransaction()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          reportingDate: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of CardAcquiringTransaction', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          reportingDate: currentDate.format(DATE_FORMAT),
          terminalId: 'BBBBBB',
          numberOfTransactions: 1,
          valueOfTransactionsInLCY: 1,
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          reportingDate: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a CardAcquiringTransaction', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addCardAcquiringTransactionToCollectionIfMissing', () => {
      it('should add a CardAcquiringTransaction to an empty array', () => {
        const cardAcquiringTransaction: ICardAcquiringTransaction = { id: 123 };
        expectedResult = service.addCardAcquiringTransactionToCollectionIfMissing([], cardAcquiringTransaction);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(cardAcquiringTransaction);
      });

      it('should not add a CardAcquiringTransaction to an array that contains it', () => {
        const cardAcquiringTransaction: ICardAcquiringTransaction = { id: 123 };
        const cardAcquiringTransactionCollection: ICardAcquiringTransaction[] = [
          {
            ...cardAcquiringTransaction,
          },
          { id: 456 },
        ];
        expectedResult = service.addCardAcquiringTransactionToCollectionIfMissing(
          cardAcquiringTransactionCollection,
          cardAcquiringTransaction
        );
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CardAcquiringTransaction to an array that doesn't contain it", () => {
        const cardAcquiringTransaction: ICardAcquiringTransaction = { id: 123 };
        const cardAcquiringTransactionCollection: ICardAcquiringTransaction[] = [{ id: 456 }];
        expectedResult = service.addCardAcquiringTransactionToCollectionIfMissing(
          cardAcquiringTransactionCollection,
          cardAcquiringTransaction
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(cardAcquiringTransaction);
      });

      it('should add only unique CardAcquiringTransaction to an array', () => {
        const cardAcquiringTransactionArray: ICardAcquiringTransaction[] = [{ id: 123 }, { id: 456 }, { id: 61698 }];
        const cardAcquiringTransactionCollection: ICardAcquiringTransaction[] = [{ id: 123 }];
        expectedResult = service.addCardAcquiringTransactionToCollectionIfMissing(
          cardAcquiringTransactionCollection,
          ...cardAcquiringTransactionArray
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const cardAcquiringTransaction: ICardAcquiringTransaction = { id: 123 };
        const cardAcquiringTransaction2: ICardAcquiringTransaction = { id: 456 };
        expectedResult = service.addCardAcquiringTransactionToCollectionIfMissing([], cardAcquiringTransaction, cardAcquiringTransaction2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(cardAcquiringTransaction);
        expect(expectedResult).toContain(cardAcquiringTransaction2);
      });

      it('should accept null and undefined values', () => {
        const cardAcquiringTransaction: ICardAcquiringTransaction = { id: 123 };
        expectedResult = service.addCardAcquiringTransactionToCollectionIfMissing([], null, cardAcquiringTransaction, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(cardAcquiringTransaction);
      });

      it('should return initial array if no CardAcquiringTransaction is added', () => {
        const cardAcquiringTransactionCollection: ICardAcquiringTransaction[] = [{ id: 123 }];
        expectedResult = service.addCardAcquiringTransactionToCollectionIfMissing(cardAcquiringTransactionCollection, undefined, null);
        expect(expectedResult).toEqual(cardAcquiringTransactionCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
