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

import { ICustomerIDDocumentType, CustomerIDDocumentType } from '../customer-id-document-type.model';

import { CustomerIDDocumentTypeService } from './customer-id-document-type.service';

describe('CustomerIDDocumentType Service', () => {
  let service: CustomerIDDocumentTypeService;
  let httpMock: HttpTestingController;
  let elemDefault: ICustomerIDDocumentType;
  let expectedResult: ICustomerIDDocumentType | ICustomerIDDocumentType[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CustomerIDDocumentTypeService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      documentCode: 'AAAAAAA',
      documentType: 'AAAAAAA',
      documentTypeDescription: 'AAAAAAA',
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

    it('should create a CustomerIDDocumentType', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new CustomerIDDocumentType()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CustomerIDDocumentType', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          documentCode: 'BBBBBB',
          documentType: 'BBBBBB',
          documentTypeDescription: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a CustomerIDDocumentType', () => {
      const patchObject = Object.assign(
        {
          documentCode: 'BBBBBB',
          documentType: 'BBBBBB',
          documentTypeDescription: 'BBBBBB',
        },
        new CustomerIDDocumentType()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of CustomerIDDocumentType', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          documentCode: 'BBBBBB',
          documentType: 'BBBBBB',
          documentTypeDescription: 'BBBBBB',
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

    it('should delete a CustomerIDDocumentType', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addCustomerIDDocumentTypeToCollectionIfMissing', () => {
      it('should add a CustomerIDDocumentType to an empty array', () => {
        const customerIDDocumentType: ICustomerIDDocumentType = { id: 123 };
        expectedResult = service.addCustomerIDDocumentTypeToCollectionIfMissing([], customerIDDocumentType);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(customerIDDocumentType);
      });

      it('should not add a CustomerIDDocumentType to an array that contains it', () => {
        const customerIDDocumentType: ICustomerIDDocumentType = { id: 123 };
        const customerIDDocumentTypeCollection: ICustomerIDDocumentType[] = [
          {
            ...customerIDDocumentType,
          },
          { id: 456 },
        ];
        expectedResult = service.addCustomerIDDocumentTypeToCollectionIfMissing(customerIDDocumentTypeCollection, customerIDDocumentType);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CustomerIDDocumentType to an array that doesn't contain it", () => {
        const customerIDDocumentType: ICustomerIDDocumentType = { id: 123 };
        const customerIDDocumentTypeCollection: ICustomerIDDocumentType[] = [{ id: 456 }];
        expectedResult = service.addCustomerIDDocumentTypeToCollectionIfMissing(customerIDDocumentTypeCollection, customerIDDocumentType);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(customerIDDocumentType);
      });

      it('should add only unique CustomerIDDocumentType to an array', () => {
        const customerIDDocumentTypeArray: ICustomerIDDocumentType[] = [{ id: 123 }, { id: 456 }, { id: 18371 }];
        const customerIDDocumentTypeCollection: ICustomerIDDocumentType[] = [{ id: 123 }];
        expectedResult = service.addCustomerIDDocumentTypeToCollectionIfMissing(
          customerIDDocumentTypeCollection,
          ...customerIDDocumentTypeArray
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const customerIDDocumentType: ICustomerIDDocumentType = { id: 123 };
        const customerIDDocumentType2: ICustomerIDDocumentType = { id: 456 };
        expectedResult = service.addCustomerIDDocumentTypeToCollectionIfMissing([], customerIDDocumentType, customerIDDocumentType2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(customerIDDocumentType);
        expect(expectedResult).toContain(customerIDDocumentType2);
      });

      it('should accept null and undefined values', () => {
        const customerIDDocumentType: ICustomerIDDocumentType = { id: 123 };
        expectedResult = service.addCustomerIDDocumentTypeToCollectionIfMissing([], null, customerIDDocumentType, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(customerIDDocumentType);
      });

      it('should return initial array if no CustomerIDDocumentType is added', () => {
        const customerIDDocumentTypeCollection: ICustomerIDDocumentType[] = [{ id: 123 }];
        expectedResult = service.addCustomerIDDocumentTypeToCollectionIfMissing(customerIDDocumentTypeCollection, undefined, null);
        expect(expectedResult).toEqual(customerIDDocumentTypeCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
