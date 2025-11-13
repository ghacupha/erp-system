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

import { IUltimateBeneficiaryCategory, UltimateBeneficiaryCategory } from '../ultimate-beneficiary-category.model';

import { UltimateBeneficiaryCategoryService } from './ultimate-beneficiary-category.service';

describe('UltimateBeneficiaryCategory Service', () => {
  let service: UltimateBeneficiaryCategoryService;
  let httpMock: HttpTestingController;
  let elemDefault: IUltimateBeneficiaryCategory;
  let expectedResult: IUltimateBeneficiaryCategory | IUltimateBeneficiaryCategory[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(UltimateBeneficiaryCategoryService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      ultimateBeneficiaryCategoryTypeCode: 'AAAAAAA',
      ultimateBeneficiaryType: 'AAAAAAA',
      ultimateBeneficiaryCategoryTypeDetails: 'AAAAAAA',
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

    it('should create a UltimateBeneficiaryCategory', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new UltimateBeneficiaryCategory()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a UltimateBeneficiaryCategory', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          ultimateBeneficiaryCategoryTypeCode: 'BBBBBB',
          ultimateBeneficiaryType: 'BBBBBB',
          ultimateBeneficiaryCategoryTypeDetails: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a UltimateBeneficiaryCategory', () => {
      const patchObject = Object.assign(
        {
          ultimateBeneficiaryCategoryTypeCode: 'BBBBBB',
          ultimateBeneficiaryType: 'BBBBBB',
          ultimateBeneficiaryCategoryTypeDetails: 'BBBBBB',
        },
        new UltimateBeneficiaryCategory()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of UltimateBeneficiaryCategory', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          ultimateBeneficiaryCategoryTypeCode: 'BBBBBB',
          ultimateBeneficiaryType: 'BBBBBB',
          ultimateBeneficiaryCategoryTypeDetails: 'BBBBBB',
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

    it('should delete a UltimateBeneficiaryCategory', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addUltimateBeneficiaryCategoryToCollectionIfMissing', () => {
      it('should add a UltimateBeneficiaryCategory to an empty array', () => {
        const ultimateBeneficiaryCategory: IUltimateBeneficiaryCategory = { id: 123 };
        expectedResult = service.addUltimateBeneficiaryCategoryToCollectionIfMissing([], ultimateBeneficiaryCategory);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(ultimateBeneficiaryCategory);
      });

      it('should not add a UltimateBeneficiaryCategory to an array that contains it', () => {
        const ultimateBeneficiaryCategory: IUltimateBeneficiaryCategory = { id: 123 };
        const ultimateBeneficiaryCategoryCollection: IUltimateBeneficiaryCategory[] = [
          {
            ...ultimateBeneficiaryCategory,
          },
          { id: 456 },
        ];
        expectedResult = service.addUltimateBeneficiaryCategoryToCollectionIfMissing(
          ultimateBeneficiaryCategoryCollection,
          ultimateBeneficiaryCategory
        );
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a UltimateBeneficiaryCategory to an array that doesn't contain it", () => {
        const ultimateBeneficiaryCategory: IUltimateBeneficiaryCategory = { id: 123 };
        const ultimateBeneficiaryCategoryCollection: IUltimateBeneficiaryCategory[] = [{ id: 456 }];
        expectedResult = service.addUltimateBeneficiaryCategoryToCollectionIfMissing(
          ultimateBeneficiaryCategoryCollection,
          ultimateBeneficiaryCategory
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(ultimateBeneficiaryCategory);
      });

      it('should add only unique UltimateBeneficiaryCategory to an array', () => {
        const ultimateBeneficiaryCategoryArray: IUltimateBeneficiaryCategory[] = [{ id: 123 }, { id: 456 }, { id: 51824 }];
        const ultimateBeneficiaryCategoryCollection: IUltimateBeneficiaryCategory[] = [{ id: 123 }];
        expectedResult = service.addUltimateBeneficiaryCategoryToCollectionIfMissing(
          ultimateBeneficiaryCategoryCollection,
          ...ultimateBeneficiaryCategoryArray
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const ultimateBeneficiaryCategory: IUltimateBeneficiaryCategory = { id: 123 };
        const ultimateBeneficiaryCategory2: IUltimateBeneficiaryCategory = { id: 456 };
        expectedResult = service.addUltimateBeneficiaryCategoryToCollectionIfMissing(
          [],
          ultimateBeneficiaryCategory,
          ultimateBeneficiaryCategory2
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(ultimateBeneficiaryCategory);
        expect(expectedResult).toContain(ultimateBeneficiaryCategory2);
      });

      it('should accept null and undefined values', () => {
        const ultimateBeneficiaryCategory: IUltimateBeneficiaryCategory = { id: 123 };
        expectedResult = service.addUltimateBeneficiaryCategoryToCollectionIfMissing([], null, ultimateBeneficiaryCategory, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(ultimateBeneficiaryCategory);
      });

      it('should return initial array if no UltimateBeneficiaryCategory is added', () => {
        const ultimateBeneficiaryCategoryCollection: IUltimateBeneficiaryCategory[] = [{ id: 123 }];
        expectedResult = service.addUltimateBeneficiaryCategoryToCollectionIfMissing(
          ultimateBeneficiaryCategoryCollection,
          undefined,
          null
        );
        expect(expectedResult).toEqual(ultimateBeneficiaryCategoryCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
