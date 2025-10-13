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

import { ICardCharges, CardCharges } from '../card-charges.model';

import { CardChargesService } from './card-charges.service';

describe('CardCharges Service', () => {
  let service: CardChargesService;
  let httpMock: HttpTestingController;
  let elemDefault: ICardCharges;
  let expectedResult: ICardCharges | ICardCharges[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CardChargesService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      cardChargeType: 'AAAAAAA',
      cardChargeTypeName: 'AAAAAAA',
      cardChargeDetails: 'AAAAAAA',
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

    it('should create a CardCharges', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new CardCharges()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CardCharges', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          cardChargeType: 'BBBBBB',
          cardChargeTypeName: 'BBBBBB',
          cardChargeDetails: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a CardCharges', () => {
      const patchObject = Object.assign(
        {
          cardChargeType: 'BBBBBB',
          cardChargeTypeName: 'BBBBBB',
          cardChargeDetails: 'BBBBBB',
        },
        new CardCharges()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of CardCharges', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          cardChargeType: 'BBBBBB',
          cardChargeTypeName: 'BBBBBB',
          cardChargeDetails: 'BBBBBB',
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

    it('should delete a CardCharges', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addCardChargesToCollectionIfMissing', () => {
      it('should add a CardCharges to an empty array', () => {
        const cardCharges: ICardCharges = { id: 123 };
        expectedResult = service.addCardChargesToCollectionIfMissing([], cardCharges);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(cardCharges);
      });

      it('should not add a CardCharges to an array that contains it', () => {
        const cardCharges: ICardCharges = { id: 123 };
        const cardChargesCollection: ICardCharges[] = [
          {
            ...cardCharges,
          },
          { id: 456 },
        ];
        expectedResult = service.addCardChargesToCollectionIfMissing(cardChargesCollection, cardCharges);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CardCharges to an array that doesn't contain it", () => {
        const cardCharges: ICardCharges = { id: 123 };
        const cardChargesCollection: ICardCharges[] = [{ id: 456 }];
        expectedResult = service.addCardChargesToCollectionIfMissing(cardChargesCollection, cardCharges);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(cardCharges);
      });

      it('should add only unique CardCharges to an array', () => {
        const cardChargesArray: ICardCharges[] = [{ id: 123 }, { id: 456 }, { id: 59282 }];
        const cardChargesCollection: ICardCharges[] = [{ id: 123 }];
        expectedResult = service.addCardChargesToCollectionIfMissing(cardChargesCollection, ...cardChargesArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const cardCharges: ICardCharges = { id: 123 };
        const cardCharges2: ICardCharges = { id: 456 };
        expectedResult = service.addCardChargesToCollectionIfMissing([], cardCharges, cardCharges2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(cardCharges);
        expect(expectedResult).toContain(cardCharges2);
      });

      it('should accept null and undefined values', () => {
        const cardCharges: ICardCharges = { id: 123 };
        expectedResult = service.addCardChargesToCollectionIfMissing([], null, cardCharges, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(cardCharges);
      });

      it('should return initial array if no CardCharges is added', () => {
        const cardChargesCollection: ICardCharges[] = [{ id: 123 }];
        expectedResult = service.addCardChargesToCollectionIfMissing(cardChargesCollection, undefined, null);
        expect(expectedResult).toEqual(cardChargesCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
