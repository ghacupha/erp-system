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

import { IPrepaymentMapping, PrepaymentMapping } from '../prepayment-mapping.model';

import { PrepaymentMappingService } from './prepayment-mapping.service';

describe('PrepaymentMapping Service', () => {
  let service: PrepaymentMappingService;
  let httpMock: HttpTestingController;
  let elemDefault: IPrepaymentMapping;
  let expectedResult: IPrepaymentMapping | IPrepaymentMapping[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(PrepaymentMappingService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      parameterKey: 'AAAAAAA',
      parameterGuid: 'AAAAAAA',
      parameter: 'AAAAAAA',
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

    it('should create a PrepaymentMapping', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new PrepaymentMapping()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a PrepaymentMapping', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          parameterKey: 'BBBBBB',
          parameterGuid: 'BBBBBB',
          parameter: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a PrepaymentMapping', () => {
      const patchObject = Object.assign(
        {
          parameterKey: 'BBBBBB',
          parameterGuid: 'BBBBBB',
          parameter: 'BBBBBB',
        },
        new PrepaymentMapping()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of PrepaymentMapping', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          parameterKey: 'BBBBBB',
          parameterGuid: 'BBBBBB',
          parameter: 'BBBBBB',
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

    it('should delete a PrepaymentMapping', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addPrepaymentMappingToCollectionIfMissing', () => {
      it('should add a PrepaymentMapping to an empty array', () => {
        const prepaymentMapping: IPrepaymentMapping = { id: 123 };
        expectedResult = service.addPrepaymentMappingToCollectionIfMissing([], prepaymentMapping);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(prepaymentMapping);
      });

      it('should not add a PrepaymentMapping to an array that contains it', () => {
        const prepaymentMapping: IPrepaymentMapping = { id: 123 };
        const prepaymentMappingCollection: IPrepaymentMapping[] = [
          {
            ...prepaymentMapping,
          },
          { id: 456 },
        ];
        expectedResult = service.addPrepaymentMappingToCollectionIfMissing(prepaymentMappingCollection, prepaymentMapping);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a PrepaymentMapping to an array that doesn't contain it", () => {
        const prepaymentMapping: IPrepaymentMapping = { id: 123 };
        const prepaymentMappingCollection: IPrepaymentMapping[] = [{ id: 456 }];
        expectedResult = service.addPrepaymentMappingToCollectionIfMissing(prepaymentMappingCollection, prepaymentMapping);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(prepaymentMapping);
      });

      it('should add only unique PrepaymentMapping to an array', () => {
        const prepaymentMappingArray: IPrepaymentMapping[] = [{ id: 123 }, { id: 456 }, { id: 50496 }];
        const prepaymentMappingCollection: IPrepaymentMapping[] = [{ id: 123 }];
        expectedResult = service.addPrepaymentMappingToCollectionIfMissing(prepaymentMappingCollection, ...prepaymentMappingArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const prepaymentMapping: IPrepaymentMapping = { id: 123 };
        const prepaymentMapping2: IPrepaymentMapping = { id: 456 };
        expectedResult = service.addPrepaymentMappingToCollectionIfMissing([], prepaymentMapping, prepaymentMapping2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(prepaymentMapping);
        expect(expectedResult).toContain(prepaymentMapping2);
      });

      it('should accept null and undefined values', () => {
        const prepaymentMapping: IPrepaymentMapping = { id: 123 };
        expectedResult = service.addPrepaymentMappingToCollectionIfMissing([], null, prepaymentMapping, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(prepaymentMapping);
      });

      it('should return initial array if no PrepaymentMapping is added', () => {
        const prepaymentMappingCollection: IPrepaymentMapping[] = [{ id: 123 }];
        expectedResult = service.addPrepaymentMappingToCollectionIfMissing(prepaymentMappingCollection, undefined, null);
        expect(expectedResult).toEqual(prepaymentMappingCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
