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
import { IRouDepreciationPostingReport, RouDepreciationPostingReport } from '../rou-depreciation-posting-report.model';

import { RouDepreciationPostingReportService } from './rou-depreciation-posting-report.service';

describe('RouDepreciationPostingReport Service', () => {
  let service: RouDepreciationPostingReportService;
  let httpMock: HttpTestingController;
  let elemDefault: IRouDepreciationPostingReport;
  let expectedResult: IRouDepreciationPostingReport | IRouDepreciationPostingReport[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(RouDepreciationPostingReportService);
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

    it('should create a RouDepreciationPostingReport', () => {
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

      service.create(new RouDepreciationPostingReport()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a RouDepreciationPostingReport', () => {
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

    it('should partial update a RouDepreciationPostingReport', () => {
      const patchObject = Object.assign(
        {
          requestId: 'BBBBBB',
          timeOfRequest: currentDate.format(DATE_TIME_FORMAT),
          reportIsCompiled: true,
          fileChecksum: 'BBBBBB',
          filename: 'BBBBBB',
          reportFile: 'BBBBBB',
        },
        new RouDepreciationPostingReport()
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

    it('should return a list of RouDepreciationPostingReport', () => {
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

    it('should delete a RouDepreciationPostingReport', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addRouDepreciationPostingReportToCollectionIfMissing', () => {
      it('should add a RouDepreciationPostingReport to an empty array', () => {
        const rouDepreciationPostingReport: IRouDepreciationPostingReport = { id: 123 };
        expectedResult = service.addRouDepreciationPostingReportToCollectionIfMissing([], rouDepreciationPostingReport);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(rouDepreciationPostingReport);
      });

      it('should not add a RouDepreciationPostingReport to an array that contains it', () => {
        const rouDepreciationPostingReport: IRouDepreciationPostingReport = { id: 123 };
        const rouDepreciationPostingReportCollection: IRouDepreciationPostingReport[] = [
          {
            ...rouDepreciationPostingReport,
          },
          { id: 456 },
        ];
        expectedResult = service.addRouDepreciationPostingReportToCollectionIfMissing(
          rouDepreciationPostingReportCollection,
          rouDepreciationPostingReport
        );
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a RouDepreciationPostingReport to an array that doesn't contain it", () => {
        const rouDepreciationPostingReport: IRouDepreciationPostingReport = { id: 123 };
        const rouDepreciationPostingReportCollection: IRouDepreciationPostingReport[] = [{ id: 456 }];
        expectedResult = service.addRouDepreciationPostingReportToCollectionIfMissing(
          rouDepreciationPostingReportCollection,
          rouDepreciationPostingReport
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(rouDepreciationPostingReport);
      });

      it('should add only unique RouDepreciationPostingReport to an array', () => {
        const rouDepreciationPostingReportArray: IRouDepreciationPostingReport[] = [{ id: 123 }, { id: 456 }, { id: 98075 }];
        const rouDepreciationPostingReportCollection: IRouDepreciationPostingReport[] = [{ id: 123 }];
        expectedResult = service.addRouDepreciationPostingReportToCollectionIfMissing(
          rouDepreciationPostingReportCollection,
          ...rouDepreciationPostingReportArray
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const rouDepreciationPostingReport: IRouDepreciationPostingReport = { id: 123 };
        const rouDepreciationPostingReport2: IRouDepreciationPostingReport = { id: 456 };
        expectedResult = service.addRouDepreciationPostingReportToCollectionIfMissing(
          [],
          rouDepreciationPostingReport,
          rouDepreciationPostingReport2
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(rouDepreciationPostingReport);
        expect(expectedResult).toContain(rouDepreciationPostingReport2);
      });

      it('should accept null and undefined values', () => {
        const rouDepreciationPostingReport: IRouDepreciationPostingReport = { id: 123 };
        expectedResult = service.addRouDepreciationPostingReportToCollectionIfMissing([], null, rouDepreciationPostingReport, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(rouDepreciationPostingReport);
      });

      it('should return initial array if no RouDepreciationPostingReport is added', () => {
        const rouDepreciationPostingReportCollection: IRouDepreciationPostingReport[] = [{ id: 123 }];
        expectedResult = service.addRouDepreciationPostingReportToCollectionIfMissing(
          rouDepreciationPostingReportCollection,
          undefined,
          null
        );
        expect(expectedResult).toEqual(rouDepreciationPostingReportCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
