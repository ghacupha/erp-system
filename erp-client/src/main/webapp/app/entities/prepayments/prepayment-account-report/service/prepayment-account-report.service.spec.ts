import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IPrepaymentAccountReport } from '../prepayment-account-report.model';

import { PrepaymentAccountReportService } from './prepayment-account-report.service';

describe('PrepaymentAccountReport Service', () => {
  let service: PrepaymentAccountReportService;
  let httpMock: HttpTestingController;
  let elemDefault: IPrepaymentAccountReport;
  let expectedResult: IPrepaymentAccountReport | IPrepaymentAccountReport[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(PrepaymentAccountReportService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      accountName: 'AAAAAAA',
      accountNumber: 'AAAAAAA',
      prepaymentAmount: 0,
      amortisedAmount: 0,
      outstandingAmount: 0,
      numberOfPrepaymentAccounts: 0,
      numberOfAmortisedItems: 0,
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

    it('should return a list of PrepaymentAccountReport', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          accountName: 'BBBBBB',
          accountNumber: 'BBBBBB',
          prepaymentAmount: 1,
          amortisedAmount: 1,
          outstandingAmount: 1,
          numberOfPrepaymentAccounts: 1,
          numberOfAmortisedItems: 1,
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

    describe('addPrepaymentAccountReportToCollectionIfMissing', () => {
      it('should add a PrepaymentAccountReport to an empty array', () => {
        const prepaymentAccountReport: IPrepaymentAccountReport = { id: 123 };
        expectedResult = service.addPrepaymentAccountReportToCollectionIfMissing([], prepaymentAccountReport);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(prepaymentAccountReport);
      });

      it('should not add a PrepaymentAccountReport to an array that contains it', () => {
        const prepaymentAccountReport: IPrepaymentAccountReport = { id: 123 };
        const prepaymentAccountReportCollection: IPrepaymentAccountReport[] = [
          {
            ...prepaymentAccountReport,
          },
          { id: 456 },
        ];
        expectedResult = service.addPrepaymentAccountReportToCollectionIfMissing(
          prepaymentAccountReportCollection,
          prepaymentAccountReport
        );
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a PrepaymentAccountReport to an array that doesn't contain it", () => {
        const prepaymentAccountReport: IPrepaymentAccountReport = { id: 123 };
        const prepaymentAccountReportCollection: IPrepaymentAccountReport[] = [{ id: 456 }];
        expectedResult = service.addPrepaymentAccountReportToCollectionIfMissing(
          prepaymentAccountReportCollection,
          prepaymentAccountReport
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(prepaymentAccountReport);
      });

      it('should add only unique PrepaymentAccountReport to an array', () => {
        const prepaymentAccountReportArray: IPrepaymentAccountReport[] = [{ id: 123 }, { id: 456 }, { id: 50660 }];
        const prepaymentAccountReportCollection: IPrepaymentAccountReport[] = [{ id: 123 }];
        expectedResult = service.addPrepaymentAccountReportToCollectionIfMissing(
          prepaymentAccountReportCollection,
          ...prepaymentAccountReportArray
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const prepaymentAccountReport: IPrepaymentAccountReport = { id: 123 };
        const prepaymentAccountReport2: IPrepaymentAccountReport = { id: 456 };
        expectedResult = service.addPrepaymentAccountReportToCollectionIfMissing([], prepaymentAccountReport, prepaymentAccountReport2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(prepaymentAccountReport);
        expect(expectedResult).toContain(prepaymentAccountReport2);
      });

      it('should accept null and undefined values', () => {
        const prepaymentAccountReport: IPrepaymentAccountReport = { id: 123 };
        expectedResult = service.addPrepaymentAccountReportToCollectionIfMissing([], null, prepaymentAccountReport, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(prepaymentAccountReport);
      });

      it('should return initial array if no PrepaymentAccountReport is added', () => {
        const prepaymentAccountReportCollection: IPrepaymentAccountReport[] = [{ id: 123 }];
        expectedResult = service.addPrepaymentAccountReportToCollectionIfMissing(prepaymentAccountReportCollection, undefined, null);
        expect(expectedResult).toEqual(prepaymentAccountReportCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
