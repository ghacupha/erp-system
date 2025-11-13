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
import { IRouInitialDirectCost, RouInitialDirectCost } from '../rou-initial-direct-cost.model';

import { RouInitialDirectCostService } from './rou-initial-direct-cost.service';

describe('RouInitialDirectCost Service', () => {
  let service: RouInitialDirectCostService;
  let httpMock: HttpTestingController;
  let elemDefault: IRouInitialDirectCost;
  let expectedResult: IRouInitialDirectCost | IRouInitialDirectCost[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(RouInitialDirectCostService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      transactionDate: currentDate,
      description: 'AAAAAAA',
      cost: 0,
      referenceNumber: 0,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          transactionDate: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a RouInitialDirectCost', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          transactionDate: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          transactionDate: currentDate,
        },
        returnedFromService
      );

      service.create(new RouInitialDirectCost()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a RouInitialDirectCost', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          transactionDate: currentDate.format(DATE_FORMAT),
          description: 'BBBBBB',
          cost: 1,
          referenceNumber: 1,
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          transactionDate: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a RouInitialDirectCost', () => {
      const patchObject = Object.assign(
        {
          transactionDate: currentDate.format(DATE_FORMAT),
          cost: 1,
        },
        new RouInitialDirectCost()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          transactionDate: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of RouInitialDirectCost', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          transactionDate: currentDate.format(DATE_FORMAT),
          description: 'BBBBBB',
          cost: 1,
          referenceNumber: 1,
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          transactionDate: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a RouInitialDirectCost', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addRouInitialDirectCostToCollectionIfMissing', () => {
      it('should add a RouInitialDirectCost to an empty array', () => {
        const rouInitialDirectCost: IRouInitialDirectCost = { id: 123 };
        expectedResult = service.addRouInitialDirectCostToCollectionIfMissing([], rouInitialDirectCost);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(rouInitialDirectCost);
      });

      it('should not add a RouInitialDirectCost to an array that contains it', () => {
        const rouInitialDirectCost: IRouInitialDirectCost = { id: 123 };
        const rouInitialDirectCostCollection: IRouInitialDirectCost[] = [
          {
            ...rouInitialDirectCost,
          },
          { id: 456 },
        ];
        expectedResult = service.addRouInitialDirectCostToCollectionIfMissing(rouInitialDirectCostCollection, rouInitialDirectCost);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a RouInitialDirectCost to an array that doesn't contain it", () => {
        const rouInitialDirectCost: IRouInitialDirectCost = { id: 123 };
        const rouInitialDirectCostCollection: IRouInitialDirectCost[] = [{ id: 456 }];
        expectedResult = service.addRouInitialDirectCostToCollectionIfMissing(rouInitialDirectCostCollection, rouInitialDirectCost);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(rouInitialDirectCost);
      });

      it('should add only unique RouInitialDirectCost to an array', () => {
        const rouInitialDirectCostArray: IRouInitialDirectCost[] = [{ id: 123 }, { id: 456 }, { id: 18416 }];
        const rouInitialDirectCostCollection: IRouInitialDirectCost[] = [{ id: 123 }];
        expectedResult = service.addRouInitialDirectCostToCollectionIfMissing(rouInitialDirectCostCollection, ...rouInitialDirectCostArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const rouInitialDirectCost: IRouInitialDirectCost = { id: 123 };
        const rouInitialDirectCost2: IRouInitialDirectCost = { id: 456 };
        expectedResult = service.addRouInitialDirectCostToCollectionIfMissing([], rouInitialDirectCost, rouInitialDirectCost2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(rouInitialDirectCost);
        expect(expectedResult).toContain(rouInitialDirectCost2);
      });

      it('should accept null and undefined values', () => {
        const rouInitialDirectCost: IRouInitialDirectCost = { id: 123 };
        expectedResult = service.addRouInitialDirectCostToCollectionIfMissing([], null, rouInitialDirectCost, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(rouInitialDirectCost);
      });

      it('should return initial array if no RouInitialDirectCost is added', () => {
        const rouInitialDirectCostCollection: IRouInitialDirectCost[] = [{ id: 123 }];
        expectedResult = service.addRouInitialDirectCostToCollectionIfMissing(rouInitialDirectCostCollection, undefined, null);
        expect(expectedResult).toEqual(rouInitialDirectCostCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
