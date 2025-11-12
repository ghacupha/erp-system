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

import { CreditCardOwnershipTypes } from 'app/entities/enumerations/credit-card-ownership-types.model';
import { ICreditCardOwnership, CreditCardOwnership } from '../credit-card-ownership.model';

import { CreditCardOwnershipService } from './credit-card-ownership.service';

describe('CreditCardOwnership Service', () => {
  let service: CreditCardOwnershipService;
  let httpMock: HttpTestingController;
  let elemDefault: ICreditCardOwnership;
  let expectedResult: ICreditCardOwnership | ICreditCardOwnership[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CreditCardOwnershipService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      creditCardOwnershipCategoryCode: 'AAAAAAA',
      creditCardOwnershipCategoryType: CreditCardOwnershipTypes.INDIVIDUAL,
      description: 'AAAAAAA',
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

    it('should create a CreditCardOwnership', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new CreditCardOwnership()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CreditCardOwnership', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          creditCardOwnershipCategoryCode: 'BBBBBB',
          creditCardOwnershipCategoryType: 'BBBBBB',
          description: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a CreditCardOwnership', () => {
      const patchObject = Object.assign(
        {
          creditCardOwnershipCategoryCode: 'BBBBBB',
          description: 'BBBBBB',
        },
        new CreditCardOwnership()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of CreditCardOwnership', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          creditCardOwnershipCategoryCode: 'BBBBBB',
          creditCardOwnershipCategoryType: 'BBBBBB',
          description: 'BBBBBB',
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

    it('should delete a CreditCardOwnership', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addCreditCardOwnershipToCollectionIfMissing', () => {
      it('should add a CreditCardOwnership to an empty array', () => {
        const creditCardOwnership: ICreditCardOwnership = { id: 123 };
        expectedResult = service.addCreditCardOwnershipToCollectionIfMissing([], creditCardOwnership);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(creditCardOwnership);
      });

      it('should not add a CreditCardOwnership to an array that contains it', () => {
        const creditCardOwnership: ICreditCardOwnership = { id: 123 };
        const creditCardOwnershipCollection: ICreditCardOwnership[] = [
          {
            ...creditCardOwnership,
          },
          { id: 456 },
        ];
        expectedResult = service.addCreditCardOwnershipToCollectionIfMissing(creditCardOwnershipCollection, creditCardOwnership);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CreditCardOwnership to an array that doesn't contain it", () => {
        const creditCardOwnership: ICreditCardOwnership = { id: 123 };
        const creditCardOwnershipCollection: ICreditCardOwnership[] = [{ id: 456 }];
        expectedResult = service.addCreditCardOwnershipToCollectionIfMissing(creditCardOwnershipCollection, creditCardOwnership);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(creditCardOwnership);
      });

      it('should add only unique CreditCardOwnership to an array', () => {
        const creditCardOwnershipArray: ICreditCardOwnership[] = [{ id: 123 }, { id: 456 }, { id: 11020 }];
        const creditCardOwnershipCollection: ICreditCardOwnership[] = [{ id: 123 }];
        expectedResult = service.addCreditCardOwnershipToCollectionIfMissing(creditCardOwnershipCollection, ...creditCardOwnershipArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const creditCardOwnership: ICreditCardOwnership = { id: 123 };
        const creditCardOwnership2: ICreditCardOwnership = { id: 456 };
        expectedResult = service.addCreditCardOwnershipToCollectionIfMissing([], creditCardOwnership, creditCardOwnership2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(creditCardOwnership);
        expect(expectedResult).toContain(creditCardOwnership2);
      });

      it('should accept null and undefined values', () => {
        const creditCardOwnership: ICreditCardOwnership = { id: 123 };
        expectedResult = service.addCreditCardOwnershipToCollectionIfMissing([], null, creditCardOwnership, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(creditCardOwnership);
      });

      it('should return initial array if no CreditCardOwnership is added', () => {
        const creditCardOwnershipCollection: ICreditCardOwnership[] = [{ id: 123 }];
        expectedResult = service.addCreditCardOwnershipToCollectionIfMissing(creditCardOwnershipCollection, undefined, null);
        expect(expectedResult).toEqual(creditCardOwnershipCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
