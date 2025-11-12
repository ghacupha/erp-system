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
import { ILeaseLiabilityPostingReport, LeaseLiabilityPostingReport } from '../lease-liability-posting-report.model';

import { LeaseLiabilityPostingReportService } from './lease-liability-posting-report.service';

describe('LeaseLiabilityPostingReport Service', () => {
  let service: LeaseLiabilityPostingReportService;
  let httpMock: HttpTestingController;
  let elemDefault: ILeaseLiabilityPostingReport;
  let expectedResult: ILeaseLiabilityPostingReport | ILeaseLiabilityPostingReport[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(LeaseLiabilityPostingReportService);
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

    it('should create a LeaseLiabilityPostingReport', () => {
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

      service.create(new LeaseLiabilityPostingReport()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a LeaseLiabilityPostingReport', () => {
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

    it('should partial update a LeaseLiabilityPostingReport', () => {
      const patchObject = Object.assign(
        {
          tampered: true,
          filename: 'BBBBBB',
          reportParameters: 'BBBBBB',
        },
        new LeaseLiabilityPostingReport()
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

    it('should return a list of LeaseLiabilityPostingReport', () => {
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

    it('should delete a LeaseLiabilityPostingReport', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addLeaseLiabilityPostingReportToCollectionIfMissing', () => {
      it('should add a LeaseLiabilityPostingReport to an empty array', () => {
        const leaseLiabilityPostingReport: ILeaseLiabilityPostingReport = { id: 123 };
        expectedResult = service.addLeaseLiabilityPostingReportToCollectionIfMissing([], leaseLiabilityPostingReport);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(leaseLiabilityPostingReport);
      });

      it('should not add a LeaseLiabilityPostingReport to an array that contains it', () => {
        const leaseLiabilityPostingReport: ILeaseLiabilityPostingReport = { id: 123 };
        const leaseLiabilityPostingReportCollection: ILeaseLiabilityPostingReport[] = [
          {
            ...leaseLiabilityPostingReport,
          },
          { id: 456 },
        ];
        expectedResult = service.addLeaseLiabilityPostingReportToCollectionIfMissing(
          leaseLiabilityPostingReportCollection,
          leaseLiabilityPostingReport
        );
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a LeaseLiabilityPostingReport to an array that doesn't contain it", () => {
        const leaseLiabilityPostingReport: ILeaseLiabilityPostingReport = { id: 123 };
        const leaseLiabilityPostingReportCollection: ILeaseLiabilityPostingReport[] = [{ id: 456 }];
        expectedResult = service.addLeaseLiabilityPostingReportToCollectionIfMissing(
          leaseLiabilityPostingReportCollection,
          leaseLiabilityPostingReport
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(leaseLiabilityPostingReport);
      });

      it('should add only unique LeaseLiabilityPostingReport to an array', () => {
        const leaseLiabilityPostingReportArray: ILeaseLiabilityPostingReport[] = [{ id: 123 }, { id: 456 }, { id: 19560 }];
        const leaseLiabilityPostingReportCollection: ILeaseLiabilityPostingReport[] = [{ id: 123 }];
        expectedResult = service.addLeaseLiabilityPostingReportToCollectionIfMissing(
          leaseLiabilityPostingReportCollection,
          ...leaseLiabilityPostingReportArray
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const leaseLiabilityPostingReport: ILeaseLiabilityPostingReport = { id: 123 };
        const leaseLiabilityPostingReport2: ILeaseLiabilityPostingReport = { id: 456 };
        expectedResult = service.addLeaseLiabilityPostingReportToCollectionIfMissing(
          [],
          leaseLiabilityPostingReport,
          leaseLiabilityPostingReport2
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(leaseLiabilityPostingReport);
        expect(expectedResult).toContain(leaseLiabilityPostingReport2);
      });

      it('should accept null and undefined values', () => {
        const leaseLiabilityPostingReport: ILeaseLiabilityPostingReport = { id: 123 };
        expectedResult = service.addLeaseLiabilityPostingReportToCollectionIfMissing([], null, leaseLiabilityPostingReport, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(leaseLiabilityPostingReport);
      });

      it('should return initial array if no LeaseLiabilityPostingReport is added', () => {
        const leaseLiabilityPostingReportCollection: ILeaseLiabilityPostingReport[] = [{ id: 123 }];
        expectedResult = service.addLeaseLiabilityPostingReportToCollectionIfMissing(
          leaseLiabilityPostingReportCollection,
          undefined,
          null
        );
        expect(expectedResult).toEqual(leaseLiabilityPostingReportCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
