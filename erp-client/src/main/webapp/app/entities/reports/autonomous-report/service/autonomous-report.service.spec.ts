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
import { IAutonomousReport } from '../autonomous-report.model';

import { AutonomousReportService } from './autonomous-report.service';

describe('AutonomousReport Service', () => {
  let service: AutonomousReportService;
  let httpMock: HttpTestingController;
  let elemDefault: IAutonomousReport;
  let expectedResult: IAutonomousReport | IAutonomousReport[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(AutonomousReportService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      reportName: 'AAAAAAA',
      reportParameters: 'AAAAAAA',
      createdAt: currentDate,
      reportFileContentType: 'image/png',
      reportFile: 'AAAAAAA',
      reportTampered: false,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          createdAt: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should return a list of AutonomousReport', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          reportName: 'BBBBBB',
          reportParameters: 'BBBBBB',
          createdAt: currentDate.format(DATE_TIME_FORMAT),
          reportFile: 'BBBBBB',
          reportTampered: true,
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          createdAt: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    describe('addAutonomousReportToCollectionIfMissing', () => {
      it('should add a AutonomousReport to an empty array', () => {
        const autonomousReport: IAutonomousReport = { id: 123 };
        expectedResult = service.addAutonomousReportToCollectionIfMissing([], autonomousReport);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(autonomousReport);
      });

      it('should not add a AutonomousReport to an array that contains it', () => {
        const autonomousReport: IAutonomousReport = { id: 123 };
        const autonomousReportCollection: IAutonomousReport[] = [
          {
            ...autonomousReport,
          },
          { id: 456 },
        ];
        expectedResult = service.addAutonomousReportToCollectionIfMissing(autonomousReportCollection, autonomousReport);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a AutonomousReport to an array that doesn't contain it", () => {
        const autonomousReport: IAutonomousReport = { id: 123 };
        const autonomousReportCollection: IAutonomousReport[] = [{ id: 456 }];
        expectedResult = service.addAutonomousReportToCollectionIfMissing(autonomousReportCollection, autonomousReport);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(autonomousReport);
      });

      it('should add only unique AutonomousReport to an array', () => {
        const autonomousReportArray: IAutonomousReport[] = [{ id: 123 }, { id: 456 }, { id: 2610 }];
        const autonomousReportCollection: IAutonomousReport[] = [{ id: 123 }];
        expectedResult = service.addAutonomousReportToCollectionIfMissing(autonomousReportCollection, ...autonomousReportArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const autonomousReport: IAutonomousReport = { id: 123 };
        const autonomousReport2: IAutonomousReport = { id: 456 };
        expectedResult = service.addAutonomousReportToCollectionIfMissing([], autonomousReport, autonomousReport2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(autonomousReport);
        expect(expectedResult).toContain(autonomousReport2);
      });

      it('should accept null and undefined values', () => {
        const autonomousReport: IAutonomousReport = { id: 123 };
        expectedResult = service.addAutonomousReportToCollectionIfMissing([], null, autonomousReport, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(autonomousReport);
      });

      it('should return initial array if no AutonomousReport is added', () => {
        const autonomousReportCollection: IAutonomousReport[] = [{ id: 123 }];
        expectedResult = service.addAutonomousReportToCollectionIfMissing(autonomousReportCollection, undefined, null);
        expect(expectedResult).toEqual(autonomousReportCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
