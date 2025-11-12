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

import { IDepartmentType, DepartmentType } from '../department-type.model';

import { DepartmentTypeService } from './department-type.service';

describe('DepartmentType Service', () => {
  let service: DepartmentTypeService;
  let httpMock: HttpTestingController;
  let elemDefault: IDepartmentType;
  let expectedResult: IDepartmentType | IDepartmentType[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(DepartmentTypeService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      departmentTypeCode: 'AAAAAAA',
      departmentType: 'AAAAAAA',
      departmentTypeDetails: 'AAAAAAA',
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

    it('should create a DepartmentType', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new DepartmentType()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a DepartmentType', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          departmentTypeCode: 'BBBBBB',
          departmentType: 'BBBBBB',
          departmentTypeDetails: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a DepartmentType', () => {
      const patchObject = Object.assign(
        {
          departmentTypeCode: 'BBBBBB',
          departmentType: 'BBBBBB',
          departmentTypeDetails: 'BBBBBB',
        },
        new DepartmentType()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of DepartmentType', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          departmentTypeCode: 'BBBBBB',
          departmentType: 'BBBBBB',
          departmentTypeDetails: 'BBBBBB',
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

    it('should delete a DepartmentType', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addDepartmentTypeToCollectionIfMissing', () => {
      it('should add a DepartmentType to an empty array', () => {
        const departmentType: IDepartmentType = { id: 123 };
        expectedResult = service.addDepartmentTypeToCollectionIfMissing([], departmentType);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(departmentType);
      });

      it('should not add a DepartmentType to an array that contains it', () => {
        const departmentType: IDepartmentType = { id: 123 };
        const departmentTypeCollection: IDepartmentType[] = [
          {
            ...departmentType,
          },
          { id: 456 },
        ];
        expectedResult = service.addDepartmentTypeToCollectionIfMissing(departmentTypeCollection, departmentType);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a DepartmentType to an array that doesn't contain it", () => {
        const departmentType: IDepartmentType = { id: 123 };
        const departmentTypeCollection: IDepartmentType[] = [{ id: 456 }];
        expectedResult = service.addDepartmentTypeToCollectionIfMissing(departmentTypeCollection, departmentType);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(departmentType);
      });

      it('should add only unique DepartmentType to an array', () => {
        const departmentTypeArray: IDepartmentType[] = [{ id: 123 }, { id: 456 }, { id: 48959 }];
        const departmentTypeCollection: IDepartmentType[] = [{ id: 123 }];
        expectedResult = service.addDepartmentTypeToCollectionIfMissing(departmentTypeCollection, ...departmentTypeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const departmentType: IDepartmentType = { id: 123 };
        const departmentType2: IDepartmentType = { id: 456 };
        expectedResult = service.addDepartmentTypeToCollectionIfMissing([], departmentType, departmentType2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(departmentType);
        expect(expectedResult).toContain(departmentType2);
      });

      it('should accept null and undefined values', () => {
        const departmentType: IDepartmentType = { id: 123 };
        expectedResult = service.addDepartmentTypeToCollectionIfMissing([], null, departmentType, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(departmentType);
      });

      it('should return initial array if no DepartmentType is added', () => {
        const departmentTypeCollection: IDepartmentType[] = [{ id: 123 }];
        expectedResult = service.addDepartmentTypeToCollectionIfMissing(departmentTypeCollection, undefined, null);
        expect(expectedResult).toEqual(departmentTypeCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
