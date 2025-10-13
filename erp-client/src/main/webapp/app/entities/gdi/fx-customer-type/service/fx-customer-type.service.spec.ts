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

import { IFxCustomerType, FxCustomerType } from '../fx-customer-type.model';

import { FxCustomerTypeService } from './fx-customer-type.service';

describe('FxCustomerType Service', () => {
  let service: FxCustomerTypeService;
  let httpMock: HttpTestingController;
  let elemDefault: IFxCustomerType;
  let expectedResult: IFxCustomerType | IFxCustomerType[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(FxCustomerTypeService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      foreignExchangeCustomerTypeCode: 'AAAAAAA',
      foreignCustomerType: 'AAAAAAA',
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

    it('should create a FxCustomerType', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new FxCustomerType()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a FxCustomerType', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          foreignExchangeCustomerTypeCode: 'BBBBBB',
          foreignCustomerType: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a FxCustomerType', () => {
      const patchObject = Object.assign(
        {
          foreignExchangeCustomerTypeCode: 'BBBBBB',
          foreignCustomerType: 'BBBBBB',
        },
        new FxCustomerType()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of FxCustomerType', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          foreignExchangeCustomerTypeCode: 'BBBBBB',
          foreignCustomerType: 'BBBBBB',
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

    it('should delete a FxCustomerType', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addFxCustomerTypeToCollectionIfMissing', () => {
      it('should add a FxCustomerType to an empty array', () => {
        const fxCustomerType: IFxCustomerType = { id: 123 };
        expectedResult = service.addFxCustomerTypeToCollectionIfMissing([], fxCustomerType);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(fxCustomerType);
      });

      it('should not add a FxCustomerType to an array that contains it', () => {
        const fxCustomerType: IFxCustomerType = { id: 123 };
        const fxCustomerTypeCollection: IFxCustomerType[] = [
          {
            ...fxCustomerType,
          },
          { id: 456 },
        ];
        expectedResult = service.addFxCustomerTypeToCollectionIfMissing(fxCustomerTypeCollection, fxCustomerType);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a FxCustomerType to an array that doesn't contain it", () => {
        const fxCustomerType: IFxCustomerType = { id: 123 };
        const fxCustomerTypeCollection: IFxCustomerType[] = [{ id: 456 }];
        expectedResult = service.addFxCustomerTypeToCollectionIfMissing(fxCustomerTypeCollection, fxCustomerType);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(fxCustomerType);
      });

      it('should add only unique FxCustomerType to an array', () => {
        const fxCustomerTypeArray: IFxCustomerType[] = [{ id: 123 }, { id: 456 }, { id: 94099 }];
        const fxCustomerTypeCollection: IFxCustomerType[] = [{ id: 123 }];
        expectedResult = service.addFxCustomerTypeToCollectionIfMissing(fxCustomerTypeCollection, ...fxCustomerTypeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const fxCustomerType: IFxCustomerType = { id: 123 };
        const fxCustomerType2: IFxCustomerType = { id: 456 };
        expectedResult = service.addFxCustomerTypeToCollectionIfMissing([], fxCustomerType, fxCustomerType2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(fxCustomerType);
        expect(expectedResult).toContain(fxCustomerType2);
      });

      it('should accept null and undefined values', () => {
        const fxCustomerType: IFxCustomerType = { id: 123 };
        expectedResult = service.addFxCustomerTypeToCollectionIfMissing([], null, fxCustomerType, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(fxCustomerType);
      });

      it('should return initial array if no FxCustomerType is added', () => {
        const fxCustomerTypeCollection: IFxCustomerType[] = [{ id: 123 }];
        expectedResult = service.addFxCustomerTypeToCollectionIfMissing(fxCustomerTypeCollection, undefined, null);
        expect(expectedResult).toEqual(fxCustomerTypeCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
