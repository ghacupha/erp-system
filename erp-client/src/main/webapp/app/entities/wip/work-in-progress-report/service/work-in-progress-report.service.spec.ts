import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IWorkInProgressReport } from '../work-in-progress-report.model';

import { WorkInProgressReportService } from './work-in-progress-report.service';

describe('WorkInProgressReport Service', () => {
  let service: WorkInProgressReportService;
  let httpMock: HttpTestingController;
  let elemDefault: IWorkInProgressReport;
  let expectedResult: IWorkInProgressReport | IWorkInProgressReport[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(WorkInProgressReportService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      projectTitle: 'AAAAAAA',
      dealerName: 'AAAAAAA',
      numberOfItems: 0,
      instalmentAmount: 0,
      transferAmount: 0,
      outstandingAmount: 0,
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

    it('should return a list of WorkInProgressReport', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          projectTitle: 'BBBBBB',
          dealerName: 'BBBBBB',
          numberOfItems: 1,
          instalmentAmount: 1,
          transferAmount: 1,
          outstandingAmount: 1,
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

    describe('addWorkInProgressReportToCollectionIfMissing', () => {
      it('should add a WorkInProgressReport to an empty array', () => {
        const workInProgressReport: IWorkInProgressReport = { id: 123 };
        expectedResult = service.addWorkInProgressReportToCollectionIfMissing([], workInProgressReport);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(workInProgressReport);
      });

      it('should not add a WorkInProgressReport to an array that contains it', () => {
        const workInProgressReport: IWorkInProgressReport = { id: 123 };
        const workInProgressReportCollection: IWorkInProgressReport[] = [
          {
            ...workInProgressReport,
          },
          { id: 456 },
        ];
        expectedResult = service.addWorkInProgressReportToCollectionIfMissing(workInProgressReportCollection, workInProgressReport);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a WorkInProgressReport to an array that doesn't contain it", () => {
        const workInProgressReport: IWorkInProgressReport = { id: 123 };
        const workInProgressReportCollection: IWorkInProgressReport[] = [{ id: 456 }];
        expectedResult = service.addWorkInProgressReportToCollectionIfMissing(workInProgressReportCollection, workInProgressReport);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(workInProgressReport);
      });

      it('should add only unique WorkInProgressReport to an array', () => {
        const workInProgressReportArray: IWorkInProgressReport[] = [{ id: 123 }, { id: 456 }, { id: 35636 }];
        const workInProgressReportCollection: IWorkInProgressReport[] = [{ id: 123 }];
        expectedResult = service.addWorkInProgressReportToCollectionIfMissing(workInProgressReportCollection, ...workInProgressReportArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const workInProgressReport: IWorkInProgressReport = { id: 123 };
        const workInProgressReport2: IWorkInProgressReport = { id: 456 };
        expectedResult = service.addWorkInProgressReportToCollectionIfMissing([], workInProgressReport, workInProgressReport2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(workInProgressReport);
        expect(expectedResult).toContain(workInProgressReport2);
      });

      it('should accept null and undefined values', () => {
        const workInProgressReport: IWorkInProgressReport = { id: 123 };
        expectedResult = service.addWorkInProgressReportToCollectionIfMissing([], null, workInProgressReport, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(workInProgressReport);
      });

      it('should return initial array if no WorkInProgressReport is added', () => {
        const workInProgressReportCollection: IWorkInProgressReport[] = [{ id: 123 }];
        expectedResult = service.addWorkInProgressReportToCollectionIfMissing(workInProgressReportCollection, undefined, null);
        expect(expectedResult).toEqual(workInProgressReportCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
