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

import { ITALeaseRecognitionRule, TALeaseRecognitionRule } from '../ta-lease-recognition-rule.model';

import { TALeaseRecognitionRuleService } from './ta-lease-recognition-rule.service';

describe('TALeaseRecognitionRule Service', () => {
  let service: TALeaseRecognitionRuleService;
  let httpMock: HttpTestingController;
  let elemDefault: ITALeaseRecognitionRule;
  let expectedResult: ITALeaseRecognitionRule | ITALeaseRecognitionRule[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(TALeaseRecognitionRuleService);
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

    it('should create a TALeaseRecognitionRule', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new TALeaseRecognitionRule()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a TALeaseRecognitionRule', () => {
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

    it('should partial update a TALeaseRecognitionRule', () => {
      const patchObject = Object.assign({}, new TALeaseRecognitionRule());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of TALeaseRecognitionRule', () => {
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

    it('should delete a TALeaseRecognitionRule', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addTALeaseRecognitionRuleToCollectionIfMissing', () => {
      it('should add a TALeaseRecognitionRule to an empty array', () => {
        const tALeaseRecognitionRule: ITALeaseRecognitionRule = { id: 123 };
        expectedResult = service.addTALeaseRecognitionRuleToCollectionIfMissing([], tALeaseRecognitionRule);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(tALeaseRecognitionRule);
      });

      it('should not add a TALeaseRecognitionRule to an array that contains it', () => {
        const tALeaseRecognitionRule: ITALeaseRecognitionRule = { id: 123 };
        const tALeaseRecognitionRuleCollection: ITALeaseRecognitionRule[] = [
          {
            ...tALeaseRecognitionRule,
          },
          { id: 456 },
        ];
        expectedResult = service.addTALeaseRecognitionRuleToCollectionIfMissing(tALeaseRecognitionRuleCollection, tALeaseRecognitionRule);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a TALeaseRecognitionRule to an array that doesn't contain it", () => {
        const tALeaseRecognitionRule: ITALeaseRecognitionRule = { id: 123 };
        const tALeaseRecognitionRuleCollection: ITALeaseRecognitionRule[] = [{ id: 456 }];
        expectedResult = service.addTALeaseRecognitionRuleToCollectionIfMissing(tALeaseRecognitionRuleCollection, tALeaseRecognitionRule);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(tALeaseRecognitionRule);
      });

      it('should add only unique TALeaseRecognitionRule to an array', () => {
        const tALeaseRecognitionRuleArray: ITALeaseRecognitionRule[] = [{ id: 123 }, { id: 456 }, { id: 97413 }];
        const tALeaseRecognitionRuleCollection: ITALeaseRecognitionRule[] = [{ id: 123 }];
        expectedResult = service.addTALeaseRecognitionRuleToCollectionIfMissing(
          tALeaseRecognitionRuleCollection,
          ...tALeaseRecognitionRuleArray
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const tALeaseRecognitionRule: ITALeaseRecognitionRule = { id: 123 };
        const tALeaseRecognitionRule2: ITALeaseRecognitionRule = { id: 456 };
        expectedResult = service.addTALeaseRecognitionRuleToCollectionIfMissing([], tALeaseRecognitionRule, tALeaseRecognitionRule2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(tALeaseRecognitionRule);
        expect(expectedResult).toContain(tALeaseRecognitionRule2);
      });

      it('should accept null and undefined values', () => {
        const tALeaseRecognitionRule: ITALeaseRecognitionRule = { id: 123 };
        expectedResult = service.addTALeaseRecognitionRuleToCollectionIfMissing([], null, tALeaseRecognitionRule, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(tALeaseRecognitionRule);
      });

      it('should return initial array if no TALeaseRecognitionRule is added', () => {
        const tALeaseRecognitionRuleCollection: ITALeaseRecognitionRule[] = [{ id: 123 }];
        expectedResult = service.addTALeaseRecognitionRuleToCollectionIfMissing(tALeaseRecognitionRuleCollection, undefined, null);
        expect(expectedResult).toEqual(tALeaseRecognitionRuleCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
