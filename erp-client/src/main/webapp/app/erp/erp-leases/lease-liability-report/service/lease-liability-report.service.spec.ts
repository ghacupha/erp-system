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
import { ILeaseLiabilityReport, LeaseLiabilityReport } from '../lease-liability-report.model';

import { LeaseLiabilityReportService } from './lease-liability-report.service';

describe('LeaseLiabilityReport Service', () => {
  let service: LeaseLiabilityReportService;
  let httpMock: HttpTestingController;
  let elemDefault: ILeaseLiabilityReport;
  let expectedResult: ILeaseLiabilityReport | ILeaseLiabilityReport[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(LeaseLiabilityReportService);
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

    it('should create a LeaseLiabilityReport', () => {
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

      service.create(new LeaseLiabilityReport()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a LeaseLiabilityReport', () => {
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

    it('should partial update a LeaseLiabilityReport', () => {
      const patchObject = Object.assign(
        {
          filename: 'BBBBBB',
        },
        new LeaseLiabilityReport()
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

    it('should return a list of LeaseLiabilityReport', () => {
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

    it('should delete a LeaseLiabilityReport', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addLeaseLiabilityReportToCollectionIfMissing', () => {
      it('should add a LeaseLiabilityReport to an empty array', () => {
        const leaseLiabilityReport: ILeaseLiabilityReport = { id: 123 };
        expectedResult = service.addLeaseLiabilityReportToCollectionIfMissing([], leaseLiabilityReport);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(leaseLiabilityReport);
      });

      it('should not add a LeaseLiabilityReport to an array that contains it', () => {
        const leaseLiabilityReport: ILeaseLiabilityReport = { id: 123 };
        const leaseLiabilityReportCollection: ILeaseLiabilityReport[] = [
          {
            ...leaseLiabilityReport,
          },
          { id: 456 },
        ];
        expectedResult = service.addLeaseLiabilityReportToCollectionIfMissing(leaseLiabilityReportCollection, leaseLiabilityReport);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a LeaseLiabilityReport to an array that doesn't contain it", () => {
        const leaseLiabilityReport: ILeaseLiabilityReport = { id: 123 };
        const leaseLiabilityReportCollection: ILeaseLiabilityReport[] = [{ id: 456 }];
        expectedResult = service.addLeaseLiabilityReportToCollectionIfMissing(leaseLiabilityReportCollection, leaseLiabilityReport);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(leaseLiabilityReport);
      });

      it('should add only unique LeaseLiabilityReport to an array', () => {
        const leaseLiabilityReportArray: ILeaseLiabilityReport[] = [{ id: 123 }, { id: 456 }, { id: 22520 }];
        const leaseLiabilityReportCollection: ILeaseLiabilityReport[] = [{ id: 123 }];
        expectedResult = service.addLeaseLiabilityReportToCollectionIfMissing(leaseLiabilityReportCollection, ...leaseLiabilityReportArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const leaseLiabilityReport: ILeaseLiabilityReport = { id: 123 };
        const leaseLiabilityReport2: ILeaseLiabilityReport = { id: 456 };
        expectedResult = service.addLeaseLiabilityReportToCollectionIfMissing([], leaseLiabilityReport, leaseLiabilityReport2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(leaseLiabilityReport);
        expect(expectedResult).toContain(leaseLiabilityReport2);
      });

      it('should accept null and undefined values', () => {
        const leaseLiabilityReport: ILeaseLiabilityReport = { id: 123 };
        expectedResult = service.addLeaseLiabilityReportToCollectionIfMissing([], null, leaseLiabilityReport, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(leaseLiabilityReport);
      });

      it('should return initial array if no LeaseLiabilityReport is added', () => {
        const leaseLiabilityReportCollection: ILeaseLiabilityReport[] = [{ id: 123 }];
        expectedResult = service.addLeaseLiabilityReportToCollectionIfMissing(leaseLiabilityReportCollection, undefined, null);
        expect(expectedResult).toEqual(leaseLiabilityReportCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
