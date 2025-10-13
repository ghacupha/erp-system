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

import { CardCategoryFlag } from 'app/entities/enumerations/card-category-flag.model';
import { ICardCategoryType, CardCategoryType } from '../card-category-type.model';

import { CardCategoryTypeService } from './card-category-type.service';

describe('CardCategoryType Service', () => {
  let service: CardCategoryTypeService;
  let httpMock: HttpTestingController;
  let elemDefault: ICardCategoryType;
  let expectedResult: ICardCategoryType | ICardCategoryType[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CardCategoryTypeService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      cardCategoryFlag: CardCategoryFlag.L,
      cardCategoryDescription: 'AAAAAAA',
      cardCategoryDetails: 'AAAAAAA',
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

    it('should create a CardCategoryType', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new CardCategoryType()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CardCategoryType', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          cardCategoryFlag: 'BBBBBB',
          cardCategoryDescription: 'BBBBBB',
          cardCategoryDetails: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a CardCategoryType', () => {
      const patchObject = Object.assign(
        {
          cardCategoryFlag: 'BBBBBB',
          cardCategoryDetails: 'BBBBBB',
        },
        new CardCategoryType()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of CardCategoryType', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          cardCategoryFlag: 'BBBBBB',
          cardCategoryDescription: 'BBBBBB',
          cardCategoryDetails: 'BBBBBB',
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

    it('should delete a CardCategoryType', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addCardCategoryTypeToCollectionIfMissing', () => {
      it('should add a CardCategoryType to an empty array', () => {
        const cardCategoryType: ICardCategoryType = { id: 123 };
        expectedResult = service.addCardCategoryTypeToCollectionIfMissing([], cardCategoryType);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(cardCategoryType);
      });

      it('should not add a CardCategoryType to an array that contains it', () => {
        const cardCategoryType: ICardCategoryType = { id: 123 };
        const cardCategoryTypeCollection: ICardCategoryType[] = [
          {
            ...cardCategoryType,
          },
          { id: 456 },
        ];
        expectedResult = service.addCardCategoryTypeToCollectionIfMissing(cardCategoryTypeCollection, cardCategoryType);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CardCategoryType to an array that doesn't contain it", () => {
        const cardCategoryType: ICardCategoryType = { id: 123 };
        const cardCategoryTypeCollection: ICardCategoryType[] = [{ id: 456 }];
        expectedResult = service.addCardCategoryTypeToCollectionIfMissing(cardCategoryTypeCollection, cardCategoryType);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(cardCategoryType);
      });

      it('should add only unique CardCategoryType to an array', () => {
        const cardCategoryTypeArray: ICardCategoryType[] = [{ id: 123 }, { id: 456 }, { id: 84970 }];
        const cardCategoryTypeCollection: ICardCategoryType[] = [{ id: 123 }];
        expectedResult = service.addCardCategoryTypeToCollectionIfMissing(cardCategoryTypeCollection, ...cardCategoryTypeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const cardCategoryType: ICardCategoryType = { id: 123 };
        const cardCategoryType2: ICardCategoryType = { id: 456 };
        expectedResult = service.addCardCategoryTypeToCollectionIfMissing([], cardCategoryType, cardCategoryType2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(cardCategoryType);
        expect(expectedResult).toContain(cardCategoryType2);
      });

      it('should accept null and undefined values', () => {
        const cardCategoryType: ICardCategoryType = { id: 123 };
        expectedResult = service.addCardCategoryTypeToCollectionIfMissing([], null, cardCategoryType, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(cardCategoryType);
      });

      it('should return initial array if no CardCategoryType is added', () => {
        const cardCategoryTypeCollection: ICardCategoryType[] = [{ id: 123 }];
        expectedResult = service.addCardCategoryTypeToCollectionIfMissing(cardCategoryTypeCollection, undefined, null);
        expect(expectedResult).toEqual(cardCategoryTypeCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
