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

import { INetBookValueEntry, NetBookValueEntry } from '../net-book-value-entry.model';

import { NetBookValueEntryService } from './net-book-value-entry.service';

describe('NetBookValueEntry Service', () => {
  let service: NetBookValueEntryService;
  let httpMock: HttpTestingController;
  let elemDefault: INetBookValueEntry;
  let expectedResult: INetBookValueEntry | INetBookValueEntry[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(NetBookValueEntryService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      assetNumber: 'AAAAAAA',
      assetTag: 'AAAAAAA',
      assetDescription: 'AAAAAAA',
      nbvIdentifier: 'AAAAAAA',
      compilationJobIdentifier: 'AAAAAAA',
      compilationBatchIdentifier: 'AAAAAAA',
      elapsedMonths: 0,
      priorMonths: 0,
      usefulLifeYears: 0,
      netBookValueAmount: 0,
      previousNetBookValueAmount: 0,
      historicalCost: 0,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign({}, elemDefault);

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a NetBookValueEntry', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new NetBookValueEntry()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a NetBookValueEntry', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          assetNumber: 'BBBBBB',
          assetTag: 'BBBBBB',
          assetDescription: 'BBBBBB',
          nbvIdentifier: 'BBBBBB',
          compilationJobIdentifier: 'BBBBBB',
          compilationBatchIdentifier: 'BBBBBB',
          elapsedMonths: 1,
          priorMonths: 1,
          usefulLifeYears: 1,
          netBookValueAmount: 1,
          previousNetBookValueAmount: 1,
          historicalCost: 1,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a NetBookValueEntry', () => {
      const patchObject = Object.assign(
        {
          assetNumber: 'BBBBBB',
          assetTag: 'BBBBBB',
          nbvIdentifier: 'BBBBBB',
          compilationJobIdentifier: 'BBBBBB',
          compilationBatchIdentifier: 'BBBBBB',
          elapsedMonths: 1,
          priorMonths: 1,
          netBookValueAmount: 1,
          previousNetBookValueAmount: 1,
          historicalCost: 1,
        },
        new NetBookValueEntry()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of NetBookValueEntry', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          assetNumber: 'BBBBBB',
          assetTag: 'BBBBBB',
          assetDescription: 'BBBBBB',
          nbvIdentifier: 'BBBBBB',
          compilationJobIdentifier: 'BBBBBB',
          compilationBatchIdentifier: 'BBBBBB',
          elapsedMonths: 1,
          priorMonths: 1,
          usefulLifeYears: 1,
          netBookValueAmount: 1,
          previousNetBookValueAmount: 1,
          historicalCost: 1,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a NetBookValueEntry', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addNetBookValueEntryToCollectionIfMissing', () => {
      it('should add a NetBookValueEntry to an empty array', () => {
        const netBookValueEntry: INetBookValueEntry = { id: 123 };
        expectedResult = service.addNetBookValueEntryToCollectionIfMissing([], netBookValueEntry);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(netBookValueEntry);
      });

      it('should not add a NetBookValueEntry to an array that contains it', () => {
        const netBookValueEntry: INetBookValueEntry = { id: 123 };
        const netBookValueEntryCollection: INetBookValueEntry[] = [
          {
            ...netBookValueEntry,
          },
          { id: 456 },
        ];
        expectedResult = service.addNetBookValueEntryToCollectionIfMissing(netBookValueEntryCollection, netBookValueEntry);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a NetBookValueEntry to an array that doesn't contain it", () => {
        const netBookValueEntry: INetBookValueEntry = { id: 123 };
        const netBookValueEntryCollection: INetBookValueEntry[] = [{ id: 456 }];
        expectedResult = service.addNetBookValueEntryToCollectionIfMissing(netBookValueEntryCollection, netBookValueEntry);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(netBookValueEntry);
      });

      it('should add only unique NetBookValueEntry to an array', () => {
        const netBookValueEntryArray: INetBookValueEntry[] = [{ id: 123 }, { id: 456 }, { id: 10008 }];
        const netBookValueEntryCollection: INetBookValueEntry[] = [{ id: 123 }];
        expectedResult = service.addNetBookValueEntryToCollectionIfMissing(netBookValueEntryCollection, ...netBookValueEntryArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const netBookValueEntry: INetBookValueEntry = { id: 123 };
        const netBookValueEntry2: INetBookValueEntry = { id: 456 };
        expectedResult = service.addNetBookValueEntryToCollectionIfMissing([], netBookValueEntry, netBookValueEntry2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(netBookValueEntry);
        expect(expectedResult).toContain(netBookValueEntry2);
      });

      it('should accept null and undefined values', () => {
        const netBookValueEntry: INetBookValueEntry = { id: 123 };
        expectedResult = service.addNetBookValueEntryToCollectionIfMissing([], null, netBookValueEntry, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(netBookValueEntry);
      });

      it('should return initial array if no NetBookValueEntry is added', () => {
        const netBookValueEntryCollection: INetBookValueEntry[] = [{ id: 123 }];
        expectedResult = service.addNetBookValueEntryToCollectionIfMissing(netBookValueEntryCollection, undefined, null);
        expect(expectedResult).toEqual(netBookValueEntryCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
