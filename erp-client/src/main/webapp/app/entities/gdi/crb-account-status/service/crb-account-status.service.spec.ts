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

import { ICrbAccountStatus, CrbAccountStatus } from '../crb-account-status.model';

import { CrbAccountStatusService } from './crb-account-status.service';

describe('CrbAccountStatus Service', () => {
  let service: CrbAccountStatusService;
  let httpMock: HttpTestingController;
  let elemDefault: ICrbAccountStatus;
  let expectedResult: ICrbAccountStatus | ICrbAccountStatus[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CrbAccountStatusService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      accountStatusTypeCode: 'AAAAAAA',
      accountStatusType: 'AAAAAAA',
      accountStatusTypeDetails: 'AAAAAAA',
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

    it('should create a CrbAccountStatus', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new CrbAccountStatus()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CrbAccountStatus', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          accountStatusTypeCode: 'BBBBBB',
          accountStatusType: 'BBBBBB',
          accountStatusTypeDetails: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a CrbAccountStatus', () => {
      const patchObject = Object.assign({}, new CrbAccountStatus());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of CrbAccountStatus', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          accountStatusTypeCode: 'BBBBBB',
          accountStatusType: 'BBBBBB',
          accountStatusTypeDetails: 'BBBBBB',
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

    it('should delete a CrbAccountStatus', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addCrbAccountStatusToCollectionIfMissing', () => {
      it('should add a CrbAccountStatus to an empty array', () => {
        const crbAccountStatus: ICrbAccountStatus = { id: 123 };
        expectedResult = service.addCrbAccountStatusToCollectionIfMissing([], crbAccountStatus);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(crbAccountStatus);
      });

      it('should not add a CrbAccountStatus to an array that contains it', () => {
        const crbAccountStatus: ICrbAccountStatus = { id: 123 };
        const crbAccountStatusCollection: ICrbAccountStatus[] = [
          {
            ...crbAccountStatus,
          },
          { id: 456 },
        ];
        expectedResult = service.addCrbAccountStatusToCollectionIfMissing(crbAccountStatusCollection, crbAccountStatus);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CrbAccountStatus to an array that doesn't contain it", () => {
        const crbAccountStatus: ICrbAccountStatus = { id: 123 };
        const crbAccountStatusCollection: ICrbAccountStatus[] = [{ id: 456 }];
        expectedResult = service.addCrbAccountStatusToCollectionIfMissing(crbAccountStatusCollection, crbAccountStatus);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(crbAccountStatus);
      });

      it('should add only unique CrbAccountStatus to an array', () => {
        const crbAccountStatusArray: ICrbAccountStatus[] = [{ id: 123 }, { id: 456 }, { id: 19694 }];
        const crbAccountStatusCollection: ICrbAccountStatus[] = [{ id: 123 }];
        expectedResult = service.addCrbAccountStatusToCollectionIfMissing(crbAccountStatusCollection, ...crbAccountStatusArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const crbAccountStatus: ICrbAccountStatus = { id: 123 };
        const crbAccountStatus2: ICrbAccountStatus = { id: 456 };
        expectedResult = service.addCrbAccountStatusToCollectionIfMissing([], crbAccountStatus, crbAccountStatus2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(crbAccountStatus);
        expect(expectedResult).toContain(crbAccountStatus2);
      });

      it('should accept null and undefined values', () => {
        const crbAccountStatus: ICrbAccountStatus = { id: 123 };
        expectedResult = service.addCrbAccountStatusToCollectionIfMissing([], null, crbAccountStatus, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(crbAccountStatus);
      });

      it('should return initial array if no CrbAccountStatus is added', () => {
        const crbAccountStatusCollection: ICrbAccountStatus[] = [{ id: 123 }];
        expectedResult = service.addCrbAccountStatusToCollectionIfMissing(crbAccountStatusCollection, undefined, null);
        expect(expectedResult).toEqual(crbAccountStatusCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
