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
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { DepreciationJobStatusType } from 'app/entities/enumerations/depreciation-job-status-type.model';
import { IDepreciationJob, DepreciationJob } from '../depreciation-job.model';

import { DepreciationJobService } from './depreciation-job.service';

describe('DepreciationJob Service', () => {
  let service: DepreciationJobService;
  let httpMock: HttpTestingController;
  let elemDefault: IDepreciationJob;
  let expectedResult: IDepreciationJob | IDepreciationJob[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(DepreciationJobService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      timeOfCommencement: currentDate,
      depreciationJobStatus: DepreciationJobStatusType.COMPLETE,
      description: 'AAAAAAA',
      numberOfBatches: 0,
      processedBatches: 0,
      lastBatchSize: 0,
      processedItems: 0,
      processingTime: 'PT1S',
      totalItems: 0,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          timeOfCommencement: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a DepreciationJob', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          timeOfCommencement: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          timeOfCommencement: currentDate,
        },
        returnedFromService
      );

      service.create(new DepreciationJob()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a DepreciationJob', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          timeOfCommencement: currentDate.format(DATE_TIME_FORMAT),
          depreciationJobStatus: 'BBBBBB',
          description: 'BBBBBB',
          numberOfBatches: 1,
          processedBatches: 1,
          lastBatchSize: 1,
          processedItems: 1,
          processingTime: 'BBBBBB',
          totalItems: 1,
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          timeOfCommencement: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a DepreciationJob', () => {
      const patchObject = Object.assign(
        {
          timeOfCommencement: currentDate.format(DATE_TIME_FORMAT),
          description: 'BBBBBB',
          processingTime: 'BBBBBB',
          totalItems: 1,
        },
        new DepreciationJob()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          timeOfCommencement: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of DepreciationJob', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          timeOfCommencement: currentDate.format(DATE_TIME_FORMAT),
          depreciationJobStatus: 'BBBBBB',
          description: 'BBBBBB',
          numberOfBatches: 1,
          processedBatches: 1,
          lastBatchSize: 1,
          processedItems: 1,
          processingTime: 'BBBBBB',
          totalItems: 1,
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          timeOfCommencement: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a DepreciationJob', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addDepreciationJobToCollectionIfMissing', () => {
      it('should add a DepreciationJob to an empty array', () => {
        const depreciationJob: IDepreciationJob = { id: 123 };
        expectedResult = service.addDepreciationJobToCollectionIfMissing([], depreciationJob);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(depreciationJob);
      });

      it('should not add a DepreciationJob to an array that contains it', () => {
        const depreciationJob: IDepreciationJob = { id: 123 };
        const depreciationJobCollection: IDepreciationJob[] = [
          {
            ...depreciationJob,
          },
          { id: 456 },
        ];
        expectedResult = service.addDepreciationJobToCollectionIfMissing(depreciationJobCollection, depreciationJob);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a DepreciationJob to an array that doesn't contain it", () => {
        const depreciationJob: IDepreciationJob = { id: 123 };
        const depreciationJobCollection: IDepreciationJob[] = [{ id: 456 }];
        expectedResult = service.addDepreciationJobToCollectionIfMissing(depreciationJobCollection, depreciationJob);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(depreciationJob);
      });

      it('should add only unique DepreciationJob to an array', () => {
        const depreciationJobArray: IDepreciationJob[] = [{ id: 123 }, { id: 456 }, { id: 29811 }];
        const depreciationJobCollection: IDepreciationJob[] = [{ id: 123 }];
        expectedResult = service.addDepreciationJobToCollectionIfMissing(depreciationJobCollection, ...depreciationJobArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const depreciationJob: IDepreciationJob = { id: 123 };
        const depreciationJob2: IDepreciationJob = { id: 456 };
        expectedResult = service.addDepreciationJobToCollectionIfMissing([], depreciationJob, depreciationJob2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(depreciationJob);
        expect(expectedResult).toContain(depreciationJob2);
      });

      it('should accept null and undefined values', () => {
        const depreciationJob: IDepreciationJob = { id: 123 };
        expectedResult = service.addDepreciationJobToCollectionIfMissing([], null, depreciationJob, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(depreciationJob);
      });

      it('should return initial array if no DepreciationJob is added', () => {
        const depreciationJobCollection: IDepreciationJob[] = [{ id: 123 }];
        expectedResult = service.addDepreciationJobToCollectionIfMissing(depreciationJobCollection, undefined, null);
        expect(expectedResult).toEqual(depreciationJobCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
