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
import { IPaymentRequisition, PaymentRequisition } from '../payment-requisition.model';

import { PaymentRequisitionService } from './payment-requisition.service';

describe('PaymentRequisition Service', () => {
  let service: PaymentRequisitionService;
  let httpMock: HttpTestingController;
  let elemDefault: IPaymentRequisition;
  let expectedResult: IPaymentRequisition | IPaymentRequisition[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(PaymentRequisitionService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      receptionDate: currentDate,
      dealerName: 'AAAAAAA',
      briefDescription: 'AAAAAAA',
      requisitionNumber: 'AAAAAAA',
      invoicedAmount: 0,
      disbursementCost: 0,
      taxableAmount: 0,
      requisitionProcessed: false,
      fileUploadToken: 'AAAAAAA',
      compilationToken: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          receptionDate: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a PaymentRequisition', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          receptionDate: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          receptionDate: currentDate,
        },
        returnedFromService
      );

      service.create(new PaymentRequisition()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a PaymentRequisition', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          receptionDate: currentDate.format(DATE_FORMAT),
          dealerName: 'BBBBBB',
          briefDescription: 'BBBBBB',
          requisitionNumber: 'BBBBBB',
          invoicedAmount: 1,
          disbursementCost: 1,
          taxableAmount: 1,
          requisitionProcessed: true,
          fileUploadToken: 'BBBBBB',
          compilationToken: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          receptionDate: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a PaymentRequisition', () => {
      const patchObject = Object.assign(
        {
          receptionDate: currentDate.format(DATE_FORMAT),
          dealerName: 'BBBBBB',
          briefDescription: 'BBBBBB',
          requisitionNumber: 'BBBBBB',
          invoicedAmount: 1,
          disbursementCost: 1,
          fileUploadToken: 'BBBBBB',
        },
        new PaymentRequisition()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          receptionDate: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of PaymentRequisition', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          receptionDate: currentDate.format(DATE_FORMAT),
          dealerName: 'BBBBBB',
          briefDescription: 'BBBBBB',
          requisitionNumber: 'BBBBBB',
          invoicedAmount: 1,
          disbursementCost: 1,
          taxableAmount: 1,
          requisitionProcessed: true,
          fileUploadToken: 'BBBBBB',
          compilationToken: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          receptionDate: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a PaymentRequisition', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addPaymentRequisitionToCollectionIfMissing', () => {
      it('should add a PaymentRequisition to an empty array', () => {
        const paymentRequisition: IPaymentRequisition = { id: 123 };
        expectedResult = service.addPaymentRequisitionToCollectionIfMissing([], paymentRequisition);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(paymentRequisition);
      });

      it('should not add a PaymentRequisition to an array that contains it', () => {
        const paymentRequisition: IPaymentRequisition = { id: 123 };
        const paymentRequisitionCollection: IPaymentRequisition[] = [
          {
            ...paymentRequisition,
          },
          { id: 456 },
        ];
        expectedResult = service.addPaymentRequisitionToCollectionIfMissing(paymentRequisitionCollection, paymentRequisition);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a PaymentRequisition to an array that doesn't contain it", () => {
        const paymentRequisition: IPaymentRequisition = { id: 123 };
        const paymentRequisitionCollection: IPaymentRequisition[] = [{ id: 456 }];
        expectedResult = service.addPaymentRequisitionToCollectionIfMissing(paymentRequisitionCollection, paymentRequisition);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(paymentRequisition);
      });

      it('should add only unique PaymentRequisition to an array', () => {
        const paymentRequisitionArray: IPaymentRequisition[] = [{ id: 123 }, { id: 456 }, { id: 87053 }];
        const paymentRequisitionCollection: IPaymentRequisition[] = [{ id: 123 }];
        expectedResult = service.addPaymentRequisitionToCollectionIfMissing(paymentRequisitionCollection, ...paymentRequisitionArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const paymentRequisition: IPaymentRequisition = { id: 123 };
        const paymentRequisition2: IPaymentRequisition = { id: 456 };
        expectedResult = service.addPaymentRequisitionToCollectionIfMissing([], paymentRequisition, paymentRequisition2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(paymentRequisition);
        expect(expectedResult).toContain(paymentRequisition2);
      });

      it('should accept null and undefined values', () => {
        const paymentRequisition: IPaymentRequisition = { id: 123 };
        expectedResult = service.addPaymentRequisitionToCollectionIfMissing([], null, paymentRequisition, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(paymentRequisition);
      });

      it('should return initial array if no PaymentRequisition is added', () => {
        const paymentRequisitionCollection: IPaymentRequisition[] = [{ id: 123 }];
        expectedResult = service.addPaymentRequisitionToCollectionIfMissing(paymentRequisitionCollection, undefined, null);
        expect(expectedResult).toEqual(paymentRequisitionCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
