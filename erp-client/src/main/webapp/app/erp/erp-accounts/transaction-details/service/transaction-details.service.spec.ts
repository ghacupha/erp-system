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

import { DATE_FORMAT, DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ITransactionDetails, TransactionDetails } from '../transaction-details.model';

import { TransactionDetailsService } from './transaction-details.service';

describe('TransactionDetails Service', () => {
  let service: TransactionDetailsService;
  let httpMock: HttpTestingController;
  let elemDefault: ITransactionDetails;
  let expectedResult: ITransactionDetails | ITransactionDetails[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(TransactionDetailsService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      entryId: 0,
      transactionDate: currentDate,
      description: 'AAAAAAA',
      amount: 0,
      createdAt: currentDate,
      modifiedAt: currentDate,
      transactionType: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          transactionDate: currentDate.format(DATE_FORMAT),
          createdAt: currentDate.format(DATE_TIME_FORMAT),
          modifiedAt: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a TransactionDetails', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          transactionDate: currentDate.format(DATE_FORMAT),
          createdAt: currentDate.format(DATE_TIME_FORMAT),
          modifiedAt: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          transactionDate: currentDate,
          createdAt: currentDate,
          modifiedAt: currentDate,
        },
        returnedFromService
      );

      service.create(new TransactionDetails()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a TransactionDetails', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          entryId: 1,
          transactionDate: currentDate.format(DATE_FORMAT),
          description: 'BBBBBB',
          amount: 1,
          createdAt: currentDate.format(DATE_TIME_FORMAT),
          modifiedAt: currentDate.format(DATE_TIME_FORMAT),
          transactionType: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          transactionDate: currentDate,
          createdAt: currentDate,
          modifiedAt: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a TransactionDetails', () => {
      const patchObject = Object.assign(
        {
          transactionDate: currentDate.format(DATE_FORMAT),
          amount: 1,
          createdAt: currentDate.format(DATE_TIME_FORMAT),
          modifiedAt: currentDate.format(DATE_TIME_FORMAT),
        },
        new TransactionDetails()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          transactionDate: currentDate,
          createdAt: currentDate,
          modifiedAt: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of TransactionDetails', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          entryId: 1,
          transactionDate: currentDate.format(DATE_FORMAT),
          description: 'BBBBBB',
          amount: 1,
          createdAt: currentDate.format(DATE_TIME_FORMAT),
          modifiedAt: currentDate.format(DATE_TIME_FORMAT),
          transactionType: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          transactionDate: currentDate,
          createdAt: currentDate,
          modifiedAt: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a TransactionDetails', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addTransactionDetailsToCollectionIfMissing', () => {
      it('should add a TransactionDetails to an empty array', () => {
        const transactionDetails: ITransactionDetails = { id: 123 };
        expectedResult = service.addTransactionDetailsToCollectionIfMissing([], transactionDetails);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(transactionDetails);
      });

      it('should not add a TransactionDetails to an array that contains it', () => {
        const transactionDetails: ITransactionDetails = { id: 123 };
        const transactionDetailsCollection: ITransactionDetails[] = [
          {
            ...transactionDetails,
          },
          { id: 456 },
        ];
        expectedResult = service.addTransactionDetailsToCollectionIfMissing(transactionDetailsCollection, transactionDetails);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a TransactionDetails to an array that doesn't contain it", () => {
        const transactionDetails: ITransactionDetails = { id: 123 };
        const transactionDetailsCollection: ITransactionDetails[] = [{ id: 456 }];
        expectedResult = service.addTransactionDetailsToCollectionIfMissing(transactionDetailsCollection, transactionDetails);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(transactionDetails);
      });

      it('should add only unique TransactionDetails to an array', () => {
        const transactionDetailsArray: ITransactionDetails[] = [{ id: 123 }, { id: 456 }, { id: 994 }];
        const transactionDetailsCollection: ITransactionDetails[] = [{ id: 123 }];
        expectedResult = service.addTransactionDetailsToCollectionIfMissing(transactionDetailsCollection, ...transactionDetailsArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const transactionDetails: ITransactionDetails = { id: 123 };
        const transactionDetails2: ITransactionDetails = { id: 456 };
        expectedResult = service.addTransactionDetailsToCollectionIfMissing([], transactionDetails, transactionDetails2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(transactionDetails);
        expect(expectedResult).toContain(transactionDetails2);
      });

      it('should accept null and undefined values', () => {
        const transactionDetails: ITransactionDetails = { id: 123 };
        expectedResult = service.addTransactionDetailsToCollectionIfMissing([], null, transactionDetails, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(transactionDetails);
      });

      it('should return initial array if no TransactionDetails is added', () => {
        const transactionDetailsCollection: ITransactionDetails[] = [{ id: 123 }];
        expectedResult = service.addTransactionDetailsToCollectionIfMissing(transactionDetailsCollection, undefined, null);
        expect(expectedResult).toEqual(transactionDetailsCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
