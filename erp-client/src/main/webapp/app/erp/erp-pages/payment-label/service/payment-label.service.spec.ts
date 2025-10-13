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


import { PaymentLabelService } from './payment-label.service';
import { IPaymentLabel, PaymentLabel } from '../payment-label.model';

describe('Service Tests', () => {
  describe('PaymentLabel Service', () => {
    let service: PaymentLabelService;
    let httpMock: HttpTestingController;
    let elemDefault: IPaymentLabel;
    let expectedResult: IPaymentLabel | IPaymentLabel[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(PaymentLabelService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        description: 'AAAAAAA',
        comments: 'AAAAAAA',
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

      it('should create a PaymentLabel', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new PaymentLabel()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a PaymentLabel', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            description: 'BBBBBB',
            comments: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a PaymentLabel', () => {
        const patchObject = Object.assign(
          {
            description: 'BBBBBB',
            comments: 'BBBBBB',
          },
          new PaymentLabel()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of PaymentLabel', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            description: 'BBBBBB',
            comments: 'BBBBBB',
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

      it('should delete a PaymentLabel', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addPaymentLabelToCollectionIfMissing', () => {
        it('should add a PaymentLabel to an empty array', () => {
          const paymentLabel: IPaymentLabel = { id: 123 };
          expectedResult = service.addPaymentLabelToCollectionIfMissing([], paymentLabel);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(paymentLabel);
        });

        it('should not add a PaymentLabel to an array that contains it', () => {
          const paymentLabel: IPaymentLabel = { id: 123 };
          const paymentLabelCollection: IPaymentLabel[] = [
            {
              ...paymentLabel,
            },
            { id: 456 },
          ];
          expectedResult = service.addPaymentLabelToCollectionIfMissing(paymentLabelCollection, paymentLabel);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a PaymentLabel to an array that doesn't contain it", () => {
          const paymentLabel: IPaymentLabel = { id: 123 };
          const paymentLabelCollection: IPaymentLabel[] = [{ id: 456 }];
          expectedResult = service.addPaymentLabelToCollectionIfMissing(paymentLabelCollection, paymentLabel);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(paymentLabel);
        });

        it('should add only unique PaymentLabel to an array', () => {
          const paymentLabelArray: IPaymentLabel[] = [{ id: 123 }, { id: 456 }, { id: 1914 }];
          const paymentLabelCollection: IPaymentLabel[] = [{ id: 123 }];
          expectedResult = service.addPaymentLabelToCollectionIfMissing(paymentLabelCollection, ...paymentLabelArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const paymentLabel: IPaymentLabel = { id: 123 };
          const paymentLabel2: IPaymentLabel = { id: 456 };
          expectedResult = service.addPaymentLabelToCollectionIfMissing([], paymentLabel, paymentLabel2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(paymentLabel);
          expect(expectedResult).toContain(paymentLabel2);
        });

        it('should accept null and undefined values', () => {
          const paymentLabel: IPaymentLabel = { id: 123 };
          expectedResult = service.addPaymentLabelToCollectionIfMissing([], null, paymentLabel, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(paymentLabel);
        });

        it('should return initial array if no PaymentLabel is added', () => {
          const paymentLabelCollection: IPaymentLabel[] = [{ id: 123 }];
          expectedResult = service.addPaymentLabelToCollectionIfMissing(paymentLabelCollection, undefined, null);
          expect(expectedResult).toEqual(paymentLabelCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
