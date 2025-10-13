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

import { IManagementMemberType, ManagementMemberType } from '../management-member-type.model';

import { ManagementMemberTypeService } from './management-member-type.service';

describe('ManagementMemberType Service', () => {
  let service: ManagementMemberTypeService;
  let httpMock: HttpTestingController;
  let elemDefault: IManagementMemberType;
  let expectedResult: IManagementMemberType | IManagementMemberType[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ManagementMemberTypeService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      managementMemberTypeCode: 'AAAAAAA',
      managementMemberType: 'AAAAAAA',
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

    it('should create a ManagementMemberType', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new ManagementMemberType()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ManagementMemberType', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          managementMemberTypeCode: 'BBBBBB',
          managementMemberType: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ManagementMemberType', () => {
      const patchObject = Object.assign(
        {
          managementMemberTypeCode: 'BBBBBB',
        },
        new ManagementMemberType()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ManagementMemberType', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          managementMemberTypeCode: 'BBBBBB',
          managementMemberType: 'BBBBBB',
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

    it('should delete a ManagementMemberType', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addManagementMemberTypeToCollectionIfMissing', () => {
      it('should add a ManagementMemberType to an empty array', () => {
        const managementMemberType: IManagementMemberType = { id: 123 };
        expectedResult = service.addManagementMemberTypeToCollectionIfMissing([], managementMemberType);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(managementMemberType);
      });

      it('should not add a ManagementMemberType to an array that contains it', () => {
        const managementMemberType: IManagementMemberType = { id: 123 };
        const managementMemberTypeCollection: IManagementMemberType[] = [
          {
            ...managementMemberType,
          },
          { id: 456 },
        ];
        expectedResult = service.addManagementMemberTypeToCollectionIfMissing(managementMemberTypeCollection, managementMemberType);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ManagementMemberType to an array that doesn't contain it", () => {
        const managementMemberType: IManagementMemberType = { id: 123 };
        const managementMemberTypeCollection: IManagementMemberType[] = [{ id: 456 }];
        expectedResult = service.addManagementMemberTypeToCollectionIfMissing(managementMemberTypeCollection, managementMemberType);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(managementMemberType);
      });

      it('should add only unique ManagementMemberType to an array', () => {
        const managementMemberTypeArray: IManagementMemberType[] = [{ id: 123 }, { id: 456 }, { id: 73012 }];
        const managementMemberTypeCollection: IManagementMemberType[] = [{ id: 123 }];
        expectedResult = service.addManagementMemberTypeToCollectionIfMissing(managementMemberTypeCollection, ...managementMemberTypeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const managementMemberType: IManagementMemberType = { id: 123 };
        const managementMemberType2: IManagementMemberType = { id: 456 };
        expectedResult = service.addManagementMemberTypeToCollectionIfMissing([], managementMemberType, managementMemberType2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(managementMemberType);
        expect(expectedResult).toContain(managementMemberType2);
      });

      it('should accept null and undefined values', () => {
        const managementMemberType: IManagementMemberType = { id: 123 };
        expectedResult = service.addManagementMemberTypeToCollectionIfMissing([], null, managementMemberType, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(managementMemberType);
      });

      it('should return initial array if no ManagementMemberType is added', () => {
        const managementMemberTypeCollection: IManagementMemberType[] = [{ id: 123 }];
        expectedResult = service.addManagementMemberTypeToCollectionIfMissing(managementMemberTypeCollection, undefined, null);
        expect(expectedResult).toEqual(managementMemberTypeCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
