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

import { ICardBrandType, CardBrandType } from '../card-brand-type.model';

import { CardBrandTypeService } from './card-brand-type.service';

describe('CardBrandType Service', () => {
  let service: CardBrandTypeService;
  let httpMock: HttpTestingController;
  let elemDefault: ICardBrandType;
  let expectedResult: ICardBrandType | ICardBrandType[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CardBrandTypeService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      cardBrandTypeCode: 'AAAAAAA',
      cardBrandType: 'AAAAAAA',
      cardBrandTypeDetails: 'AAAAAAA',
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

    it('should create a CardBrandType', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new CardBrandType()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CardBrandType', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          cardBrandTypeCode: 'BBBBBB',
          cardBrandType: 'BBBBBB',
          cardBrandTypeDetails: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a CardBrandType', () => {
      const patchObject = Object.assign(
        {
          cardBrandTypeCode: 'BBBBBB',
          cardBrandTypeDetails: 'BBBBBB',
        },
        new CardBrandType()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of CardBrandType', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          cardBrandTypeCode: 'BBBBBB',
          cardBrandType: 'BBBBBB',
          cardBrandTypeDetails: 'BBBBBB',
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

    it('should delete a CardBrandType', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addCardBrandTypeToCollectionIfMissing', () => {
      it('should add a CardBrandType to an empty array', () => {
        const cardBrandType: ICardBrandType = { id: 123 };
        expectedResult = service.addCardBrandTypeToCollectionIfMissing([], cardBrandType);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(cardBrandType);
      });

      it('should not add a CardBrandType to an array that contains it', () => {
        const cardBrandType: ICardBrandType = { id: 123 };
        const cardBrandTypeCollection: ICardBrandType[] = [
          {
            ...cardBrandType,
          },
          { id: 456 },
        ];
        expectedResult = service.addCardBrandTypeToCollectionIfMissing(cardBrandTypeCollection, cardBrandType);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CardBrandType to an array that doesn't contain it", () => {
        const cardBrandType: ICardBrandType = { id: 123 };
        const cardBrandTypeCollection: ICardBrandType[] = [{ id: 456 }];
        expectedResult = service.addCardBrandTypeToCollectionIfMissing(cardBrandTypeCollection, cardBrandType);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(cardBrandType);
      });

      it('should add only unique CardBrandType to an array', () => {
        const cardBrandTypeArray: ICardBrandType[] = [{ id: 123 }, { id: 456 }, { id: 76695 }];
        const cardBrandTypeCollection: ICardBrandType[] = [{ id: 123 }];
        expectedResult = service.addCardBrandTypeToCollectionIfMissing(cardBrandTypeCollection, ...cardBrandTypeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const cardBrandType: ICardBrandType = { id: 123 };
        const cardBrandType2: ICardBrandType = { id: 456 };
        expectedResult = service.addCardBrandTypeToCollectionIfMissing([], cardBrandType, cardBrandType2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(cardBrandType);
        expect(expectedResult).toContain(cardBrandType2);
      });

      it('should accept null and undefined values', () => {
        const cardBrandType: ICardBrandType = { id: 123 };
        expectedResult = service.addCardBrandTypeToCollectionIfMissing([], null, cardBrandType, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(cardBrandType);
      });

      it('should return initial array if no CardBrandType is added', () => {
        const cardBrandTypeCollection: ICardBrandType[] = [{ id: 123 }];
        expectedResult = service.addCardBrandTypeToCollectionIfMissing(cardBrandTypeCollection, undefined, null);
        expect(expectedResult).toEqual(cardBrandTypeCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
