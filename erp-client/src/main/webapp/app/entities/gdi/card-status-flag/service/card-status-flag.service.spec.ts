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

import { FlagCodes } from 'app/entities/enumerations/flag-codes.model';
import { ICardStatusFlag, CardStatusFlag } from '../card-status-flag.model';

import { CardStatusFlagService } from './card-status-flag.service';

describe('CardStatusFlag Service', () => {
  let service: CardStatusFlagService;
  let httpMock: HttpTestingController;
  let elemDefault: ICardStatusFlag;
  let expectedResult: ICardStatusFlag | ICardStatusFlag[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CardStatusFlagService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      cardStatusFlag: FlagCodes.Y,
      cardStatusFlagDescription: 'AAAAAAA',
      cardStatusFlagDetails: 'AAAAAAA',
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

    it('should create a CardStatusFlag', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new CardStatusFlag()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CardStatusFlag', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          cardStatusFlag: 'BBBBBB',
          cardStatusFlagDescription: 'BBBBBB',
          cardStatusFlagDetails: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a CardStatusFlag', () => {
      const patchObject = Object.assign({}, new CardStatusFlag());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of CardStatusFlag', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          cardStatusFlag: 'BBBBBB',
          cardStatusFlagDescription: 'BBBBBB',
          cardStatusFlagDetails: 'BBBBBB',
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

    it('should delete a CardStatusFlag', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addCardStatusFlagToCollectionIfMissing', () => {
      it('should add a CardStatusFlag to an empty array', () => {
        const cardStatusFlag: ICardStatusFlag = { id: 123 };
        expectedResult = service.addCardStatusFlagToCollectionIfMissing([], cardStatusFlag);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(cardStatusFlag);
      });

      it('should not add a CardStatusFlag to an array that contains it', () => {
        const cardStatusFlag: ICardStatusFlag = { id: 123 };
        const cardStatusFlagCollection: ICardStatusFlag[] = [
          {
            ...cardStatusFlag,
          },
          { id: 456 },
        ];
        expectedResult = service.addCardStatusFlagToCollectionIfMissing(cardStatusFlagCollection, cardStatusFlag);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CardStatusFlag to an array that doesn't contain it", () => {
        const cardStatusFlag: ICardStatusFlag = { id: 123 };
        const cardStatusFlagCollection: ICardStatusFlag[] = [{ id: 456 }];
        expectedResult = service.addCardStatusFlagToCollectionIfMissing(cardStatusFlagCollection, cardStatusFlag);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(cardStatusFlag);
      });

      it('should add only unique CardStatusFlag to an array', () => {
        const cardStatusFlagArray: ICardStatusFlag[] = [{ id: 123 }, { id: 456 }, { id: 28998 }];
        const cardStatusFlagCollection: ICardStatusFlag[] = [{ id: 123 }];
        expectedResult = service.addCardStatusFlagToCollectionIfMissing(cardStatusFlagCollection, ...cardStatusFlagArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const cardStatusFlag: ICardStatusFlag = { id: 123 };
        const cardStatusFlag2: ICardStatusFlag = { id: 456 };
        expectedResult = service.addCardStatusFlagToCollectionIfMissing([], cardStatusFlag, cardStatusFlag2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(cardStatusFlag);
        expect(expectedResult).toContain(cardStatusFlag2);
      });

      it('should accept null and undefined values', () => {
        const cardStatusFlag: ICardStatusFlag = { id: 123 };
        expectedResult = service.addCardStatusFlagToCollectionIfMissing([], null, cardStatusFlag, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(cardStatusFlag);
      });

      it('should return initial array if no CardStatusFlag is added', () => {
        const cardStatusFlagCollection: ICardStatusFlag[] = [{ id: 123 }];
        expectedResult = service.addCardStatusFlagToCollectionIfMissing(cardStatusFlagCollection, undefined, null);
        expect(expectedResult).toEqual(cardStatusFlagCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
