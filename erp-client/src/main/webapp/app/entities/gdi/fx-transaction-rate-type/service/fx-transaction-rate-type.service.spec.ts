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

import { IFxTransactionRateType, FxTransactionRateType } from '../fx-transaction-rate-type.model';

import { FxTransactionRateTypeService } from './fx-transaction-rate-type.service';

describe('FxTransactionRateType Service', () => {
  let service: FxTransactionRateTypeService;
  let httpMock: HttpTestingController;
  let elemDefault: IFxTransactionRateType;
  let expectedResult: IFxTransactionRateType | IFxTransactionRateType[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(FxTransactionRateTypeService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      fxTransactionRateTypeCode: 'AAAAAAA',
      fxTransactionRateType: 'AAAAAAA',
      fxTransactionRateTypeDetails: 'AAAAAAA',
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

    it('should create a FxTransactionRateType', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new FxTransactionRateType()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a FxTransactionRateType', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          fxTransactionRateTypeCode: 'BBBBBB',
          fxTransactionRateType: 'BBBBBB',
          fxTransactionRateTypeDetails: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a FxTransactionRateType', () => {
      const patchObject = Object.assign(
        {
          fxTransactionRateTypeCode: 'BBBBBB',
          fxTransactionRateTypeDetails: 'BBBBBB',
        },
        new FxTransactionRateType()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of FxTransactionRateType', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          fxTransactionRateTypeCode: 'BBBBBB',
          fxTransactionRateType: 'BBBBBB',
          fxTransactionRateTypeDetails: 'BBBBBB',
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

    it('should delete a FxTransactionRateType', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addFxTransactionRateTypeToCollectionIfMissing', () => {
      it('should add a FxTransactionRateType to an empty array', () => {
        const fxTransactionRateType: IFxTransactionRateType = { id: 123 };
        expectedResult = service.addFxTransactionRateTypeToCollectionIfMissing([], fxTransactionRateType);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(fxTransactionRateType);
      });

      it('should not add a FxTransactionRateType to an array that contains it', () => {
        const fxTransactionRateType: IFxTransactionRateType = { id: 123 };
        const fxTransactionRateTypeCollection: IFxTransactionRateType[] = [
          {
            ...fxTransactionRateType,
          },
          { id: 456 },
        ];
        expectedResult = service.addFxTransactionRateTypeToCollectionIfMissing(fxTransactionRateTypeCollection, fxTransactionRateType);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a FxTransactionRateType to an array that doesn't contain it", () => {
        const fxTransactionRateType: IFxTransactionRateType = { id: 123 };
        const fxTransactionRateTypeCollection: IFxTransactionRateType[] = [{ id: 456 }];
        expectedResult = service.addFxTransactionRateTypeToCollectionIfMissing(fxTransactionRateTypeCollection, fxTransactionRateType);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(fxTransactionRateType);
      });

      it('should add only unique FxTransactionRateType to an array', () => {
        const fxTransactionRateTypeArray: IFxTransactionRateType[] = [{ id: 123 }, { id: 456 }, { id: 40404 }];
        const fxTransactionRateTypeCollection: IFxTransactionRateType[] = [{ id: 123 }];
        expectedResult = service.addFxTransactionRateTypeToCollectionIfMissing(
          fxTransactionRateTypeCollection,
          ...fxTransactionRateTypeArray
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const fxTransactionRateType: IFxTransactionRateType = { id: 123 };
        const fxTransactionRateType2: IFxTransactionRateType = { id: 456 };
        expectedResult = service.addFxTransactionRateTypeToCollectionIfMissing([], fxTransactionRateType, fxTransactionRateType2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(fxTransactionRateType);
        expect(expectedResult).toContain(fxTransactionRateType2);
      });

      it('should accept null and undefined values', () => {
        const fxTransactionRateType: IFxTransactionRateType = { id: 123 };
        expectedResult = service.addFxTransactionRateTypeToCollectionIfMissing([], null, fxTransactionRateType, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(fxTransactionRateType);
      });

      it('should return initial array if no FxTransactionRateType is added', () => {
        const fxTransactionRateTypeCollection: IFxTransactionRateType[] = [{ id: 123 }];
        expectedResult = service.addFxTransactionRateTypeToCollectionIfMissing(fxTransactionRateTypeCollection, undefined, null);
        expect(expectedResult).toEqual(fxTransactionRateTypeCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
