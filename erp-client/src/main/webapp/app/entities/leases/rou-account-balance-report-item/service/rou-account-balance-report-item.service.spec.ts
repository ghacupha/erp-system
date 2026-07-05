import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IRouAccountBalanceReportItem } from '../rou-account-balance-report-item.model';

import { RouAccountBalanceReportItemService } from './rou-account-balance-report-item.service';

describe('RouAccountBalanceReportItem Service', () => {
  let service: RouAccountBalanceReportItemService;
  let httpMock: HttpTestingController;
  let elemDefault: IRouAccountBalanceReportItem;
  let expectedResult: IRouAccountBalanceReportItem | IRouAccountBalanceReportItem[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(RouAccountBalanceReportItemService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      assetAccountName: 'AAAAAAA',
      assetAccountNumber: 'AAAAAAA',
      depreciationAccountNumber: 'AAAAAAA',
      totalLeaseAmount: 0,
      accruedDepreciationAmount: 0,
      currentPeriodDepreciationAmount: 0,
      netBookValue: 0,
      fiscalPeriodEndDate: currentDate,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          fiscalPeriodEndDate: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should return a list of RouAccountBalanceReportItem', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          assetAccountName: 'BBBBBB',
          assetAccountNumber: 'BBBBBB',
          depreciationAccountNumber: 'BBBBBB',
          totalLeaseAmount: 1,
          accruedDepreciationAmount: 1,
          currentPeriodDepreciationAmount: 1,
          netBookValue: 1,
          fiscalPeriodEndDate: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          fiscalPeriodEndDate: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    describe('addRouAccountBalanceReportItemToCollectionIfMissing', () => {
      it('should add a RouAccountBalanceReportItem to an empty array', () => {
        const rouAccountBalanceReportItem: IRouAccountBalanceReportItem = { id: 123 };
        expectedResult = service.addRouAccountBalanceReportItemToCollectionIfMissing([], rouAccountBalanceReportItem);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(rouAccountBalanceReportItem);
      });

      it('should not add a RouAccountBalanceReportItem to an array that contains it', () => {
        const rouAccountBalanceReportItem: IRouAccountBalanceReportItem = { id: 123 };
        const rouAccountBalanceReportItemCollection: IRouAccountBalanceReportItem[] = [
          {
            ...rouAccountBalanceReportItem,
          },
          { id: 456 },
        ];
        expectedResult = service.addRouAccountBalanceReportItemToCollectionIfMissing(
          rouAccountBalanceReportItemCollection,
          rouAccountBalanceReportItem
        );
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a RouAccountBalanceReportItem to an array that doesn't contain it", () => {
        const rouAccountBalanceReportItem: IRouAccountBalanceReportItem = { id: 123 };
        const rouAccountBalanceReportItemCollection: IRouAccountBalanceReportItem[] = [{ id: 456 }];
        expectedResult = service.addRouAccountBalanceReportItemToCollectionIfMissing(
          rouAccountBalanceReportItemCollection,
          rouAccountBalanceReportItem
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(rouAccountBalanceReportItem);
      });

      it('should add only unique RouAccountBalanceReportItem to an array', () => {
        const rouAccountBalanceReportItemArray: IRouAccountBalanceReportItem[] = [{ id: 123 }, { id: 456 }, { id: 6900 }];
        const rouAccountBalanceReportItemCollection: IRouAccountBalanceReportItem[] = [{ id: 123 }];
        expectedResult = service.addRouAccountBalanceReportItemToCollectionIfMissing(
          rouAccountBalanceReportItemCollection,
          ...rouAccountBalanceReportItemArray
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const rouAccountBalanceReportItem: IRouAccountBalanceReportItem = { id: 123 };
        const rouAccountBalanceReportItem2: IRouAccountBalanceReportItem = { id: 456 };
        expectedResult = service.addRouAccountBalanceReportItemToCollectionIfMissing(
          [],
          rouAccountBalanceReportItem,
          rouAccountBalanceReportItem2
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(rouAccountBalanceReportItem);
        expect(expectedResult).toContain(rouAccountBalanceReportItem2);
      });

      it('should accept null and undefined values', () => {
        const rouAccountBalanceReportItem: IRouAccountBalanceReportItem = { id: 123 };
        expectedResult = service.addRouAccountBalanceReportItemToCollectionIfMissing([], null, rouAccountBalanceReportItem, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(rouAccountBalanceReportItem);
      });

      it('should return initial array if no RouAccountBalanceReportItem is added', () => {
        const rouAccountBalanceReportItemCollection: IRouAccountBalanceReportItem[] = [{ id: 123 }];
        expectedResult = service.addRouAccountBalanceReportItemToCollectionIfMissing(
          rouAccountBalanceReportItemCollection,
          undefined,
          null
        );
        expect(expectedResult).toEqual(rouAccountBalanceReportItemCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
