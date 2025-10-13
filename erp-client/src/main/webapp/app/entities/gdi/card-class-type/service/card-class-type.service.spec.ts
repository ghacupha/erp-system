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

import { ICardClassType, CardClassType } from '../card-class-type.model';

import { CardClassTypeService } from './card-class-type.service';

describe('CardClassType Service', () => {
  let service: CardClassTypeService;
  let httpMock: HttpTestingController;
  let elemDefault: ICardClassType;
  let expectedResult: ICardClassType | ICardClassType[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CardClassTypeService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      cardClassTypeCode: 'AAAAAAA',
      cardClassType: 'AAAAAAA',
      cardClassDetails: 'AAAAAAA',
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

    it('should create a CardClassType', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new CardClassType()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CardClassType', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          cardClassTypeCode: 'BBBBBB',
          cardClassType: 'BBBBBB',
          cardClassDetails: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a CardClassType', () => {
      const patchObject = Object.assign(
        {
          cardClassTypeCode: 'BBBBBB',
          cardClassType: 'BBBBBB',
        },
        new CardClassType()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of CardClassType', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          cardClassTypeCode: 'BBBBBB',
          cardClassType: 'BBBBBB',
          cardClassDetails: 'BBBBBB',
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

    it('should delete a CardClassType', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addCardClassTypeToCollectionIfMissing', () => {
      it('should add a CardClassType to an empty array', () => {
        const cardClassType: ICardClassType = { id: 123 };
        expectedResult = service.addCardClassTypeToCollectionIfMissing([], cardClassType);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(cardClassType);
      });

      it('should not add a CardClassType to an array that contains it', () => {
        const cardClassType: ICardClassType = { id: 123 };
        const cardClassTypeCollection: ICardClassType[] = [
          {
            ...cardClassType,
          },
          { id: 456 },
        ];
        expectedResult = service.addCardClassTypeToCollectionIfMissing(cardClassTypeCollection, cardClassType);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CardClassType to an array that doesn't contain it", () => {
        const cardClassType: ICardClassType = { id: 123 };
        const cardClassTypeCollection: ICardClassType[] = [{ id: 456 }];
        expectedResult = service.addCardClassTypeToCollectionIfMissing(cardClassTypeCollection, cardClassType);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(cardClassType);
      });

      it('should add only unique CardClassType to an array', () => {
        const cardClassTypeArray: ICardClassType[] = [{ id: 123 }, { id: 456 }, { id: 34518 }];
        const cardClassTypeCollection: ICardClassType[] = [{ id: 123 }];
        expectedResult = service.addCardClassTypeToCollectionIfMissing(cardClassTypeCollection, ...cardClassTypeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const cardClassType: ICardClassType = { id: 123 };
        const cardClassType2: ICardClassType = { id: 456 };
        expectedResult = service.addCardClassTypeToCollectionIfMissing([], cardClassType, cardClassType2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(cardClassType);
        expect(expectedResult).toContain(cardClassType2);
      });

      it('should accept null and undefined values', () => {
        const cardClassType: ICardClassType = { id: 123 };
        expectedResult = service.addCardClassTypeToCollectionIfMissing([], null, cardClassType, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(cardClassType);
      });

      it('should return initial array if no CardClassType is added', () => {
        const cardClassTypeCollection: ICardClassType[] = [{ id: 123 }];
        expectedResult = service.addCardClassTypeToCollectionIfMissing(cardClassTypeCollection, undefined, null);
        expect(expectedResult).toEqual(cardClassTypeCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
