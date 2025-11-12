///
/// Erp System - Mark X No 11 (Jehoiada Series) Client 1.7.8
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

import { IReportStatus, ReportStatus } from '../report-status.model';

import { ReportStatusService } from './report-status.service';

describe('ReportStatus Service', () => {
  let service: ReportStatusService;
  let httpMock: HttpTestingController;
  let elemDefault: IReportStatus;
  let expectedResult: IReportStatus | IReportStatus[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ReportStatusService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      reportName: 'AAAAAAA',
      reportId: 'AAAAAAA',
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

    it('should create a ReportStatus', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new ReportStatus()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ReportStatus', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          reportName: 'BBBBBB',
          reportId: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ReportStatus', () => {
      const patchObject = Object.assign(
        {
          reportName: 'BBBBBB',
        },
        new ReportStatus()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ReportStatus', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          reportName: 'BBBBBB',
          reportId: 'BBBBBB',
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

    it('should delete a ReportStatus', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addReportStatusToCollectionIfMissing', () => {
      it('should add a ReportStatus to an empty array', () => {
        const reportStatus: IReportStatus = { id: 123 };
        expectedResult = service.addReportStatusToCollectionIfMissing([], reportStatus);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(reportStatus);
      });

      it('should not add a ReportStatus to an array that contains it', () => {
        const reportStatus: IReportStatus = { id: 123 };
        const reportStatusCollection: IReportStatus[] = [
          {
            ...reportStatus,
          },
          { id: 456 },
        ];
        expectedResult = service.addReportStatusToCollectionIfMissing(reportStatusCollection, reportStatus);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ReportStatus to an array that doesn't contain it", () => {
        const reportStatus: IReportStatus = { id: 123 };
        const reportStatusCollection: IReportStatus[] = [{ id: 456 }];
        expectedResult = service.addReportStatusToCollectionIfMissing(reportStatusCollection, reportStatus);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(reportStatus);
      });

      it('should add only unique ReportStatus to an array', () => {
        const reportStatusArray: IReportStatus[] = [{ id: 123 }, { id: 456 }, { id: 79543 }];
        const reportStatusCollection: IReportStatus[] = [{ id: 123 }];
        expectedResult = service.addReportStatusToCollectionIfMissing(reportStatusCollection, ...reportStatusArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const reportStatus: IReportStatus = { id: 123 };
        const reportStatus2: IReportStatus = { id: 456 };
        expectedResult = service.addReportStatusToCollectionIfMissing([], reportStatus, reportStatus2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(reportStatus);
        expect(expectedResult).toContain(reportStatus2);
      });

      it('should accept null and undefined values', () => {
        const reportStatus: IReportStatus = { id: 123 };
        expectedResult = service.addReportStatusToCollectionIfMissing([], null, reportStatus, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(reportStatus);
      });

      it('should return initial array if no ReportStatus is added', () => {
        const reportStatusCollection: IReportStatus[] = [{ id: 123 }];
        expectedResult = service.addReportStatusToCollectionIfMissing(reportStatusCollection, undefined, null);
        expect(expectedResult).toEqual(reportStatusCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
