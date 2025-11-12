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
import { IWIPListReport, WIPListReport } from '../wip-list-report.model';

import { WIPListReportService } from './wip-list-report.service';

describe('WIPListReport Service', () => {
  let service: WIPListReportService;
  let httpMock: HttpTestingController;
  let elemDefault: IWIPListReport;
  let expectedResult: IWIPListReport | IWIPListReport[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(WIPListReportService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      timeOfRequest: currentDate,
      requestId: 'AAAAAAA',
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

    it('should create a WIPListReport', () => {
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

      service.create(new WIPListReport()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a WIPListReport', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          timeOfRequest: currentDate.format(DATE_TIME_FORMAT),
          requestId: 'BBBBBB',
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

    it('should partial update a WIPListReport', () => {
      const patchObject = Object.assign(
        {
          timeOfRequest: currentDate.format(DATE_TIME_FORMAT),
          tampered: true,
          filename: 'BBBBBB',
          reportFile: 'BBBBBB',
        },
        new WIPListReport()
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

    it('should return a list of WIPListReport', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          timeOfRequest: currentDate.format(DATE_TIME_FORMAT),
          requestId: 'BBBBBB',
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

    it('should delete a WIPListReport', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addWIPListReportToCollectionIfMissing', () => {
      it('should add a WIPListReport to an empty array', () => {
        const wIPListReport: IWIPListReport = { id: 123 };
        expectedResult = service.addWIPListReportToCollectionIfMissing([], wIPListReport);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(wIPListReport);
      });

      it('should not add a WIPListReport to an array that contains it', () => {
        const wIPListReport: IWIPListReport = { id: 123 };
        const wIPListReportCollection: IWIPListReport[] = [
          {
            ...wIPListReport,
          },
          { id: 456 },
        ];
        expectedResult = service.addWIPListReportToCollectionIfMissing(wIPListReportCollection, wIPListReport);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a WIPListReport to an array that doesn't contain it", () => {
        const wIPListReport: IWIPListReport = { id: 123 };
        const wIPListReportCollection: IWIPListReport[] = [{ id: 456 }];
        expectedResult = service.addWIPListReportToCollectionIfMissing(wIPListReportCollection, wIPListReport);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(wIPListReport);
      });

      it('should add only unique WIPListReport to an array', () => {
        const wIPListReportArray: IWIPListReport[] = [{ id: 123 }, { id: 456 }, { id: 18372 }];
        const wIPListReportCollection: IWIPListReport[] = [{ id: 123 }];
        expectedResult = service.addWIPListReportToCollectionIfMissing(wIPListReportCollection, ...wIPListReportArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const wIPListReport: IWIPListReport = { id: 123 };
        const wIPListReport2: IWIPListReport = { id: 456 };
        expectedResult = service.addWIPListReportToCollectionIfMissing([], wIPListReport, wIPListReport2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(wIPListReport);
        expect(expectedResult).toContain(wIPListReport2);
      });

      it('should accept null and undefined values', () => {
        const wIPListReport: IWIPListReport = { id: 123 };
        expectedResult = service.addWIPListReportToCollectionIfMissing([], null, wIPListReport, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(wIPListReport);
      });

      it('should return initial array if no WIPListReport is added', () => {
        const wIPListReportCollection: IWIPListReport[] = [{ id: 123 }];
        expectedResult = service.addWIPListReportToCollectionIfMissing(wIPListReportCollection, undefined, null);
        expect(expectedResult).toEqual(wIPListReportCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
