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

import { ILoanRepaymentFrequency, LoanRepaymentFrequency } from '../loan-repayment-frequency.model';

import { LoanRepaymentFrequencyService } from './loan-repayment-frequency.service';

describe('LoanRepaymentFrequency Service', () => {
  let service: LoanRepaymentFrequencyService;
  let httpMock: HttpTestingController;
  let elemDefault: ILoanRepaymentFrequency;
  let expectedResult: ILoanRepaymentFrequency | ILoanRepaymentFrequency[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(LoanRepaymentFrequencyService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      frequencyTypeCode: 'AAAAAAA',
      frequencyType: 'AAAAAAA',
      frequencyTypeDetails: 'AAAAAAA',
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

    it('should create a LoanRepaymentFrequency', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new LoanRepaymentFrequency()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a LoanRepaymentFrequency', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          frequencyTypeCode: 'BBBBBB',
          frequencyType: 'BBBBBB',
          frequencyTypeDetails: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a LoanRepaymentFrequency', () => {
      const patchObject = Object.assign(
        {
          frequencyTypeCode: 'BBBBBB',
        },
        new LoanRepaymentFrequency()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of LoanRepaymentFrequency', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          frequencyTypeCode: 'BBBBBB',
          frequencyType: 'BBBBBB',
          frequencyTypeDetails: 'BBBBBB',
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

    it('should delete a LoanRepaymentFrequency', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addLoanRepaymentFrequencyToCollectionIfMissing', () => {
      it('should add a LoanRepaymentFrequency to an empty array', () => {
        const loanRepaymentFrequency: ILoanRepaymentFrequency = { id: 123 };
        expectedResult = service.addLoanRepaymentFrequencyToCollectionIfMissing([], loanRepaymentFrequency);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(loanRepaymentFrequency);
      });

      it('should not add a LoanRepaymentFrequency to an array that contains it', () => {
        const loanRepaymentFrequency: ILoanRepaymentFrequency = { id: 123 };
        const loanRepaymentFrequencyCollection: ILoanRepaymentFrequency[] = [
          {
            ...loanRepaymentFrequency,
          },
          { id: 456 },
        ];
        expectedResult = service.addLoanRepaymentFrequencyToCollectionIfMissing(loanRepaymentFrequencyCollection, loanRepaymentFrequency);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a LoanRepaymentFrequency to an array that doesn't contain it", () => {
        const loanRepaymentFrequency: ILoanRepaymentFrequency = { id: 123 };
        const loanRepaymentFrequencyCollection: ILoanRepaymentFrequency[] = [{ id: 456 }];
        expectedResult = service.addLoanRepaymentFrequencyToCollectionIfMissing(loanRepaymentFrequencyCollection, loanRepaymentFrequency);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(loanRepaymentFrequency);
      });

      it('should add only unique LoanRepaymentFrequency to an array', () => {
        const loanRepaymentFrequencyArray: ILoanRepaymentFrequency[] = [{ id: 123 }, { id: 456 }, { id: 86692 }];
        const loanRepaymentFrequencyCollection: ILoanRepaymentFrequency[] = [{ id: 123 }];
        expectedResult = service.addLoanRepaymentFrequencyToCollectionIfMissing(
          loanRepaymentFrequencyCollection,
          ...loanRepaymentFrequencyArray
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const loanRepaymentFrequency: ILoanRepaymentFrequency = { id: 123 };
        const loanRepaymentFrequency2: ILoanRepaymentFrequency = { id: 456 };
        expectedResult = service.addLoanRepaymentFrequencyToCollectionIfMissing([], loanRepaymentFrequency, loanRepaymentFrequency2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(loanRepaymentFrequency);
        expect(expectedResult).toContain(loanRepaymentFrequency2);
      });

      it('should accept null and undefined values', () => {
        const loanRepaymentFrequency: ILoanRepaymentFrequency = { id: 123 };
        expectedResult = service.addLoanRepaymentFrequencyToCollectionIfMissing([], null, loanRepaymentFrequency, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(loanRepaymentFrequency);
      });

      it('should return initial array if no LoanRepaymentFrequency is added', () => {
        const loanRepaymentFrequencyCollection: ILoanRepaymentFrequency[] = [{ id: 123 }];
        expectedResult = service.addLoanRepaymentFrequencyToCollectionIfMissing(loanRepaymentFrequencyCollection, undefined, null);
        expect(expectedResult).toEqual(loanRepaymentFrequencyCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
