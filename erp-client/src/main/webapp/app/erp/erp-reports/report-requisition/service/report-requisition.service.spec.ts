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
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IReportRequisition, ReportRequisition } from '../report-requisition.model';

import { ReportRequisitionService } from './report-requisition.service';
import { ReportStatusTypes } from '../../../erp-common/enumerations/report-status-types.model';

describe('ReportRequisition Service', () => {
  let service: ReportRequisitionService;
  let httpMock: HttpTestingController;
  let elemDefault: IReportRequisition;
  let expectedResult: IReportRequisition | IReportRequisition[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ReportRequisitionService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      reportName: 'AAAAAAA',
      reportRequestTime: currentDate,
      reportPassword: 'AAAAAAA',
      reportStatus: ReportStatusTypes.GENERATING,
      reportId: 'AAAAAAA',
      reportFileAttachmentContentType: 'image/png',
      reportFileAttachment: 'AAAAAAA',
      reportFileCheckSum: 'AAAAAAA',
      reportNotesContentType: 'image/png',
      reportNotes: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          reportRequestTime: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a ReportRequisition', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          reportRequestTime: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          reportRequestTime: currentDate,
        },
        returnedFromService
      );

      service.create(new ReportRequisition()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ReportRequisition', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          reportName: 'BBBBBB',
          reportRequestTime: currentDate.format(DATE_TIME_FORMAT),
          reportPassword: 'BBBBBB',
          reportStatus: 'BBBBBB',
          reportId: 'BBBBBB',
          reportFileAttachment: 'BBBBBB',
          reportFileCheckSum: 'BBBBBB',
          reportNotes: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          reportRequestTime: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ReportRequisition', () => {
      const patchObject = Object.assign(
        {
          reportName: 'BBBBBB',
          reportRequestTime: currentDate.format(DATE_TIME_FORMAT),
          reportPassword: 'BBBBBB',
          reportStatus: 'BBBBBB',
          reportId: 'BBBBBB',
          reportFileCheckSum: 'BBBBBB',
          reportNotes: 'BBBBBB',
        },
        new ReportRequisition()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          reportRequestTime: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ReportRequisition', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          reportName: 'BBBBBB',
          reportRequestTime: currentDate.format(DATE_TIME_FORMAT),
          reportPassword: 'BBBBBB',
          reportStatus: 'BBBBBB',
          reportId: 'BBBBBB',
          reportFileAttachment: 'BBBBBB',
          reportFileCheckSum: 'BBBBBB',
          reportNotes: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          reportRequestTime: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a ReportRequisition', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addReportRequisitionToCollectionIfMissing', () => {
      it('should add a ReportRequisition to an empty array', () => {
        const reportRequisition: IReportRequisition = { id: 123 };
        expectedResult = service.addReportRequisitionToCollectionIfMissing([], reportRequisition);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(reportRequisition);
      });

      it('should not add a ReportRequisition to an array that contains it', () => {
        const reportRequisition: IReportRequisition = { id: 123 };
        const reportRequisitionCollection: IReportRequisition[] = [
          {
            ...reportRequisition,
          },
          { id: 456 },
        ];
        expectedResult = service.addReportRequisitionToCollectionIfMissing(reportRequisitionCollection, reportRequisition);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ReportRequisition to an array that doesn't contain it", () => {
        const reportRequisition: IReportRequisition = { id: 123 };
        const reportRequisitionCollection: IReportRequisition[] = [{ id: 456 }];
        expectedResult = service.addReportRequisitionToCollectionIfMissing(reportRequisitionCollection, reportRequisition);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(reportRequisition);
      });

      it('should add only unique ReportRequisition to an array', () => {
        const reportRequisitionArray: IReportRequisition[] = [{ id: 123 }, { id: 456 }, { id: 2427 }];
        const reportRequisitionCollection: IReportRequisition[] = [{ id: 123 }];
        expectedResult = service.addReportRequisitionToCollectionIfMissing(reportRequisitionCollection, ...reportRequisitionArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const reportRequisition: IReportRequisition = { id: 123 };
        const reportRequisition2: IReportRequisition = { id: 456 };
        expectedResult = service.addReportRequisitionToCollectionIfMissing([], reportRequisition, reportRequisition2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(reportRequisition);
        expect(expectedResult).toContain(reportRequisition2);
      });

      it('should accept null and undefined values', () => {
        const reportRequisition: IReportRequisition = { id: 123 };
        expectedResult = service.addReportRequisitionToCollectionIfMissing([], null, reportRequisition, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(reportRequisition);
      });

      it('should return initial array if no ReportRequisition is added', () => {
        const reportRequisitionCollection: IReportRequisition[] = [{ id: 123 }];
        expectedResult = service.addReportRequisitionToCollectionIfMissing(reportRequisitionCollection, undefined, null);
        expect(expectedResult).toEqual(reportRequisitionCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
