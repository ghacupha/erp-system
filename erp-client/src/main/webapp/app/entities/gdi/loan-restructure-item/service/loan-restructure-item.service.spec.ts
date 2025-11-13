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

import { ILoanRestructureItem, LoanRestructureItem } from '../loan-restructure-item.model';

import { LoanRestructureItemService } from './loan-restructure-item.service';

describe('LoanRestructureItem Service', () => {
  let service: LoanRestructureItemService;
  let httpMock: HttpTestingController;
  let elemDefault: ILoanRestructureItem;
  let expectedResult: ILoanRestructureItem | ILoanRestructureItem[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(LoanRestructureItemService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      loanRestructureItemCode: 'AAAAAAA',
      loanRestructureItemType: 'AAAAAAA',
      loanRestructureItemDetails: 'AAAAAAA',
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

    it('should create a LoanRestructureItem', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new LoanRestructureItem()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a LoanRestructureItem', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          loanRestructureItemCode: 'BBBBBB',
          loanRestructureItemType: 'BBBBBB',
          loanRestructureItemDetails: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a LoanRestructureItem', () => {
      const patchObject = Object.assign(
        {
          loanRestructureItemType: 'BBBBBB',
          loanRestructureItemDetails: 'BBBBBB',
        },
        new LoanRestructureItem()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of LoanRestructureItem', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          loanRestructureItemCode: 'BBBBBB',
          loanRestructureItemType: 'BBBBBB',
          loanRestructureItemDetails: 'BBBBBB',
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

    it('should delete a LoanRestructureItem', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addLoanRestructureItemToCollectionIfMissing', () => {
      it('should add a LoanRestructureItem to an empty array', () => {
        const loanRestructureItem: ILoanRestructureItem = { id: 123 };
        expectedResult = service.addLoanRestructureItemToCollectionIfMissing([], loanRestructureItem);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(loanRestructureItem);
      });

      it('should not add a LoanRestructureItem to an array that contains it', () => {
        const loanRestructureItem: ILoanRestructureItem = { id: 123 };
        const loanRestructureItemCollection: ILoanRestructureItem[] = [
          {
            ...loanRestructureItem,
          },
          { id: 456 },
        ];
        expectedResult = service.addLoanRestructureItemToCollectionIfMissing(loanRestructureItemCollection, loanRestructureItem);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a LoanRestructureItem to an array that doesn't contain it", () => {
        const loanRestructureItem: ILoanRestructureItem = { id: 123 };
        const loanRestructureItemCollection: ILoanRestructureItem[] = [{ id: 456 }];
        expectedResult = service.addLoanRestructureItemToCollectionIfMissing(loanRestructureItemCollection, loanRestructureItem);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(loanRestructureItem);
      });

      it('should add only unique LoanRestructureItem to an array', () => {
        const loanRestructureItemArray: ILoanRestructureItem[] = [{ id: 123 }, { id: 456 }, { id: 74540 }];
        const loanRestructureItemCollection: ILoanRestructureItem[] = [{ id: 123 }];
        expectedResult = service.addLoanRestructureItemToCollectionIfMissing(loanRestructureItemCollection, ...loanRestructureItemArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const loanRestructureItem: ILoanRestructureItem = { id: 123 };
        const loanRestructureItem2: ILoanRestructureItem = { id: 456 };
        expectedResult = service.addLoanRestructureItemToCollectionIfMissing([], loanRestructureItem, loanRestructureItem2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(loanRestructureItem);
        expect(expectedResult).toContain(loanRestructureItem2);
      });

      it('should accept null and undefined values', () => {
        const loanRestructureItem: ILoanRestructureItem = { id: 123 };
        expectedResult = service.addLoanRestructureItemToCollectionIfMissing([], null, loanRestructureItem, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(loanRestructureItem);
      });

      it('should return initial array if no LoanRestructureItem is added', () => {
        const loanRestructureItemCollection: ILoanRestructureItem[] = [{ id: 123 }];
        expectedResult = service.addLoanRestructureItemToCollectionIfMissing(loanRestructureItemCollection, undefined, null);
        expect(expectedResult).toEqual(loanRestructureItemCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
