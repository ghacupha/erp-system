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

import { ICustomerType, CustomerType } from '../customer-type.model';

import { CustomerTypeService } from './customer-type.service';

describe('CustomerType Service', () => {
  let service: CustomerTypeService;
  let httpMock: HttpTestingController;
  let elemDefault: ICustomerType;
  let expectedResult: ICustomerType | ICustomerType[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CustomerTypeService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      customerTypeCode: 'AAAAAAA',
      customerType: 'AAAAAAA',
      customerTypeDescription: 'AAAAAAA',
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

    it('should create a CustomerType', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new CustomerType()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CustomerType', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          customerTypeCode: 'BBBBBB',
          customerType: 'BBBBBB',
          customerTypeDescription: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a CustomerType', () => {
      const patchObject = Object.assign(
        {
          customerTypeDescription: 'BBBBBB',
        },
        new CustomerType()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of CustomerType', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          customerTypeCode: 'BBBBBB',
          customerType: 'BBBBBB',
          customerTypeDescription: 'BBBBBB',
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

    it('should delete a CustomerType', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addCustomerTypeToCollectionIfMissing', () => {
      it('should add a CustomerType to an empty array', () => {
        const customerType: ICustomerType = { id: 123 };
        expectedResult = service.addCustomerTypeToCollectionIfMissing([], customerType);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(customerType);
      });

      it('should not add a CustomerType to an array that contains it', () => {
        const customerType: ICustomerType = { id: 123 };
        const customerTypeCollection: ICustomerType[] = [
          {
            ...customerType,
          },
          { id: 456 },
        ];
        expectedResult = service.addCustomerTypeToCollectionIfMissing(customerTypeCollection, customerType);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CustomerType to an array that doesn't contain it", () => {
        const customerType: ICustomerType = { id: 123 };
        const customerTypeCollection: ICustomerType[] = [{ id: 456 }];
        expectedResult = service.addCustomerTypeToCollectionIfMissing(customerTypeCollection, customerType);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(customerType);
      });

      it('should add only unique CustomerType to an array', () => {
        const customerTypeArray: ICustomerType[] = [{ id: 123 }, { id: 456 }, { id: 51648 }];
        const customerTypeCollection: ICustomerType[] = [{ id: 123 }];
        expectedResult = service.addCustomerTypeToCollectionIfMissing(customerTypeCollection, ...customerTypeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const customerType: ICustomerType = { id: 123 };
        const customerType2: ICustomerType = { id: 456 };
        expectedResult = service.addCustomerTypeToCollectionIfMissing([], customerType, customerType2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(customerType);
        expect(expectedResult).toContain(customerType2);
      });

      it('should accept null and undefined values', () => {
        const customerType: ICustomerType = { id: 123 };
        expectedResult = service.addCustomerTypeToCollectionIfMissing([], null, customerType, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(customerType);
      });

      it('should return initial array if no CustomerType is added', () => {
        const customerTypeCollection: ICustomerType[] = [{ id: 123 }];
        expectedResult = service.addCustomerTypeToCollectionIfMissing(customerTypeCollection, undefined, null);
        expect(expectedResult).toEqual(customerTypeCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
