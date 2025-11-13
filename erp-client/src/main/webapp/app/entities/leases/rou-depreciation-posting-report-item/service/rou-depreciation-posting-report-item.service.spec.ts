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

import { IRouDepreciationPostingReportItem } from '../rou-depreciation-posting-report-item.model';

import { RouDepreciationPostingReportItemService } from './rou-depreciation-posting-report-item.service';

describe('RouDepreciationPostingReportItem Service', () => {
  let service: RouDepreciationPostingReportItemService;
  let httpMock: HttpTestingController;
  let elemDefault: IRouDepreciationPostingReportItem;
  let expectedResult: IRouDepreciationPostingReportItem | IRouDepreciationPostingReportItem[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(RouDepreciationPostingReportItemService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      leaseContractNumber: 'AAAAAAA',
      leaseDescription: 'AAAAAAA',
      fiscalMonthCode: 'AAAAAAA',
      accountForCredit: 'AAAAAAA',
      accountForDebit: 'AAAAAAA',
      depreciationAmount: 0,
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

    it('should return a list of RouDepreciationPostingReportItem', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          leaseContractNumber: 'BBBBBB',
          leaseDescription: 'BBBBBB',
          fiscalMonthCode: 'BBBBBB',
          accountForCredit: 'BBBBBB',
          accountForDebit: 'BBBBBB',
          depreciationAmount: 1,
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

    describe('addRouDepreciationPostingReportItemToCollectionIfMissing', () => {
      it('should add a RouDepreciationPostingReportItem to an empty array', () => {
        const rouDepreciationPostingReportItem: IRouDepreciationPostingReportItem = { id: 123 };
        expectedResult = service.addRouDepreciationPostingReportItemToCollectionIfMissing([], rouDepreciationPostingReportItem);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(rouDepreciationPostingReportItem);
      });

      it('should not add a RouDepreciationPostingReportItem to an array that contains it', () => {
        const rouDepreciationPostingReportItem: IRouDepreciationPostingReportItem = { id: 123 };
        const rouDepreciationPostingReportItemCollection: IRouDepreciationPostingReportItem[] = [
          {
            ...rouDepreciationPostingReportItem,
          },
          { id: 456 },
        ];
        expectedResult = service.addRouDepreciationPostingReportItemToCollectionIfMissing(
          rouDepreciationPostingReportItemCollection,
          rouDepreciationPostingReportItem
        );
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a RouDepreciationPostingReportItem to an array that doesn't contain it", () => {
        const rouDepreciationPostingReportItem: IRouDepreciationPostingReportItem = { id: 123 };
        const rouDepreciationPostingReportItemCollection: IRouDepreciationPostingReportItem[] = [{ id: 456 }];
        expectedResult = service.addRouDepreciationPostingReportItemToCollectionIfMissing(
          rouDepreciationPostingReportItemCollection,
          rouDepreciationPostingReportItem
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(rouDepreciationPostingReportItem);
      });

      it('should add only unique RouDepreciationPostingReportItem to an array', () => {
        const rouDepreciationPostingReportItemArray: IRouDepreciationPostingReportItem[] = [{ id: 123 }, { id: 456 }, { id: 75087 }];
        const rouDepreciationPostingReportItemCollection: IRouDepreciationPostingReportItem[] = [{ id: 123 }];
        expectedResult = service.addRouDepreciationPostingReportItemToCollectionIfMissing(
          rouDepreciationPostingReportItemCollection,
          ...rouDepreciationPostingReportItemArray
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const rouDepreciationPostingReportItem: IRouDepreciationPostingReportItem = { id: 123 };
        const rouDepreciationPostingReportItem2: IRouDepreciationPostingReportItem = { id: 456 };
        expectedResult = service.addRouDepreciationPostingReportItemToCollectionIfMissing(
          [],
          rouDepreciationPostingReportItem,
          rouDepreciationPostingReportItem2
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(rouDepreciationPostingReportItem);
        expect(expectedResult).toContain(rouDepreciationPostingReportItem2);
      });

      it('should accept null and undefined values', () => {
        const rouDepreciationPostingReportItem: IRouDepreciationPostingReportItem = { id: 123 };
        expectedResult = service.addRouDepreciationPostingReportItemToCollectionIfMissing(
          [],
          null,
          rouDepreciationPostingReportItem,
          undefined
        );
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(rouDepreciationPostingReportItem);
      });

      it('should return initial array if no RouDepreciationPostingReportItem is added', () => {
        const rouDepreciationPostingReportItemCollection: IRouDepreciationPostingReportItem[] = [{ id: 123 }];
        expectedResult = service.addRouDepreciationPostingReportItemToCollectionIfMissing(
          rouDepreciationPostingReportItemCollection,
          undefined,
          null
        );
        expect(expectedResult).toEqual(rouDepreciationPostingReportItemCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
