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
import { IXlsxReportRequisition, XlsxReportRequisition } from '../xlsx-report-requisition.model';

import { XlsxReportRequisitionService } from './xlsx-report-requisition.service';

describe('XlsxReportRequisition Service', () => {
  let service: XlsxReportRequisitionService;
  let httpMock: HttpTestingController;
  let elemDefault: IXlsxReportRequisition;
  let expectedResult: IXlsxReportRequisition | IXlsxReportRequisition[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(XlsxReportRequisitionService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      reportName: 'AAAAAAA',
      reportDate: currentDate,
      userPassword: 'AAAAAAA',
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

    it('should create a XlsxReportRequisition', () => {
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

      service.create(new XlsxReportRequisition()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a XlsxReportRequisition', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          reportName: 'BBBBBB',
          reportDate: currentDate.format(DATE_FORMAT),
          userPassword: 'BBBBBB',
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

    it('should partial update a XlsxReportRequisition', () => {
      const patchObject = Object.assign(
        {
          reportDate: currentDate.format(DATE_FORMAT),
          reportId: 'BBBBBB',
        },
        new XlsxReportRequisition()
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

    it('should return a list of XlsxReportRequisition', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          reportName: 'BBBBBB',
          reportDate: currentDate.format(DATE_FORMAT),
          userPassword: 'BBBBBB',
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

    it('should delete a XlsxReportRequisition', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addXlsxReportRequisitionToCollectionIfMissing', () => {
      it('should add a XlsxReportRequisition to an empty array', () => {
        const xlsxReportRequisition: IXlsxReportRequisition = { id: 123 };
        expectedResult = service.addXlsxReportRequisitionToCollectionIfMissing([], xlsxReportRequisition);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(xlsxReportRequisition);
      });

      it('should not add a XlsxReportRequisition to an array that contains it', () => {
        const xlsxReportRequisition: IXlsxReportRequisition = { id: 123 };
        const xlsxReportRequisitionCollection: IXlsxReportRequisition[] = [
          {
            ...xlsxReportRequisition,
          },
          { id: 456 },
        ];
        expectedResult = service.addXlsxReportRequisitionToCollectionIfMissing(xlsxReportRequisitionCollection, xlsxReportRequisition);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a XlsxReportRequisition to an array that doesn't contain it", () => {
        const xlsxReportRequisition: IXlsxReportRequisition = { id: 123 };
        const xlsxReportRequisitionCollection: IXlsxReportRequisition[] = [{ id: 456 }];
        expectedResult = service.addXlsxReportRequisitionToCollectionIfMissing(xlsxReportRequisitionCollection, xlsxReportRequisition);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(xlsxReportRequisition);
      });

      it('should add only unique XlsxReportRequisition to an array', () => {
        const xlsxReportRequisitionArray: IXlsxReportRequisition[] = [{ id: 123 }, { id: 456 }, { id: 66603 }];
        const xlsxReportRequisitionCollection: IXlsxReportRequisition[] = [{ id: 123 }];
        expectedResult = service.addXlsxReportRequisitionToCollectionIfMissing(
          xlsxReportRequisitionCollection,
          ...xlsxReportRequisitionArray
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const xlsxReportRequisition: IXlsxReportRequisition = { id: 123 };
        const xlsxReportRequisition2: IXlsxReportRequisition = { id: 456 };
        expectedResult = service.addXlsxReportRequisitionToCollectionIfMissing([], xlsxReportRequisition, xlsxReportRequisition2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(xlsxReportRequisition);
        expect(expectedResult).toContain(xlsxReportRequisition2);
      });

      it('should accept null and undefined values', () => {
        const xlsxReportRequisition: IXlsxReportRequisition = { id: 123 };
        expectedResult = service.addXlsxReportRequisitionToCollectionIfMissing([], null, xlsxReportRequisition, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(xlsxReportRequisition);
      });

      it('should return initial array if no XlsxReportRequisition is added', () => {
        const xlsxReportRequisitionCollection: IXlsxReportRequisition[] = [{ id: 123 }];
        expectedResult = service.addXlsxReportRequisitionToCollectionIfMissing(xlsxReportRequisitionCollection, undefined, null);
        expect(expectedResult).toEqual(xlsxReportRequisitionCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
