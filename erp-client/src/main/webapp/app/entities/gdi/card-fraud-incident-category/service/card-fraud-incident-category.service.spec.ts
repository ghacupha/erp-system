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

import { ICardFraudIncidentCategory, CardFraudIncidentCategory } from '../card-fraud-incident-category.model';

import { CardFraudIncidentCategoryService } from './card-fraud-incident-category.service';

describe('CardFraudIncidentCategory Service', () => {
  let service: CardFraudIncidentCategoryService;
  let httpMock: HttpTestingController;
  let elemDefault: ICardFraudIncidentCategory;
  let expectedResult: ICardFraudIncidentCategory | ICardFraudIncidentCategory[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CardFraudIncidentCategoryService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      cardFraudCategoryTypeCode: 'AAAAAAA',
      cardFraudCategoryType: 'AAAAAAA',
      cardFraudCategoryTypeDescription: 'AAAAAAA',
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

    it('should create a CardFraudIncidentCategory', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new CardFraudIncidentCategory()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CardFraudIncidentCategory', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          cardFraudCategoryTypeCode: 'BBBBBB',
          cardFraudCategoryType: 'BBBBBB',
          cardFraudCategoryTypeDescription: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a CardFraudIncidentCategory', () => {
      const patchObject = Object.assign(
        {
          cardFraudCategoryTypeCode: 'BBBBBB',
          cardFraudCategoryType: 'BBBBBB',
        },
        new CardFraudIncidentCategory()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of CardFraudIncidentCategory', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          cardFraudCategoryTypeCode: 'BBBBBB',
          cardFraudCategoryType: 'BBBBBB',
          cardFraudCategoryTypeDescription: 'BBBBBB',
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

    it('should delete a CardFraudIncidentCategory', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addCardFraudIncidentCategoryToCollectionIfMissing', () => {
      it('should add a CardFraudIncidentCategory to an empty array', () => {
        const cardFraudIncidentCategory: ICardFraudIncidentCategory = { id: 123 };
        expectedResult = service.addCardFraudIncidentCategoryToCollectionIfMissing([], cardFraudIncidentCategory);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(cardFraudIncidentCategory);
      });

      it('should not add a CardFraudIncidentCategory to an array that contains it', () => {
        const cardFraudIncidentCategory: ICardFraudIncidentCategory = { id: 123 };
        const cardFraudIncidentCategoryCollection: ICardFraudIncidentCategory[] = [
          {
            ...cardFraudIncidentCategory,
          },
          { id: 456 },
        ];
        expectedResult = service.addCardFraudIncidentCategoryToCollectionIfMissing(
          cardFraudIncidentCategoryCollection,
          cardFraudIncidentCategory
        );
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CardFraudIncidentCategory to an array that doesn't contain it", () => {
        const cardFraudIncidentCategory: ICardFraudIncidentCategory = { id: 123 };
        const cardFraudIncidentCategoryCollection: ICardFraudIncidentCategory[] = [{ id: 456 }];
        expectedResult = service.addCardFraudIncidentCategoryToCollectionIfMissing(
          cardFraudIncidentCategoryCollection,
          cardFraudIncidentCategory
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(cardFraudIncidentCategory);
      });

      it('should add only unique CardFraudIncidentCategory to an array', () => {
        const cardFraudIncidentCategoryArray: ICardFraudIncidentCategory[] = [{ id: 123 }, { id: 456 }, { id: 1061 }];
        const cardFraudIncidentCategoryCollection: ICardFraudIncidentCategory[] = [{ id: 123 }];
        expectedResult = service.addCardFraudIncidentCategoryToCollectionIfMissing(
          cardFraudIncidentCategoryCollection,
          ...cardFraudIncidentCategoryArray
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const cardFraudIncidentCategory: ICardFraudIncidentCategory = { id: 123 };
        const cardFraudIncidentCategory2: ICardFraudIncidentCategory = { id: 456 };
        expectedResult = service.addCardFraudIncidentCategoryToCollectionIfMissing(
          [],
          cardFraudIncidentCategory,
          cardFraudIncidentCategory2
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(cardFraudIncidentCategory);
        expect(expectedResult).toContain(cardFraudIncidentCategory2);
      });

      it('should accept null and undefined values', () => {
        const cardFraudIncidentCategory: ICardFraudIncidentCategory = { id: 123 };
        expectedResult = service.addCardFraudIncidentCategoryToCollectionIfMissing([], null, cardFraudIncidentCategory, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(cardFraudIncidentCategory);
      });

      it('should return initial array if no CardFraudIncidentCategory is added', () => {
        const cardFraudIncidentCategoryCollection: ICardFraudIncidentCategory[] = [{ id: 123 }];
        expectedResult = service.addCardFraudIncidentCategoryToCollectionIfMissing(cardFraudIncidentCategoryCollection, undefined, null);
        expect(expectedResult).toEqual(cardFraudIncidentCategoryCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
