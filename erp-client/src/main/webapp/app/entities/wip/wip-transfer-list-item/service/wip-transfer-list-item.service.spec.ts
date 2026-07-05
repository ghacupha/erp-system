import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IWIPTransferListItem } from '../wip-transfer-list-item.model';

import { WIPTransferListItemService } from './wip-transfer-list-item.service';

describe('WIPTransferListItem Service', () => {
  let service: WIPTransferListItemService;
  let httpMock: HttpTestingController;
  let elemDefault: IWIPTransferListItem;
  let expectedResult: IWIPTransferListItem | IWIPTransferListItem[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(WIPTransferListItemService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      wipSequence: 0,
      wipParticulars: 'AAAAAAA',
      transferType: 'AAAAAAA',
      transferSettlement: 'AAAAAAA',
      transferSettlementDate: currentDate,
      transferAmount: 0,
      wipTransferDate: currentDate,
      originalSettlement: 'AAAAAAA',
      originalSettlementDate: currentDate,
      assetCategory: 'AAAAAAA',
      serviceOutlet: 'AAAAAAA',
      workProject: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          transferSettlementDate: currentDate.format(DATE_FORMAT),
          wipTransferDate: currentDate.format(DATE_FORMAT),
          originalSettlementDate: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should return a list of WIPTransferListItem', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          wipSequence: 1,
          wipParticulars: 'BBBBBB',
          transferType: 'BBBBBB',
          transferSettlement: 'BBBBBB',
          transferSettlementDate: currentDate.format(DATE_FORMAT),
          transferAmount: 1,
          wipTransferDate: currentDate.format(DATE_FORMAT),
          originalSettlement: 'BBBBBB',
          originalSettlementDate: currentDate.format(DATE_FORMAT),
          assetCategory: 'BBBBBB',
          serviceOutlet: 'BBBBBB',
          workProject: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          transferSettlementDate: currentDate,
          wipTransferDate: currentDate,
          originalSettlementDate: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    describe('addWIPTransferListItemToCollectionIfMissing', () => {
      it('should add a WIPTransferListItem to an empty array', () => {
        const wIPTransferListItem: IWIPTransferListItem = { id: 123 };
        expectedResult = service.addWIPTransferListItemToCollectionIfMissing([], wIPTransferListItem);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(wIPTransferListItem);
      });

      it('should not add a WIPTransferListItem to an array that contains it', () => {
        const wIPTransferListItem: IWIPTransferListItem = { id: 123 };
        const wIPTransferListItemCollection: IWIPTransferListItem[] = [
          {
            ...wIPTransferListItem,
          },
          { id: 456 },
        ];
        expectedResult = service.addWIPTransferListItemToCollectionIfMissing(wIPTransferListItemCollection, wIPTransferListItem);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a WIPTransferListItem to an array that doesn't contain it", () => {
        const wIPTransferListItem: IWIPTransferListItem = { id: 123 };
        const wIPTransferListItemCollection: IWIPTransferListItem[] = [{ id: 456 }];
        expectedResult = service.addWIPTransferListItemToCollectionIfMissing(wIPTransferListItemCollection, wIPTransferListItem);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(wIPTransferListItem);
      });

      it('should add only unique WIPTransferListItem to an array', () => {
        const wIPTransferListItemArray: IWIPTransferListItem[] = [{ id: 123 }, { id: 456 }, { id: 94434 }];
        const wIPTransferListItemCollection: IWIPTransferListItem[] = [{ id: 123 }];
        expectedResult = service.addWIPTransferListItemToCollectionIfMissing(wIPTransferListItemCollection, ...wIPTransferListItemArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const wIPTransferListItem: IWIPTransferListItem = { id: 123 };
        const wIPTransferListItem2: IWIPTransferListItem = { id: 456 };
        expectedResult = service.addWIPTransferListItemToCollectionIfMissing([], wIPTransferListItem, wIPTransferListItem2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(wIPTransferListItem);
        expect(expectedResult).toContain(wIPTransferListItem2);
      });

      it('should accept null and undefined values', () => {
        const wIPTransferListItem: IWIPTransferListItem = { id: 123 };
        expectedResult = service.addWIPTransferListItemToCollectionIfMissing([], null, wIPTransferListItem, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(wIPTransferListItem);
      });

      it('should return initial array if no WIPTransferListItem is added', () => {
        const wIPTransferListItemCollection: IWIPTransferListItem[] = [{ id: 123 }];
        expectedResult = service.addWIPTransferListItemToCollectionIfMissing(wIPTransferListItemCollection, undefined, null);
        expect(expectedResult).toEqual(wIPTransferListItemCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
