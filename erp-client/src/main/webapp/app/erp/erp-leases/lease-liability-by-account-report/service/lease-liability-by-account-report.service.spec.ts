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
import { ILeaseLiabilityByAccountReport, LeaseLiabilityByAccountReport } from '../lease-liability-by-account-report.model';

import { LeaseLiabilityByAccountReportService } from './lease-liability-by-account-report.service';

describe('LeaseLiabilityByAccountReport Service', () => {
  let service: LeaseLiabilityByAccountReportService;
  let httpMock: HttpTestingController;
  let elemDefault: ILeaseLiabilityByAccountReport;
  let expectedResult: ILeaseLiabilityByAccountReport | ILeaseLiabilityByAccountReport[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(LeaseLiabilityByAccountReportService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      reportId: 'AAAAAAA',
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

    it('should create a LeaseLiabilityByAccountReport', () => {
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

      service.create(new LeaseLiabilityByAccountReport()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a LeaseLiabilityByAccountReport', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          reportId: 'BBBBBB',
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

    it('should partial update a LeaseLiabilityByAccountReport', () => {
      const patchObject = Object.assign(
        {
          reportId: 'BBBBBB',
          filename: 'BBBBBB',
        },
        new LeaseLiabilityByAccountReport()
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

    it('should return a list of LeaseLiabilityByAccountReport', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          reportId: 'BBBBBB',
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

    it('should delete a LeaseLiabilityByAccountReport', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addLeaseLiabilityByAccountReportToCollectionIfMissing', () => {
      it('should add a LeaseLiabilityByAccountReport to an empty array', () => {
        const leaseLiabilityByAccountReport: ILeaseLiabilityByAccountReport = { id: 123 };
        expectedResult = service.addLeaseLiabilityByAccountReportToCollectionIfMissing([], leaseLiabilityByAccountReport);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(leaseLiabilityByAccountReport);
      });

      it('should not add a LeaseLiabilityByAccountReport to an array that contains it', () => {
        const leaseLiabilityByAccountReport: ILeaseLiabilityByAccountReport = { id: 123 };
        const leaseLiabilityByAccountReportCollection: ILeaseLiabilityByAccountReport[] = [
          {
            ...leaseLiabilityByAccountReport,
          },
          { id: 456 },
        ];
        expectedResult = service.addLeaseLiabilityByAccountReportToCollectionIfMissing(
          leaseLiabilityByAccountReportCollection,
          leaseLiabilityByAccountReport
        );
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a LeaseLiabilityByAccountReport to an array that doesn't contain it", () => {
        const leaseLiabilityByAccountReport: ILeaseLiabilityByAccountReport = { id: 123 };
        const leaseLiabilityByAccountReportCollection: ILeaseLiabilityByAccountReport[] = [{ id: 456 }];
        expectedResult = service.addLeaseLiabilityByAccountReportToCollectionIfMissing(
          leaseLiabilityByAccountReportCollection,
          leaseLiabilityByAccountReport
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(leaseLiabilityByAccountReport);
      });

      it('should add only unique LeaseLiabilityByAccountReport to an array', () => {
        const leaseLiabilityByAccountReportArray: ILeaseLiabilityByAccountReport[] = [{ id: 123 }, { id: 456 }, { id: 31705 }];
        const leaseLiabilityByAccountReportCollection: ILeaseLiabilityByAccountReport[] = [{ id: 123 }];
        expectedResult = service.addLeaseLiabilityByAccountReportToCollectionIfMissing(
          leaseLiabilityByAccountReportCollection,
          ...leaseLiabilityByAccountReportArray
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const leaseLiabilityByAccountReport: ILeaseLiabilityByAccountReport = { id: 123 };
        const leaseLiabilityByAccountReport2: ILeaseLiabilityByAccountReport = { id: 456 };
        expectedResult = service.addLeaseLiabilityByAccountReportToCollectionIfMissing(
          [],
          leaseLiabilityByAccountReport,
          leaseLiabilityByAccountReport2
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(leaseLiabilityByAccountReport);
        expect(expectedResult).toContain(leaseLiabilityByAccountReport2);
      });

      it('should accept null and undefined values', () => {
        const leaseLiabilityByAccountReport: ILeaseLiabilityByAccountReport = { id: 123 };
        expectedResult = service.addLeaseLiabilityByAccountReportToCollectionIfMissing([], null, leaseLiabilityByAccountReport, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(leaseLiabilityByAccountReport);
      });

      it('should return initial array if no LeaseLiabilityByAccountReport is added', () => {
        const leaseLiabilityByAccountReportCollection: ILeaseLiabilityByAccountReport[] = [{ id: 123 }];
        expectedResult = service.addLeaseLiabilityByAccountReportToCollectionIfMissing(
          leaseLiabilityByAccountReportCollection,
          undefined,
          null
        );
        expect(expectedResult).toEqual(leaseLiabilityByAccountReportCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
