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

import { IPrepaymentOutstandingOverviewReport } from '../prepayment-outstanding-overview-report.model';

import { PrepaymentOutstandingOverviewReportService } from './prepayment-outstanding-overview-report.service';

describe('PrepaymentOutstandingOverviewReport Service', () => {
  let service: PrepaymentOutstandingOverviewReportService;
  let httpMock: HttpTestingController;
  let elemDefault: IPrepaymentOutstandingOverviewReport;
  let expectedResult: IPrepaymentOutstandingOverviewReport | IPrepaymentOutstandingOverviewReport[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(PrepaymentOutstandingOverviewReportService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      totalPrepaymentAmount: 0,
      totalAmortisedAmount: 0,
      totalOutstandingAmount: 0,
      numberOfPrepaymentAccounts: 0,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign({}, elemDefault);

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should return a list of PrepaymentOutstandingOverviewReport', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          totalPrepaymentAmount: 1,
          totalAmortisedAmount: 1,
          totalOutstandingAmount: 1,
          numberOfPrepaymentAccounts: 1,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    describe('addPrepaymentOutstandingOverviewReportToCollectionIfMissing', () => {
      it('should add a PrepaymentOutstandingOverviewReport to an empty array', () => {
        const prepaymentOutstandingOverviewReport: IPrepaymentOutstandingOverviewReport = { id: 123 };
        expectedResult = service.addPrepaymentOutstandingOverviewReportToCollectionIfMissing([], prepaymentOutstandingOverviewReport);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(prepaymentOutstandingOverviewReport);
      });

      it('should not add a PrepaymentOutstandingOverviewReport to an array that contains it', () => {
        const prepaymentOutstandingOverviewReport: IPrepaymentOutstandingOverviewReport = { id: 123 };
        const prepaymentOutstandingOverviewReportCollection: IPrepaymentOutstandingOverviewReport[] = [
          {
            ...prepaymentOutstandingOverviewReport,
          },
          { id: 456 },
        ];
        expectedResult = service.addPrepaymentOutstandingOverviewReportToCollectionIfMissing(
          prepaymentOutstandingOverviewReportCollection,
          prepaymentOutstandingOverviewReport
        );
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a PrepaymentOutstandingOverviewReport to an array that doesn't contain it", () => {
        const prepaymentOutstandingOverviewReport: IPrepaymentOutstandingOverviewReport = { id: 123 };
        const prepaymentOutstandingOverviewReportCollection: IPrepaymentOutstandingOverviewReport[] = [{ id: 456 }];
        expectedResult = service.addPrepaymentOutstandingOverviewReportToCollectionIfMissing(
          prepaymentOutstandingOverviewReportCollection,
          prepaymentOutstandingOverviewReport
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(prepaymentOutstandingOverviewReport);
      });

      it('should add only unique PrepaymentOutstandingOverviewReport to an array', () => {
        const prepaymentOutstandingOverviewReportArray: IPrepaymentOutstandingOverviewReport[] = [{ id: 123 }, { id: 456 }, { id: 21880 }];
        const prepaymentOutstandingOverviewReportCollection: IPrepaymentOutstandingOverviewReport[] = [{ id: 123 }];
        expectedResult = service.addPrepaymentOutstandingOverviewReportToCollectionIfMissing(
          prepaymentOutstandingOverviewReportCollection,
          ...prepaymentOutstandingOverviewReportArray
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const prepaymentOutstandingOverviewReport: IPrepaymentOutstandingOverviewReport = { id: 123 };
        const prepaymentOutstandingOverviewReport2: IPrepaymentOutstandingOverviewReport = { id: 456 };
        expectedResult = service.addPrepaymentOutstandingOverviewReportToCollectionIfMissing(
          [],
          prepaymentOutstandingOverviewReport,
          prepaymentOutstandingOverviewReport2
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(prepaymentOutstandingOverviewReport);
        expect(expectedResult).toContain(prepaymentOutstandingOverviewReport2);
      });

      it('should accept null and undefined values', () => {
        const prepaymentOutstandingOverviewReport: IPrepaymentOutstandingOverviewReport = { id: 123 };
        expectedResult = service.addPrepaymentOutstandingOverviewReportToCollectionIfMissing(
          [],
          null,
          prepaymentOutstandingOverviewReport,
          undefined
        );
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(prepaymentOutstandingOverviewReport);
      });

      it('should return initial array if no PrepaymentOutstandingOverviewReport is added', () => {
        const prepaymentOutstandingOverviewReportCollection: IPrepaymentOutstandingOverviewReport[] = [{ id: 123 }];
        expectedResult = service.addPrepaymentOutstandingOverviewReportToCollectionIfMissing(
          prepaymentOutstandingOverviewReportCollection,
          undefined,
          null
        );
        expect(expectedResult).toEqual(prepaymentOutstandingOverviewReportCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
