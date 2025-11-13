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

import { IBankBranchCode, BankBranchCode } from '../bank-branch-code.model';

import { BankBranchCodeService } from './bank-branch-code.service';

describe('BankBranchCode Service', () => {
  let service: BankBranchCodeService;
  let httpMock: HttpTestingController;
  let elemDefault: IBankBranchCode;
  let expectedResult: IBankBranchCode | IBankBranchCode[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(BankBranchCodeService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      bankCode: 'AAAAAAA',
      bankName: 'AAAAAAA',
      branchCode: 'AAAAAAA',
      branchName: 'AAAAAAA',
      notes: 'AAAAAAA',
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

    it('should create a BankBranchCode', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new BankBranchCode()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a BankBranchCode', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          bankCode: 'BBBBBB',
          bankName: 'BBBBBB',
          branchCode: 'BBBBBB',
          branchName: 'BBBBBB',
          notes: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a BankBranchCode', () => {
      const patchObject = Object.assign(
        {
          branchName: 'BBBBBB',
          notes: 'BBBBBB',
        },
        new BankBranchCode()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of BankBranchCode', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          bankCode: 'BBBBBB',
          bankName: 'BBBBBB',
          branchCode: 'BBBBBB',
          branchName: 'BBBBBB',
          notes: 'BBBBBB',
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

    it('should delete a BankBranchCode', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addBankBranchCodeToCollectionIfMissing', () => {
      it('should add a BankBranchCode to an empty array', () => {
        const bankBranchCode: IBankBranchCode = { id: 123 };
        expectedResult = service.addBankBranchCodeToCollectionIfMissing([], bankBranchCode);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(bankBranchCode);
      });

      it('should not add a BankBranchCode to an array that contains it', () => {
        const bankBranchCode: IBankBranchCode = { id: 123 };
        const bankBranchCodeCollection: IBankBranchCode[] = [
          {
            ...bankBranchCode,
          },
          { id: 456 },
        ];
        expectedResult = service.addBankBranchCodeToCollectionIfMissing(bankBranchCodeCollection, bankBranchCode);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a BankBranchCode to an array that doesn't contain it", () => {
        const bankBranchCode: IBankBranchCode = { id: 123 };
        const bankBranchCodeCollection: IBankBranchCode[] = [{ id: 456 }];
        expectedResult = service.addBankBranchCodeToCollectionIfMissing(bankBranchCodeCollection, bankBranchCode);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(bankBranchCode);
      });

      it('should add only unique BankBranchCode to an array', () => {
        const bankBranchCodeArray: IBankBranchCode[] = [{ id: 123 }, { id: 456 }, { id: 72246 }];
        const bankBranchCodeCollection: IBankBranchCode[] = [{ id: 123 }];
        expectedResult = service.addBankBranchCodeToCollectionIfMissing(bankBranchCodeCollection, ...bankBranchCodeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const bankBranchCode: IBankBranchCode = { id: 123 };
        const bankBranchCode2: IBankBranchCode = { id: 456 };
        expectedResult = service.addBankBranchCodeToCollectionIfMissing([], bankBranchCode, bankBranchCode2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(bankBranchCode);
        expect(expectedResult).toContain(bankBranchCode2);
      });

      it('should accept null and undefined values', () => {
        const bankBranchCode: IBankBranchCode = { id: 123 };
        expectedResult = service.addBankBranchCodeToCollectionIfMissing([], null, bankBranchCode, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(bankBranchCode);
      });

      it('should return initial array if no BankBranchCode is added', () => {
        const bankBranchCodeCollection: IBankBranchCode[] = [{ id: 123 }];
        expectedResult = service.addBankBranchCodeToCollectionIfMissing(bankBranchCodeCollection, undefined, null);
        expect(expectedResult).toEqual(bankBranchCodeCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
