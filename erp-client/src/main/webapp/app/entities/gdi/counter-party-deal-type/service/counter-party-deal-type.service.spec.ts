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

import { ICounterPartyDealType, CounterPartyDealType } from '../counter-party-deal-type.model';

import { CounterPartyDealTypeService } from './counter-party-deal-type.service';

describe('CounterPartyDealType Service', () => {
  let service: CounterPartyDealTypeService;
  let httpMock: HttpTestingController;
  let elemDefault: ICounterPartyDealType;
  let expectedResult: ICounterPartyDealType | ICounterPartyDealType[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CounterPartyDealTypeService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      counterpartyDealCode: 'AAAAAAA',
      counterpartyDealTypeDetails: 'AAAAAAA',
      counterpartyDealTypeDescription: 'AAAAAAA',
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

    it('should create a CounterPartyDealType', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new CounterPartyDealType()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CounterPartyDealType', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          counterpartyDealCode: 'BBBBBB',
          counterpartyDealTypeDetails: 'BBBBBB',
          counterpartyDealTypeDescription: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a CounterPartyDealType', () => {
      const patchObject = Object.assign(
        {
          counterpartyDealCode: 'BBBBBB',
          counterpartyDealTypeDetails: 'BBBBBB',
          counterpartyDealTypeDescription: 'BBBBBB',
        },
        new CounterPartyDealType()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of CounterPartyDealType', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          counterpartyDealCode: 'BBBBBB',
          counterpartyDealTypeDetails: 'BBBBBB',
          counterpartyDealTypeDescription: 'BBBBBB',
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

    it('should delete a CounterPartyDealType', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addCounterPartyDealTypeToCollectionIfMissing', () => {
      it('should add a CounterPartyDealType to an empty array', () => {
        const counterPartyDealType: ICounterPartyDealType = { id: 123 };
        expectedResult = service.addCounterPartyDealTypeToCollectionIfMissing([], counterPartyDealType);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(counterPartyDealType);
      });

      it('should not add a CounterPartyDealType to an array that contains it', () => {
        const counterPartyDealType: ICounterPartyDealType = { id: 123 };
        const counterPartyDealTypeCollection: ICounterPartyDealType[] = [
          {
            ...counterPartyDealType,
          },
          { id: 456 },
        ];
        expectedResult = service.addCounterPartyDealTypeToCollectionIfMissing(counterPartyDealTypeCollection, counterPartyDealType);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CounterPartyDealType to an array that doesn't contain it", () => {
        const counterPartyDealType: ICounterPartyDealType = { id: 123 };
        const counterPartyDealTypeCollection: ICounterPartyDealType[] = [{ id: 456 }];
        expectedResult = service.addCounterPartyDealTypeToCollectionIfMissing(counterPartyDealTypeCollection, counterPartyDealType);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(counterPartyDealType);
      });

      it('should add only unique CounterPartyDealType to an array', () => {
        const counterPartyDealTypeArray: ICounterPartyDealType[] = [{ id: 123 }, { id: 456 }, { id: 75025 }];
        const counterPartyDealTypeCollection: ICounterPartyDealType[] = [{ id: 123 }];
        expectedResult = service.addCounterPartyDealTypeToCollectionIfMissing(counterPartyDealTypeCollection, ...counterPartyDealTypeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const counterPartyDealType: ICounterPartyDealType = { id: 123 };
        const counterPartyDealType2: ICounterPartyDealType = { id: 456 };
        expectedResult = service.addCounterPartyDealTypeToCollectionIfMissing([], counterPartyDealType, counterPartyDealType2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(counterPartyDealType);
        expect(expectedResult).toContain(counterPartyDealType2);
      });

      it('should accept null and undefined values', () => {
        const counterPartyDealType: ICounterPartyDealType = { id: 123 };
        expectedResult = service.addCounterPartyDealTypeToCollectionIfMissing([], null, counterPartyDealType, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(counterPartyDealType);
      });

      it('should return initial array if no CounterPartyDealType is added', () => {
        const counterPartyDealTypeCollection: ICounterPartyDealType[] = [{ id: 123 }];
        expectedResult = service.addCounterPartyDealTypeToCollectionIfMissing(counterPartyDealTypeCollection, undefined, null);
        expect(expectedResult).toEqual(counterPartyDealTypeCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
