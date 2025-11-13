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

import { ILoanApplicationStatus, LoanApplicationStatus } from '../loan-application-status.model';

import { LoanApplicationStatusService } from './loan-application-status.service';

describe('LoanApplicationStatus Service', () => {
  let service: LoanApplicationStatusService;
  let httpMock: HttpTestingController;
  let elemDefault: ILoanApplicationStatus;
  let expectedResult: ILoanApplicationStatus | ILoanApplicationStatus[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(LoanApplicationStatusService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      loanApplicationStatusTypeCode: 'AAAAAAA',
      loanApplicationStatusType: 'AAAAAAA',
      loanApplicationStatusDetails: 'AAAAAAA',
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

    it('should create a LoanApplicationStatus', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new LoanApplicationStatus()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a LoanApplicationStatus', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          loanApplicationStatusTypeCode: 'BBBBBB',
          loanApplicationStatusType: 'BBBBBB',
          loanApplicationStatusDetails: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a LoanApplicationStatus', () => {
      const patchObject = Object.assign(
        {
          loanApplicationStatusTypeCode: 'BBBBBB',
          loanApplicationStatusType: 'BBBBBB',
        },
        new LoanApplicationStatus()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of LoanApplicationStatus', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          loanApplicationStatusTypeCode: 'BBBBBB',
          loanApplicationStatusType: 'BBBBBB',
          loanApplicationStatusDetails: 'BBBBBB',
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

    it('should delete a LoanApplicationStatus', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addLoanApplicationStatusToCollectionIfMissing', () => {
      it('should add a LoanApplicationStatus to an empty array', () => {
        const loanApplicationStatus: ILoanApplicationStatus = { id: 123 };
        expectedResult = service.addLoanApplicationStatusToCollectionIfMissing([], loanApplicationStatus);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(loanApplicationStatus);
      });

      it('should not add a LoanApplicationStatus to an array that contains it', () => {
        const loanApplicationStatus: ILoanApplicationStatus = { id: 123 };
        const loanApplicationStatusCollection: ILoanApplicationStatus[] = [
          {
            ...loanApplicationStatus,
          },
          { id: 456 },
        ];
        expectedResult = service.addLoanApplicationStatusToCollectionIfMissing(loanApplicationStatusCollection, loanApplicationStatus);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a LoanApplicationStatus to an array that doesn't contain it", () => {
        const loanApplicationStatus: ILoanApplicationStatus = { id: 123 };
        const loanApplicationStatusCollection: ILoanApplicationStatus[] = [{ id: 456 }];
        expectedResult = service.addLoanApplicationStatusToCollectionIfMissing(loanApplicationStatusCollection, loanApplicationStatus);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(loanApplicationStatus);
      });

      it('should add only unique LoanApplicationStatus to an array', () => {
        const loanApplicationStatusArray: ILoanApplicationStatus[] = [{ id: 123 }, { id: 456 }, { id: 37164 }];
        const loanApplicationStatusCollection: ILoanApplicationStatus[] = [{ id: 123 }];
        expectedResult = service.addLoanApplicationStatusToCollectionIfMissing(
          loanApplicationStatusCollection,
          ...loanApplicationStatusArray
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const loanApplicationStatus: ILoanApplicationStatus = { id: 123 };
        const loanApplicationStatus2: ILoanApplicationStatus = { id: 456 };
        expectedResult = service.addLoanApplicationStatusToCollectionIfMissing([], loanApplicationStatus, loanApplicationStatus2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(loanApplicationStatus);
        expect(expectedResult).toContain(loanApplicationStatus2);
      });

      it('should accept null and undefined values', () => {
        const loanApplicationStatus: ILoanApplicationStatus = { id: 123 };
        expectedResult = service.addLoanApplicationStatusToCollectionIfMissing([], null, loanApplicationStatus, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(loanApplicationStatus);
      });

      it('should return initial array if no LoanApplicationStatus is added', () => {
        const loanApplicationStatusCollection: ILoanApplicationStatus[] = [{ id: 123 }];
        expectedResult = service.addLoanApplicationStatusToCollectionIfMissing(loanApplicationStatusCollection, undefined, null);
        expect(expectedResult).toEqual(loanApplicationStatusCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
