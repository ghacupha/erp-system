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
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { INbvReport, NbvReport } from '../nbv-report.model';

import { NbvReportService } from './nbv-report.service';

describe('NbvReport Service', () => {
  let service: NbvReportService;
  let httpMock: HttpTestingController;
  let elemDefault: INbvReport;
  let expectedResult: INbvReport | INbvReport[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(NbvReportService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      reportName: 'AAAAAAA',
      timeOfReportRequest: currentDate,
      fileChecksum: 'AAAAAAA',
      tampered: false,
      filename: 'AAAAAAA',
      reportParameters: 'AAAAAAA',
      reportFileContentType: 'image/png',
      reportFile: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          timeOfReportRequest: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a NbvReport', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          timeOfReportRequest: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          timeOfReportRequest: currentDate,
        },
        returnedFromService
      );

      service.create(new NbvReport()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a NbvReport', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          reportName: 'BBBBBB',
          timeOfReportRequest: currentDate.format(DATE_TIME_FORMAT),
          fileChecksum: 'BBBBBB',
          tampered: true,
          filename: 'BBBBBB',
          reportParameters: 'BBBBBB',
          reportFile: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          timeOfReportRequest: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a NbvReport', () => {
      const patchObject = Object.assign(
        {
          reportName: 'BBBBBB',
          tampered: true,
          filename: 'BBBBBB',
          reportParameters: 'BBBBBB',
          reportFile: 'BBBBBB',
        },
        new NbvReport()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          timeOfReportRequest: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of NbvReport', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          reportName: 'BBBBBB',
          timeOfReportRequest: currentDate.format(DATE_TIME_FORMAT),
          fileChecksum: 'BBBBBB',
          tampered: true,
          filename: 'BBBBBB',
          reportParameters: 'BBBBBB',
          reportFile: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          timeOfReportRequest: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a NbvReport', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addNbvReportToCollectionIfMissing', () => {
      it('should add a NbvReport to an empty array', () => {
        const nbvReport: INbvReport = { id: 123 };
        expectedResult = service.addNbvReportToCollectionIfMissing([], nbvReport);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(nbvReport);
      });

      it('should not add a NbvReport to an array that contains it', () => {
        const nbvReport: INbvReport = { id: 123 };
        const nbvReportCollection: INbvReport[] = [
          {
            ...nbvReport,
          },
          { id: 456 },
        ];
        expectedResult = service.addNbvReportToCollectionIfMissing(nbvReportCollection, nbvReport);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a NbvReport to an array that doesn't contain it", () => {
        const nbvReport: INbvReport = { id: 123 };
        const nbvReportCollection: INbvReport[] = [{ id: 456 }];
        expectedResult = service.addNbvReportToCollectionIfMissing(nbvReportCollection, nbvReport);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(nbvReport);
      });

      it('should add only unique NbvReport to an array', () => {
        const nbvReportArray: INbvReport[] = [{ id: 123 }, { id: 456 }, { id: 47689 }];
        const nbvReportCollection: INbvReport[] = [{ id: 123 }];
        expectedResult = service.addNbvReportToCollectionIfMissing(nbvReportCollection, ...nbvReportArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const nbvReport: INbvReport = { id: 123 };
        const nbvReport2: INbvReport = { id: 456 };
        expectedResult = service.addNbvReportToCollectionIfMissing([], nbvReport, nbvReport2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(nbvReport);
        expect(expectedResult).toContain(nbvReport2);
      });

      it('should accept null and undefined values', () => {
        const nbvReport: INbvReport = { id: 123 };
        expectedResult = service.addNbvReportToCollectionIfMissing([], null, nbvReport, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(nbvReport);
      });

      it('should return initial array if no NbvReport is added', () => {
        const nbvReportCollection: INbvReport[] = [{ id: 123 }];
        expectedResult = service.addNbvReportToCollectionIfMissing(nbvReportCollection, undefined, null);
        expect(expectedResult).toEqual(nbvReportCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
