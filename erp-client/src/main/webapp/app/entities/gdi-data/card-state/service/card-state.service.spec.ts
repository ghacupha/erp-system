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

import { CardStateFlagTypes } from 'app/entities/enumerations/card-state-flag-types.model';
import { ICardState, CardState } from '../card-state.model';

import { CardStateService } from './card-state.service';

describe('CardState Service', () => {
  let service: CardStateService;
  let httpMock: HttpTestingController;
  let elemDefault: ICardState;
  let expectedResult: ICardState | ICardState[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CardStateService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      cardStateFlag: CardStateFlagTypes.P,
      cardStateFlagDetails: 'AAAAAAA',
      cardStateFlagDescription: 'AAAAAAA',
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

    it('should create a CardState', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new CardState()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CardState', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          cardStateFlag: 'BBBBBB',
          cardStateFlagDetails: 'BBBBBB',
          cardStateFlagDescription: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a CardState', () => {
      const patchObject = Object.assign(
        {
          cardStateFlagDetails: 'BBBBBB',
          cardStateFlagDescription: 'BBBBBB',
        },
        new CardState()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of CardState', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          cardStateFlag: 'BBBBBB',
          cardStateFlagDetails: 'BBBBBB',
          cardStateFlagDescription: 'BBBBBB',
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

    it('should delete a CardState', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addCardStateToCollectionIfMissing', () => {
      it('should add a CardState to an empty array', () => {
        const cardState: ICardState = { id: 123 };
        expectedResult = service.addCardStateToCollectionIfMissing([], cardState);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(cardState);
      });

      it('should not add a CardState to an array that contains it', () => {
        const cardState: ICardState = { id: 123 };
        const cardStateCollection: ICardState[] = [
          {
            ...cardState,
          },
          { id: 456 },
        ];
        expectedResult = service.addCardStateToCollectionIfMissing(cardStateCollection, cardState);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CardState to an array that doesn't contain it", () => {
        const cardState: ICardState = { id: 123 };
        const cardStateCollection: ICardState[] = [{ id: 456 }];
        expectedResult = service.addCardStateToCollectionIfMissing(cardStateCollection, cardState);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(cardState);
      });

      it('should add only unique CardState to an array', () => {
        const cardStateArray: ICardState[] = [{ id: 123 }, { id: 456 }, { id: 34612 }];
        const cardStateCollection: ICardState[] = [{ id: 123 }];
        expectedResult = service.addCardStateToCollectionIfMissing(cardStateCollection, ...cardStateArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const cardState: ICardState = { id: 123 };
        const cardState2: ICardState = { id: 456 };
        expectedResult = service.addCardStateToCollectionIfMissing([], cardState, cardState2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(cardState);
        expect(expectedResult).toContain(cardState2);
      });

      it('should accept null and undefined values', () => {
        const cardState: ICardState = { id: 123 };
        expectedResult = service.addCardStateToCollectionIfMissing([], null, cardState, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(cardState);
      });

      it('should return initial array if no CardState is added', () => {
        const cardStateCollection: ICardState[] = [{ id: 123 }];
        expectedResult = service.addCardStateToCollectionIfMissing(cardStateCollection, undefined, null);
        expect(expectedResult).toEqual(cardStateCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
