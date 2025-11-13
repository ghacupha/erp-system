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
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IRouAssetListReportItem } from '../rou-asset-list-report-item.model';

import { RouAssetListReportItemService } from './rou-asset-list-report-item.service';

describe('RouAssetListReportItem Service', () => {
  let service: RouAssetListReportItemService;
  let httpMock: HttpTestingController;
  let elemDefault: IRouAssetListReportItem;
  let expectedResult: IRouAssetListReportItem | IRouAssetListReportItem[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(RouAssetListReportItemService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      modelTitle: 'AAAAAAA',
      modelVersion: 0,
      description: 'AAAAAAA',
      leaseTermPeriods: 0,
      rouModelReference: 'AAAAAAA',
      commencementDate: currentDate,
      expirationDate: currentDate,
      leaseContractTitle: 'AAAAAAA',
      assetAccountNumber: 'AAAAAAA',
      depreciationAccountNumber: 'AAAAAAA',
      accruedDepreciationAccountNumber: 'AAAAAAA',
      assetCategoryName: 'AAAAAAA',
      leaseAmount: 0,
      leaseContractSerialNumber: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          commencementDate: currentDate.format(DATE_FORMAT),
          expirationDate: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should return a list of RouAssetListReportItem', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          modelTitle: 'BBBBBB',
          modelVersion: 1,
          description: 'BBBBBB',
          leaseTermPeriods: 1,
          rouModelReference: 'BBBBBB',
          commencementDate: currentDate.format(DATE_FORMAT),
          expirationDate: currentDate.format(DATE_FORMAT),
          leaseContractTitle: 'BBBBBB',
          assetAccountNumber: 'BBBBBB',
          depreciationAccountNumber: 'BBBBBB',
          accruedDepreciationAccountNumber: 'BBBBBB',
          assetCategoryName: 'BBBBBB',
          leaseAmount: 1,
          leaseContractSerialNumber: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          commencementDate: currentDate,
          expirationDate: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    describe('addRouAssetListReportItemToCollectionIfMissing', () => {
      it('should add a RouAssetListReportItem to an empty array', () => {
        const rouAssetListReportItem: IRouAssetListReportItem = { id: 123 };
        expectedResult = service.addRouAssetListReportItemToCollectionIfMissing([], rouAssetListReportItem);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(rouAssetListReportItem);
      });

      it('should not add a RouAssetListReportItem to an array that contains it', () => {
        const rouAssetListReportItem: IRouAssetListReportItem = { id: 123 };
        const rouAssetListReportItemCollection: IRouAssetListReportItem[] = [
          {
            ...rouAssetListReportItem,
          },
          { id: 456 },
        ];
        expectedResult = service.addRouAssetListReportItemToCollectionIfMissing(rouAssetListReportItemCollection, rouAssetListReportItem);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a RouAssetListReportItem to an array that doesn't contain it", () => {
        const rouAssetListReportItem: IRouAssetListReportItem = { id: 123 };
        const rouAssetListReportItemCollection: IRouAssetListReportItem[] = [{ id: 456 }];
        expectedResult = service.addRouAssetListReportItemToCollectionIfMissing(rouAssetListReportItemCollection, rouAssetListReportItem);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(rouAssetListReportItem);
      });

      it('should add only unique RouAssetListReportItem to an array', () => {
        const rouAssetListReportItemArray: IRouAssetListReportItem[] = [{ id: 123 }, { id: 456 }, { id: 94788 }];
        const rouAssetListReportItemCollection: IRouAssetListReportItem[] = [{ id: 123 }];
        expectedResult = service.addRouAssetListReportItemToCollectionIfMissing(
          rouAssetListReportItemCollection,
          ...rouAssetListReportItemArray
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const rouAssetListReportItem: IRouAssetListReportItem = { id: 123 };
        const rouAssetListReportItem2: IRouAssetListReportItem = { id: 456 };
        expectedResult = service.addRouAssetListReportItemToCollectionIfMissing([], rouAssetListReportItem, rouAssetListReportItem2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(rouAssetListReportItem);
        expect(expectedResult).toContain(rouAssetListReportItem2);
      });

      it('should accept null and undefined values', () => {
        const rouAssetListReportItem: IRouAssetListReportItem = { id: 123 };
        expectedResult = service.addRouAssetListReportItemToCollectionIfMissing([], null, rouAssetListReportItem, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(rouAssetListReportItem);
      });

      it('should return initial array if no RouAssetListReportItem is added', () => {
        const rouAssetListReportItemCollection: IRouAssetListReportItem[] = [{ id: 123 }];
        expectedResult = service.addRouAssetListReportItemToCollectionIfMissing(rouAssetListReportItemCollection, undefined, null);
        expect(expectedResult).toEqual(rouAssetListReportItemCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
