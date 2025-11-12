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

import { ICategoryOfSecurity, CategoryOfSecurity } from '../category-of-security.model';

import { CategoryOfSecurityService } from './category-of-security.service';

describe('CategoryOfSecurity Service', () => {
  let service: CategoryOfSecurityService;
  let httpMock: HttpTestingController;
  let elemDefault: ICategoryOfSecurity;
  let expectedResult: ICategoryOfSecurity | ICategoryOfSecurity[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CategoryOfSecurityService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      categoryOfSecurity: 'AAAAAAA',
      categoryOfSecurityDetails: 'AAAAAAA',
      categoryOfSecurityDescription: 'AAAAAAA',
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

    it('should create a CategoryOfSecurity', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new CategoryOfSecurity()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CategoryOfSecurity', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          categoryOfSecurity: 'BBBBBB',
          categoryOfSecurityDetails: 'BBBBBB',
          categoryOfSecurityDescription: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a CategoryOfSecurity', () => {
      const patchObject = Object.assign(
        {
          categoryOfSecurity: 'BBBBBB',
          categoryOfSecurityDescription: 'BBBBBB',
        },
        new CategoryOfSecurity()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of CategoryOfSecurity', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          categoryOfSecurity: 'BBBBBB',
          categoryOfSecurityDetails: 'BBBBBB',
          categoryOfSecurityDescription: 'BBBBBB',
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

    it('should delete a CategoryOfSecurity', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addCategoryOfSecurityToCollectionIfMissing', () => {
      it('should add a CategoryOfSecurity to an empty array', () => {
        const categoryOfSecurity: ICategoryOfSecurity = { id: 123 };
        expectedResult = service.addCategoryOfSecurityToCollectionIfMissing([], categoryOfSecurity);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(categoryOfSecurity);
      });

      it('should not add a CategoryOfSecurity to an array that contains it', () => {
        const categoryOfSecurity: ICategoryOfSecurity = { id: 123 };
        const categoryOfSecurityCollection: ICategoryOfSecurity[] = [
          {
            ...categoryOfSecurity,
          },
          { id: 456 },
        ];
        expectedResult = service.addCategoryOfSecurityToCollectionIfMissing(categoryOfSecurityCollection, categoryOfSecurity);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CategoryOfSecurity to an array that doesn't contain it", () => {
        const categoryOfSecurity: ICategoryOfSecurity = { id: 123 };
        const categoryOfSecurityCollection: ICategoryOfSecurity[] = [{ id: 456 }];
        expectedResult = service.addCategoryOfSecurityToCollectionIfMissing(categoryOfSecurityCollection, categoryOfSecurity);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(categoryOfSecurity);
      });

      it('should add only unique CategoryOfSecurity to an array', () => {
        const categoryOfSecurityArray: ICategoryOfSecurity[] = [{ id: 123 }, { id: 456 }, { id: 26874 }];
        const categoryOfSecurityCollection: ICategoryOfSecurity[] = [{ id: 123 }];
        expectedResult = service.addCategoryOfSecurityToCollectionIfMissing(categoryOfSecurityCollection, ...categoryOfSecurityArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const categoryOfSecurity: ICategoryOfSecurity = { id: 123 };
        const categoryOfSecurity2: ICategoryOfSecurity = { id: 456 };
        expectedResult = service.addCategoryOfSecurityToCollectionIfMissing([], categoryOfSecurity, categoryOfSecurity2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(categoryOfSecurity);
        expect(expectedResult).toContain(categoryOfSecurity2);
      });

      it('should accept null and undefined values', () => {
        const categoryOfSecurity: ICategoryOfSecurity = { id: 123 };
        expectedResult = service.addCategoryOfSecurityToCollectionIfMissing([], null, categoryOfSecurity, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(categoryOfSecurity);
      });

      it('should return initial array if no CategoryOfSecurity is added', () => {
        const categoryOfSecurityCollection: ICategoryOfSecurity[] = [{ id: 123 }];
        expectedResult = service.addCategoryOfSecurityToCollectionIfMissing(categoryOfSecurityCollection, undefined, null);
        expect(expectedResult).toEqual(categoryOfSecurityCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
