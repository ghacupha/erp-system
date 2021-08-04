import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IPaymentCalculation, PaymentCalculation } from '../payment-calculation.model';

import { PaymentCalculationService } from './payment-calculation.service';

describe('Service Tests', () => {
  describe('PaymentCalculation Service', () => {
    let service: PaymentCalculationService;
    let httpMock: HttpTestingController;
    let elemDefault: IPaymentCalculation;
    let expectedResult: IPaymentCalculation | IPaymentCalculation[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(PaymentCalculationService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        paymentNumber: 'AAAAAAA',
        paymentDate: currentDate,
        paymentExpense: 0,
        withholdingVAT: 0,
        withholdingTax: 0,
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
            id: 1,
            paymentNumber: 'BBBBBB',
            paymentDate: currentDate.format(DATE_FORMAT),
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

      it('should partial update a PaymentCalculation', () => {
        const patchObject = Object.assign(
          {
            paymentDate: currentDate.format(DATE_FORMAT),
            paymentExpense: 1,
            withholdingTax: 1,
            paymentAmount: 1,
          },
          new PaymentCalculation()
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

      it('should return a list of PaymentCalculation', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            paymentNumber: 'BBBBBB',
            paymentDate: currentDate.format(DATE_FORMAT),
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
          const paymentCalculationArray: IPaymentCalculation[] = [{ id: 123 }, { id: 456 }, { id: 13521 }];
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
