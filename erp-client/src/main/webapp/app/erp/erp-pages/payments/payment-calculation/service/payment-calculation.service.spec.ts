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

import { IPaymentCalculation, PaymentCalculation } from '../payment-calculation.model';

import { PaymentCalculationService } from './payment-calculation.service';
import { ErpCommonModule } from '../../../../erp-common/erp-common.module';

describe('Service Tests', () => {
  describe('PaymentCalculation Service', () => {
    let service: PaymentCalculationService;
    let httpMock: HttpTestingController;
    let elemDefault: IPaymentCalculation;
    let expectedResult: IPaymentCalculation | IPaymentCalculation[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ErpCommonModule, HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(PaymentCalculationService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        paymentExpense: 0,
        withholdingVAT: 0,
        withholdingTax: 0,
        paymentAmount: 0,
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

      it('should create a PaymentCalculation', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new PaymentCalculation()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a PaymentCalculation', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            paymentExpense: 1,
            withholdingVAT: 1,
            withholdingTax: 1,
            paymentAmount: 1,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a PaymentCalculation', () => {
        const patchObject = Object.assign(
          {
            withholdingVAT: 1,
            withholdingTax: 1,
          },
          new PaymentCalculation()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of PaymentCalculation', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            paymentExpense: 1,
            withholdingVAT: 1,
            withholdingTax: 1,
            paymentAmount: 1,
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

      it('should delete a PaymentCalculation', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addPaymentCalculationToCollectionIfMissing', () => {
        it('should add a PaymentCalculation to an empty array', () => {
          const paymentCalculation: IPaymentCalculation = { id: 123 };
          expectedResult = service.addPaymentCalculationToCollectionIfMissing([], paymentCalculation);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(paymentCalculation);
        });

        it('should not add a PaymentCalculation to an array that contains it', () => {
          const paymentCalculation: IPaymentCalculation = { id: 123 };
          const paymentCalculationCollection: IPaymentCalculation[] = [
            {
              ...paymentCalculation,
            },
            { id: 456 },
          ];
          expectedResult = service.addPaymentCalculationToCollectionIfMissing(paymentCalculationCollection, paymentCalculation);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a PaymentCalculation to an array that doesn't contain it", () => {
          const paymentCalculation: IPaymentCalculation = { id: 123 };
          const paymentCalculationCollection: IPaymentCalculation[] = [{ id: 456 }];
          expectedResult = service.addPaymentCalculationToCollectionIfMissing(paymentCalculationCollection, paymentCalculation);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(paymentCalculation);
        });

        it('should add only unique PaymentCalculation to an array', () => {
          const paymentCalculationArray: IPaymentCalculation[] = [{ id: 123 }, { id: 456 }, { id: 52541 }];
          const paymentCalculationCollection: IPaymentCalculation[] = [{ id: 123 }];
          expectedResult = service.addPaymentCalculationToCollectionIfMissing(paymentCalculationCollection, ...paymentCalculationArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const paymentCalculation: IPaymentCalculation = { id: 123 };
          const paymentCalculation2: IPaymentCalculation = { id: 456 };
          expectedResult = service.addPaymentCalculationToCollectionIfMissing([], paymentCalculation, paymentCalculation2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(paymentCalculation);
          expect(expectedResult).toContain(paymentCalculation2);
        });

        it('should accept null and undefined values', () => {
          const paymentCalculation: IPaymentCalculation = { id: 123 };
          expectedResult = service.addPaymentCalculationToCollectionIfMissing([], null, paymentCalculation, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(paymentCalculation);
        });

        it('should return initial array if no PaymentCalculation is added', () => {
          const paymentCalculationCollection: IPaymentCalculation[] = [{ id: 123 }];
          expectedResult = service.addPaymentCalculationToCollectionIfMissing(paymentCalculationCollection, undefined, null);
          expect(expectedResult).toEqual(paymentCalculationCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
