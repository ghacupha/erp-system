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

import { ICrbAccountHolderType, CrbAccountHolderType } from '../crb-account-holder-type.model';

import { CrbAccountHolderTypeService } from './crb-account-holder-type.service';

describe('CrbAccountHolderType Service', () => {
  let service: CrbAccountHolderTypeService;
  let httpMock: HttpTestingController;
  let elemDefault: ICrbAccountHolderType;
  let expectedResult: ICrbAccountHolderType | ICrbAccountHolderType[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CrbAccountHolderTypeService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      accountHolderCategoryTypeCode: 'AAAAAAA',
      accountHolderCategoryType: 'AAAAAAA',
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

    it('should create a CrbAccountHolderType', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new CrbAccountHolderType()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CrbAccountHolderType', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          accountHolderCategoryTypeCode: 'BBBBBB',
          accountHolderCategoryType: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a CrbAccountHolderType', () => {
      const patchObject = Object.assign(
        {
          accountHolderCategoryTypeCode: 'BBBBBB',
          accountHolderCategoryType: 'BBBBBB',
        },
        new CrbAccountHolderType()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of CrbAccountHolderType', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          accountHolderCategoryTypeCode: 'BBBBBB',
          accountHolderCategoryType: 'BBBBBB',
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

    it('should delete a CrbAccountHolderType', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addCrbAccountHolderTypeToCollectionIfMissing', () => {
      it('should add a CrbAccountHolderType to an empty array', () => {
        const crbAccountHolderType: ICrbAccountHolderType = { id: 123 };
        expectedResult = service.addCrbAccountHolderTypeToCollectionIfMissing([], crbAccountHolderType);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(crbAccountHolderType);
      });

      it('should not add a CrbAccountHolderType to an array that contains it', () => {
        const crbAccountHolderType: ICrbAccountHolderType = { id: 123 };
        const crbAccountHolderTypeCollection: ICrbAccountHolderType[] = [
          {
            ...crbAccountHolderType,
          },
          { id: 456 },
        ];
        expectedResult = service.addCrbAccountHolderTypeToCollectionIfMissing(crbAccountHolderTypeCollection, crbAccountHolderType);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CrbAccountHolderType to an array that doesn't contain it", () => {
        const crbAccountHolderType: ICrbAccountHolderType = { id: 123 };
        const crbAccountHolderTypeCollection: ICrbAccountHolderType[] = [{ id: 456 }];
        expectedResult = service.addCrbAccountHolderTypeToCollectionIfMissing(crbAccountHolderTypeCollection, crbAccountHolderType);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(crbAccountHolderType);
      });

      it('should add only unique CrbAccountHolderType to an array', () => {
        const crbAccountHolderTypeArray: ICrbAccountHolderType[] = [{ id: 123 }, { id: 456 }, { id: 26844 }];
        const crbAccountHolderTypeCollection: ICrbAccountHolderType[] = [{ id: 123 }];
        expectedResult = service.addCrbAccountHolderTypeToCollectionIfMissing(crbAccountHolderTypeCollection, ...crbAccountHolderTypeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const crbAccountHolderType: ICrbAccountHolderType = { id: 123 };
        const crbAccountHolderType2: ICrbAccountHolderType = { id: 456 };
        expectedResult = service.addCrbAccountHolderTypeToCollectionIfMissing([], crbAccountHolderType, crbAccountHolderType2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(crbAccountHolderType);
        expect(expectedResult).toContain(crbAccountHolderType2);
      });

      it('should accept null and undefined values', () => {
        const crbAccountHolderType: ICrbAccountHolderType = { id: 123 };
        expectedResult = service.addCrbAccountHolderTypeToCollectionIfMissing([], null, crbAccountHolderType, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(crbAccountHolderType);
      });

      it('should return initial array if no CrbAccountHolderType is added', () => {
        const crbAccountHolderTypeCollection: ICrbAccountHolderType[] = [{ id: 123 }];
        expectedResult = service.addCrbAccountHolderTypeToCollectionIfMissing(crbAccountHolderTypeCollection, undefined, null);
        expect(expectedResult).toEqual(crbAccountHolderTypeCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
