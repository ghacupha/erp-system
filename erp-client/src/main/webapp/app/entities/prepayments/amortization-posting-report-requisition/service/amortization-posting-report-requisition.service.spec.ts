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
import {
  IAmortizationPostingReportRequisition,
  AmortizationPostingReportRequisition,
} from '../amortization-posting-report-requisition.model';

import { AmortizationPostingReportRequisitionService } from './amortization-posting-report-requisition.service';

describe('AmortizationPostingReportRequisition Service', () => {
  let service: AmortizationPostingReportRequisitionService;
  let httpMock: HttpTestingController;
  let elemDefault: IAmortizationPostingReportRequisition;
  let expectedResult: IAmortizationPostingReportRequisition | IAmortizationPostingReportRequisition[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(AmortizationPostingReportRequisitionService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      requestId: 'AAAAAAA',
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
          timeOfRequisition: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a AmortizationPostingReportRequisition', () => {
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

      service.create(new AmortizationPostingReportRequisition()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a AmortizationPostingReportRequisition', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          requestId: 'BBBBBB',
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
          timeOfRequisition: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a AmortizationPostingReportRequisition', () => {
      const patchObject = Object.assign(
        {
          timeOfRequisition: currentDate.format(DATE_TIME_FORMAT),
          fileChecksum: 'BBBBBB',
          tampered: true,
          reportParameters: 'BBBBBB',
          reportFile: 'BBBBBB',
        },
        new AmortizationPostingReportRequisition()
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

    it('should return a list of AmortizationPostingReportRequisition', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          requestId: 'BBBBBB',
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

    it('should delete a AmortizationPostingReportRequisition', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addAmortizationPostingReportRequisitionToCollectionIfMissing', () => {
      it('should add a AmortizationPostingReportRequisition to an empty array', () => {
        const amortizationPostingReportRequisition: IAmortizationPostingReportRequisition = { id: 123 };
        expectedResult = service.addAmortizationPostingReportRequisitionToCollectionIfMissing([], amortizationPostingReportRequisition);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(amortizationPostingReportRequisition);
      });

      it('should not add a AmortizationPostingReportRequisition to an array that contains it', () => {
        const amortizationPostingReportRequisition: IAmortizationPostingReportRequisition = { id: 123 };
        const amortizationPostingReportRequisitionCollection: IAmortizationPostingReportRequisition[] = [
          {
            ...amortizationPostingReportRequisition,
          },
          { id: 456 },
        ];
        expectedResult = service.addAmortizationPostingReportRequisitionToCollectionIfMissing(
          amortizationPostingReportRequisitionCollection,
          amortizationPostingReportRequisition
        );
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a AmortizationPostingReportRequisition to an array that doesn't contain it", () => {
        const amortizationPostingReportRequisition: IAmortizationPostingReportRequisition = { id: 123 };
        const amortizationPostingReportRequisitionCollection: IAmortizationPostingReportRequisition[] = [{ id: 456 }];
        expectedResult = service.addAmortizationPostingReportRequisitionToCollectionIfMissing(
          amortizationPostingReportRequisitionCollection,
          amortizationPostingReportRequisition
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(amortizationPostingReportRequisition);
      });

      it('should add only unique AmortizationPostingReportRequisition to an array', () => {
        const amortizationPostingReportRequisitionArray: IAmortizationPostingReportRequisition[] = [
          { id: 123 },
          { id: 456 },
          { id: 35392 },
        ];
        const amortizationPostingReportRequisitionCollection: IAmortizationPostingReportRequisition[] = [{ id: 123 }];
        expectedResult = service.addAmortizationPostingReportRequisitionToCollectionIfMissing(
          amortizationPostingReportRequisitionCollection,
          ...amortizationPostingReportRequisitionArray
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const amortizationPostingReportRequisition: IAmortizationPostingReportRequisition = { id: 123 };
        const amortizationPostingReportRequisition2: IAmortizationPostingReportRequisition = { id: 456 };
        expectedResult = service.addAmortizationPostingReportRequisitionToCollectionIfMissing(
          [],
          amortizationPostingReportRequisition,
          amortizationPostingReportRequisition2
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(amortizationPostingReportRequisition);
        expect(expectedResult).toContain(amortizationPostingReportRequisition2);
      });

      it('should accept null and undefined values', () => {
        const amortizationPostingReportRequisition: IAmortizationPostingReportRequisition = { id: 123 };
        expectedResult = service.addAmortizationPostingReportRequisitionToCollectionIfMissing(
          [],
          null,
          amortizationPostingReportRequisition,
          undefined
        );
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(amortizationPostingReportRequisition);
      });

      it('should return initial array if no AmortizationPostingReportRequisition is added', () => {
        const amortizationPostingReportRequisitionCollection: IAmortizationPostingReportRequisition[] = [{ id: 123 }];
        expectedResult = service.addAmortizationPostingReportRequisitionToCollectionIfMissing(
          amortizationPostingReportRequisitionCollection,
          undefined,
          null
        );
        expect(expectedResult).toEqual(amortizationPostingReportRequisitionCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
