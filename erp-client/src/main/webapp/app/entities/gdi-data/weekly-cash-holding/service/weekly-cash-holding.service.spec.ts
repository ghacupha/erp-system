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
import { IWeeklyCashHolding, WeeklyCashHolding } from '../weekly-cash-holding.model';

import { WeeklyCashHoldingService } from './weekly-cash-holding.service';

describe('WeeklyCashHolding Service', () => {
  let service: WeeklyCashHoldingService;
  let httpMock: HttpTestingController;
  let elemDefault: IWeeklyCashHolding;
  let expectedResult: IWeeklyCashHolding | IWeeklyCashHolding[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(WeeklyCashHoldingService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      reportingDate: currentDate,
      fitUnits: 0,
      unfitUnits: 0,
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

    it('should create a WeeklyCashHolding', () => {
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

      service.create(new WeeklyCashHolding()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a WeeklyCashHolding', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          reportingDate: currentDate.format(DATE_FORMAT),
          fitUnits: 1,
          unfitUnits: 1,
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

    it('should partial update a WeeklyCashHolding', () => {
      const patchObject = Object.assign(
        {
          reportingDate: currentDate.format(DATE_FORMAT),
        },
        new WeeklyCashHolding()
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

    it('should return a list of WeeklyCashHolding', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          reportingDate: currentDate.format(DATE_FORMAT),
          fitUnits: 1,
          unfitUnits: 1,
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

    it('should delete a WeeklyCashHolding', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addWeeklyCashHoldingToCollectionIfMissing', () => {
      it('should add a WeeklyCashHolding to an empty array', () => {
        const weeklyCashHolding: IWeeklyCashHolding = { id: 123 };
        expectedResult = service.addWeeklyCashHoldingToCollectionIfMissing([], weeklyCashHolding);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(weeklyCashHolding);
      });

      it('should not add a WeeklyCashHolding to an array that contains it', () => {
        const weeklyCashHolding: IWeeklyCashHolding = { id: 123 };
        const weeklyCashHoldingCollection: IWeeklyCashHolding[] = [
          {
            ...weeklyCashHolding,
          },
          { id: 456 },
        ];
        expectedResult = service.addWeeklyCashHoldingToCollectionIfMissing(weeklyCashHoldingCollection, weeklyCashHolding);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a WeeklyCashHolding to an array that doesn't contain it", () => {
        const weeklyCashHolding: IWeeklyCashHolding = { id: 123 };
        const weeklyCashHoldingCollection: IWeeklyCashHolding[] = [{ id: 456 }];
        expectedResult = service.addWeeklyCashHoldingToCollectionIfMissing(weeklyCashHoldingCollection, weeklyCashHolding);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(weeklyCashHolding);
      });

      it('should add only unique WeeklyCashHolding to an array', () => {
        const weeklyCashHoldingArray: IWeeklyCashHolding[] = [{ id: 123 }, { id: 456 }, { id: 23652 }];
        const weeklyCashHoldingCollection: IWeeklyCashHolding[] = [{ id: 123 }];
        expectedResult = service.addWeeklyCashHoldingToCollectionIfMissing(weeklyCashHoldingCollection, ...weeklyCashHoldingArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const weeklyCashHolding: IWeeklyCashHolding = { id: 123 };
        const weeklyCashHolding2: IWeeklyCashHolding = { id: 456 };
        expectedResult = service.addWeeklyCashHoldingToCollectionIfMissing([], weeklyCashHolding, weeklyCashHolding2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(weeklyCashHolding);
        expect(expectedResult).toContain(weeklyCashHolding2);
      });

      it('should accept null and undefined values', () => {
        const weeklyCashHolding: IWeeklyCashHolding = { id: 123 };
        expectedResult = service.addWeeklyCashHoldingToCollectionIfMissing([], null, weeklyCashHolding, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(weeklyCashHolding);
      });

      it('should return initial array if no WeeklyCashHolding is added', () => {
        const weeklyCashHoldingCollection: IWeeklyCashHolding[] = [{ id: 123 }];
        expectedResult = service.addWeeklyCashHoldingToCollectionIfMissing(weeklyCashHoldingCollection, undefined, null);
        expect(expectedResult).toEqual(weeklyCashHoldingCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
