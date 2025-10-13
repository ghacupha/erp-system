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
import { IWIPTransferListReport, WIPTransferListReport } from '../wip-transfer-list-report.model';

import { WIPTransferListReportService } from './wip-transfer-list-report.service';

describe('WIPTransferListReport Service', () => {
  let service: WIPTransferListReportService;
  let httpMock: HttpTestingController;
  let elemDefault: IWIPTransferListReport;
  let expectedResult: IWIPTransferListReport | IWIPTransferListReport[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(WIPTransferListReportService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      timeOfRequest: currentDate,
      requestId: 'AAAAAAA',
      fileChecksum: 'AAAAAAA',
      tempered: false,
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

    it('should create a WIPTransferListReport', () => {
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

      service.create(new WIPTransferListReport()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a WIPTransferListReport', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          timeOfRequest: currentDate.format(DATE_TIME_FORMAT),
          requestId: 'BBBBBB',
          fileChecksum: 'BBBBBB',
          tempered: true,
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

    it('should partial update a WIPTransferListReport', () => {
      const patchObject = Object.assign(
        {
          requestId: 'BBBBBB',
          fileChecksum: 'BBBBBB',
          filename: 'BBBBBB',
          reportParameters: 'BBBBBB',
        },
        new WIPTransferListReport()
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

    it('should return a list of WIPTransferListReport', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          timeOfRequest: currentDate.format(DATE_TIME_FORMAT),
          requestId: 'BBBBBB',
          fileChecksum: 'BBBBBB',
          tempered: true,
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

    it('should delete a WIPTransferListReport', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addWIPTransferListReportToCollectionIfMissing', () => {
      it('should add a WIPTransferListReport to an empty array', () => {
        const wIPTransferListReport: IWIPTransferListReport = { id: 123 };
        expectedResult = service.addWIPTransferListReportToCollectionIfMissing([], wIPTransferListReport);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(wIPTransferListReport);
      });

      it('should not add a WIPTransferListReport to an array that contains it', () => {
        const wIPTransferListReport: IWIPTransferListReport = { id: 123 };
        const wIPTransferListReportCollection: IWIPTransferListReport[] = [
          {
            ...wIPTransferListReport,
          },
          { id: 456 },
        ];
        expectedResult = service.addWIPTransferListReportToCollectionIfMissing(wIPTransferListReportCollection, wIPTransferListReport);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a WIPTransferListReport to an array that doesn't contain it", () => {
        const wIPTransferListReport: IWIPTransferListReport = { id: 123 };
        const wIPTransferListReportCollection: IWIPTransferListReport[] = [{ id: 456 }];
        expectedResult = service.addWIPTransferListReportToCollectionIfMissing(wIPTransferListReportCollection, wIPTransferListReport);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(wIPTransferListReport);
      });

      it('should add only unique WIPTransferListReport to an array', () => {
        const wIPTransferListReportArray: IWIPTransferListReport[] = [{ id: 123 }, { id: 456 }, { id: 41723 }];
        const wIPTransferListReportCollection: IWIPTransferListReport[] = [{ id: 123 }];
        expectedResult = service.addWIPTransferListReportToCollectionIfMissing(
          wIPTransferListReportCollection,
          ...wIPTransferListReportArray
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const wIPTransferListReport: IWIPTransferListReport = { id: 123 };
        const wIPTransferListReport2: IWIPTransferListReport = { id: 456 };
        expectedResult = service.addWIPTransferListReportToCollectionIfMissing([], wIPTransferListReport, wIPTransferListReport2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(wIPTransferListReport);
        expect(expectedResult).toContain(wIPTransferListReport2);
      });

      it('should accept null and undefined values', () => {
        const wIPTransferListReport: IWIPTransferListReport = { id: 123 };
        expectedResult = service.addWIPTransferListReportToCollectionIfMissing([], null, wIPTransferListReport, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(wIPTransferListReport);
      });

      it('should return initial array if no WIPTransferListReport is added', () => {
        const wIPTransferListReportCollection: IWIPTransferListReport[] = [{ id: 123 }];
        expectedResult = service.addWIPTransferListReportToCollectionIfMissing(wIPTransferListReportCollection, undefined, null);
        expect(expectedResult).toEqual(wIPTransferListReportCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
