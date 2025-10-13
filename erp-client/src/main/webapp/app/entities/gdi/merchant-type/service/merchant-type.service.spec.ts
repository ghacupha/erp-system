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

import { IMerchantType, MerchantType } from '../merchant-type.model';

import { MerchantTypeService } from './merchant-type.service';

describe('MerchantType Service', () => {
  let service: MerchantTypeService;
  let httpMock: HttpTestingController;
  let elemDefault: IMerchantType;
  let expectedResult: IMerchantType | IMerchantType[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(MerchantTypeService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      merchantTypeCode: 'AAAAAAA',
      merchantType: 'AAAAAAA',
      merchantTypeDetails: 'AAAAAAA',
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

    it('should create a MerchantType', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new MerchantType()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a MerchantType', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          merchantTypeCode: 'BBBBBB',
          merchantType: 'BBBBBB',
          merchantTypeDetails: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a MerchantType', () => {
      const patchObject = Object.assign({}, new MerchantType());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of MerchantType', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          merchantTypeCode: 'BBBBBB',
          merchantType: 'BBBBBB',
          merchantTypeDetails: 'BBBBBB',
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

    it('should delete a MerchantType', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addMerchantTypeToCollectionIfMissing', () => {
      it('should add a MerchantType to an empty array', () => {
        const merchantType: IMerchantType = { id: 123 };
        expectedResult = service.addMerchantTypeToCollectionIfMissing([], merchantType);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(merchantType);
      });

      it('should not add a MerchantType to an array that contains it', () => {
        const merchantType: IMerchantType = { id: 123 };
        const merchantTypeCollection: IMerchantType[] = [
          {
            ...merchantType,
          },
          { id: 456 },
        ];
        expectedResult = service.addMerchantTypeToCollectionIfMissing(merchantTypeCollection, merchantType);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a MerchantType to an array that doesn't contain it", () => {
        const merchantType: IMerchantType = { id: 123 };
        const merchantTypeCollection: IMerchantType[] = [{ id: 456 }];
        expectedResult = service.addMerchantTypeToCollectionIfMissing(merchantTypeCollection, merchantType);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(merchantType);
      });

      it('should add only unique MerchantType to an array', () => {
        const merchantTypeArray: IMerchantType[] = [{ id: 123 }, { id: 456 }, { id: 92960 }];
        const merchantTypeCollection: IMerchantType[] = [{ id: 123 }];
        expectedResult = service.addMerchantTypeToCollectionIfMissing(merchantTypeCollection, ...merchantTypeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const merchantType: IMerchantType = { id: 123 };
        const merchantType2: IMerchantType = { id: 456 };
        expectedResult = service.addMerchantTypeToCollectionIfMissing([], merchantType, merchantType2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(merchantType);
        expect(expectedResult).toContain(merchantType2);
      });

      it('should accept null and undefined values', () => {
        const merchantType: IMerchantType = { id: 123 };
        expectedResult = service.addMerchantTypeToCollectionIfMissing([], null, merchantType, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(merchantType);
      });

      it('should return initial array if no MerchantType is added', () => {
        const merchantTypeCollection: IMerchantType[] = [{ id: 123 }];
        expectedResult = service.addMerchantTypeToCollectionIfMissing(merchantTypeCollection, undefined, null);
        expect(expectedResult).toEqual(merchantTypeCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
