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

import { ILeaseLiabilityByAccountReportItem } from '../lease-liability-by-account-report-item.model';

import { LeaseLiabilityByAccountReportItemService } from './lease-liability-by-account-report-item.service';

describe('LeaseLiabilityByAccountReportItem Service', () => {
  let service: LeaseLiabilityByAccountReportItemService;
  let httpMock: HttpTestingController;
  let elemDefault: ILeaseLiabilityByAccountReportItem;
  let expectedResult: ILeaseLiabilityByAccountReportItem | ILeaseLiabilityByAccountReportItem[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(LeaseLiabilityByAccountReportItemService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      accountName: 'AAAAAAA',
      accountNumber: 'AAAAAAA',
      description: 'AAAAAAA',
      accountBalance: 0,
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

    it('should return a list of LeaseLiabilityByAccountReportItem', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          accountName: 'BBBBBB',
          accountNumber: 'BBBBBB',
          description: 'BBBBBB',
          accountBalance: 1,
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

    describe('addLeaseLiabilityByAccountReportItemToCollectionIfMissing', () => {
      it('should add a LeaseLiabilityByAccountReportItem to an empty array', () => {
        const leaseLiabilityByAccountReportItem: ILeaseLiabilityByAccountReportItem = { id: 123 };
        expectedResult = service.addLeaseLiabilityByAccountReportItemToCollectionIfMissing([], leaseLiabilityByAccountReportItem);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(leaseLiabilityByAccountReportItem);
      });

      it('should not add a LeaseLiabilityByAccountReportItem to an array that contains it', () => {
        const leaseLiabilityByAccountReportItem: ILeaseLiabilityByAccountReportItem = { id: 123 };
        const leaseLiabilityByAccountReportItemCollection: ILeaseLiabilityByAccountReportItem[] = [
          {
            ...leaseLiabilityByAccountReportItem,
          },
          { id: 456 },
        ];
        expectedResult = service.addLeaseLiabilityByAccountReportItemToCollectionIfMissing(
          leaseLiabilityByAccountReportItemCollection,
          leaseLiabilityByAccountReportItem
        );
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a LeaseLiabilityByAccountReportItem to an array that doesn't contain it", () => {
        const leaseLiabilityByAccountReportItem: ILeaseLiabilityByAccountReportItem = { id: 123 };
        const leaseLiabilityByAccountReportItemCollection: ILeaseLiabilityByAccountReportItem[] = [{ id: 456 }];
        expectedResult = service.addLeaseLiabilityByAccountReportItemToCollectionIfMissing(
          leaseLiabilityByAccountReportItemCollection,
          leaseLiabilityByAccountReportItem
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(leaseLiabilityByAccountReportItem);
      });

      it('should add only unique LeaseLiabilityByAccountReportItem to an array', () => {
        const leaseLiabilityByAccountReportItemArray: ILeaseLiabilityByAccountReportItem[] = [{ id: 123 }, { id: 456 }, { id: 96971 }];
        const leaseLiabilityByAccountReportItemCollection: ILeaseLiabilityByAccountReportItem[] = [{ id: 123 }];
        expectedResult = service.addLeaseLiabilityByAccountReportItemToCollectionIfMissing(
          leaseLiabilityByAccountReportItemCollection,
          ...leaseLiabilityByAccountReportItemArray
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const leaseLiabilityByAccountReportItem: ILeaseLiabilityByAccountReportItem = { id: 123 };
        const leaseLiabilityByAccountReportItem2: ILeaseLiabilityByAccountReportItem = { id: 456 };
        expectedResult = service.addLeaseLiabilityByAccountReportItemToCollectionIfMissing(
          [],
          leaseLiabilityByAccountReportItem,
          leaseLiabilityByAccountReportItem2
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(leaseLiabilityByAccountReportItem);
        expect(expectedResult).toContain(leaseLiabilityByAccountReportItem2);
      });

      it('should accept null and undefined values', () => {
        const leaseLiabilityByAccountReportItem: ILeaseLiabilityByAccountReportItem = { id: 123 };
        expectedResult = service.addLeaseLiabilityByAccountReportItemToCollectionIfMissing(
          [],
          null,
          leaseLiabilityByAccountReportItem,
          undefined
        );
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(leaseLiabilityByAccountReportItem);
      });

      it('should return initial array if no LeaseLiabilityByAccountReportItem is added', () => {
        const leaseLiabilityByAccountReportItemCollection: ILeaseLiabilityByAccountReportItem[] = [{ id: 123 }];
        expectedResult = service.addLeaseLiabilityByAccountReportItemToCollectionIfMissing(
          leaseLiabilityByAccountReportItemCollection,
          undefined,
          null
        );
        expect(expectedResult).toEqual(leaseLiabilityByAccountReportItemCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
