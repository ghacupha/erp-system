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

import { ITAInterestPaidTransferRule, TAInterestPaidTransferRule } from '../ta-interest-paid-transfer-rule.model';

import { TAInterestPaidTransferRuleService } from './ta-interest-paid-transfer-rule.service';

describe('TAInterestPaidTransferRule Service', () => {
  let service: TAInterestPaidTransferRuleService;
  let httpMock: HttpTestingController;
  let elemDefault: ITAInterestPaidTransferRule;
  let expectedResult: ITAInterestPaidTransferRule | ITAInterestPaidTransferRule[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(TAInterestPaidTransferRuleService);
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

    it('should create a TAInterestPaidTransferRule', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new TAInterestPaidTransferRule()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a TAInterestPaidTransferRule', () => {
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

    it('should partial update a TAInterestPaidTransferRule', () => {
      const patchObject = Object.assign(
        {
          name: 'BBBBBB',
        },
        new TAInterestPaidTransferRule()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of TAInterestPaidTransferRule', () => {
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

    it('should delete a TAInterestPaidTransferRule', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addTAInterestPaidTransferRuleToCollectionIfMissing', () => {
      it('should add a TAInterestPaidTransferRule to an empty array', () => {
        const tAInterestPaidTransferRule: ITAInterestPaidTransferRule = { id: 123 };
        expectedResult = service.addTAInterestPaidTransferRuleToCollectionIfMissing([], tAInterestPaidTransferRule);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(tAInterestPaidTransferRule);
      });

      it('should not add a TAInterestPaidTransferRule to an array that contains it', () => {
        const tAInterestPaidTransferRule: ITAInterestPaidTransferRule = { id: 123 };
        const tAInterestPaidTransferRuleCollection: ITAInterestPaidTransferRule[] = [
          {
            ...tAInterestPaidTransferRule,
          },
          { id: 456 },
        ];
        expectedResult = service.addTAInterestPaidTransferRuleToCollectionIfMissing(
          tAInterestPaidTransferRuleCollection,
          tAInterestPaidTransferRule
        );
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a TAInterestPaidTransferRule to an array that doesn't contain it", () => {
        const tAInterestPaidTransferRule: ITAInterestPaidTransferRule = { id: 123 };
        const tAInterestPaidTransferRuleCollection: ITAInterestPaidTransferRule[] = [{ id: 456 }];
        expectedResult = service.addTAInterestPaidTransferRuleToCollectionIfMissing(
          tAInterestPaidTransferRuleCollection,
          tAInterestPaidTransferRule
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(tAInterestPaidTransferRule);
      });

      it('should add only unique TAInterestPaidTransferRule to an array', () => {
        const tAInterestPaidTransferRuleArray: ITAInterestPaidTransferRule[] = [{ id: 123 }, { id: 456 }, { id: 43586 }];
        const tAInterestPaidTransferRuleCollection: ITAInterestPaidTransferRule[] = [{ id: 123 }];
        expectedResult = service.addTAInterestPaidTransferRuleToCollectionIfMissing(
          tAInterestPaidTransferRuleCollection,
          ...tAInterestPaidTransferRuleArray
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const tAInterestPaidTransferRule: ITAInterestPaidTransferRule = { id: 123 };
        const tAInterestPaidTransferRule2: ITAInterestPaidTransferRule = { id: 456 };
        expectedResult = service.addTAInterestPaidTransferRuleToCollectionIfMissing(
          [],
          tAInterestPaidTransferRule,
          tAInterestPaidTransferRule2
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(tAInterestPaidTransferRule);
        expect(expectedResult).toContain(tAInterestPaidTransferRule2);
      });

      it('should accept null and undefined values', () => {
        const tAInterestPaidTransferRule: ITAInterestPaidTransferRule = { id: 123 };
        expectedResult = service.addTAInterestPaidTransferRuleToCollectionIfMissing([], null, tAInterestPaidTransferRule, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(tAInterestPaidTransferRule);
      });

      it('should return initial array if no TAInterestPaidTransferRule is added', () => {
        const tAInterestPaidTransferRuleCollection: ITAInterestPaidTransferRule[] = [{ id: 123 }];
        expectedResult = service.addTAInterestPaidTransferRuleToCollectionIfMissing(tAInterestPaidTransferRuleCollection, undefined, null);
        expect(expectedResult).toEqual(tAInterestPaidTransferRuleCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
