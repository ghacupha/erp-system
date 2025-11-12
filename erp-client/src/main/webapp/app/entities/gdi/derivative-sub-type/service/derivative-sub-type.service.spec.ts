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

import { IDerivativeSubType, DerivativeSubType } from '../derivative-sub-type.model';

import { DerivativeSubTypeService } from './derivative-sub-type.service';

describe('DerivativeSubType Service', () => {
  let service: DerivativeSubTypeService;
  let httpMock: HttpTestingController;
  let elemDefault: IDerivativeSubType;
  let expectedResult: IDerivativeSubType | IDerivativeSubType[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(DerivativeSubTypeService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      financialDerivativeSubTypeCode: 'AAAAAAA',
      financialDerivativeSubTye: 'AAAAAAA',
      financialDerivativeSubtypeDetails: 'AAAAAAA',
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

    it('should create a DerivativeSubType', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new DerivativeSubType()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a DerivativeSubType', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          financialDerivativeSubTypeCode: 'BBBBBB',
          financialDerivativeSubTye: 'BBBBBB',
          financialDerivativeSubtypeDetails: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a DerivativeSubType', () => {
      const patchObject = Object.assign(
        {
          financialDerivativeSubtypeDetails: 'BBBBBB',
        },
        new DerivativeSubType()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of DerivativeSubType', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          financialDerivativeSubTypeCode: 'BBBBBB',
          financialDerivativeSubTye: 'BBBBBB',
          financialDerivativeSubtypeDetails: 'BBBBBB',
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

    it('should delete a DerivativeSubType', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addDerivativeSubTypeToCollectionIfMissing', () => {
      it('should add a DerivativeSubType to an empty array', () => {
        const derivativeSubType: IDerivativeSubType = { id: 123 };
        expectedResult = service.addDerivativeSubTypeToCollectionIfMissing([], derivativeSubType);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(derivativeSubType);
      });

      it('should not add a DerivativeSubType to an array that contains it', () => {
        const derivativeSubType: IDerivativeSubType = { id: 123 };
        const derivativeSubTypeCollection: IDerivativeSubType[] = [
          {
            ...derivativeSubType,
          },
          { id: 456 },
        ];
        expectedResult = service.addDerivativeSubTypeToCollectionIfMissing(derivativeSubTypeCollection, derivativeSubType);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a DerivativeSubType to an array that doesn't contain it", () => {
        const derivativeSubType: IDerivativeSubType = { id: 123 };
        const derivativeSubTypeCollection: IDerivativeSubType[] = [{ id: 456 }];
        expectedResult = service.addDerivativeSubTypeToCollectionIfMissing(derivativeSubTypeCollection, derivativeSubType);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(derivativeSubType);
      });

      it('should add only unique DerivativeSubType to an array', () => {
        const derivativeSubTypeArray: IDerivativeSubType[] = [{ id: 123 }, { id: 456 }, { id: 27649 }];
        const derivativeSubTypeCollection: IDerivativeSubType[] = [{ id: 123 }];
        expectedResult = service.addDerivativeSubTypeToCollectionIfMissing(derivativeSubTypeCollection, ...derivativeSubTypeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const derivativeSubType: IDerivativeSubType = { id: 123 };
        const derivativeSubType2: IDerivativeSubType = { id: 456 };
        expectedResult = service.addDerivativeSubTypeToCollectionIfMissing([], derivativeSubType, derivativeSubType2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(derivativeSubType);
        expect(expectedResult).toContain(derivativeSubType2);
      });

      it('should accept null and undefined values', () => {
        const derivativeSubType: IDerivativeSubType = { id: 123 };
        expectedResult = service.addDerivativeSubTypeToCollectionIfMissing([], null, derivativeSubType, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(derivativeSubType);
      });

      it('should return initial array if no DerivativeSubType is added', () => {
        const derivativeSubTypeCollection: IDerivativeSubType[] = [{ id: 123 }];
        expectedResult = service.addDerivativeSubTypeToCollectionIfMissing(derivativeSubTypeCollection, undefined, null);
        expect(expectedResult).toEqual(derivativeSubTypeCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
