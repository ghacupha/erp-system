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

import { LoanAccountMutationTypes } from 'app/entities/enumerations/loan-account-mutation-types.model';
import { ILoanAccountCategory, LoanAccountCategory } from '../loan-account-category.model';

import { LoanAccountCategoryService } from './loan-account-category.service';

describe('LoanAccountCategory Service', () => {
  let service: LoanAccountCategoryService;
  let httpMock: HttpTestingController;
  let elemDefault: ILoanAccountCategory;
  let expectedResult: ILoanAccountCategory | ILoanAccountCategory[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(LoanAccountCategoryService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      loanAccountMutationCode: 'AAAAAAA',
      loanAccountMutationType: LoanAccountMutationTypes.RESTRUCTURED,
      loanAccountMutationDetails: 'AAAAAAA',
      loanAccountMutationDescription: 'AAAAAAA',
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

    it('should create a LoanAccountCategory', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new LoanAccountCategory()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a LoanAccountCategory', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          loanAccountMutationCode: 'BBBBBB',
          loanAccountMutationType: 'BBBBBB',
          loanAccountMutationDetails: 'BBBBBB',
          loanAccountMutationDescription: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a LoanAccountCategory', () => {
      const patchObject = Object.assign(
        {
          loanAccountMutationCode: 'BBBBBB',
          loanAccountMutationDetails: 'BBBBBB',
          loanAccountMutationDescription: 'BBBBBB',
        },
        new LoanAccountCategory()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of LoanAccountCategory', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          loanAccountMutationCode: 'BBBBBB',
          loanAccountMutationType: 'BBBBBB',
          loanAccountMutationDetails: 'BBBBBB',
          loanAccountMutationDescription: 'BBBBBB',
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

    it('should delete a LoanAccountCategory', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addLoanAccountCategoryToCollectionIfMissing', () => {
      it('should add a LoanAccountCategory to an empty array', () => {
        const loanAccountCategory: ILoanAccountCategory = { id: 123 };
        expectedResult = service.addLoanAccountCategoryToCollectionIfMissing([], loanAccountCategory);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(loanAccountCategory);
      });

      it('should not add a LoanAccountCategory to an array that contains it', () => {
        const loanAccountCategory: ILoanAccountCategory = { id: 123 };
        const loanAccountCategoryCollection: ILoanAccountCategory[] = [
          {
            ...loanAccountCategory,
          },
          { id: 456 },
        ];
        expectedResult = service.addLoanAccountCategoryToCollectionIfMissing(loanAccountCategoryCollection, loanAccountCategory);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a LoanAccountCategory to an array that doesn't contain it", () => {
        const loanAccountCategory: ILoanAccountCategory = { id: 123 };
        const loanAccountCategoryCollection: ILoanAccountCategory[] = [{ id: 456 }];
        expectedResult = service.addLoanAccountCategoryToCollectionIfMissing(loanAccountCategoryCollection, loanAccountCategory);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(loanAccountCategory);
      });

      it('should add only unique LoanAccountCategory to an array', () => {
        const loanAccountCategoryArray: ILoanAccountCategory[] = [{ id: 123 }, { id: 456 }, { id: 71362 }];
        const loanAccountCategoryCollection: ILoanAccountCategory[] = [{ id: 123 }];
        expectedResult = service.addLoanAccountCategoryToCollectionIfMissing(loanAccountCategoryCollection, ...loanAccountCategoryArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const loanAccountCategory: ILoanAccountCategory = { id: 123 };
        const loanAccountCategory2: ILoanAccountCategory = { id: 456 };
        expectedResult = service.addLoanAccountCategoryToCollectionIfMissing([], loanAccountCategory, loanAccountCategory2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(loanAccountCategory);
        expect(expectedResult).toContain(loanAccountCategory2);
      });

      it('should accept null and undefined values', () => {
        const loanAccountCategory: ILoanAccountCategory = { id: 123 };
        expectedResult = service.addLoanAccountCategoryToCollectionIfMissing([], null, loanAccountCategory, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(loanAccountCategory);
      });

      it('should return initial array if no LoanAccountCategory is added', () => {
        const loanAccountCategoryCollection: ILoanAccountCategory[] = [{ id: 123 }];
        expectedResult = service.addLoanAccountCategoryToCollectionIfMissing(loanAccountCategoryCollection, undefined, null);
        expect(expectedResult).toEqual(loanAccountCategoryCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
