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

import { ILoanProductType, LoanProductType } from '../loan-product-type.model';

import { LoanProductTypeService } from './loan-product-type.service';

describe('LoanProductType Service', () => {
  let service: LoanProductTypeService;
  let httpMock: HttpTestingController;
  let elemDefault: ILoanProductType;
  let expectedResult: ILoanProductType | ILoanProductType[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(LoanProductTypeService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      productCode: 'AAAAAAA',
      productType: 'AAAAAAA',
      productTypeDescription: 'AAAAAAA',
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

    it('should create a LoanProductType', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new LoanProductType()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a LoanProductType', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          productCode: 'BBBBBB',
          productType: 'BBBBBB',
          productTypeDescription: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a LoanProductType', () => {
      const patchObject = Object.assign({}, new LoanProductType());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of LoanProductType', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          productCode: 'BBBBBB',
          productType: 'BBBBBB',
          productTypeDescription: 'BBBBBB',
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

    it('should delete a LoanProductType', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addLoanProductTypeToCollectionIfMissing', () => {
      it('should add a LoanProductType to an empty array', () => {
        const loanProductType: ILoanProductType = { id: 123 };
        expectedResult = service.addLoanProductTypeToCollectionIfMissing([], loanProductType);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(loanProductType);
      });

      it('should not add a LoanProductType to an array that contains it', () => {
        const loanProductType: ILoanProductType = { id: 123 };
        const loanProductTypeCollection: ILoanProductType[] = [
          {
            ...loanProductType,
          },
          { id: 456 },
        ];
        expectedResult = service.addLoanProductTypeToCollectionIfMissing(loanProductTypeCollection, loanProductType);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a LoanProductType to an array that doesn't contain it", () => {
        const loanProductType: ILoanProductType = { id: 123 };
        const loanProductTypeCollection: ILoanProductType[] = [{ id: 456 }];
        expectedResult = service.addLoanProductTypeToCollectionIfMissing(loanProductTypeCollection, loanProductType);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(loanProductType);
      });

      it('should add only unique LoanProductType to an array', () => {
        const loanProductTypeArray: ILoanProductType[] = [{ id: 123 }, { id: 456 }, { id: 24041 }];
        const loanProductTypeCollection: ILoanProductType[] = [{ id: 123 }];
        expectedResult = service.addLoanProductTypeToCollectionIfMissing(loanProductTypeCollection, ...loanProductTypeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const loanProductType: ILoanProductType = { id: 123 };
        const loanProductType2: ILoanProductType = { id: 456 };
        expectedResult = service.addLoanProductTypeToCollectionIfMissing([], loanProductType, loanProductType2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(loanProductType);
        expect(expectedResult).toContain(loanProductType2);
      });

      it('should accept null and undefined values', () => {
        const loanProductType: ILoanProductType = { id: 123 };
        expectedResult = service.addLoanProductTypeToCollectionIfMissing([], null, loanProductType, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(loanProductType);
      });

      it('should return initial array if no LoanProductType is added', () => {
        const loanProductTypeCollection: ILoanProductType[] = [{ id: 123 }];
        expectedResult = service.addLoanProductTypeToCollectionIfMissing(loanProductTypeCollection, undefined, null);
        expect(expectedResult).toEqual(loanProductTypeCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
