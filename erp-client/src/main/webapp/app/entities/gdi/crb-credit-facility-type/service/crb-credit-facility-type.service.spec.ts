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

import { ICrbCreditFacilityType, CrbCreditFacilityType } from '../crb-credit-facility-type.model';

import { CrbCreditFacilityTypeService } from './crb-credit-facility-type.service';

describe('CrbCreditFacilityType Service', () => {
  let service: CrbCreditFacilityTypeService;
  let httpMock: HttpTestingController;
  let elemDefault: ICrbCreditFacilityType;
  let expectedResult: ICrbCreditFacilityType | ICrbCreditFacilityType[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CrbCreditFacilityTypeService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      creditFacilityTypeCode: 'AAAAAAA',
      creditFacilityType: 'AAAAAAA',
      creditFacilityDescription: 'AAAAAAA',
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

    it('should create a CrbCreditFacilityType', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new CrbCreditFacilityType()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CrbCreditFacilityType', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          creditFacilityTypeCode: 'BBBBBB',
          creditFacilityType: 'BBBBBB',
          creditFacilityDescription: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a CrbCreditFacilityType', () => {
      const patchObject = Object.assign(
        {
          creditFacilityDescription: 'BBBBBB',
        },
        new CrbCreditFacilityType()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of CrbCreditFacilityType', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          creditFacilityTypeCode: 'BBBBBB',
          creditFacilityType: 'BBBBBB',
          creditFacilityDescription: 'BBBBBB',
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

    it('should delete a CrbCreditFacilityType', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addCrbCreditFacilityTypeToCollectionIfMissing', () => {
      it('should add a CrbCreditFacilityType to an empty array', () => {
        const crbCreditFacilityType: ICrbCreditFacilityType = { id: 123 };
        expectedResult = service.addCrbCreditFacilityTypeToCollectionIfMissing([], crbCreditFacilityType);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(crbCreditFacilityType);
      });

      it('should not add a CrbCreditFacilityType to an array that contains it', () => {
        const crbCreditFacilityType: ICrbCreditFacilityType = { id: 123 };
        const crbCreditFacilityTypeCollection: ICrbCreditFacilityType[] = [
          {
            ...crbCreditFacilityType,
          },
          { id: 456 },
        ];
        expectedResult = service.addCrbCreditFacilityTypeToCollectionIfMissing(crbCreditFacilityTypeCollection, crbCreditFacilityType);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CrbCreditFacilityType to an array that doesn't contain it", () => {
        const crbCreditFacilityType: ICrbCreditFacilityType = { id: 123 };
        const crbCreditFacilityTypeCollection: ICrbCreditFacilityType[] = [{ id: 456 }];
        expectedResult = service.addCrbCreditFacilityTypeToCollectionIfMissing(crbCreditFacilityTypeCollection, crbCreditFacilityType);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(crbCreditFacilityType);
      });

      it('should add only unique CrbCreditFacilityType to an array', () => {
        const crbCreditFacilityTypeArray: ICrbCreditFacilityType[] = [{ id: 123 }, { id: 456 }, { id: 86876 }];
        const crbCreditFacilityTypeCollection: ICrbCreditFacilityType[] = [{ id: 123 }];
        expectedResult = service.addCrbCreditFacilityTypeToCollectionIfMissing(
          crbCreditFacilityTypeCollection,
          ...crbCreditFacilityTypeArray
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const crbCreditFacilityType: ICrbCreditFacilityType = { id: 123 };
        const crbCreditFacilityType2: ICrbCreditFacilityType = { id: 456 };
        expectedResult = service.addCrbCreditFacilityTypeToCollectionIfMissing([], crbCreditFacilityType, crbCreditFacilityType2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(crbCreditFacilityType);
        expect(expectedResult).toContain(crbCreditFacilityType2);
      });

      it('should accept null and undefined values', () => {
        const crbCreditFacilityType: ICrbCreditFacilityType = { id: 123 };
        expectedResult = service.addCrbCreditFacilityTypeToCollectionIfMissing([], null, crbCreditFacilityType, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(crbCreditFacilityType);
      });

      it('should return initial array if no CrbCreditFacilityType is added', () => {
        const crbCreditFacilityTypeCollection: ICrbCreditFacilityType[] = [{ id: 123 }];
        expectedResult = service.addCrbCreditFacilityTypeToCollectionIfMissing(crbCreditFacilityTypeCollection, undefined, null);
        expect(expectedResult).toEqual(crbCreditFacilityTypeCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
