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

import { DATE_FORMAT } from 'app/config/input.constants';
import { IPrepaymentReport } from '../prepayment-report.model';

import { PrepaymentReportService } from './prepayment-report.service';

describe('PrepaymentReport Service', () => {
  let service: PrepaymentReportService;
  let httpMock: HttpTestingController;
  let elemDefault: IPrepaymentReport;
  let expectedResult: IPrepaymentReport | IPrepaymentReport[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(PrepaymentReportService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      catalogueNumber: 'AAAAAAA',
      particulars: 'AAAAAAA',
      dealerName: 'AAAAAAA',
      paymentNumber: 'AAAAAAA',
      paymentDate: currentDate,
      currencyCode: 'AAAAAAA',
      prepaymentAmount: 0,
      amortisedAmount: 0,
      outstandingAmount: 0,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          paymentDate: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should return a list of PrepaymentReport', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          catalogueNumber: 'BBBBBB',
          particulars: 'BBBBBB',
          dealerName: 'BBBBBB',
          paymentNumber: 'BBBBBB',
          paymentDate: currentDate.format(DATE_FORMAT),
          currencyCode: 'BBBBBB',
          prepaymentAmount: 1,
          amortisedAmount: 1,
          outstandingAmount: 1,
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          paymentDate: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    describe('addPrepaymentReportToCollectionIfMissing', () => {
      it('should add a PrepaymentReport to an empty array', () => {
        const prepaymentReport: IPrepaymentReport = { id: 123 };
        expectedResult = service.addPrepaymentReportToCollectionIfMissing([], prepaymentReport);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(prepaymentReport);
      });

      it('should not add a PrepaymentReport to an array that contains it', () => {
        const prepaymentReport: IPrepaymentReport = { id: 123 };
        const prepaymentReportCollection: IPrepaymentReport[] = [
          {
            ...prepaymentReport,
          },
          { id: 456 },
        ];
        expectedResult = service.addPrepaymentReportToCollectionIfMissing(prepaymentReportCollection, prepaymentReport);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a PrepaymentReport to an array that doesn't contain it", () => {
        const prepaymentReport: IPrepaymentReport = { id: 123 };
        const prepaymentReportCollection: IPrepaymentReport[] = [{ id: 456 }];
        expectedResult = service.addPrepaymentReportToCollectionIfMissing(prepaymentReportCollection, prepaymentReport);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(prepaymentReport);
      });

      it('should add only unique PrepaymentReport to an array', () => {
        const prepaymentReportArray: IPrepaymentReport[] = [{ id: 123 }, { id: 456 }, { id: 56725 }];
        const prepaymentReportCollection: IPrepaymentReport[] = [{ id: 123 }];
        expectedResult = service.addPrepaymentReportToCollectionIfMissing(prepaymentReportCollection, ...prepaymentReportArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const prepaymentReport: IPrepaymentReport = { id: 123 };
        const prepaymentReport2: IPrepaymentReport = { id: 456 };
        expectedResult = service.addPrepaymentReportToCollectionIfMissing([], prepaymentReport, prepaymentReport2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(prepaymentReport);
        expect(expectedResult).toContain(prepaymentReport2);
      });

      it('should accept null and undefined values', () => {
        const prepaymentReport: IPrepaymentReport = { id: 123 };
        expectedResult = service.addPrepaymentReportToCollectionIfMissing([], null, prepaymentReport, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(prepaymentReport);
      });

      it('should return initial array if no PrepaymentReport is added', () => {
        const prepaymentReportCollection: IPrepaymentReport[] = [{ id: 123 }];
        expectedResult = service.addPrepaymentReportToCollectionIfMissing(prepaymentReportCollection, undefined, null);
        expect(expectedResult).toEqual(prepaymentReportCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
