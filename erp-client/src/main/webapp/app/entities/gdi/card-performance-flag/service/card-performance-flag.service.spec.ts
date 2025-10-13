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

import { CardPerformanceFlags } from 'app/entities/enumerations/card-performance-flags.model';
import { ICardPerformanceFlag, CardPerformanceFlag } from '../card-performance-flag.model';

import { CardPerformanceFlagService } from './card-performance-flag.service';

describe('CardPerformanceFlag Service', () => {
  let service: CardPerformanceFlagService;
  let httpMock: HttpTestingController;
  let elemDefault: ICardPerformanceFlag;
  let expectedResult: ICardPerformanceFlag | ICardPerformanceFlag[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CardPerformanceFlagService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      cardPerformanceFlag: CardPerformanceFlags.Y,
      cardPerformanceFlagDescription: 'AAAAAAA',
      cardPerformanceFlagDetails: 'AAAAAAA',
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

    it('should create a CardPerformanceFlag', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new CardPerformanceFlag()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CardPerformanceFlag', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          cardPerformanceFlag: 'BBBBBB',
          cardPerformanceFlagDescription: 'BBBBBB',
          cardPerformanceFlagDetails: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a CardPerformanceFlag', () => {
      const patchObject = Object.assign(
        {
          cardPerformanceFlag: 'BBBBBB',
          cardPerformanceFlagDescription: 'BBBBBB',
          cardPerformanceFlagDetails: 'BBBBBB',
        },
        new CardPerformanceFlag()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of CardPerformanceFlag', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          cardPerformanceFlag: 'BBBBBB',
          cardPerformanceFlagDescription: 'BBBBBB',
          cardPerformanceFlagDetails: 'BBBBBB',
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

    it('should delete a CardPerformanceFlag', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addCardPerformanceFlagToCollectionIfMissing', () => {
      it('should add a CardPerformanceFlag to an empty array', () => {
        const cardPerformanceFlag: ICardPerformanceFlag = { id: 123 };
        expectedResult = service.addCardPerformanceFlagToCollectionIfMissing([], cardPerformanceFlag);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(cardPerformanceFlag);
      });

      it('should not add a CardPerformanceFlag to an array that contains it', () => {
        const cardPerformanceFlag: ICardPerformanceFlag = { id: 123 };
        const cardPerformanceFlagCollection: ICardPerformanceFlag[] = [
          {
            ...cardPerformanceFlag,
          },
          { id: 456 },
        ];
        expectedResult = service.addCardPerformanceFlagToCollectionIfMissing(cardPerformanceFlagCollection, cardPerformanceFlag);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CardPerformanceFlag to an array that doesn't contain it", () => {
        const cardPerformanceFlag: ICardPerformanceFlag = { id: 123 };
        const cardPerformanceFlagCollection: ICardPerformanceFlag[] = [{ id: 456 }];
        expectedResult = service.addCardPerformanceFlagToCollectionIfMissing(cardPerformanceFlagCollection, cardPerformanceFlag);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(cardPerformanceFlag);
      });

      it('should add only unique CardPerformanceFlag to an array', () => {
        const cardPerformanceFlagArray: ICardPerformanceFlag[] = [{ id: 123 }, { id: 456 }, { id: 91406 }];
        const cardPerformanceFlagCollection: ICardPerformanceFlag[] = [{ id: 123 }];
        expectedResult = service.addCardPerformanceFlagToCollectionIfMissing(cardPerformanceFlagCollection, ...cardPerformanceFlagArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const cardPerformanceFlag: ICardPerformanceFlag = { id: 123 };
        const cardPerformanceFlag2: ICardPerformanceFlag = { id: 456 };
        expectedResult = service.addCardPerformanceFlagToCollectionIfMissing([], cardPerformanceFlag, cardPerformanceFlag2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(cardPerformanceFlag);
        expect(expectedResult).toContain(cardPerformanceFlag2);
      });

      it('should accept null and undefined values', () => {
        const cardPerformanceFlag: ICardPerformanceFlag = { id: 123 };
        expectedResult = service.addCardPerformanceFlagToCollectionIfMissing([], null, cardPerformanceFlag, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(cardPerformanceFlag);
      });

      it('should return initial array if no CardPerformanceFlag is added', () => {
        const cardPerformanceFlagCollection: ICardPerformanceFlag[] = [{ id: 123 }];
        expectedResult = service.addCardPerformanceFlagToCollectionIfMissing(cardPerformanceFlagCollection, undefined, null);
        expect(expectedResult).toEqual(cardPerformanceFlagCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
