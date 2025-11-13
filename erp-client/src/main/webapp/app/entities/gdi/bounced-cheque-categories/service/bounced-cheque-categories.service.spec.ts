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

import { IBouncedChequeCategories, BouncedChequeCategories } from '../bounced-cheque-categories.model';

import { BouncedChequeCategoriesService } from './bounced-cheque-categories.service';

describe('BouncedChequeCategories Service', () => {
  let service: BouncedChequeCategoriesService;
  let httpMock: HttpTestingController;
  let elemDefault: IBouncedChequeCategories;
  let expectedResult: IBouncedChequeCategories | IBouncedChequeCategories[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(BouncedChequeCategoriesService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      bouncedChequeCategoryTypeCode: 'AAAAAAA',
      bouncedChequeCategoryType: 'AAAAAAA',
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

    it('should create a BouncedChequeCategories', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new BouncedChequeCategories()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a BouncedChequeCategories', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          bouncedChequeCategoryTypeCode: 'BBBBBB',
          bouncedChequeCategoryType: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a BouncedChequeCategories', () => {
      const patchObject = Object.assign(
        {
          bouncedChequeCategoryTypeCode: 'BBBBBB',
          bouncedChequeCategoryType: 'BBBBBB',
        },
        new BouncedChequeCategories()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of BouncedChequeCategories', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          bouncedChequeCategoryTypeCode: 'BBBBBB',
          bouncedChequeCategoryType: 'BBBBBB',
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

    it('should delete a BouncedChequeCategories', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addBouncedChequeCategoriesToCollectionIfMissing', () => {
      it('should add a BouncedChequeCategories to an empty array', () => {
        const bouncedChequeCategories: IBouncedChequeCategories = { id: 123 };
        expectedResult = service.addBouncedChequeCategoriesToCollectionIfMissing([], bouncedChequeCategories);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(bouncedChequeCategories);
      });

      it('should not add a BouncedChequeCategories to an array that contains it', () => {
        const bouncedChequeCategories: IBouncedChequeCategories = { id: 123 };
        const bouncedChequeCategoriesCollection: IBouncedChequeCategories[] = [
          {
            ...bouncedChequeCategories,
          },
          { id: 456 },
        ];
        expectedResult = service.addBouncedChequeCategoriesToCollectionIfMissing(
          bouncedChequeCategoriesCollection,
          bouncedChequeCategories
        );
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a BouncedChequeCategories to an array that doesn't contain it", () => {
        const bouncedChequeCategories: IBouncedChequeCategories = { id: 123 };
        const bouncedChequeCategoriesCollection: IBouncedChequeCategories[] = [{ id: 456 }];
        expectedResult = service.addBouncedChequeCategoriesToCollectionIfMissing(
          bouncedChequeCategoriesCollection,
          bouncedChequeCategories
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(bouncedChequeCategories);
      });

      it('should add only unique BouncedChequeCategories to an array', () => {
        const bouncedChequeCategoriesArray: IBouncedChequeCategories[] = [{ id: 123 }, { id: 456 }, { id: 9786 }];
        const bouncedChequeCategoriesCollection: IBouncedChequeCategories[] = [{ id: 123 }];
        expectedResult = service.addBouncedChequeCategoriesToCollectionIfMissing(
          bouncedChequeCategoriesCollection,
          ...bouncedChequeCategoriesArray
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const bouncedChequeCategories: IBouncedChequeCategories = { id: 123 };
        const bouncedChequeCategories2: IBouncedChequeCategories = { id: 456 };
        expectedResult = service.addBouncedChequeCategoriesToCollectionIfMissing([], bouncedChequeCategories, bouncedChequeCategories2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(bouncedChequeCategories);
        expect(expectedResult).toContain(bouncedChequeCategories2);
      });

      it('should accept null and undefined values', () => {
        const bouncedChequeCategories: IBouncedChequeCategories = { id: 123 };
        expectedResult = service.addBouncedChequeCategoriesToCollectionIfMissing([], null, bouncedChequeCategories, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(bouncedChequeCategories);
      });

      it('should return initial array if no BouncedChequeCategories is added', () => {
        const bouncedChequeCategoriesCollection: IBouncedChequeCategories[] = [{ id: 123 }];
        expectedResult = service.addBouncedChequeCategoriesToCollectionIfMissing(bouncedChequeCategoriesCollection, undefined, null);
        expect(expectedResult).toEqual(bouncedChequeCategoriesCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
