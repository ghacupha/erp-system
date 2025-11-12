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

import { IInterestCalcMethod, InterestCalcMethod } from '../interest-calc-method.model';

import { InterestCalcMethodService } from './interest-calc-method.service';

describe('InterestCalcMethod Service', () => {
  let service: InterestCalcMethodService;
  let httpMock: HttpTestingController;
  let elemDefault: IInterestCalcMethod;
  let expectedResult: IInterestCalcMethod | IInterestCalcMethod[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(InterestCalcMethodService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      interestCalculationMethodCode: 'AAAAAAA',
      interestCalculationMthodType: 'AAAAAAA',
      interestCalculationMethodDetails: 'AAAAAAA',
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

    it('should create a InterestCalcMethod', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new InterestCalcMethod()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a InterestCalcMethod', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          interestCalculationMethodCode: 'BBBBBB',
          interestCalculationMthodType: 'BBBBBB',
          interestCalculationMethodDetails: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a InterestCalcMethod', () => {
      const patchObject = Object.assign(
        {
          interestCalculationMthodType: 'BBBBBB',
        },
        new InterestCalcMethod()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of InterestCalcMethod', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          interestCalculationMethodCode: 'BBBBBB',
          interestCalculationMthodType: 'BBBBBB',
          interestCalculationMethodDetails: 'BBBBBB',
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

    it('should delete a InterestCalcMethod', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addInterestCalcMethodToCollectionIfMissing', () => {
      it('should add a InterestCalcMethod to an empty array', () => {
        const interestCalcMethod: IInterestCalcMethod = { id: 123 };
        expectedResult = service.addInterestCalcMethodToCollectionIfMissing([], interestCalcMethod);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(interestCalcMethod);
      });

      it('should not add a InterestCalcMethod to an array that contains it', () => {
        const interestCalcMethod: IInterestCalcMethod = { id: 123 };
        const interestCalcMethodCollection: IInterestCalcMethod[] = [
          {
            ...interestCalcMethod,
          },
          { id: 456 },
        ];
        expectedResult = service.addInterestCalcMethodToCollectionIfMissing(interestCalcMethodCollection, interestCalcMethod);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a InterestCalcMethod to an array that doesn't contain it", () => {
        const interestCalcMethod: IInterestCalcMethod = { id: 123 };
        const interestCalcMethodCollection: IInterestCalcMethod[] = [{ id: 456 }];
        expectedResult = service.addInterestCalcMethodToCollectionIfMissing(interestCalcMethodCollection, interestCalcMethod);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(interestCalcMethod);
      });

      it('should add only unique InterestCalcMethod to an array', () => {
        const interestCalcMethodArray: IInterestCalcMethod[] = [{ id: 123 }, { id: 456 }, { id: 28072 }];
        const interestCalcMethodCollection: IInterestCalcMethod[] = [{ id: 123 }];
        expectedResult = service.addInterestCalcMethodToCollectionIfMissing(interestCalcMethodCollection, ...interestCalcMethodArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const interestCalcMethod: IInterestCalcMethod = { id: 123 };
        const interestCalcMethod2: IInterestCalcMethod = { id: 456 };
        expectedResult = service.addInterestCalcMethodToCollectionIfMissing([], interestCalcMethod, interestCalcMethod2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(interestCalcMethod);
        expect(expectedResult).toContain(interestCalcMethod2);
      });

      it('should accept null and undefined values', () => {
        const interestCalcMethod: IInterestCalcMethod = { id: 123 };
        expectedResult = service.addInterestCalcMethodToCollectionIfMissing([], null, interestCalcMethod, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(interestCalcMethod);
      });

      it('should return initial array if no InterestCalcMethod is added', () => {
        const interestCalcMethodCollection: IInterestCalcMethod[] = [{ id: 123 }];
        expectedResult = service.addInterestCalcMethodToCollectionIfMissing(interestCalcMethodCollection, undefined, null);
        expect(expectedResult).toEqual(interestCalcMethodCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
