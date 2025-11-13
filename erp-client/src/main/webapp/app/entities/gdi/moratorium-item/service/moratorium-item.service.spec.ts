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

import { IMoratoriumItem, MoratoriumItem } from '../moratorium-item.model';

import { MoratoriumItemService } from './moratorium-item.service';

describe('MoratoriumItem Service', () => {
  let service: MoratoriumItemService;
  let httpMock: HttpTestingController;
  let elemDefault: IMoratoriumItem;
  let expectedResult: IMoratoriumItem | IMoratoriumItem[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(MoratoriumItemService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      moratoriumItemTypeCode: 'AAAAAAA',
      moratoriumItemType: 'AAAAAAA',
      moratoriumTypeDetails: 'AAAAAAA',
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

    it('should create a MoratoriumItem', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new MoratoriumItem()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a MoratoriumItem', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          moratoriumItemTypeCode: 'BBBBBB',
          moratoriumItemType: 'BBBBBB',
          moratoriumTypeDetails: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a MoratoriumItem', () => {
      const patchObject = Object.assign(
        {
          moratoriumItemTypeCode: 'BBBBBB',
        },
        new MoratoriumItem()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of MoratoriumItem', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          moratoriumItemTypeCode: 'BBBBBB',
          moratoriumItemType: 'BBBBBB',
          moratoriumTypeDetails: 'BBBBBB',
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

    it('should delete a MoratoriumItem', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addMoratoriumItemToCollectionIfMissing', () => {
      it('should add a MoratoriumItem to an empty array', () => {
        const moratoriumItem: IMoratoriumItem = { id: 123 };
        expectedResult = service.addMoratoriumItemToCollectionIfMissing([], moratoriumItem);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(moratoriumItem);
      });

      it('should not add a MoratoriumItem to an array that contains it', () => {
        const moratoriumItem: IMoratoriumItem = { id: 123 };
        const moratoriumItemCollection: IMoratoriumItem[] = [
          {
            ...moratoriumItem,
          },
          { id: 456 },
        ];
        expectedResult = service.addMoratoriumItemToCollectionIfMissing(moratoriumItemCollection, moratoriumItem);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a MoratoriumItem to an array that doesn't contain it", () => {
        const moratoriumItem: IMoratoriumItem = { id: 123 };
        const moratoriumItemCollection: IMoratoriumItem[] = [{ id: 456 }];
        expectedResult = service.addMoratoriumItemToCollectionIfMissing(moratoriumItemCollection, moratoriumItem);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(moratoriumItem);
      });

      it('should add only unique MoratoriumItem to an array', () => {
        const moratoriumItemArray: IMoratoriumItem[] = [{ id: 123 }, { id: 456 }, { id: 69142 }];
        const moratoriumItemCollection: IMoratoriumItem[] = [{ id: 123 }];
        expectedResult = service.addMoratoriumItemToCollectionIfMissing(moratoriumItemCollection, ...moratoriumItemArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const moratoriumItem: IMoratoriumItem = { id: 123 };
        const moratoriumItem2: IMoratoriumItem = { id: 456 };
        expectedResult = service.addMoratoriumItemToCollectionIfMissing([], moratoriumItem, moratoriumItem2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(moratoriumItem);
        expect(expectedResult).toContain(moratoriumItem2);
      });

      it('should accept null and undefined values', () => {
        const moratoriumItem: IMoratoriumItem = { id: 123 };
        expectedResult = service.addMoratoriumItemToCollectionIfMissing([], null, moratoriumItem, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(moratoriumItem);
      });

      it('should return initial array if no MoratoriumItem is added', () => {
        const moratoriumItemCollection: IMoratoriumItem[] = [{ id: 123 }];
        expectedResult = service.addMoratoriumItemToCollectionIfMissing(moratoriumItemCollection, undefined, null);
        expect(expectedResult).toEqual(moratoriumItemCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
