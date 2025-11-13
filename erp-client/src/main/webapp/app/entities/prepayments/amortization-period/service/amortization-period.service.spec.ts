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
import { IAmortizationPeriod, AmortizationPeriod } from '../amortization-period.model';

import { AmortizationPeriodService } from './amortization-period.service';

describe('AmortizationPeriod Service', () => {
  let service: AmortizationPeriodService;
  let httpMock: HttpTestingController;
  let elemDefault: IAmortizationPeriod;
  let expectedResult: IAmortizationPeriod | IAmortizationPeriod[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(AmortizationPeriodService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      sequenceNumber: 0,
      startDate: currentDate,
      endDate: currentDate,
      periodCode: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          startDate: currentDate.format(DATE_FORMAT),
          endDate: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a AmortizationPeriod', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          startDate: currentDate.format(DATE_FORMAT),
          endDate: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          startDate: currentDate,
          endDate: currentDate,
        },
        returnedFromService
      );

      service.create(new AmortizationPeriod()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a AmortizationPeriod', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          sequenceNumber: 1,
          startDate: currentDate.format(DATE_FORMAT),
          endDate: currentDate.format(DATE_FORMAT),
          periodCode: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          startDate: currentDate,
          endDate: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a AmortizationPeriod', () => {
      const patchObject = Object.assign(
        {
          endDate: currentDate.format(DATE_FORMAT),
          periodCode: 'BBBBBB',
        },
        new AmortizationPeriod()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          startDate: currentDate,
          endDate: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of AmortizationPeriod', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          sequenceNumber: 1,
          startDate: currentDate.format(DATE_FORMAT),
          endDate: currentDate.format(DATE_FORMAT),
          periodCode: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          startDate: currentDate,
          endDate: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a AmortizationPeriod', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addAmortizationPeriodToCollectionIfMissing', () => {
      it('should add a AmortizationPeriod to an empty array', () => {
        const amortizationPeriod: IAmortizationPeriod = { id: 123 };
        expectedResult = service.addAmortizationPeriodToCollectionIfMissing([], amortizationPeriod);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(amortizationPeriod);
      });

      it('should not add a AmortizationPeriod to an array that contains it', () => {
        const amortizationPeriod: IAmortizationPeriod = { id: 123 };
        const amortizationPeriodCollection: IAmortizationPeriod[] = [
          {
            ...amortizationPeriod,
          },
          { id: 456 },
        ];
        expectedResult = service.addAmortizationPeriodToCollectionIfMissing(amortizationPeriodCollection, amortizationPeriod);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a AmortizationPeriod to an array that doesn't contain it", () => {
        const amortizationPeriod: IAmortizationPeriod = { id: 123 };
        const amortizationPeriodCollection: IAmortizationPeriod[] = [{ id: 456 }];
        expectedResult = service.addAmortizationPeriodToCollectionIfMissing(amortizationPeriodCollection, amortizationPeriod);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(amortizationPeriod);
      });

      it('should add only unique AmortizationPeriod to an array', () => {
        const amortizationPeriodArray: IAmortizationPeriod[] = [{ id: 123 }, { id: 456 }, { id: 35439 }];
        const amortizationPeriodCollection: IAmortizationPeriod[] = [{ id: 123 }];
        expectedResult = service.addAmortizationPeriodToCollectionIfMissing(amortizationPeriodCollection, ...amortizationPeriodArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const amortizationPeriod: IAmortizationPeriod = { id: 123 };
        const amortizationPeriod2: IAmortizationPeriod = { id: 456 };
        expectedResult = service.addAmortizationPeriodToCollectionIfMissing([], amortizationPeriod, amortizationPeriod2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(amortizationPeriod);
        expect(expectedResult).toContain(amortizationPeriod2);
      });

      it('should accept null and undefined values', () => {
        const amortizationPeriod: IAmortizationPeriod = { id: 123 };
        expectedResult = service.addAmortizationPeriodToCollectionIfMissing([], null, amortizationPeriod, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(amortizationPeriod);
      });

      it('should return initial array if no AmortizationPeriod is added', () => {
        const amortizationPeriodCollection: IAmortizationPeriod[] = [{ id: 123 }];
        expectedResult = service.addAmortizationPeriodToCollectionIfMissing(amortizationPeriodCollection, undefined, null);
        expect(expectedResult).toEqual(amortizationPeriodCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
