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

import { DATE_FORMAT } from 'app/config/input.constants';
import { ISettlement, Settlement } from '../settlement.model';

import { SettlementService } from './settlement.service';

describe('Settlement Service', () => {
  let service: SettlementService;
  let httpMock: HttpTestingController;
  let elemDefault: ISettlement;
  let expectedResult: ISettlement | ISettlement[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(SettlementService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      paymentNumber: 'AAAAAAA',
      paymentDate: currentDate,
      paymentAmount: 0,
      description: 'AAAAAAA',
      notes: 'AAAAAAA',
      calculationFileContentType: 'image/png',
      calculationFile: 'AAAAAAA',
      fileUploadToken: 'AAAAAAA',
      compilationToken: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          paymentDate: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Settlement', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          paymentDate: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          paymentDate: currentDate,
        },
        returnedFromService
      );

      service.create(new Settlement()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Settlement', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          paymentNumber: 'BBBBBB',
          paymentDate: currentDate.format(DATE_FORMAT),
          paymentAmount: 1,
          description: 'BBBBBB',
          notes: 'BBBBBB',
          calculationFile: 'BBBBBB',
          fileUploadToken: 'BBBBBB',
          compilationToken: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          paymentDate: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Settlement', () => {
      const patchObject = Object.assign(
        {
          paymentNumber: 'BBBBBB',
          paymentDate: currentDate.format(DATE_FORMAT),
          paymentAmount: 1,
          notes: 'BBBBBB',
          calculationFile: 'BBBBBB',
          fileUploadToken: 'BBBBBB',
          compilationToken: 'BBBBBB',
        },
        new Settlement()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          paymentDate: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Settlement', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          paymentNumber: 'BBBBBB',
          paymentDate: currentDate.format(DATE_FORMAT),
          paymentAmount: 1,
          description: 'BBBBBB',
          notes: 'BBBBBB',
          calculationFile: 'BBBBBB',
          fileUploadToken: 'BBBBBB',
          compilationToken: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          paymentDate: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Settlement', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addSettlementToCollectionIfMissing', () => {
      it('should add a Settlement to an empty array', () => {
        const settlement: ISettlement = { id: 123 };
        expectedResult = service.addSettlementToCollectionIfMissing([], settlement);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(settlement);
      });

      it('should not add a Settlement to an array that contains it', () => {
        const settlement: ISettlement = { id: 123 };
        const settlementCollection: ISettlement[] = [
          {
            ...settlement,
          },
          { id: 456 },
        ];
        expectedResult = service.addSettlementToCollectionIfMissing(settlementCollection, settlement);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Settlement to an array that doesn't contain it", () => {
        const settlement: ISettlement = { id: 123 };
        const settlementCollection: ISettlement[] = [{ id: 456 }];
        expectedResult = service.addSettlementToCollectionIfMissing(settlementCollection, settlement);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(settlement);
      });

      it('should add only unique Settlement to an array', () => {
        const settlementArray: ISettlement[] = [{ id: 123 }, { id: 456 }, { id: 94339 }];
        const settlementCollection: ISettlement[] = [{ id: 123 }];
        expectedResult = service.addSettlementToCollectionIfMissing(settlementCollection, ...settlementArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const settlement: ISettlement = { id: 123 };
        const settlement2: ISettlement = { id: 456 };
        expectedResult = service.addSettlementToCollectionIfMissing([], settlement, settlement2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(settlement);
        expect(expectedResult).toContain(settlement2);
      });

      it('should accept null and undefined values', () => {
        const settlement: ISettlement = { id: 123 };
        expectedResult = service.addSettlementToCollectionIfMissing([], null, settlement, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(settlement);
      });

      it('should return initial array if no Settlement is added', () => {
        const settlementCollection: ISettlement[] = [{ id: 123 }];
        expectedResult = service.addSettlementToCollectionIfMissing(settlementCollection, undefined, null);
        expect(expectedResult).toEqual(settlementCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
