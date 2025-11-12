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
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { DepreciationBatchStatusType } from 'app/entities/enumerations/depreciation-batch-status-type.model';
import { IDepreciationBatchSequence, DepreciationBatchSequence } from '../depreciation-batch-sequence.model';

import { DepreciationBatchSequenceService } from './depreciation-batch-sequence.service';

describe('DepreciationBatchSequence Service', () => {
  let service: DepreciationBatchSequenceService;
  let httpMock: HttpTestingController;
  let elemDefault: IDepreciationBatchSequence;
  let expectedResult: IDepreciationBatchSequence | IDepreciationBatchSequence[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(DepreciationBatchSequenceService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      startIndex: 0,
      endIndex: 0,
      createdAt: currentDate,
      depreciationBatchStatus: DepreciationBatchStatusType.CREATED,
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
      const returnedFromService = Object.assign(
        {
          createdAt: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a DepreciationBatchSequence', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          createdAt: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          createdAt: currentDate,
        },
        returnedFromService
      );

      service.create(new DepreciationBatchSequence()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a DepreciationBatchSequence', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          startIndex: 1,
          endIndex: 1,
          createdAt: currentDate.format(DATE_TIME_FORMAT),
          depreciationBatchStatus: 'BBBBBB',
          batchSize: 1,
          processedItems: 1,
          sequenceNumber: 1,
          isLastBatch: true,
          processingTime: 'BBBBBB',
          totalItems: 1,
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          createdAt: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a DepreciationBatchSequence', () => {
      const patchObject = Object.assign(
        {
          depreciationBatchStatus: 'BBBBBB',
          processedItems: 1,
          sequenceNumber: 1,
          totalItems: 1,
        },
        new DepreciationBatchSequence()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          createdAt: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of DepreciationBatchSequence', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          startIndex: 1,
          endIndex: 1,
          createdAt: currentDate.format(DATE_TIME_FORMAT),
          depreciationBatchStatus: 'BBBBBB',
          batchSize: 1,
          processedItems: 1,
          sequenceNumber: 1,
          isLastBatch: true,
          processingTime: 'BBBBBB',
          totalItems: 1,
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          createdAt: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a DepreciationBatchSequence', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addDepreciationBatchSequenceToCollectionIfMissing', () => {
      it('should add a DepreciationBatchSequence to an empty array', () => {
        const depreciationBatchSequence: IDepreciationBatchSequence = { id: 123 };
        expectedResult = service.addDepreciationBatchSequenceToCollectionIfMissing([], depreciationBatchSequence);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(depreciationBatchSequence);
      });

      it('should not add a DepreciationBatchSequence to an array that contains it', () => {
        const depreciationBatchSequence: IDepreciationBatchSequence = { id: 123 };
        const depreciationBatchSequenceCollection: IDepreciationBatchSequence[] = [
          {
            ...depreciationBatchSequence,
          },
          { id: 456 },
        ];
        expectedResult = service.addDepreciationBatchSequenceToCollectionIfMissing(
          depreciationBatchSequenceCollection,
          depreciationBatchSequence
        );
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a DepreciationBatchSequence to an array that doesn't contain it", () => {
        const depreciationBatchSequence: IDepreciationBatchSequence = { id: 123 };
        const depreciationBatchSequenceCollection: IDepreciationBatchSequence[] = [{ id: 456 }];
        expectedResult = service.addDepreciationBatchSequenceToCollectionIfMissing(
          depreciationBatchSequenceCollection,
          depreciationBatchSequence
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(depreciationBatchSequence);
      });

      it('should add only unique DepreciationBatchSequence to an array', () => {
        const depreciationBatchSequenceArray: IDepreciationBatchSequence[] = [{ id: 123 }, { id: 456 }, { id: 88559 }];
        const depreciationBatchSequenceCollection: IDepreciationBatchSequence[] = [{ id: 123 }];
        expectedResult = service.addDepreciationBatchSequenceToCollectionIfMissing(
          depreciationBatchSequenceCollection,
          ...depreciationBatchSequenceArray
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const depreciationBatchSequence: IDepreciationBatchSequence = { id: 123 };
        const depreciationBatchSequence2: IDepreciationBatchSequence = { id: 456 };
        expectedResult = service.addDepreciationBatchSequenceToCollectionIfMissing(
          [],
          depreciationBatchSequence,
          depreciationBatchSequence2
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(depreciationBatchSequence);
        expect(expectedResult).toContain(depreciationBatchSequence2);
      });

      it('should accept null and undefined values', () => {
        const depreciationBatchSequence: IDepreciationBatchSequence = { id: 123 };
        expectedResult = service.addDepreciationBatchSequenceToCollectionIfMissing([], null, depreciationBatchSequence, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(depreciationBatchSequence);
      });

      it('should return initial array if no DepreciationBatchSequence is added', () => {
        const depreciationBatchSequenceCollection: IDepreciationBatchSequence[] = [{ id: 123 }];
        expectedResult = service.addDepreciationBatchSequenceToCollectionIfMissing(depreciationBatchSequenceCollection, undefined, null);
        expect(expectedResult).toEqual(depreciationBatchSequenceCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
