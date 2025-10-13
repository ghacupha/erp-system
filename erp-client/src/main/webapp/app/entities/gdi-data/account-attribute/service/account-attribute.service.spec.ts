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
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IAccountAttribute, AccountAttribute } from '../account-attribute.model';

import { AccountAttributeService } from './account-attribute.service';

describe('AccountAttribute Service', () => {
  let service: AccountAttributeService;
  let httpMock: HttpTestingController;
  let elemDefault: IAccountAttribute;
  let expectedResult: IAccountAttribute | IAccountAttribute[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(AccountAttributeService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      reportingDate: currentDate,
      customerNumber: 'AAAAAAA',
      accountContractNumber: 'AAAAAAA',
      accountName: 'AAAAAAA',
      accountOpeningDate: currentDate,
      accountClosingDate: currentDate,
      debitInterestRate: 0,
      creditInterestRate: 0,
      sanctionedAccountLimitFcy: 0,
      sanctionedAccountLimitLcy: 0,
      accountStatusChangeDate: currentDate,
      expiryDate: currentDate,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          reportingDate: currentDate.format(DATE_FORMAT),
          accountOpeningDate: currentDate.format(DATE_FORMAT),
          accountClosingDate: currentDate.format(DATE_FORMAT),
          accountStatusChangeDate: currentDate.format(DATE_FORMAT),
          expiryDate: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a AccountAttribute', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          reportingDate: currentDate.format(DATE_FORMAT),
          accountOpeningDate: currentDate.format(DATE_FORMAT),
          accountClosingDate: currentDate.format(DATE_FORMAT),
          accountStatusChangeDate: currentDate.format(DATE_FORMAT),
          expiryDate: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          reportingDate: currentDate,
          accountOpeningDate: currentDate,
          accountClosingDate: currentDate,
          accountStatusChangeDate: currentDate,
          expiryDate: currentDate,
        },
        returnedFromService
      );

      service.create(new AccountAttribute()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a AccountAttribute', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          reportingDate: currentDate.format(DATE_FORMAT),
          customerNumber: 'BBBBBB',
          accountContractNumber: 'BBBBBB',
          accountName: 'BBBBBB',
          accountOpeningDate: currentDate.format(DATE_FORMAT),
          accountClosingDate: currentDate.format(DATE_FORMAT),
          debitInterestRate: 1,
          creditInterestRate: 1,
          sanctionedAccountLimitFcy: 1,
          sanctionedAccountLimitLcy: 1,
          accountStatusChangeDate: currentDate.format(DATE_FORMAT),
          expiryDate: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          reportingDate: currentDate,
          accountOpeningDate: currentDate,
          accountClosingDate: currentDate,
          accountStatusChangeDate: currentDate,
          expiryDate: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a AccountAttribute', () => {
      const patchObject = Object.assign(
        {
          reportingDate: currentDate.format(DATE_FORMAT),
          accountContractNumber: 'BBBBBB',
          accountOpeningDate: currentDate.format(DATE_FORMAT),
          debitInterestRate: 1,
          sanctionedAccountLimitLcy: 1,
          accountStatusChangeDate: currentDate.format(DATE_FORMAT),
          expiryDate: currentDate.format(DATE_FORMAT),
        },
        new AccountAttribute()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          reportingDate: currentDate,
          accountOpeningDate: currentDate,
          accountClosingDate: currentDate,
          accountStatusChangeDate: currentDate,
          expiryDate: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of AccountAttribute', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          reportingDate: currentDate.format(DATE_FORMAT),
          customerNumber: 'BBBBBB',
          accountContractNumber: 'BBBBBB',
          accountName: 'BBBBBB',
          accountOpeningDate: currentDate.format(DATE_FORMAT),
          accountClosingDate: currentDate.format(DATE_FORMAT),
          debitInterestRate: 1,
          creditInterestRate: 1,
          sanctionedAccountLimitFcy: 1,
          sanctionedAccountLimitLcy: 1,
          accountStatusChangeDate: currentDate.format(DATE_FORMAT),
          expiryDate: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          reportingDate: currentDate,
          accountOpeningDate: currentDate,
          accountClosingDate: currentDate,
          accountStatusChangeDate: currentDate,
          expiryDate: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a AccountAttribute', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addAccountAttributeToCollectionIfMissing', () => {
      it('should add a AccountAttribute to an empty array', () => {
        const accountAttribute: IAccountAttribute = { id: 123 };
        expectedResult = service.addAccountAttributeToCollectionIfMissing([], accountAttribute);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(accountAttribute);
      });

      it('should not add a AccountAttribute to an array that contains it', () => {
        const accountAttribute: IAccountAttribute = { id: 123 };
        const accountAttributeCollection: IAccountAttribute[] = [
          {
            ...accountAttribute,
          },
          { id: 456 },
        ];
        expectedResult = service.addAccountAttributeToCollectionIfMissing(accountAttributeCollection, accountAttribute);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a AccountAttribute to an array that doesn't contain it", () => {
        const accountAttribute: IAccountAttribute = { id: 123 };
        const accountAttributeCollection: IAccountAttribute[] = [{ id: 456 }];
        expectedResult = service.addAccountAttributeToCollectionIfMissing(accountAttributeCollection, accountAttribute);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(accountAttribute);
      });

      it('should add only unique AccountAttribute to an array', () => {
        const accountAttributeArray: IAccountAttribute[] = [{ id: 123 }, { id: 456 }, { id: 74794 }];
        const accountAttributeCollection: IAccountAttribute[] = [{ id: 123 }];
        expectedResult = service.addAccountAttributeToCollectionIfMissing(accountAttributeCollection, ...accountAttributeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const accountAttribute: IAccountAttribute = { id: 123 };
        const accountAttribute2: IAccountAttribute = { id: 456 };
        expectedResult = service.addAccountAttributeToCollectionIfMissing([], accountAttribute, accountAttribute2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(accountAttribute);
        expect(expectedResult).toContain(accountAttribute2);
      });

      it('should accept null and undefined values', () => {
        const accountAttribute: IAccountAttribute = { id: 123 };
        expectedResult = service.addAccountAttributeToCollectionIfMissing([], null, accountAttribute, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(accountAttribute);
      });

      it('should return initial array if no AccountAttribute is added', () => {
        const accountAttributeCollection: IAccountAttribute[] = [{ id: 123 }];
        expectedResult = service.addAccountAttributeToCollectionIfMissing(accountAttributeCollection, undefined, null);
        expect(expectedResult).toEqual(accountAttributeCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
