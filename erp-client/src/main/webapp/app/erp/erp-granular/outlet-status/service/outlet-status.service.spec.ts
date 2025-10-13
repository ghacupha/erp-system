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

import { IOutletStatus, OutletStatus } from '../outlet-status.model';

import { OutletStatusService } from './outlet-status.service';
import { BranchStatusType } from '../../../erp-common/enumerations/branch-status-type.model';

describe('OutletStatus Service', () => {
  let service: OutletStatusService;
  let httpMock: HttpTestingController;
  let elemDefault: IOutletStatus;
  let expectedResult: IOutletStatus | IOutletStatus[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(OutletStatusService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      branchStatusTypeCode: 'AAAAAAA',
      branchStatusType: BranchStatusType.ACTIVE,
      branchStatusTypeDescription: 'AAAAAAA',
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

    it('should create a OutletStatus', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new OutletStatus()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a OutletStatus', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          branchStatusTypeCode: 'BBBBBB',
          branchStatusType: 'BBBBBB',
          branchStatusTypeDescription: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a OutletStatus', () => {
      const patchObject = Object.assign(
        {
          branchStatusType: 'BBBBBB',
        },
        new OutletStatus()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of OutletStatus', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          branchStatusTypeCode: 'BBBBBB',
          branchStatusType: 'BBBBBB',
          branchStatusTypeDescription: 'BBBBBB',
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

    it('should delete a OutletStatus', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addOutletStatusToCollectionIfMissing', () => {
      it('should add a OutletStatus to an empty array', () => {
        const outletStatus: IOutletStatus = { id: 123 };
        expectedResult = service.addOutletStatusToCollectionIfMissing([], outletStatus);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(outletStatus);
      });

      it('should not add a OutletStatus to an array that contains it', () => {
        const outletStatus: IOutletStatus = { id: 123 };
        const outletStatusCollection: IOutletStatus[] = [
          {
            ...outletStatus,
          },
          { id: 456 },
        ];
        expectedResult = service.addOutletStatusToCollectionIfMissing(outletStatusCollection, outletStatus);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a OutletStatus to an array that doesn't contain it", () => {
        const outletStatus: IOutletStatus = { id: 123 };
        const outletStatusCollection: IOutletStatus[] = [{ id: 456 }];
        expectedResult = service.addOutletStatusToCollectionIfMissing(outletStatusCollection, outletStatus);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(outletStatus);
      });

      it('should add only unique OutletStatus to an array', () => {
        const outletStatusArray: IOutletStatus[] = [{ id: 123 }, { id: 456 }, { id: 93917 }];
        const outletStatusCollection: IOutletStatus[] = [{ id: 123 }];
        expectedResult = service.addOutletStatusToCollectionIfMissing(outletStatusCollection, ...outletStatusArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const outletStatus: IOutletStatus = { id: 123 };
        const outletStatus2: IOutletStatus = { id: 456 };
        expectedResult = service.addOutletStatusToCollectionIfMissing([], outletStatus, outletStatus2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(outletStatus);
        expect(expectedResult).toContain(outletStatus2);
      });

      it('should accept null and undefined values', () => {
        const outletStatus: IOutletStatus = { id: 123 };
        expectedResult = service.addOutletStatusToCollectionIfMissing([], null, outletStatus, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(outletStatus);
      });

      it('should return initial array if no OutletStatus is added', () => {
        const outletStatusCollection: IOutletStatus[] = [{ id: 123 }];
        expectedResult = service.addOutletStatusToCollectionIfMissing(outletStatusCollection, undefined, null);
        expect(expectedResult).toEqual(outletStatusCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
