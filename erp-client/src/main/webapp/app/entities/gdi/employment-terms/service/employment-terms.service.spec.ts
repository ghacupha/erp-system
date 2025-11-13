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

import { IEmploymentTerms, EmploymentTerms } from '../employment-terms.model';

import { EmploymentTermsService } from './employment-terms.service';

describe('EmploymentTerms Service', () => {
  let service: EmploymentTermsService;
  let httpMock: HttpTestingController;
  let elemDefault: IEmploymentTerms;
  let expectedResult: IEmploymentTerms | IEmploymentTerms[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(EmploymentTermsService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      employmentTermsCode: 'AAAAAAA',
      employmentTermsStatus: 'AAAAAAA',
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

    it('should create a EmploymentTerms', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new EmploymentTerms()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a EmploymentTerms', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          employmentTermsCode: 'BBBBBB',
          employmentTermsStatus: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a EmploymentTerms', () => {
      const patchObject = Object.assign(
        {
          employmentTermsCode: 'BBBBBB',
          employmentTermsStatus: 'BBBBBB',
        },
        new EmploymentTerms()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of EmploymentTerms', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          employmentTermsCode: 'BBBBBB',
          employmentTermsStatus: 'BBBBBB',
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

    it('should delete a EmploymentTerms', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addEmploymentTermsToCollectionIfMissing', () => {
      it('should add a EmploymentTerms to an empty array', () => {
        const employmentTerms: IEmploymentTerms = { id: 123 };
        expectedResult = service.addEmploymentTermsToCollectionIfMissing([], employmentTerms);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(employmentTerms);
      });

      it('should not add a EmploymentTerms to an array that contains it', () => {
        const employmentTerms: IEmploymentTerms = { id: 123 };
        const employmentTermsCollection: IEmploymentTerms[] = [
          {
            ...employmentTerms,
          },
          { id: 456 },
        ];
        expectedResult = service.addEmploymentTermsToCollectionIfMissing(employmentTermsCollection, employmentTerms);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a EmploymentTerms to an array that doesn't contain it", () => {
        const employmentTerms: IEmploymentTerms = { id: 123 };
        const employmentTermsCollection: IEmploymentTerms[] = [{ id: 456 }];
        expectedResult = service.addEmploymentTermsToCollectionIfMissing(employmentTermsCollection, employmentTerms);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(employmentTerms);
      });

      it('should add only unique EmploymentTerms to an array', () => {
        const employmentTermsArray: IEmploymentTerms[] = [{ id: 123 }, { id: 456 }, { id: 30965 }];
        const employmentTermsCollection: IEmploymentTerms[] = [{ id: 123 }];
        expectedResult = service.addEmploymentTermsToCollectionIfMissing(employmentTermsCollection, ...employmentTermsArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const employmentTerms: IEmploymentTerms = { id: 123 };
        const employmentTerms2: IEmploymentTerms = { id: 456 };
        expectedResult = service.addEmploymentTermsToCollectionIfMissing([], employmentTerms, employmentTerms2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(employmentTerms);
        expect(expectedResult).toContain(employmentTerms2);
      });

      it('should accept null and undefined values', () => {
        const employmentTerms: IEmploymentTerms = { id: 123 };
        expectedResult = service.addEmploymentTermsToCollectionIfMissing([], null, employmentTerms, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(employmentTerms);
      });

      it('should return initial array if no EmploymentTerms is added', () => {
        const employmentTermsCollection: IEmploymentTerms[] = [{ id: 123 }];
        expectedResult = service.addEmploymentTermsToCollectionIfMissing(employmentTermsCollection, undefined, null);
        expect(expectedResult).toEqual(employmentTermsCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
