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

import { ICrbSubmittingInstitutionCategory, CrbSubmittingInstitutionCategory } from '../crb-submitting-institution-category.model';

import { CrbSubmittingInstitutionCategoryService } from './crb-submitting-institution-category.service';

describe('CrbSubmittingInstitutionCategory Service', () => {
  let service: CrbSubmittingInstitutionCategoryService;
  let httpMock: HttpTestingController;
  let elemDefault: ICrbSubmittingInstitutionCategory;
  let expectedResult: ICrbSubmittingInstitutionCategory | ICrbSubmittingInstitutionCategory[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CrbSubmittingInstitutionCategoryService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      submittingInstitutionCategoryTypeCode: 'AAAAAAA',
      submittingInstitutionCategoryType: 'AAAAAAA',
      submittingInstitutionCategoryDetails: 'AAAAAAA',
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

    it('should create a CrbSubmittingInstitutionCategory', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new CrbSubmittingInstitutionCategory()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CrbSubmittingInstitutionCategory', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          submittingInstitutionCategoryTypeCode: 'BBBBBB',
          submittingInstitutionCategoryType: 'BBBBBB',
          submittingInstitutionCategoryDetails: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a CrbSubmittingInstitutionCategory', () => {
      const patchObject = Object.assign(
        {
          submittingInstitutionCategoryTypeCode: 'BBBBBB',
          submittingInstitutionCategoryDetails: 'BBBBBB',
        },
        new CrbSubmittingInstitutionCategory()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of CrbSubmittingInstitutionCategory', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          submittingInstitutionCategoryTypeCode: 'BBBBBB',
          submittingInstitutionCategoryType: 'BBBBBB',
          submittingInstitutionCategoryDetails: 'BBBBBB',
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

    it('should delete a CrbSubmittingInstitutionCategory', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addCrbSubmittingInstitutionCategoryToCollectionIfMissing', () => {
      it('should add a CrbSubmittingInstitutionCategory to an empty array', () => {
        const crbSubmittingInstitutionCategory: ICrbSubmittingInstitutionCategory = { id: 123 };
        expectedResult = service.addCrbSubmittingInstitutionCategoryToCollectionIfMissing([], crbSubmittingInstitutionCategory);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(crbSubmittingInstitutionCategory);
      });

      it('should not add a CrbSubmittingInstitutionCategory to an array that contains it', () => {
        const crbSubmittingInstitutionCategory: ICrbSubmittingInstitutionCategory = { id: 123 };
        const crbSubmittingInstitutionCategoryCollection: ICrbSubmittingInstitutionCategory[] = [
          {
            ...crbSubmittingInstitutionCategory,
          },
          { id: 456 },
        ];
        expectedResult = service.addCrbSubmittingInstitutionCategoryToCollectionIfMissing(
          crbSubmittingInstitutionCategoryCollection,
          crbSubmittingInstitutionCategory
        );
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CrbSubmittingInstitutionCategory to an array that doesn't contain it", () => {
        const crbSubmittingInstitutionCategory: ICrbSubmittingInstitutionCategory = { id: 123 };
        const crbSubmittingInstitutionCategoryCollection: ICrbSubmittingInstitutionCategory[] = [{ id: 456 }];
        expectedResult = service.addCrbSubmittingInstitutionCategoryToCollectionIfMissing(
          crbSubmittingInstitutionCategoryCollection,
          crbSubmittingInstitutionCategory
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(crbSubmittingInstitutionCategory);
      });

      it('should add only unique CrbSubmittingInstitutionCategory to an array', () => {
        const crbSubmittingInstitutionCategoryArray: ICrbSubmittingInstitutionCategory[] = [{ id: 123 }, { id: 456 }, { id: 37615 }];
        const crbSubmittingInstitutionCategoryCollection: ICrbSubmittingInstitutionCategory[] = [{ id: 123 }];
        expectedResult = service.addCrbSubmittingInstitutionCategoryToCollectionIfMissing(
          crbSubmittingInstitutionCategoryCollection,
          ...crbSubmittingInstitutionCategoryArray
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const crbSubmittingInstitutionCategory: ICrbSubmittingInstitutionCategory = { id: 123 };
        const crbSubmittingInstitutionCategory2: ICrbSubmittingInstitutionCategory = { id: 456 };
        expectedResult = service.addCrbSubmittingInstitutionCategoryToCollectionIfMissing(
          [],
          crbSubmittingInstitutionCategory,
          crbSubmittingInstitutionCategory2
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(crbSubmittingInstitutionCategory);
        expect(expectedResult).toContain(crbSubmittingInstitutionCategory2);
      });

      it('should accept null and undefined values', () => {
        const crbSubmittingInstitutionCategory: ICrbSubmittingInstitutionCategory = { id: 123 };
        expectedResult = service.addCrbSubmittingInstitutionCategoryToCollectionIfMissing(
          [],
          null,
          crbSubmittingInstitutionCategory,
          undefined
        );
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(crbSubmittingInstitutionCategory);
      });

      it('should return initial array if no CrbSubmittingInstitutionCategory is added', () => {
        const crbSubmittingInstitutionCategoryCollection: ICrbSubmittingInstitutionCategory[] = [{ id: 123 }];
        expectedResult = service.addCrbSubmittingInstitutionCategoryToCollectionIfMissing(
          crbSubmittingInstitutionCategoryCollection,
          undefined,
          null
        );
        expect(expectedResult).toEqual(crbSubmittingInstitutionCategoryCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
