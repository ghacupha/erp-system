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

import { IInsiderCategoryTypes, InsiderCategoryTypes } from '../insider-category-types.model';

import { InsiderCategoryTypesService } from './insider-category-types.service';

describe('InsiderCategoryTypes Service', () => {
  let service: InsiderCategoryTypesService;
  let httpMock: HttpTestingController;
  let elemDefault: IInsiderCategoryTypes;
  let expectedResult: IInsiderCategoryTypes | IInsiderCategoryTypes[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(InsiderCategoryTypesService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      insiderCategoryTypeCode: 'AAAAAAA',
      insiderCategoryTypeDetail: 'AAAAAAA',
      insiderCategoryDescription: 'AAAAAAA',
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

    it('should create a InsiderCategoryTypes', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new InsiderCategoryTypes()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a InsiderCategoryTypes', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          insiderCategoryTypeCode: 'BBBBBB',
          insiderCategoryTypeDetail: 'BBBBBB',
          insiderCategoryDescription: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a InsiderCategoryTypes', () => {
      const patchObject = Object.assign({}, new InsiderCategoryTypes());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of InsiderCategoryTypes', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          insiderCategoryTypeCode: 'BBBBBB',
          insiderCategoryTypeDetail: 'BBBBBB',
          insiderCategoryDescription: 'BBBBBB',
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

    it('should delete a InsiderCategoryTypes', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addInsiderCategoryTypesToCollectionIfMissing', () => {
      it('should add a InsiderCategoryTypes to an empty array', () => {
        const insiderCategoryTypes: IInsiderCategoryTypes = { id: 123 };
        expectedResult = service.addInsiderCategoryTypesToCollectionIfMissing([], insiderCategoryTypes);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(insiderCategoryTypes);
      });

      it('should not add a InsiderCategoryTypes to an array that contains it', () => {
        const insiderCategoryTypes: IInsiderCategoryTypes = { id: 123 };
        const insiderCategoryTypesCollection: IInsiderCategoryTypes[] = [
          {
            ...insiderCategoryTypes,
          },
          { id: 456 },
        ];
        expectedResult = service.addInsiderCategoryTypesToCollectionIfMissing(insiderCategoryTypesCollection, insiderCategoryTypes);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a InsiderCategoryTypes to an array that doesn't contain it", () => {
        const insiderCategoryTypes: IInsiderCategoryTypes = { id: 123 };
        const insiderCategoryTypesCollection: IInsiderCategoryTypes[] = [{ id: 456 }];
        expectedResult = service.addInsiderCategoryTypesToCollectionIfMissing(insiderCategoryTypesCollection, insiderCategoryTypes);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(insiderCategoryTypes);
      });

      it('should add only unique InsiderCategoryTypes to an array', () => {
        const insiderCategoryTypesArray: IInsiderCategoryTypes[] = [{ id: 123 }, { id: 456 }, { id: 48866 }];
        const insiderCategoryTypesCollection: IInsiderCategoryTypes[] = [{ id: 123 }];
        expectedResult = service.addInsiderCategoryTypesToCollectionIfMissing(insiderCategoryTypesCollection, ...insiderCategoryTypesArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const insiderCategoryTypes: IInsiderCategoryTypes = { id: 123 };
        const insiderCategoryTypes2: IInsiderCategoryTypes = { id: 456 };
        expectedResult = service.addInsiderCategoryTypesToCollectionIfMissing([], insiderCategoryTypes, insiderCategoryTypes2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(insiderCategoryTypes);
        expect(expectedResult).toContain(insiderCategoryTypes2);
      });

      it('should accept null and undefined values', () => {
        const insiderCategoryTypes: IInsiderCategoryTypes = { id: 123 };
        expectedResult = service.addInsiderCategoryTypesToCollectionIfMissing([], null, insiderCategoryTypes, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(insiderCategoryTypes);
      });

      it('should return initial array if no InsiderCategoryTypes is added', () => {
        const insiderCategoryTypesCollection: IInsiderCategoryTypes[] = [{ id: 123 }];
        expectedResult = service.addInsiderCategoryTypesToCollectionIfMissing(insiderCategoryTypesCollection, undefined, null);
        expect(expectedResult).toEqual(insiderCategoryTypesCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
