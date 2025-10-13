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

import { ILoanPerformanceClassification, LoanPerformanceClassification } from '../loan-performance-classification.model';

import { LoanPerformanceClassificationService } from './loan-performance-classification.service';

describe('LoanPerformanceClassification Service', () => {
  let service: LoanPerformanceClassificationService;
  let httpMock: HttpTestingController;
  let elemDefault: ILoanPerformanceClassification;
  let expectedResult: ILoanPerformanceClassification | ILoanPerformanceClassification[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(LoanPerformanceClassificationService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      loanPerformanceClassificationCode: 'AAAAAAA',
      loanPerformanceClassificationType: 'AAAAAAA',
      commercialBankDescription: 'AAAAAAA',
      microfinanceDescription: 'AAAAAAA',
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

    it('should create a LoanPerformanceClassification', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new LoanPerformanceClassification()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a LoanPerformanceClassification', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          loanPerformanceClassificationCode: 'BBBBBB',
          loanPerformanceClassificationType: 'BBBBBB',
          commercialBankDescription: 'BBBBBB',
          microfinanceDescription: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a LoanPerformanceClassification', () => {
      const patchObject = Object.assign(
        {
          loanPerformanceClassificationCode: 'BBBBBB',
          loanPerformanceClassificationType: 'BBBBBB',
        },
        new LoanPerformanceClassification()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of LoanPerformanceClassification', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          loanPerformanceClassificationCode: 'BBBBBB',
          loanPerformanceClassificationType: 'BBBBBB',
          commercialBankDescription: 'BBBBBB',
          microfinanceDescription: 'BBBBBB',
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

    it('should delete a LoanPerformanceClassification', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addLoanPerformanceClassificationToCollectionIfMissing', () => {
      it('should add a LoanPerformanceClassification to an empty array', () => {
        const loanPerformanceClassification: ILoanPerformanceClassification = { id: 123 };
        expectedResult = service.addLoanPerformanceClassificationToCollectionIfMissing([], loanPerformanceClassification);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(loanPerformanceClassification);
      });

      it('should not add a LoanPerformanceClassification to an array that contains it', () => {
        const loanPerformanceClassification: ILoanPerformanceClassification = { id: 123 };
        const loanPerformanceClassificationCollection: ILoanPerformanceClassification[] = [
          {
            ...loanPerformanceClassification,
          },
          { id: 456 },
        ];
        expectedResult = service.addLoanPerformanceClassificationToCollectionIfMissing(
          loanPerformanceClassificationCollection,
          loanPerformanceClassification
        );
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a LoanPerformanceClassification to an array that doesn't contain it", () => {
        const loanPerformanceClassification: ILoanPerformanceClassification = { id: 123 };
        const loanPerformanceClassificationCollection: ILoanPerformanceClassification[] = [{ id: 456 }];
        expectedResult = service.addLoanPerformanceClassificationToCollectionIfMissing(
          loanPerformanceClassificationCollection,
          loanPerformanceClassification
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(loanPerformanceClassification);
      });

      it('should add only unique LoanPerformanceClassification to an array', () => {
        const loanPerformanceClassificationArray: ILoanPerformanceClassification[] = [{ id: 123 }, { id: 456 }, { id: 36398 }];
        const loanPerformanceClassificationCollection: ILoanPerformanceClassification[] = [{ id: 123 }];
        expectedResult = service.addLoanPerformanceClassificationToCollectionIfMissing(
          loanPerformanceClassificationCollection,
          ...loanPerformanceClassificationArray
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const loanPerformanceClassification: ILoanPerformanceClassification = { id: 123 };
        const loanPerformanceClassification2: ILoanPerformanceClassification = { id: 456 };
        expectedResult = service.addLoanPerformanceClassificationToCollectionIfMissing(
          [],
          loanPerformanceClassification,
          loanPerformanceClassification2
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(loanPerformanceClassification);
        expect(expectedResult).toContain(loanPerformanceClassification2);
      });

      it('should accept null and undefined values', () => {
        const loanPerformanceClassification: ILoanPerformanceClassification = { id: 123 };
        expectedResult = service.addLoanPerformanceClassificationToCollectionIfMissing([], null, loanPerformanceClassification, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(loanPerformanceClassification);
      });

      it('should return initial array if no LoanPerformanceClassification is added', () => {
        const loanPerformanceClassificationCollection: ILoanPerformanceClassification[] = [{ id: 123 }];
        expectedResult = service.addLoanPerformanceClassificationToCollectionIfMissing(
          loanPerformanceClassificationCollection,
          undefined,
          null
        );
        expect(expectedResult).toEqual(loanPerformanceClassificationCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
