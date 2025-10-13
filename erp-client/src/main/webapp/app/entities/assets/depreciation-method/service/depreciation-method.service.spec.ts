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

import { DepreciationTypes } from 'app/entities/enumerations/depreciation-types.model';
import { IDepreciationMethod, DepreciationMethod } from '../depreciation-method.model';

import { DepreciationMethodService } from './depreciation-method.service';

describe('DepreciationMethod Service', () => {
  let service: DepreciationMethodService;
  let httpMock: HttpTestingController;
  let elemDefault: IDepreciationMethod;
  let expectedResult: IDepreciationMethod | IDepreciationMethod[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(DepreciationMethodService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      depreciationMethodName: 'AAAAAAA',
      description: 'AAAAAAA',
      depreciationType: DepreciationTypes.STRAIGHT_LINE,
      remarks: 'AAAAAAA',
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

    it('should create a DepreciationMethod', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new DepreciationMethod()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a DepreciationMethod', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          depreciationMethodName: 'BBBBBB',
          description: 'BBBBBB',
          depreciationType: 'BBBBBB',
          remarks: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a DepreciationMethod', () => {
      const patchObject = Object.assign(
        {
          depreciationMethodName: 'BBBBBB',
          depreciationType: 'BBBBBB',
        },
        new DepreciationMethod()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of DepreciationMethod', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          depreciationMethodName: 'BBBBBB',
          description: 'BBBBBB',
          depreciationType: 'BBBBBB',
          remarks: 'BBBBBB',
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

    it('should delete a DepreciationMethod', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addDepreciationMethodToCollectionIfMissing', () => {
      it('should add a DepreciationMethod to an empty array', () => {
        const depreciationMethod: IDepreciationMethod = { id: 123 };
        expectedResult = service.addDepreciationMethodToCollectionIfMissing([], depreciationMethod);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(depreciationMethod);
      });

      it('should not add a DepreciationMethod to an array that contains it', () => {
        const depreciationMethod: IDepreciationMethod = { id: 123 };
        const depreciationMethodCollection: IDepreciationMethod[] = [
          {
            ...depreciationMethod,
          },
          { id: 456 },
        ];
        expectedResult = service.addDepreciationMethodToCollectionIfMissing(depreciationMethodCollection, depreciationMethod);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a DepreciationMethod to an array that doesn't contain it", () => {
        const depreciationMethod: IDepreciationMethod = { id: 123 };
        const depreciationMethodCollection: IDepreciationMethod[] = [{ id: 456 }];
        expectedResult = service.addDepreciationMethodToCollectionIfMissing(depreciationMethodCollection, depreciationMethod);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(depreciationMethod);
      });

      it('should add only unique DepreciationMethod to an array', () => {
        const depreciationMethodArray: IDepreciationMethod[] = [{ id: 123 }, { id: 456 }, { id: 93665 }];
        const depreciationMethodCollection: IDepreciationMethod[] = [{ id: 123 }];
        expectedResult = service.addDepreciationMethodToCollectionIfMissing(depreciationMethodCollection, ...depreciationMethodArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const depreciationMethod: IDepreciationMethod = { id: 123 };
        const depreciationMethod2: IDepreciationMethod = { id: 456 };
        expectedResult = service.addDepreciationMethodToCollectionIfMissing([], depreciationMethod, depreciationMethod2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(depreciationMethod);
        expect(expectedResult).toContain(depreciationMethod2);
      });

      it('should accept null and undefined values', () => {
        const depreciationMethod: IDepreciationMethod = { id: 123 };
        expectedResult = service.addDepreciationMethodToCollectionIfMissing([], null, depreciationMethod, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(depreciationMethod);
      });

      it('should return initial array if no DepreciationMethod is added', () => {
        const depreciationMethodCollection: IDepreciationMethod[] = [{ id: 123 }];
        expectedResult = service.addDepreciationMethodToCollectionIfMissing(depreciationMethodCollection, undefined, null);
        expect(expectedResult).toEqual(depreciationMethodCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
