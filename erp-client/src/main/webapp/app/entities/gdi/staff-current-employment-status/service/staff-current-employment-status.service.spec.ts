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

import { IStaffCurrentEmploymentStatus, StaffCurrentEmploymentStatus } from '../staff-current-employment-status.model';

import { StaffCurrentEmploymentStatusService } from './staff-current-employment-status.service';

describe('StaffCurrentEmploymentStatus Service', () => {
  let service: StaffCurrentEmploymentStatusService;
  let httpMock: HttpTestingController;
  let elemDefault: IStaffCurrentEmploymentStatus;
  let expectedResult: IStaffCurrentEmploymentStatus | IStaffCurrentEmploymentStatus[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(StaffCurrentEmploymentStatusService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      staffCurrentEmploymentStatusTypeCode: 'AAAAAAA',
      staffCurrentEmploymentStatusType: 'AAAAAAA',
      staffCurrentEmploymentStatusTypeDetails: 'AAAAAAA',
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

    it('should create a StaffCurrentEmploymentStatus', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new StaffCurrentEmploymentStatus()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a StaffCurrentEmploymentStatus', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          staffCurrentEmploymentStatusTypeCode: 'BBBBBB',
          staffCurrentEmploymentStatusType: 'BBBBBB',
          staffCurrentEmploymentStatusTypeDetails: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a StaffCurrentEmploymentStatus', () => {
      const patchObject = Object.assign(
        {
          staffCurrentEmploymentStatusTypeCode: 'BBBBBB',
          staffCurrentEmploymentStatusType: 'BBBBBB',
        },
        new StaffCurrentEmploymentStatus()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of StaffCurrentEmploymentStatus', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          staffCurrentEmploymentStatusTypeCode: 'BBBBBB',
          staffCurrentEmploymentStatusType: 'BBBBBB',
          staffCurrentEmploymentStatusTypeDetails: 'BBBBBB',
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

    it('should delete a StaffCurrentEmploymentStatus', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addStaffCurrentEmploymentStatusToCollectionIfMissing', () => {
      it('should add a StaffCurrentEmploymentStatus to an empty array', () => {
        const staffCurrentEmploymentStatus: IStaffCurrentEmploymentStatus = { id: 123 };
        expectedResult = service.addStaffCurrentEmploymentStatusToCollectionIfMissing([], staffCurrentEmploymentStatus);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(staffCurrentEmploymentStatus);
      });

      it('should not add a StaffCurrentEmploymentStatus to an array that contains it', () => {
        const staffCurrentEmploymentStatus: IStaffCurrentEmploymentStatus = { id: 123 };
        const staffCurrentEmploymentStatusCollection: IStaffCurrentEmploymentStatus[] = [
          {
            ...staffCurrentEmploymentStatus,
          },
          { id: 456 },
        ];
        expectedResult = service.addStaffCurrentEmploymentStatusToCollectionIfMissing(
          staffCurrentEmploymentStatusCollection,
          staffCurrentEmploymentStatus
        );
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a StaffCurrentEmploymentStatus to an array that doesn't contain it", () => {
        const staffCurrentEmploymentStatus: IStaffCurrentEmploymentStatus = { id: 123 };
        const staffCurrentEmploymentStatusCollection: IStaffCurrentEmploymentStatus[] = [{ id: 456 }];
        expectedResult = service.addStaffCurrentEmploymentStatusToCollectionIfMissing(
          staffCurrentEmploymentStatusCollection,
          staffCurrentEmploymentStatus
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(staffCurrentEmploymentStatus);
      });

      it('should add only unique StaffCurrentEmploymentStatus to an array', () => {
        const staffCurrentEmploymentStatusArray: IStaffCurrentEmploymentStatus[] = [{ id: 123 }, { id: 456 }, { id: 40389 }];
        const staffCurrentEmploymentStatusCollection: IStaffCurrentEmploymentStatus[] = [{ id: 123 }];
        expectedResult = service.addStaffCurrentEmploymentStatusToCollectionIfMissing(
          staffCurrentEmploymentStatusCollection,
          ...staffCurrentEmploymentStatusArray
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const staffCurrentEmploymentStatus: IStaffCurrentEmploymentStatus = { id: 123 };
        const staffCurrentEmploymentStatus2: IStaffCurrentEmploymentStatus = { id: 456 };
        expectedResult = service.addStaffCurrentEmploymentStatusToCollectionIfMissing(
          [],
          staffCurrentEmploymentStatus,
          staffCurrentEmploymentStatus2
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(staffCurrentEmploymentStatus);
        expect(expectedResult).toContain(staffCurrentEmploymentStatus2);
      });

      it('should accept null and undefined values', () => {
        const staffCurrentEmploymentStatus: IStaffCurrentEmploymentStatus = { id: 123 };
        expectedResult = service.addStaffCurrentEmploymentStatusToCollectionIfMissing([], null, staffCurrentEmploymentStatus, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(staffCurrentEmploymentStatus);
      });

      it('should return initial array if no StaffCurrentEmploymentStatus is added', () => {
        const staffCurrentEmploymentStatusCollection: IStaffCurrentEmploymentStatus[] = [{ id: 123 }];
        expectedResult = service.addStaffCurrentEmploymentStatusToCollectionIfMissing(
          staffCurrentEmploymentStatusCollection,
          undefined,
          null
        );
        expect(expectedResult).toEqual(staffCurrentEmploymentStatusCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
