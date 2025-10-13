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

import { AccountStatusTypes } from 'app/entities/enumerations/account-status-types.model';
import { IAccountStatusType, AccountStatusType } from '../account-status-type.model';

import { AccountStatusTypeService } from './account-status-type.service';

describe('AccountStatusType Service', () => {
  let service: AccountStatusTypeService;
  let httpMock: HttpTestingController;
  let elemDefault: IAccountStatusType;
  let expectedResult: IAccountStatusType | IAccountStatusType[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(AccountStatusTypeService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      accountStatusCode: 'AAAAAAA',
      accountStatusType: AccountStatusTypes.ACTIVE,
      accountStatusDescription: 'AAAAAAA',
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

    it('should create a AccountStatusType', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new AccountStatusType()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a AccountStatusType', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          accountStatusCode: 'BBBBBB',
          accountStatusType: 'BBBBBB',
          accountStatusDescription: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a AccountStatusType', () => {
      const patchObject = Object.assign(
        {
          accountStatusDescription: 'BBBBBB',
        },
        new AccountStatusType()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of AccountStatusType', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          accountStatusCode: 'BBBBBB',
          accountStatusType: 'BBBBBB',
          accountStatusDescription: 'BBBBBB',
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

    it('should delete a AccountStatusType', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addAccountStatusTypeToCollectionIfMissing', () => {
      it('should add a AccountStatusType to an empty array', () => {
        const accountStatusType: IAccountStatusType = { id: 123 };
        expectedResult = service.addAccountStatusTypeToCollectionIfMissing([], accountStatusType);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(accountStatusType);
      });

      it('should not add a AccountStatusType to an array that contains it', () => {
        const accountStatusType: IAccountStatusType = { id: 123 };
        const accountStatusTypeCollection: IAccountStatusType[] = [
          {
            ...accountStatusType,
          },
          { id: 456 },
        ];
        expectedResult = service.addAccountStatusTypeToCollectionIfMissing(accountStatusTypeCollection, accountStatusType);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a AccountStatusType to an array that doesn't contain it", () => {
        const accountStatusType: IAccountStatusType = { id: 123 };
        const accountStatusTypeCollection: IAccountStatusType[] = [{ id: 456 }];
        expectedResult = service.addAccountStatusTypeToCollectionIfMissing(accountStatusTypeCollection, accountStatusType);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(accountStatusType);
      });

      it('should add only unique AccountStatusType to an array', () => {
        const accountStatusTypeArray: IAccountStatusType[] = [{ id: 123 }, { id: 456 }, { id: 40949 }];
        const accountStatusTypeCollection: IAccountStatusType[] = [{ id: 123 }];
        expectedResult = service.addAccountStatusTypeToCollectionIfMissing(accountStatusTypeCollection, ...accountStatusTypeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const accountStatusType: IAccountStatusType = { id: 123 };
        const accountStatusType2: IAccountStatusType = { id: 456 };
        expectedResult = service.addAccountStatusTypeToCollectionIfMissing([], accountStatusType, accountStatusType2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(accountStatusType);
        expect(expectedResult).toContain(accountStatusType2);
      });

      it('should accept null and undefined values', () => {
        const accountStatusType: IAccountStatusType = { id: 123 };
        expectedResult = service.addAccountStatusTypeToCollectionIfMissing([], null, accountStatusType, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(accountStatusType);
      });

      it('should return initial array if no AccountStatusType is added', () => {
        const accountStatusTypeCollection: IAccountStatusType[] = [{ id: 123 }];
        expectedResult = service.addAccountStatusTypeToCollectionIfMissing(accountStatusTypeCollection, undefined, null);
        expect(expectedResult).toEqual(accountStatusTypeCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
