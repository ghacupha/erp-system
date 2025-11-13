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
import {
  IPrepaymentByAccountReportRequisition,
  PrepaymentByAccountReportRequisition,
} from '../prepayment-by-account-report-requisition.model';

import { PrepaymentByAccountReportRequisitionService } from './prepayment-by-account-report-requisition.service';

describe('PrepaymentByAccountReportRequisition Service', () => {
  let service: PrepaymentByAccountReportRequisitionService;
  let httpMock: HttpTestingController;
  let elemDefault: IPrepaymentByAccountReportRequisition;
  let expectedResult: IPrepaymentByAccountReportRequisition | IPrepaymentByAccountReportRequisition[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(PrepaymentByAccountReportRequisitionService);
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
      reportDate: currentDate,
      tampered: false,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          timeOfRequisition: currentDate.format(DATE_TIME_FORMAT),
          reportDate: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a PrepaymentByAccountReportRequisition', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          timeOfRequisition: currentDate.format(DATE_TIME_FORMAT),
          reportDate: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          timeOfRequisition: currentDate,
          reportDate: currentDate,
        },
        returnedFromService
      );

      service.create(new PrepaymentByAccountReportRequisition()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a PrepaymentByAccountReportRequisition', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          requestId: 'BBBBBB',
          timeOfRequisition: currentDate.format(DATE_TIME_FORMAT),
          fileChecksum: 'BBBBBB',
          filename: 'BBBBBB',
          reportParameters: 'BBBBBB',
          reportFile: 'BBBBBB',
          reportDate: currentDate.format(DATE_FORMAT),
          tampered: true,
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          timeOfRequisition: currentDate,
          reportDate: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a PrepaymentByAccountReportRequisition', () => {
      const patchObject = Object.assign(
        {
          requestId: 'BBBBBB',
          reportParameters: 'BBBBBB',
          reportDate: currentDate.format(DATE_FORMAT),
        },
        new PrepaymentByAccountReportRequisition()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          timeOfRequisition: currentDate,
          reportDate: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of PrepaymentByAccountReportRequisition', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          requestId: 'BBBBBB',
          timeOfRequisition: currentDate.format(DATE_TIME_FORMAT),
          fileChecksum: 'BBBBBB',
          filename: 'BBBBBB',
          reportParameters: 'BBBBBB',
          reportFile: 'BBBBBB',
          reportDate: currentDate.format(DATE_FORMAT),
          tampered: true,
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          timeOfRequisition: currentDate,
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

    it('should delete a PrepaymentByAccountReportRequisition', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addPrepaymentByAccountReportRequisitionToCollectionIfMissing', () => {
      it('should add a PrepaymentByAccountReportRequisition to an empty array', () => {
        const prepaymentByAccountReportRequisition: IPrepaymentByAccountReportRequisition = { id: 123 };
        expectedResult = service.addPrepaymentByAccountReportRequisitionToCollectionIfMissing([], prepaymentByAccountReportRequisition);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(prepaymentByAccountReportRequisition);
      });

      it('should not add a PrepaymentByAccountReportRequisition to an array that contains it', () => {
        const prepaymentByAccountReportRequisition: IPrepaymentByAccountReportRequisition = { id: 123 };
        const prepaymentByAccountReportRequisitionCollection: IPrepaymentByAccountReportRequisition[] = [
          {
            ...prepaymentByAccountReportRequisition,
          },
          { id: 456 },
        ];
        expectedResult = service.addPrepaymentByAccountReportRequisitionToCollectionIfMissing(
          prepaymentByAccountReportRequisitionCollection,
          prepaymentByAccountReportRequisition
        );
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a PrepaymentByAccountReportRequisition to an array that doesn't contain it", () => {
        const prepaymentByAccountReportRequisition: IPrepaymentByAccountReportRequisition = { id: 123 };
        const prepaymentByAccountReportRequisitionCollection: IPrepaymentByAccountReportRequisition[] = [{ id: 456 }];
        expectedResult = service.addPrepaymentByAccountReportRequisitionToCollectionIfMissing(
          prepaymentByAccountReportRequisitionCollection,
          prepaymentByAccountReportRequisition
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(prepaymentByAccountReportRequisition);
      });

      it('should add only unique PrepaymentByAccountReportRequisition to an array', () => {
        const prepaymentByAccountReportRequisitionArray: IPrepaymentByAccountReportRequisition[] = [{ id: 123 }, { id: 456 }, { id: 6889 }];
        const prepaymentByAccountReportRequisitionCollection: IPrepaymentByAccountReportRequisition[] = [{ id: 123 }];
        expectedResult = service.addPrepaymentByAccountReportRequisitionToCollectionIfMissing(
          prepaymentByAccountReportRequisitionCollection,
          ...prepaymentByAccountReportRequisitionArray
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const prepaymentByAccountReportRequisition: IPrepaymentByAccountReportRequisition = { id: 123 };
        const prepaymentByAccountReportRequisition2: IPrepaymentByAccountReportRequisition = { id: 456 };
        expectedResult = service.addPrepaymentByAccountReportRequisitionToCollectionIfMissing(
          [],
          prepaymentByAccountReportRequisition,
          prepaymentByAccountReportRequisition2
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(prepaymentByAccountReportRequisition);
        expect(expectedResult).toContain(prepaymentByAccountReportRequisition2);
      });

      it('should accept null and undefined values', () => {
        const prepaymentByAccountReportRequisition: IPrepaymentByAccountReportRequisition = { id: 123 };
        expectedResult = service.addPrepaymentByAccountReportRequisitionToCollectionIfMissing(
          [],
          null,
          prepaymentByAccountReportRequisition,
          undefined
        );
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(prepaymentByAccountReportRequisition);
      });

      it('should return initial array if no PrepaymentByAccountReportRequisition is added', () => {
        const prepaymentByAccountReportRequisitionCollection: IPrepaymentByAccountReportRequisition[] = [{ id: 123 }];
        expectedResult = service.addPrepaymentByAccountReportRequisitionToCollectionIfMissing(
          prepaymentByAccountReportRequisitionCollection,
          undefined,
          null
        );
        expect(expectedResult).toEqual(prepaymentByAccountReportRequisitionCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
