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

import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IAccountBalance, AccountBalance } from '../account-balance.model';

import { AccountBalanceService } from './account-balance.service';

describe('AccountBalance Service', () => {
  let service: AccountBalanceService;
  let httpMock: HttpTestingController;
  let elemDefault: IAccountBalance;
  let expectedResult: IAccountBalance | IAccountBalance[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(AccountBalanceService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      reportingDate: currentDate,
      customerId: 'AAAAAAA',
      accountContractNumber: 'AAAAAAA',
      accruedInterestBalanceFCY: 0,
      accruedInterestBalanceLCY: 0,
      accountBalanceFCY: 0,
      accountBalanceLCY: 0,
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

    it('should create a AccountBalance', () => {
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

      service.create(new AccountBalance()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a AccountBalance', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          reportingDate: currentDate.format(DATE_FORMAT),
          customerId: 'BBBBBB',
          accountContractNumber: 'BBBBBB',
          accruedInterestBalanceFCY: 1,
          accruedInterestBalanceLCY: 1,
          accountBalanceFCY: 1,
          accountBalanceLCY: 1,
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

    it('should partial update a AccountBalance', () => {
      const patchObject = Object.assign(
        {
          reportingDate: currentDate.format(DATE_FORMAT),
          customerId: 'BBBBBB',
          accountContractNumber: 'BBBBBB',
          accruedInterestBalanceLCY: 1,
          accountBalanceFCY: 1,
          accountBalanceLCY: 1,
        },
        new AccountBalance()
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

    it('should return a list of AccountBalance', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          reportingDate: currentDate.format(DATE_FORMAT),
          customerId: 'BBBBBB',
          accountContractNumber: 'BBBBBB',
          accruedInterestBalanceFCY: 1,
          accruedInterestBalanceLCY: 1,
          accountBalanceFCY: 1,
          accountBalanceLCY: 1,
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

    it('should delete a AccountBalance', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addAccountBalanceToCollectionIfMissing', () => {
      it('should add a AccountBalance to an empty array', () => {
        const accountBalance: IAccountBalance = { id: 123 };
        expectedResult = service.addAccountBalanceToCollectionIfMissing([], accountBalance);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(accountBalance);
      });

      it('should not add a AccountBalance to an array that contains it', () => {
        const accountBalance: IAccountBalance = { id: 123 };
        const accountBalanceCollection: IAccountBalance[] = [
          {
            ...accountBalance,
          },
          { id: 456 },
        ];
        expectedResult = service.addAccountBalanceToCollectionIfMissing(accountBalanceCollection, accountBalance);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a AccountBalance to an array that doesn't contain it", () => {
        const accountBalance: IAccountBalance = { id: 123 };
        const accountBalanceCollection: IAccountBalance[] = [{ id: 456 }];
        expectedResult = service.addAccountBalanceToCollectionIfMissing(accountBalanceCollection, accountBalance);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(accountBalance);
      });

      it('should add only unique AccountBalance to an array', () => {
        const accountBalanceArray: IAccountBalance[] = [{ id: 123 }, { id: 456 }, { id: 96745 }];
        const accountBalanceCollection: IAccountBalance[] = [{ id: 123 }];
        expectedResult = service.addAccountBalanceToCollectionIfMissing(accountBalanceCollection, ...accountBalanceArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const accountBalance: IAccountBalance = { id: 123 };
        const accountBalance2: IAccountBalance = { id: 456 };
        expectedResult = service.addAccountBalanceToCollectionIfMissing([], accountBalance, accountBalance2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(accountBalance);
        expect(expectedResult).toContain(accountBalance2);
      });

      it('should accept null and undefined values', () => {
        const accountBalance: IAccountBalance = { id: 123 };
        expectedResult = service.addAccountBalanceToCollectionIfMissing([], null, accountBalance, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(accountBalance);
      });

      it('should return initial array if no AccountBalance is added', () => {
        const accountBalanceCollection: IAccountBalance[] = [{ id: 123 }];
        expectedResult = service.addAccountBalanceToCollectionIfMissing(accountBalanceCollection, undefined, null);
        expect(expectedResult).toEqual(accountBalanceCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
