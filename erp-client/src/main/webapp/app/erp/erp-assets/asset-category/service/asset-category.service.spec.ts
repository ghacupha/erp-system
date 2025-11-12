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

import { IAssetCategory, AssetCategory } from '../asset-category.model';

import { AssetCategoryService } from './asset-category.service';

describe('AssetCategory Service', () => {
  let service: AssetCategoryService;
  let httpMock: HttpTestingController;
  let elemDefault: IAssetCategory;
  let expectedResult: IAssetCategory | IAssetCategory[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(AssetCategoryService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      assetCategoryName: 'AAAAAAA',
      description: 'AAAAAAA',
      notes: 'AAAAAAA',
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

    it('should create a AssetCategory', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new AssetCategory()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a AssetCategory', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          assetCategoryName: 'BBBBBB',
          description: 'BBBBBB',
          notes: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a AssetCategory', () => {
      const patchObject = Object.assign(
        {
          notes: 'BBBBBB',
        },
        new AssetCategory()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of AssetCategory', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          assetCategoryName: 'BBBBBB',
          description: 'BBBBBB',
          notes: 'BBBBBB',
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

    it('should delete a AssetCategory', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addAssetCategoryToCollectionIfMissing', () => {
      it('should add a AssetCategory to an empty array', () => {
        const assetCategory: IAssetCategory = { id: 123 };
        expectedResult = service.addAssetCategoryToCollectionIfMissing([], assetCategory);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(assetCategory);
      });

      it('should not add a AssetCategory to an array that contains it', () => {
        const assetCategory: IAssetCategory = { id: 123 };
        const assetCategoryCollection: IAssetCategory[] = [
          {
            ...assetCategory,
          },
          { id: 456 },
        ];
        expectedResult = service.addAssetCategoryToCollectionIfMissing(assetCategoryCollection, assetCategory);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a AssetCategory to an array that doesn't contain it", () => {
        const assetCategory: IAssetCategory = { id: 123 };
        const assetCategoryCollection: IAssetCategory[] = [{ id: 456 }];
        expectedResult = service.addAssetCategoryToCollectionIfMissing(assetCategoryCollection, assetCategory);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(assetCategory);
      });

      it('should add only unique AssetCategory to an array', () => {
        const assetCategoryArray: IAssetCategory[] = [{ id: 123 }, { id: 456 }, { id: 55057 }];
        const assetCategoryCollection: IAssetCategory[] = [{ id: 123 }];
        expectedResult = service.addAssetCategoryToCollectionIfMissing(assetCategoryCollection, ...assetCategoryArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const assetCategory: IAssetCategory = { id: 123 };
        const assetCategory2: IAssetCategory = { id: 456 };
        expectedResult = service.addAssetCategoryToCollectionIfMissing([], assetCategory, assetCategory2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(assetCategory);
        expect(expectedResult).toContain(assetCategory2);
      });

      it('should accept null and undefined values', () => {
        const assetCategory: IAssetCategory = { id: 123 };
        expectedResult = service.addAssetCategoryToCollectionIfMissing([], null, assetCategory, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(assetCategory);
      });

      it('should return initial array if no AssetCategory is added', () => {
        const assetCategoryCollection: IAssetCategory[] = [{ id: 123 }];
        expectedResult = service.addAssetCategoryToCollectionIfMissing(assetCategoryCollection, undefined, null);
        expect(expectedResult).toEqual(assetCategoryCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
