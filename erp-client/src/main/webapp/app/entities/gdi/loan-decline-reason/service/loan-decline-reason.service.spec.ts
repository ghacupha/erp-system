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

import { ILoanDeclineReason, LoanDeclineReason } from '../loan-decline-reason.model';

import { LoanDeclineReasonService } from './loan-decline-reason.service';

describe('LoanDeclineReason Service', () => {
  let service: LoanDeclineReasonService;
  let httpMock: HttpTestingController;
  let elemDefault: ILoanDeclineReason;
  let expectedResult: ILoanDeclineReason | ILoanDeclineReason[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(LoanDeclineReasonService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      loanDeclineReasonTypeCode: 'AAAAAAA',
      loanDeclineReasonType: 'AAAAAAA',
      loanDeclineReasonDetails: 'AAAAAAA',
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

    it('should create a LoanDeclineReason', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new LoanDeclineReason()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a LoanDeclineReason', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          loanDeclineReasonTypeCode: 'BBBBBB',
          loanDeclineReasonType: 'BBBBBB',
          loanDeclineReasonDetails: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a LoanDeclineReason', () => {
      const patchObject = Object.assign(
        {
          loanDeclineReasonType: 'BBBBBB',
          loanDeclineReasonDetails: 'BBBBBB',
        },
        new LoanDeclineReason()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of LoanDeclineReason', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          loanDeclineReasonTypeCode: 'BBBBBB',
          loanDeclineReasonType: 'BBBBBB',
          loanDeclineReasonDetails: 'BBBBBB',
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

    it('should delete a LoanDeclineReason', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addLoanDeclineReasonToCollectionIfMissing', () => {
      it('should add a LoanDeclineReason to an empty array', () => {
        const loanDeclineReason: ILoanDeclineReason = { id: 123 };
        expectedResult = service.addLoanDeclineReasonToCollectionIfMissing([], loanDeclineReason);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(loanDeclineReason);
      });

      it('should not add a LoanDeclineReason to an array that contains it', () => {
        const loanDeclineReason: ILoanDeclineReason = { id: 123 };
        const loanDeclineReasonCollection: ILoanDeclineReason[] = [
          {
            ...loanDeclineReason,
          },
          { id: 456 },
        ];
        expectedResult = service.addLoanDeclineReasonToCollectionIfMissing(loanDeclineReasonCollection, loanDeclineReason);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a LoanDeclineReason to an array that doesn't contain it", () => {
        const loanDeclineReason: ILoanDeclineReason = { id: 123 };
        const loanDeclineReasonCollection: ILoanDeclineReason[] = [{ id: 456 }];
        expectedResult = service.addLoanDeclineReasonToCollectionIfMissing(loanDeclineReasonCollection, loanDeclineReason);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(loanDeclineReason);
      });

      it('should add only unique LoanDeclineReason to an array', () => {
        const loanDeclineReasonArray: ILoanDeclineReason[] = [{ id: 123 }, { id: 456 }, { id: 9101 }];
        const loanDeclineReasonCollection: ILoanDeclineReason[] = [{ id: 123 }];
        expectedResult = service.addLoanDeclineReasonToCollectionIfMissing(loanDeclineReasonCollection, ...loanDeclineReasonArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const loanDeclineReason: ILoanDeclineReason = { id: 123 };
        const loanDeclineReason2: ILoanDeclineReason = { id: 456 };
        expectedResult = service.addLoanDeclineReasonToCollectionIfMissing([], loanDeclineReason, loanDeclineReason2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(loanDeclineReason);
        expect(expectedResult).toContain(loanDeclineReason2);
      });

      it('should accept null and undefined values', () => {
        const loanDeclineReason: ILoanDeclineReason = { id: 123 };
        expectedResult = service.addLoanDeclineReasonToCollectionIfMissing([], null, loanDeclineReason, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(loanDeclineReason);
      });

      it('should return initial array if no LoanDeclineReason is added', () => {
        const loanDeclineReasonCollection: ILoanDeclineReason[] = [{ id: 123 }];
        expectedResult = service.addLoanDeclineReasonToCollectionIfMissing(loanDeclineReasonCollection, undefined, null);
        expect(expectedResult).toEqual(loanDeclineReasonCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
