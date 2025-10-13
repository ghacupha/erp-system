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
import { IWeeklyCounterfeitHolding, WeeklyCounterfeitHolding } from '../weekly-counterfeit-holding.model';

import { WeeklyCounterfeitHoldingService } from './weekly-counterfeit-holding.service';

describe('WeeklyCounterfeitHolding Service', () => {
  let service: WeeklyCounterfeitHoldingService;
  let httpMock: HttpTestingController;
  let elemDefault: IWeeklyCounterfeitHolding;
  let expectedResult: IWeeklyCounterfeitHolding | IWeeklyCounterfeitHolding[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(WeeklyCounterfeitHoldingService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      reportingDate: currentDate,
      dateConfiscated: currentDate,
      serialNumber: 'AAAAAAA',
      depositorsNames: 'AAAAAAA',
      tellersNames: 'AAAAAAA',
      dateSubmittedToCBK: currentDate,
      remarks: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          reportingDate: currentDate.format(DATE_FORMAT),
          dateConfiscated: currentDate.format(DATE_FORMAT),
          dateSubmittedToCBK: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a WeeklyCounterfeitHolding', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          reportingDate: currentDate.format(DATE_FORMAT),
          dateConfiscated: currentDate.format(DATE_FORMAT),
          dateSubmittedToCBK: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          reportingDate: currentDate,
          dateConfiscated: currentDate,
          dateSubmittedToCBK: currentDate,
        },
        returnedFromService
      );

      service.create(new WeeklyCounterfeitHolding()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a WeeklyCounterfeitHolding', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          reportingDate: currentDate.format(DATE_FORMAT),
          dateConfiscated: currentDate.format(DATE_FORMAT),
          serialNumber: 'BBBBBB',
          depositorsNames: 'BBBBBB',
          tellersNames: 'BBBBBB',
          dateSubmittedToCBK: currentDate.format(DATE_FORMAT),
          remarks: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          reportingDate: currentDate,
          dateConfiscated: currentDate,
          dateSubmittedToCBK: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a WeeklyCounterfeitHolding', () => {
      const patchObject = Object.assign(
        {
          serialNumber: 'BBBBBB',
          depositorsNames: 'BBBBBB',
          tellersNames: 'BBBBBB',
          dateSubmittedToCBK: currentDate.format(DATE_FORMAT),
          remarks: 'BBBBBB',
        },
        new WeeklyCounterfeitHolding()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          reportingDate: currentDate,
          dateConfiscated: currentDate,
          dateSubmittedToCBK: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of WeeklyCounterfeitHolding', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          reportingDate: currentDate.format(DATE_FORMAT),
          dateConfiscated: currentDate.format(DATE_FORMAT),
          serialNumber: 'BBBBBB',
          depositorsNames: 'BBBBBB',
          tellersNames: 'BBBBBB',
          dateSubmittedToCBK: currentDate.format(DATE_FORMAT),
          remarks: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          reportingDate: currentDate,
          dateConfiscated: currentDate,
          dateSubmittedToCBK: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a WeeklyCounterfeitHolding', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addWeeklyCounterfeitHoldingToCollectionIfMissing', () => {
      it('should add a WeeklyCounterfeitHolding to an empty array', () => {
        const weeklyCounterfeitHolding: IWeeklyCounterfeitHolding = { id: 123 };
        expectedResult = service.addWeeklyCounterfeitHoldingToCollectionIfMissing([], weeklyCounterfeitHolding);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(weeklyCounterfeitHolding);
      });

      it('should not add a WeeklyCounterfeitHolding to an array that contains it', () => {
        const weeklyCounterfeitHolding: IWeeklyCounterfeitHolding = { id: 123 };
        const weeklyCounterfeitHoldingCollection: IWeeklyCounterfeitHolding[] = [
          {
            ...weeklyCounterfeitHolding,
          },
          { id: 456 },
        ];
        expectedResult = service.addWeeklyCounterfeitHoldingToCollectionIfMissing(
          weeklyCounterfeitHoldingCollection,
          weeklyCounterfeitHolding
        );
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a WeeklyCounterfeitHolding to an array that doesn't contain it", () => {
        const weeklyCounterfeitHolding: IWeeklyCounterfeitHolding = { id: 123 };
        const weeklyCounterfeitHoldingCollection: IWeeklyCounterfeitHolding[] = [{ id: 456 }];
        expectedResult = service.addWeeklyCounterfeitHoldingToCollectionIfMissing(
          weeklyCounterfeitHoldingCollection,
          weeklyCounterfeitHolding
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(weeklyCounterfeitHolding);
      });

      it('should add only unique WeeklyCounterfeitHolding to an array', () => {
        const weeklyCounterfeitHoldingArray: IWeeklyCounterfeitHolding[] = [{ id: 123 }, { id: 456 }, { id: 45791 }];
        const weeklyCounterfeitHoldingCollection: IWeeklyCounterfeitHolding[] = [{ id: 123 }];
        expectedResult = service.addWeeklyCounterfeitHoldingToCollectionIfMissing(
          weeklyCounterfeitHoldingCollection,
          ...weeklyCounterfeitHoldingArray
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const weeklyCounterfeitHolding: IWeeklyCounterfeitHolding = { id: 123 };
        const weeklyCounterfeitHolding2: IWeeklyCounterfeitHolding = { id: 456 };
        expectedResult = service.addWeeklyCounterfeitHoldingToCollectionIfMissing([], weeklyCounterfeitHolding, weeklyCounterfeitHolding2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(weeklyCounterfeitHolding);
        expect(expectedResult).toContain(weeklyCounterfeitHolding2);
      });

      it('should accept null and undefined values', () => {
        const weeklyCounterfeitHolding: IWeeklyCounterfeitHolding = { id: 123 };
        expectedResult = service.addWeeklyCounterfeitHoldingToCollectionIfMissing([], null, weeklyCounterfeitHolding, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(weeklyCounterfeitHolding);
      });

      it('should return initial array if no WeeklyCounterfeitHolding is added', () => {
        const weeklyCounterfeitHoldingCollection: IWeeklyCounterfeitHolding[] = [{ id: 123 }];
        expectedResult = service.addWeeklyCounterfeitHoldingToCollectionIfMissing(weeklyCounterfeitHoldingCollection, undefined, null);
        expect(expectedResult).toEqual(weeklyCounterfeitHoldingCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
