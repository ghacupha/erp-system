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

import { IGlMapping, GlMapping } from '../gl-mapping.model';

import { GlMappingService } from './gl-mapping.service';

describe('GlMapping Service', () => {
  let service: GlMappingService;
  let httpMock: HttpTestingController;
  let elemDefault: IGlMapping;
  let expectedResult: IGlMapping | IGlMapping[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(GlMappingService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      subGLCode: 'AAAAAAA',
      subGLDescription: 'AAAAAAA',
      mainGLCode: 'AAAAAAA',
      mainGLDescription: 'AAAAAAA',
      glType: 'AAAAAAA',
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

    it('should create a GlMapping', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new GlMapping()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a GlMapping', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          subGLCode: 'BBBBBB',
          subGLDescription: 'BBBBBB',
          mainGLCode: 'BBBBBB',
          mainGLDescription: 'BBBBBB',
          glType: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a GlMapping', () => {
      const patchObject = Object.assign(
        {
          subGLCode: 'BBBBBB',
          subGLDescription: 'BBBBBB',
          mainGLDescription: 'BBBBBB',
          glType: 'BBBBBB',
        },
        new GlMapping()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of GlMapping', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          subGLCode: 'BBBBBB',
          subGLDescription: 'BBBBBB',
          mainGLCode: 'BBBBBB',
          mainGLDescription: 'BBBBBB',
          glType: 'BBBBBB',
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

    it('should delete a GlMapping', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addGlMappingToCollectionIfMissing', () => {
      it('should add a GlMapping to an empty array', () => {
        const glMapping: IGlMapping = { id: 123 };
        expectedResult = service.addGlMappingToCollectionIfMissing([], glMapping);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(glMapping);
      });

      it('should not add a GlMapping to an array that contains it', () => {
        const glMapping: IGlMapping = { id: 123 };
        const glMappingCollection: IGlMapping[] = [
          {
            ...glMapping,
          },
          { id: 456 },
        ];
        expectedResult = service.addGlMappingToCollectionIfMissing(glMappingCollection, glMapping);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a GlMapping to an array that doesn't contain it", () => {
        const glMapping: IGlMapping = { id: 123 };
        const glMappingCollection: IGlMapping[] = [{ id: 456 }];
        expectedResult = service.addGlMappingToCollectionIfMissing(glMappingCollection, glMapping);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(glMapping);
      });

      it('should add only unique GlMapping to an array', () => {
        const glMappingArray: IGlMapping[] = [{ id: 123 }, { id: 456 }, { id: 2654 }];
        const glMappingCollection: IGlMapping[] = [{ id: 123 }];
        expectedResult = service.addGlMappingToCollectionIfMissing(glMappingCollection, ...glMappingArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const glMapping: IGlMapping = { id: 123 };
        const glMapping2: IGlMapping = { id: 456 };
        expectedResult = service.addGlMappingToCollectionIfMissing([], glMapping, glMapping2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(glMapping);
        expect(expectedResult).toContain(glMapping2);
      });

      it('should accept null and undefined values', () => {
        const glMapping: IGlMapping = { id: 123 };
        expectedResult = service.addGlMappingToCollectionIfMissing([], null, glMapping, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(glMapping);
      });

      it('should return initial array if no GlMapping is added', () => {
        const glMappingCollection: IGlMapping[] = [{ id: 123 }];
        expectedResult = service.addGlMappingToCollectionIfMissing(glMappingCollection, undefined, null);
        expect(expectedResult).toEqual(glMappingCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
