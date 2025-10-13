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

import { FlagCodes } from 'app/entities/enumerations/flag-codes.model';
import { ILoanRestructureFlag, LoanRestructureFlag } from '../loan-restructure-flag.model';

import { LoanRestructureFlagService } from './loan-restructure-flag.service';

describe('LoanRestructureFlag Service', () => {
  let service: LoanRestructureFlagService;
  let httpMock: HttpTestingController;
  let elemDefault: ILoanRestructureFlag;
  let expectedResult: ILoanRestructureFlag | ILoanRestructureFlag[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(LoanRestructureFlagService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      loanRestructureFlagCode: FlagCodes.Y,
      loanRestructureFlagType: 'AAAAAAA',
      loanRestructureFlagDetails: 'AAAAAAA',
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

    it('should create a LoanRestructureFlag', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new LoanRestructureFlag()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a LoanRestructureFlag', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          loanRestructureFlagCode: 'BBBBBB',
          loanRestructureFlagType: 'BBBBBB',
          loanRestructureFlagDetails: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a LoanRestructureFlag', () => {
      const patchObject = Object.assign(
        {
          loanRestructureFlagCode: 'BBBBBB',
          loanRestructureFlagType: 'BBBBBB',
          loanRestructureFlagDetails: 'BBBBBB',
        },
        new LoanRestructureFlag()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of LoanRestructureFlag', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          loanRestructureFlagCode: 'BBBBBB',
          loanRestructureFlagType: 'BBBBBB',
          loanRestructureFlagDetails: 'BBBBBB',
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

    it('should delete a LoanRestructureFlag', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addLoanRestructureFlagToCollectionIfMissing', () => {
      it('should add a LoanRestructureFlag to an empty array', () => {
        const loanRestructureFlag: ILoanRestructureFlag = { id: 123 };
        expectedResult = service.addLoanRestructureFlagToCollectionIfMissing([], loanRestructureFlag);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(loanRestructureFlag);
      });

      it('should not add a LoanRestructureFlag to an array that contains it', () => {
        const loanRestructureFlag: ILoanRestructureFlag = { id: 123 };
        const loanRestructureFlagCollection: ILoanRestructureFlag[] = [
          {
            ...loanRestructureFlag,
          },
          { id: 456 },
        ];
        expectedResult = service.addLoanRestructureFlagToCollectionIfMissing(loanRestructureFlagCollection, loanRestructureFlag);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a LoanRestructureFlag to an array that doesn't contain it", () => {
        const loanRestructureFlag: ILoanRestructureFlag = { id: 123 };
        const loanRestructureFlagCollection: ILoanRestructureFlag[] = [{ id: 456 }];
        expectedResult = service.addLoanRestructureFlagToCollectionIfMissing(loanRestructureFlagCollection, loanRestructureFlag);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(loanRestructureFlag);
      });

      it('should add only unique LoanRestructureFlag to an array', () => {
        const loanRestructureFlagArray: ILoanRestructureFlag[] = [{ id: 123 }, { id: 456 }, { id: 79157 }];
        const loanRestructureFlagCollection: ILoanRestructureFlag[] = [{ id: 123 }];
        expectedResult = service.addLoanRestructureFlagToCollectionIfMissing(loanRestructureFlagCollection, ...loanRestructureFlagArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const loanRestructureFlag: ILoanRestructureFlag = { id: 123 };
        const loanRestructureFlag2: ILoanRestructureFlag = { id: 456 };
        expectedResult = service.addLoanRestructureFlagToCollectionIfMissing([], loanRestructureFlag, loanRestructureFlag2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(loanRestructureFlag);
        expect(expectedResult).toContain(loanRestructureFlag2);
      });

      it('should accept null and undefined values', () => {
        const loanRestructureFlag: ILoanRestructureFlag = { id: 123 };
        expectedResult = service.addLoanRestructureFlagToCollectionIfMissing([], null, loanRestructureFlag, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(loanRestructureFlag);
      });

      it('should return initial array if no LoanRestructureFlag is added', () => {
        const loanRestructureFlagCollection: ILoanRestructureFlag[] = [{ id: 123 }];
        expectedResult = service.addLoanRestructureFlagToCollectionIfMissing(loanRestructureFlagCollection, undefined, null);
        expect(expectedResult).toEqual(loanRestructureFlagCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
