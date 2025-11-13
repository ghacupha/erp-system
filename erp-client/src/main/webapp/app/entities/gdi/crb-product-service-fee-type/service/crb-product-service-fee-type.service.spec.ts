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

import { ICrbProductServiceFeeType, CrbProductServiceFeeType } from '../crb-product-service-fee-type.model';

import { CrbProductServiceFeeTypeService } from './crb-product-service-fee-type.service';

describe('CrbProductServiceFeeType Service', () => {
  let service: CrbProductServiceFeeTypeService;
  let httpMock: HttpTestingController;
  let elemDefault: ICrbProductServiceFeeType;
  let expectedResult: ICrbProductServiceFeeType | ICrbProductServiceFeeType[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CrbProductServiceFeeTypeService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      chargeTypeCode: 'AAAAAAA',
      chargeTypeDescription: 'AAAAAAA',
      chargeTypeCategory: 'AAAAAAA',
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

    it('should create a CrbProductServiceFeeType', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new CrbProductServiceFeeType()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CrbProductServiceFeeType', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          chargeTypeCode: 'BBBBBB',
          chargeTypeDescription: 'BBBBBB',
          chargeTypeCategory: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a CrbProductServiceFeeType', () => {
      const patchObject = Object.assign(
        {
          chargeTypeDescription: 'BBBBBB',
          chargeTypeCategory: 'BBBBBB',
        },
        new CrbProductServiceFeeType()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of CrbProductServiceFeeType', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          chargeTypeCode: 'BBBBBB',
          chargeTypeDescription: 'BBBBBB',
          chargeTypeCategory: 'BBBBBB',
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

    it('should delete a CrbProductServiceFeeType', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addCrbProductServiceFeeTypeToCollectionIfMissing', () => {
      it('should add a CrbProductServiceFeeType to an empty array', () => {
        const crbProductServiceFeeType: ICrbProductServiceFeeType = { id: 123 };
        expectedResult = service.addCrbProductServiceFeeTypeToCollectionIfMissing([], crbProductServiceFeeType);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(crbProductServiceFeeType);
      });

      it('should not add a CrbProductServiceFeeType to an array that contains it', () => {
        const crbProductServiceFeeType: ICrbProductServiceFeeType = { id: 123 };
        const crbProductServiceFeeTypeCollection: ICrbProductServiceFeeType[] = [
          {
            ...crbProductServiceFeeType,
          },
          { id: 456 },
        ];
        expectedResult = service.addCrbProductServiceFeeTypeToCollectionIfMissing(
          crbProductServiceFeeTypeCollection,
          crbProductServiceFeeType
        );
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CrbProductServiceFeeType to an array that doesn't contain it", () => {
        const crbProductServiceFeeType: ICrbProductServiceFeeType = { id: 123 };
        const crbProductServiceFeeTypeCollection: ICrbProductServiceFeeType[] = [{ id: 456 }];
        expectedResult = service.addCrbProductServiceFeeTypeToCollectionIfMissing(
          crbProductServiceFeeTypeCollection,
          crbProductServiceFeeType
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(crbProductServiceFeeType);
      });

      it('should add only unique CrbProductServiceFeeType to an array', () => {
        const crbProductServiceFeeTypeArray: ICrbProductServiceFeeType[] = [{ id: 123 }, { id: 456 }, { id: 2337 }];
        const crbProductServiceFeeTypeCollection: ICrbProductServiceFeeType[] = [{ id: 123 }];
        expectedResult = service.addCrbProductServiceFeeTypeToCollectionIfMissing(
          crbProductServiceFeeTypeCollection,
          ...crbProductServiceFeeTypeArray
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const crbProductServiceFeeType: ICrbProductServiceFeeType = { id: 123 };
        const crbProductServiceFeeType2: ICrbProductServiceFeeType = { id: 456 };
        expectedResult = service.addCrbProductServiceFeeTypeToCollectionIfMissing([], crbProductServiceFeeType, crbProductServiceFeeType2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(crbProductServiceFeeType);
        expect(expectedResult).toContain(crbProductServiceFeeType2);
      });

      it('should accept null and undefined values', () => {
        const crbProductServiceFeeType: ICrbProductServiceFeeType = { id: 123 };
        expectedResult = service.addCrbProductServiceFeeTypeToCollectionIfMissing([], null, crbProductServiceFeeType, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(crbProductServiceFeeType);
      });

      it('should return initial array if no CrbProductServiceFeeType is added', () => {
        const crbProductServiceFeeTypeCollection: ICrbProductServiceFeeType[] = [{ id: 123 }];
        expectedResult = service.addCrbProductServiceFeeTypeToCollectionIfMissing(crbProductServiceFeeTypeCollection, undefined, null);
        expect(expectedResult).toEqual(crbProductServiceFeeTypeCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
