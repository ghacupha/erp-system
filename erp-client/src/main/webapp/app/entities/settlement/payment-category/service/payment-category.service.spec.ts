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

import { CategoryTypes } from 'app/entities/enumerations/category-types.model';
import { IPaymentCategory, PaymentCategory } from '../payment-category.model';

import { PaymentCategoryService } from './payment-category.service';

describe('PaymentCategory Service', () => {
  let service: PaymentCategoryService;
  let httpMock: HttpTestingController;
  let elemDefault: IPaymentCategory;
  let expectedResult: IPaymentCategory | IPaymentCategory[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(PaymentCategoryService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      categoryName: 'AAAAAAA',
      categoryDescription: 'AAAAAAA',
      categoryType: CategoryTypes.UNDEFINED,
      fileUploadToken: 'AAAAAAA',
      compilationToken: 'AAAAAAA',
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

    it('should create a PaymentCategory', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new PaymentCategory()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a PaymentCategory', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          categoryName: 'BBBBBB',
          categoryDescription: 'BBBBBB',
          categoryType: 'BBBBBB',
          fileUploadToken: 'BBBBBB',
          compilationToken: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a PaymentCategory', () => {
      const patchObject = Object.assign(
        {
          categoryName: 'BBBBBB',
          categoryDescription: 'BBBBBB',
          categoryType: 'BBBBBB',
          fileUploadToken: 'BBBBBB',
        },
        new PaymentCategory()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of PaymentCategory', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          categoryName: 'BBBBBB',
          categoryDescription: 'BBBBBB',
          categoryType: 'BBBBBB',
          fileUploadToken: 'BBBBBB',
          compilationToken: 'BBBBBB',
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

    it('should delete a PaymentCategory', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addPaymentCategoryToCollectionIfMissing', () => {
      it('should add a PaymentCategory to an empty array', () => {
        const paymentCategory: IPaymentCategory = { id: 123 };
        expectedResult = service.addPaymentCategoryToCollectionIfMissing([], paymentCategory);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(paymentCategory);
      });

      it('should not add a PaymentCategory to an array that contains it', () => {
        const paymentCategory: IPaymentCategory = { id: 123 };
        const paymentCategoryCollection: IPaymentCategory[] = [
          {
            ...paymentCategory,
          },
          { id: 456 },
        ];
        expectedResult = service.addPaymentCategoryToCollectionIfMissing(paymentCategoryCollection, paymentCategory);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a PaymentCategory to an array that doesn't contain it", () => {
        const paymentCategory: IPaymentCategory = { id: 123 };
        const paymentCategoryCollection: IPaymentCategory[] = [{ id: 456 }];
        expectedResult = service.addPaymentCategoryToCollectionIfMissing(paymentCategoryCollection, paymentCategory);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(paymentCategory);
      });

      it('should add only unique PaymentCategory to an array', () => {
        const paymentCategoryArray: IPaymentCategory[] = [{ id: 123 }, { id: 456 }, { id: 88706 }];
        const paymentCategoryCollection: IPaymentCategory[] = [{ id: 123 }];
        expectedResult = service.addPaymentCategoryToCollectionIfMissing(paymentCategoryCollection, ...paymentCategoryArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const paymentCategory: IPaymentCategory = { id: 123 };
        const paymentCategory2: IPaymentCategory = { id: 456 };
        expectedResult = service.addPaymentCategoryToCollectionIfMissing([], paymentCategory, paymentCategory2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(paymentCategory);
        expect(expectedResult).toContain(paymentCategory2);
      });

      it('should accept null and undefined values', () => {
        const paymentCategory: IPaymentCategory = { id: 123 };
        expectedResult = service.addPaymentCategoryToCollectionIfMissing([], null, paymentCategory, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(paymentCategory);
      });

      it('should return initial array if no PaymentCategory is added', () => {
        const paymentCategoryCollection: IPaymentCategory[] = [{ id: 123 }];
        expectedResult = service.addPaymentCategoryToCollectionIfMissing(paymentCategoryCollection, undefined, null);
        expect(expectedResult).toEqual(paymentCategoryCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
