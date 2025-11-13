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
import { IPrepaymentAmortization, PrepaymentAmortization } from '../prepayment-amortization.model';

import { PrepaymentAmortizationService } from './prepayment-amortization.service';

describe('PrepaymentAmortization Service', () => {
  let service: PrepaymentAmortizationService;
  let httpMock: HttpTestingController;
  let elemDefault: IPrepaymentAmortization;
  let expectedResult: IPrepaymentAmortization | IPrepaymentAmortization[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(PrepaymentAmortizationService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      description: 'AAAAAAA',
      prepaymentPeriod: currentDate,
      prepaymentAmount: 0,
      inactive: false,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          prepaymentPeriod: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a PrepaymentAmortization', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          prepaymentPeriod: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          prepaymentPeriod: currentDate,
        },
        returnedFromService
      );

      service.create(new PrepaymentAmortization()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a PrepaymentAmortization', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          description: 'BBBBBB',
          prepaymentPeriod: currentDate.format(DATE_FORMAT),
          prepaymentAmount: 1,
          inactive: true,
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          prepaymentPeriod: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a PrepaymentAmortization', () => {
      const patchObject = Object.assign(
        {
          description: 'BBBBBB',
          inactive: true,
        },
        new PrepaymentAmortization()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          prepaymentPeriod: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of PrepaymentAmortization', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          description: 'BBBBBB',
          prepaymentPeriod: currentDate.format(DATE_FORMAT),
          prepaymentAmount: 1,
          inactive: true,
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          prepaymentPeriod: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a PrepaymentAmortization', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addPrepaymentAmortizationToCollectionIfMissing', () => {
      it('should add a PrepaymentAmortization to an empty array', () => {
        const prepaymentAmortization: IPrepaymentAmortization = { id: 123 };
        expectedResult = service.addPrepaymentAmortizationToCollectionIfMissing([], prepaymentAmortization);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(prepaymentAmortization);
      });

      it('should not add a PrepaymentAmortization to an array that contains it', () => {
        const prepaymentAmortization: IPrepaymentAmortization = { id: 123 };
        const prepaymentAmortizationCollection: IPrepaymentAmortization[] = [
          {
            ...prepaymentAmortization,
          },
          { id: 456 },
        ];
        expectedResult = service.addPrepaymentAmortizationToCollectionIfMissing(prepaymentAmortizationCollection, prepaymentAmortization);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a PrepaymentAmortization to an array that doesn't contain it", () => {
        const prepaymentAmortization: IPrepaymentAmortization = { id: 123 };
        const prepaymentAmortizationCollection: IPrepaymentAmortization[] = [{ id: 456 }];
        expectedResult = service.addPrepaymentAmortizationToCollectionIfMissing(prepaymentAmortizationCollection, prepaymentAmortization);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(prepaymentAmortization);
      });

      it('should add only unique PrepaymentAmortization to an array', () => {
        const prepaymentAmortizationArray: IPrepaymentAmortization[] = [{ id: 123 }, { id: 456 }, { id: 78042 }];
        const prepaymentAmortizationCollection: IPrepaymentAmortization[] = [{ id: 123 }];
        expectedResult = service.addPrepaymentAmortizationToCollectionIfMissing(
          prepaymentAmortizationCollection,
          ...prepaymentAmortizationArray
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const prepaymentAmortization: IPrepaymentAmortization = { id: 123 };
        const prepaymentAmortization2: IPrepaymentAmortization = { id: 456 };
        expectedResult = service.addPrepaymentAmortizationToCollectionIfMissing([], prepaymentAmortization, prepaymentAmortization2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(prepaymentAmortization);
        expect(expectedResult).toContain(prepaymentAmortization2);
      });

      it('should accept null and undefined values', () => {
        const prepaymentAmortization: IPrepaymentAmortization = { id: 123 };
        expectedResult = service.addPrepaymentAmortizationToCollectionIfMissing([], null, prepaymentAmortization, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(prepaymentAmortization);
      });

      it('should return initial array if no PrepaymentAmortization is added', () => {
        const prepaymentAmortizationCollection: IPrepaymentAmortization[] = [{ id: 123 }];
        expectedResult = service.addPrepaymentAmortizationToCollectionIfMissing(prepaymentAmortizationCollection, undefined, null);
        expect(expectedResult).toEqual(prepaymentAmortizationCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
