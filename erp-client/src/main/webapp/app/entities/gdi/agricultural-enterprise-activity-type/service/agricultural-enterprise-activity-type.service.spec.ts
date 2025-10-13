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

import { IAgriculturalEnterpriseActivityType, AgriculturalEnterpriseActivityType } from '../agricultural-enterprise-activity-type.model';

import { AgriculturalEnterpriseActivityTypeService } from './agricultural-enterprise-activity-type.service';

describe('AgriculturalEnterpriseActivityType Service', () => {
  let service: AgriculturalEnterpriseActivityTypeService;
  let httpMock: HttpTestingController;
  let elemDefault: IAgriculturalEnterpriseActivityType;
  let expectedResult: IAgriculturalEnterpriseActivityType | IAgriculturalEnterpriseActivityType[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(AgriculturalEnterpriseActivityTypeService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      agriculturalEnterpriseActivityTypeCode: 'AAAAAAA',
      agriculturalEnterpriseActivityType: 'AAAAAAA',
      agriculturalEnterpriseActivityTypeDescription: 'AAAAAAA',
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

    it('should create a AgriculturalEnterpriseActivityType', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new AgriculturalEnterpriseActivityType()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a AgriculturalEnterpriseActivityType', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          agriculturalEnterpriseActivityTypeCode: 'BBBBBB',
          agriculturalEnterpriseActivityType: 'BBBBBB',
          agriculturalEnterpriseActivityTypeDescription: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a AgriculturalEnterpriseActivityType', () => {
      const patchObject = Object.assign(
        {
          agriculturalEnterpriseActivityTypeDescription: 'BBBBBB',
        },
        new AgriculturalEnterpriseActivityType()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of AgriculturalEnterpriseActivityType', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          agriculturalEnterpriseActivityTypeCode: 'BBBBBB',
          agriculturalEnterpriseActivityType: 'BBBBBB',
          agriculturalEnterpriseActivityTypeDescription: 'BBBBBB',
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

    it('should delete a AgriculturalEnterpriseActivityType', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addAgriculturalEnterpriseActivityTypeToCollectionIfMissing', () => {
      it('should add a AgriculturalEnterpriseActivityType to an empty array', () => {
        const agriculturalEnterpriseActivityType: IAgriculturalEnterpriseActivityType = { id: 123 };
        expectedResult = service.addAgriculturalEnterpriseActivityTypeToCollectionIfMissing([], agriculturalEnterpriseActivityType);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(agriculturalEnterpriseActivityType);
      });

      it('should not add a AgriculturalEnterpriseActivityType to an array that contains it', () => {
        const agriculturalEnterpriseActivityType: IAgriculturalEnterpriseActivityType = { id: 123 };
        const agriculturalEnterpriseActivityTypeCollection: IAgriculturalEnterpriseActivityType[] = [
          {
            ...agriculturalEnterpriseActivityType,
          },
          { id: 456 },
        ];
        expectedResult = service.addAgriculturalEnterpriseActivityTypeToCollectionIfMissing(
          agriculturalEnterpriseActivityTypeCollection,
          agriculturalEnterpriseActivityType
        );
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a AgriculturalEnterpriseActivityType to an array that doesn't contain it", () => {
        const agriculturalEnterpriseActivityType: IAgriculturalEnterpriseActivityType = { id: 123 };
        const agriculturalEnterpriseActivityTypeCollection: IAgriculturalEnterpriseActivityType[] = [{ id: 456 }];
        expectedResult = service.addAgriculturalEnterpriseActivityTypeToCollectionIfMissing(
          agriculturalEnterpriseActivityTypeCollection,
          agriculturalEnterpriseActivityType
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(agriculturalEnterpriseActivityType);
      });

      it('should add only unique AgriculturalEnterpriseActivityType to an array', () => {
        const agriculturalEnterpriseActivityTypeArray: IAgriculturalEnterpriseActivityType[] = [{ id: 123 }, { id: 456 }, { id: 28649 }];
        const agriculturalEnterpriseActivityTypeCollection: IAgriculturalEnterpriseActivityType[] = [{ id: 123 }];
        expectedResult = service.addAgriculturalEnterpriseActivityTypeToCollectionIfMissing(
          agriculturalEnterpriseActivityTypeCollection,
          ...agriculturalEnterpriseActivityTypeArray
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const agriculturalEnterpriseActivityType: IAgriculturalEnterpriseActivityType = { id: 123 };
        const agriculturalEnterpriseActivityType2: IAgriculturalEnterpriseActivityType = { id: 456 };
        expectedResult = service.addAgriculturalEnterpriseActivityTypeToCollectionIfMissing(
          [],
          agriculturalEnterpriseActivityType,
          agriculturalEnterpriseActivityType2
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(agriculturalEnterpriseActivityType);
        expect(expectedResult).toContain(agriculturalEnterpriseActivityType2);
      });

      it('should accept null and undefined values', () => {
        const agriculturalEnterpriseActivityType: IAgriculturalEnterpriseActivityType = { id: 123 };
        expectedResult = service.addAgriculturalEnterpriseActivityTypeToCollectionIfMissing(
          [],
          null,
          agriculturalEnterpriseActivityType,
          undefined
        );
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(agriculturalEnterpriseActivityType);
      });

      it('should return initial array if no AgriculturalEnterpriseActivityType is added', () => {
        const agriculturalEnterpriseActivityTypeCollection: IAgriculturalEnterpriseActivityType[] = [{ id: 123 }];
        expectedResult = service.addAgriculturalEnterpriseActivityTypeToCollectionIfMissing(
          agriculturalEnterpriseActivityTypeCollection,
          undefined,
          null
        );
        expect(expectedResult).toEqual(agriculturalEnterpriseActivityTypeCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
