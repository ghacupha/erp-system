///
/// Erp System - Mark X No 11 (Jehoiada Series) Client 1.7.9
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

import { DATE_FORMAT } from 'app/config/input.constants';
import { ReportStatusTypes } from 'app/entities/enumerations/report-status-types.model';
import { IPdfReportRequisition, PdfReportRequisition } from '../pdf-report-requisition.model';

import { PdfReportRequisitionService } from './pdf-report-requisition.service';

describe('PdfReportRequisition Service', () => {
  let service: PdfReportRequisitionService;
  let httpMock: HttpTestingController;
  let elemDefault: IPdfReportRequisition;
  let expectedResult: IPdfReportRequisition | IPdfReportRequisition[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(PdfReportRequisitionService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      reportName: 'AAAAAAA',
      reportDate: currentDate,
      userPassword: 'AAAAAAA',
      ownerPassword: 'AAAAAAA',
      reportFileChecksum: 'AAAAAAA',
      reportStatus: ReportStatusTypes.GENERATING,
      reportId: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          reportDate: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a PdfReportRequisition', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          reportDate: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          reportDate: currentDate,
        },
        returnedFromService
      );

      service.create(new PdfReportRequisition()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a PdfReportRequisition', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          reportName: 'BBBBBB',
          reportDate: currentDate.format(DATE_FORMAT),
          userPassword: 'BBBBBB',
          ownerPassword: 'BBBBBB',
          reportFileChecksum: 'BBBBBB',
          reportStatus: 'BBBBBB',
          reportId: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          reportDate: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a PdfReportRequisition', () => {
      const patchObject = Object.assign(
        {
          reportName: 'BBBBBB',
          reportDate: currentDate.format(DATE_FORMAT),
          userPassword: 'BBBBBB',
          reportStatus: 'BBBBBB',
        },
        new PdfReportRequisition()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          reportDate: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of PdfReportRequisition', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          reportName: 'BBBBBB',
          reportDate: currentDate.format(DATE_FORMAT),
          userPassword: 'BBBBBB',
          ownerPassword: 'BBBBBB',
          reportFileChecksum: 'BBBBBB',
          reportStatus: 'BBBBBB',
          reportId: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          reportDate: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a PdfReportRequisition', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addPdfReportRequisitionToCollectionIfMissing', () => {
      it('should add a PdfReportRequisition to an empty array', () => {
        const pdfReportRequisition: IPdfReportRequisition = { id: 123 };
        expectedResult = service.addPdfReportRequisitionToCollectionIfMissing([], pdfReportRequisition);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(pdfReportRequisition);
      });

      it('should not add a PdfReportRequisition to an array that contains it', () => {
        const pdfReportRequisition: IPdfReportRequisition = { id: 123 };
        const pdfReportRequisitionCollection: IPdfReportRequisition[] = [
          {
            ...pdfReportRequisition,
          },
          { id: 456 },
        ];
        expectedResult = service.addPdfReportRequisitionToCollectionIfMissing(pdfReportRequisitionCollection, pdfReportRequisition);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a PdfReportRequisition to an array that doesn't contain it", () => {
        const pdfReportRequisition: IPdfReportRequisition = { id: 123 };
        const pdfReportRequisitionCollection: IPdfReportRequisition[] = [{ id: 456 }];
        expectedResult = service.addPdfReportRequisitionToCollectionIfMissing(pdfReportRequisitionCollection, pdfReportRequisition);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(pdfReportRequisition);
      });

      it('should add only unique PdfReportRequisition to an array', () => {
        const pdfReportRequisitionArray: IPdfReportRequisition[] = [{ id: 123 }, { id: 456 }, { id: 19038 }];
        const pdfReportRequisitionCollection: IPdfReportRequisition[] = [{ id: 123 }];
        expectedResult = service.addPdfReportRequisitionToCollectionIfMissing(pdfReportRequisitionCollection, ...pdfReportRequisitionArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const pdfReportRequisition: IPdfReportRequisition = { id: 123 };
        const pdfReportRequisition2: IPdfReportRequisition = { id: 456 };
        expectedResult = service.addPdfReportRequisitionToCollectionIfMissing([], pdfReportRequisition, pdfReportRequisition2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(pdfReportRequisition);
        expect(expectedResult).toContain(pdfReportRequisition2);
      });

      it('should accept null and undefined values', () => {
        const pdfReportRequisition: IPdfReportRequisition = { id: 123 };
        expectedResult = service.addPdfReportRequisitionToCollectionIfMissing([], null, pdfReportRequisition, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(pdfReportRequisition);
      });

      it('should return initial array if no PdfReportRequisition is added', () => {
        const pdfReportRequisitionCollection: IPdfReportRequisition[] = [{ id: 123 }];
        expectedResult = service.addPdfReportRequisitionToCollectionIfMissing(pdfReportRequisitionCollection, undefined, null);
        expect(expectedResult).toEqual(pdfReportRequisitionCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
