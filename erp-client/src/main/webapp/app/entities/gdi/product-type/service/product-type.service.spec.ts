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

import { IProductType, ProductType } from '../product-type.model';

import { ProductTypeService } from './product-type.service';

describe('ProductType Service', () => {
  let service: ProductTypeService;
  let httpMock: HttpTestingController;
  let elemDefault: IProductType;
  let expectedResult: IProductType | IProductType[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ProductTypeService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      productCode: 'AAAAAAA',
      productType: 'AAAAAAA',
      productTypeDescription: 'AAAAAAA',
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

    it('should create a ProductType', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new ProductType()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ProductType', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          productCode: 'BBBBBB',
          productType: 'BBBBBB',
          productTypeDescription: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ProductType', () => {
      const patchObject = Object.assign(
        {
          productType: 'BBBBBB',
          productTypeDescription: 'BBBBBB',
        },
        new ProductType()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ProductType', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          productCode: 'BBBBBB',
          productType: 'BBBBBB',
          productTypeDescription: 'BBBBBB',
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

    it('should delete a ProductType', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addProductTypeToCollectionIfMissing', () => {
      it('should add a ProductType to an empty array', () => {
        const productType: IProductType = { id: 123 };
        expectedResult = service.addProductTypeToCollectionIfMissing([], productType);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(productType);
      });

      it('should not add a ProductType to an array that contains it', () => {
        const productType: IProductType = { id: 123 };
        const productTypeCollection: IProductType[] = [
          {
            ...productType,
          },
          { id: 456 },
        ];
        expectedResult = service.addProductTypeToCollectionIfMissing(productTypeCollection, productType);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ProductType to an array that doesn't contain it", () => {
        const productType: IProductType = { id: 123 };
        const productTypeCollection: IProductType[] = [{ id: 456 }];
        expectedResult = service.addProductTypeToCollectionIfMissing(productTypeCollection, productType);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(productType);
      });

      it('should add only unique ProductType to an array', () => {
        const productTypeArray: IProductType[] = [{ id: 123 }, { id: 456 }, { id: 69627 }];
        const productTypeCollection: IProductType[] = [{ id: 123 }];
        expectedResult = service.addProductTypeToCollectionIfMissing(productTypeCollection, ...productTypeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const productType: IProductType = { id: 123 };
        const productType2: IProductType = { id: 456 };
        expectedResult = service.addProductTypeToCollectionIfMissing([], productType, productType2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(productType);
        expect(expectedResult).toContain(productType2);
      });

      it('should accept null and undefined values', () => {
        const productType: IProductType = { id: 123 };
        expectedResult = service.addProductTypeToCollectionIfMissing([], null, productType, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(productType);
      });

      it('should return initial array if no ProductType is added', () => {
        const productTypeCollection: IProductType[] = [{ id: 123 }];
        expectedResult = service.addProductTypeToCollectionIfMissing(productTypeCollection, undefined, null);
        expect(expectedResult).toEqual(productTypeCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
