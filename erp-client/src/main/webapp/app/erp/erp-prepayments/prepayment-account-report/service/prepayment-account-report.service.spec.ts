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

import { IPrepaymentAccountReport } from '../prepayment-account-report.model';

import { PrepaymentAccountReportService } from './prepayment-account-report.service';

describe('PrepaymentAccountReport Service', () => {
  let service: PrepaymentAccountReportService;
  let httpMock: HttpTestingController;
  let elemDefault: IPrepaymentAccountReport;
  let expectedResult: IPrepaymentAccountReport | IPrepaymentAccountReport[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(PrepaymentAccountReportService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      prepaymentAccount: 'AAAAAAA',
      prepaymentAmount: 0,
      amortisedAmount: 0,
      outstandingAmount: 0,
      numberOfPrepaymentAccounts: 0,
      numberOfAmortisedItems: 0,
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

    it('should return a list of PrepaymentAccountReport', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          prepaymentAccount: 'BBBBBB',
          prepaymentAmount: 1,
          amortisedAmount: 1,
          outstandingAmount: 1,
          numberOfPrepaymentAccounts: 1,
          numberOfAmortisedItems: 1,
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

    describe('addPrepaymentAccountReportToCollectionIfMissing', () => {
      it('should add a PrepaymentAccountReport to an empty array', () => {
        const prepaymentAccountReport: IPrepaymentAccountReport = { id: 123 };
        expectedResult = service.addPrepaymentAccountReportToCollectionIfMissing([], prepaymentAccountReport);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(prepaymentAccountReport);
      });

      it('should not add a PrepaymentAccountReport to an array that contains it', () => {
        const prepaymentAccountReport: IPrepaymentAccountReport = { id: 123 };
        const prepaymentAccountReportCollection: IPrepaymentAccountReport[] = [
          {
            ...prepaymentAccountReport,
          },
          { id: 456 },
        ];
        expectedResult = service.addPrepaymentAccountReportToCollectionIfMissing(
          prepaymentAccountReportCollection,
          prepaymentAccountReport
        );
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a PrepaymentAccountReport to an array that doesn't contain it", () => {
        const prepaymentAccountReport: IPrepaymentAccountReport = { id: 123 };
        const prepaymentAccountReportCollection: IPrepaymentAccountReport[] = [{ id: 456 }];
        expectedResult = service.addPrepaymentAccountReportToCollectionIfMissing(
          prepaymentAccountReportCollection,
          prepaymentAccountReport
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(prepaymentAccountReport);
      });

      it('should add only unique PrepaymentAccountReport to an array', () => {
        const prepaymentAccountReportArray: IPrepaymentAccountReport[] = [{ id: 123 }, { id: 456 }, { id: 50660 }];
        const prepaymentAccountReportCollection: IPrepaymentAccountReport[] = [{ id: 123 }];
        expectedResult = service.addPrepaymentAccountReportToCollectionIfMissing(
          prepaymentAccountReportCollection,
          ...prepaymentAccountReportArray
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const prepaymentAccountReport: IPrepaymentAccountReport = { id: 123 };
        const prepaymentAccountReport2: IPrepaymentAccountReport = { id: 456 };
        expectedResult = service.addPrepaymentAccountReportToCollectionIfMissing([], prepaymentAccountReport, prepaymentAccountReport2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(prepaymentAccountReport);
        expect(expectedResult).toContain(prepaymentAccountReport2);
      });

      it('should accept null and undefined values', () => {
        const prepaymentAccountReport: IPrepaymentAccountReport = { id: 123 };
        expectedResult = service.addPrepaymentAccountReportToCollectionIfMissing([], null, prepaymentAccountReport, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(prepaymentAccountReport);
      });

      it('should return initial array if no PrepaymentAccountReport is added', () => {
        const prepaymentAccountReportCollection: IPrepaymentAccountReport[] = [{ id: 123 }];
        expectedResult = service.addPrepaymentAccountReportToCollectionIfMissing(prepaymentAccountReportCollection, undefined, null);
        expect(expectedResult).toEqual(prepaymentAccountReportCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
