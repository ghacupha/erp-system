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

import { IFxTransactionType, FxTransactionType } from '../fx-transaction-type.model';

import { FxTransactionTypeService } from './fx-transaction-type.service';

describe('FxTransactionType Service', () => {
  let service: FxTransactionTypeService;
  let httpMock: HttpTestingController;
  let elemDefault: IFxTransactionType;
  let expectedResult: IFxTransactionType | IFxTransactionType[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(FxTransactionTypeService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      fxTransactionTypeCode: 'AAAAAAA',
      fxTransactionType: 'AAAAAAA',
      fxTransactionTypeDescription: 'AAAAAAA',
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

    it('should create a FxTransactionType', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new FxTransactionType()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a FxTransactionType', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          fxTransactionTypeCode: 'BBBBBB',
          fxTransactionType: 'BBBBBB',
          fxTransactionTypeDescription: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a FxTransactionType', () => {
      const patchObject = Object.assign(
        {
          fxTransactionType: 'BBBBBB',
        },
        new FxTransactionType()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of FxTransactionType', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          fxTransactionTypeCode: 'BBBBBB',
          fxTransactionType: 'BBBBBB',
          fxTransactionTypeDescription: 'BBBBBB',
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

    it('should delete a FxTransactionType', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addFxTransactionTypeToCollectionIfMissing', () => {
      it('should add a FxTransactionType to an empty array', () => {
        const fxTransactionType: IFxTransactionType = { id: 123 };
        expectedResult = service.addFxTransactionTypeToCollectionIfMissing([], fxTransactionType);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(fxTransactionType);
      });

      it('should not add a FxTransactionType to an array that contains it', () => {
        const fxTransactionType: IFxTransactionType = { id: 123 };
        const fxTransactionTypeCollection: IFxTransactionType[] = [
          {
            ...fxTransactionType,
          },
          { id: 456 },
        ];
        expectedResult = service.addFxTransactionTypeToCollectionIfMissing(fxTransactionTypeCollection, fxTransactionType);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a FxTransactionType to an array that doesn't contain it", () => {
        const fxTransactionType: IFxTransactionType = { id: 123 };
        const fxTransactionTypeCollection: IFxTransactionType[] = [{ id: 456 }];
        expectedResult = service.addFxTransactionTypeToCollectionIfMissing(fxTransactionTypeCollection, fxTransactionType);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(fxTransactionType);
      });

      it('should add only unique FxTransactionType to an array', () => {
        const fxTransactionTypeArray: IFxTransactionType[] = [{ id: 123 }, { id: 456 }, { id: 4023 }];
        const fxTransactionTypeCollection: IFxTransactionType[] = [{ id: 123 }];
        expectedResult = service.addFxTransactionTypeToCollectionIfMissing(fxTransactionTypeCollection, ...fxTransactionTypeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const fxTransactionType: IFxTransactionType = { id: 123 };
        const fxTransactionType2: IFxTransactionType = { id: 456 };
        expectedResult = service.addFxTransactionTypeToCollectionIfMissing([], fxTransactionType, fxTransactionType2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(fxTransactionType);
        expect(expectedResult).toContain(fxTransactionType2);
      });

      it('should accept null and undefined values', () => {
        const fxTransactionType: IFxTransactionType = { id: 123 };
        expectedResult = service.addFxTransactionTypeToCollectionIfMissing([], null, fxTransactionType, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(fxTransactionType);
      });

      it('should return initial array if no FxTransactionType is added', () => {
        const fxTransactionTypeCollection: IFxTransactionType[] = [{ id: 123 }];
        expectedResult = service.addFxTransactionTypeToCollectionIfMissing(fxTransactionTypeCollection, undefined, null);
        expect(expectedResult).toEqual(fxTransactionTypeCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
