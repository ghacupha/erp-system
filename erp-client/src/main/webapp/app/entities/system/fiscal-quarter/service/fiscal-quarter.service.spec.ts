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
import { IFiscalQuarter, FiscalQuarter } from '../fiscal-quarter.model';

import { FiscalQuarterService } from './fiscal-quarter.service';

describe('FiscalQuarter Service', () => {
  let service: FiscalQuarterService;
  let httpMock: HttpTestingController;
  let elemDefault: IFiscalQuarter;
  let expectedResult: IFiscalQuarter | IFiscalQuarter[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(FiscalQuarterService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      quarterNumber: 0,
      startDate: currentDate,
      endDate: currentDate,
      fiscalQuarterCode: 'AAAAAAA',
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

    it('should create a FiscalQuarter', () => {
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

      service.create(new FiscalQuarter()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a FiscalQuarter', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          quarterNumber: 1,
          startDate: currentDate.format(DATE_FORMAT),
          endDate: currentDate.format(DATE_FORMAT),
          fiscalQuarterCode: 'BBBBBB',
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

    it('should partial update a FiscalQuarter', () => {
      const patchObject = Object.assign(
        {
          startDate: currentDate.format(DATE_FORMAT),
        },
        new FiscalQuarter()
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

    it('should return a list of FiscalQuarter', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          quarterNumber: 1,
          startDate: currentDate.format(DATE_FORMAT),
          endDate: currentDate.format(DATE_FORMAT),
          fiscalQuarterCode: 'BBBBBB',
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

    it('should delete a FiscalQuarter', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addFiscalQuarterToCollectionIfMissing', () => {
      it('should add a FiscalQuarter to an empty array', () => {
        const fiscalQuarter: IFiscalQuarter = { id: 123 };
        expectedResult = service.addFiscalQuarterToCollectionIfMissing([], fiscalQuarter);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(fiscalQuarter);
      });

      it('should not add a FiscalQuarter to an array that contains it', () => {
        const fiscalQuarter: IFiscalQuarter = { id: 123 };
        const fiscalQuarterCollection: IFiscalQuarter[] = [
          {
            ...fiscalQuarter,
          },
          { id: 456 },
        ];
        expectedResult = service.addFiscalQuarterToCollectionIfMissing(fiscalQuarterCollection, fiscalQuarter);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a FiscalQuarter to an array that doesn't contain it", () => {
        const fiscalQuarter: IFiscalQuarter = { id: 123 };
        const fiscalQuarterCollection: IFiscalQuarter[] = [{ id: 456 }];
        expectedResult = service.addFiscalQuarterToCollectionIfMissing(fiscalQuarterCollection, fiscalQuarter);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(fiscalQuarter);
      });

      it('should add only unique FiscalQuarter to an array', () => {
        const fiscalQuarterArray: IFiscalQuarter[] = [{ id: 123 }, { id: 456 }, { id: 90183 }];
        const fiscalQuarterCollection: IFiscalQuarter[] = [{ id: 123 }];
        expectedResult = service.addFiscalQuarterToCollectionIfMissing(fiscalQuarterCollection, ...fiscalQuarterArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const fiscalQuarter: IFiscalQuarter = { id: 123 };
        const fiscalQuarter2: IFiscalQuarter = { id: 456 };
        expectedResult = service.addFiscalQuarterToCollectionIfMissing([], fiscalQuarter, fiscalQuarter2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(fiscalQuarter);
        expect(expectedResult).toContain(fiscalQuarter2);
      });

      it('should accept null and undefined values', () => {
        const fiscalQuarter: IFiscalQuarter = { id: 123 };
        expectedResult = service.addFiscalQuarterToCollectionIfMissing([], null, fiscalQuarter, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(fiscalQuarter);
      });

      it('should return initial array if no FiscalQuarter is added', () => {
        const fiscalQuarterCollection: IFiscalQuarter[] = [{ id: 123 }];
        expectedResult = service.addFiscalQuarterToCollectionIfMissing(fiscalQuarterCollection, undefined, null);
        expect(expectedResult).toEqual(fiscalQuarterCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
