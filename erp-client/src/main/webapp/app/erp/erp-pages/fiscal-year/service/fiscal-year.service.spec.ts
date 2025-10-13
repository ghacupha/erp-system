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
import { IFiscalYear, FiscalYear } from '../fiscal-year.model';

import { FiscalYearService } from './fiscal-year.service';
import { FiscalYearStatusType } from '../../../erp-common/enumerations/fiscal-year-status-type.model';

describe('FiscalYear Service', () => {
  let service: FiscalYearService;
  let httpMock: HttpTestingController;
  let elemDefault: IFiscalYear;
  let expectedResult: IFiscalYear | IFiscalYear[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(FiscalYearService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      fiscalYearCode: 'AAAAAAA',
      startDate: currentDate,
      endDate: currentDate,
      fiscalYearStatus: FiscalYearStatusType.OPEN,
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

    it('should create a FiscalYear', () => {
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

      service.create(new FiscalYear()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a FiscalYear', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          fiscalYearCode: 'BBBBBB',
          startDate: currentDate.format(DATE_FORMAT),
          endDate: currentDate.format(DATE_FORMAT),
          fiscalYearStatus: 'BBBBBB',
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

    it('should partial update a FiscalYear', () => {
      const patchObject = Object.assign(
        {
          startDate: currentDate.format(DATE_FORMAT),
        },
        new FiscalYear()
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

    it('should return a list of FiscalYear', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          fiscalYearCode: 'BBBBBB',
          startDate: currentDate.format(DATE_FORMAT),
          endDate: currentDate.format(DATE_FORMAT),
          fiscalYearStatus: 'BBBBBB',
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

    it('should delete a FiscalYear', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addFiscalYearToCollectionIfMissing', () => {
      it('should add a FiscalYear to an empty array', () => {
        const fiscalYear: IFiscalYear = { id: 123 };
        expectedResult = service.addFiscalYearToCollectionIfMissing([], fiscalYear);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(fiscalYear);
      });

      it('should not add a FiscalYear to an array that contains it', () => {
        const fiscalYear: IFiscalYear = { id: 123 };
        const fiscalYearCollection: IFiscalYear[] = [
          {
            ...fiscalYear,
          },
          { id: 456 },
        ];
        expectedResult = service.addFiscalYearToCollectionIfMissing(fiscalYearCollection, fiscalYear);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a FiscalYear to an array that doesn't contain it", () => {
        const fiscalYear: IFiscalYear = { id: 123 };
        const fiscalYearCollection: IFiscalYear[] = [{ id: 456 }];
        expectedResult = service.addFiscalYearToCollectionIfMissing(fiscalYearCollection, fiscalYear);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(fiscalYear);
      });

      it('should add only unique FiscalYear to an array', () => {
        const fiscalYearArray: IFiscalYear[] = [{ id: 123 }, { id: 456 }, { id: 2619 }];
        const fiscalYearCollection: IFiscalYear[] = [{ id: 123 }];
        expectedResult = service.addFiscalYearToCollectionIfMissing(fiscalYearCollection, ...fiscalYearArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const fiscalYear: IFiscalYear = { id: 123 };
        const fiscalYear2: IFiscalYear = { id: 456 };
        expectedResult = service.addFiscalYearToCollectionIfMissing([], fiscalYear, fiscalYear2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(fiscalYear);
        expect(expectedResult).toContain(fiscalYear2);
      });

      it('should accept null and undefined values', () => {
        const fiscalYear: IFiscalYear = { id: 123 };
        expectedResult = service.addFiscalYearToCollectionIfMissing([], null, fiscalYear, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(fiscalYear);
      });

      it('should return initial array if no FiscalYear is added', () => {
        const fiscalYearCollection: IFiscalYear[] = [{ id: 123 }];
        expectedResult = service.addFiscalYearToCollectionIfMissing(fiscalYearCollection, undefined, null);
        expect(expectedResult).toEqual(fiscalYearCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
