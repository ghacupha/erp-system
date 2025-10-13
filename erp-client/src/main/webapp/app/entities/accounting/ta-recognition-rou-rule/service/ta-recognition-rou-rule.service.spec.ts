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

import { ITARecognitionROURule, TARecognitionROURule } from '../ta-recognition-rou-rule.model';

import { TARecognitionROURuleService } from './ta-recognition-rou-rule.service';

describe('TARecognitionROURule Service', () => {
  let service: TARecognitionROURuleService;
  let httpMock: HttpTestingController;
  let elemDefault: ITARecognitionROURule;
  let expectedResult: ITARecognitionROURule | ITARecognitionROURule[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(TARecognitionROURuleService);
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

    it('should create a TARecognitionROURule', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new TARecognitionROURule()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a TARecognitionROURule', () => {
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

    it('should partial update a TARecognitionROURule', () => {
      const patchObject = Object.assign({}, new TARecognitionROURule());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of TARecognitionROURule', () => {
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

    it('should delete a TARecognitionROURule', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addTARecognitionROURuleToCollectionIfMissing', () => {
      it('should add a TARecognitionROURule to an empty array', () => {
        const tARecognitionROURule: ITARecognitionROURule = { id: 123 };
        expectedResult = service.addTARecognitionROURuleToCollectionIfMissing([], tARecognitionROURule);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(tARecognitionROURule);
      });

      it('should not add a TARecognitionROURule to an array that contains it', () => {
        const tARecognitionROURule: ITARecognitionROURule = { id: 123 };
        const tARecognitionROURuleCollection: ITARecognitionROURule[] = [
          {
            ...tARecognitionROURule,
          },
          { id: 456 },
        ];
        expectedResult = service.addTARecognitionROURuleToCollectionIfMissing(tARecognitionROURuleCollection, tARecognitionROURule);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a TARecognitionROURule to an array that doesn't contain it", () => {
        const tARecognitionROURule: ITARecognitionROURule = { id: 123 };
        const tARecognitionROURuleCollection: ITARecognitionROURule[] = [{ id: 456 }];
        expectedResult = service.addTARecognitionROURuleToCollectionIfMissing(tARecognitionROURuleCollection, tARecognitionROURule);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(tARecognitionROURule);
      });

      it('should add only unique TARecognitionROURule to an array', () => {
        const tARecognitionROURuleArray: ITARecognitionROURule[] = [{ id: 123 }, { id: 456 }, { id: 96231 }];
        const tARecognitionROURuleCollection: ITARecognitionROURule[] = [{ id: 123 }];
        expectedResult = service.addTARecognitionROURuleToCollectionIfMissing(tARecognitionROURuleCollection, ...tARecognitionROURuleArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const tARecognitionROURule: ITARecognitionROURule = { id: 123 };
        const tARecognitionROURule2: ITARecognitionROURule = { id: 456 };
        expectedResult = service.addTARecognitionROURuleToCollectionIfMissing([], tARecognitionROURule, tARecognitionROURule2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(tARecognitionROURule);
        expect(expectedResult).toContain(tARecognitionROURule2);
      });

      it('should accept null and undefined values', () => {
        const tARecognitionROURule: ITARecognitionROURule = { id: 123 };
        expectedResult = service.addTARecognitionROURuleToCollectionIfMissing([], null, tARecognitionROURule, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(tARecognitionROURule);
      });

      it('should return initial array if no TARecognitionROURule is added', () => {
        const tARecognitionROURuleCollection: ITARecognitionROURule[] = [{ id: 123 }];
        expectedResult = service.addTARecognitionROURuleToCollectionIfMissing(tARecognitionROURuleCollection, undefined, null);
        expect(expectedResult).toEqual(tARecognitionROURuleCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
