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

import { IBankTransactionType, BankTransactionType } from '../bank-transaction-type.model';

import { BankTransactionTypeService } from './bank-transaction-type.service';

describe('BankTransactionType Service', () => {
  let service: BankTransactionTypeService;
  let httpMock: HttpTestingController;
  let elemDefault: IBankTransactionType;
  let expectedResult: IBankTransactionType | IBankTransactionType[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(BankTransactionTypeService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      transactionTypeCode: 'AAAAAAA',
      transactionTypeDetails: 'AAAAAAA',
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

    it('should create a BankTransactionType', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new BankTransactionType()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a BankTransactionType', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          transactionTypeCode: 'BBBBBB',
          transactionTypeDetails: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a BankTransactionType', () => {
      const patchObject = Object.assign(
        {
          transactionTypeCode: 'BBBBBB',
        },
        new BankTransactionType()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of BankTransactionType', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          transactionTypeCode: 'BBBBBB',
          transactionTypeDetails: 'BBBBBB',
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

    it('should delete a BankTransactionType', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addBankTransactionTypeToCollectionIfMissing', () => {
      it('should add a BankTransactionType to an empty array', () => {
        const bankTransactionType: IBankTransactionType = { id: 123 };
        expectedResult = service.addBankTransactionTypeToCollectionIfMissing([], bankTransactionType);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(bankTransactionType);
      });

      it('should not add a BankTransactionType to an array that contains it', () => {
        const bankTransactionType: IBankTransactionType = { id: 123 };
        const bankTransactionTypeCollection: IBankTransactionType[] = [
          {
            ...bankTransactionType,
          },
          { id: 456 },
        ];
        expectedResult = service.addBankTransactionTypeToCollectionIfMissing(bankTransactionTypeCollection, bankTransactionType);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a BankTransactionType to an array that doesn't contain it", () => {
        const bankTransactionType: IBankTransactionType = { id: 123 };
        const bankTransactionTypeCollection: IBankTransactionType[] = [{ id: 456 }];
        expectedResult = service.addBankTransactionTypeToCollectionIfMissing(bankTransactionTypeCollection, bankTransactionType);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(bankTransactionType);
      });

      it('should add only unique BankTransactionType to an array', () => {
        const bankTransactionTypeArray: IBankTransactionType[] = [{ id: 123 }, { id: 456 }, { id: 58619 }];
        const bankTransactionTypeCollection: IBankTransactionType[] = [{ id: 123 }];
        expectedResult = service.addBankTransactionTypeToCollectionIfMissing(bankTransactionTypeCollection, ...bankTransactionTypeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const bankTransactionType: IBankTransactionType = { id: 123 };
        const bankTransactionType2: IBankTransactionType = { id: 456 };
        expectedResult = service.addBankTransactionTypeToCollectionIfMissing([], bankTransactionType, bankTransactionType2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(bankTransactionType);
        expect(expectedResult).toContain(bankTransactionType2);
      });

      it('should accept null and undefined values', () => {
        const bankTransactionType: IBankTransactionType = { id: 123 };
        expectedResult = service.addBankTransactionTypeToCollectionIfMissing([], null, bankTransactionType, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(bankTransactionType);
      });

      it('should return initial array if no BankTransactionType is added', () => {
        const bankTransactionTypeCollection: IBankTransactionType[] = [{ id: 123 }];
        expectedResult = service.addBankTransactionTypeToCollectionIfMissing(bankTransactionTypeCollection, undefined, null);
        expect(expectedResult).toEqual(bankTransactionTypeCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
