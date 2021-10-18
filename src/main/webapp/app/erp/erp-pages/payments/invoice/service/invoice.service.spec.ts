import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { CurrencyTypes } from 'app/entities/enumerations/currency-types.model';
import { IInvoice, Invoice } from '../invoice.model';

import { InvoiceService } from './invoice.service';
import {MockStore, provideMockStore} from "@ngrx/store/testing";
import {initialState} from "../../../../store/global-store.definition";

describe('Service Tests', () => {
  describe('Invoice Service', () => {
    let service: InvoiceService;
    let httpMock: HttpTestingController;
    let elemDefault: IInvoice;
    let expectedResult: IInvoice | IInvoice[] | boolean | null;
    let currentDate: dayjs.Dayjs;
    let store: MockStore

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [provideMockStore({initialState})]
      });
      expectedResult = null;
      service = TestBed.inject(InvoiceService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();
      store = TestBed.inject(MockStore);

      elemDefault = {
        id: 0,
        invoiceNumber: 'AAAAAAA',
        invoiceDate: currentDate,
        invoiceAmount: 0,
        currency: CurrencyTypes.KES,
        conversionRate: 0,
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

      it('should create a Invoice', () => {
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

        service.create(new Invoice()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Invoice', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            invoiceNumber: 'BBBBBB',
            invoiceDate: currentDate.format(DATE_FORMAT),
            invoiceAmount: 1,
            currency: 'BBBBBB',
            conversionRate: 1,
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

      it('should partial update a Invoice', () => {
        const patchObject = Object.assign(
          {
            invoiceDate: currentDate.format(DATE_FORMAT),
            invoiceAmount: 1,
            currency: 'BBBBBB',
          },
          new Invoice()
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

      it('should return a list of Invoice', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            invoiceNumber: 'BBBBBB',
            invoiceDate: currentDate.format(DATE_FORMAT),
            invoiceAmount: 1,
            currency: 'BBBBBB',
            conversionRate: 1,
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

      it('should delete a Invoice', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addInvoiceToCollectionIfMissing', () => {
        it('should add a Invoice to an empty array', () => {
          const invoice: IInvoice = { id: 123 };
          expectedResult = service.addInvoiceToCollectionIfMissing([], invoice);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(invoice);
        });

        it('should not add a Invoice to an array that contains it', () => {
          const invoice: IInvoice = { id: 123 };
          const invoiceCollection: IInvoice[] = [
            {
              ...invoice,
            },
            { id: 456 },
          ];
          expectedResult = service.addInvoiceToCollectionIfMissing(invoiceCollection, invoice);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Invoice to an array that doesn't contain it", () => {
          const invoice: IInvoice = { id: 123 };
          const invoiceCollection: IInvoice[] = [{ id: 456 }];
          expectedResult = service.addInvoiceToCollectionIfMissing(invoiceCollection, invoice);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(invoice);
        });

        it('should add only unique Invoice to an array', () => {
          const invoiceArray: IInvoice[] = [{ id: 123 }, { id: 456 }, { id: 95170 }];
          const invoiceCollection: IInvoice[] = [{ id: 123 }];
          expectedResult = service.addInvoiceToCollectionIfMissing(invoiceCollection, ...invoiceArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const invoice: IInvoice = { id: 123 };
          const invoice2: IInvoice = { id: 456 };
          expectedResult = service.addInvoiceToCollectionIfMissing([], invoice, invoice2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(invoice);
          expect(expectedResult).toContain(invoice2);
        });

        it('should accept null and undefined values', () => {
          const invoice: IInvoice = { id: 123 };
          expectedResult = service.addInvoiceToCollectionIfMissing([], null, invoice, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(invoice);
        });

        it('should return initial array if no Invoice is added', () => {
          const invoiceCollection: IInvoice[] = [{ id: 123 }];
          expectedResult = service.addInvoiceToCollectionIfMissing(invoiceCollection, undefined, null);
          expect(expectedResult).toEqual(invoiceCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
