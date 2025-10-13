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

import { ITALeaseInterestAccrualRule, TALeaseInterestAccrualRule } from '../ta-lease-interest-accrual-rule.model';

import { TALeaseInterestAccrualRuleService } from './ta-lease-interest-accrual-rule.service';

describe('TALeaseInterestAccrualRule Service', () => {
  let service: TALeaseInterestAccrualRuleService;
  let httpMock: HttpTestingController;
  let elemDefault: ITALeaseInterestAccrualRule;
  let expectedResult: ITALeaseInterestAccrualRule | ITALeaseInterestAccrualRule[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(TALeaseInterestAccrualRuleService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      name: 'AAAAAAA',
      identifier: 'AAAAAAA',
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

    it('should create a TALeaseInterestAccrualRule', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new TALeaseInterestAccrualRule()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a TALeaseInterestAccrualRule', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
          identifier: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a TALeaseInterestAccrualRule', () => {
      const patchObject = Object.assign(
        {
          identifier: 'BBBBBB',
        },
        new TALeaseInterestAccrualRule()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of TALeaseInterestAccrualRule', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
          identifier: 'BBBBBB',
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

    it('should delete a TALeaseInterestAccrualRule', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addTALeaseInterestAccrualRuleToCollectionIfMissing', () => {
      it('should add a TALeaseInterestAccrualRule to an empty array', () => {
        const tALeaseInterestAccrualRule: ITALeaseInterestAccrualRule = { id: 123 };
        expectedResult = service.addTALeaseInterestAccrualRuleToCollectionIfMissing([], tALeaseInterestAccrualRule);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(tALeaseInterestAccrualRule);
      });

      it('should not add a TALeaseInterestAccrualRule to an array that contains it', () => {
        const tALeaseInterestAccrualRule: ITALeaseInterestAccrualRule = { id: 123 };
        const tALeaseInterestAccrualRuleCollection: ITALeaseInterestAccrualRule[] = [
          {
            ...tALeaseInterestAccrualRule,
          },
          { id: 456 },
        ];
        expectedResult = service.addTALeaseInterestAccrualRuleToCollectionIfMissing(
          tALeaseInterestAccrualRuleCollection,
          tALeaseInterestAccrualRule
        );
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a TALeaseInterestAccrualRule to an array that doesn't contain it", () => {
        const tALeaseInterestAccrualRule: ITALeaseInterestAccrualRule = { id: 123 };
        const tALeaseInterestAccrualRuleCollection: ITALeaseInterestAccrualRule[] = [{ id: 456 }];
        expectedResult = service.addTALeaseInterestAccrualRuleToCollectionIfMissing(
          tALeaseInterestAccrualRuleCollection,
          tALeaseInterestAccrualRule
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(tALeaseInterestAccrualRule);
      });

      it('should add only unique TALeaseInterestAccrualRule to an array', () => {
        const tALeaseInterestAccrualRuleArray: ITALeaseInterestAccrualRule[] = [{ id: 123 }, { id: 456 }, { id: 99798 }];
        const tALeaseInterestAccrualRuleCollection: ITALeaseInterestAccrualRule[] = [{ id: 123 }];
        expectedResult = service.addTALeaseInterestAccrualRuleToCollectionIfMissing(
          tALeaseInterestAccrualRuleCollection,
          ...tALeaseInterestAccrualRuleArray
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const tALeaseInterestAccrualRule: ITALeaseInterestAccrualRule = { id: 123 };
        const tALeaseInterestAccrualRule2: ITALeaseInterestAccrualRule = { id: 456 };
        expectedResult = service.addTALeaseInterestAccrualRuleToCollectionIfMissing(
          [],
          tALeaseInterestAccrualRule,
          tALeaseInterestAccrualRule2
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(tALeaseInterestAccrualRule);
        expect(expectedResult).toContain(tALeaseInterestAccrualRule2);
      });

      it('should accept null and undefined values', () => {
        const tALeaseInterestAccrualRule: ITALeaseInterestAccrualRule = { id: 123 };
        expectedResult = service.addTALeaseInterestAccrualRuleToCollectionIfMissing([], null, tALeaseInterestAccrualRule, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(tALeaseInterestAccrualRule);
      });

      it('should return initial array if no TALeaseInterestAccrualRule is added', () => {
        const tALeaseInterestAccrualRuleCollection: ITALeaseInterestAccrualRule[] = [{ id: 123 }];
        expectedResult = service.addTALeaseInterestAccrualRuleToCollectionIfMissing(tALeaseInterestAccrualRuleCollection, undefined, null);
        expect(expectedResult).toEqual(tALeaseInterestAccrualRuleCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
