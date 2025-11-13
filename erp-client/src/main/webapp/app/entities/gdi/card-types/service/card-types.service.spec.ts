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

import { ICardTypes, CardTypes } from '../card-types.model';

import { CardTypesService } from './card-types.service';

describe('CardTypes Service', () => {
  let service: CardTypesService;
  let httpMock: HttpTestingController;
  let elemDefault: ICardTypes;
  let expectedResult: ICardTypes | ICardTypes[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CardTypesService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      cardTypeCode: 'AAAAAAA',
      cardType: 'AAAAAAA',
      cardTypeDetails: 'AAAAAAA',
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

    it('should create a CardTypes', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new CardTypes()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CardTypes', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          cardTypeCode: 'BBBBBB',
          cardType: 'BBBBBB',
          cardTypeDetails: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a CardTypes', () => {
      const patchObject = Object.assign(
        {
          cardType: 'BBBBBB',
        },
        new CardTypes()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of CardTypes', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          cardTypeCode: 'BBBBBB',
          cardType: 'BBBBBB',
          cardTypeDetails: 'BBBBBB',
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

    it('should delete a CardTypes', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addCardTypesToCollectionIfMissing', () => {
      it('should add a CardTypes to an empty array', () => {
        const cardTypes: ICardTypes = { id: 123 };
        expectedResult = service.addCardTypesToCollectionIfMissing([], cardTypes);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(cardTypes);
      });

      it('should not add a CardTypes to an array that contains it', () => {
        const cardTypes: ICardTypes = { id: 123 };
        const cardTypesCollection: ICardTypes[] = [
          {
            ...cardTypes,
          },
          { id: 456 },
        ];
        expectedResult = service.addCardTypesToCollectionIfMissing(cardTypesCollection, cardTypes);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CardTypes to an array that doesn't contain it", () => {
        const cardTypes: ICardTypes = { id: 123 };
        const cardTypesCollection: ICardTypes[] = [{ id: 456 }];
        expectedResult = service.addCardTypesToCollectionIfMissing(cardTypesCollection, cardTypes);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(cardTypes);
      });

      it('should add only unique CardTypes to an array', () => {
        const cardTypesArray: ICardTypes[] = [{ id: 123 }, { id: 456 }, { id: 20478 }];
        const cardTypesCollection: ICardTypes[] = [{ id: 123 }];
        expectedResult = service.addCardTypesToCollectionIfMissing(cardTypesCollection, ...cardTypesArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const cardTypes: ICardTypes = { id: 123 };
        const cardTypes2: ICardTypes = { id: 456 };
        expectedResult = service.addCardTypesToCollectionIfMissing([], cardTypes, cardTypes2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(cardTypes);
        expect(expectedResult).toContain(cardTypes2);
      });

      it('should accept null and undefined values', () => {
        const cardTypes: ICardTypes = { id: 123 };
        expectedResult = service.addCardTypesToCollectionIfMissing([], null, cardTypes, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(cardTypes);
      });

      it('should return initial array if no CardTypes is added', () => {
        const cardTypesCollection: ICardTypes[] = [{ id: 123 }];
        expectedResult = service.addCardTypesToCollectionIfMissing(cardTypesCollection, undefined, null);
        expect(expectedResult).toEqual(cardTypesCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
