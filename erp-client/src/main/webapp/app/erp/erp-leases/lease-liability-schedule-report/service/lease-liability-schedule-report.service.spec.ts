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
import { ILeaseLiabilityScheduleReport, LeaseLiabilityScheduleReport } from '../lease-liability-schedule-report.model';

import { LeaseLiabilityScheduleReportService } from './lease-liability-schedule-report.service';

describe('LeaseLiabilityScheduleReport Service', () => {
  let service: LeaseLiabilityScheduleReportService;
  let httpMock: HttpTestingController;
  let elemDefault: ILeaseLiabilityScheduleReport;
  let expectedResult: ILeaseLiabilityScheduleReport | ILeaseLiabilityScheduleReport[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(LeaseLiabilityScheduleReportService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      requestId: 'AAAAAAA',
      timeOfRequest: currentDate,
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
          timeOfRequest: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a LeaseLiabilityScheduleReport', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          timeOfRequest: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          timeOfRequest: currentDate,
        },
        returnedFromService
      );

      service.create(new LeaseLiabilityScheduleReport()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a LeaseLiabilityScheduleReport', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          requestId: 'BBBBBB',
          timeOfRequest: currentDate.format(DATE_TIME_FORMAT),
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
          timeOfRequest: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a LeaseLiabilityScheduleReport', () => {
      const patchObject = Object.assign(
        {
          requestId: 'BBBBBB',
          timeOfRequest: currentDate.format(DATE_TIME_FORMAT),
          filename: 'BBBBBB',
          reportParameters: 'BBBBBB',
        },
        new LeaseLiabilityScheduleReport()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          timeOfRequest: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of LeaseLiabilityScheduleReport', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          requestId: 'BBBBBB',
          timeOfRequest: currentDate.format(DATE_TIME_FORMAT),
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
          timeOfRequest: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a LeaseLiabilityScheduleReport', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addLeaseLiabilityScheduleReportToCollectionIfMissing', () => {
      it('should add a LeaseLiabilityScheduleReport to an empty array', () => {
        const leaseLiabilityScheduleReport: ILeaseLiabilityScheduleReport = { id: 123 };
        expectedResult = service.addLeaseLiabilityScheduleReportToCollectionIfMissing([], leaseLiabilityScheduleReport);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(leaseLiabilityScheduleReport);
      });

      it('should not add a LeaseLiabilityScheduleReport to an array that contains it', () => {
        const leaseLiabilityScheduleReport: ILeaseLiabilityScheduleReport = { id: 123 };
        const leaseLiabilityScheduleReportCollection: ILeaseLiabilityScheduleReport[] = [
          {
            ...leaseLiabilityScheduleReport,
          },
          { id: 456 },
        ];
        expectedResult = service.addLeaseLiabilityScheduleReportToCollectionIfMissing(
          leaseLiabilityScheduleReportCollection,
          leaseLiabilityScheduleReport
        );
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a LeaseLiabilityScheduleReport to an array that doesn't contain it", () => {
        const leaseLiabilityScheduleReport: ILeaseLiabilityScheduleReport = { id: 123 };
        const leaseLiabilityScheduleReportCollection: ILeaseLiabilityScheduleReport[] = [{ id: 456 }];
        expectedResult = service.addLeaseLiabilityScheduleReportToCollectionIfMissing(
          leaseLiabilityScheduleReportCollection,
          leaseLiabilityScheduleReport
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(leaseLiabilityScheduleReport);
      });

      it('should add only unique LeaseLiabilityScheduleReport to an array', () => {
        const leaseLiabilityScheduleReportArray: ILeaseLiabilityScheduleReport[] = [{ id: 123 }, { id: 456 }, { id: 90133 }];
        const leaseLiabilityScheduleReportCollection: ILeaseLiabilityScheduleReport[] = [{ id: 123 }];
        expectedResult = service.addLeaseLiabilityScheduleReportToCollectionIfMissing(
          leaseLiabilityScheduleReportCollection,
          ...leaseLiabilityScheduleReportArray
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const leaseLiabilityScheduleReport: ILeaseLiabilityScheduleReport = { id: 123 };
        const leaseLiabilityScheduleReport2: ILeaseLiabilityScheduleReport = { id: 456 };
        expectedResult = service.addLeaseLiabilityScheduleReportToCollectionIfMissing(
          [],
          leaseLiabilityScheduleReport,
          leaseLiabilityScheduleReport2
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(leaseLiabilityScheduleReport);
        expect(expectedResult).toContain(leaseLiabilityScheduleReport2);
      });

      it('should accept null and undefined values', () => {
        const leaseLiabilityScheduleReport: ILeaseLiabilityScheduleReport = { id: 123 };
        expectedResult = service.addLeaseLiabilityScheduleReportToCollectionIfMissing([], null, leaseLiabilityScheduleReport, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(leaseLiabilityScheduleReport);
      });

      it('should return initial array if no LeaseLiabilityScheduleReport is added', () => {
        const leaseLiabilityScheduleReportCollection: ILeaseLiabilityScheduleReport[] = [{ id: 123 }];
        expectedResult = service.addLeaseLiabilityScheduleReportToCollectionIfMissing(
          leaseLiabilityScheduleReportCollection,
          undefined,
          null
        );
        expect(expectedResult).toEqual(leaseLiabilityScheduleReportCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
