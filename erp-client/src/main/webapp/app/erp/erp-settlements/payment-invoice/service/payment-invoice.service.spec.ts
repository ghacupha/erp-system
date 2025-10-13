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
import { IPaymentInvoice, PaymentInvoice } from '../payment-invoice.model';

import { PaymentInvoiceService } from './payment-invoice.service';

describe('PaymentInvoice Service', () => {
  let service: PaymentInvoiceService;
  let httpMock: HttpTestingController;
  let elemDefault: IPaymentInvoice;
  let expectedResult: IPaymentInvoice | IPaymentInvoice[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(PaymentInvoiceService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      invoiceNumber: 'AAAAAAA',
      invoiceDate: currentDate,
      invoiceAmount: 0,
      fileUploadToken: 'AAAAAAA',
      compilationToken: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          invoiceDate: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a PaymentInvoice', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          invoiceDate: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          invoiceDate: currentDate,
        },
        returnedFromService
      );

      service.create(new PaymentInvoice()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a PaymentInvoice', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          invoiceNumber: 'BBBBBB',
          invoiceDate: currentDate.format(DATE_FORMAT),
          invoiceAmount: 1,
          fileUploadToken: 'BBBBBB',
          compilationToken: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          invoiceDate: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a PaymentInvoice', () => {
      const patchObject = Object.assign(
        {
          invoiceNumber: 'BBBBBB',
        },
        new PaymentInvoice()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          invoiceDate: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of PaymentInvoice', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          invoiceNumber: 'BBBBBB',
          invoiceDate: currentDate.format(DATE_FORMAT),
          invoiceAmount: 1,
          fileUploadToken: 'BBBBBB',
          compilationToken: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          invoiceDate: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a PaymentInvoice', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addPaymentInvoiceToCollectionIfMissing', () => {
      it('should add a PaymentInvoice to an empty array', () => {
        const paymentInvoice: IPaymentInvoice = { id: 123 };
        expectedResult = service.addPaymentInvoiceToCollectionIfMissing([], paymentInvoice);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(paymentInvoice);
      });

      it('should not add a PaymentInvoice to an array that contains it', () => {
        const paymentInvoice: IPaymentInvoice = { id: 123 };
        const paymentInvoiceCollection: IPaymentInvoice[] = [
          {
            ...paymentInvoice,
          },
          { id: 456 },
        ];
        expectedResult = service.addPaymentInvoiceToCollectionIfMissing(paymentInvoiceCollection, paymentInvoice);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a PaymentInvoice to an array that doesn't contain it", () => {
        const paymentInvoice: IPaymentInvoice = { id: 123 };
        const paymentInvoiceCollection: IPaymentInvoice[] = [{ id: 456 }];
        expectedResult = service.addPaymentInvoiceToCollectionIfMissing(paymentInvoiceCollection, paymentInvoice);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(paymentInvoice);
      });

      it('should add only unique PaymentInvoice to an array', () => {
        const paymentInvoiceArray: IPaymentInvoice[] = [{ id: 123 }, { id: 456 }, { id: 69659 }];
        const paymentInvoiceCollection: IPaymentInvoice[] = [{ id: 123 }];
        expectedResult = service.addPaymentInvoiceToCollectionIfMissing(paymentInvoiceCollection, ...paymentInvoiceArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const paymentInvoice: IPaymentInvoice = { id: 123 };
        const paymentInvoice2: IPaymentInvoice = { id: 456 };
        expectedResult = service.addPaymentInvoiceToCollectionIfMissing([], paymentInvoice, paymentInvoice2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(paymentInvoice);
        expect(expectedResult).toContain(paymentInvoice2);
      });

      it('should accept null and undefined values', () => {
        const paymentInvoice: IPaymentInvoice = { id: 123 };
        expectedResult = service.addPaymentInvoiceToCollectionIfMissing([], null, paymentInvoice, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(paymentInvoice);
      });

      it('should return initial array if no PaymentInvoice is added', () => {
        const paymentInvoiceCollection: IPaymentInvoice[] = [{ id: 123 }];
        expectedResult = service.addPaymentInvoiceToCollectionIfMissing(paymentInvoiceCollection, undefined, null);
        expect(expectedResult).toEqual(paymentInvoiceCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
