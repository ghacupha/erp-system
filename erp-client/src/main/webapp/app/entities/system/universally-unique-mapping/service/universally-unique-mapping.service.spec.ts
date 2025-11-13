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

import { IUniversallyUniqueMapping, UniversallyUniqueMapping } from '../universally-unique-mapping.model';

import { UniversallyUniqueMappingService } from './universally-unique-mapping.service';

describe('UniversallyUniqueMapping Service', () => {
  let service: UniversallyUniqueMappingService;
  let httpMock: HttpTestingController;
  let elemDefault: IUniversallyUniqueMapping;
  let expectedResult: IUniversallyUniqueMapping | IUniversallyUniqueMapping[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(UniversallyUniqueMappingService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      universalKey: 'AAAAAAA',
      mappedValue: 'AAAAAAA',
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

    it('should create a UniversallyUniqueMapping', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new UniversallyUniqueMapping()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a UniversallyUniqueMapping', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          universalKey: 'BBBBBB',
          mappedValue: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a UniversallyUniqueMapping', () => {
      const patchObject = Object.assign(
        {
          universalKey: 'BBBBBB',
        },
        new UniversallyUniqueMapping()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of UniversallyUniqueMapping', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          universalKey: 'BBBBBB',
          mappedValue: 'BBBBBB',
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

    it('should delete a UniversallyUniqueMapping', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addUniversallyUniqueMappingToCollectionIfMissing', () => {
      it('should add a UniversallyUniqueMapping to an empty array', () => {
        const universallyUniqueMapping: IUniversallyUniqueMapping = { id: 123 };
        expectedResult = service.addUniversallyUniqueMappingToCollectionIfMissing([], universallyUniqueMapping);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(universallyUniqueMapping);
      });

      it('should not add a UniversallyUniqueMapping to an array that contains it', () => {
        const universallyUniqueMapping: IUniversallyUniqueMapping = { id: 123 };
        const universallyUniqueMappingCollection: IUniversallyUniqueMapping[] = [
          {
            ...universallyUniqueMapping,
          },
          { id: 456 },
        ];
        expectedResult = service.addUniversallyUniqueMappingToCollectionIfMissing(
          universallyUniqueMappingCollection,
          universallyUniqueMapping
        );
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a UniversallyUniqueMapping to an array that doesn't contain it", () => {
        const universallyUniqueMapping: IUniversallyUniqueMapping = { id: 123 };
        const universallyUniqueMappingCollection: IUniversallyUniqueMapping[] = [{ id: 456 }];
        expectedResult = service.addUniversallyUniqueMappingToCollectionIfMissing(
          universallyUniqueMappingCollection,
          universallyUniqueMapping
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(universallyUniqueMapping);
      });

      it('should add only unique UniversallyUniqueMapping to an array', () => {
        const universallyUniqueMappingArray: IUniversallyUniqueMapping[] = [{ id: 123 }, { id: 456 }, { id: 68837 }];
        const universallyUniqueMappingCollection: IUniversallyUniqueMapping[] = [{ id: 123 }];
        expectedResult = service.addUniversallyUniqueMappingToCollectionIfMissing(
          universallyUniqueMappingCollection,
          ...universallyUniqueMappingArray
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const universallyUniqueMapping: IUniversallyUniqueMapping = { id: 123 };
        const universallyUniqueMapping2: IUniversallyUniqueMapping = { id: 456 };
        expectedResult = service.addUniversallyUniqueMappingToCollectionIfMissing([], universallyUniqueMapping, universallyUniqueMapping2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(universallyUniqueMapping);
        expect(expectedResult).toContain(universallyUniqueMapping2);
      });

      it('should accept null and undefined values', () => {
        const universallyUniqueMapping: IUniversallyUniqueMapping = { id: 123 };
        expectedResult = service.addUniversallyUniqueMappingToCollectionIfMissing([], null, universallyUniqueMapping, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(universallyUniqueMapping);
      });

      it('should return initial array if no UniversallyUniqueMapping is added', () => {
        const universallyUniqueMappingCollection: IUniversallyUniqueMapping[] = [{ id: 123 }];
        expectedResult = service.addUniversallyUniqueMappingToCollectionIfMissing(universallyUniqueMappingCollection, undefined, null);
        expect(expectedResult).toEqual(universallyUniqueMappingCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
