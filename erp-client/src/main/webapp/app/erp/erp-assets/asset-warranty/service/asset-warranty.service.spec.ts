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
import { IAssetWarranty, AssetWarranty } from '../asset-warranty.model';

import { AssetWarrantyService } from './asset-warranty.service';

describe('AssetWarranty Service', () => {
  let service: AssetWarrantyService;
  let httpMock: HttpTestingController;
  let elemDefault: IAssetWarranty;
  let expectedResult: IAssetWarranty | IAssetWarranty[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(AssetWarrantyService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      assetTag: 'AAAAAAA',
      description: 'AAAAAAA',
      modelNumber: 'AAAAAAA',
      serialNumber: 'AAAAAAA',
      expiryDate: currentDate,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          expiryDate: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a AssetWarranty', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          expiryDate: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          expiryDate: currentDate,
        },
        returnedFromService
      );

      service.create(new AssetWarranty()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a AssetWarranty', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          assetTag: 'BBBBBB',
          description: 'BBBBBB',
          modelNumber: 'BBBBBB',
          serialNumber: 'BBBBBB',
          expiryDate: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          expiryDate: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a AssetWarranty', () => {
      const patchObject = Object.assign(
        {
          description: 'BBBBBB',
          modelNumber: 'BBBBBB',
          expiryDate: currentDate.format(DATE_FORMAT),
        },
        new AssetWarranty()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          expiryDate: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of AssetWarranty', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          assetTag: 'BBBBBB',
          description: 'BBBBBB',
          modelNumber: 'BBBBBB',
          serialNumber: 'BBBBBB',
          expiryDate: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          expiryDate: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a AssetWarranty', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addAssetWarrantyToCollectionIfMissing', () => {
      it('should add a AssetWarranty to an empty array', () => {
        const assetWarranty: IAssetWarranty = { id: 123 };
        expectedResult = service.addAssetWarrantyToCollectionIfMissing([], assetWarranty);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(assetWarranty);
      });

      it('should not add a AssetWarranty to an array that contains it', () => {
        const assetWarranty: IAssetWarranty = { id: 123 };
        const assetWarrantyCollection: IAssetWarranty[] = [
          {
            ...assetWarranty,
          },
          { id: 456 },
        ];
        expectedResult = service.addAssetWarrantyToCollectionIfMissing(assetWarrantyCollection, assetWarranty);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a AssetWarranty to an array that doesn't contain it", () => {
        const assetWarranty: IAssetWarranty = { id: 123 };
        const assetWarrantyCollection: IAssetWarranty[] = [{ id: 456 }];
        expectedResult = service.addAssetWarrantyToCollectionIfMissing(assetWarrantyCollection, assetWarranty);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(assetWarranty);
      });

      it('should add only unique AssetWarranty to an array', () => {
        const assetWarrantyArray: IAssetWarranty[] = [{ id: 123 }, { id: 456 }, { id: 78222 }];
        const assetWarrantyCollection: IAssetWarranty[] = [{ id: 123 }];
        expectedResult = service.addAssetWarrantyToCollectionIfMissing(assetWarrantyCollection, ...assetWarrantyArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const assetWarranty: IAssetWarranty = { id: 123 };
        const assetWarranty2: IAssetWarranty = { id: 456 };
        expectedResult = service.addAssetWarrantyToCollectionIfMissing([], assetWarranty, assetWarranty2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(assetWarranty);
        expect(expectedResult).toContain(assetWarranty2);
      });

      it('should accept null and undefined values', () => {
        const assetWarranty: IAssetWarranty = { id: 123 };
        expectedResult = service.addAssetWarrantyToCollectionIfMissing([], null, assetWarranty, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(assetWarranty);
      });

      it('should return initial array if no AssetWarranty is added', () => {
        const assetWarrantyCollection: IAssetWarranty[] = [{ id: 123 }];
        expectedResult = service.addAssetWarrantyToCollectionIfMissing(assetWarrantyCollection, undefined, null);
        expect(expectedResult).toEqual(assetWarrantyCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
