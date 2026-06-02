import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ITransactionAccountReportItem } from '../transaction-account-report-item.model';

import { TransactionAccountReportItemService } from './transaction-account-report-item.service';

describe('TransactionAccountReportItem Service', () => {
  let service: TransactionAccountReportItemService;
  let httpMock: HttpTestingController;
  let elemDefault: ITransactionAccountReportItem;
  let expectedResult: ITransactionAccountReportItem | ITransactionAccountReportItem[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(TransactionAccountReportItemService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      accountName: 'AAAAAAA',
      accountNumber: 'AAAAAAA',
      accountBalance: 0,
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

    it('should return a list of TransactionAccountReportItem', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          accountName: 'BBBBBB',
          accountNumber: 'BBBBBB',
          accountBalance: 1,
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

    describe('addTransactionAccountReportItemToCollectionIfMissing', () => {
      it('should add a TransactionAccountReportItem to an empty array', () => {
        const transactionAccountReportItem: ITransactionAccountReportItem = { id: 123 };
        expectedResult = service.addTransactionAccountReportItemToCollectionIfMissing([], transactionAccountReportItem);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(transactionAccountReportItem);
      });

      it('should not add a TransactionAccountReportItem to an array that contains it', () => {
        const transactionAccountReportItem: ITransactionAccountReportItem = { id: 123 };
        const transactionAccountReportItemCollection: ITransactionAccountReportItem[] = [
          {
            ...transactionAccountReportItem,
          },
          { id: 456 },
        ];
        expectedResult = service.addTransactionAccountReportItemToCollectionIfMissing(
          transactionAccountReportItemCollection,
          transactionAccountReportItem
        );
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a TransactionAccountReportItem to an array that doesn't contain it", () => {
        const transactionAccountReportItem: ITransactionAccountReportItem = { id: 123 };
        const transactionAccountReportItemCollection: ITransactionAccountReportItem[] = [{ id: 456 }];
        expectedResult = service.addTransactionAccountReportItemToCollectionIfMissing(
          transactionAccountReportItemCollection,
          transactionAccountReportItem
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(transactionAccountReportItem);
      });

      it('should add only unique TransactionAccountReportItem to an array', () => {
        const transactionAccountReportItemArray: ITransactionAccountReportItem[] = [{ id: 123 }, { id: 456 }, { id: 47477 }];
        const transactionAccountReportItemCollection: ITransactionAccountReportItem[] = [{ id: 123 }];
        expectedResult = service.addTransactionAccountReportItemToCollectionIfMissing(
          transactionAccountReportItemCollection,
          ...transactionAccountReportItemArray
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const transactionAccountReportItem: ITransactionAccountReportItem = { id: 123 };
        const transactionAccountReportItem2: ITransactionAccountReportItem = { id: 456 };
        expectedResult = service.addTransactionAccountReportItemToCollectionIfMissing(
          [],
          transactionAccountReportItem,
          transactionAccountReportItem2
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(transactionAccountReportItem);
        expect(expectedResult).toContain(transactionAccountReportItem2);
      });

      it('should accept null and undefined values', () => {
        const transactionAccountReportItem: ITransactionAccountReportItem = { id: 123 };
        expectedResult = service.addTransactionAccountReportItemToCollectionIfMissing([], null, transactionAccountReportItem, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(transactionAccountReportItem);
      });

      it('should return initial array if no TransactionAccountReportItem is added', () => {
        const transactionAccountReportItemCollection: ITransactionAccountReportItem[] = [{ id: 123 }];
        expectedResult = service.addTransactionAccountReportItemToCollectionIfMissing(
          transactionAccountReportItemCollection,
          undefined,
          null
        );
        expect(expectedResult).toEqual(transactionAccountReportItemCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
