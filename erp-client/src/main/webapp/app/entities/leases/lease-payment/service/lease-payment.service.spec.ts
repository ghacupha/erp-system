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
import { ILeasePayment, LeasePayment } from '../lease-payment.model';

import { LeasePaymentService } from './lease-payment.service';

describe('LeasePayment Service', () => {
  let service: LeasePaymentService;
  let httpMock: HttpTestingController;
  let elemDefault: ILeasePayment;
  let expectedResult: ILeasePayment | ILeasePayment[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(LeasePaymentService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      paymentDate: currentDate,
      paymentAmount: 0,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          paymentDate: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a LeasePayment', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          paymentDate: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          paymentDate: currentDate,
        },
        returnedFromService
      );

      service.create(new LeasePayment()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a LeasePayment', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          paymentDate: currentDate.format(DATE_FORMAT),
          paymentAmount: 1,
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          paymentDate: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a LeasePayment', () => {
      const patchObject = Object.assign(
        {
          paymentDate: currentDate.format(DATE_FORMAT),
          paymentAmount: 1,
        },
        new LeasePayment()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          paymentDate: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of LeasePayment', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          paymentDate: currentDate.format(DATE_FORMAT),
          paymentAmount: 1,
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          paymentDate: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a LeasePayment', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addLeasePaymentToCollectionIfMissing', () => {
      it('should add a LeasePayment to an empty array', () => {
        const leasePayment: ILeasePayment = { id: 123 };
        expectedResult = service.addLeasePaymentToCollectionIfMissing([], leasePayment);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(leasePayment);
      });

      it('should not add a LeasePayment to an array that contains it', () => {
        const leasePayment: ILeasePayment = { id: 123 };
        const leasePaymentCollection: ILeasePayment[] = [
          {
            ...leasePayment,
          },
          { id: 456 },
        ];
        expectedResult = service.addLeasePaymentToCollectionIfMissing(leasePaymentCollection, leasePayment);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a LeasePayment to an array that doesn't contain it", () => {
        const leasePayment: ILeasePayment = { id: 123 };
        const leasePaymentCollection: ILeasePayment[] = [{ id: 456 }];
        expectedResult = service.addLeasePaymentToCollectionIfMissing(leasePaymentCollection, leasePayment);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(leasePayment);
      });

      it('should add only unique LeasePayment to an array', () => {
        const leasePaymentArray: ILeasePayment[] = [{ id: 123 }, { id: 456 }, { id: 23398 }];
        const leasePaymentCollection: ILeasePayment[] = [{ id: 123 }];
        expectedResult = service.addLeasePaymentToCollectionIfMissing(leasePaymentCollection, ...leasePaymentArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const leasePayment: ILeasePayment = { id: 123 };
        const leasePayment2: ILeasePayment = { id: 456 };
        expectedResult = service.addLeasePaymentToCollectionIfMissing([], leasePayment, leasePayment2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(leasePayment);
        expect(expectedResult).toContain(leasePayment2);
      });

      it('should accept null and undefined values', () => {
        const leasePayment: ILeasePayment = { id: 123 };
        expectedResult = service.addLeasePaymentToCollectionIfMissing([], null, leasePayment, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(leasePayment);
      });

      it('should return initial array if no LeasePayment is added', () => {
        const leasePaymentCollection: ILeasePayment[] = [{ id: 123 }];
        expectedResult = service.addLeasePaymentToCollectionIfMissing(leasePaymentCollection, undefined, null);
        expect(expectedResult).toEqual(leasePaymentCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
