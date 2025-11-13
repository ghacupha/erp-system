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

import { IFinancialDerivativeTypeCode, FinancialDerivativeTypeCode } from '../financial-derivative-type-code.model';

import { FinancialDerivativeTypeCodeService } from './financial-derivative-type-code.service';

describe('FinancialDerivativeTypeCode Service', () => {
  let service: FinancialDerivativeTypeCodeService;
  let httpMock: HttpTestingController;
  let elemDefault: IFinancialDerivativeTypeCode;
  let expectedResult: IFinancialDerivativeTypeCode | IFinancialDerivativeTypeCode[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(FinancialDerivativeTypeCodeService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      financialDerivativeTypeCode: 'AAAAAAA',
      financialDerivativeType: 'AAAAAAA',
      financialDerivativeTypeDetails: 'AAAAAAA',
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

    it('should create a FinancialDerivativeTypeCode', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new FinancialDerivativeTypeCode()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a FinancialDerivativeTypeCode', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          financialDerivativeTypeCode: 'BBBBBB',
          financialDerivativeType: 'BBBBBB',
          financialDerivativeTypeDetails: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a FinancialDerivativeTypeCode', () => {
      const patchObject = Object.assign(
        {
          financialDerivativeType: 'BBBBBB',
        },
        new FinancialDerivativeTypeCode()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of FinancialDerivativeTypeCode', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          financialDerivativeTypeCode: 'BBBBBB',
          financialDerivativeType: 'BBBBBB',
          financialDerivativeTypeDetails: 'BBBBBB',
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

    it('should delete a FinancialDerivativeTypeCode', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addFinancialDerivativeTypeCodeToCollectionIfMissing', () => {
      it('should add a FinancialDerivativeTypeCode to an empty array', () => {
        const financialDerivativeTypeCode: IFinancialDerivativeTypeCode = { id: 123 };
        expectedResult = service.addFinancialDerivativeTypeCodeToCollectionIfMissing([], financialDerivativeTypeCode);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(financialDerivativeTypeCode);
      });

      it('should not add a FinancialDerivativeTypeCode to an array that contains it', () => {
        const financialDerivativeTypeCode: IFinancialDerivativeTypeCode = { id: 123 };
        const financialDerivativeTypeCodeCollection: IFinancialDerivativeTypeCode[] = [
          {
            ...financialDerivativeTypeCode,
          },
          { id: 456 },
        ];
        expectedResult = service.addFinancialDerivativeTypeCodeToCollectionIfMissing(
          financialDerivativeTypeCodeCollection,
          financialDerivativeTypeCode
        );
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a FinancialDerivativeTypeCode to an array that doesn't contain it", () => {
        const financialDerivativeTypeCode: IFinancialDerivativeTypeCode = { id: 123 };
        const financialDerivativeTypeCodeCollection: IFinancialDerivativeTypeCode[] = [{ id: 456 }];
        expectedResult = service.addFinancialDerivativeTypeCodeToCollectionIfMissing(
          financialDerivativeTypeCodeCollection,
          financialDerivativeTypeCode
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(financialDerivativeTypeCode);
      });

      it('should add only unique FinancialDerivativeTypeCode to an array', () => {
        const financialDerivativeTypeCodeArray: IFinancialDerivativeTypeCode[] = [{ id: 123 }, { id: 456 }, { id: 26626 }];
        const financialDerivativeTypeCodeCollection: IFinancialDerivativeTypeCode[] = [{ id: 123 }];
        expectedResult = service.addFinancialDerivativeTypeCodeToCollectionIfMissing(
          financialDerivativeTypeCodeCollection,
          ...financialDerivativeTypeCodeArray
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const financialDerivativeTypeCode: IFinancialDerivativeTypeCode = { id: 123 };
        const financialDerivativeTypeCode2: IFinancialDerivativeTypeCode = { id: 456 };
        expectedResult = service.addFinancialDerivativeTypeCodeToCollectionIfMissing(
          [],
          financialDerivativeTypeCode,
          financialDerivativeTypeCode2
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(financialDerivativeTypeCode);
        expect(expectedResult).toContain(financialDerivativeTypeCode2);
      });

      it('should accept null and undefined values', () => {
        const financialDerivativeTypeCode: IFinancialDerivativeTypeCode = { id: 123 };
        expectedResult = service.addFinancialDerivativeTypeCodeToCollectionIfMissing([], null, financialDerivativeTypeCode, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(financialDerivativeTypeCode);
      });

      it('should return initial array if no FinancialDerivativeTypeCode is added', () => {
        const financialDerivativeTypeCodeCollection: IFinancialDerivativeTypeCode[] = [{ id: 123 }];
        expectedResult = service.addFinancialDerivativeTypeCodeToCollectionIfMissing(
          financialDerivativeTypeCodeCollection,
          undefined,
          null
        );
        expect(expectedResult).toEqual(financialDerivativeTypeCodeCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
