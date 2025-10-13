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

import { CurrencyServiceabilityFlagTypes } from 'app/entities/enumerations/currency-serviceability-flag-types.model';
import { CurrencyServiceability } from 'app/entities/enumerations/currency-serviceability.model';
import { ICurrencyServiceabilityFlag, CurrencyServiceabilityFlag } from '../currency-serviceability-flag.model';

import { CurrencyServiceabilityFlagService } from './currency-serviceability-flag.service';

describe('CurrencyServiceabilityFlag Service', () => {
  let service: CurrencyServiceabilityFlagService;
  let httpMock: HttpTestingController;
  let elemDefault: ICurrencyServiceabilityFlag;
  let expectedResult: ICurrencyServiceabilityFlag | ICurrencyServiceabilityFlag[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CurrencyServiceabilityFlagService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      currencyServiceabilityFlag: CurrencyServiceabilityFlagTypes.Y,
      currencyServiceability: CurrencyServiceability.FIT,
      currencyServiceabilityFlagDetails: 'AAAAAAA',
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

    it('should create a CurrencyServiceabilityFlag', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new CurrencyServiceabilityFlag()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CurrencyServiceabilityFlag', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          currencyServiceabilityFlag: 'BBBBBB',
          currencyServiceability: 'BBBBBB',
          currencyServiceabilityFlagDetails: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a CurrencyServiceabilityFlag', () => {
      const patchObject = Object.assign({}, new CurrencyServiceabilityFlag());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of CurrencyServiceabilityFlag', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          currencyServiceabilityFlag: 'BBBBBB',
          currencyServiceability: 'BBBBBB',
          currencyServiceabilityFlagDetails: 'BBBBBB',
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

    it('should delete a CurrencyServiceabilityFlag', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addCurrencyServiceabilityFlagToCollectionIfMissing', () => {
      it('should add a CurrencyServiceabilityFlag to an empty array', () => {
        const currencyServiceabilityFlag: ICurrencyServiceabilityFlag = { id: 123 };
        expectedResult = service.addCurrencyServiceabilityFlagToCollectionIfMissing([], currencyServiceabilityFlag);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(currencyServiceabilityFlag);
      });

      it('should not add a CurrencyServiceabilityFlag to an array that contains it', () => {
        const currencyServiceabilityFlag: ICurrencyServiceabilityFlag = { id: 123 };
        const currencyServiceabilityFlagCollection: ICurrencyServiceabilityFlag[] = [
          {
            ...currencyServiceabilityFlag,
          },
          { id: 456 },
        ];
        expectedResult = service.addCurrencyServiceabilityFlagToCollectionIfMissing(
          currencyServiceabilityFlagCollection,
          currencyServiceabilityFlag
        );
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CurrencyServiceabilityFlag to an array that doesn't contain it", () => {
        const currencyServiceabilityFlag: ICurrencyServiceabilityFlag = { id: 123 };
        const currencyServiceabilityFlagCollection: ICurrencyServiceabilityFlag[] = [{ id: 456 }];
        expectedResult = service.addCurrencyServiceabilityFlagToCollectionIfMissing(
          currencyServiceabilityFlagCollection,
          currencyServiceabilityFlag
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(currencyServiceabilityFlag);
      });

      it('should add only unique CurrencyServiceabilityFlag to an array', () => {
        const currencyServiceabilityFlagArray: ICurrencyServiceabilityFlag[] = [{ id: 123 }, { id: 456 }, { id: 3929 }];
        const currencyServiceabilityFlagCollection: ICurrencyServiceabilityFlag[] = [{ id: 123 }];
        expectedResult = service.addCurrencyServiceabilityFlagToCollectionIfMissing(
          currencyServiceabilityFlagCollection,
          ...currencyServiceabilityFlagArray
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const currencyServiceabilityFlag: ICurrencyServiceabilityFlag = { id: 123 };
        const currencyServiceabilityFlag2: ICurrencyServiceabilityFlag = { id: 456 };
        expectedResult = service.addCurrencyServiceabilityFlagToCollectionIfMissing(
          [],
          currencyServiceabilityFlag,
          currencyServiceabilityFlag2
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(currencyServiceabilityFlag);
        expect(expectedResult).toContain(currencyServiceabilityFlag2);
      });

      it('should accept null and undefined values', () => {
        const currencyServiceabilityFlag: ICurrencyServiceabilityFlag = { id: 123 };
        expectedResult = service.addCurrencyServiceabilityFlagToCollectionIfMissing([], null, currencyServiceabilityFlag, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(currencyServiceabilityFlag);
      });

      it('should return initial array if no CurrencyServiceabilityFlag is added', () => {
        const currencyServiceabilityFlagCollection: ICurrencyServiceabilityFlag[] = [{ id: 123 }];
        expectedResult = service.addCurrencyServiceabilityFlagToCollectionIfMissing(currencyServiceabilityFlagCollection, undefined, null);
        expect(expectedResult).toEqual(currencyServiceabilityFlagCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
