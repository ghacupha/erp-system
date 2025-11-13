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
import { ISettlementRequisition, SettlementRequisition } from '../settlement-requisition.model';

import { SettlementRequisitionService } from './settlement-requisition.service';
import { PaymentStatus } from '../../../erp-common/enumerations/payment-status.model';

describe('SettlementRequisition Service', () => {
  let service: SettlementRequisitionService;
  let httpMock: HttpTestingController;
  let elemDefault: ISettlementRequisition;
  let expectedResult: ISettlementRequisition | ISettlementRequisition[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(SettlementRequisitionService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      description: 'AAAAAAA',
      serialNumber: 'AAAAAAA',
      timeOfRequisition: currentDate,
      requisitionNumber: 'AAAAAAA',
      paymentAmount: 0,
      paymentStatus: PaymentStatus.PROCESSED,
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

    it('should create a SettlementRequisition', () => {
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

      service.create(new SettlementRequisition()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a SettlementRequisition', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          description: 'BBBBBB',
          serialNumber: 'BBBBBB',
          timeOfRequisition: currentDate.format(DATE_TIME_FORMAT),
          requisitionNumber: 'BBBBBB',
          paymentAmount: 1,
          paymentStatus: 'BBBBBB',
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

    it('should partial update a SettlementRequisition', () => {
      const patchObject = Object.assign(
        {
          description: 'BBBBBB',
          paymentStatus: 'BBBBBB',
        },
        new SettlementRequisition()
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

    it('should return a list of SettlementRequisition', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          description: 'BBBBBB',
          serialNumber: 'BBBBBB',
          timeOfRequisition: currentDate.format(DATE_TIME_FORMAT),
          requisitionNumber: 'BBBBBB',
          paymentAmount: 1,
          paymentStatus: 'BBBBBB',
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

    it('should delete a SettlementRequisition', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addSettlementRequisitionToCollectionIfMissing', () => {
      it('should add a SettlementRequisition to an empty array', () => {
        const settlementRequisition: ISettlementRequisition = { id: 123 };
        expectedResult = service.addSettlementRequisitionToCollectionIfMissing([], settlementRequisition);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(settlementRequisition);
      });

      it('should not add a SettlementRequisition to an array that contains it', () => {
        const settlementRequisition: ISettlementRequisition = { id: 123 };
        const settlementRequisitionCollection: ISettlementRequisition[] = [
          {
            ...settlementRequisition,
          },
          { id: 456 },
        ];
        expectedResult = service.addSettlementRequisitionToCollectionIfMissing(settlementRequisitionCollection, settlementRequisition);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a SettlementRequisition to an array that doesn't contain it", () => {
        const settlementRequisition: ISettlementRequisition = { id: 123 };
        const settlementRequisitionCollection: ISettlementRequisition[] = [{ id: 456 }];
        expectedResult = service.addSettlementRequisitionToCollectionIfMissing(settlementRequisitionCollection, settlementRequisition);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(settlementRequisition);
      });

      it('should add only unique SettlementRequisition to an array', () => {
        const settlementRequisitionArray: ISettlementRequisition[] = [{ id: 123 }, { id: 456 }, { id: 40587 }];
        const settlementRequisitionCollection: ISettlementRequisition[] = [{ id: 123 }];
        expectedResult = service.addSettlementRequisitionToCollectionIfMissing(
          settlementRequisitionCollection,
          ...settlementRequisitionArray
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const settlementRequisition: ISettlementRequisition = { id: 123 };
        const settlementRequisition2: ISettlementRequisition = { id: 456 };
        expectedResult = service.addSettlementRequisitionToCollectionIfMissing([], settlementRequisition, settlementRequisition2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(settlementRequisition);
        expect(expectedResult).toContain(settlementRequisition2);
      });

      it('should accept null and undefined values', () => {
        const settlementRequisition: ISettlementRequisition = { id: 123 };
        expectedResult = service.addSettlementRequisitionToCollectionIfMissing([], null, settlementRequisition, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(settlementRequisition);
      });

      it('should return initial array if no SettlementRequisition is added', () => {
        const settlementRequisitionCollection: ISettlementRequisition[] = [{ id: 123 }];
        expectedResult = service.addSettlementRequisitionToCollectionIfMissing(settlementRequisitionCollection, undefined, null);
        expect(expectedResult).toEqual(settlementRequisitionCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
