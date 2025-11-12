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

import { ICounterpartyType, CounterpartyType } from '../counterparty-type.model';

import { CounterpartyTypeService } from './counterparty-type.service';

describe('CounterpartyType Service', () => {
  let service: CounterpartyTypeService;
  let httpMock: HttpTestingController;
  let elemDefault: ICounterpartyType;
  let expectedResult: ICounterpartyType | ICounterpartyType[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CounterpartyTypeService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      counterpartyTypeCode: 'AAAAAAA',
      counterPartyType: 'AAAAAAA',
      counterpartyTypeDescription: 'AAAAAAA',
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

    it('should create a CounterpartyType', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new CounterpartyType()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CounterpartyType', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          counterpartyTypeCode: 'BBBBBB',
          counterPartyType: 'BBBBBB',
          counterpartyTypeDescription: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a CounterpartyType', () => {
      const patchObject = Object.assign({}, new CounterpartyType());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of CounterpartyType', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          counterpartyTypeCode: 'BBBBBB',
          counterPartyType: 'BBBBBB',
          counterpartyTypeDescription: 'BBBBBB',
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

    it('should delete a CounterpartyType', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addCounterpartyTypeToCollectionIfMissing', () => {
      it('should add a CounterpartyType to an empty array', () => {
        const counterpartyType: ICounterpartyType = { id: 123 };
        expectedResult = service.addCounterpartyTypeToCollectionIfMissing([], counterpartyType);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(counterpartyType);
      });

      it('should not add a CounterpartyType to an array that contains it', () => {
        const counterpartyType: ICounterpartyType = { id: 123 };
        const counterpartyTypeCollection: ICounterpartyType[] = [
          {
            ...counterpartyType,
          },
          { id: 456 },
        ];
        expectedResult = service.addCounterpartyTypeToCollectionIfMissing(counterpartyTypeCollection, counterpartyType);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CounterpartyType to an array that doesn't contain it", () => {
        const counterpartyType: ICounterpartyType = { id: 123 };
        const counterpartyTypeCollection: ICounterpartyType[] = [{ id: 456 }];
        expectedResult = service.addCounterpartyTypeToCollectionIfMissing(counterpartyTypeCollection, counterpartyType);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(counterpartyType);
      });

      it('should add only unique CounterpartyType to an array', () => {
        const counterpartyTypeArray: ICounterpartyType[] = [{ id: 123 }, { id: 456 }, { id: 6987 }];
        const counterpartyTypeCollection: ICounterpartyType[] = [{ id: 123 }];
        expectedResult = service.addCounterpartyTypeToCollectionIfMissing(counterpartyTypeCollection, ...counterpartyTypeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const counterpartyType: ICounterpartyType = { id: 123 };
        const counterpartyType2: ICounterpartyType = { id: 456 };
        expectedResult = service.addCounterpartyTypeToCollectionIfMissing([], counterpartyType, counterpartyType2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(counterpartyType);
        expect(expectedResult).toContain(counterpartyType2);
      });

      it('should accept null and undefined values', () => {
        const counterpartyType: ICounterpartyType = { id: 123 };
        expectedResult = service.addCounterpartyTypeToCollectionIfMissing([], null, counterpartyType, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(counterpartyType);
      });

      it('should return initial array if no CounterpartyType is added', () => {
        const counterpartyTypeCollection: ICounterpartyType[] = [{ id: 123 }];
        expectedResult = service.addCounterpartyTypeToCollectionIfMissing(counterpartyTypeCollection, undefined, null);
        expect(expectedResult).toEqual(counterpartyTypeCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
