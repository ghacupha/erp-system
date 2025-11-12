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

import { DATE_FORMAT, DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IRouDepreciationEntryReport, RouDepreciationEntryReport } from '../rou-depreciation-entry-report.model';

import { RouDepreciationEntryReportService } from './rou-depreciation-entry-report.service';

describe('RouDepreciationEntryReport Service', () => {
  let service: RouDepreciationEntryReportService;
  let httpMock: HttpTestingController;
  let elemDefault: IRouDepreciationEntryReport;
  let expectedResult: IRouDepreciationEntryReport | IRouDepreciationEntryReport[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(RouDepreciationEntryReportService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      requestId: 'AAAAAAA',
      timeOfRequest: currentDate,
      reportIsCompiled: false,
      periodStartDate: currentDate,
      periodEndDate: currentDate,
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
          periodStartDate: currentDate.format(DATE_FORMAT),
          periodEndDate: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a RouDepreciationEntryReport', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          timeOfRequest: currentDate.format(DATE_TIME_FORMAT),
          periodStartDate: currentDate.format(DATE_FORMAT),
          periodEndDate: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          timeOfRequest: currentDate,
          periodStartDate: currentDate,
          periodEndDate: currentDate,
        },
        returnedFromService
      );

      service.create(new RouDepreciationEntryReport()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a RouDepreciationEntryReport', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          requestId: 'BBBBBB',
          timeOfRequest: currentDate.format(DATE_TIME_FORMAT),
          reportIsCompiled: true,
          periodStartDate: currentDate.format(DATE_FORMAT),
          periodEndDate: currentDate.format(DATE_FORMAT),
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
          periodStartDate: currentDate,
          periodEndDate: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a RouDepreciationEntryReport', () => {
      const patchObject = Object.assign(
        {
          requestId: 'BBBBBB',
          timeOfRequest: currentDate.format(DATE_TIME_FORMAT),
          fileChecksum: 'BBBBBB',
          tampered: true,
          reportFile: 'BBBBBB',
        },
        new RouDepreciationEntryReport()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          timeOfRequest: currentDate,
          periodStartDate: currentDate,
          periodEndDate: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of RouDepreciationEntryReport', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          requestId: 'BBBBBB',
          timeOfRequest: currentDate.format(DATE_TIME_FORMAT),
          reportIsCompiled: true,
          periodStartDate: currentDate.format(DATE_FORMAT),
          periodEndDate: currentDate.format(DATE_FORMAT),
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
          periodStartDate: currentDate,
          periodEndDate: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a RouDepreciationEntryReport', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addRouDepreciationEntryReportToCollectionIfMissing', () => {
      it('should add a RouDepreciationEntryReport to an empty array', () => {
        const rouDepreciationEntryReport: IRouDepreciationEntryReport = { id: 123 };
        expectedResult = service.addRouDepreciationEntryReportToCollectionIfMissing([], rouDepreciationEntryReport);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(rouDepreciationEntryReport);
      });

      it('should not add a RouDepreciationEntryReport to an array that contains it', () => {
        const rouDepreciationEntryReport: IRouDepreciationEntryReport = { id: 123 };
        const rouDepreciationEntryReportCollection: IRouDepreciationEntryReport[] = [
          {
            ...rouDepreciationEntryReport,
          },
          { id: 456 },
        ];
        expectedResult = service.addRouDepreciationEntryReportToCollectionIfMissing(
          rouDepreciationEntryReportCollection,
          rouDepreciationEntryReport
        );
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a RouDepreciationEntryReport to an array that doesn't contain it", () => {
        const rouDepreciationEntryReport: IRouDepreciationEntryReport = { id: 123 };
        const rouDepreciationEntryReportCollection: IRouDepreciationEntryReport[] = [{ id: 456 }];
        expectedResult = service.addRouDepreciationEntryReportToCollectionIfMissing(
          rouDepreciationEntryReportCollection,
          rouDepreciationEntryReport
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(rouDepreciationEntryReport);
      });

      it('should add only unique RouDepreciationEntryReport to an array', () => {
        const rouDepreciationEntryReportArray: IRouDepreciationEntryReport[] = [{ id: 123 }, { id: 456 }, { id: 35066 }];
        const rouDepreciationEntryReportCollection: IRouDepreciationEntryReport[] = [{ id: 123 }];
        expectedResult = service.addRouDepreciationEntryReportToCollectionIfMissing(
          rouDepreciationEntryReportCollection,
          ...rouDepreciationEntryReportArray
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const rouDepreciationEntryReport: IRouDepreciationEntryReport = { id: 123 };
        const rouDepreciationEntryReport2: IRouDepreciationEntryReport = { id: 456 };
        expectedResult = service.addRouDepreciationEntryReportToCollectionIfMissing(
          [],
          rouDepreciationEntryReport,
          rouDepreciationEntryReport2
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(rouDepreciationEntryReport);
        expect(expectedResult).toContain(rouDepreciationEntryReport2);
      });

      it('should accept null and undefined values', () => {
        const rouDepreciationEntryReport: IRouDepreciationEntryReport = { id: 123 };
        expectedResult = service.addRouDepreciationEntryReportToCollectionIfMissing([], null, rouDepreciationEntryReport, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(rouDepreciationEntryReport);
      });

      it('should return initial array if no RouDepreciationEntryReport is added', () => {
        const rouDepreciationEntryReportCollection: IRouDepreciationEntryReport[] = [{ id: 123 }];
        expectedResult = service.addRouDepreciationEntryReportToCollectionIfMissing(rouDepreciationEntryReportCollection, undefined, null);
        expect(expectedResult).toEqual(rouDepreciationEntryReportCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
