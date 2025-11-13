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
import { IRouAssetNBVReportItem } from '../rou-asset-nbv-report-item.model';

import { RouAssetNBVReportItemService } from './rou-asset-nbv-report-item.service';

describe('RouAssetNBVReportItem Service', () => {
  let service: RouAssetNBVReportItemService;
  let httpMock: HttpTestingController;
  let elemDefault: IRouAssetNBVReportItem;
  let expectedResult: IRouAssetNBVReportItem | IRouAssetNBVReportItem[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(RouAssetNBVReportItemService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      modelTitle: 'AAAAAAA',
      modelVersion: 0,
      description: 'AAAAAAA',
      rouModelReference: 'AAAAAAA',
      commencementDate: currentDate,
      expirationDate: currentDate,
      assetCategoryName: 'AAAAAAA',
      assetAccountNumber: 'AAAAAAA',
      depreciationAccountNumber: 'AAAAAAA',
      fiscalPeriodEndDate: currentDate,
      leaseAmount: 0,
      netBookValue: 0,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          commencementDate: currentDate.format(DATE_FORMAT),
          expirationDate: currentDate.format(DATE_FORMAT),
          fiscalPeriodEndDate: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should return a list of RouAssetNBVReportItem', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          modelTitle: 'BBBBBB',
          modelVersion: 1,
          description: 'BBBBBB',
          rouModelReference: 'BBBBBB',
          commencementDate: currentDate.format(DATE_FORMAT),
          expirationDate: currentDate.format(DATE_FORMAT),
          assetCategoryName: 'BBBBBB',
          assetAccountNumber: 'BBBBBB',
          depreciationAccountNumber: 'BBBBBB',
          fiscalPeriodEndDate: currentDate.format(DATE_FORMAT),
          leaseAmount: 1,
          netBookValue: 1,
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          commencementDate: currentDate,
          expirationDate: currentDate,
          fiscalPeriodEndDate: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    describe('addRouAssetNBVReportItemToCollectionIfMissing', () => {
      it('should add a RouAssetNBVReportItem to an empty array', () => {
        const rouAssetNBVReportItem: IRouAssetNBVReportItem = { id: 123 };
        expectedResult = service.addRouAssetNBVReportItemToCollectionIfMissing([], rouAssetNBVReportItem);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(rouAssetNBVReportItem);
      });

      it('should not add a RouAssetNBVReportItem to an array that contains it', () => {
        const rouAssetNBVReportItem: IRouAssetNBVReportItem = { id: 123 };
        const rouAssetNBVReportItemCollection: IRouAssetNBVReportItem[] = [
          {
            ...rouAssetNBVReportItem,
          },
          { id: 456 },
        ];
        expectedResult = service.addRouAssetNBVReportItemToCollectionIfMissing(rouAssetNBVReportItemCollection, rouAssetNBVReportItem);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a RouAssetNBVReportItem to an array that doesn't contain it", () => {
        const rouAssetNBVReportItem: IRouAssetNBVReportItem = { id: 123 };
        const rouAssetNBVReportItemCollection: IRouAssetNBVReportItem[] = [{ id: 456 }];
        expectedResult = service.addRouAssetNBVReportItemToCollectionIfMissing(rouAssetNBVReportItemCollection, rouAssetNBVReportItem);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(rouAssetNBVReportItem);
      });

      it('should add only unique RouAssetNBVReportItem to an array', () => {
        const rouAssetNBVReportItemArray: IRouAssetNBVReportItem[] = [{ id: 123 }, { id: 456 }, { id: 21288 }];
        const rouAssetNBVReportItemCollection: IRouAssetNBVReportItem[] = [{ id: 123 }];
        expectedResult = service.addRouAssetNBVReportItemToCollectionIfMissing(
          rouAssetNBVReportItemCollection,
          ...rouAssetNBVReportItemArray
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const rouAssetNBVReportItem: IRouAssetNBVReportItem = { id: 123 };
        const rouAssetNBVReportItem2: IRouAssetNBVReportItem = { id: 456 };
        expectedResult = service.addRouAssetNBVReportItemToCollectionIfMissing([], rouAssetNBVReportItem, rouAssetNBVReportItem2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(rouAssetNBVReportItem);
        expect(expectedResult).toContain(rouAssetNBVReportItem2);
      });

      it('should accept null and undefined values', () => {
        const rouAssetNBVReportItem: IRouAssetNBVReportItem = { id: 123 };
        expectedResult = service.addRouAssetNBVReportItemToCollectionIfMissing([], null, rouAssetNBVReportItem, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(rouAssetNBVReportItem);
      });

      it('should return initial array if no RouAssetNBVReportItem is added', () => {
        const rouAssetNBVReportItemCollection: IRouAssetNBVReportItem[] = [{ id: 123 }];
        expectedResult = service.addRouAssetNBVReportItemToCollectionIfMissing(rouAssetNBVReportItemCollection, undefined, null);
        expect(expectedResult).toEqual(rouAssetNBVReportItemCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
