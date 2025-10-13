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

import { IStaffRoleType, StaffRoleType } from '../staff-role-type.model';

import { StaffRoleTypeService } from './staff-role-type.service';

describe('StaffRoleType Service', () => {
  let service: StaffRoleTypeService;
  let httpMock: HttpTestingController;
  let elemDefault: IStaffRoleType;
  let expectedResult: IStaffRoleType | IStaffRoleType[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(StaffRoleTypeService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      staffRoleTypeCode: 'AAAAAAA',
      staffRoleType: 'AAAAAAA',
      staffRoleTypeDetails: 'AAAAAAA',
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

    it('should create a StaffRoleType', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new StaffRoleType()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a StaffRoleType', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          staffRoleTypeCode: 'BBBBBB',
          staffRoleType: 'BBBBBB',
          staffRoleTypeDetails: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a StaffRoleType', () => {
      const patchObject = Object.assign({}, new StaffRoleType());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of StaffRoleType', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          staffRoleTypeCode: 'BBBBBB',
          staffRoleType: 'BBBBBB',
          staffRoleTypeDetails: 'BBBBBB',
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

    it('should delete a StaffRoleType', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addStaffRoleTypeToCollectionIfMissing', () => {
      it('should add a StaffRoleType to an empty array', () => {
        const staffRoleType: IStaffRoleType = { id: 123 };
        expectedResult = service.addStaffRoleTypeToCollectionIfMissing([], staffRoleType);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(staffRoleType);
      });

      it('should not add a StaffRoleType to an array that contains it', () => {
        const staffRoleType: IStaffRoleType = { id: 123 };
        const staffRoleTypeCollection: IStaffRoleType[] = [
          {
            ...staffRoleType,
          },
          { id: 456 },
        ];
        expectedResult = service.addStaffRoleTypeToCollectionIfMissing(staffRoleTypeCollection, staffRoleType);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a StaffRoleType to an array that doesn't contain it", () => {
        const staffRoleType: IStaffRoleType = { id: 123 };
        const staffRoleTypeCollection: IStaffRoleType[] = [{ id: 456 }];
        expectedResult = service.addStaffRoleTypeToCollectionIfMissing(staffRoleTypeCollection, staffRoleType);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(staffRoleType);
      });

      it('should add only unique StaffRoleType to an array', () => {
        const staffRoleTypeArray: IStaffRoleType[] = [{ id: 123 }, { id: 456 }, { id: 71092 }];
        const staffRoleTypeCollection: IStaffRoleType[] = [{ id: 123 }];
        expectedResult = service.addStaffRoleTypeToCollectionIfMissing(staffRoleTypeCollection, ...staffRoleTypeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const staffRoleType: IStaffRoleType = { id: 123 };
        const staffRoleType2: IStaffRoleType = { id: 456 };
        expectedResult = service.addStaffRoleTypeToCollectionIfMissing([], staffRoleType, staffRoleType2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(staffRoleType);
        expect(expectedResult).toContain(staffRoleType2);
      });

      it('should accept null and undefined values', () => {
        const staffRoleType: IStaffRoleType = { id: 123 };
        expectedResult = service.addStaffRoleTypeToCollectionIfMissing([], null, staffRoleType, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(staffRoleType);
      });

      it('should return initial array if no StaffRoleType is added', () => {
        const staffRoleTypeCollection: IStaffRoleType[] = [{ id: 123 }];
        expectedResult = service.addStaffRoleTypeToCollectionIfMissing(staffRoleTypeCollection, undefined, null);
        expect(expectedResult).toEqual(staffRoleTypeCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
