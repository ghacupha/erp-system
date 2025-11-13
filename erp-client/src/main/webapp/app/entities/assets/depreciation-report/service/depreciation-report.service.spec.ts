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
import { IDepreciationReport, DepreciationReport } from '../depreciation-report.model';

import { DepreciationReportService } from './depreciation-report.service';

describe('DepreciationReport Service', () => {
  let service: DepreciationReportService;
  let httpMock: HttpTestingController;
  let elemDefault: IDepreciationReport;
  let expectedResult: IDepreciationReport | IDepreciationReport[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(DepreciationReportService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      reportName: 'AAAAAAA',
      timeOfReportRequest: currentDate,
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
          timeOfReportRequest: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a DepreciationReport', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          timeOfReportRequest: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          timeOfReportRequest: currentDate,
        },
        returnedFromService
      );

      service.create(new DepreciationReport()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a DepreciationReport', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          reportName: 'BBBBBB',
          timeOfReportRequest: currentDate.format(DATE_TIME_FORMAT),
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
          timeOfReportRequest: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a DepreciationReport', () => {
      const patchObject = Object.assign(
        {
          reportName: 'BBBBBB',
          timeOfReportRequest: currentDate.format(DATE_TIME_FORMAT),
          fileChecksum: 'BBBBBB',
          filename: 'BBBBBB',
        },
        new DepreciationReport()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          timeOfReportRequest: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of DepreciationReport', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          reportName: 'BBBBBB',
          timeOfReportRequest: currentDate.format(DATE_TIME_FORMAT),
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
          timeOfReportRequest: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a DepreciationReport', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addDepreciationReportToCollectionIfMissing', () => {
      it('should add a DepreciationReport to an empty array', () => {
        const depreciationReport: IDepreciationReport = { id: 123 };
        expectedResult = service.addDepreciationReportToCollectionIfMissing([], depreciationReport);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(depreciationReport);
      });

      it('should not add a DepreciationReport to an array that contains it', () => {
        const depreciationReport: IDepreciationReport = { id: 123 };
        const depreciationReportCollection: IDepreciationReport[] = [
          {
            ...depreciationReport,
          },
          { id: 456 },
        ];
        expectedResult = service.addDepreciationReportToCollectionIfMissing(depreciationReportCollection, depreciationReport);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a DepreciationReport to an array that doesn't contain it", () => {
        const depreciationReport: IDepreciationReport = { id: 123 };
        const depreciationReportCollection: IDepreciationReport[] = [{ id: 456 }];
        expectedResult = service.addDepreciationReportToCollectionIfMissing(depreciationReportCollection, depreciationReport);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(depreciationReport);
      });

      it('should add only unique DepreciationReport to an array', () => {
        const depreciationReportArray: IDepreciationReport[] = [{ id: 123 }, { id: 456 }, { id: 58246 }];
        const depreciationReportCollection: IDepreciationReport[] = [{ id: 123 }];
        expectedResult = service.addDepreciationReportToCollectionIfMissing(depreciationReportCollection, ...depreciationReportArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const depreciationReport: IDepreciationReport = { id: 123 };
        const depreciationReport2: IDepreciationReport = { id: 456 };
        expectedResult = service.addDepreciationReportToCollectionIfMissing([], depreciationReport, depreciationReport2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(depreciationReport);
        expect(expectedResult).toContain(depreciationReport2);
      });

      it('should accept null and undefined values', () => {
        const depreciationReport: IDepreciationReport = { id: 123 };
        expectedResult = service.addDepreciationReportToCollectionIfMissing([], null, depreciationReport, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(depreciationReport);
      });

      it('should return initial array if no DepreciationReport is added', () => {
        const depreciationReportCollection: IDepreciationReport[] = [{ id: 123 }];
        expectedResult = service.addDepreciationReportToCollectionIfMissing(depreciationReportCollection, undefined, null);
        expect(expectedResult).toEqual(depreciationReportCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
