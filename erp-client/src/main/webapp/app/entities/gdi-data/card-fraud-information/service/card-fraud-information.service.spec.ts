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
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { ICardFraudInformation, CardFraudInformation } from '../card-fraud-information.model';

import { CardFraudInformationService } from './card-fraud-information.service';

describe('CardFraudInformation Service', () => {
  let service: CardFraudInformationService;
  let httpMock: HttpTestingController;
  let elemDefault: ICardFraudInformation;
  let expectedResult: ICardFraudInformation | ICardFraudInformation[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CardFraudInformationService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      reportingDate: currentDate,
      totalNumberOfFraudIncidents: 0,
      valueOfFraudIncedentsInLCY: 0,
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

    it('should create a CardFraudInformation', () => {
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

      service.create(new CardFraudInformation()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CardFraudInformation', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          reportingDate: currentDate.format(DATE_FORMAT),
          totalNumberOfFraudIncidents: 1,
          valueOfFraudIncedentsInLCY: 1,
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

    it('should partial update a CardFraudInformation', () => {
      const patchObject = Object.assign(
        {
          reportingDate: currentDate.format(DATE_FORMAT),
          totalNumberOfFraudIncidents: 1,
          valueOfFraudIncedentsInLCY: 1,
        },
        new CardFraudInformation()
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

    it('should return a list of CardFraudInformation', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          reportingDate: currentDate.format(DATE_FORMAT),
          totalNumberOfFraudIncidents: 1,
          valueOfFraudIncedentsInLCY: 1,
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

    it('should delete a CardFraudInformation', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addCardFraudInformationToCollectionIfMissing', () => {
      it('should add a CardFraudInformation to an empty array', () => {
        const cardFraudInformation: ICardFraudInformation = { id: 123 };
        expectedResult = service.addCardFraudInformationToCollectionIfMissing([], cardFraudInformation);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(cardFraudInformation);
      });

      it('should not add a CardFraudInformation to an array that contains it', () => {
        const cardFraudInformation: ICardFraudInformation = { id: 123 };
        const cardFraudInformationCollection: ICardFraudInformation[] = [
          {
            ...cardFraudInformation,
          },
          { id: 456 },
        ];
        expectedResult = service.addCardFraudInformationToCollectionIfMissing(cardFraudInformationCollection, cardFraudInformation);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CardFraudInformation to an array that doesn't contain it", () => {
        const cardFraudInformation: ICardFraudInformation = { id: 123 };
        const cardFraudInformationCollection: ICardFraudInformation[] = [{ id: 456 }];
        expectedResult = service.addCardFraudInformationToCollectionIfMissing(cardFraudInformationCollection, cardFraudInformation);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(cardFraudInformation);
      });

      it('should add only unique CardFraudInformation to an array', () => {
        const cardFraudInformationArray: ICardFraudInformation[] = [{ id: 123 }, { id: 456 }, { id: 88819 }];
        const cardFraudInformationCollection: ICardFraudInformation[] = [{ id: 123 }];
        expectedResult = service.addCardFraudInformationToCollectionIfMissing(cardFraudInformationCollection, ...cardFraudInformationArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const cardFraudInformation: ICardFraudInformation = { id: 123 };
        const cardFraudInformation2: ICardFraudInformation = { id: 456 };
        expectedResult = service.addCardFraudInformationToCollectionIfMissing([], cardFraudInformation, cardFraudInformation2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(cardFraudInformation);
        expect(expectedResult).toContain(cardFraudInformation2);
      });

      it('should accept null and undefined values', () => {
        const cardFraudInformation: ICardFraudInformation = { id: 123 };
        expectedResult = service.addCardFraudInformationToCollectionIfMissing([], null, cardFraudInformation, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(cardFraudInformation);
      });

      it('should return initial array if no CardFraudInformation is added', () => {
        const cardFraudInformationCollection: ICardFraudInformation[] = [{ id: 123 }];
        expectedResult = service.addCardFraudInformationToCollectionIfMissing(cardFraudInformationCollection, undefined, null);
        expect(expectedResult).toEqual(cardFraudInformationCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
