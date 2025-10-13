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

import { CompilationBatchStatusTypes } from 'app/entities/enumerations/compilation-batch-status-types.model';
import { INbvCompilationBatch, NbvCompilationBatch } from '../nbv-compilation-batch.model';

import { NbvCompilationBatchService } from './nbv-compilation-batch.service';

describe('NbvCompilationBatch Service', () => {
  let service: NbvCompilationBatchService;
  let httpMock: HttpTestingController;
  let elemDefault: INbvCompilationBatch;
  let expectedResult: INbvCompilationBatch | INbvCompilationBatch[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(NbvCompilationBatchService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      startIndex: 0,
      endIndex: 0,
      compilationBatchStatus: CompilationBatchStatusTypes.CREATED,
      compilationBatchIdentifier: 'AAAAAAA',
      compilationJobidentifier: 'AAAAAAA',
      depreciationPeriodIdentifier: 'AAAAAAA',
      fiscalMonthIdentifier: 'AAAAAAA',
      batchSize: 0,
      processedItems: 0,
      sequenceNumber: 0,
      isLastBatch: false,
      processingTime: 'PT1S',
      totalItems: 0,
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

    it('should create a NbvCompilationBatch', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new NbvCompilationBatch()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a NbvCompilationBatch', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          startIndex: 1,
          endIndex: 1,
          compilationBatchStatus: 'BBBBBB',
          compilationBatchIdentifier: 'BBBBBB',
          compilationJobidentifier: 'BBBBBB',
          depreciationPeriodIdentifier: 'BBBBBB',
          fiscalMonthIdentifier: 'BBBBBB',
          batchSize: 1,
          processedItems: 1,
          sequenceNumber: 1,
          isLastBatch: true,
          processingTime: 'BBBBBB',
          totalItems: 1,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a NbvCompilationBatch', () => {
      const patchObject = Object.assign(
        {
          compilationBatchIdentifier: 'BBBBBB',
          compilationJobidentifier: 'BBBBBB',
          depreciationPeriodIdentifier: 'BBBBBB',
          batchSize: 1,
          processedItems: 1,
          totalItems: 1,
        },
        new NbvCompilationBatch()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of NbvCompilationBatch', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          startIndex: 1,
          endIndex: 1,
          compilationBatchStatus: 'BBBBBB',
          compilationBatchIdentifier: 'BBBBBB',
          compilationJobidentifier: 'BBBBBB',
          depreciationPeriodIdentifier: 'BBBBBB',
          fiscalMonthIdentifier: 'BBBBBB',
          batchSize: 1,
          processedItems: 1,
          sequenceNumber: 1,
          isLastBatch: true,
          processingTime: 'BBBBBB',
          totalItems: 1,
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

    it('should delete a NbvCompilationBatch', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addNbvCompilationBatchToCollectionIfMissing', () => {
      it('should add a NbvCompilationBatch to an empty array', () => {
        const nbvCompilationBatch: INbvCompilationBatch = { id: 123 };
        expectedResult = service.addNbvCompilationBatchToCollectionIfMissing([], nbvCompilationBatch);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(nbvCompilationBatch);
      });

      it('should not add a NbvCompilationBatch to an array that contains it', () => {
        const nbvCompilationBatch: INbvCompilationBatch = { id: 123 };
        const nbvCompilationBatchCollection: INbvCompilationBatch[] = [
          {
            ...nbvCompilationBatch,
          },
          { id: 456 },
        ];
        expectedResult = service.addNbvCompilationBatchToCollectionIfMissing(nbvCompilationBatchCollection, nbvCompilationBatch);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a NbvCompilationBatch to an array that doesn't contain it", () => {
        const nbvCompilationBatch: INbvCompilationBatch = { id: 123 };
        const nbvCompilationBatchCollection: INbvCompilationBatch[] = [{ id: 456 }];
        expectedResult = service.addNbvCompilationBatchToCollectionIfMissing(nbvCompilationBatchCollection, nbvCompilationBatch);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(nbvCompilationBatch);
      });

      it('should add only unique NbvCompilationBatch to an array', () => {
        const nbvCompilationBatchArray: INbvCompilationBatch[] = [{ id: 123 }, { id: 456 }, { id: 24417 }];
        const nbvCompilationBatchCollection: INbvCompilationBatch[] = [{ id: 123 }];
        expectedResult = service.addNbvCompilationBatchToCollectionIfMissing(nbvCompilationBatchCollection, ...nbvCompilationBatchArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const nbvCompilationBatch: INbvCompilationBatch = { id: 123 };
        const nbvCompilationBatch2: INbvCompilationBatch = { id: 456 };
        expectedResult = service.addNbvCompilationBatchToCollectionIfMissing([], nbvCompilationBatch, nbvCompilationBatch2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(nbvCompilationBatch);
        expect(expectedResult).toContain(nbvCompilationBatch2);
      });

      it('should accept null and undefined values', () => {
        const nbvCompilationBatch: INbvCompilationBatch = { id: 123 };
        expectedResult = service.addNbvCompilationBatchToCollectionIfMissing([], null, nbvCompilationBatch, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(nbvCompilationBatch);
      });

      it('should return initial array if no NbvCompilationBatch is added', () => {
        const nbvCompilationBatchCollection: INbvCompilationBatch[] = [{ id: 123 }];
        expectedResult = service.addNbvCompilationBatchToCollectionIfMissing(nbvCompilationBatchCollection, undefined, null);
        expect(expectedResult).toEqual(nbvCompilationBatchCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
