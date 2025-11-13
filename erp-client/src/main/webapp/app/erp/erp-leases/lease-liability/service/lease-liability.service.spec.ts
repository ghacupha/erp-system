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
import { ILeaseLiability, LeaseLiability } from '../lease-liability.model';

import { LeaseLiabilityService } from './lease-liability.service';

describe('LeaseLiability Service', () => {
  let service: LeaseLiabilityService;
  let httpMock: HttpTestingController;
  let elemDefault: ILeaseLiability;
  let expectedResult: ILeaseLiability | ILeaseLiability[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(LeaseLiabilityService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      leaseId: 'AAAAAAA',
      liabilityAmount: 0,
      interestRate: 0,
      startDate: currentDate,
      endDate: currentDate,
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

    it('should create a LeaseLiability', () => {
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

      service.create(new LeaseLiability()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a LeaseLiability', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          leaseId: 'BBBBBB',
          liabilityAmount: 1,
          interestRate: 1,
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

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a LeaseLiability', () => {
      const patchObject = Object.assign(
        {
          leaseId: 'BBBBBB',
          interestRate: 1,
          startDate: currentDate.format(DATE_FORMAT),
        },
        new LeaseLiability()
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

    it('should return a list of LeaseLiability', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          leaseId: 'BBBBBB',
          liabilityAmount: 1,
          interestRate: 1,
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

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a LeaseLiability', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addLeaseLiabilityToCollectionIfMissing', () => {
      it('should add a LeaseLiability to an empty array', () => {
        const leaseLiability: ILeaseLiability = { id: 123 };
        expectedResult = service.addLeaseLiabilityToCollectionIfMissing([], leaseLiability);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(leaseLiability);
      });

      it('should not add a LeaseLiability to an array that contains it', () => {
        const leaseLiability: ILeaseLiability = { id: 123 };
        const leaseLiabilityCollection: ILeaseLiability[] = [
          {
            ...leaseLiability,
          },
          { id: 456 },
        ];
        expectedResult = service.addLeaseLiabilityToCollectionIfMissing(leaseLiabilityCollection, leaseLiability);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a LeaseLiability to an array that doesn't contain it", () => {
        const leaseLiability: ILeaseLiability = { id: 123 };
        const leaseLiabilityCollection: ILeaseLiability[] = [{ id: 456 }];
        expectedResult = service.addLeaseLiabilityToCollectionIfMissing(leaseLiabilityCollection, leaseLiability);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(leaseLiability);
      });

      it('should add only unique LeaseLiability to an array', () => {
        const leaseLiabilityArray: ILeaseLiability[] = [{ id: 123 }, { id: 456 }, { id: 59942 }];
        const leaseLiabilityCollection: ILeaseLiability[] = [{ id: 123 }];
        expectedResult = service.addLeaseLiabilityToCollectionIfMissing(leaseLiabilityCollection, ...leaseLiabilityArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const leaseLiability: ILeaseLiability = { id: 123 };
        const leaseLiability2: ILeaseLiability = { id: 456 };
        expectedResult = service.addLeaseLiabilityToCollectionIfMissing([], leaseLiability, leaseLiability2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(leaseLiability);
        expect(expectedResult).toContain(leaseLiability2);
      });

      it('should accept null and undefined values', () => {
        const leaseLiability: ILeaseLiability = { id: 123 };
        expectedResult = service.addLeaseLiabilityToCollectionIfMissing([], null, leaseLiability, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(leaseLiability);
      });

      it('should return initial array if no LeaseLiability is added', () => {
        const leaseLiabilityCollection: ILeaseLiability[] = [{ id: 123 }];
        expectedResult = service.addLeaseLiabilityToCollectionIfMissing(leaseLiabilityCollection, undefined, null);
        expect(expectedResult).toEqual(leaseLiabilityCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
