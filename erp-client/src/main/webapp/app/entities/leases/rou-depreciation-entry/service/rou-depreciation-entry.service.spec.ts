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

import { IRouDepreciationEntry, RouDepreciationEntry } from '../rou-depreciation-entry.model';

import { RouDepreciationEntryService } from './rou-depreciation-entry.service';

describe('RouDepreciationEntry Service', () => {
  let service: RouDepreciationEntryService;
  let httpMock: HttpTestingController;
  let elemDefault: IRouDepreciationEntry;
  let expectedResult: IRouDepreciationEntry | IRouDepreciationEntry[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(RouDepreciationEntryService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      description: 'AAAAAAA',
      depreciationAmount: 0,
      outstandingAmount: 0,
      rouAssetIdentifier: 'AAAAAAA',
      rouDepreciationIdentifier: 'AAAAAAA',
      sequenceNumber: 0,
      invalidated: false,
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

    it('should create a RouDepreciationEntry', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new RouDepreciationEntry()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a RouDepreciationEntry', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          description: 'BBBBBB',
          depreciationAmount: 1,
          outstandingAmount: 1,
          rouAssetIdentifier: 'BBBBBB',
          rouDepreciationIdentifier: 'BBBBBB',
          sequenceNumber: 1,
          invalidated: true,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a RouDepreciationEntry', () => {
      const patchObject = Object.assign(
        {
          depreciationAmount: 1,
          rouAssetIdentifier: 'BBBBBB',
          sequenceNumber: 1,
        },
        new RouDepreciationEntry()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of RouDepreciationEntry', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          description: 'BBBBBB',
          depreciationAmount: 1,
          outstandingAmount: 1,
          rouAssetIdentifier: 'BBBBBB',
          rouDepreciationIdentifier: 'BBBBBB',
          sequenceNumber: 1,
          invalidated: true,
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

    it('should delete a RouDepreciationEntry', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addRouDepreciationEntryToCollectionIfMissing', () => {
      it('should add a RouDepreciationEntry to an empty array', () => {
        const rouDepreciationEntry: IRouDepreciationEntry = { id: 123 };
        expectedResult = service.addRouDepreciationEntryToCollectionIfMissing([], rouDepreciationEntry);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(rouDepreciationEntry);
      });

      it('should not add a RouDepreciationEntry to an array that contains it', () => {
        const rouDepreciationEntry: IRouDepreciationEntry = { id: 123 };
        const rouDepreciationEntryCollection: IRouDepreciationEntry[] = [
          {
            ...rouDepreciationEntry,
          },
          { id: 456 },
        ];
        expectedResult = service.addRouDepreciationEntryToCollectionIfMissing(rouDepreciationEntryCollection, rouDepreciationEntry);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a RouDepreciationEntry to an array that doesn't contain it", () => {
        const rouDepreciationEntry: IRouDepreciationEntry = { id: 123 };
        const rouDepreciationEntryCollection: IRouDepreciationEntry[] = [{ id: 456 }];
        expectedResult = service.addRouDepreciationEntryToCollectionIfMissing(rouDepreciationEntryCollection, rouDepreciationEntry);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(rouDepreciationEntry);
      });

      it('should add only unique RouDepreciationEntry to an array', () => {
        const rouDepreciationEntryArray: IRouDepreciationEntry[] = [{ id: 123 }, { id: 456 }, { id: 40901 }];
        const rouDepreciationEntryCollection: IRouDepreciationEntry[] = [{ id: 123 }];
        expectedResult = service.addRouDepreciationEntryToCollectionIfMissing(rouDepreciationEntryCollection, ...rouDepreciationEntryArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const rouDepreciationEntry: IRouDepreciationEntry = { id: 123 };
        const rouDepreciationEntry2: IRouDepreciationEntry = { id: 456 };
        expectedResult = service.addRouDepreciationEntryToCollectionIfMissing([], rouDepreciationEntry, rouDepreciationEntry2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(rouDepreciationEntry);
        expect(expectedResult).toContain(rouDepreciationEntry2);
      });

      it('should accept null and undefined values', () => {
        const rouDepreciationEntry: IRouDepreciationEntry = { id: 123 };
        expectedResult = service.addRouDepreciationEntryToCollectionIfMissing([], null, rouDepreciationEntry, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(rouDepreciationEntry);
      });

      it('should return initial array if no RouDepreciationEntry is added', () => {
        const rouDepreciationEntryCollection: IRouDepreciationEntry[] = [{ id: 123 }];
        expectedResult = service.addRouDepreciationEntryToCollectionIfMissing(rouDepreciationEntryCollection, undefined, null);
        expect(expectedResult).toEqual(rouDepreciationEntryCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
