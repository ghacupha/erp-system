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

import { IAccountType, AccountType } from '../account-type.model';

import { AccountTypeService } from './account-type.service';

describe('AccountType Service', () => {
  let service: AccountTypeService;
  let httpMock: HttpTestingController;
  let elemDefault: IAccountType;
  let expectedResult: IAccountType | IAccountType[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(AccountTypeService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      accountTypeCode: 'AAAAAAA',
      accountType: 'AAAAAAA',
      description: 'AAAAAAA',
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

    it('should create a AccountType', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new AccountType()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a AccountType', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          accountTypeCode: 'BBBBBB',
          accountType: 'BBBBBB',
          description: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a AccountType', () => {
      const patchObject = Object.assign(
        {
          accountTypeCode: 'BBBBBB',
          accountType: 'BBBBBB',
        },
        new AccountType()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of AccountType', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          accountTypeCode: 'BBBBBB',
          accountType: 'BBBBBB',
          description: 'BBBBBB',
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

    it('should delete a AccountType', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addAccountTypeToCollectionIfMissing', () => {
      it('should add a AccountType to an empty array', () => {
        const accountType: IAccountType = { id: 123 };
        expectedResult = service.addAccountTypeToCollectionIfMissing([], accountType);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(accountType);
      });

      it('should not add a AccountType to an array that contains it', () => {
        const accountType: IAccountType = { id: 123 };
        const accountTypeCollection: IAccountType[] = [
          {
            ...accountType,
          },
          { id: 456 },
        ];
        expectedResult = service.addAccountTypeToCollectionIfMissing(accountTypeCollection, accountType);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a AccountType to an array that doesn't contain it", () => {
        const accountType: IAccountType = { id: 123 };
        const accountTypeCollection: IAccountType[] = [{ id: 456 }];
        expectedResult = service.addAccountTypeToCollectionIfMissing(accountTypeCollection, accountType);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(accountType);
      });

      it('should add only unique AccountType to an array', () => {
        const accountTypeArray: IAccountType[] = [{ id: 123 }, { id: 456 }, { id: 71983 }];
        const accountTypeCollection: IAccountType[] = [{ id: 123 }];
        expectedResult = service.addAccountTypeToCollectionIfMissing(accountTypeCollection, ...accountTypeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const accountType: IAccountType = { id: 123 };
        const accountType2: IAccountType = { id: 456 };
        expectedResult = service.addAccountTypeToCollectionIfMissing([], accountType, accountType2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(accountType);
        expect(expectedResult).toContain(accountType2);
      });

      it('should accept null and undefined values', () => {
        const accountType: IAccountType = { id: 123 };
        expectedResult = service.addAccountTypeToCollectionIfMissing([], null, accountType, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(accountType);
      });

      it('should return initial array if no AccountType is added', () => {
        const accountTypeCollection: IAccountType[] = [{ id: 123 }];
        expectedResult = service.addAccountTypeToCollectionIfMissing(accountTypeCollection, undefined, null);
        expect(expectedResult).toEqual(accountTypeCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
