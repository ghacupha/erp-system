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

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IDepreciationEntry, DepreciationEntry } from '../depreciation-entry.model';

import { DepreciationEntryService } from './depreciation-entry.service';

describe('DepreciationEntry Service', () => {
  let service: DepreciationEntryService;
  let httpMock: HttpTestingController;
  let elemDefault: IDepreciationEntry;
  let expectedResult: IDepreciationEntry | IDepreciationEntry[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(DepreciationEntryService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      postedAt: currentDate,
      depreciationAmount: 0,
      assetNumber: 0,
      batchSequenceNumber: 0,
      processedItems: 'AAAAAAA',
      totalItemsProcessed: 0,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          postedAt: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a DepreciationEntry', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          postedAt: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          postedAt: currentDate,
        },
        returnedFromService
      );

      service.create(new DepreciationEntry()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a DepreciationEntry', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          postedAt: currentDate.format(DATE_TIME_FORMAT),
          depreciationAmount: 1,
          assetNumber: 1,
          batchSequenceNumber: 1,
          processedItems: 'BBBBBB',
          totalItemsProcessed: 1,
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          postedAt: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a DepreciationEntry', () => {
      const patchObject = Object.assign(
        {
          postedAt: currentDate.format(DATE_TIME_FORMAT),
          depreciationAmount: 1,
          processedItems: 'BBBBBB',
        },
        new DepreciationEntry()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          postedAt: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of DepreciationEntry', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          postedAt: currentDate.format(DATE_TIME_FORMAT),
          depreciationAmount: 1,
          assetNumber: 1,
          batchSequenceNumber: 1,
          processedItems: 'BBBBBB',
          totalItemsProcessed: 1,
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          postedAt: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a DepreciationEntry', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addDepreciationEntryToCollectionIfMissing', () => {
      it('should add a DepreciationEntry to an empty array', () => {
        const depreciationEntry: IDepreciationEntry = { id: 123 };
        expectedResult = service.addDepreciationEntryToCollectionIfMissing([], depreciationEntry);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(depreciationEntry);
      });

      it('should not add a DepreciationEntry to an array that contains it', () => {
        const depreciationEntry: IDepreciationEntry = { id: 123 };
        const depreciationEntryCollection: IDepreciationEntry[] = [
          {
            ...depreciationEntry,
          },
          { id: 456 },
        ];
        expectedResult = service.addDepreciationEntryToCollectionIfMissing(depreciationEntryCollection, depreciationEntry);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a DepreciationEntry to an array that doesn't contain it", () => {
        const depreciationEntry: IDepreciationEntry = { id: 123 };
        const depreciationEntryCollection: IDepreciationEntry[] = [{ id: 456 }];
        expectedResult = service.addDepreciationEntryToCollectionIfMissing(depreciationEntryCollection, depreciationEntry);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(depreciationEntry);
      });

      it('should add only unique DepreciationEntry to an array', () => {
        const depreciationEntryArray: IDepreciationEntry[] = [{ id: 123 }, { id: 456 }, { id: 61329 }];
        const depreciationEntryCollection: IDepreciationEntry[] = [{ id: 123 }];
        expectedResult = service.addDepreciationEntryToCollectionIfMissing(depreciationEntryCollection, ...depreciationEntryArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const depreciationEntry: IDepreciationEntry = { id: 123 };
        const depreciationEntry2: IDepreciationEntry = { id: 456 };
        expectedResult = service.addDepreciationEntryToCollectionIfMissing([], depreciationEntry, depreciationEntry2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(depreciationEntry);
        expect(expectedResult).toContain(depreciationEntry2);
      });

      it('should accept null and undefined values', () => {
        const depreciationEntry: IDepreciationEntry = { id: 123 };
        expectedResult = service.addDepreciationEntryToCollectionIfMissing([], null, depreciationEntry, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(depreciationEntry);
      });

      it('should return initial array if no DepreciationEntry is added', () => {
        const depreciationEntryCollection: IDepreciationEntry[] = [{ id: 123 }];
        expectedResult = service.addDepreciationEntryToCollectionIfMissing(depreciationEntryCollection, undefined, null);
        expect(expectedResult).toEqual(depreciationEntryCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
