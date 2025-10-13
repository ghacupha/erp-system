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
import { IAssetDisposal, AssetDisposal } from '../asset-disposal.model';

import { AssetDisposalService } from './asset-disposal.service';

describe('AssetDisposal Service', () => {
  let service: AssetDisposalService;
  let httpMock: HttpTestingController;
  let elemDefault: IAssetDisposal;
  let expectedResult: IAssetDisposal | IAssetDisposal[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(AssetDisposalService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      assetDisposalReference: 'AAAAAAA',
      description: 'AAAAAAA',
      assetCost: 0,
      historicalCost: 0,
      accruedDepreciation: 0,
      netBookValue: 0,
      decommissioningDate: currentDate,
      disposalDate: currentDate,
      dormant: false,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          decommissioningDate: currentDate.format(DATE_FORMAT),
          disposalDate: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a AssetDisposal', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          decommissioningDate: currentDate.format(DATE_FORMAT),
          disposalDate: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          decommissioningDate: currentDate,
          disposalDate: currentDate,
        },
        returnedFromService
      );

      service.create(new AssetDisposal()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a AssetDisposal', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          assetDisposalReference: 'BBBBBB',
          description: 'BBBBBB',
          assetCost: 1,
          historicalCost: 1,
          accruedDepreciation: 1,
          netBookValue: 1,
          decommissioningDate: currentDate.format(DATE_FORMAT),
          disposalDate: currentDate.format(DATE_FORMAT),
          dormant: true,
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          decommissioningDate: currentDate,
          disposalDate: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a AssetDisposal', () => {
      const patchObject = Object.assign(
        {
          assetDisposalReference: 'BBBBBB',
          description: 'BBBBBB',
          netBookValue: 1,
          dormant: true,
        },
        new AssetDisposal()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          decommissioningDate: currentDate,
          disposalDate: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of AssetDisposal', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          assetDisposalReference: 'BBBBBB',
          description: 'BBBBBB',
          assetCost: 1,
          historicalCost: 1,
          accruedDepreciation: 1,
          netBookValue: 1,
          decommissioningDate: currentDate.format(DATE_FORMAT),
          disposalDate: currentDate.format(DATE_FORMAT),
          dormant: true,
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          decommissioningDate: currentDate,
          disposalDate: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a AssetDisposal', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addAssetDisposalToCollectionIfMissing', () => {
      it('should add a AssetDisposal to an empty array', () => {
        const assetDisposal: IAssetDisposal = { id: 123 };
        expectedResult = service.addAssetDisposalToCollectionIfMissing([], assetDisposal);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(assetDisposal);
      });

      it('should not add a AssetDisposal to an array that contains it', () => {
        const assetDisposal: IAssetDisposal = { id: 123 };
        const assetDisposalCollection: IAssetDisposal[] = [
          {
            ...assetDisposal,
          },
          { id: 456 },
        ];
        expectedResult = service.addAssetDisposalToCollectionIfMissing(assetDisposalCollection, assetDisposal);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a AssetDisposal to an array that doesn't contain it", () => {
        const assetDisposal: IAssetDisposal = { id: 123 };
        const assetDisposalCollection: IAssetDisposal[] = [{ id: 456 }];
        expectedResult = service.addAssetDisposalToCollectionIfMissing(assetDisposalCollection, assetDisposal);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(assetDisposal);
      });

      it('should add only unique AssetDisposal to an array', () => {
        const assetDisposalArray: IAssetDisposal[] = [{ id: 123 }, { id: 456 }, { id: 89968 }];
        const assetDisposalCollection: IAssetDisposal[] = [{ id: 123 }];
        expectedResult = service.addAssetDisposalToCollectionIfMissing(assetDisposalCollection, ...assetDisposalArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const assetDisposal: IAssetDisposal = { id: 123 };
        const assetDisposal2: IAssetDisposal = { id: 456 };
        expectedResult = service.addAssetDisposalToCollectionIfMissing([], assetDisposal, assetDisposal2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(assetDisposal);
        expect(expectedResult).toContain(assetDisposal2);
      });

      it('should accept null and undefined values', () => {
        const assetDisposal: IAssetDisposal = { id: 123 };
        expectedResult = service.addAssetDisposalToCollectionIfMissing([], null, assetDisposal, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(assetDisposal);
      });

      it('should return initial array if no AssetDisposal is added', () => {
        const assetDisposalCollection: IAssetDisposal[] = [{ id: 123 }];
        expectedResult = service.addAssetDisposalToCollectionIfMissing(assetDisposalCollection, undefined, null);
        expect(expectedResult).toEqual(assetDisposalCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
