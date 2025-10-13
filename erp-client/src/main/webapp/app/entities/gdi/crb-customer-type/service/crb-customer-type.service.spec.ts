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

import { ICrbCustomerType, CrbCustomerType } from '../crb-customer-type.model';

import { CrbCustomerTypeService } from './crb-customer-type.service';

describe('CrbCustomerType Service', () => {
  let service: CrbCustomerTypeService;
  let httpMock: HttpTestingController;
  let elemDefault: ICrbCustomerType;
  let expectedResult: ICrbCustomerType | ICrbCustomerType[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CrbCustomerTypeService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      customerTypeCode: 'AAAAAAA',
      customerType: 'AAAAAAA',
      description: 'AAAAAAA',
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

    it('should create a CrbCustomerType', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new CrbCustomerType()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CrbCustomerType', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          customerTypeCode: 'BBBBBB',
          customerType: 'BBBBBB',
          description: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a CrbCustomerType', () => {
      const patchObject = Object.assign(
        {
          customerTypeCode: 'BBBBBB',
          description: 'BBBBBB',
        },
        new CrbCustomerType()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of CrbCustomerType', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          customerTypeCode: 'BBBBBB',
          customerType: 'BBBBBB',
          description: 'BBBBBB',
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

    it('should delete a CrbCustomerType', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addCrbCustomerTypeToCollectionIfMissing', () => {
      it('should add a CrbCustomerType to an empty array', () => {
        const crbCustomerType: ICrbCustomerType = { id: 123 };
        expectedResult = service.addCrbCustomerTypeToCollectionIfMissing([], crbCustomerType);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(crbCustomerType);
      });

      it('should not add a CrbCustomerType to an array that contains it', () => {
        const crbCustomerType: ICrbCustomerType = { id: 123 };
        const crbCustomerTypeCollection: ICrbCustomerType[] = [
          {
            ...crbCustomerType,
          },
          { id: 456 },
        ];
        expectedResult = service.addCrbCustomerTypeToCollectionIfMissing(crbCustomerTypeCollection, crbCustomerType);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CrbCustomerType to an array that doesn't contain it", () => {
        const crbCustomerType: ICrbCustomerType = { id: 123 };
        const crbCustomerTypeCollection: ICrbCustomerType[] = [{ id: 456 }];
        expectedResult = service.addCrbCustomerTypeToCollectionIfMissing(crbCustomerTypeCollection, crbCustomerType);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(crbCustomerType);
      });

      it('should add only unique CrbCustomerType to an array', () => {
        const crbCustomerTypeArray: ICrbCustomerType[] = [{ id: 123 }, { id: 456 }, { id: 55177 }];
        const crbCustomerTypeCollection: ICrbCustomerType[] = [{ id: 123 }];
        expectedResult = service.addCrbCustomerTypeToCollectionIfMissing(crbCustomerTypeCollection, ...crbCustomerTypeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const crbCustomerType: ICrbCustomerType = { id: 123 };
        const crbCustomerType2: ICrbCustomerType = { id: 456 };
        expectedResult = service.addCrbCustomerTypeToCollectionIfMissing([], crbCustomerType, crbCustomerType2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(crbCustomerType);
        expect(expectedResult).toContain(crbCustomerType2);
      });

      it('should accept null and undefined values', () => {
        const crbCustomerType: ICrbCustomerType = { id: 123 };
        expectedResult = service.addCrbCustomerTypeToCollectionIfMissing([], null, crbCustomerType, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(crbCustomerType);
      });

      it('should return initial array if no CrbCustomerType is added', () => {
        const crbCustomerTypeCollection: ICrbCustomerType[] = [{ id: 123 }];
        expectedResult = service.addCrbCustomerTypeToCollectionIfMissing(crbCustomerTypeCollection, undefined, null);
        expect(expectedResult).toEqual(crbCustomerTypeCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
