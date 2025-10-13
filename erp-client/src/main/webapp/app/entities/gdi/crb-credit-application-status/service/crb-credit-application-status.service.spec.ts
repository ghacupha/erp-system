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

import { ICrbCreditApplicationStatus, CrbCreditApplicationStatus } from '../crb-credit-application-status.model';

import { CrbCreditApplicationStatusService } from './crb-credit-application-status.service';

describe('CrbCreditApplicationStatus Service', () => {
  let service: CrbCreditApplicationStatusService;
  let httpMock: HttpTestingController;
  let elemDefault: ICrbCreditApplicationStatus;
  let expectedResult: ICrbCreditApplicationStatus | ICrbCreditApplicationStatus[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CrbCreditApplicationStatusService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      crbCreditApplicationStatusTypeCode: 'AAAAAAA',
      crbCreditApplicationStatusType: 'AAAAAAA',
      crbCreditApplicationStatusDetails: 'AAAAAAA',
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

    it('should create a CrbCreditApplicationStatus', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new CrbCreditApplicationStatus()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CrbCreditApplicationStatus', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          crbCreditApplicationStatusTypeCode: 'BBBBBB',
          crbCreditApplicationStatusType: 'BBBBBB',
          crbCreditApplicationStatusDetails: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a CrbCreditApplicationStatus', () => {
      const patchObject = Object.assign(
        {
          crbCreditApplicationStatusTypeCode: 'BBBBBB',
          crbCreditApplicationStatusType: 'BBBBBB',
        },
        new CrbCreditApplicationStatus()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of CrbCreditApplicationStatus', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          crbCreditApplicationStatusTypeCode: 'BBBBBB',
          crbCreditApplicationStatusType: 'BBBBBB',
          crbCreditApplicationStatusDetails: 'BBBBBB',
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

    it('should delete a CrbCreditApplicationStatus', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addCrbCreditApplicationStatusToCollectionIfMissing', () => {
      it('should add a CrbCreditApplicationStatus to an empty array', () => {
        const crbCreditApplicationStatus: ICrbCreditApplicationStatus = { id: 123 };
        expectedResult = service.addCrbCreditApplicationStatusToCollectionIfMissing([], crbCreditApplicationStatus);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(crbCreditApplicationStatus);
      });

      it('should not add a CrbCreditApplicationStatus to an array that contains it', () => {
        const crbCreditApplicationStatus: ICrbCreditApplicationStatus = { id: 123 };
        const crbCreditApplicationStatusCollection: ICrbCreditApplicationStatus[] = [
          {
            ...crbCreditApplicationStatus,
          },
          { id: 456 },
        ];
        expectedResult = service.addCrbCreditApplicationStatusToCollectionIfMissing(
          crbCreditApplicationStatusCollection,
          crbCreditApplicationStatus
        );
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CrbCreditApplicationStatus to an array that doesn't contain it", () => {
        const crbCreditApplicationStatus: ICrbCreditApplicationStatus = { id: 123 };
        const crbCreditApplicationStatusCollection: ICrbCreditApplicationStatus[] = [{ id: 456 }];
        expectedResult = service.addCrbCreditApplicationStatusToCollectionIfMissing(
          crbCreditApplicationStatusCollection,
          crbCreditApplicationStatus
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(crbCreditApplicationStatus);
      });

      it('should add only unique CrbCreditApplicationStatus to an array', () => {
        const crbCreditApplicationStatusArray: ICrbCreditApplicationStatus[] = [{ id: 123 }, { id: 456 }, { id: 18362 }];
        const crbCreditApplicationStatusCollection: ICrbCreditApplicationStatus[] = [{ id: 123 }];
        expectedResult = service.addCrbCreditApplicationStatusToCollectionIfMissing(
          crbCreditApplicationStatusCollection,
          ...crbCreditApplicationStatusArray
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const crbCreditApplicationStatus: ICrbCreditApplicationStatus = { id: 123 };
        const crbCreditApplicationStatus2: ICrbCreditApplicationStatus = { id: 456 };
        expectedResult = service.addCrbCreditApplicationStatusToCollectionIfMissing(
          [],
          crbCreditApplicationStatus,
          crbCreditApplicationStatus2
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(crbCreditApplicationStatus);
        expect(expectedResult).toContain(crbCreditApplicationStatus2);
      });

      it('should accept null and undefined values', () => {
        const crbCreditApplicationStatus: ICrbCreditApplicationStatus = { id: 123 };
        expectedResult = service.addCrbCreditApplicationStatusToCollectionIfMissing([], null, crbCreditApplicationStatus, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(crbCreditApplicationStatus);
      });

      it('should return initial array if no CrbCreditApplicationStatus is added', () => {
        const crbCreditApplicationStatusCollection: ICrbCreditApplicationStatus[] = [{ id: 123 }];
        expectedResult = service.addCrbCreditApplicationStatusToCollectionIfMissing(crbCreditApplicationStatusCollection, undefined, null);
        expect(expectedResult).toEqual(crbCreditApplicationStatusCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
