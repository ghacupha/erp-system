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

import { ILeaseLiabilityScheduleReportItem } from '../lease-liability-schedule-report-item.model';

import { LeaseLiabilityScheduleReportItemService } from './lease-liability-schedule-report-item.service';

describe('LeaseLiabilityScheduleReportItem Service', () => {
  let service: LeaseLiabilityScheduleReportItemService;
  let httpMock: HttpTestingController;
  let elemDefault: ILeaseLiabilityScheduleReportItem;
  let expectedResult: ILeaseLiabilityScheduleReportItem | ILeaseLiabilityScheduleReportItem[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(LeaseLiabilityScheduleReportItemService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      sequenceNumber: 0,
      openingBalance: 0,
      cashPayment: 0,
      principalPayment: 0,
      interestPayment: 0,
      outstandingBalance: 0,
      interestPayableOpening: 0,
      interestAccrued: 0,
      interestPayableClosing: 0,
      amortizationScheduleId: 0,
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

    it('should return a list of LeaseLiabilityScheduleReportItem', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          sequenceNumber: 1,
          openingBalance: 1,
          cashPayment: 1,
          principalPayment: 1,
          interestPayment: 1,
          outstandingBalance: 1,
          interestPayableOpening: 1,
          interestAccrued: 1,
          interestPayableClosing: 1,
          amortizationScheduleId: 1,
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

    describe('addLeaseLiabilityScheduleReportItemToCollectionIfMissing', () => {
      it('should add a LeaseLiabilityScheduleReportItem to an empty array', () => {
        const leaseLiabilityScheduleReportItem: ILeaseLiabilityScheduleReportItem = { id: 123 };
        expectedResult = service.addLeaseLiabilityScheduleReportItemToCollectionIfMissing([], leaseLiabilityScheduleReportItem);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(leaseLiabilityScheduleReportItem);
      });

      it('should not add a LeaseLiabilityScheduleReportItem to an array that contains it', () => {
        const leaseLiabilityScheduleReportItem: ILeaseLiabilityScheduleReportItem = { id: 123 };
        const leaseLiabilityScheduleReportItemCollection: ILeaseLiabilityScheduleReportItem[] = [
          {
            ...leaseLiabilityScheduleReportItem,
          },
          { id: 456 },
        ];
        expectedResult = service.addLeaseLiabilityScheduleReportItemToCollectionIfMissing(
          leaseLiabilityScheduleReportItemCollection,
          leaseLiabilityScheduleReportItem
        );
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a LeaseLiabilityScheduleReportItem to an array that doesn't contain it", () => {
        const leaseLiabilityScheduleReportItem: ILeaseLiabilityScheduleReportItem = { id: 123 };
        const leaseLiabilityScheduleReportItemCollection: ILeaseLiabilityScheduleReportItem[] = [{ id: 456 }];
        expectedResult = service.addLeaseLiabilityScheduleReportItemToCollectionIfMissing(
          leaseLiabilityScheduleReportItemCollection,
          leaseLiabilityScheduleReportItem
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(leaseLiabilityScheduleReportItem);
      });

      it('should add only unique LeaseLiabilityScheduleReportItem to an array', () => {
        const leaseLiabilityScheduleReportItemArray: ILeaseLiabilityScheduleReportItem[] = [{ id: 123 }, { id: 456 }, { id: 3522 }];
        const leaseLiabilityScheduleReportItemCollection: ILeaseLiabilityScheduleReportItem[] = [{ id: 123 }];
        expectedResult = service.addLeaseLiabilityScheduleReportItemToCollectionIfMissing(
          leaseLiabilityScheduleReportItemCollection,
          ...leaseLiabilityScheduleReportItemArray
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const leaseLiabilityScheduleReportItem: ILeaseLiabilityScheduleReportItem = { id: 123 };
        const leaseLiabilityScheduleReportItem2: ILeaseLiabilityScheduleReportItem = { id: 456 };
        expectedResult = service.addLeaseLiabilityScheduleReportItemToCollectionIfMissing(
          [],
          leaseLiabilityScheduleReportItem,
          leaseLiabilityScheduleReportItem2
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(leaseLiabilityScheduleReportItem);
        expect(expectedResult).toContain(leaseLiabilityScheduleReportItem2);
      });

      it('should accept null and undefined values', () => {
        const leaseLiabilityScheduleReportItem: ILeaseLiabilityScheduleReportItem = { id: 123 };
        expectedResult = service.addLeaseLiabilityScheduleReportItemToCollectionIfMissing(
          [],
          null,
          leaseLiabilityScheduleReportItem,
          undefined
        );
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(leaseLiabilityScheduleReportItem);
      });

      it('should return initial array if no LeaseLiabilityScheduleReportItem is added', () => {
        const leaseLiabilityScheduleReportItemCollection: ILeaseLiabilityScheduleReportItem[] = [{ id: 123 }];
        expectedResult = service.addLeaseLiabilityScheduleReportItemToCollectionIfMissing(
          leaseLiabilityScheduleReportItemCollection,
          undefined,
          null
        );
        expect(expectedResult).toEqual(leaseLiabilityScheduleReportItemCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
