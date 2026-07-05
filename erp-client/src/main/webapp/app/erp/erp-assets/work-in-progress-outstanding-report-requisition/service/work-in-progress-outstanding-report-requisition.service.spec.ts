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

import { DATE_FORMAT, DATE_TIME_FORMAT } from 'app/config/input.constants';
import {
  IWorkInProgressOutstandingReportRequisition,
  WorkInProgressOutstandingReportRequisition,
} from '../work-in-progress-outstanding-report-requisition.model';

import { WorkInProgressOutstandingReportRequisitionService } from './work-in-progress-outstanding-report-requisition.service';

describe('WorkInProgressOutstandingReportRequisition Service', () => {
  let service: WorkInProgressOutstandingReportRequisitionService;
  let httpMock: HttpTestingController;
  let elemDefault: IWorkInProgressOutstandingReportRequisition;
  let expectedResult: IWorkInProgressOutstandingReportRequisition | IWorkInProgressOutstandingReportRequisition[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(WorkInProgressOutstandingReportRequisitionService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      requestId: 'AAAAAAA',
      reportDate: currentDate,
      timeOfRequisition: currentDate,
      fileChecksum: 'AAAAAAA',
      tampered: false,
      filename: 'AAAAAAA',
      reportParameters: 'AAAAAAA',
      reportFileContentType: 'image/png',
      reportFile: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          reportDate: currentDate.format(DATE_FORMAT),
          timeOfRequisition: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a WorkInProgressOutstandingReportRequisition', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          reportDate: currentDate.format(DATE_FORMAT),
          timeOfRequisition: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          reportDate: currentDate,
          timeOfRequisition: currentDate,
        },
        returnedFromService
      );

      service.create(new WorkInProgressOutstandingReportRequisition()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a WorkInProgressOutstandingReportRequisition', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          requestId: 'BBBBBB',
          reportDate: currentDate.format(DATE_FORMAT),
          timeOfRequisition: currentDate.format(DATE_TIME_FORMAT),
          fileChecksum: 'BBBBBB',
          tampered: true,
          filename: 'BBBBBB',
          reportParameters: 'BBBBBB',
          reportFile: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          reportDate: currentDate,
          timeOfRequisition: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a WorkInProgressOutstandingReportRequisition', () => {
      const patchObject = Object.assign(
        {
          requestId: 'BBBBBB',
          reportDate: currentDate.format(DATE_FORMAT),
          fileChecksum: 'BBBBBB',
          tampered: true,
          filename: 'BBBBBB',
          reportParameters: 'BBBBBB',
          reportFile: 'BBBBBB',
        },
        new WorkInProgressOutstandingReportRequisition()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          reportDate: currentDate,
          timeOfRequisition: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of WorkInProgressOutstandingReportRequisition', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          requestId: 'BBBBBB',
          reportDate: currentDate.format(DATE_FORMAT),
          timeOfRequisition: currentDate.format(DATE_TIME_FORMAT),
          fileChecksum: 'BBBBBB',
          tampered: true,
          filename: 'BBBBBB',
          reportParameters: 'BBBBBB',
          reportFile: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          reportDate: currentDate,
          timeOfRequisition: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a WorkInProgressOutstandingReportRequisition', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addWorkInProgressOutstandingReportRequisitionToCollectionIfMissing', () => {
      it('should add a WorkInProgressOutstandingReportRequisition to an empty array', () => {
        const workInProgressOutstandingReportRequisition: IWorkInProgressOutstandingReportRequisition = { id: 123 };
        expectedResult = service.addWorkInProgressOutstandingReportRequisitionToCollectionIfMissing(
          [],
          workInProgressOutstandingReportRequisition
        );
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(workInProgressOutstandingReportRequisition);
      });

      it('should not add a WorkInProgressOutstandingReportRequisition to an array that contains it', () => {
        const workInProgressOutstandingReportRequisition: IWorkInProgressOutstandingReportRequisition = { id: 123 };
        const workInProgressOutstandingReportRequisitionCollection: IWorkInProgressOutstandingReportRequisition[] = [
          {
            ...workInProgressOutstandingReportRequisition,
          },
          { id: 456 },
        ];
        expectedResult = service.addWorkInProgressOutstandingReportRequisitionToCollectionIfMissing(
          workInProgressOutstandingReportRequisitionCollection,
          workInProgressOutstandingReportRequisition
        );
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a WorkInProgressOutstandingReportRequisition to an array that doesn't contain it", () => {
        const workInProgressOutstandingReportRequisition: IWorkInProgressOutstandingReportRequisition = { id: 123 };
        const workInProgressOutstandingReportRequisitionCollection: IWorkInProgressOutstandingReportRequisition[] = [{ id: 456 }];
        expectedResult = service.addWorkInProgressOutstandingReportRequisitionToCollectionIfMissing(
          workInProgressOutstandingReportRequisitionCollection,
          workInProgressOutstandingReportRequisition
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(workInProgressOutstandingReportRequisition);
      });

      it('should add only unique WorkInProgressOutstandingReportRequisition to an array', () => {
        const workInProgressOutstandingReportRequisitionArray: IWorkInProgressOutstandingReportRequisition[] = [
          { id: 123 },
          { id: 456 },
          { id: 36933 },
        ];
        const workInProgressOutstandingReportRequisitionCollection: IWorkInProgressOutstandingReportRequisition[] = [{ id: 123 }];
        expectedResult = service.addWorkInProgressOutstandingReportRequisitionToCollectionIfMissing(
          workInProgressOutstandingReportRequisitionCollection,
          ...workInProgressOutstandingReportRequisitionArray
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const workInProgressOutstandingReportRequisition: IWorkInProgressOutstandingReportRequisition = { id: 123 };
        const workInProgressOutstandingReportRequisition2: IWorkInProgressOutstandingReportRequisition = { id: 456 };
        expectedResult = service.addWorkInProgressOutstandingReportRequisitionToCollectionIfMissing(
          [],
          workInProgressOutstandingReportRequisition,
          workInProgressOutstandingReportRequisition2
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(workInProgressOutstandingReportRequisition);
        expect(expectedResult).toContain(workInProgressOutstandingReportRequisition2);
      });

      it('should accept null and undefined values', () => {
        const workInProgressOutstandingReportRequisition: IWorkInProgressOutstandingReportRequisition = { id: 123 };
        expectedResult = service.addWorkInProgressOutstandingReportRequisitionToCollectionIfMissing(
          [],
          null,
          workInProgressOutstandingReportRequisition,
          undefined
        );
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(workInProgressOutstandingReportRequisition);
      });

      it('should return initial array if no WorkInProgressOutstandingReportRequisition is added', () => {
        const workInProgressOutstandingReportRequisitionCollection: IWorkInProgressOutstandingReportRequisition[] = [{ id: 123 }];
        expectedResult = service.addWorkInProgressOutstandingReportRequisitionToCollectionIfMissing(
          workInProgressOutstandingReportRequisitionCollection,
          undefined,
          null
        );
        expect(expectedResult).toEqual(workInProgressOutstandingReportRequisitionCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
