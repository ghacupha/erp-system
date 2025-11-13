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
import { IRouAssetNBVReport, RouAssetNBVReport } from '../rou-asset-nbv-report.model';

import { RouAssetNBVReportService } from './rou-asset-nbv-report.service';

describe('RouAssetNBVReport Service', () => {
  let service: RouAssetNBVReportService;
  let httpMock: HttpTestingController;
  let elemDefault: IRouAssetNBVReport;
  let expectedResult: IRouAssetNBVReport | IRouAssetNBVReport[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(RouAssetNBVReportService);
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

    it('should create a RouAssetNBVReport', () => {
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

      service.create(new RouAssetNBVReport()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a RouAssetNBVReport', () => {
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

    it('should partial update a RouAssetNBVReport', () => {
      const patchObject = Object.assign(
        {
          requestId: 'BBBBBB',
          timeOfRequest: currentDate.format(DATE_TIME_FORMAT),
          filename: 'BBBBBB',
          reportParameters: 'BBBBBB',
        },
        new RouAssetNBVReport()
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

    it('should return a list of RouAssetNBVReport', () => {
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

    it('should delete a RouAssetNBVReport', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addRouAssetNBVReportToCollectionIfMissing', () => {
      it('should add a RouAssetNBVReport to an empty array', () => {
        const rouAssetNBVReport: IRouAssetNBVReport = { id: 123 };
        expectedResult = service.addRouAssetNBVReportToCollectionIfMissing([], rouAssetNBVReport);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(rouAssetNBVReport);
      });

      it('should not add a RouAssetNBVReport to an array that contains it', () => {
        const rouAssetNBVReport: IRouAssetNBVReport = { id: 123 };
        const rouAssetNBVReportCollection: IRouAssetNBVReport[] = [
          {
            ...rouAssetNBVReport,
          },
          { id: 456 },
        ];
        expectedResult = service.addRouAssetNBVReportToCollectionIfMissing(rouAssetNBVReportCollection, rouAssetNBVReport);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a RouAssetNBVReport to an array that doesn't contain it", () => {
        const rouAssetNBVReport: IRouAssetNBVReport = { id: 123 };
        const rouAssetNBVReportCollection: IRouAssetNBVReport[] = [{ id: 456 }];
        expectedResult = service.addRouAssetNBVReportToCollectionIfMissing(rouAssetNBVReportCollection, rouAssetNBVReport);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(rouAssetNBVReport);
      });

      it('should add only unique RouAssetNBVReport to an array', () => {
        const rouAssetNBVReportArray: IRouAssetNBVReport[] = [{ id: 123 }, { id: 456 }, { id: 79404 }];
        const rouAssetNBVReportCollection: IRouAssetNBVReport[] = [{ id: 123 }];
        expectedResult = service.addRouAssetNBVReportToCollectionIfMissing(rouAssetNBVReportCollection, ...rouAssetNBVReportArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const rouAssetNBVReport: IRouAssetNBVReport = { id: 123 };
        const rouAssetNBVReport2: IRouAssetNBVReport = { id: 456 };
        expectedResult = service.addRouAssetNBVReportToCollectionIfMissing([], rouAssetNBVReport, rouAssetNBVReport2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(rouAssetNBVReport);
        expect(expectedResult).toContain(rouAssetNBVReport2);
      });

      it('should accept null and undefined values', () => {
        const rouAssetNBVReport: IRouAssetNBVReport = { id: 123 };
        expectedResult = service.addRouAssetNBVReportToCollectionIfMissing([], null, rouAssetNBVReport, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(rouAssetNBVReport);
      });

      it('should return initial array if no RouAssetNBVReport is added', () => {
        const rouAssetNBVReportCollection: IRouAssetNBVReport[] = [{ id: 123 }];
        expectedResult = service.addRouAssetNBVReportToCollectionIfMissing(rouAssetNBVReportCollection, undefined, null);
        expect(expectedResult).toEqual(rouAssetNBVReportCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
