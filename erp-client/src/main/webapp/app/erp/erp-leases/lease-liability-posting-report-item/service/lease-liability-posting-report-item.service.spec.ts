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

import { ILeaseLiabilityPostingReportItem } from '../lease-liability-posting-report-item.model';

import { LeaseLiabilityPostingReportItemService } from './lease-liability-posting-report-item.service';

describe('LeaseLiabilityPostingReportItem Service', () => {
  let service: LeaseLiabilityPostingReportItemService;
  let httpMock: HttpTestingController;
  let elemDefault: ILeaseLiabilityPostingReportItem;
  let expectedResult: ILeaseLiabilityPostingReportItem | ILeaseLiabilityPostingReportItem[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(LeaseLiabilityPostingReportItemService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      bookingId: 'AAAAAAA',
      leaseTitle: 'AAAAAAA',
      leaseDescription: 'AAAAAAA',
      accountNumber: 'AAAAAAA',
      posting: 'AAAAAAA',
      postingAmount: 0,
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

    it('should return a list of LeaseLiabilityPostingReportItem', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          bookingId: 'BBBBBB',
          leaseTitle: 'BBBBBB',
          leaseDescription: 'BBBBBB',
          accountNumber: 'BBBBBB',
          posting: 'BBBBBB',
          postingAmount: 1,
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

    describe('addLeaseLiabilityPostingReportItemToCollectionIfMissing', () => {
      it('should add a LeaseLiabilityPostingReportItem to an empty array', () => {
        const leaseLiabilityPostingReportItem: ILeaseLiabilityPostingReportItem = { id: 123 };
        expectedResult = service.addLeaseLiabilityPostingReportItemToCollectionIfMissing([], leaseLiabilityPostingReportItem);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(leaseLiabilityPostingReportItem);
      });

      it('should not add a LeaseLiabilityPostingReportItem to an array that contains it', () => {
        const leaseLiabilityPostingReportItem: ILeaseLiabilityPostingReportItem = { id: 123 };
        const leaseLiabilityPostingReportItemCollection: ILeaseLiabilityPostingReportItem[] = [
          {
            ...leaseLiabilityPostingReportItem,
          },
          { id: 456 },
        ];
        expectedResult = service.addLeaseLiabilityPostingReportItemToCollectionIfMissing(
          leaseLiabilityPostingReportItemCollection,
          leaseLiabilityPostingReportItem
        );
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a LeaseLiabilityPostingReportItem to an array that doesn't contain it", () => {
        const leaseLiabilityPostingReportItem: ILeaseLiabilityPostingReportItem = { id: 123 };
        const leaseLiabilityPostingReportItemCollection: ILeaseLiabilityPostingReportItem[] = [{ id: 456 }];
        expectedResult = service.addLeaseLiabilityPostingReportItemToCollectionIfMissing(
          leaseLiabilityPostingReportItemCollection,
          leaseLiabilityPostingReportItem
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(leaseLiabilityPostingReportItem);
      });

      it('should add only unique LeaseLiabilityPostingReportItem to an array', () => {
        const leaseLiabilityPostingReportItemArray: ILeaseLiabilityPostingReportItem[] = [{ id: 123 }, { id: 456 }, { id: 4341 }];
        const leaseLiabilityPostingReportItemCollection: ILeaseLiabilityPostingReportItem[] = [{ id: 123 }];
        expectedResult = service.addLeaseLiabilityPostingReportItemToCollectionIfMissing(
          leaseLiabilityPostingReportItemCollection,
          ...leaseLiabilityPostingReportItemArray
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const leaseLiabilityPostingReportItem: ILeaseLiabilityPostingReportItem = { id: 123 };
        const leaseLiabilityPostingReportItem2: ILeaseLiabilityPostingReportItem = { id: 456 };
        expectedResult = service.addLeaseLiabilityPostingReportItemToCollectionIfMissing(
          [],
          leaseLiabilityPostingReportItem,
          leaseLiabilityPostingReportItem2
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(leaseLiabilityPostingReportItem);
        expect(expectedResult).toContain(leaseLiabilityPostingReportItem2);
      });

      it('should accept null and undefined values', () => {
        const leaseLiabilityPostingReportItem: ILeaseLiabilityPostingReportItem = { id: 123 };
        expectedResult = service.addLeaseLiabilityPostingReportItemToCollectionIfMissing(
          [],
          null,
          leaseLiabilityPostingReportItem,
          undefined
        );
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(leaseLiabilityPostingReportItem);
      });

      it('should return initial array if no LeaseLiabilityPostingReportItem is added', () => {
        const leaseLiabilityPostingReportItemCollection: ILeaseLiabilityPostingReportItem[] = [{ id: 123 }];
        expectedResult = service.addLeaseLiabilityPostingReportItemToCollectionIfMissing(
          leaseLiabilityPostingReportItemCollection,
          undefined,
          null
        );
        expect(expectedResult).toEqual(leaseLiabilityPostingReportItemCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
