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

import { ITAAmortizationRule, TAAmortizationRule } from '../ta-amortization-rule.model';

import { TAAmortizationRuleService } from './ta-amortization-rule.service';

describe('TAAmortizationRule Service', () => {
  let service: TAAmortizationRuleService;
  let httpMock: HttpTestingController;
  let elemDefault: ITAAmortizationRule;
  let expectedResult: ITAAmortizationRule | ITAAmortizationRule[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(TAAmortizationRuleService);
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

    it('should create a TAAmortizationRule', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new TAAmortizationRule()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a TAAmortizationRule', () => {
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

    it('should partial update a TAAmortizationRule', () => {
      const patchObject = Object.assign(
        {
          name: 'BBBBBB',
          identifier: 'BBBBBB',
        },
        new TAAmortizationRule()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of TAAmortizationRule', () => {
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

    it('should delete a TAAmortizationRule', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addTAAmortizationRuleToCollectionIfMissing', () => {
      it('should add a TAAmortizationRule to an empty array', () => {
        const tAAmortizationRule: ITAAmortizationRule = { id: 123 };
        expectedResult = service.addTAAmortizationRuleToCollectionIfMissing([], tAAmortizationRule);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(tAAmortizationRule);
      });

      it('should not add a TAAmortizationRule to an array that contains it', () => {
        const tAAmortizationRule: ITAAmortizationRule = { id: 123 };
        const tAAmortizationRuleCollection: ITAAmortizationRule[] = [
          {
            ...tAAmortizationRule,
          },
          { id: 456 },
        ];
        expectedResult = service.addTAAmortizationRuleToCollectionIfMissing(tAAmortizationRuleCollection, tAAmortizationRule);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a TAAmortizationRule to an array that doesn't contain it", () => {
        const tAAmortizationRule: ITAAmortizationRule = { id: 123 };
        const tAAmortizationRuleCollection: ITAAmortizationRule[] = [{ id: 456 }];
        expectedResult = service.addTAAmortizationRuleToCollectionIfMissing(tAAmortizationRuleCollection, tAAmortizationRule);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(tAAmortizationRule);
      });

      it('should add only unique TAAmortizationRule to an array', () => {
        const tAAmortizationRuleArray: ITAAmortizationRule[] = [{ id: 123 }, { id: 456 }, { id: 71993 }];
        const tAAmortizationRuleCollection: ITAAmortizationRule[] = [{ id: 123 }];
        expectedResult = service.addTAAmortizationRuleToCollectionIfMissing(tAAmortizationRuleCollection, ...tAAmortizationRuleArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const tAAmortizationRule: ITAAmortizationRule = { id: 123 };
        const tAAmortizationRule2: ITAAmortizationRule = { id: 456 };
        expectedResult = service.addTAAmortizationRuleToCollectionIfMissing([], tAAmortizationRule, tAAmortizationRule2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(tAAmortizationRule);
        expect(expectedResult).toContain(tAAmortizationRule2);
      });

      it('should accept null and undefined values', () => {
        const tAAmortizationRule: ITAAmortizationRule = { id: 123 };
        expectedResult = service.addTAAmortizationRuleToCollectionIfMissing([], null, tAAmortizationRule, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(tAAmortizationRule);
      });

      it('should return initial array if no TAAmortizationRule is added', () => {
        const tAAmortizationRuleCollection: ITAAmortizationRule[] = [{ id: 123 }];
        expectedResult = service.addTAAmortizationRuleToCollectionIfMissing(tAAmortizationRuleCollection, undefined, null);
        expect(expectedResult).toEqual(tAAmortizationRuleCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
