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

import { DATE_FORMAT } from 'app/config/input.constants';
import { IJobSheet, JobSheet } from '../job-sheet.model';

import { JobSheetService } from './job-sheet.service';

describe('JobSheet Service', () => {
  let service: JobSheetService;
  let httpMock: HttpTestingController;
  let elemDefault: IJobSheet;
  let expectedResult: IJobSheet | IJobSheet[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(JobSheetService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      serialNumber: 'AAAAAAA',
      jobSheetDate: currentDate,
      details: 'AAAAAAA',
      remarks: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          jobSheetDate: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a JobSheet', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          jobSheetDate: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          jobSheetDate: currentDate,
        },
        returnedFromService
      );

      service.create(new JobSheet()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a JobSheet', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          serialNumber: 'BBBBBB',
          jobSheetDate: currentDate.format(DATE_FORMAT),
          details: 'BBBBBB',
          remarks: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          jobSheetDate: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a JobSheet', () => {
      const patchObject = Object.assign(
        {
          serialNumber: 'BBBBBB',
        },
        new JobSheet()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          jobSheetDate: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of JobSheet', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          serialNumber: 'BBBBBB',
          jobSheetDate: currentDate.format(DATE_FORMAT),
          details: 'BBBBBB',
          remarks: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          jobSheetDate: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a JobSheet', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addJobSheetToCollectionIfMissing', () => {
      it('should add a JobSheet to an empty array', () => {
        const jobSheet: IJobSheet = { id: 123 };
        expectedResult = service.addJobSheetToCollectionIfMissing([], jobSheet);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(jobSheet);
      });

      it('should not add a JobSheet to an array that contains it', () => {
        const jobSheet: IJobSheet = { id: 123 };
        const jobSheetCollection: IJobSheet[] = [
          {
            ...jobSheet,
          },
          { id: 456 },
        ];
        expectedResult = service.addJobSheetToCollectionIfMissing(jobSheetCollection, jobSheet);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a JobSheet to an array that doesn't contain it", () => {
        const jobSheet: IJobSheet = { id: 123 };
        const jobSheetCollection: IJobSheet[] = [{ id: 456 }];
        expectedResult = service.addJobSheetToCollectionIfMissing(jobSheetCollection, jobSheet);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(jobSheet);
      });

      it('should add only unique JobSheet to an array', () => {
        const jobSheetArray: IJobSheet[] = [{ id: 123 }, { id: 456 }, { id: 17162 }];
        const jobSheetCollection: IJobSheet[] = [{ id: 123 }];
        expectedResult = service.addJobSheetToCollectionIfMissing(jobSheetCollection, ...jobSheetArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const jobSheet: IJobSheet = { id: 123 };
        const jobSheet2: IJobSheet = { id: 456 };
        expectedResult = service.addJobSheetToCollectionIfMissing([], jobSheet, jobSheet2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(jobSheet);
        expect(expectedResult).toContain(jobSheet2);
      });

      it('should accept null and undefined values', () => {
        const jobSheet: IJobSheet = { id: 123 };
        expectedResult = service.addJobSheetToCollectionIfMissing([], null, jobSheet, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(jobSheet);
      });

      it('should return initial array if no JobSheet is added', () => {
        const jobSheetCollection: IJobSheet[] = [{ id: 123 }];
        expectedResult = service.addJobSheetToCollectionIfMissing(jobSheetCollection, undefined, null);
        expect(expectedResult).toEqual(jobSheetCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
