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

import { IExecutiveCategoryType, ExecutiveCategoryType } from '../executive-category-type.model';

import { ExecutiveCategoryTypeService } from './executive-category-type.service';

describe('ExecutiveCategoryType Service', () => {
  let service: ExecutiveCategoryTypeService;
  let httpMock: HttpTestingController;
  let elemDefault: IExecutiveCategoryType;
  let expectedResult: IExecutiveCategoryType | IExecutiveCategoryType[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ExecutiveCategoryTypeService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      directorCategoryTypeCode: 'AAAAAAA',
      directorCategoryType: 'AAAAAAA',
      directorCategoryTypeDetails: 'AAAAAAA',
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

    it('should create a ExecutiveCategoryType', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new ExecutiveCategoryType()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ExecutiveCategoryType', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          directorCategoryTypeCode: 'BBBBBB',
          directorCategoryType: 'BBBBBB',
          directorCategoryTypeDetails: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ExecutiveCategoryType', () => {
      const patchObject = Object.assign(
        {
          directorCategoryTypeCode: 'BBBBBB',
          directorCategoryTypeDetails: 'BBBBBB',
        },
        new ExecutiveCategoryType()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ExecutiveCategoryType', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          directorCategoryTypeCode: 'BBBBBB',
          directorCategoryType: 'BBBBBB',
          directorCategoryTypeDetails: 'BBBBBB',
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

    it('should delete a ExecutiveCategoryType', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addExecutiveCategoryTypeToCollectionIfMissing', () => {
      it('should add a ExecutiveCategoryType to an empty array', () => {
        const executiveCategoryType: IExecutiveCategoryType = { id: 123 };
        expectedResult = service.addExecutiveCategoryTypeToCollectionIfMissing([], executiveCategoryType);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(executiveCategoryType);
      });

      it('should not add a ExecutiveCategoryType to an array that contains it', () => {
        const executiveCategoryType: IExecutiveCategoryType = { id: 123 };
        const executiveCategoryTypeCollection: IExecutiveCategoryType[] = [
          {
            ...executiveCategoryType,
          },
          { id: 456 },
        ];
        expectedResult = service.addExecutiveCategoryTypeToCollectionIfMissing(executiveCategoryTypeCollection, executiveCategoryType);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ExecutiveCategoryType to an array that doesn't contain it", () => {
        const executiveCategoryType: IExecutiveCategoryType = { id: 123 };
        const executiveCategoryTypeCollection: IExecutiveCategoryType[] = [{ id: 456 }];
        expectedResult = service.addExecutiveCategoryTypeToCollectionIfMissing(executiveCategoryTypeCollection, executiveCategoryType);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(executiveCategoryType);
      });

      it('should add only unique ExecutiveCategoryType to an array', () => {
        const executiveCategoryTypeArray: IExecutiveCategoryType[] = [{ id: 123 }, { id: 456 }, { id: 82068 }];
        const executiveCategoryTypeCollection: IExecutiveCategoryType[] = [{ id: 123 }];
        expectedResult = service.addExecutiveCategoryTypeToCollectionIfMissing(
          executiveCategoryTypeCollection,
          ...executiveCategoryTypeArray
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const executiveCategoryType: IExecutiveCategoryType = { id: 123 };
        const executiveCategoryType2: IExecutiveCategoryType = { id: 456 };
        expectedResult = service.addExecutiveCategoryTypeToCollectionIfMissing([], executiveCategoryType, executiveCategoryType2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(executiveCategoryType);
        expect(expectedResult).toContain(executiveCategoryType2);
      });

      it('should accept null and undefined values', () => {
        const executiveCategoryType: IExecutiveCategoryType = { id: 123 };
        expectedResult = service.addExecutiveCategoryTypeToCollectionIfMissing([], null, executiveCategoryType, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(executiveCategoryType);
      });

      it('should return initial array if no ExecutiveCategoryType is added', () => {
        const executiveCategoryTypeCollection: IExecutiveCategoryType[] = [{ id: 123 }];
        expectedResult = service.addExecutiveCategoryTypeToCollectionIfMissing(executiveCategoryTypeCollection, undefined, null);
        expect(expectedResult).toEqual(executiveCategoryTypeCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
