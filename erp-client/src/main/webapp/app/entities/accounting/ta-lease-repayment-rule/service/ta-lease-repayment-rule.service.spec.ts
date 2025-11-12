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

import { ITALeaseRepaymentRule, TALeaseRepaymentRule } from '../ta-lease-repayment-rule.model';

import { TALeaseRepaymentRuleService } from './ta-lease-repayment-rule.service';

describe('TALeaseRepaymentRule Service', () => {
  let service: TALeaseRepaymentRuleService;
  let httpMock: HttpTestingController;
  let elemDefault: ITALeaseRepaymentRule;
  let expectedResult: ITALeaseRepaymentRule | ITALeaseRepaymentRule[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(TALeaseRepaymentRuleService);
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

    it('should create a TALeaseRepaymentRule', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new TALeaseRepaymentRule()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a TALeaseRepaymentRule', () => {
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

    it('should partial update a TALeaseRepaymentRule', () => {
      const patchObject = Object.assign(
        {
          identifier: 'BBBBBB',
        },
        new TALeaseRepaymentRule()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of TALeaseRepaymentRule', () => {
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

    it('should delete a TALeaseRepaymentRule', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addTALeaseRepaymentRuleToCollectionIfMissing', () => {
      it('should add a TALeaseRepaymentRule to an empty array', () => {
        const tALeaseRepaymentRule: ITALeaseRepaymentRule = { id: 123 };
        expectedResult = service.addTALeaseRepaymentRuleToCollectionIfMissing([], tALeaseRepaymentRule);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(tALeaseRepaymentRule);
      });

      it('should not add a TALeaseRepaymentRule to an array that contains it', () => {
        const tALeaseRepaymentRule: ITALeaseRepaymentRule = { id: 123 };
        const tALeaseRepaymentRuleCollection: ITALeaseRepaymentRule[] = [
          {
            ...tALeaseRepaymentRule,
          },
          { id: 456 },
        ];
        expectedResult = service.addTALeaseRepaymentRuleToCollectionIfMissing(tALeaseRepaymentRuleCollection, tALeaseRepaymentRule);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a TALeaseRepaymentRule to an array that doesn't contain it", () => {
        const tALeaseRepaymentRule: ITALeaseRepaymentRule = { id: 123 };
        const tALeaseRepaymentRuleCollection: ITALeaseRepaymentRule[] = [{ id: 456 }];
        expectedResult = service.addTALeaseRepaymentRuleToCollectionIfMissing(tALeaseRepaymentRuleCollection, tALeaseRepaymentRule);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(tALeaseRepaymentRule);
      });

      it('should add only unique TALeaseRepaymentRule to an array', () => {
        const tALeaseRepaymentRuleArray: ITALeaseRepaymentRule[] = [{ id: 123 }, { id: 456 }, { id: 71718 }];
        const tALeaseRepaymentRuleCollection: ITALeaseRepaymentRule[] = [{ id: 123 }];
        expectedResult = service.addTALeaseRepaymentRuleToCollectionIfMissing(tALeaseRepaymentRuleCollection, ...tALeaseRepaymentRuleArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const tALeaseRepaymentRule: ITALeaseRepaymentRule = { id: 123 };
        const tALeaseRepaymentRule2: ITALeaseRepaymentRule = { id: 456 };
        expectedResult = service.addTALeaseRepaymentRuleToCollectionIfMissing([], tALeaseRepaymentRule, tALeaseRepaymentRule2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(tALeaseRepaymentRule);
        expect(expectedResult).toContain(tALeaseRepaymentRule2);
      });

      it('should accept null and undefined values', () => {
        const tALeaseRepaymentRule: ITALeaseRepaymentRule = { id: 123 };
        expectedResult = service.addTALeaseRepaymentRuleToCollectionIfMissing([], null, tALeaseRepaymentRule, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(tALeaseRepaymentRule);
      });

      it('should return initial array if no TALeaseRepaymentRule is added', () => {
        const tALeaseRepaymentRuleCollection: ITALeaseRepaymentRule[] = [{ id: 123 }];
        expectedResult = service.addTALeaseRepaymentRuleToCollectionIfMissing(tALeaseRepaymentRuleCollection, undefined, null);
        expect(expectedResult).toEqual(tALeaseRepaymentRuleCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
