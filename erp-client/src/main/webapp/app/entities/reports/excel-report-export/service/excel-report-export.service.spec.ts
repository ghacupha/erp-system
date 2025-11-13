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

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IExcelReportExport, ExcelReportExport } from '../excel-report-export.model';

import { ExcelReportExportService } from './excel-report-export.service';

describe('ExcelReportExport Service', () => {
  let service: ExcelReportExportService;
  let httpMock: HttpTestingController;
  let elemDefault: IExcelReportExport;
  let expectedResult: IExcelReportExport | IExcelReportExport[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ExcelReportExportService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      reportName: 'AAAAAAA',
      reportPassword: 'AAAAAAA',
      reportNotesContentType: 'image/png',
      reportNotes: 'AAAAAAA',
      fileCheckSum: 'AAAAAAA',
      reportFileContentType: 'image/png',
      reportFile: 'AAAAAAA',
      reportTimeStamp: currentDate,
      reportId: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          reportTimeStamp: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a ExcelReportExport', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          reportTimeStamp: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          reportTimeStamp: currentDate,
        },
        returnedFromService
      );

      service.create(new ExcelReportExport()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ExcelReportExport', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          reportName: 'BBBBBB',
          reportPassword: 'BBBBBB',
          reportNotes: 'BBBBBB',
          fileCheckSum: 'BBBBBB',
          reportFile: 'BBBBBB',
          reportTimeStamp: currentDate.format(DATE_TIME_FORMAT),
          reportId: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          reportTimeStamp: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ExcelReportExport', () => {
      const patchObject = Object.assign(
        {
          reportPassword: 'BBBBBB',
          reportNotes: 'BBBBBB',
          reportFile: 'BBBBBB',
          reportId: 'BBBBBB',
        },
        new ExcelReportExport()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          reportTimeStamp: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ExcelReportExport', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          reportName: 'BBBBBB',
          reportPassword: 'BBBBBB',
          reportNotes: 'BBBBBB',
          fileCheckSum: 'BBBBBB',
          reportFile: 'BBBBBB',
          reportTimeStamp: currentDate.format(DATE_TIME_FORMAT),
          reportId: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          reportTimeStamp: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a ExcelReportExport', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addExcelReportExportToCollectionIfMissing', () => {
      it('should add a ExcelReportExport to an empty array', () => {
        const excelReportExport: IExcelReportExport = { id: 123 };
        expectedResult = service.addExcelReportExportToCollectionIfMissing([], excelReportExport);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(excelReportExport);
      });

      it('should not add a ExcelReportExport to an array that contains it', () => {
        const excelReportExport: IExcelReportExport = { id: 123 };
        const excelReportExportCollection: IExcelReportExport[] = [
          {
            ...excelReportExport,
          },
          { id: 456 },
        ];
        expectedResult = service.addExcelReportExportToCollectionIfMissing(excelReportExportCollection, excelReportExport);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ExcelReportExport to an array that doesn't contain it", () => {
        const excelReportExport: IExcelReportExport = { id: 123 };
        const excelReportExportCollection: IExcelReportExport[] = [{ id: 456 }];
        expectedResult = service.addExcelReportExportToCollectionIfMissing(excelReportExportCollection, excelReportExport);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(excelReportExport);
      });

      it('should add only unique ExcelReportExport to an array', () => {
        const excelReportExportArray: IExcelReportExport[] = [{ id: 123 }, { id: 456 }, { id: 67168 }];
        const excelReportExportCollection: IExcelReportExport[] = [{ id: 123 }];
        expectedResult = service.addExcelReportExportToCollectionIfMissing(excelReportExportCollection, ...excelReportExportArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const excelReportExport: IExcelReportExport = { id: 123 };
        const excelReportExport2: IExcelReportExport = { id: 456 };
        expectedResult = service.addExcelReportExportToCollectionIfMissing([], excelReportExport, excelReportExport2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(excelReportExport);
        expect(expectedResult).toContain(excelReportExport2);
      });

      it('should accept null and undefined values', () => {
        const excelReportExport: IExcelReportExport = { id: 123 };
        expectedResult = service.addExcelReportExportToCollectionIfMissing([], null, excelReportExport, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(excelReportExport);
      });

      it('should return initial array if no ExcelReportExport is added', () => {
        const excelReportExportCollection: IExcelReportExport[] = [{ id: 123 }];
        expectedResult = service.addExcelReportExportToCollectionIfMissing(excelReportExportCollection, undefined, null);
        expect(expectedResult).toEqual(excelReportExportCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
