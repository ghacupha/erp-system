///
/// Copyright © 2021 Edwin Njeru (mailnjeru@gmail.com)
///
/// Licensed under the Apache License, Version 2.0 (the "License");
/// you may not use this file except in compliance with the License.
/// You may obtain a copy of the License at
///
///     http://www.apache.org/licenses/LICENSE-2.0
///
/// Unless required by applicable law or agreed to in writing, software
/// distributed under the License is distributed on an "AS IS" BASIS,
/// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
/// See the License for the specific language governing permissions and
/// limitations under the License.
///

import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { PaymentCalculationService } from 'app/entities/payments/payment-calculation/payment-calculation.service';
import { IPaymentCalculation, PaymentCalculation } from 'app/shared/model/payments/payment-calculation.model';

describe('Service Tests', () => {
  describe('PaymentCalculation Service', () => {
    let injector: TestBed;
    let service: PaymentCalculationService;
    let httpMock: HttpTestingController;
    let elemDefault: IPaymentCalculation;
    let expectedResult: IPaymentCalculation | IPaymentCalculation[] | boolean | null;
    let currentDate: moment.Moment;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(PaymentCalculationService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new PaymentCalculation(0, 'AAAAAAA', currentDate, 'AAAAAAA', 0, 0, 0, 0);
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

      it('should create a PaymentCalculation', () => {
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

        service.create(new PaymentCalculation()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a PaymentCalculation', () => {
        const returnedFromService = Object.assign(
          {
            paymentNumber: 'BBBBBB',
            paymentDate: currentDate.format(DATE_FORMAT),
            paymentCategory: 'BBBBBB',
            paymentExpense: 1,
            withholdingVAT: 1,
            withholdingTax: 1,
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

      it('should return a list of PaymentCalculation', () => {
        const returnedFromService = Object.assign(
          {
            paymentNumber: 'BBBBBB',
            paymentDate: currentDate.format(DATE_FORMAT),
            paymentCategory: 'BBBBBB',
            paymentExpense: 1,
            withholdingVAT: 1,
            withholdingTax: 1,
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

      it('should delete a PaymentCalculation', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
