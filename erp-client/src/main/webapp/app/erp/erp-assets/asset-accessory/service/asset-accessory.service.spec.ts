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

import { IAssetAccessory, AssetAccessory } from '../asset-accessory.model';

import { AssetAccessoryService } from './asset-accessory.service';

describe('AssetAccessory Service', () => {
  let service: AssetAccessoryService;
  let httpMock: HttpTestingController;
  let elemDefault: IAssetAccessory;
  let expectedResult: IAssetAccessory | IAssetAccessory[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(AssetAccessoryService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      assetTag: 'AAAAAAA',
      assetDetails: 'AAAAAAA',
      commentsContentType: 'image/png',
      comments: 'AAAAAAA',
      modelNumber: 'AAAAAAA',
      serialNumber: 'AAAAAAA',
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

    it('should create a AssetAccessory', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new AssetAccessory()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a AssetAccessory', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          assetTag: 'BBBBBB',
          assetDetails: 'BBBBBB',
          comments: 'BBBBBB',
          modelNumber: 'BBBBBB',
          serialNumber: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a AssetAccessory', () => {
      const patchObject = Object.assign(
        {
          assetDetails: 'BBBBBB',
        },
        new AssetAccessory()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of AssetAccessory', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          assetTag: 'BBBBBB',
          assetDetails: 'BBBBBB',
          comments: 'BBBBBB',
          modelNumber: 'BBBBBB',
          serialNumber: 'BBBBBB',
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

    it('should delete a AssetAccessory', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addAssetAccessoryToCollectionIfMissing', () => {
      it('should add a AssetAccessory to an empty array', () => {
        const assetAccessory: IAssetAccessory = { id: 123 };
        expectedResult = service.addAssetAccessoryToCollectionIfMissing([], assetAccessory);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(assetAccessory);
      });

      it('should not add a AssetAccessory to an array that contains it', () => {
        const assetAccessory: IAssetAccessory = { id: 123 };
        const assetAccessoryCollection: IAssetAccessory[] = [
          {
            ...assetAccessory,
          },
          { id: 456 },
        ];
        expectedResult = service.addAssetAccessoryToCollectionIfMissing(assetAccessoryCollection, assetAccessory);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a AssetAccessory to an array that doesn't contain it", () => {
        const assetAccessory: IAssetAccessory = { id: 123 };
        const assetAccessoryCollection: IAssetAccessory[] = [{ id: 456 }];
        expectedResult = service.addAssetAccessoryToCollectionIfMissing(assetAccessoryCollection, assetAccessory);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(assetAccessory);
      });

      it('should add only unique AssetAccessory to an array', () => {
        const assetAccessoryArray: IAssetAccessory[] = [{ id: 123 }, { id: 456 }, { id: 91969 }];
        const assetAccessoryCollection: IAssetAccessory[] = [{ id: 123 }];
        expectedResult = service.addAssetAccessoryToCollectionIfMissing(assetAccessoryCollection, ...assetAccessoryArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const assetAccessory: IAssetAccessory = { id: 123 };
        const assetAccessory2: IAssetAccessory = { id: 456 };
        expectedResult = service.addAssetAccessoryToCollectionIfMissing([], assetAccessory, assetAccessory2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(assetAccessory);
        expect(expectedResult).toContain(assetAccessory2);
      });

      it('should accept null and undefined values', () => {
        const assetAccessory: IAssetAccessory = { id: 123 };
        expectedResult = service.addAssetAccessoryToCollectionIfMissing([], null, assetAccessory, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(assetAccessory);
      });

      it('should return initial array if no AssetAccessory is added', () => {
        const assetAccessoryCollection: IAssetAccessory[] = [{ id: 123 }];
        expectedResult = service.addAssetAccessoryToCollectionIfMissing(assetAccessoryCollection, undefined, null);
        expect(expectedResult).toEqual(assetAccessoryCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
