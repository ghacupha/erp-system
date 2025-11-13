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

import { ILeaseLiabilityReportItem } from '../lease-liability-report-item.model';

import { LeaseLiabilityReportItemService } from './lease-liability-report-item.service';

describe('LeaseLiabilityReportItem Service', () => {
  let service: LeaseLiabilityReportItemService;
  let httpMock: HttpTestingController;
  let elemDefault: ILeaseLiabilityReportItem;
  let expectedResult: ILeaseLiabilityReportItem | ILeaseLiabilityReportItem[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(LeaseLiabilityReportItemService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      bookingId: 'AAAAAAA',
      leaseTitle: 'AAAAAAA',
      liabilityAccountNumber: 'AAAAAAA',
      liabilityAmount: 0,
      interestPayableAccountNumber: 'AAAAAAA',
      interestPayableAmount: 0,
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

    it('should return a list of LeaseLiabilityReportItem', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          bookingId: 'BBBBBB',
          leaseTitle: 'BBBBBB',
          liabilityAccountNumber: 'BBBBBB',
          liabilityAmount: 1,
          interestPayableAccountNumber: 'BBBBBB',
          interestPayableAmount: 1,
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

    describe('addLeaseLiabilityReportItemToCollectionIfMissing', () => {
      it('should add a LeaseLiabilityReportItem to an empty array', () => {
        const leaseLiabilityReportItem: ILeaseLiabilityReportItem = { id: 123 };
        expectedResult = service.addLeaseLiabilityReportItemToCollectionIfMissing([], leaseLiabilityReportItem);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(leaseLiabilityReportItem);
      });

      it('should not add a LeaseLiabilityReportItem to an array that contains it', () => {
        const leaseLiabilityReportItem: ILeaseLiabilityReportItem = { id: 123 };
        const leaseLiabilityReportItemCollection: ILeaseLiabilityReportItem[] = [
          {
            ...leaseLiabilityReportItem,
          },
          { id: 456 },
        ];
        expectedResult = service.addLeaseLiabilityReportItemToCollectionIfMissing(
          leaseLiabilityReportItemCollection,
          leaseLiabilityReportItem
        );
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a LeaseLiabilityReportItem to an array that doesn't contain it", () => {
        const leaseLiabilityReportItem: ILeaseLiabilityReportItem = { id: 123 };
        const leaseLiabilityReportItemCollection: ILeaseLiabilityReportItem[] = [{ id: 456 }];
        expectedResult = service.addLeaseLiabilityReportItemToCollectionIfMissing(
          leaseLiabilityReportItemCollection,
          leaseLiabilityReportItem
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(leaseLiabilityReportItem);
      });

      it('should add only unique LeaseLiabilityReportItem to an array', () => {
        const leaseLiabilityReportItemArray: ILeaseLiabilityReportItem[] = [{ id: 123 }, { id: 456 }, { id: 24265 }];
        const leaseLiabilityReportItemCollection: ILeaseLiabilityReportItem[] = [{ id: 123 }];
        expectedResult = service.addLeaseLiabilityReportItemToCollectionIfMissing(
          leaseLiabilityReportItemCollection,
          ...leaseLiabilityReportItemArray
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const leaseLiabilityReportItem: ILeaseLiabilityReportItem = { id: 123 };
        const leaseLiabilityReportItem2: ILeaseLiabilityReportItem = { id: 456 };
        expectedResult = service.addLeaseLiabilityReportItemToCollectionIfMissing([], leaseLiabilityReportItem, leaseLiabilityReportItem2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(leaseLiabilityReportItem);
        expect(expectedResult).toContain(leaseLiabilityReportItem2);
      });

      it('should accept null and undefined values', () => {
        const leaseLiabilityReportItem: ILeaseLiabilityReportItem = { id: 123 };
        expectedResult = service.addLeaseLiabilityReportItemToCollectionIfMissing([], null, leaseLiabilityReportItem, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(leaseLiabilityReportItem);
      });

      it('should return initial array if no LeaseLiabilityReportItem is added', () => {
        const leaseLiabilityReportItemCollection: ILeaseLiabilityReportItem[] = [{ id: 123 }];
        expectedResult = service.addLeaseLiabilityReportItemToCollectionIfMissing(leaseLiabilityReportItemCollection, undefined, null);
        expect(expectedResult).toEqual(leaseLiabilityReportItemCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
