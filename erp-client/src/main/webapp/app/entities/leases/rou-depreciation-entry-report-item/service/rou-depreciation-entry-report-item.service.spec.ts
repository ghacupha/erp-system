import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IRouDepreciationEntryReportItem } from '../rou-depreciation-entry-report-item.model';

import { RouDepreciationEntryReportItemService } from './rou-depreciation-entry-report-item.service';

describe('RouDepreciationEntryReportItem Service', () => {
  let service: RouDepreciationEntryReportItemService;
  let httpMock: HttpTestingController;
  let elemDefault: IRouDepreciationEntryReportItem;
  let expectedResult: IRouDepreciationEntryReportItem | IRouDepreciationEntryReportItem[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(RouDepreciationEntryReportItemService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      leaseContractNumber: 'AAAAAAA',
      fiscalPeriodCode: 'AAAAAAA',
      fiscalPeriodEndDate: currentDate,
      assetCategoryName: 'AAAAAAA',
      debitAccountNumber: 'AAAAAAA',
      creditAccountNumber: 'AAAAAAA',
      description: 'AAAAAAA',
      shortTitle: 'AAAAAAA',
      rouAssetIdentifier: 'AAAAAAA',
      sequenceNumber: 0,
      depreciationAmount: 0,
      outstandingAmount: 0,
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

    it('should return a list of RouDepreciationEntryReportItem', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          leaseContractNumber: 'BBBBBB',
          fiscalPeriodCode: 'BBBBBB',
          fiscalPeriodEndDate: currentDate.format(DATE_FORMAT),
          assetCategoryName: 'BBBBBB',
          debitAccountNumber: 'BBBBBB',
          creditAccountNumber: 'BBBBBB',
          description: 'BBBBBB',
          shortTitle: 'BBBBBB',
          rouAssetIdentifier: 'BBBBBB',
          sequenceNumber: 1,
          depreciationAmount: 1,
          outstandingAmount: 1,
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

    describe('addRouDepreciationEntryReportItemToCollectionIfMissing', () => {
      it('should add a RouDepreciationEntryReportItem to an empty array', () => {
        const rouDepreciationEntryReportItem: IRouDepreciationEntryReportItem = { id: 123 };
        expectedResult = service.addRouDepreciationEntryReportItemToCollectionIfMissing([], rouDepreciationEntryReportItem);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(rouDepreciationEntryReportItem);
      });

      it('should not add a RouDepreciationEntryReportItem to an array that contains it', () => {
        const rouDepreciationEntryReportItem: IRouDepreciationEntryReportItem = { id: 123 };
        const rouDepreciationEntryReportItemCollection: IRouDepreciationEntryReportItem[] = [
          {
            ...rouDepreciationEntryReportItem,
          },
          { id: 456 },
        ];
        expectedResult = service.addRouDepreciationEntryReportItemToCollectionIfMissing(
          rouDepreciationEntryReportItemCollection,
          rouDepreciationEntryReportItem
        );
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a RouDepreciationEntryReportItem to an array that doesn't contain it", () => {
        const rouDepreciationEntryReportItem: IRouDepreciationEntryReportItem = { id: 123 };
        const rouDepreciationEntryReportItemCollection: IRouDepreciationEntryReportItem[] = [{ id: 456 }];
        expectedResult = service.addRouDepreciationEntryReportItemToCollectionIfMissing(
          rouDepreciationEntryReportItemCollection,
          rouDepreciationEntryReportItem
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(rouDepreciationEntryReportItem);
      });

      it('should add only unique RouDepreciationEntryReportItem to an array', () => {
        const rouDepreciationEntryReportItemArray: IRouDepreciationEntryReportItem[] = [{ id: 123 }, { id: 456 }, { id: 82887 }];
        const rouDepreciationEntryReportItemCollection: IRouDepreciationEntryReportItem[] = [{ id: 123 }];
        expectedResult = service.addRouDepreciationEntryReportItemToCollectionIfMissing(
          rouDepreciationEntryReportItemCollection,
          ...rouDepreciationEntryReportItemArray
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const rouDepreciationEntryReportItem: IRouDepreciationEntryReportItem = { id: 123 };
        const rouDepreciationEntryReportItem2: IRouDepreciationEntryReportItem = { id: 456 };
        expectedResult = service.addRouDepreciationEntryReportItemToCollectionIfMissing(
          [],
          rouDepreciationEntryReportItem,
          rouDepreciationEntryReportItem2
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(rouDepreciationEntryReportItem);
        expect(expectedResult).toContain(rouDepreciationEntryReportItem2);
      });

      it('should accept null and undefined values', () => {
        const rouDepreciationEntryReportItem: IRouDepreciationEntryReportItem = { id: 123 };
        expectedResult = service.addRouDepreciationEntryReportItemToCollectionIfMissing(
          [],
          null,
          rouDepreciationEntryReportItem,
          undefined
        );
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(rouDepreciationEntryReportItem);
      });

      it('should return initial array if no RouDepreciationEntryReportItem is added', () => {
        const rouDepreciationEntryReportItemCollection: IRouDepreciationEntryReportItem[] = [{ id: 123 }];
        expectedResult = service.addRouDepreciationEntryReportItemToCollectionIfMissing(
          rouDepreciationEntryReportItemCollection,
          undefined,
          null
        );
        expect(expectedResult).toEqual(rouDepreciationEntryReportItemCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
