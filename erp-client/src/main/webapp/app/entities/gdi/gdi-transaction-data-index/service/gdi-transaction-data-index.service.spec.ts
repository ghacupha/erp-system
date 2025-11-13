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

import { UpdateFrequencyTypes } from 'app/entities/enumerations/update-frequency-types.model';
import { DatasetBehaviorTypes } from 'app/entities/enumerations/dataset-behavior-types.model';
import { IGdiTransactionDataIndex, GdiTransactionDataIndex } from '../gdi-transaction-data-index.model';

import { GdiTransactionDataIndexService } from './gdi-transaction-data-index.service';

describe('GdiTransactionDataIndex Service', () => {
  let service: GdiTransactionDataIndexService;
  let httpMock: HttpTestingController;
  let elemDefault: IGdiTransactionDataIndex;
  let expectedResult: IGdiTransactionDataIndex | IGdiTransactionDataIndex[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(GdiTransactionDataIndexService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      datasetName: 'AAAAAAA',
      databaseName: 'AAAAAAA',
      updateFrequency: UpdateFrequencyTypes.DAILY,
      datasetBehavior: DatasetBehaviorTypes.INSERT_AND_UPDATE,
      minimumDatarowsPerRequest: 0,
      maximumDataRowsPerRequest: 0,
      datasetDescription: 'AAAAAAA',
      dataTemplateContentType: 'image/png',
      dataTemplate: 'AAAAAAA',
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

    it('should create a GdiTransactionDataIndex', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new GdiTransactionDataIndex()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a GdiTransactionDataIndex', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          datasetName: 'BBBBBB',
          databaseName: 'BBBBBB',
          updateFrequency: 'BBBBBB',
          datasetBehavior: 'BBBBBB',
          minimumDatarowsPerRequest: 1,
          maximumDataRowsPerRequest: 1,
          datasetDescription: 'BBBBBB',
          dataTemplate: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a GdiTransactionDataIndex', () => {
      const patchObject = Object.assign(
        {
          maximumDataRowsPerRequest: 1,
          dataTemplate: 'BBBBBB',
        },
        new GdiTransactionDataIndex()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of GdiTransactionDataIndex', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          datasetName: 'BBBBBB',
          databaseName: 'BBBBBB',
          updateFrequency: 'BBBBBB',
          datasetBehavior: 'BBBBBB',
          minimumDatarowsPerRequest: 1,
          maximumDataRowsPerRequest: 1,
          datasetDescription: 'BBBBBB',
          dataTemplate: 'BBBBBB',
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

    it('should delete a GdiTransactionDataIndex', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addGdiTransactionDataIndexToCollectionIfMissing', () => {
      it('should add a GdiTransactionDataIndex to an empty array', () => {
        const gdiTransactionDataIndex: IGdiTransactionDataIndex = { id: 123 };
        expectedResult = service.addGdiTransactionDataIndexToCollectionIfMissing([], gdiTransactionDataIndex);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(gdiTransactionDataIndex);
      });

      it('should not add a GdiTransactionDataIndex to an array that contains it', () => {
        const gdiTransactionDataIndex: IGdiTransactionDataIndex = { id: 123 };
        const gdiTransactionDataIndexCollection: IGdiTransactionDataIndex[] = [
          {
            ...gdiTransactionDataIndex,
          },
          { id: 456 },
        ];
        expectedResult = service.addGdiTransactionDataIndexToCollectionIfMissing(
          gdiTransactionDataIndexCollection,
          gdiTransactionDataIndex
        );
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a GdiTransactionDataIndex to an array that doesn't contain it", () => {
        const gdiTransactionDataIndex: IGdiTransactionDataIndex = { id: 123 };
        const gdiTransactionDataIndexCollection: IGdiTransactionDataIndex[] = [{ id: 456 }];
        expectedResult = service.addGdiTransactionDataIndexToCollectionIfMissing(
          gdiTransactionDataIndexCollection,
          gdiTransactionDataIndex
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(gdiTransactionDataIndex);
      });

      it('should add only unique GdiTransactionDataIndex to an array', () => {
        const gdiTransactionDataIndexArray: IGdiTransactionDataIndex[] = [{ id: 123 }, { id: 456 }, { id: 17444 }];
        const gdiTransactionDataIndexCollection: IGdiTransactionDataIndex[] = [{ id: 123 }];
        expectedResult = service.addGdiTransactionDataIndexToCollectionIfMissing(
          gdiTransactionDataIndexCollection,
          ...gdiTransactionDataIndexArray
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const gdiTransactionDataIndex: IGdiTransactionDataIndex = { id: 123 };
        const gdiTransactionDataIndex2: IGdiTransactionDataIndex = { id: 456 };
        expectedResult = service.addGdiTransactionDataIndexToCollectionIfMissing([], gdiTransactionDataIndex, gdiTransactionDataIndex2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(gdiTransactionDataIndex);
        expect(expectedResult).toContain(gdiTransactionDataIndex2);
      });

      it('should accept null and undefined values', () => {
        const gdiTransactionDataIndex: IGdiTransactionDataIndex = { id: 123 };
        expectedResult = service.addGdiTransactionDataIndexToCollectionIfMissing([], null, gdiTransactionDataIndex, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(gdiTransactionDataIndex);
      });

      it('should return initial array if no GdiTransactionDataIndex is added', () => {
        const gdiTransactionDataIndexCollection: IGdiTransactionDataIndex[] = [{ id: 123 }];
        expectedResult = service.addGdiTransactionDataIndexToCollectionIfMissing(gdiTransactionDataIndexCollection, undefined, null);
        expect(expectedResult).toEqual(gdiTransactionDataIndexCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
