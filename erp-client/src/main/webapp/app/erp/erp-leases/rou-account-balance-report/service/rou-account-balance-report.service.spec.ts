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
import { IRouAccountBalanceReport, RouAccountBalanceReport } from '../rou-account-balance-report.model';

import { RouAccountBalanceReportService } from './rou-account-balance-report.service';

describe('RouAccountBalanceReport Service', () => {
  let service: RouAccountBalanceReportService;
  let httpMock: HttpTestingController;
  let elemDefault: IRouAccountBalanceReport;
  let expectedResult: IRouAccountBalanceReport | IRouAccountBalanceReport[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(RouAccountBalanceReportService);
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

    it('should create a RouAccountBalanceReport', () => {
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

      service.create(new RouAccountBalanceReport()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a RouAccountBalanceReport', () => {
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

    it('should partial update a RouAccountBalanceReport', () => {
      const patchObject = Object.assign(
        {
          fileChecksum: 'BBBBBB',
          filename: 'BBBBBB',
          reportParameters: 'BBBBBB',
        },
        new RouAccountBalanceReport()
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

    it('should return a list of RouAccountBalanceReport', () => {
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

    it('should delete a RouAccountBalanceReport', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addRouAccountBalanceReportToCollectionIfMissing', () => {
      it('should add a RouAccountBalanceReport to an empty array', () => {
        const rouAccountBalanceReport: IRouAccountBalanceReport = { id: 123 };
        expectedResult = service.addRouAccountBalanceReportToCollectionIfMissing([], rouAccountBalanceReport);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(rouAccountBalanceReport);
      });

      it('should not add a RouAccountBalanceReport to an array that contains it', () => {
        const rouAccountBalanceReport: IRouAccountBalanceReport = { id: 123 };
        const rouAccountBalanceReportCollection: IRouAccountBalanceReport[] = [
          {
            ...rouAccountBalanceReport,
          },
          { id: 456 },
        ];
        expectedResult = service.addRouAccountBalanceReportToCollectionIfMissing(
          rouAccountBalanceReportCollection,
          rouAccountBalanceReport
        );
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a RouAccountBalanceReport to an array that doesn't contain it", () => {
        const rouAccountBalanceReport: IRouAccountBalanceReport = { id: 123 };
        const rouAccountBalanceReportCollection: IRouAccountBalanceReport[] = [{ id: 456 }];
        expectedResult = service.addRouAccountBalanceReportToCollectionIfMissing(
          rouAccountBalanceReportCollection,
          rouAccountBalanceReport
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(rouAccountBalanceReport);
      });

      it('should add only unique RouAccountBalanceReport to an array', () => {
        const rouAccountBalanceReportArray: IRouAccountBalanceReport[] = [{ id: 123 }, { id: 456 }, { id: 21113 }];
        const rouAccountBalanceReportCollection: IRouAccountBalanceReport[] = [{ id: 123 }];
        expectedResult = service.addRouAccountBalanceReportToCollectionIfMissing(
          rouAccountBalanceReportCollection,
          ...rouAccountBalanceReportArray
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const rouAccountBalanceReport: IRouAccountBalanceReport = { id: 123 };
        const rouAccountBalanceReport2: IRouAccountBalanceReport = { id: 456 };
        expectedResult = service.addRouAccountBalanceReportToCollectionIfMissing([], rouAccountBalanceReport, rouAccountBalanceReport2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(rouAccountBalanceReport);
        expect(expectedResult).toContain(rouAccountBalanceReport2);
      });

      it('should accept null and undefined values', () => {
        const rouAccountBalanceReport: IRouAccountBalanceReport = { id: 123 };
        expectedResult = service.addRouAccountBalanceReportToCollectionIfMissing([], null, rouAccountBalanceReport, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(rouAccountBalanceReport);
      });

      it('should return initial array if no RouAccountBalanceReport is added', () => {
        const rouAccountBalanceReportCollection: IRouAccountBalanceReport[] = [{ id: 123 }];
        expectedResult = service.addRouAccountBalanceReportToCollectionIfMissing(rouAccountBalanceReportCollection, undefined, null);
        expect(expectedResult).toEqual(rouAccountBalanceReportCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
