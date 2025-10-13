///
/// Erp System - Mark X No 10 (Jehoiada Series) Client 1.7.8
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
import { IRouMonthlyDepreciationReportItem, RouMonthlyDepreciationReportItem } from '../rou-monthly-depreciation-report-item.model';

import { RouMonthlyDepreciationReportItemService } from './rou-monthly-depreciation-report-item.service';

describe('RouMonthlyDepreciationReportItem Service', () => {
  let service: RouMonthlyDepreciationReportItemService;
  let httpMock: HttpTestingController;
  let elemDefault: IRouMonthlyDepreciationReportItem;
  let expectedResult: IRouMonthlyDepreciationReportItem | IRouMonthlyDepreciationReportItem[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(RouMonthlyDepreciationReportItemService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      fiscalMonthStartDate: currentDate,
      fiscalMonthEndDate: currentDate,
      totalDepreciationAmount: 0,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          fiscalMonthStartDate: currentDate.format(DATE_FORMAT),
          fiscalMonthEndDate: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a RouMonthlyDepreciationReportItem', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          fiscalMonthStartDate: currentDate.format(DATE_FORMAT),
          fiscalMonthEndDate: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          fiscalMonthStartDate: currentDate,
          fiscalMonthEndDate: currentDate,
        },
        returnedFromService
      );

      service.create(new RouMonthlyDepreciationReportItem()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a RouMonthlyDepreciationReportItem', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          fiscalMonthStartDate: currentDate.format(DATE_FORMAT),
          fiscalMonthEndDate: currentDate.format(DATE_FORMAT),
          totalDepreciationAmount: 1,
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          fiscalMonthStartDate: currentDate,
          fiscalMonthEndDate: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a RouMonthlyDepreciationReportItem', () => {
      const patchObject = Object.assign(
        {
          fiscalMonthStartDate: currentDate.format(DATE_FORMAT),
          fiscalMonthEndDate: currentDate.format(DATE_FORMAT),
        },
        new RouMonthlyDepreciationReportItem()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          fiscalMonthStartDate: currentDate,
          fiscalMonthEndDate: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of RouMonthlyDepreciationReportItem', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          fiscalMonthStartDate: currentDate.format(DATE_FORMAT),
          fiscalMonthEndDate: currentDate.format(DATE_FORMAT),
          totalDepreciationAmount: 1,
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          fiscalMonthStartDate: currentDate,
          fiscalMonthEndDate: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a RouMonthlyDepreciationReportItem', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addRouMonthlyDepreciationReportItemToCollectionIfMissing', () => {
      it('should add a RouMonthlyDepreciationReportItem to an empty array', () => {
        const rouMonthlyDepreciationReportItem: IRouMonthlyDepreciationReportItem = { id: 123 };
        expectedResult = service.addRouMonthlyDepreciationReportItemToCollectionIfMissing([], rouMonthlyDepreciationReportItem);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(rouMonthlyDepreciationReportItem);
      });

      it('should not add a RouMonthlyDepreciationReportItem to an array that contains it', () => {
        const rouMonthlyDepreciationReportItem: IRouMonthlyDepreciationReportItem = { id: 123 };
        const rouMonthlyDepreciationReportItemCollection: IRouMonthlyDepreciationReportItem[] = [
          {
            ...rouMonthlyDepreciationReportItem,
          },
          { id: 456 },
        ];
        expectedResult = service.addRouMonthlyDepreciationReportItemToCollectionIfMissing(
          rouMonthlyDepreciationReportItemCollection,
          rouMonthlyDepreciationReportItem
        );
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a RouMonthlyDepreciationReportItem to an array that doesn't contain it", () => {
        const rouMonthlyDepreciationReportItem: IRouMonthlyDepreciationReportItem = { id: 123 };
        const rouMonthlyDepreciationReportItemCollection: IRouMonthlyDepreciationReportItem[] = [{ id: 456 }];
        expectedResult = service.addRouMonthlyDepreciationReportItemToCollectionIfMissing(
          rouMonthlyDepreciationReportItemCollection,
          rouMonthlyDepreciationReportItem
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(rouMonthlyDepreciationReportItem);
      });

      it('should add only unique RouMonthlyDepreciationReportItem to an array', () => {
        const rouMonthlyDepreciationReportItemArray: IRouMonthlyDepreciationReportItem[] = [{ id: 123 }, { id: 456 }, { id: 29853 }];
        const rouMonthlyDepreciationReportItemCollection: IRouMonthlyDepreciationReportItem[] = [{ id: 123 }];
        expectedResult = service.addRouMonthlyDepreciationReportItemToCollectionIfMissing(
          rouMonthlyDepreciationReportItemCollection,
          ...rouMonthlyDepreciationReportItemArray
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const rouMonthlyDepreciationReportItem: IRouMonthlyDepreciationReportItem = { id: 123 };
        const rouMonthlyDepreciationReportItem2: IRouMonthlyDepreciationReportItem = { id: 456 };
        expectedResult = service.addRouMonthlyDepreciationReportItemToCollectionIfMissing(
          [],
          rouMonthlyDepreciationReportItem,
          rouMonthlyDepreciationReportItem2
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(rouMonthlyDepreciationReportItem);
        expect(expectedResult).toContain(rouMonthlyDepreciationReportItem2);
      });

      it('should accept null and undefined values', () => {
        const rouMonthlyDepreciationReportItem: IRouMonthlyDepreciationReportItem = { id: 123 };
        expectedResult = service.addRouMonthlyDepreciationReportItemToCollectionIfMissing(
          [],
          null,
          rouMonthlyDepreciationReportItem,
          undefined
        );
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(rouMonthlyDepreciationReportItem);
      });

      it('should return initial array if no RouMonthlyDepreciationReportItem is added', () => {
        const rouMonthlyDepreciationReportItemCollection: IRouMonthlyDepreciationReportItem[] = [{ id: 123 }];
        expectedResult = service.addRouMonthlyDepreciationReportItemToCollectionIfMissing(
          rouMonthlyDepreciationReportItemCollection,
          undefined,
          null
        );
        expect(expectedResult).toEqual(rouMonthlyDepreciationReportItemCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
