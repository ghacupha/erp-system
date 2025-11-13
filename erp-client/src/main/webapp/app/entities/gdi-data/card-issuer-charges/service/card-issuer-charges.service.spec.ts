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
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { ICardIssuerCharges, CardIssuerCharges } from '../card-issuer-charges.model';

import { CardIssuerChargesService } from './card-issuer-charges.service';

describe('CardIssuerCharges Service', () => {
  let service: CardIssuerChargesService;
  let httpMock: HttpTestingController;
  let elemDefault: ICardIssuerCharges;
  let expectedResult: ICardIssuerCharges | ICardIssuerCharges[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CardIssuerChargesService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      reportingDate: currentDate,
      cardFeeChargeInLCY: 0,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          reportingDate: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a CardIssuerCharges', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          reportingDate: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          reportingDate: currentDate,
        },
        returnedFromService
      );

      service.create(new CardIssuerCharges()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CardIssuerCharges', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          reportingDate: currentDate.format(DATE_FORMAT),
          cardFeeChargeInLCY: 1,
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          reportingDate: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a CardIssuerCharges', () => {
      const patchObject = Object.assign(
        {
          reportingDate: currentDate.format(DATE_FORMAT),
          cardFeeChargeInLCY: 1,
        },
        new CardIssuerCharges()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          reportingDate: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of CardIssuerCharges', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          reportingDate: currentDate.format(DATE_FORMAT),
          cardFeeChargeInLCY: 1,
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          reportingDate: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a CardIssuerCharges', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addCardIssuerChargesToCollectionIfMissing', () => {
      it('should add a CardIssuerCharges to an empty array', () => {
        const cardIssuerCharges: ICardIssuerCharges = { id: 123 };
        expectedResult = service.addCardIssuerChargesToCollectionIfMissing([], cardIssuerCharges);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(cardIssuerCharges);
      });

      it('should not add a CardIssuerCharges to an array that contains it', () => {
        const cardIssuerCharges: ICardIssuerCharges = { id: 123 };
        const cardIssuerChargesCollection: ICardIssuerCharges[] = [
          {
            ...cardIssuerCharges,
          },
          { id: 456 },
        ];
        expectedResult = service.addCardIssuerChargesToCollectionIfMissing(cardIssuerChargesCollection, cardIssuerCharges);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CardIssuerCharges to an array that doesn't contain it", () => {
        const cardIssuerCharges: ICardIssuerCharges = { id: 123 };
        const cardIssuerChargesCollection: ICardIssuerCharges[] = [{ id: 456 }];
        expectedResult = service.addCardIssuerChargesToCollectionIfMissing(cardIssuerChargesCollection, cardIssuerCharges);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(cardIssuerCharges);
      });

      it('should add only unique CardIssuerCharges to an array', () => {
        const cardIssuerChargesArray: ICardIssuerCharges[] = [{ id: 123 }, { id: 456 }, { id: 90358 }];
        const cardIssuerChargesCollection: ICardIssuerCharges[] = [{ id: 123 }];
        expectedResult = service.addCardIssuerChargesToCollectionIfMissing(cardIssuerChargesCollection, ...cardIssuerChargesArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const cardIssuerCharges: ICardIssuerCharges = { id: 123 };
        const cardIssuerCharges2: ICardIssuerCharges = { id: 456 };
        expectedResult = service.addCardIssuerChargesToCollectionIfMissing([], cardIssuerCharges, cardIssuerCharges2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(cardIssuerCharges);
        expect(expectedResult).toContain(cardIssuerCharges2);
      });

      it('should accept null and undefined values', () => {
        const cardIssuerCharges: ICardIssuerCharges = { id: 123 };
        expectedResult = service.addCardIssuerChargesToCollectionIfMissing([], null, cardIssuerCharges, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(cardIssuerCharges);
      });

      it('should return initial array if no CardIssuerCharges is added', () => {
        const cardIssuerChargesCollection: ICardIssuerCharges[] = [{ id: 123 }];
        expectedResult = service.addCardIssuerChargesToCollectionIfMissing(cardIssuerChargesCollection, undefined, null);
        expect(expectedResult).toEqual(cardIssuerChargesCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
