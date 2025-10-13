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

import { IGdiMasterDataIndex, GdiMasterDataIndex } from '../gdi-master-data-index.model';

import { GdiMasterDataIndexService } from './gdi-master-data-index.service';

describe('GdiMasterDataIndex Service', () => {
  let service: GdiMasterDataIndexService;
  let httpMock: HttpTestingController;
  let elemDefault: IGdiMasterDataIndex;
  let expectedResult: IGdiMasterDataIndex | IGdiMasterDataIndex[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(GdiMasterDataIndexService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      entityName: 'AAAAAAA',
      databaseName: 'AAAAAAA',
      businessDescription: 'AAAAAAA',
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

    it('should create a GdiMasterDataIndex', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new GdiMasterDataIndex()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a GdiMasterDataIndex', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          entityName: 'BBBBBB',
          databaseName: 'BBBBBB',
          businessDescription: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a GdiMasterDataIndex', () => {
      const patchObject = Object.assign(
        {
          databaseName: 'BBBBBB',
        },
        new GdiMasterDataIndex()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of GdiMasterDataIndex', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          entityName: 'BBBBBB',
          databaseName: 'BBBBBB',
          businessDescription: 'BBBBBB',
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

    it('should delete a GdiMasterDataIndex', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addGdiMasterDataIndexToCollectionIfMissing', () => {
      it('should add a GdiMasterDataIndex to an empty array', () => {
        const gdiMasterDataIndex: IGdiMasterDataIndex = { id: 123 };
        expectedResult = service.addGdiMasterDataIndexToCollectionIfMissing([], gdiMasterDataIndex);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(gdiMasterDataIndex);
      });

      it('should not add a GdiMasterDataIndex to an array that contains it', () => {
        const gdiMasterDataIndex: IGdiMasterDataIndex = { id: 123 };
        const gdiMasterDataIndexCollection: IGdiMasterDataIndex[] = [
          {
            ...gdiMasterDataIndex,
          },
          { id: 456 },
        ];
        expectedResult = service.addGdiMasterDataIndexToCollectionIfMissing(gdiMasterDataIndexCollection, gdiMasterDataIndex);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a GdiMasterDataIndex to an array that doesn't contain it", () => {
        const gdiMasterDataIndex: IGdiMasterDataIndex = { id: 123 };
        const gdiMasterDataIndexCollection: IGdiMasterDataIndex[] = [{ id: 456 }];
        expectedResult = service.addGdiMasterDataIndexToCollectionIfMissing(gdiMasterDataIndexCollection, gdiMasterDataIndex);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(gdiMasterDataIndex);
      });

      it('should add only unique GdiMasterDataIndex to an array', () => {
        const gdiMasterDataIndexArray: IGdiMasterDataIndex[] = [{ id: 123 }, { id: 456 }, { id: 15608 }];
        const gdiMasterDataIndexCollection: IGdiMasterDataIndex[] = [{ id: 123 }];
        expectedResult = service.addGdiMasterDataIndexToCollectionIfMissing(gdiMasterDataIndexCollection, ...gdiMasterDataIndexArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const gdiMasterDataIndex: IGdiMasterDataIndex = { id: 123 };
        const gdiMasterDataIndex2: IGdiMasterDataIndex = { id: 456 };
        expectedResult = service.addGdiMasterDataIndexToCollectionIfMissing([], gdiMasterDataIndex, gdiMasterDataIndex2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(gdiMasterDataIndex);
        expect(expectedResult).toContain(gdiMasterDataIndex2);
      });

      it('should accept null and undefined values', () => {
        const gdiMasterDataIndex: IGdiMasterDataIndex = { id: 123 };
        expectedResult = service.addGdiMasterDataIndexToCollectionIfMissing([], null, gdiMasterDataIndex, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(gdiMasterDataIndex);
      });

      it('should return initial array if no GdiMasterDataIndex is added', () => {
        const gdiMasterDataIndexCollection: IGdiMasterDataIndex[] = [{ id: 123 }];
        expectedResult = service.addGdiMasterDataIndexToCollectionIfMissing(gdiMasterDataIndexCollection, undefined, null);
        expect(expectedResult).toEqual(gdiMasterDataIndexCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
