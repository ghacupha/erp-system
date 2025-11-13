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
import { IExchangeRate, ExchangeRate } from '../exchange-rate.model';

import { ExchangeRateService } from './exchange-rate.service';

describe('ExchangeRate Service', () => {
  let service: ExchangeRateService;
  let httpMock: HttpTestingController;
  let elemDefault: IExchangeRate;
  let expectedResult: IExchangeRate | IExchangeRate[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ExchangeRateService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      businessReportingDay: currentDate,
      buyingRate: 0,
      sellingRate: 0,
      meanRate: 0,
      closingBidRate: 0,
      closingOfferRate: 0,
      usdCrossRate: 0,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          businessReportingDay: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a ExchangeRate', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          businessReportingDay: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          businessReportingDay: currentDate,
        },
        returnedFromService
      );

      service.create(new ExchangeRate()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ExchangeRate', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          businessReportingDay: currentDate.format(DATE_FORMAT),
          buyingRate: 1,
          sellingRate: 1,
          meanRate: 1,
          closingBidRate: 1,
          closingOfferRate: 1,
          usdCrossRate: 1,
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          businessReportingDay: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ExchangeRate', () => {
      const patchObject = Object.assign(
        {
          closingBidRate: 1,
        },
        new ExchangeRate()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          businessReportingDay: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ExchangeRate', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          businessReportingDay: currentDate.format(DATE_FORMAT),
          buyingRate: 1,
          sellingRate: 1,
          meanRate: 1,
          closingBidRate: 1,
          closingOfferRate: 1,
          usdCrossRate: 1,
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          businessReportingDay: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a ExchangeRate', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addExchangeRateToCollectionIfMissing', () => {
      it('should add a ExchangeRate to an empty array', () => {
        const exchangeRate: IExchangeRate = { id: 123 };
        expectedResult = service.addExchangeRateToCollectionIfMissing([], exchangeRate);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(exchangeRate);
      });

      it('should not add a ExchangeRate to an array that contains it', () => {
        const exchangeRate: IExchangeRate = { id: 123 };
        const exchangeRateCollection: IExchangeRate[] = [
          {
            ...exchangeRate,
          },
          { id: 456 },
        ];
        expectedResult = service.addExchangeRateToCollectionIfMissing(exchangeRateCollection, exchangeRate);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ExchangeRate to an array that doesn't contain it", () => {
        const exchangeRate: IExchangeRate = { id: 123 };
        const exchangeRateCollection: IExchangeRate[] = [{ id: 456 }];
        expectedResult = service.addExchangeRateToCollectionIfMissing(exchangeRateCollection, exchangeRate);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(exchangeRate);
      });

      it('should add only unique ExchangeRate to an array', () => {
        const exchangeRateArray: IExchangeRate[] = [{ id: 123 }, { id: 456 }, { id: 73328 }];
        const exchangeRateCollection: IExchangeRate[] = [{ id: 123 }];
        expectedResult = service.addExchangeRateToCollectionIfMissing(exchangeRateCollection, ...exchangeRateArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const exchangeRate: IExchangeRate = { id: 123 };
        const exchangeRate2: IExchangeRate = { id: 456 };
        expectedResult = service.addExchangeRateToCollectionIfMissing([], exchangeRate, exchangeRate2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(exchangeRate);
        expect(expectedResult).toContain(exchangeRate2);
      });

      it('should accept null and undefined values', () => {
        const exchangeRate: IExchangeRate = { id: 123 };
        expectedResult = service.addExchangeRateToCollectionIfMissing([], null, exchangeRate, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(exchangeRate);
      });

      it('should return initial array if no ExchangeRate is added', () => {
        const exchangeRateCollection: IExchangeRate[] = [{ id: 123 }];
        expectedResult = service.addExchangeRateToCollectionIfMissing(exchangeRateCollection, undefined, null);
        expect(expectedResult).toEqual(exchangeRateCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
