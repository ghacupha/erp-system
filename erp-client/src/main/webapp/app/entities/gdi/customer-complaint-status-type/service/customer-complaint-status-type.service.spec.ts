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

import { ICustomerComplaintStatusType, CustomerComplaintStatusType } from '../customer-complaint-status-type.model';

import { CustomerComplaintStatusTypeService } from './customer-complaint-status-type.service';

describe('CustomerComplaintStatusType Service', () => {
  let service: CustomerComplaintStatusTypeService;
  let httpMock: HttpTestingController;
  let elemDefault: ICustomerComplaintStatusType;
  let expectedResult: ICustomerComplaintStatusType | ICustomerComplaintStatusType[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CustomerComplaintStatusTypeService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      customerComplaintStatusTypeCode: 'AAAAAAA',
      customerComplaintStatusType: 'AAAAAAA',
      customerComplaintStatusTypeDetails: 'AAAAAAA',
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

    it('should create a CustomerComplaintStatusType', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new CustomerComplaintStatusType()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CustomerComplaintStatusType', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          customerComplaintStatusTypeCode: 'BBBBBB',
          customerComplaintStatusType: 'BBBBBB',
          customerComplaintStatusTypeDetails: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a CustomerComplaintStatusType', () => {
      const patchObject = Object.assign(
        {
          customerComplaintStatusTypeCode: 'BBBBBB',
          customerComplaintStatusType: 'BBBBBB',
          customerComplaintStatusTypeDetails: 'BBBBBB',
        },
        new CustomerComplaintStatusType()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of CustomerComplaintStatusType', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          customerComplaintStatusTypeCode: 'BBBBBB',
          customerComplaintStatusType: 'BBBBBB',
          customerComplaintStatusTypeDetails: 'BBBBBB',
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

    it('should delete a CustomerComplaintStatusType', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addCustomerComplaintStatusTypeToCollectionIfMissing', () => {
      it('should add a CustomerComplaintStatusType to an empty array', () => {
        const customerComplaintStatusType: ICustomerComplaintStatusType = { id: 123 };
        expectedResult = service.addCustomerComplaintStatusTypeToCollectionIfMissing([], customerComplaintStatusType);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(customerComplaintStatusType);
      });

      it('should not add a CustomerComplaintStatusType to an array that contains it', () => {
        const customerComplaintStatusType: ICustomerComplaintStatusType = { id: 123 };
        const customerComplaintStatusTypeCollection: ICustomerComplaintStatusType[] = [
          {
            ...customerComplaintStatusType,
          },
          { id: 456 },
        ];
        expectedResult = service.addCustomerComplaintStatusTypeToCollectionIfMissing(
          customerComplaintStatusTypeCollection,
          customerComplaintStatusType
        );
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CustomerComplaintStatusType to an array that doesn't contain it", () => {
        const customerComplaintStatusType: ICustomerComplaintStatusType = { id: 123 };
        const customerComplaintStatusTypeCollection: ICustomerComplaintStatusType[] = [{ id: 456 }];
        expectedResult = service.addCustomerComplaintStatusTypeToCollectionIfMissing(
          customerComplaintStatusTypeCollection,
          customerComplaintStatusType
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(customerComplaintStatusType);
      });

      it('should add only unique CustomerComplaintStatusType to an array', () => {
        const customerComplaintStatusTypeArray: ICustomerComplaintStatusType[] = [{ id: 123 }, { id: 456 }, { id: 97734 }];
        const customerComplaintStatusTypeCollection: ICustomerComplaintStatusType[] = [{ id: 123 }];
        expectedResult = service.addCustomerComplaintStatusTypeToCollectionIfMissing(
          customerComplaintStatusTypeCollection,
          ...customerComplaintStatusTypeArray
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const customerComplaintStatusType: ICustomerComplaintStatusType = { id: 123 };
        const customerComplaintStatusType2: ICustomerComplaintStatusType = { id: 456 };
        expectedResult = service.addCustomerComplaintStatusTypeToCollectionIfMissing(
          [],
          customerComplaintStatusType,
          customerComplaintStatusType2
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(customerComplaintStatusType);
        expect(expectedResult).toContain(customerComplaintStatusType2);
      });

      it('should accept null and undefined values', () => {
        const customerComplaintStatusType: ICustomerComplaintStatusType = { id: 123 };
        expectedResult = service.addCustomerComplaintStatusTypeToCollectionIfMissing([], null, customerComplaintStatusType, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(customerComplaintStatusType);
      });

      it('should return initial array if no CustomerComplaintStatusType is added', () => {
        const customerComplaintStatusTypeCollection: ICustomerComplaintStatusType[] = [{ id: 123 }];
        expectedResult = service.addCustomerComplaintStatusTypeToCollectionIfMissing(
          customerComplaintStatusTypeCollection,
          undefined,
          null
        );
        expect(expectedResult).toEqual(customerComplaintStatusTypeCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
