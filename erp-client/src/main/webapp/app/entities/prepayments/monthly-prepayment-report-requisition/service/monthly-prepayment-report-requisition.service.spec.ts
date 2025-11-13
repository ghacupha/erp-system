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
import { IMonthlyPrepaymentReportRequisition, MonthlyPrepaymentReportRequisition } from '../monthly-prepayment-report-requisition.model';

import { MonthlyPrepaymentReportRequisitionService } from './monthly-prepayment-report-requisition.service';

describe('MonthlyPrepaymentReportRequisition Service', () => {
  let service: MonthlyPrepaymentReportRequisitionService;
  let httpMock: HttpTestingController;
  let elemDefault: IMonthlyPrepaymentReportRequisition;
  let expectedResult: IMonthlyPrepaymentReportRequisition | IMonthlyPrepaymentReportRequisition[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(MonthlyPrepaymentReportRequisitionService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      requestId: 'AAAAAAA',
      timeOfRequisition: currentDate,
      fileChecksum: 'AAAAAAA',
      filename: 'AAAAAAA',
      reportParameters: 'AAAAAAA',
      reportFileContentType: 'image/png',
      reportFile: 'AAAAAAA',
      tampered: false,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          timeOfRequisition: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a MonthlyPrepaymentReportRequisition', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          timeOfRequisition: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          timeOfRequisition: currentDate,
        },
        returnedFromService
      );

      service.create(new MonthlyPrepaymentReportRequisition()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a MonthlyPrepaymentReportRequisition', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          requestId: 'BBBBBB',
          timeOfRequisition: currentDate.format(DATE_TIME_FORMAT),
          fileChecksum: 'BBBBBB',
          filename: 'BBBBBB',
          reportParameters: 'BBBBBB',
          reportFile: 'BBBBBB',
          tampered: true,
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          timeOfRequisition: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a MonthlyPrepaymentReportRequisition', () => {
      const patchObject = Object.assign(
        {
          timeOfRequisition: currentDate.format(DATE_TIME_FORMAT),
          filename: 'BBBBBB',
          reportParameters: 'BBBBBB',
          reportFile: 'BBBBBB',
          tampered: true,
        },
        new MonthlyPrepaymentReportRequisition()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          timeOfRequisition: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of MonthlyPrepaymentReportRequisition', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          requestId: 'BBBBBB',
          timeOfRequisition: currentDate.format(DATE_TIME_FORMAT),
          fileChecksum: 'BBBBBB',
          filename: 'BBBBBB',
          reportParameters: 'BBBBBB',
          reportFile: 'BBBBBB',
          tampered: true,
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          timeOfRequisition: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a MonthlyPrepaymentReportRequisition', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addMonthlyPrepaymentReportRequisitionToCollectionIfMissing', () => {
      it('should add a MonthlyPrepaymentReportRequisition to an empty array', () => {
        const monthlyPrepaymentReportRequisition: IMonthlyPrepaymentReportRequisition = { id: 123 };
        expectedResult = service.addMonthlyPrepaymentReportRequisitionToCollectionIfMissing([], monthlyPrepaymentReportRequisition);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(monthlyPrepaymentReportRequisition);
      });

      it('should not add a MonthlyPrepaymentReportRequisition to an array that contains it', () => {
        const monthlyPrepaymentReportRequisition: IMonthlyPrepaymentReportRequisition = { id: 123 };
        const monthlyPrepaymentReportRequisitionCollection: IMonthlyPrepaymentReportRequisition[] = [
          {
            ...monthlyPrepaymentReportRequisition,
          },
          { id: 456 },
        ];
        expectedResult = service.addMonthlyPrepaymentReportRequisitionToCollectionIfMissing(
          monthlyPrepaymentReportRequisitionCollection,
          monthlyPrepaymentReportRequisition
        );
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a MonthlyPrepaymentReportRequisition to an array that doesn't contain it", () => {
        const monthlyPrepaymentReportRequisition: IMonthlyPrepaymentReportRequisition = { id: 123 };
        const monthlyPrepaymentReportRequisitionCollection: IMonthlyPrepaymentReportRequisition[] = [{ id: 456 }];
        expectedResult = service.addMonthlyPrepaymentReportRequisitionToCollectionIfMissing(
          monthlyPrepaymentReportRequisitionCollection,
          monthlyPrepaymentReportRequisition
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(monthlyPrepaymentReportRequisition);
      });

      it('should add only unique MonthlyPrepaymentReportRequisition to an array', () => {
        const monthlyPrepaymentReportRequisitionArray: IMonthlyPrepaymentReportRequisition[] = [{ id: 123 }, { id: 456 }, { id: 7947 }];
        const monthlyPrepaymentReportRequisitionCollection: IMonthlyPrepaymentReportRequisition[] = [{ id: 123 }];
        expectedResult = service.addMonthlyPrepaymentReportRequisitionToCollectionIfMissing(
          monthlyPrepaymentReportRequisitionCollection,
          ...monthlyPrepaymentReportRequisitionArray
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const monthlyPrepaymentReportRequisition: IMonthlyPrepaymentReportRequisition = { id: 123 };
        const monthlyPrepaymentReportRequisition2: IMonthlyPrepaymentReportRequisition = { id: 456 };
        expectedResult = service.addMonthlyPrepaymentReportRequisitionToCollectionIfMissing(
          [],
          monthlyPrepaymentReportRequisition,
          monthlyPrepaymentReportRequisition2
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(monthlyPrepaymentReportRequisition);
        expect(expectedResult).toContain(monthlyPrepaymentReportRequisition2);
      });

      it('should accept null and undefined values', () => {
        const monthlyPrepaymentReportRequisition: IMonthlyPrepaymentReportRequisition = { id: 123 };
        expectedResult = service.addMonthlyPrepaymentReportRequisitionToCollectionIfMissing(
          [],
          null,
          monthlyPrepaymentReportRequisition,
          undefined
        );
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(monthlyPrepaymentReportRequisition);
      });

      it('should return initial array if no MonthlyPrepaymentReportRequisition is added', () => {
        const monthlyPrepaymentReportRequisitionCollection: IMonthlyPrepaymentReportRequisition[] = [{ id: 123 }];
        expectedResult = service.addMonthlyPrepaymentReportRequisitionToCollectionIfMissing(
          monthlyPrepaymentReportRequisitionCollection,
          undefined,
          null
        );
        expect(expectedResult).toEqual(monthlyPrepaymentReportRequisitionCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
