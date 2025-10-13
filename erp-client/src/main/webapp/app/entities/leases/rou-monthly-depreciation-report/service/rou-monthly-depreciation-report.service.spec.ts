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
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IRouMonthlyDepreciationReport, RouMonthlyDepreciationReport } from '../rou-monthly-depreciation-report.model';

import { RouMonthlyDepreciationReportService } from './rou-monthly-depreciation-report.service';

describe('RouMonthlyDepreciationReport Service', () => {
  let service: RouMonthlyDepreciationReportService;
  let httpMock: HttpTestingController;
  let elemDefault: IRouMonthlyDepreciationReport;
  let expectedResult: IRouMonthlyDepreciationReport | IRouMonthlyDepreciationReport[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(RouMonthlyDepreciationReportService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      requestId: 'AAAAAAA',
      timeOfRequest: currentDate,
      reportIsCompiled: false,
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

    it('should create a RouMonthlyDepreciationReport', () => {
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

      service.create(new RouMonthlyDepreciationReport()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a RouMonthlyDepreciationReport', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          requestId: 'BBBBBB',
          timeOfRequest: currentDate.format(DATE_TIME_FORMAT),
          reportIsCompiled: true,
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

    it('should partial update a RouMonthlyDepreciationReport', () => {
      const patchObject = Object.assign(
        {
          timeOfRequest: currentDate.format(DATE_TIME_FORMAT),
          reportIsCompiled: true,
          fileChecksum: 'BBBBBB',
          tampered: true,
          filename: 'BBBBBB',
        },
        new RouMonthlyDepreciationReport()
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

    it('should return a list of RouMonthlyDepreciationReport', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          requestId: 'BBBBBB',
          timeOfRequest: currentDate.format(DATE_TIME_FORMAT),
          reportIsCompiled: true,
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

    it('should delete a RouMonthlyDepreciationReport', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addRouMonthlyDepreciationReportToCollectionIfMissing', () => {
      it('should add a RouMonthlyDepreciationReport to an empty array', () => {
        const rouMonthlyDepreciationReport: IRouMonthlyDepreciationReport = { id: 123 };
        expectedResult = service.addRouMonthlyDepreciationReportToCollectionIfMissing([], rouMonthlyDepreciationReport);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(rouMonthlyDepreciationReport);
      });

      it('should not add a RouMonthlyDepreciationReport to an array that contains it', () => {
        const rouMonthlyDepreciationReport: IRouMonthlyDepreciationReport = { id: 123 };
        const rouMonthlyDepreciationReportCollection: IRouMonthlyDepreciationReport[] = [
          {
            ...rouMonthlyDepreciationReport,
          },
          { id: 456 },
        ];
        expectedResult = service.addRouMonthlyDepreciationReportToCollectionIfMissing(
          rouMonthlyDepreciationReportCollection,
          rouMonthlyDepreciationReport
        );
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a RouMonthlyDepreciationReport to an array that doesn't contain it", () => {
        const rouMonthlyDepreciationReport: IRouMonthlyDepreciationReport = { id: 123 };
        const rouMonthlyDepreciationReportCollection: IRouMonthlyDepreciationReport[] = [{ id: 456 }];
        expectedResult = service.addRouMonthlyDepreciationReportToCollectionIfMissing(
          rouMonthlyDepreciationReportCollection,
          rouMonthlyDepreciationReport
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(rouMonthlyDepreciationReport);
      });

      it('should add only unique RouMonthlyDepreciationReport to an array', () => {
        const rouMonthlyDepreciationReportArray: IRouMonthlyDepreciationReport[] = [{ id: 123 }, { id: 456 }, { id: 27077 }];
        const rouMonthlyDepreciationReportCollection: IRouMonthlyDepreciationReport[] = [{ id: 123 }];
        expectedResult = service.addRouMonthlyDepreciationReportToCollectionIfMissing(
          rouMonthlyDepreciationReportCollection,
          ...rouMonthlyDepreciationReportArray
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const rouMonthlyDepreciationReport: IRouMonthlyDepreciationReport = { id: 123 };
        const rouMonthlyDepreciationReport2: IRouMonthlyDepreciationReport = { id: 456 };
        expectedResult = service.addRouMonthlyDepreciationReportToCollectionIfMissing(
          [],
          rouMonthlyDepreciationReport,
          rouMonthlyDepreciationReport2
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(rouMonthlyDepreciationReport);
        expect(expectedResult).toContain(rouMonthlyDepreciationReport2);
      });

      it('should accept null and undefined values', () => {
        const rouMonthlyDepreciationReport: IRouMonthlyDepreciationReport = { id: 123 };
        expectedResult = service.addRouMonthlyDepreciationReportToCollectionIfMissing([], null, rouMonthlyDepreciationReport, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(rouMonthlyDepreciationReport);
      });

      it('should return initial array if no RouMonthlyDepreciationReport is added', () => {
        const rouMonthlyDepreciationReportCollection: IRouMonthlyDepreciationReport[] = [{ id: 123 }];
        expectedResult = service.addRouMonthlyDepreciationReportToCollectionIfMissing(
          rouMonthlyDepreciationReportCollection,
          undefined,
          null
        );
        expect(expectedResult).toEqual(rouMonthlyDepreciationReportCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
