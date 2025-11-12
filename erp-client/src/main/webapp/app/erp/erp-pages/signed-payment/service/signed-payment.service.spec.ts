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
import { ISignedPayment, SignedPayment } from '../signed-payment.model';

import { SignedPaymentService } from './signed-payment.service';
import { CurrencyTypes } from '../../../erp-common/enumerations/currency-types.model';

describe('Service Tests', () => {
  describe('SignedPayment Service', () => {
    let service: SignedPaymentService;
    let httpMock: HttpTestingController;
    let elemDefault: ISignedPayment;
    let expectedResult: ISignedPayment | ISignedPayment[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(SignedPaymentService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        transactionNumber: 'AAAAAAA',
        transactionDate: currentDate,
        transactionCurrency: CurrencyTypes.KES,
        transactionAmount: 0,
        dealerName: 'AAAAAAA',
        fileUploadToken: 'AAAAAAA',
        compilationToken: 'AAAAAAA',
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            transactionDate: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a SignedPayment', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            transactionDate: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            transactionDate: currentDate,
          },
          returnedFromService
        );

        service.create(new SignedPayment()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a SignedPayment', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            transactionNumber: 'BBBBBB',
            transactionDate: currentDate.format(DATE_FORMAT),
            transactionCurrency: 'BBBBBB',
            transactionAmount: 1,
            dealerName: 'BBBBBB',
            fileUploadToken: 'BBBBBB',
            compilationToken: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            transactionDate: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a SignedPayment', () => {
        const patchObject = Object.assign(
          {
            transactionNumber: 'BBBBBB',
            transactionDate: currentDate.format(DATE_FORMAT),
            transactionCurrency: 'BBBBBB',
            dealerName: 'BBBBBB',
          },
          new SignedPayment()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            transactionDate: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of SignedPayment', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            transactionNumber: 'BBBBBB',
            transactionDate: currentDate.format(DATE_FORMAT),
            transactionCurrency: 'BBBBBB',
            transactionAmount: 1,
            dealerName: 'BBBBBB',
            fileUploadToken: 'BBBBBB',
            compilationToken: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            transactionDate: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a SignedPayment', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addSignedPaymentToCollectionIfMissing', () => {
        it('should add a SignedPayment to an empty array', () => {
          const signedPayment: ISignedPayment = { id: 123 };
          expectedResult = service.addSignedPaymentToCollectionIfMissing([], signedPayment);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(signedPayment);
        });

        it('should not add a SignedPayment to an array that contains it', () => {
          const signedPayment: ISignedPayment = { id: 123 };
          const signedPaymentCollection: ISignedPayment[] = [
            {
              ...signedPayment,
            },
            { id: 456 },
          ];
          expectedResult = service.addSignedPaymentToCollectionIfMissing(signedPaymentCollection, signedPayment);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a SignedPayment to an array that doesn't contain it", () => {
          const signedPayment: ISignedPayment = { id: 123 };
          const signedPaymentCollection: ISignedPayment[] = [{ id: 456 }];
          expectedResult = service.addSignedPaymentToCollectionIfMissing(signedPaymentCollection, signedPayment);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(signedPayment);
        });

        it('should add only unique SignedPayment to an array', () => {
          const signedPaymentArray: ISignedPayment[] = [{ id: 123 }, { id: 456 }, { id: 6885 }];
          const signedPaymentCollection: ISignedPayment[] = [{ id: 123 }];
          expectedResult = service.addSignedPaymentToCollectionIfMissing(signedPaymentCollection, ...signedPaymentArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const signedPayment: ISignedPayment = { id: 123 };
          const signedPayment2: ISignedPayment = { id: 456 };
          expectedResult = service.addSignedPaymentToCollectionIfMissing([], signedPayment, signedPayment2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(signedPayment);
          expect(expectedResult).toContain(signedPayment2);
        });

        it('should accept null and undefined values', () => {
          const signedPayment: ISignedPayment = { id: 123 };
          expectedResult = service.addSignedPaymentToCollectionIfMissing([], null, signedPayment, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(signedPayment);
        });

        it('should return initial array if no SignedPayment is added', () => {
          const signedPaymentCollection: ISignedPayment[] = [{ id: 123 }];
          expectedResult = service.addSignedPaymentToCollectionIfMissing(signedPaymentCollection, undefined, null);
          expect(expectedResult).toEqual(signedPaymentCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
