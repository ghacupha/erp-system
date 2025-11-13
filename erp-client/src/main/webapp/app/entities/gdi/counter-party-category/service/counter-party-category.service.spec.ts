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

import { CounterpartyCategory } from 'app/entities/enumerations/counterparty-category.model';
import { ICounterPartyCategory, CounterPartyCategory } from '../counter-party-category.model';

import { CounterPartyCategoryService } from './counter-party-category.service';

describe('CounterPartyCategory Service', () => {
  let service: CounterPartyCategoryService;
  let httpMock: HttpTestingController;
  let elemDefault: ICounterPartyCategory;
  let expectedResult: ICounterPartyCategory | ICounterPartyCategory[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CounterPartyCategoryService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      counterpartyCategoryCode: 'AAAAAAA',
      counterpartyCategoryCodeDetails: CounterpartyCategory.LOCAL,
      counterpartyCategoryDescription: 'AAAAAAA',
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

    it('should create a CounterPartyCategory', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new CounterPartyCategory()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CounterPartyCategory', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          counterpartyCategoryCode: 'BBBBBB',
          counterpartyCategoryCodeDetails: 'BBBBBB',
          counterpartyCategoryDescription: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a CounterPartyCategory', () => {
      const patchObject = Object.assign(
        {
          counterpartyCategoryCodeDetails: 'BBBBBB',
          counterpartyCategoryDescription: 'BBBBBB',
        },
        new CounterPartyCategory()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of CounterPartyCategory', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          counterpartyCategoryCode: 'BBBBBB',
          counterpartyCategoryCodeDetails: 'BBBBBB',
          counterpartyCategoryDescription: 'BBBBBB',
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

    it('should delete a CounterPartyCategory', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addCounterPartyCategoryToCollectionIfMissing', () => {
      it('should add a CounterPartyCategory to an empty array', () => {
        const counterPartyCategory: ICounterPartyCategory = { id: 123 };
        expectedResult = service.addCounterPartyCategoryToCollectionIfMissing([], counterPartyCategory);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(counterPartyCategory);
      });

      it('should not add a CounterPartyCategory to an array that contains it', () => {
        const counterPartyCategory: ICounterPartyCategory = { id: 123 };
        const counterPartyCategoryCollection: ICounterPartyCategory[] = [
          {
            ...counterPartyCategory,
          },
          { id: 456 },
        ];
        expectedResult = service.addCounterPartyCategoryToCollectionIfMissing(counterPartyCategoryCollection, counterPartyCategory);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CounterPartyCategory to an array that doesn't contain it", () => {
        const counterPartyCategory: ICounterPartyCategory = { id: 123 };
        const counterPartyCategoryCollection: ICounterPartyCategory[] = [{ id: 456 }];
        expectedResult = service.addCounterPartyCategoryToCollectionIfMissing(counterPartyCategoryCollection, counterPartyCategory);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(counterPartyCategory);
      });

      it('should add only unique CounterPartyCategory to an array', () => {
        const counterPartyCategoryArray: ICounterPartyCategory[] = [{ id: 123 }, { id: 456 }, { id: 84832 }];
        const counterPartyCategoryCollection: ICounterPartyCategory[] = [{ id: 123 }];
        expectedResult = service.addCounterPartyCategoryToCollectionIfMissing(counterPartyCategoryCollection, ...counterPartyCategoryArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const counterPartyCategory: ICounterPartyCategory = { id: 123 };
        const counterPartyCategory2: ICounterPartyCategory = { id: 456 };
        expectedResult = service.addCounterPartyCategoryToCollectionIfMissing([], counterPartyCategory, counterPartyCategory2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(counterPartyCategory);
        expect(expectedResult).toContain(counterPartyCategory2);
      });

      it('should accept null and undefined values', () => {
        const counterPartyCategory: ICounterPartyCategory = { id: 123 };
        expectedResult = service.addCounterPartyCategoryToCollectionIfMissing([], null, counterPartyCategory, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(counterPartyCategory);
      });

      it('should return initial array if no CounterPartyCategory is added', () => {
        const counterPartyCategoryCollection: ICounterPartyCategory[] = [{ id: 123 }];
        expectedResult = service.addCounterPartyCategoryToCollectionIfMissing(counterPartyCategoryCollection, undefined, null);
        expect(expectedResult).toEqual(counterPartyCategoryCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
