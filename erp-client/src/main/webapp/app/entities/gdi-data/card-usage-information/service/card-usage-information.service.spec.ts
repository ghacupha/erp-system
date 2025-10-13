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
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { ICardUsageInformation, CardUsageInformation } from '../card-usage-information.model';

import { CardUsageInformationService } from './card-usage-information.service';

describe('CardUsageInformation Service', () => {
  let service: CardUsageInformationService;
  let httpMock: HttpTestingController;
  let elemDefault: ICardUsageInformation;
  let expectedResult: ICardUsageInformation | ICardUsageInformation[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CardUsageInformationService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      reportingDate: currentDate,
      totalNumberOfLiveCards: 0,
      totalActiveCards: 0,
      totalNumberOfTransactionsDone: 0,
      totalValueOfTransactionsDoneInLCY: 0,
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

    it('should create a CardUsageInformation', () => {
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

      service.create(new CardUsageInformation()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CardUsageInformation', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          reportingDate: currentDate.format(DATE_FORMAT),
          totalNumberOfLiveCards: 1,
          totalActiveCards: 1,
          totalNumberOfTransactionsDone: 1,
          totalValueOfTransactionsDoneInLCY: 1,
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

    it('should partial update a CardUsageInformation', () => {
      const patchObject = Object.assign(
        {
          reportingDate: currentDate.format(DATE_FORMAT),
          totalNumberOfLiveCards: 1,
          totalNumberOfTransactionsDone: 1,
          totalValueOfTransactionsDoneInLCY: 1,
        },
        new CardUsageInformation()
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

    it('should return a list of CardUsageInformation', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          reportingDate: currentDate.format(DATE_FORMAT),
          totalNumberOfLiveCards: 1,
          totalActiveCards: 1,
          totalNumberOfTransactionsDone: 1,
          totalValueOfTransactionsDoneInLCY: 1,
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

    it('should delete a CardUsageInformation', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addCardUsageInformationToCollectionIfMissing', () => {
      it('should add a CardUsageInformation to an empty array', () => {
        const cardUsageInformation: ICardUsageInformation = { id: 123 };
        expectedResult = service.addCardUsageInformationToCollectionIfMissing([], cardUsageInformation);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(cardUsageInformation);
      });

      it('should not add a CardUsageInformation to an array that contains it', () => {
        const cardUsageInformation: ICardUsageInformation = { id: 123 };
        const cardUsageInformationCollection: ICardUsageInformation[] = [
          {
            ...cardUsageInformation,
          },
          { id: 456 },
        ];
        expectedResult = service.addCardUsageInformationToCollectionIfMissing(cardUsageInformationCollection, cardUsageInformation);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CardUsageInformation to an array that doesn't contain it", () => {
        const cardUsageInformation: ICardUsageInformation = { id: 123 };
        const cardUsageInformationCollection: ICardUsageInformation[] = [{ id: 456 }];
        expectedResult = service.addCardUsageInformationToCollectionIfMissing(cardUsageInformationCollection, cardUsageInformation);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(cardUsageInformation);
      });

      it('should add only unique CardUsageInformation to an array', () => {
        const cardUsageInformationArray: ICardUsageInformation[] = [{ id: 123 }, { id: 456 }, { id: 41504 }];
        const cardUsageInformationCollection: ICardUsageInformation[] = [{ id: 123 }];
        expectedResult = service.addCardUsageInformationToCollectionIfMissing(cardUsageInformationCollection, ...cardUsageInformationArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const cardUsageInformation: ICardUsageInformation = { id: 123 };
        const cardUsageInformation2: ICardUsageInformation = { id: 456 };
        expectedResult = service.addCardUsageInformationToCollectionIfMissing([], cardUsageInformation, cardUsageInformation2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(cardUsageInformation);
        expect(expectedResult).toContain(cardUsageInformation2);
      });

      it('should accept null and undefined values', () => {
        const cardUsageInformation: ICardUsageInformation = { id: 123 };
        expectedResult = service.addCardUsageInformationToCollectionIfMissing([], null, cardUsageInformation, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(cardUsageInformation);
      });

      it('should return initial array if no CardUsageInformation is added', () => {
        const cardUsageInformationCollection: ICardUsageInformation[] = [{ id: 123 }];
        expectedResult = service.addCardUsageInformationToCollectionIfMissing(cardUsageInformationCollection, undefined, null);
        expect(expectedResult).toEqual(cardUsageInformationCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
