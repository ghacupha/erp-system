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
import { ILeasePeriod, LeasePeriod } from '../lease-period.model';

import { LeasePeriodService } from './lease-period.service';

describe('LeasePeriod Service', () => {
  let service: LeasePeriodService;
  let httpMock: HttpTestingController;
  let elemDefault: ILeasePeriod;
  let expectedResult: ILeasePeriod | ILeasePeriod[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(LeasePeriodService);
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

    it('should create a LeasePeriod', () => {
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

      service.create(new LeasePeriod()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a LeasePeriod', () => {
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

    it('should partial update a LeasePeriod', () => {
      const patchObject = Object.assign(
        {
          startDate: currentDate.format(DATE_FORMAT),
          periodCode: 'BBBBBB',
        },
        new LeasePeriod()
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

    it('should return a list of LeasePeriod', () => {
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

    it('should delete a LeasePeriod', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addLeasePeriodToCollectionIfMissing', () => {
      it('should add a LeasePeriod to an empty array', () => {
        const leasePeriod: ILeasePeriod = { id: 123 };
        expectedResult = service.addLeasePeriodToCollectionIfMissing([], leasePeriod);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(leasePeriod);
      });

      it('should not add a LeasePeriod to an array that contains it', () => {
        const leasePeriod: ILeasePeriod = { id: 123 };
        const leasePeriodCollection: ILeasePeriod[] = [
          {
            ...leasePeriod,
          },
          { id: 456 },
        ];
        expectedResult = service.addLeasePeriodToCollectionIfMissing(leasePeriodCollection, leasePeriod);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a LeasePeriod to an array that doesn't contain it", () => {
        const leasePeriod: ILeasePeriod = { id: 123 };
        const leasePeriodCollection: ILeasePeriod[] = [{ id: 456 }];
        expectedResult = service.addLeasePeriodToCollectionIfMissing(leasePeriodCollection, leasePeriod);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(leasePeriod);
      });

      it('should add only unique LeasePeriod to an array', () => {
        const leasePeriodArray: ILeasePeriod[] = [{ id: 123 }, { id: 456 }, { id: 30659 }];
        const leasePeriodCollection: ILeasePeriod[] = [{ id: 123 }];
        expectedResult = service.addLeasePeriodToCollectionIfMissing(leasePeriodCollection, ...leasePeriodArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const leasePeriod: ILeasePeriod = { id: 123 };
        const leasePeriod2: ILeasePeriod = { id: 456 };
        expectedResult = service.addLeasePeriodToCollectionIfMissing([], leasePeriod, leasePeriod2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(leasePeriod);
        expect(expectedResult).toContain(leasePeriod2);
      });

      it('should accept null and undefined values', () => {
        const leasePeriod: ILeasePeriod = { id: 123 };
        expectedResult = service.addLeasePeriodToCollectionIfMissing([], null, leasePeriod, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(leasePeriod);
      });

      it('should return initial array if no LeasePeriod is added', () => {
        const leasePeriodCollection: ILeasePeriod[] = [{ id: 123 }];
        expectedResult = service.addLeasePeriodToCollectionIfMissing(leasePeriodCollection, undefined, null);
        expect(expectedResult).toEqual(leasePeriodCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
