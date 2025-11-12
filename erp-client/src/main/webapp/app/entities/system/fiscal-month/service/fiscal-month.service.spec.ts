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
import { IFiscalMonth, FiscalMonth } from '../fiscal-month.model';

import { FiscalMonthService } from './fiscal-month.service';

describe('FiscalMonth Service', () => {
  let service: FiscalMonthService;
  let httpMock: HttpTestingController;
  let elemDefault: IFiscalMonth;
  let expectedResult: IFiscalMonth | IFiscalMonth[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(FiscalMonthService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      monthNumber: 0,
      startDate: currentDate,
      endDate: currentDate,
      fiscalMonthCode: 'AAAAAAA',
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

    it('should create a FiscalMonth', () => {
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

      service.create(new FiscalMonth()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a FiscalMonth', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          monthNumber: 1,
          startDate: currentDate.format(DATE_FORMAT),
          endDate: currentDate.format(DATE_FORMAT),
          fiscalMonthCode: 'BBBBBB',
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

    it('should partial update a FiscalMonth', () => {
      const patchObject = Object.assign(
        {
          monthNumber: 1,
          startDate: currentDate.format(DATE_FORMAT),
          endDate: currentDate.format(DATE_FORMAT),
          fiscalMonthCode: 'BBBBBB',
        },
        new FiscalMonth()
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

    it('should return a list of FiscalMonth', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          monthNumber: 1,
          startDate: currentDate.format(DATE_FORMAT),
          endDate: currentDate.format(DATE_FORMAT),
          fiscalMonthCode: 'BBBBBB',
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

    it('should delete a FiscalMonth', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addFiscalMonthToCollectionIfMissing', () => {
      it('should add a FiscalMonth to an empty array', () => {
        const fiscalMonth: IFiscalMonth = { id: 123 };
        expectedResult = service.addFiscalMonthToCollectionIfMissing([], fiscalMonth);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(fiscalMonth);
      });

      it('should not add a FiscalMonth to an array that contains it', () => {
        const fiscalMonth: IFiscalMonth = { id: 123 };
        const fiscalMonthCollection: IFiscalMonth[] = [
          {
            ...fiscalMonth,
          },
          { id: 456 },
        ];
        expectedResult = service.addFiscalMonthToCollectionIfMissing(fiscalMonthCollection, fiscalMonth);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a FiscalMonth to an array that doesn't contain it", () => {
        const fiscalMonth: IFiscalMonth = { id: 123 };
        const fiscalMonthCollection: IFiscalMonth[] = [{ id: 456 }];
        expectedResult = service.addFiscalMonthToCollectionIfMissing(fiscalMonthCollection, fiscalMonth);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(fiscalMonth);
      });

      it('should add only unique FiscalMonth to an array', () => {
        const fiscalMonthArray: IFiscalMonth[] = [{ id: 123 }, { id: 456 }, { id: 32299 }];
        const fiscalMonthCollection: IFiscalMonth[] = [{ id: 123 }];
        expectedResult = service.addFiscalMonthToCollectionIfMissing(fiscalMonthCollection, ...fiscalMonthArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const fiscalMonth: IFiscalMonth = { id: 123 };
        const fiscalMonth2: IFiscalMonth = { id: 456 };
        expectedResult = service.addFiscalMonthToCollectionIfMissing([], fiscalMonth, fiscalMonth2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(fiscalMonth);
        expect(expectedResult).toContain(fiscalMonth2);
      });

      it('should accept null and undefined values', () => {
        const fiscalMonth: IFiscalMonth = { id: 123 };
        expectedResult = service.addFiscalMonthToCollectionIfMissing([], null, fiscalMonth, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(fiscalMonth);
      });

      it('should return initial array if no FiscalMonth is added', () => {
        const fiscalMonthCollection: IFiscalMonth[] = [{ id: 123 }];
        expectedResult = service.addFiscalMonthToCollectionIfMissing(fiscalMonthCollection, undefined, null);
        expect(expectedResult).toEqual(fiscalMonthCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
