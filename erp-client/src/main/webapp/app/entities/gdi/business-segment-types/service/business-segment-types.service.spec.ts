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

import { IBusinessSegmentTypes, BusinessSegmentTypes } from '../business-segment-types.model';

import { BusinessSegmentTypesService } from './business-segment-types.service';

describe('BusinessSegmentTypes Service', () => {
  let service: BusinessSegmentTypesService;
  let httpMock: HttpTestingController;
  let elemDefault: IBusinessSegmentTypes;
  let expectedResult: IBusinessSegmentTypes | IBusinessSegmentTypes[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(BusinessSegmentTypesService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      businessEconomicSegmentCode: 'AAAAAAA',
      businessEconomicSegment: 'AAAAAAA',
      details: 'AAAAAAA',
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

    it('should create a BusinessSegmentTypes', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new BusinessSegmentTypes()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a BusinessSegmentTypes', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          businessEconomicSegmentCode: 'BBBBBB',
          businessEconomicSegment: 'BBBBBB',
          details: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a BusinessSegmentTypes', () => {
      const patchObject = Object.assign(
        {
          businessEconomicSegment: 'BBBBBB',
        },
        new BusinessSegmentTypes()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of BusinessSegmentTypes', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          businessEconomicSegmentCode: 'BBBBBB',
          businessEconomicSegment: 'BBBBBB',
          details: 'BBBBBB',
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

    it('should delete a BusinessSegmentTypes', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addBusinessSegmentTypesToCollectionIfMissing', () => {
      it('should add a BusinessSegmentTypes to an empty array', () => {
        const businessSegmentTypes: IBusinessSegmentTypes = { id: 123 };
        expectedResult = service.addBusinessSegmentTypesToCollectionIfMissing([], businessSegmentTypes);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(businessSegmentTypes);
      });

      it('should not add a BusinessSegmentTypes to an array that contains it', () => {
        const businessSegmentTypes: IBusinessSegmentTypes = { id: 123 };
        const businessSegmentTypesCollection: IBusinessSegmentTypes[] = [
          {
            ...businessSegmentTypes,
          },
          { id: 456 },
        ];
        expectedResult = service.addBusinessSegmentTypesToCollectionIfMissing(businessSegmentTypesCollection, businessSegmentTypes);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a BusinessSegmentTypes to an array that doesn't contain it", () => {
        const businessSegmentTypes: IBusinessSegmentTypes = { id: 123 };
        const businessSegmentTypesCollection: IBusinessSegmentTypes[] = [{ id: 456 }];
        expectedResult = service.addBusinessSegmentTypesToCollectionIfMissing(businessSegmentTypesCollection, businessSegmentTypes);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(businessSegmentTypes);
      });

      it('should add only unique BusinessSegmentTypes to an array', () => {
        const businessSegmentTypesArray: IBusinessSegmentTypes[] = [{ id: 123 }, { id: 456 }, { id: 99018 }];
        const businessSegmentTypesCollection: IBusinessSegmentTypes[] = [{ id: 123 }];
        expectedResult = service.addBusinessSegmentTypesToCollectionIfMissing(businessSegmentTypesCollection, ...businessSegmentTypesArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const businessSegmentTypes: IBusinessSegmentTypes = { id: 123 };
        const businessSegmentTypes2: IBusinessSegmentTypes = { id: 456 };
        expectedResult = service.addBusinessSegmentTypesToCollectionIfMissing([], businessSegmentTypes, businessSegmentTypes2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(businessSegmentTypes);
        expect(expectedResult).toContain(businessSegmentTypes2);
      });

      it('should accept null and undefined values', () => {
        const businessSegmentTypes: IBusinessSegmentTypes = { id: 123 };
        expectedResult = service.addBusinessSegmentTypesToCollectionIfMissing([], null, businessSegmentTypes, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(businessSegmentTypes);
      });

      it('should return initial array if no BusinessSegmentTypes is added', () => {
        const businessSegmentTypesCollection: IBusinessSegmentTypes[] = [{ id: 123 }];
        expectedResult = service.addBusinessSegmentTypesToCollectionIfMissing(businessSegmentTypesCollection, undefined, null);
        expect(expectedResult).toEqual(businessSegmentTypesCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
