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

import { DATE_FORMAT } from 'app/config/input.constants';
import { IWIPListItem } from '../wip-list-item.model';

import { WIPListItemService } from './wip-list-item.service';

describe('WIPListItem Service', () => {
  let service: WIPListItemService;
  let httpMock: HttpTestingController;
  let elemDefault: IWIPListItem;
  let expectedResult: IWIPListItem | IWIPListItem[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(WIPListItemService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      sequenceNumber: 'AAAAAAA',
      particulars: 'AAAAAAA',
      instalmentDate: currentDate,
      instalmentAmount: 0,
      settlementCurrency: 'AAAAAAA',
      outletCode: 'AAAAAAA',
      settlementTransaction: 'AAAAAAA',
      settlementTransactionDate: currentDate,
      dealerName: 'AAAAAAA',
      workProject: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          instalmentDate: currentDate.format(DATE_FORMAT),
          settlementTransactionDate: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should return a list of WIPListItem', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          sequenceNumber: 'BBBBBB',
          particulars: 'BBBBBB',
          instalmentDate: currentDate.format(DATE_FORMAT),
          instalmentAmount: 1,
          settlementCurrency: 'BBBBBB',
          outletCode: 'BBBBBB',
          settlementTransaction: 'BBBBBB',
          settlementTransactionDate: currentDate.format(DATE_FORMAT),
          dealerName: 'BBBBBB',
          workProject: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          instalmentDate: currentDate,
          settlementTransactionDate: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    describe('addWIPListItemToCollectionIfMissing', () => {
      it('should add a WIPListItem to an empty array', () => {
        const wIPListItem: IWIPListItem = { id: 123 };
        expectedResult = service.addWIPListItemToCollectionIfMissing([], wIPListItem);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(wIPListItem);
      });

      it('should not add a WIPListItem to an array that contains it', () => {
        const wIPListItem: IWIPListItem = { id: 123 };
        const wIPListItemCollection: IWIPListItem[] = [
          {
            ...wIPListItem,
          },
          { id: 456 },
        ];
        expectedResult = service.addWIPListItemToCollectionIfMissing(wIPListItemCollection, wIPListItem);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a WIPListItem to an array that doesn't contain it", () => {
        const wIPListItem: IWIPListItem = { id: 123 };
        const wIPListItemCollection: IWIPListItem[] = [{ id: 456 }];
        expectedResult = service.addWIPListItemToCollectionIfMissing(wIPListItemCollection, wIPListItem);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(wIPListItem);
      });

      it('should add only unique WIPListItem to an array', () => {
        const wIPListItemArray: IWIPListItem[] = [{ id: 123 }, { id: 456 }, { id: 50475 }];
        const wIPListItemCollection: IWIPListItem[] = [{ id: 123 }];
        expectedResult = service.addWIPListItemToCollectionIfMissing(wIPListItemCollection, ...wIPListItemArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const wIPListItem: IWIPListItem = { id: 123 };
        const wIPListItem2: IWIPListItem = { id: 456 };
        expectedResult = service.addWIPListItemToCollectionIfMissing([], wIPListItem, wIPListItem2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(wIPListItem);
        expect(expectedResult).toContain(wIPListItem2);
      });

      it('should accept null and undefined values', () => {
        const wIPListItem: IWIPListItem = { id: 123 };
        expectedResult = service.addWIPListItemToCollectionIfMissing([], null, wIPListItem, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(wIPListItem);
      });

      it('should return initial array if no WIPListItem is added', () => {
        const wIPListItemCollection: IWIPListItem[] = [{ id: 123 }];
        expectedResult = service.addWIPListItemToCollectionIfMissing(wIPListItemCollection, undefined, null);
        expect(expectedResult).toEqual(wIPListItemCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
