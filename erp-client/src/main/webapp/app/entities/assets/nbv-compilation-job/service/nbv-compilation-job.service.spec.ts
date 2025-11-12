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
import { NVBCompilationStatus } from 'app/entities/enumerations/nvb-compilation-status.model';
import { INbvCompilationJob, NbvCompilationJob } from '../nbv-compilation-job.model';

import { NbvCompilationJobService } from './nbv-compilation-job.service';

describe('NbvCompilationJob Service', () => {
  let service: NbvCompilationJobService;
  let httpMock: HttpTestingController;
  let elemDefault: INbvCompilationJob;
  let expectedResult: INbvCompilationJob | INbvCompilationJob[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(NbvCompilationJobService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      compilationJobIdentifier: 'AAAAAAA',
      compilationJobTimeOfRequest: currentDate,
      entitiesAffected: 0,
      compilationStatus: NVBCompilationStatus.STARTED,
      jobTitle: 'AAAAAAA',
      numberOfBatches: 0,
      numberOfProcessedBatches: 0,
      processingTime: 'PT1S',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          compilationJobTimeOfRequest: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a NbvCompilationJob', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          compilationJobTimeOfRequest: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          compilationJobTimeOfRequest: currentDate,
        },
        returnedFromService
      );

      service.create(new NbvCompilationJob()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a NbvCompilationJob', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          compilationJobIdentifier: 'BBBBBB',
          compilationJobTimeOfRequest: currentDate.format(DATE_TIME_FORMAT),
          entitiesAffected: 1,
          compilationStatus: 'BBBBBB',
          jobTitle: 'BBBBBB',
          numberOfBatches: 1,
          numberOfProcessedBatches: 1,
          processingTime: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          compilationJobTimeOfRequest: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a NbvCompilationJob', () => {
      const patchObject = Object.assign(
        {
          compilationJobIdentifier: 'BBBBBB',
          compilationJobTimeOfRequest: currentDate.format(DATE_TIME_FORMAT),
          jobTitle: 'BBBBBB',
          numberOfBatches: 1,
          numberOfProcessedBatches: 1,
          processingTime: 'BBBBBB',
        },
        new NbvCompilationJob()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          compilationJobTimeOfRequest: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of NbvCompilationJob', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          compilationJobIdentifier: 'BBBBBB',
          compilationJobTimeOfRequest: currentDate.format(DATE_TIME_FORMAT),
          entitiesAffected: 1,
          compilationStatus: 'BBBBBB',
          jobTitle: 'BBBBBB',
          numberOfBatches: 1,
          numberOfProcessedBatches: 1,
          processingTime: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          compilationJobTimeOfRequest: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a NbvCompilationJob', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addNbvCompilationJobToCollectionIfMissing', () => {
      it('should add a NbvCompilationJob to an empty array', () => {
        const nbvCompilationJob: INbvCompilationJob = { id: 123 };
        expectedResult = service.addNbvCompilationJobToCollectionIfMissing([], nbvCompilationJob);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(nbvCompilationJob);
      });

      it('should not add a NbvCompilationJob to an array that contains it', () => {
        const nbvCompilationJob: INbvCompilationJob = { id: 123 };
        const nbvCompilationJobCollection: INbvCompilationJob[] = [
          {
            ...nbvCompilationJob,
          },
          { id: 456 },
        ];
        expectedResult = service.addNbvCompilationJobToCollectionIfMissing(nbvCompilationJobCollection, nbvCompilationJob);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a NbvCompilationJob to an array that doesn't contain it", () => {
        const nbvCompilationJob: INbvCompilationJob = { id: 123 };
        const nbvCompilationJobCollection: INbvCompilationJob[] = [{ id: 456 }];
        expectedResult = service.addNbvCompilationJobToCollectionIfMissing(nbvCompilationJobCollection, nbvCompilationJob);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(nbvCompilationJob);
      });

      it('should add only unique NbvCompilationJob to an array', () => {
        const nbvCompilationJobArray: INbvCompilationJob[] = [{ id: 123 }, { id: 456 }, { id: 30338 }];
        const nbvCompilationJobCollection: INbvCompilationJob[] = [{ id: 123 }];
        expectedResult = service.addNbvCompilationJobToCollectionIfMissing(nbvCompilationJobCollection, ...nbvCompilationJobArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const nbvCompilationJob: INbvCompilationJob = { id: 123 };
        const nbvCompilationJob2: INbvCompilationJob = { id: 456 };
        expectedResult = service.addNbvCompilationJobToCollectionIfMissing([], nbvCompilationJob, nbvCompilationJob2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(nbvCompilationJob);
        expect(expectedResult).toContain(nbvCompilationJob2);
      });

      it('should accept null and undefined values', () => {
        const nbvCompilationJob: INbvCompilationJob = { id: 123 };
        expectedResult = service.addNbvCompilationJobToCollectionIfMissing([], null, nbvCompilationJob, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(nbvCompilationJob);
      });

      it('should return initial array if no NbvCompilationJob is added', () => {
        const nbvCompilationJobCollection: INbvCompilationJob[] = [{ id: 123 }];
        expectedResult = service.addNbvCompilationJobToCollectionIfMissing(nbvCompilationJobCollection, undefined, null);
        expect(expectedResult).toEqual(nbvCompilationJobCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
