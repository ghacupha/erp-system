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

import { CurrencyAuthenticityFlags } from 'app/entities/enumerations/currency-authenticity-flags.model';
import { CurrencyAuthenticityTypes } from 'app/entities/enumerations/currency-authenticity-types.model';
import { ICurrencyAuthenticityFlag, CurrencyAuthenticityFlag } from '../currency-authenticity-flag.model';

import { CurrencyAuthenticityFlagService } from './currency-authenticity-flag.service';

describe('CurrencyAuthenticityFlag Service', () => {
  let service: CurrencyAuthenticityFlagService;
  let httpMock: HttpTestingController;
  let elemDefault: ICurrencyAuthenticityFlag;
  let expectedResult: ICurrencyAuthenticityFlag | ICurrencyAuthenticityFlag[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CurrencyAuthenticityFlagService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      currencyAuthenticityFlag: CurrencyAuthenticityFlags.Y,
      currencyAuthenticityType: CurrencyAuthenticityTypes.GENUINE,
      currencyAuthenticityTypeDetails: 'AAAAAAA',
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

    it('should create a CurrencyAuthenticityFlag', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new CurrencyAuthenticityFlag()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CurrencyAuthenticityFlag', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          currencyAuthenticityFlag: 'BBBBBB',
          currencyAuthenticityType: 'BBBBBB',
          currencyAuthenticityTypeDetails: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a CurrencyAuthenticityFlag', () => {
      const patchObject = Object.assign(
        {
          currencyAuthenticityFlag: 'BBBBBB',
          currencyAuthenticityType: 'BBBBBB',
        },
        new CurrencyAuthenticityFlag()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of CurrencyAuthenticityFlag', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          currencyAuthenticityFlag: 'BBBBBB',
          currencyAuthenticityType: 'BBBBBB',
          currencyAuthenticityTypeDetails: 'BBBBBB',
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

    it('should delete a CurrencyAuthenticityFlag', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addCurrencyAuthenticityFlagToCollectionIfMissing', () => {
      it('should add a CurrencyAuthenticityFlag to an empty array', () => {
        const currencyAuthenticityFlag: ICurrencyAuthenticityFlag = { id: 123 };
        expectedResult = service.addCurrencyAuthenticityFlagToCollectionIfMissing([], currencyAuthenticityFlag);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(currencyAuthenticityFlag);
      });

      it('should not add a CurrencyAuthenticityFlag to an array that contains it', () => {
        const currencyAuthenticityFlag: ICurrencyAuthenticityFlag = { id: 123 };
        const currencyAuthenticityFlagCollection: ICurrencyAuthenticityFlag[] = [
          {
            ...currencyAuthenticityFlag,
          },
          { id: 456 },
        ];
        expectedResult = service.addCurrencyAuthenticityFlagToCollectionIfMissing(
          currencyAuthenticityFlagCollection,
          currencyAuthenticityFlag
        );
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CurrencyAuthenticityFlag to an array that doesn't contain it", () => {
        const currencyAuthenticityFlag: ICurrencyAuthenticityFlag = { id: 123 };
        const currencyAuthenticityFlagCollection: ICurrencyAuthenticityFlag[] = [{ id: 456 }];
        expectedResult = service.addCurrencyAuthenticityFlagToCollectionIfMissing(
          currencyAuthenticityFlagCollection,
          currencyAuthenticityFlag
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(currencyAuthenticityFlag);
      });

      it('should add only unique CurrencyAuthenticityFlag to an array', () => {
        const currencyAuthenticityFlagArray: ICurrencyAuthenticityFlag[] = [{ id: 123 }, { id: 456 }, { id: 19711 }];
        const currencyAuthenticityFlagCollection: ICurrencyAuthenticityFlag[] = [{ id: 123 }];
        expectedResult = service.addCurrencyAuthenticityFlagToCollectionIfMissing(
          currencyAuthenticityFlagCollection,
          ...currencyAuthenticityFlagArray
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const currencyAuthenticityFlag: ICurrencyAuthenticityFlag = { id: 123 };
        const currencyAuthenticityFlag2: ICurrencyAuthenticityFlag = { id: 456 };
        expectedResult = service.addCurrencyAuthenticityFlagToCollectionIfMissing([], currencyAuthenticityFlag, currencyAuthenticityFlag2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(currencyAuthenticityFlag);
        expect(expectedResult).toContain(currencyAuthenticityFlag2);
      });

      it('should accept null and undefined values', () => {
        const currencyAuthenticityFlag: ICurrencyAuthenticityFlag = { id: 123 };
        expectedResult = service.addCurrencyAuthenticityFlagToCollectionIfMissing([], null, currencyAuthenticityFlag, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(currencyAuthenticityFlag);
      });

      it('should return initial array if no CurrencyAuthenticityFlag is added', () => {
        const currencyAuthenticityFlagCollection: ICurrencyAuthenticityFlag[] = [{ id: 123 }];
        expectedResult = service.addCurrencyAuthenticityFlagToCollectionIfMissing(currencyAuthenticityFlagCollection, undefined, null);
        expect(expectedResult).toEqual(currencyAuthenticityFlagCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
