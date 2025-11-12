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

import { ILoanApplicationType, LoanApplicationType } from '../loan-application-type.model';

import { LoanApplicationTypeService } from './loan-application-type.service';

describe('LoanApplicationType Service', () => {
  let service: LoanApplicationTypeService;
  let httpMock: HttpTestingController;
  let elemDefault: ILoanApplicationType;
  let expectedResult: ILoanApplicationType | ILoanApplicationType[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(LoanApplicationTypeService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      loanApplicationTypeCode: 'AAAAAAA',
      loanApplicationType: 'AAAAAAA',
      loanApplicationDetails: 'AAAAAAA',
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

    it('should create a LoanApplicationType', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new LoanApplicationType()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a LoanApplicationType', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          loanApplicationTypeCode: 'BBBBBB',
          loanApplicationType: 'BBBBBB',
          loanApplicationDetails: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a LoanApplicationType', () => {
      const patchObject = Object.assign({}, new LoanApplicationType());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of LoanApplicationType', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          loanApplicationTypeCode: 'BBBBBB',
          loanApplicationType: 'BBBBBB',
          loanApplicationDetails: 'BBBBBB',
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

    it('should delete a LoanApplicationType', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addLoanApplicationTypeToCollectionIfMissing', () => {
      it('should add a LoanApplicationType to an empty array', () => {
        const loanApplicationType: ILoanApplicationType = { id: 123 };
        expectedResult = service.addLoanApplicationTypeToCollectionIfMissing([], loanApplicationType);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(loanApplicationType);
      });

      it('should not add a LoanApplicationType to an array that contains it', () => {
        const loanApplicationType: ILoanApplicationType = { id: 123 };
        const loanApplicationTypeCollection: ILoanApplicationType[] = [
          {
            ...loanApplicationType,
          },
          { id: 456 },
        ];
        expectedResult = service.addLoanApplicationTypeToCollectionIfMissing(loanApplicationTypeCollection, loanApplicationType);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a LoanApplicationType to an array that doesn't contain it", () => {
        const loanApplicationType: ILoanApplicationType = { id: 123 };
        const loanApplicationTypeCollection: ILoanApplicationType[] = [{ id: 456 }];
        expectedResult = service.addLoanApplicationTypeToCollectionIfMissing(loanApplicationTypeCollection, loanApplicationType);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(loanApplicationType);
      });

      it('should add only unique LoanApplicationType to an array', () => {
        const loanApplicationTypeArray: ILoanApplicationType[] = [{ id: 123 }, { id: 456 }, { id: 68992 }];
        const loanApplicationTypeCollection: ILoanApplicationType[] = [{ id: 123 }];
        expectedResult = service.addLoanApplicationTypeToCollectionIfMissing(loanApplicationTypeCollection, ...loanApplicationTypeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const loanApplicationType: ILoanApplicationType = { id: 123 };
        const loanApplicationType2: ILoanApplicationType = { id: 456 };
        expectedResult = service.addLoanApplicationTypeToCollectionIfMissing([], loanApplicationType, loanApplicationType2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(loanApplicationType);
        expect(expectedResult).toContain(loanApplicationType2);
      });

      it('should accept null and undefined values', () => {
        const loanApplicationType: ILoanApplicationType = { id: 123 };
        expectedResult = service.addLoanApplicationTypeToCollectionIfMissing([], null, loanApplicationType, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(loanApplicationType);
      });

      it('should return initial array if no LoanApplicationType is added', () => {
        const loanApplicationTypeCollection: ILoanApplicationType[] = [{ id: 123 }];
        expectedResult = service.addLoanApplicationTypeToCollectionIfMissing(loanApplicationTypeCollection, undefined, null);
        expect(expectedResult).toEqual(loanApplicationTypeCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
