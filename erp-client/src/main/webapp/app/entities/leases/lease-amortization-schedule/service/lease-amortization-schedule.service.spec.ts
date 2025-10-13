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

import { ILeaseAmortizationSchedule, LeaseAmortizationSchedule } from '../lease-amortization-schedule.model';

import { LeaseAmortizationScheduleService } from './lease-amortization-schedule.service';

describe('LeaseAmortizationSchedule Service', () => {
  let service: LeaseAmortizationScheduleService;
  let httpMock: HttpTestingController;
  let elemDefault: ILeaseAmortizationSchedule;
  let expectedResult: ILeaseAmortizationSchedule | ILeaseAmortizationSchedule[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(LeaseAmortizationScheduleService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      identifier: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign({}, elemDefault);

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a LeaseAmortizationSchedule', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new LeaseAmortizationSchedule()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a LeaseAmortizationSchedule', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          identifier: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a LeaseAmortizationSchedule', () => {
      const patchObject = Object.assign({}, new LeaseAmortizationSchedule());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of LeaseAmortizationSchedule', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          identifier: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a LeaseAmortizationSchedule', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addLeaseAmortizationScheduleToCollectionIfMissing', () => {
      it('should add a LeaseAmortizationSchedule to an empty array', () => {
        const leaseAmortizationSchedule: ILeaseAmortizationSchedule = { id: 123 };
        expectedResult = service.addLeaseAmortizationScheduleToCollectionIfMissing([], leaseAmortizationSchedule);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(leaseAmortizationSchedule);
      });

      it('should not add a LeaseAmortizationSchedule to an array that contains it', () => {
        const leaseAmortizationSchedule: ILeaseAmortizationSchedule = { id: 123 };
        const leaseAmortizationScheduleCollection: ILeaseAmortizationSchedule[] = [
          {
            ...leaseAmortizationSchedule,
          },
          { id: 456 },
        ];
        expectedResult = service.addLeaseAmortizationScheduleToCollectionIfMissing(
          leaseAmortizationScheduleCollection,
          leaseAmortizationSchedule
        );
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a LeaseAmortizationSchedule to an array that doesn't contain it", () => {
        const leaseAmortizationSchedule: ILeaseAmortizationSchedule = { id: 123 };
        const leaseAmortizationScheduleCollection: ILeaseAmortizationSchedule[] = [{ id: 456 }];
        expectedResult = service.addLeaseAmortizationScheduleToCollectionIfMissing(
          leaseAmortizationScheduleCollection,
          leaseAmortizationSchedule
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(leaseAmortizationSchedule);
      });

      it('should add only unique LeaseAmortizationSchedule to an array', () => {
        const leaseAmortizationScheduleArray: ILeaseAmortizationSchedule[] = [{ id: 123 }, { id: 456 }, { id: 20733 }];
        const leaseAmortizationScheduleCollection: ILeaseAmortizationSchedule[] = [{ id: 123 }];
        expectedResult = service.addLeaseAmortizationScheduleToCollectionIfMissing(
          leaseAmortizationScheduleCollection,
          ...leaseAmortizationScheduleArray
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const leaseAmortizationSchedule: ILeaseAmortizationSchedule = { id: 123 };
        const leaseAmortizationSchedule2: ILeaseAmortizationSchedule = { id: 456 };
        expectedResult = service.addLeaseAmortizationScheduleToCollectionIfMissing(
          [],
          leaseAmortizationSchedule,
          leaseAmortizationSchedule2
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(leaseAmortizationSchedule);
        expect(expectedResult).toContain(leaseAmortizationSchedule2);
      });

      it('should accept null and undefined values', () => {
        const leaseAmortizationSchedule: ILeaseAmortizationSchedule = { id: 123 };
        expectedResult = service.addLeaseAmortizationScheduleToCollectionIfMissing([], null, leaseAmortizationSchedule, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(leaseAmortizationSchedule);
      });

      it('should return initial array if no LeaseAmortizationSchedule is added', () => {
        const leaseAmortizationScheduleCollection: ILeaseAmortizationSchedule[] = [{ id: 123 }];
        expectedResult = service.addLeaseAmortizationScheduleToCollectionIfMissing(leaseAmortizationScheduleCollection, undefined, null);
        expect(expectedResult).toEqual(leaseAmortizationScheduleCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
