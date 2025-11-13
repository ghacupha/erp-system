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

import { DATE_FORMAT, DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IPrepaymentReportRequisition, PrepaymentReportRequisition } from '../prepayment-report-requisition.model';

import { PrepaymentReportRequisitionService } from './prepayment-report-requisition.service';

describe('PrepaymentReportRequisition Service', () => {
  let service: PrepaymentReportRequisitionService;
  let httpMock: HttpTestingController;
  let elemDefault: IPrepaymentReportRequisition;
  let expectedResult: IPrepaymentReportRequisition | IPrepaymentReportRequisition[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(PrepaymentReportRequisitionService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      reportName: 'AAAAAAA',
      reportDate: currentDate,
      timeOfRequisition: currentDate,
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
          reportDate: currentDate.format(DATE_FORMAT),
          timeOfRequisition: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a PrepaymentReportRequisition', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          reportDate: currentDate.format(DATE_FORMAT),
          timeOfRequisition: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          reportDate: currentDate,
          timeOfRequisition: currentDate,
        },
        returnedFromService
      );

      service.create(new PrepaymentReportRequisition()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a PrepaymentReportRequisition', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          reportName: 'BBBBBB',
          reportDate: currentDate.format(DATE_FORMAT),
          timeOfRequisition: currentDate.format(DATE_TIME_FORMAT),
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
          reportDate: currentDate,
          timeOfRequisition: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a PrepaymentReportRequisition', () => {
      const patchObject = Object.assign(
        {
          reportDate: currentDate.format(DATE_FORMAT),
          tampered: true,
          filename: 'BBBBBB',
          reportParameters: 'BBBBBB',
        },
        new PrepaymentReportRequisition()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          reportDate: currentDate,
          timeOfRequisition: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of PrepaymentReportRequisition', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          reportName: 'BBBBBB',
          reportDate: currentDate.format(DATE_FORMAT),
          timeOfRequisition: currentDate.format(DATE_TIME_FORMAT),
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
          reportDate: currentDate,
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

    it('should delete a PrepaymentReportRequisition', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addPrepaymentReportRequisitionToCollectionIfMissing', () => {
      it('should add a PrepaymentReportRequisition to an empty array', () => {
        const prepaymentReportRequisition: IPrepaymentReportRequisition = { id: 123 };
        expectedResult = service.addPrepaymentReportRequisitionToCollectionIfMissing([], prepaymentReportRequisition);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(prepaymentReportRequisition);
      });

      it('should not add a PrepaymentReportRequisition to an array that contains it', () => {
        const prepaymentReportRequisition: IPrepaymentReportRequisition = { id: 123 };
        const prepaymentReportRequisitionCollection: IPrepaymentReportRequisition[] = [
          {
            ...prepaymentReportRequisition,
          },
          { id: 456 },
        ];
        expectedResult = service.addPrepaymentReportRequisitionToCollectionIfMissing(
          prepaymentReportRequisitionCollection,
          prepaymentReportRequisition
        );
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a PrepaymentReportRequisition to an array that doesn't contain it", () => {
        const prepaymentReportRequisition: IPrepaymentReportRequisition = { id: 123 };
        const prepaymentReportRequisitionCollection: IPrepaymentReportRequisition[] = [{ id: 456 }];
        expectedResult = service.addPrepaymentReportRequisitionToCollectionIfMissing(
          prepaymentReportRequisitionCollection,
          prepaymentReportRequisition
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(prepaymentReportRequisition);
      });

      it('should add only unique PrepaymentReportRequisition to an array', () => {
        const prepaymentReportRequisitionArray: IPrepaymentReportRequisition[] = [{ id: 123 }, { id: 456 }, { id: 10979 }];
        const prepaymentReportRequisitionCollection: IPrepaymentReportRequisition[] = [{ id: 123 }];
        expectedResult = service.addPrepaymentReportRequisitionToCollectionIfMissing(
          prepaymentReportRequisitionCollection,
          ...prepaymentReportRequisitionArray
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const prepaymentReportRequisition: IPrepaymentReportRequisition = { id: 123 };
        const prepaymentReportRequisition2: IPrepaymentReportRequisition = { id: 456 };
        expectedResult = service.addPrepaymentReportRequisitionToCollectionIfMissing(
          [],
          prepaymentReportRequisition,
          prepaymentReportRequisition2
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(prepaymentReportRequisition);
        expect(expectedResult).toContain(prepaymentReportRequisition2);
      });

      it('should accept null and undefined values', () => {
        const prepaymentReportRequisition: IPrepaymentReportRequisition = { id: 123 };
        expectedResult = service.addPrepaymentReportRequisitionToCollectionIfMissing([], null, prepaymentReportRequisition, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(prepaymentReportRequisition);
      });

      it('should return initial array if no PrepaymentReportRequisition is added', () => {
        const prepaymentReportRequisitionCollection: IPrepaymentReportRequisition[] = [{ id: 123 }];
        expectedResult = service.addPrepaymentReportRequisitionToCollectionIfMissing(
          prepaymentReportRequisitionCollection,
          undefined,
          null
        );
        expect(expectedResult).toEqual(prepaymentReportRequisitionCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
