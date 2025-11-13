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

import { IAmortizationPostingReport } from '../amortization-posting-report.model';

import { AmortizationPostingReportService } from './amortization-posting-report.service';

describe('AmortizationPostingReport Service', () => {
  let service: AmortizationPostingReportService;
  let httpMock: HttpTestingController;
  let elemDefault: IAmortizationPostingReport;
  let expectedResult: IAmortizationPostingReport | IAmortizationPostingReport[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(AmortizationPostingReportService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      catalogueNumber: 'AAAAAAA',
      debitAccount: 'AAAAAAA',
      creditAccount: 'AAAAAAA',
      description: 'AAAAAAA',
      amortizationAmount: 0,
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

    it('should return a list of AmortizationPostingReport', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          catalogueNumber: 'BBBBBB',
          debitAccount: 'BBBBBB',
          creditAccount: 'BBBBBB',
          description: 'BBBBBB',
          amortizationAmount: 1,
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

    describe('addAmortizationPostingReportToCollectionIfMissing', () => {
      it('should add a AmortizationPostingReport to an empty array', () => {
        const amortizationPostingReport: IAmortizationPostingReport = { id: 123 };
        expectedResult = service.addAmortizationPostingReportToCollectionIfMissing([], amortizationPostingReport);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(amortizationPostingReport);
      });

      it('should not add a AmortizationPostingReport to an array that contains it', () => {
        const amortizationPostingReport: IAmortizationPostingReport = { id: 123 };
        const amortizationPostingReportCollection: IAmortizationPostingReport[] = [
          {
            ...amortizationPostingReport,
          },
          { id: 456 },
        ];
        expectedResult = service.addAmortizationPostingReportToCollectionIfMissing(
          amortizationPostingReportCollection,
          amortizationPostingReport
        );
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a AmortizationPostingReport to an array that doesn't contain it", () => {
        const amortizationPostingReport: IAmortizationPostingReport = { id: 123 };
        const amortizationPostingReportCollection: IAmortizationPostingReport[] = [{ id: 456 }];
        expectedResult = service.addAmortizationPostingReportToCollectionIfMissing(
          amortizationPostingReportCollection,
          amortizationPostingReport
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(amortizationPostingReport);
      });

      it('should add only unique AmortizationPostingReport to an array', () => {
        const amortizationPostingReportArray: IAmortizationPostingReport[] = [{ id: 123 }, { id: 456 }, { id: 54533 }];
        const amortizationPostingReportCollection: IAmortizationPostingReport[] = [{ id: 123 }];
        expectedResult = service.addAmortizationPostingReportToCollectionIfMissing(
          amortizationPostingReportCollection,
          ...amortizationPostingReportArray
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const amortizationPostingReport: IAmortizationPostingReport = { id: 123 };
        const amortizationPostingReport2: IAmortizationPostingReport = { id: 456 };
        expectedResult = service.addAmortizationPostingReportToCollectionIfMissing(
          [],
          amortizationPostingReport,
          amortizationPostingReport2
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(amortizationPostingReport);
        expect(expectedResult).toContain(amortizationPostingReport2);
      });

      it('should accept null and undefined values', () => {
        const amortizationPostingReport: IAmortizationPostingReport = { id: 123 };
        expectedResult = service.addAmortizationPostingReportToCollectionIfMissing([], null, amortizationPostingReport, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(amortizationPostingReport);
      });

      it('should return initial array if no AmortizationPostingReport is added', () => {
        const amortizationPostingReportCollection: IAmortizationPostingReport[] = [{ id: 123 }];
        expectedResult = service.addAmortizationPostingReportToCollectionIfMissing(amortizationPostingReportCollection, undefined, null);
        expect(expectedResult).toEqual(amortizationPostingReportCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
