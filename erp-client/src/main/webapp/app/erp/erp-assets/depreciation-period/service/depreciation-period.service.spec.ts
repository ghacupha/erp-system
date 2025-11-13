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
import { IDepreciationPeriod, DepreciationPeriod } from '../depreciation-period.model';

import { DepreciationPeriodService } from './depreciation-period.service';
import { DepreciationPeriodStatusTypes } from '../../../erp-common/enumerations/depreciation-period-status-types.model';

describe('DepreciationPeriod Service', () => {
  let service: DepreciationPeriodService;
  let httpMock: HttpTestingController;
  let elemDefault: IDepreciationPeriod;
  let expectedResult: IDepreciationPeriod | IDepreciationPeriod[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(DepreciationPeriodService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      startDate: currentDate,
      endDate: currentDate,
      depreciationPeriodStatus: DepreciationPeriodStatusTypes.OPEN,
      periodCode: 'AAAAAAA',
      processLocked: false,
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

    it('should create a DepreciationPeriod', () => {
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

      service.create(new DepreciationPeriod()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a DepreciationPeriod', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          startDate: currentDate.format(DATE_FORMAT),
          endDate: currentDate.format(DATE_FORMAT),
          depreciationPeriodStatus: 'BBBBBB',
          periodCode: 'BBBBBB',
          processLocked: true,
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

    it('should partial update a DepreciationPeriod', () => {
      const patchObject = Object.assign(
        {
          endDate: currentDate.format(DATE_FORMAT),
          depreciationPeriodStatus: 'BBBBBB',
          processLocked: true,
        },
        new DepreciationPeriod()
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

    it('should return a list of DepreciationPeriod', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          startDate: currentDate.format(DATE_FORMAT),
          endDate: currentDate.format(DATE_FORMAT),
          depreciationPeriodStatus: 'BBBBBB',
          periodCode: 'BBBBBB',
          processLocked: true,
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

    it('should delete a DepreciationPeriod', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addDepreciationPeriodToCollectionIfMissing', () => {
      it('should add a DepreciationPeriod to an empty array', () => {
        const depreciationPeriod: IDepreciationPeriod = { id: 123 };
        expectedResult = service.addDepreciationPeriodToCollectionIfMissing([], depreciationPeriod);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(depreciationPeriod);
      });

      it('should not add a DepreciationPeriod to an array that contains it', () => {
        const depreciationPeriod: IDepreciationPeriod = { id: 123 };
        const depreciationPeriodCollection: IDepreciationPeriod[] = [
          {
            ...depreciationPeriod,
          },
          { id: 456 },
        ];
        expectedResult = service.addDepreciationPeriodToCollectionIfMissing(depreciationPeriodCollection, depreciationPeriod);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a DepreciationPeriod to an array that doesn't contain it", () => {
        const depreciationPeriod: IDepreciationPeriod = { id: 123 };
        const depreciationPeriodCollection: IDepreciationPeriod[] = [{ id: 456 }];
        expectedResult = service.addDepreciationPeriodToCollectionIfMissing(depreciationPeriodCollection, depreciationPeriod);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(depreciationPeriod);
      });

      it('should add only unique DepreciationPeriod to an array', () => {
        const depreciationPeriodArray: IDepreciationPeriod[] = [{ id: 123 }, { id: 456 }, { id: 56507 }];
        const depreciationPeriodCollection: IDepreciationPeriod[] = [{ id: 123 }];
        expectedResult = service.addDepreciationPeriodToCollectionIfMissing(depreciationPeriodCollection, ...depreciationPeriodArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const depreciationPeriod: IDepreciationPeriod = { id: 123 };
        const depreciationPeriod2: IDepreciationPeriod = { id: 456 };
        expectedResult = service.addDepreciationPeriodToCollectionIfMissing([], depreciationPeriod, depreciationPeriod2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(depreciationPeriod);
        expect(expectedResult).toContain(depreciationPeriod2);
      });

      it('should accept null and undefined values', () => {
        const depreciationPeriod: IDepreciationPeriod = { id: 123 };
        expectedResult = service.addDepreciationPeriodToCollectionIfMissing([], null, depreciationPeriod, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(depreciationPeriod);
      });

      it('should return initial array if no DepreciationPeriod is added', () => {
        const depreciationPeriodCollection: IDepreciationPeriod[] = [{ id: 123 }];
        expectedResult = service.addDepreciationPeriodToCollectionIfMissing(depreciationPeriodCollection, undefined, null);
        expect(expectedResult).toEqual(depreciationPeriodCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
