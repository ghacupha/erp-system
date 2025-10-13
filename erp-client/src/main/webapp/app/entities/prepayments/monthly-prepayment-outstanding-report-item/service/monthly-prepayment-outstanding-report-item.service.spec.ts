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
import { IMonthlyPrepaymentOutstandingReportItem } from '../monthly-prepayment-outstanding-report-item.model';

import { MonthlyPrepaymentOutstandingReportItemService } from './monthly-prepayment-outstanding-report-item.service';

describe('MonthlyPrepaymentOutstandingReportItem Service', () => {
  let service: MonthlyPrepaymentOutstandingReportItemService;
  let httpMock: HttpTestingController;
  let elemDefault: IMonthlyPrepaymentOutstandingReportItem;
  let expectedResult: IMonthlyPrepaymentOutstandingReportItem | IMonthlyPrepaymentOutstandingReportItem[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(MonthlyPrepaymentOutstandingReportItemService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      fiscalMonthEndDate: currentDate,
      totalPrepaymentAmount: 0,
      totalAmortisedAmount: 0,
      totalOutstandingAmount: 0,
      numberOfPrepaymentAccounts: 0,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          fiscalMonthEndDate: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should return a list of MonthlyPrepaymentOutstandingReportItem', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          fiscalMonthEndDate: currentDate.format(DATE_FORMAT),
          totalPrepaymentAmount: 1,
          totalAmortisedAmount: 1,
          totalOutstandingAmount: 1,
          numberOfPrepaymentAccounts: 1,
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          fiscalMonthEndDate: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    describe('addMonthlyPrepaymentOutstandingReportItemToCollectionIfMissing', () => {
      it('should add a MonthlyPrepaymentOutstandingReportItem to an empty array', () => {
        const monthlyPrepaymentOutstandingReportItem: IMonthlyPrepaymentOutstandingReportItem = { id: 123 };
        expectedResult = service.addMonthlyPrepaymentOutstandingReportItemToCollectionIfMissing([], monthlyPrepaymentOutstandingReportItem);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(monthlyPrepaymentOutstandingReportItem);
      });

      it('should not add a MonthlyPrepaymentOutstandingReportItem to an array that contains it', () => {
        const monthlyPrepaymentOutstandingReportItem: IMonthlyPrepaymentOutstandingReportItem = { id: 123 };
        const monthlyPrepaymentOutstandingReportItemCollection: IMonthlyPrepaymentOutstandingReportItem[] = [
          {
            ...monthlyPrepaymentOutstandingReportItem,
          },
          { id: 456 },
        ];
        expectedResult = service.addMonthlyPrepaymentOutstandingReportItemToCollectionIfMissing(
          monthlyPrepaymentOutstandingReportItemCollection,
          monthlyPrepaymentOutstandingReportItem
        );
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a MonthlyPrepaymentOutstandingReportItem to an array that doesn't contain it", () => {
        const monthlyPrepaymentOutstandingReportItem: IMonthlyPrepaymentOutstandingReportItem = { id: 123 };
        const monthlyPrepaymentOutstandingReportItemCollection: IMonthlyPrepaymentOutstandingReportItem[] = [{ id: 456 }];
        expectedResult = service.addMonthlyPrepaymentOutstandingReportItemToCollectionIfMissing(
          monthlyPrepaymentOutstandingReportItemCollection,
          monthlyPrepaymentOutstandingReportItem
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(monthlyPrepaymentOutstandingReportItem);
      });

      it('should add only unique MonthlyPrepaymentOutstandingReportItem to an array', () => {
        const monthlyPrepaymentOutstandingReportItemArray: IMonthlyPrepaymentOutstandingReportItem[] = [
          { id: 123 },
          { id: 456 },
          { id: 62054 },
        ];
        const monthlyPrepaymentOutstandingReportItemCollection: IMonthlyPrepaymentOutstandingReportItem[] = [{ id: 123 }];
        expectedResult = service.addMonthlyPrepaymentOutstandingReportItemToCollectionIfMissing(
          monthlyPrepaymentOutstandingReportItemCollection,
          ...monthlyPrepaymentOutstandingReportItemArray
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const monthlyPrepaymentOutstandingReportItem: IMonthlyPrepaymentOutstandingReportItem = { id: 123 };
        const monthlyPrepaymentOutstandingReportItem2: IMonthlyPrepaymentOutstandingReportItem = { id: 456 };
        expectedResult = service.addMonthlyPrepaymentOutstandingReportItemToCollectionIfMissing(
          [],
          monthlyPrepaymentOutstandingReportItem,
          monthlyPrepaymentOutstandingReportItem2
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(monthlyPrepaymentOutstandingReportItem);
        expect(expectedResult).toContain(monthlyPrepaymentOutstandingReportItem2);
      });

      it('should accept null and undefined values', () => {
        const monthlyPrepaymentOutstandingReportItem: IMonthlyPrepaymentOutstandingReportItem = { id: 123 };
        expectedResult = service.addMonthlyPrepaymentOutstandingReportItemToCollectionIfMissing(
          [],
          null,
          monthlyPrepaymentOutstandingReportItem,
          undefined
        );
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(monthlyPrepaymentOutstandingReportItem);
      });

      it('should return initial array if no MonthlyPrepaymentOutstandingReportItem is added', () => {
        const monthlyPrepaymentOutstandingReportItemCollection: IMonthlyPrepaymentOutstandingReportItem[] = [{ id: 123 }];
        expectedResult = service.addMonthlyPrepaymentOutstandingReportItemToCollectionIfMissing(
          monthlyPrepaymentOutstandingReportItemCollection,
          undefined,
          null
        );
        expect(expectedResult).toEqual(monthlyPrepaymentOutstandingReportItemCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
